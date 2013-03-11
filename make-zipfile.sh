#!/bin/sh

rm -f calzone.zip
cd plugin
zip -rv9 ../calzone.zip teamcity-plugin.xml server/calzone.jar
