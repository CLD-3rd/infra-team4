@echo off
set MVNW_URL=https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper.jar
if not exist .mvn\wrapper\maven-wrapper.jar (
  mkdir .mvn\wrapper
  powershell -Command "Invoke-WebRequest -Uri %MVNW_URL% -OutFile .mvn\wrapper\maven-wrapper.jar"
)
java -jar .mvn\wrapper\maven-wrapper.jar %*
