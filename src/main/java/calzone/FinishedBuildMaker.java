package calzone;

import calzone.model.CalzoneFinishedBuild;
import calzone.model.FinishedBuildStatus;
import jetbrains.buildServer.messages.Status;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SFinishedBuild;

import java.util.Date;
import java.util.List;

public class FinishedBuildMaker {
    private final SBuildServer buildServer;
    private final TimeFormatter timeFormatter;

    public FinishedBuildMaker(SBuildServer buildServer, TimeFormatter timeFormatter) {
        this.buildServer = buildServer;
        this.timeFormatter = timeFormatter;
    }

    public CalzoneFinishedBuild lastFinishedBuild(SBuildType sBuildType) {
        SBuild lastFinished = sBuildType.getLastChangesFinished();
        if (lastFinished == null) {
            return new CalzoneFinishedBuild(FinishedBuildStatus.none, formattedTimeSinceFirstFailure(sBuildType));
        }

        if (lastFinished.getBuildStatus().equals(Status.UNKNOWN)) {
            // need to dig deeper for last build status - most recent build probably cancelled
            Date latestCompletedStatusDate = null;
            List<SFinishedBuild> buildHistory = sBuildType.getHistory(null, false, false);
            for (SFinishedBuild finishedBuild : buildHistory) {
                Status status = finishedBuild.getBuildStatus();
                if (!status.equals(Status.UNKNOWN) && (latestCompletedStatusDate == null || finishedBuild.getStartDate().compareTo(latestCompletedStatusDate) > 0)) {
                    lastFinished = finishedBuild;
                    latestCompletedStatusDate = finishedBuild.getStartDate();
                }
            }
        }
        return new CalzoneFinishedBuild(FinishedBuildStatus.from(lastFinished.getStatusDescriptor()), formattedTimeSinceFirstFailure(sBuildType));
    }

    private String formattedTimeSinceFirstFailure(SBuildType sBuildType) {
        if (!sBuildType.getStatus().isFailed()) {
            return null;
        }

        SBuild lastGoodBuild = sBuildType.getLastChangesSuccessfullyFinished();

        if (lastGoodBuild == null) {
            return null;
        }

        List<SBuild> entriesSince = buildServer.getEntriesSince(lastGoodBuild, sBuildType);
        if (entriesSince.isEmpty()) {
            return null;
        }

        return timeFormatter.formatDateSince(entriesSince.get(entriesSince.size() - 2).getStartDate());
    }
}