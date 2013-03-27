package calzone;

import calzone.model.CalzoneRunningBuild;
import calzone.model.RunningBuildStatus;
import jetbrains.buildServer.messages.Status;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SRunningBuild;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RunningBuildMakerTest {

    private RunningBuildMaker buildMaker;
    private SRunningBuild greenBuild;

    @Before
    public void setUp() throws Exception {
        TimeFormatter timeFormatter = mock(TimeFormatter.class);
        buildMaker = new RunningBuildMaker(timeFormatter);
        greenBuild = greenRunningBuild();
    }

    @Test
    public void notRunningBuild() throws Exception {
        SBuildType buildType = mock(SBuildType.class);
        CalzoneRunningBuild runningBuild = buildMaker.runningBuild(buildType);

        assertThat(runningBuild.getStatus(), is(RunningBuildStatus.notRunning));
    }

    @Test
    public void passingBuild() throws Exception {
        SBuildType buildType = mock(SBuildType.class);
        when(buildType.getRunningBuilds()).thenReturn(singletonList(greenBuild));

        CalzoneRunningBuild runningBuild = buildMaker.runningBuild(buildType);

        assertThat(runningBuild.getStatus(), is(RunningBuildStatus.passing));
    }

    @Test
    public void failingBuild() throws Exception {
        SBuildType buildType = mock(SBuildType.class);
        SRunningBuild redBuild = redRunningBuild();
        when(buildType.getRunningBuilds()).thenReturn(singletonList(redBuild));

        CalzoneRunningBuild runningBuild = buildMaker.runningBuild(buildType);

        assertThat(runningBuild.getStatus(), is(RunningBuildStatus.failing));
    }

    private SRunningBuild redRunningBuild() {
        SRunningBuild redRunningBuild = mock(SRunningBuild.class);
        when(redRunningBuild.getBuildStatus()).thenReturn(Status.FAILURE);
        return redRunningBuild;
    }

    private SRunningBuild greenRunningBuild() {
        SRunningBuild greenRunningBuild = mock(SRunningBuild.class);
        when(greenRunningBuild.getBuildStatus()).thenReturn(Status.NORMAL);
        return greenRunningBuild;
    }
}
