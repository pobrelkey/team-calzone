package calzone.model;

import jetbrains.buildServer.messages.Status;

import static java.util.Arrays.asList;

public enum RunningBuildStatus {
    notRunning, passing, failing;

    public static RunningBuildStatus from(Status status) {
        if (asList(Status.FAILURE, Status.ERROR).contains(status)) {
            return failing;
        } else if (Status.NORMAL.equals(status)) {
            return passing;
        } else {
            return notRunning;
        }
    }

    public boolean isGreen() {
        return asList(notRunning, passing).contains(this);
    }
}