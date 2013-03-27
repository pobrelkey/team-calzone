package calzone.model;

public class CalzoneFinishedBuild {
    private final FinishedBuildStatus status;
    private final String timeSinceFirstFailure;

    public CalzoneFinishedBuild(FinishedBuildStatus status, String timeSinceFirstFailure) {
        this.status = status;
        this.timeSinceFirstFailure = timeSinceFirstFailure;
    }

    public FinishedBuildStatus getStatus() {
        return status;
    }

    public String getTimeSinceFirstFailure() {
        return timeSinceFirstFailure;
    }
}
