package calzone;

import calzone.model.BuildInfo;
import calzone.model.CalzoneFinishedBuild;
import calzone.model.CalzoneRunningBuild;
import jetbrains.buildServer.responsibility.ResponsibilityEntry;
import jetbrains.buildServer.serverSide.SBuildType;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BuildFormatterTest {

    private BuildFormatter buildFormatter;
    private SBuildType buildType;
    private CalzoneRunningBuild runningBuild;
    private CalzoneFinishedBuild finishedBuild;
    private String theBuildName;
    private ResponsibilityEntry responsibilityEntry;

    @Before
    public void setUp() throws Exception {
        theBuildName = "the build name";
        runningBuild = mock(CalzoneRunningBuild.class);
        finishedBuild = mock(CalzoneFinishedBuild.class);

        responsibilityEntry = mock(ResponsibilityEntry.class);
        buildType = mock(SBuildType.class);
        when(buildType.getName()).thenReturn(theBuildName);
        when(buildType.getResponsibilityInfo()).thenReturn(this.responsibilityEntry);

        RunningBuildMaker runningBuildMaker = mock(RunningBuildMaker.class);
        when(runningBuildMaker.runningBuild(buildType)).thenReturn(runningBuild);

        FinishedBuildMaker finishedBuildMaker = mock(FinishedBuildMaker.class);
        when(finishedBuildMaker.lastFinishedBuild(buildType)).thenReturn(finishedBuild);

        buildFormatter = new BuildFormatter(finishedBuildMaker, runningBuildMaker);
    }

    @Test
    public void formatsBuildWithNoResponsibility() throws Exception {
        when(this.responsibilityEntry.getState()).thenReturn(ResponsibilityEntry.State.NONE);

        BuildInfo buildInfo = buildFormatter.format(buildType);

        assertThat(buildInfo.getBuildName(), is(theBuildName));
        assertThat(buildInfo.getLastFinished(), is(finishedBuild));
        assertThat(buildInfo.getRunningBuild(), is(runningBuild));
        assertThat(buildInfo.isResponsibilityAssigned(), is(false));
    }

    @Test
    public void formatsBuildWithResponsibility() throws Exception {
        when(this.responsibilityEntry.getState()).thenReturn(ResponsibilityEntry.State.TAKEN);

        BuildInfo buildInfo = buildFormatter.format(buildType);

        assertThat(buildInfo.getBuildName(), is(theBuildName));
        assertThat(buildInfo.getLastFinished(), is(finishedBuild));
        assertThat(buildInfo.getRunningBuild(), is(runningBuild));
        assertThat(buildInfo.isResponsibilityAssigned(), is(true));
    }

    @Test
    public void formatsBuildWithGivenUpResponsibility() throws Exception {
        when(this.responsibilityEntry.getState()).thenReturn(ResponsibilityEntry.State.GIVEN_UP);

        BuildInfo buildInfo = buildFormatter.format(buildType);

        assertThat(buildInfo.getBuildName(), is(theBuildName));
        assertThat(buildInfo.getLastFinished(), is(finishedBuild));
        assertThat(buildInfo.getRunningBuild(), is(runningBuild));
        assertThat(buildInfo.isResponsibilityAssigned(), is(false));
    }

    @Test
    public void formatsBuildWithFixedResponsibility() throws Exception {
        when(this.responsibilityEntry.getState()).thenReturn(ResponsibilityEntry.State.FIXED);

        BuildInfo buildInfo = buildFormatter.format(buildType);

        assertThat(buildInfo.getBuildName(), is(theBuildName));
        assertThat(buildInfo.getLastFinished(), is(finishedBuild));
        assertThat(buildInfo.getRunningBuild(), is(runningBuild));
        assertThat(buildInfo.isResponsibilityAssigned(), is(false));
    }
}
