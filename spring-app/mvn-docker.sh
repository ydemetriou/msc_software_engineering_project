#!/usr/bin/env bash
set -euo pipefail

# Runs Maven inside a Docker container against the spring-app module.
# Usage: ./mvn-docker.sh [maven goals/args]
# If no arguments are provided, it defaults to 'compile'.

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
IMAGE="maven:3.9.6-eclipse-temurin-17"

if ! command -v docker >/dev/null 2>&1; then
  echo "Error: docker is not installed or not in PATH." >&2
  exit 127
fi

# Default to 'compile' when no arguments are passed
if [ $# -eq 0 ]; then
  set -- compile
fi

# Mount project and Maven cache for faster builds
exec docker run --rm \
  -v "${SCRIPT_DIR}":/app \
  -v "${HOME}/.m2":/root/.m2 \
  -w /app \
  "${IMAGE}" mvn "$@"

