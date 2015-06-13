@echo off
echo [INFO] Use maven jetty-plugin run the project.

cd %~dp0
cd ..

set MAVEN_OPTS=-Xdebug -Xnoagent -Djava.compiler=NONE -Djetty.reload=automatic -Djetty.scanIntervalSeconds=5 -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n -XX:MaxPermSize=128m 
call mvn jetty:run -Djetty.port=80

cd bin
pause