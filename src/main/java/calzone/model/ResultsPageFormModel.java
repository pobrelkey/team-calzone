package calzone.model;

public class ResultsPageFormModel {
    private final String[] buildsToDisplay;
    private final String[] projectsToDisplay;
    private final String failFontSize;
    private final int frequency;
    private final double dissolveRate;
    private final boolean dontShowGreenBuilds;
    private final boolean runTogether;
    private final boolean blink;
    private final boolean showDividers;
    private final boolean showTimeRemaining;
    private final boolean showTimeSinceFirstFail;
    private final boolean runningInItalics;

    public ResultsPageFormModel(String[] buildsToDisplay,
                                String[] projectsToDisplay,
                                String failFontSize,
                                boolean dontShowGreenBuilds,
                                boolean runTogether,
                                boolean blink,
                                boolean showDividers,
                                boolean showTimeRemaining,
                                boolean showTimeSinceFirstFail,
                                boolean runningInItalics,
                                int frequency,
                                double dissolveRate) {
        this.dontShowGreenBuilds = dontShowGreenBuilds;
        this.buildsToDisplay = buildsToDisplay;
        this.projectsToDisplay = projectsToDisplay;
        this.failFontSize = failFontSize;
        this.runTogether = runTogether;
        this.blink = blink;
        this.showDividers = showDividers;
        this.showTimeRemaining = showTimeRemaining;
        this.showTimeSinceFirstFail = showTimeSinceFirstFail;
        this.runningInItalics = runningInItalics;
        this.frequency = frequency;
        this.dissolveRate = dissolveRate;
    }

    public String[] getBuildsToDisplay() {
        return buildsToDisplay;
    }

    public String[] getProjectsToDisplay() {
        return projectsToDisplay;
    }

    public String getFailFontSize() {
        return failFontSize;
    }

    public boolean isDontShowGreenBuilds() {
        return dontShowGreenBuilds;
    }

    public boolean isRunTogether() {
        return runTogether;
    }

    public boolean isBlink() {
        return blink;
    }

    public boolean isShowDividers() {
        return showDividers;
    }

    public boolean isShowTimeRemaining() {
        return showTimeRemaining;
    }

    public boolean isShowTimeSinceFirstFail() {
        return showTimeSinceFirstFail;
    }

    public boolean isRunningInItalics() {
        return runningInItalics;
    }

    public int getFrequency() {
        return frequency;
    }

    public double getDissolveRate() {
        return dissolveRate;
    }
}
