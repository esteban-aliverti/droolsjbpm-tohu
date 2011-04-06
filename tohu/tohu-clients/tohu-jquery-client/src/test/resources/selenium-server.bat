set MAVEN_REPOSITORY=%HOMEDRIVE%%HOMEPATH%\.m2\repository
REM set SELENIUM_PATH=org\openqa\selenium
set SELENIUM_PATH=org\seleniumhq\selenium
REM set SELENIUM_VERSION=1.0-beta-1
set SELENIUM_VERSION=1.0.1

java -jar "%MAVEN_REPOSITORY%\%SELENIUM_PATH%\server\selenium-server\%SELENIUM_VERSION%\selenium-server-%SELENIUM_VERSION%-standalone.jar"
