BUILDING CALZONE

Build Calzone by opening the IntelliJ project and building that.  The Calzone
jar file (calzone.jar) should then appear under plugins/server; run make-zipfile.sh
to build the plugin zip file.

Note that to build Calzone you'll need to copy the TeamCity 5 API jar's to
the lib directory.  These jars aren't in source control because they're not
freely (as in speech) redistributable.

	devPackage/common-api.jar
	devPackage/runtime-util.jar
	devPackage/server-api.jar


INSTALLING CALZONE

Try one of the following two methods depending on your TeamCity environment:

Method 1: Copy calzone.zip to the plugins directory under your TeamCity
data directory, then bounce TeamCity.

Method 2: Copy calzone.jar to webapps/ROOT/WEB-INF/lib under your
TeamCity installation directory, then bounce TeamCity.

Calzone should then be visible at:

    http://YOUR_TEAMCITY_SERVER:8111/calzone/index.html

