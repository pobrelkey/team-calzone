package calzone;

import jetbrains.buildServer.Build;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.messages.Status;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SFinishedBuild;
import jetbrains.buildServer.serverSide.SProject;
import jetbrains.buildServer.serverSide.SRunningBuild;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;


public class ResultsPageController extends BaseController {
    private final Calzone calzone;
    private final SBuildServer server;
    static private final String[] FAILURE_FONT_SIZES = new String[]{"18pt", "24pt", "30pt", "36pt", "42pt", "48pt", "60pt", "72pt", "84pt", "96pt", "112pt", "128pt", "144pt", "180pt", "216pt"};
    static private final int[] REFRESH_FREQUENCIES = new int[]{5, 10, 15, 20, 30, 60, 120, 300};
    private static final ValueWithLabel[] DISSOLVE_RATES = new ValueWithLabel[]{
            new ValueWithLabel(1.0,  "direct transition"),
            new ValueWithLabel(0.25, "fast (.4s) dissolve"),
            new ValueWithLabel(0.1,  "medium (1s) dissolve"),
            new ValueWithLabel(0.02, "slow (5s) dissolve")
    };

    public ResultsPageController(SBuildServer server, Calzone calzone) {
        super(server);
        this.server = server;
        this.calzone = calzone;
    }

    private void getCurrentRunStatus(SBuildType sBuildType, TreeSet<BuildInfo> builds, boolean pendingInItalics, boolean runningInItalics) {
        Boolean goodBuild = null;
        boolean failingBuild = false;
        String timeRemaining = null;
        boolean active = false;

        List<SRunningBuild> runningBuilds = sBuildType.getRunningBuilds();
        boolean inProgress = !runningBuilds.isEmpty();
        if (inProgress) {
            active |= runningInItalics;
            Status worstRunningBuildStatus = Status.UNKNOWN;
            for (SRunningBuild runningBuild : runningBuilds) {
                Status status = runningBuild.getStatusDescriptor().getStatus();
                if (status != null && status.above(worstRunningBuildStatus)) {
                    worstRunningBuildStatus = status;
                    timeRemaining = formatMinutesAndSeconds(runningBuild.getEstimationForTimeLeft());
                }
            }
            failingBuild = worstRunningBuildStatus.isFailed();
            if (failingBuild) {
                goodBuild = false;
            }
        }

        active |= (pendingInItalics && sBuildType.getNumberQueued() > 0);

        Status latestCompletedStatus = null;
        Boolean latestCompletedIsCompileFailure = false;
        SBuild lastFinished = sBuildType.getLastFinished();
        if (lastFinished != null && lastFinished.getStatusDescriptor().getStatus().equals(Status.UNKNOWN)) {
            // need to dig deeper for last build status - most recent build probably cancelled
            Date latestCompletedStatusDate = null;
            List<SFinishedBuild> buildHistory = sBuildType.getHistory(false);
            for (SFinishedBuild finishedBuild : buildHistory) {
                Status status = finishedBuild.getStatusDescriptor().getStatus();
                if (!status.equals(Status.UNKNOWN) && (latestCompletedStatusDate == null || finishedBuild.getStartDate().compareTo(latestCompletedStatusDate) > 0)) {
                    lastFinished = finishedBuild;
                    latestCompletedIsCompileFailure = finishedBuild.getStatusDescriptor().getText().toLowerCase().contains("compilation fail");
                    latestCompletedStatusDate = finishedBuild.getStartDate();
                }
            }
        }
        if (lastFinished != null) {
            latestCompletedStatus = lastFinished.getBuildStatus();
            latestCompletedIsCompileFailure = lastFinished.getStatusDescriptor().getText().toLowerCase().contains("compilation fail");
        }

        Date lastGoodBuildDate = null;
        Build lastGoodBuild = sBuildType.getLastChangesSuccessfullyFinished();
        if (lastGoodBuild != null) {
            lastGoodBuildDate = lastGoodBuild.getStartDate();
        }

        if (latestCompletedStatus == null || latestCompletedStatus.equals(Status.UNKNOWN)) {
            // don't know anything about this build yet (hasn't run?)
        } else {
            goodBuild = latestCompletedStatus.isSuccessful() && (goodBuild == null);
        }

        if (goodBuild != null) {
            builds.add(new BuildInfo(sBuildType.getName(), goodBuild, latestCompletedIsCompileFailure, lastGoodBuildDate, active, failingBuild, timeRemaining));
        }
    }

    private String formatMinutesAndSeconds(long timeInSeconds) {
        StringBuilder result = new StringBuilder();
        if (timeInSeconds < 0) {
            result.append('-');
        }
        result.append(timeInSeconds / 60);
        result.append(':');
        long seconds = timeInSeconds % 60;
        if (seconds < 10) {
            result.append('0');
        }
        result.append(seconds);
        return result.toString();
    }

    protected ModelAndView doHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] buildsToDisplay = ServletRequestUtils.getStringParameters(request, "buildsToDisplay");
        HashSet<String> buildsToDisplaySet = asSet(buildsToDisplay);
        String[] projectsToDisplay = ServletRequestUtils.getStringParameters(request, "projectsToDisplay");
        HashSet<String> projectsToDisplaySet = asSet(projectsToDisplay);
        boolean pendingInItalics = parameterAsBoolean(request, "pendingInItalics");
        boolean runningInItalics = parameterAsBoolean(request, "runningInItalics");

        TreeSet<String> buildNames = new TreeSet<String>();
        ArrayList<String> projectNames = new ArrayList<String>();
        TreeSet<ProjectInfo> projects = new TreeSet<ProjectInfo>();
        for (SProject sProject : server.getProjectManager().getProjects()) {
            projectNames.add(sProject.getName());
            TreeSet<BuildInfo> builds = new TreeSet<BuildInfo>();
            for (SBuildType sBuildType : sProject.getBuildTypes()) {
                buildNames.add(sBuildType.getName());
                if (isBuildInWhichWeAreInterested(sBuildType, buildsToDisplaySet, projectsToDisplaySet)) {
                    getCurrentRunStatus(sBuildType, builds, pendingInItalics, runningInItalics);
                }
            }
            if (!builds.isEmpty()) {
                projects.add(new ProjectInfo(sProject.getName(), builds));
            }
        }

        String failFontSize = ServletRequestUtils.getStringParameter(request, "failFontSize", "72pt");
        boolean dontShowGreenBuilds = parameterAsBoolean(request, "dontShowGreenBuilds");
        boolean runTogether = parameterAsBoolean(request, "runTogether");
        boolean blink = parameterAsBoolean(request, "blink");
        boolean showDividers = parameterAsBoolean(request, "showDividers");
        boolean showTimeRemaining = parameterAsBoolean(request, "showTimeRemaining");
        int frequency = ServletRequestUtils.getIntParameter(request, "frequency", 5);
        double dissolveRate = ServletRequestUtils.getDoubleParameter(request, "dissolveRate", 1);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("results", projects);
        model.put("buildNames", buildNames);
        model.put("projectNames", projectNames);
        model.put("dontShowGreenBuilds", dontShowGreenBuilds);
        model.put("runTogether", runTogether);
        model.put("blink", blink);
        model.put("showDividers", showDividers);
        model.put("now", new Date());
        model.put("failFontSize", failFontSize);
        model.put("failFontSizes", FAILURE_FONT_SIZES);
        model.put("showTimeRemaining", showTimeRemaining);
        model.put("pendingInItalics", pendingInItalics);
        model.put("runningInItalics", runningInItalics);
        model.put("frequency", frequency);
        model.put("frequencies", REFRESH_FREQUENCIES);
        model.put("dissolveRate", dissolveRate);
        model.put("dissolveRates", DISSOLVE_RATES);
        model.put("formModel", new ResultsPageFormModel(buildsToDisplay, projectsToDisplay, failFontSize, dontShowGreenBuilds, runTogether, blink, showDividers, showTimeRemaining, pendingInItalics, runningInItalics, frequency, dissolveRate));

        boolean isFragment = parameterAsBoolean(request, "fragment");
        if (isFragment) {
            return new ModelAndView(calzone.resourcePath("results.jsp"), model);
        } else {
            return new ModelAndView(calzone.resourcePath("calzone.jsp"), model);
        }
    }

    private boolean parameterAsBoolean(HttpServletRequest request, String parameterName) throws ServletRequestBindingException {
        String paramString = ServletRequestUtils.getStringParameter(request, parameterName);
        return paramString != null && paramString.length() > 0;
    }

    private boolean isBuildInWhichWeAreInterested(SBuildType sBuildType, HashSet<String> buildsToDisplaySet, HashSet<String> projectsToDisplaySet) {
        return (buildsToDisplaySet.isEmpty() || buildsToDisplaySet.contains(sBuildType.getName().toLowerCase())) &&
                (projectsToDisplaySet.isEmpty() || projectsToDisplaySet.contains(sBuildType.getProjectName().toLowerCase()));
    }

    private HashSet<String> asSet(String[] buildsToDisplay) {
        HashSet<String> buildsToDisplaySet = new HashSet<String>();
        if (buildsToDisplay != null) {
            for (String s : buildsToDisplay) {
                buildsToDisplaySet.add(s.toLowerCase());
            }
        }
        return buildsToDisplaySet;
    }

    private static class ValueWithLabel {
        private Object value;
        private String label;

        private ValueWithLabel(Object value, String label) {
            this.label = label;
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public Object getValue() {
            return value;
        }
    }

    private static class ResultsPageFormModel {
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
        private final boolean pendingInItalics;
        private final boolean runningInItalics;

        public ResultsPageFormModel(String[] buildsToDisplay,
                                    String[] projectsToDisplay,
                                    String failFontSize,
                                    boolean dontShowGreenBuilds,
                                    boolean runTogether,
                                    boolean blink,
                                    boolean showDividers,
                                    boolean showTimeRemaining,
                                    boolean pendingInItalics,
                                    boolean runningInItalics, int frequency, double dissolveRate) {
            this.dontShowGreenBuilds = dontShowGreenBuilds;
            this.buildsToDisplay = buildsToDisplay;
            this.projectsToDisplay = projectsToDisplay;
            this.failFontSize = failFontSize;
            this.runTogether = runTogether;
            this.blink = blink;
            this.showDividers = showDividers;
            this.showTimeRemaining = showTimeRemaining;
            this.pendingInItalics = pendingInItalics;
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

        public boolean isPendingInItalics() {
            return pendingInItalics;
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
}
