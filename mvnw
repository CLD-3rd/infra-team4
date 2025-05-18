#!/usr/bin/env sh
if [ -z "$MAVEN_HOME" ]; then
  MVNW_URL="https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper.jar"
  MAVEN_WRAPPER_JAR=".mvn/wrapper/maven-wrapper.jar"
  mkdir -p .mvn/wrapper
  curl -sSLo "$MAVEN_WRAPPER_JAR" "$MVNW_URL"
  java -jar "$MAVEN_WRAPPER_JAR" "$@"
else
  mvn "$@"
fi
