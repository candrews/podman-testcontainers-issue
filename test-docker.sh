#!/bin/sh
DOCKER_HOST="unix:///var/run/docker.sock" TESTCONTAINERS_RYUK_DISABLED="TRUE" ./gradlew clean test

