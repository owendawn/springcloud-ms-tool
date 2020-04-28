@echo off
title Zuul-Server

java -Xmx500m -jar  %~dp0target/zuul-server-1.0.1.jar
pause