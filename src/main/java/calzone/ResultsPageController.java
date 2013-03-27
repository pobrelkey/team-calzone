package calzone;

import calzone.model.BuildInfo;
import calzone.model.ProjectInfo;
import calzone.model.ResultsPageFormModel;
import calzone.model.ValueWithLabel;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.SProject;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


public class ResultsPageController extends BaseController {
    private static final String[] FAILURE_FONT_SIZES = new String[]{"18pt", "24pt", "30pt", "36pt", "42pt", "48pt", "60pt", "72pt", "84pt", "96pt", "112pt", "128pt", "144pt", "180pt", "216pt"};
    private static final int[] REFRESH_FREQUENCIES = new int[]{5, 10, 15, 20, 30, 60, 120, 300};
    private static final ValueWithLabel[] DISSOLVE_RATES = new ValueWithLabel[]{
            new ValueWithLabel("direct transition", 1.0),
            new ValueWithLabel("fast (.4s) dissolve", 0.25),
            new ValueWithLabel("medium (1s) dissolve", 0.1),
            new ValueWithLabel("slow (5s) dissolve", 0.02)
    };

    private final SBuildServer server;
    private final PluginDescriptor pluginDescriptor;
    private final BuildFormatter buildFormatter;

    public ResultsPageController(SBuildServer server, PluginDescriptor pluginDescriptor) {
        super(server);
        this.server = server;
        this.pluginDescriptor = pluginDescriptor;
        final TimeFormatter timeFormatter = new TimeFormatter();
        buildFormatter = new BuildFormatter(new FinishedBuildMaker(server, timeFormatter), new RunningBuildMaker(timeFormatter));
    }

    @Override
    protected ModelAndView doHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] buildsToDisplay = ServletRequestUtils.getStringParameters(request, "buildsToDisplay");
        HashSet<String> buildsToDisplaySet = asSet(buildsToDisplay);
        String[] projectsToDisplay = ServletRequestUtils.getStringParameters(request, "projectsToDisplay");
        HashSet<String> projectsToDisplaySet = asSet(projectsToDisplay);

        TreeSet<String> buildNames = new TreeSet<String>();
        ArrayList<String> projectNames = new ArrayList<String>();
        TreeSet<ProjectInfo> projects = new TreeSet<ProjectInfo>();

        for (SProject sProject : server.getProjectManager().getProjects()) {
            projectNames.add(sProject.getName());
            TreeSet<BuildInfo> builds = new TreeSet<BuildInfo>();
            for (SBuildType sBuildType : sProject.getBuildTypes()) {
                buildNames.add(sBuildType.getName());
                if (isBuildInWhichWeAreInterested(sBuildType, buildsToDisplaySet, projectsToDisplaySet)) {
                    builds.add(buildFormatter.format(sBuildType));
                }
            }
            if (!builds.isEmpty()) {
                projects.add(new ProjectInfo(sProject.getName(), builds));
            }
        }

        String failFontSize = ServletRequestUtils.getStringParameter(request, "failFontSize", "72pt");
        boolean runningInItalics = parameterAsBoolean(request, "runningInItalics");
        boolean dontShowGreenBuilds = parameterAsBoolean(request, "dontShowGreenBuilds");
        boolean runTogether = parameterAsBoolean(request, "runTogether");
        boolean blink = parameterAsBoolean(request, "blink");
        boolean showDividers = parameterAsBoolean(request, "showDividers");
        boolean showTimeRemaining = parameterAsBoolean(request, "showTimeRemaining");
        boolean showTimeSinceFirstFail = parameterAsBoolean(request, "showTimeSinceFirstFail");
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
        model.put("showTimeSinceFirstFail", showTimeSinceFirstFail);
        model.put("runningInItalics", runningInItalics);
        model.put("frequency", frequency);
        model.put("frequencies", REFRESH_FREQUENCIES);
        model.put("dissolveRate", dissolveRate);
        model.put("dissolveRates", DISSOLVE_RATES);
        model.put("formModel", new ResultsPageFormModel(buildsToDisplay, projectsToDisplay, failFontSize, dontShowGreenBuilds, runTogether, blink, showDividers, showTimeRemaining, showTimeSinceFirstFail, runningInItalics, frequency, dissolveRate));

        boolean isFragment = parameterAsBoolean(request, "fragment");
        if (isFragment) {
            return new ModelAndView(pluginDescriptor.getPluginResourcesPath("results.jsp"), model);
        } else {
            return new ModelAndView(pluginDescriptor.getPluginResourcesPath("calzone.jsp"), model);
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
}