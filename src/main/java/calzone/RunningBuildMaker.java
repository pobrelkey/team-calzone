package calzone;

import calzone.model.CalzoneRunningBuild;
import calzone.model.RunningBuildStatus;
import jetbrains.buildServer.messages.Status;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SRunningBuild;

public class RunningBuildMaker {
    private final TimeFormatter timeFormatter;

    public RunningBuildMaker(TimeFormatter timeFormatter) {
        this.timeFormatter = timeFormatter;
    }

    public CalzoneRunningBuild runningBuild(SBuildType sBuildType) {
        SRunningBuild worstRunningBuild = null;
        Status worstRunningBuildStatus = Status.UNKNOWN;
        if (thereIsARunningBuild(sBuildType)) {
            for (SRunningBuild runningBuild : sBuildType.getRunningBuilds()) {
                Status status = runningBuild.getBuildStatus();
                if (status != null && status.above(worstRunningBuildStatus)) {
                    worstRunningBuild = runningBuild;
                    worstRunningBuildStatus = status;
                }
            }
        }

        return new CalzoneRunningBuild(RunningBuildStatus.from(worstRunningBuildStatus), timeRemaining(worstRunningBuild));
    }

    private boolean thereIsARunningBuild(SBuildType sBuildType) {
        return !sBuildType.getRunningBuilds().isEmpty();
    }

    private String timeRemaining(SRunningBuild runningBuild) {
        return runningBuild == null ? "" : timeFormatter.formatMinutesAndSeconds(runningBuild.getEstimationForTimeLeft());
    }
}
