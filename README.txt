INSTALLING CALZONE

Copy calzone.jar to webapps/ROOT/WEB-INF/lib/ in your TeamCity server instance,
then bounce TeamCity.  Calzone should then be visible at:

    http://YOUR_TEAMCITY_SERVER:8111/calzone/index.html


BUILDING CALZONE

Build Calzone by opening the IntelliJ project and building that; the Calzone
jar file (calzone.jar) should then appear in the project root.

Note that to build Calzone you'll need to copy the TeamCity 5 API jar's to
the lib directory.  These jars aren't in source control because they're not
freely (as in speech) redistributable.

	devPackage/common-api.jar
	devPackage/runtime-util.jar
	devPackage/server-api.jar

