#!/bin/sh
DOCKER_HOST="unix:///run/user/1000/podman/podman.sock" TESTCONTAINERS_RYUK_DISABLED="TRUE" ./gradlew clean test

