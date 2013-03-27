package calzone.model;

import jetbrains.buildServer.StatusDescriptor;
import jetbrains.buildServer.messages.Status;

import static java.util.Arrays.asList;

public enum FinishedBuildStatus {
    none, passed, failed, compilationFailed;

    public static FinishedBuildStatus from(StatusDescriptor descriptor) {
        if (asList(Status.ERROR, Status.FAILURE, Status.WARNING).contains(descriptor.getStatus())) {
            if (descriptor.getText().toLowerCase().contains("compilation fail")) {
                return compilationFailed;
            }
            return failed;
        } else if (Status.NORMAL.equals(descriptor.getStatus())) {
            return passed;
        } else {
            return none;
        }
    }


    public boolean isGreen() {
        return asList(none,passed).contains(this);
    }
}