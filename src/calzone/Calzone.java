package calzone;

import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import jetbrains.buildServer.web.openapi.WebResourcesManager;

public class Calzone {
    private static final String PLUGIN_NAME = "calzone";

    private WebResourcesManager webResourcesManager;

    public Calzone(SBuildServer server,
				  WebControllerManager webControllerManager,
				  WebResourcesManager webResourcesManager)
	{
		this.webResourcesManager = webResourcesManager;

        webResourcesManager.addPluginResources(PLUGIN_NAME, PLUGIN_NAME + ".jar");

        // http://localhost:8111/calzone/anything.html
        webControllerManager.registerController(
			"/calzone/*",
			new ResultsPageController(server, this));
    }

    public String resourcePath(String resourceName) {
        String resourcePath = webResourcesManager.resourcePath(PLUGIN_NAME, resourceName);
        return resourcePath;
    }

}
