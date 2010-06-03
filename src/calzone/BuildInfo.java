package calzone;

import java.util.Date;

public class BuildInfo implements Comparable<BuildInfo> {
    private final String buildName;
    private final Boolean green;
    private final boolean compileFailure;
    private final Date lastGoodBuildDate;
    private final boolean active;
    private final boolean failing;
    private final String timeRemaining;

    public BuildInfo(String buildName, boolean green, boolean compileFailure, Date lastGoodBuildDate, boolean active, boolean failing, String timeRemaining) {
        this.buildName = buildName;
        this.green = green;
        this.compileFailure = compileFailure;
        this.lastGoodBuildDate = lastGoodBuildDate;
        this.active = active;
        this.failing = failing;
        this.timeRemaining = timeRemaining;
    }

    public String getBuildName() {
        return buildName;
    }

    public Boolean isGreen() {
        return green;
    }

    public boolean isCompileFailure() {
        return compileFailure;
    }

    public Date getLastGoodBuildDate() {
        return lastGoodBuildDate;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isFailing() {
        return failing;
    }

    public String getTimeRemaining() {
        return timeRemaining;
    }

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
        if (this.getLastGoodBuildDate() != null && buildInfo.getLastGoodBuildDate() != null) {
            return getLastGoodBuildDate().compareTo(buildInfo.getLastGoodBuildDate());
        } else if (this.getLastGoodBuildDate() == null && buildInfo.getLastGoodBuildDate() != null) {
            return 1;
        } else if (this.getLastGoodBuildDate() != null && buildInfo.getLastGoodBuildDate() == null) {
            return -1;
        }

        // ...in pathological cases, sort by build name ascending
        return this.buildName.toLowerCase().compareTo(buildInfo.getBuildName().toLowerCase());
    }
}
