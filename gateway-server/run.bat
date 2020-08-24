@echo off
title Gateway-Server

java -Xmx500m -jar %~dp0target/gateway-server-1.0.1.jar --gateway.routes.path=/workstyle/workstation/intellij/springcloud-ms-tool/gateway-server
pause