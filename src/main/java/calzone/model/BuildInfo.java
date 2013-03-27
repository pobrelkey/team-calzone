package calzone.model;

public class BuildInfo implements Comparable<BuildInfo> {
    private final String buildName;
    private final CalzoneFinishedBuild lastFinished;
    private final CalzoneRunningBuild runningBuild;
    private final boolean responsibilityAssigned;

    public BuildInfo(String buildName, CalzoneFinishedBuild finishedBuild, CalzoneRunningBuild runningBuild, boolean responsibilityAssigned) {
        this.buildName = buildName;
        this.lastFinished = finishedBuild;
        this.runningBuild = runningBuild;
        this.responsibilityAssigned = responsibilityAssigned;
    }

    public String getBuildName() {
        return buildName;
    }

    public Boolean isGreen() {
        return lastFinished.getStatus().isGreen() && runningBuild.getStatus().isGreen();
    }

    public boolean isCompileFailure() {
        return lastFinished.getStatus() == FinishedBuildStatus.compilationFailed;
    }

    public String getTimeSinceLastGoodBuild() {
        return lastFinished.getTimeSinceFirstFailure();
    }

    public boolean isActive() {
        return runningBuild.getStatus() != RunningBuildStatus.notRunning;
    }

    public boolean isFailing() {
        return runningBuild.getStatus() == RunningBuildStatus.failing;
    }

    public String getTimeRemaining() {
        return runningBuild.getTimeRemaining();
    }

    public boolean isResponsibilityAssigned() {
        return responsibilityAssigned;
    }

    public CalzoneFinishedBuild getLastFinished() {
        return lastFinished;
    }

    public CalzoneRunningBuild getRunningBuild() {
        return runningBuild;
    }

    @Override
    public int compareTo(BuildInfo buildInfo) {
        // failing builds always sort first
        if (this.isGreen() != buildInfo.isGreen()) {
            return this.isGreen() ? 1 : -1;
        }

        // ...then compile failures sort first
        if (this.isCompileFailure() != buildInfo.isCompileFailure()) {
            return this.isCompileFailure() ? -1 : 1;
        }

        // ...then sort by most recently broken first
        if (this.getTimeSinceLastGoodBuild() != null && buildInfo.getTimeSinceLastGoodBuild() != null) {
            return getTimeSinceLastGoodBuild().compareTo(buildInfo.getTimeSinceLastGoodBuild());
        } else if (this.getTimeSinceLastGoodBuild() == null && buildInfo.getTimeSinceLastGoodBuild() != null) {
            return 1;
        } else if (this.getTimeSinceLastGoodBuild() != null && buildInfo.getTimeSinceLastGoodBuild() == null) {
            return -1;
        }

        // ...in pathological cases, sort by build name ascending
        return this.buildName.toLowerCase().compareTo(buildInfo.getBuildName().toLowerCase());
    }
}
