@echo off
title Config-Server

java -Xmx300m -jar %~dp0target/config-server-1.0.1.jar
pause