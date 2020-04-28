@echo off
title Gateway-Server

java -Xmx500m -jar %~dp0target/gateway-server-1.0.1.jar
pause