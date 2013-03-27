package calzone;

import calzone.model.CalzoneFinishedBuild;
import calzone.model.FinishedBuildStatus;
import jetbrains.buildServer.BuildTypeDescriptor;
import jetbrains.buildServer.StatusDescriptor;
import jetbrains.buildServer.messages.Status;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SFinishedBuild;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FinishedBuildMakerTest {
    private FinishedBuildMaker finishedBuildMaker;

    @Before
    public void setUp() throws Exception {
        finishedBuildMaker = new FinishedBuildMaker(mock(SBuildServer.class), mock(TimeFormatter.class));
    }

    @Test
    public void buildNeverRun() throws Exception {
        SBuildType buildType = mock(SBuildType.class);
        when(buildType.getStatus()).thenReturn(Status.UNKNOWN);
        when(buildType.getLastChangesSuccessfullyFinished()).thenReturn(null);

        CalzoneFinishedBuild finishedBuild = finishedBuildMaker.lastFinishedBuild(buildType);

        assertThat(finishedBuild.getStatus(), is(FinishedBuildStatus.none));
    }

    @Test
    public void buildGreen() throws Exception {
        SBuildType buildType = mock(SBuildType.class);
        SFinishedBuild build = mock(SFinishedBuild.class);

        when(buildType.getStatus()).thenReturn(Status.NORMAL);
        when(buildType.getLastChangesFinished()).thenReturn(build);
        when(build.getBuildStatus()).thenReturn(Status.NORMAL);
        when(build.getStatusDescriptor()).thenReturn(new StatusDescriptor(Status.NORMAL, "goodness"));

        CalzoneFinishedBuild finishedBuild = finishedBuildMaker.lastFinishedBuild(buildType);

        assertThat(finishedBuild.getStatus(), is(FinishedBuildStatus.passed));
    }

    @Test
    public void buildFailedToCompile() throws Exception {
        SBuildType buildType = mock(SBuildType.class);
        SFinishedBuild build = mock(SFinishedBuild.class);

        when(buildType.getStatus()).thenReturn(Status.NORMAL);
        when(buildType.getLastChangesFinished()).thenReturn(build);
        when(build.getBuildStatus()).thenReturn(Status.NORMAL);
        when(build.getStatusDescriptor()).thenReturn(new StatusDescriptor(Status.FAILURE, "compilation failure"));

        CalzoneFinishedBuild finishedBuild = finishedBuildMaker.lastFinishedBuild(buildType);

        assertThat(finishedBuild.getStatus(), is(FinishedBuildStatus.compilationFailed));
    }
}
