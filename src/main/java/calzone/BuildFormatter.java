package calzone;

import calzone.model.BuildInfo;
import calzone.model.CalzoneFinishedBuild;
import calzone.model.CalzoneRunningBuild;
import jetbrains.buildServer.serverSide.SBuildType;

public class BuildFormatter {
    private FinishedBuildMaker finishedBuildMaker;
    private RunningBuildMaker runningBuildMaker;

    public BuildFormatter(FinishedBuildMaker finishedBuildMaker, RunningBuildMaker runningBuildMaker) {
        this.finishedBuildMaker = finishedBuildMaker;
        this.runningBuildMaker = runningBuildMaker;
    }

    public BuildInfo format(SBuildType sBuildType) {
        CalzoneRunningBuild significantRunningBuild = runningBuildMaker.runningBuild(sBuildType);
        CalzoneFinishedBuild lastFinished = finishedBuildMaker.lastFinishedBuild(sBuildType);

        boolean responsibilityAssigned = sBuildType.getResponsibilityInfo().getState().isActive();

        return new BuildInfo(sBuildType.getName(),lastFinished, significantRunningBuild, responsibilityAssigned);
    }
}