package calzone.model;

public class CalzoneRunningBuild {

    private final RunningBuildStatus status;
    private final String timeRemaining;

    public CalzoneRunningBuild(RunningBuildStatus status, String timeRemaining) {
        this.status = status;
        this.timeRemaining = timeRemaining;
    }

    public RunningBuildStatus getStatus() {
        return status;
    }

    public String getTimeRemaining() {
        return timeRemaining;
    }
}