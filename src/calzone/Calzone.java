package calzone;

import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.WebControllerManager;

public class Calzone {
    public Calzone(SBuildServer server, WebControllerManager webControllerManager) {

        // http://localhost:8111/calzone/anything.html
        webControllerManager.registerController(
                "/calzone/*",
                new ResultsPageController(server, this));
    }
}