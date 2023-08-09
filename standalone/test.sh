#!/bin/bash
# vim: ts=2 sw=2 sts=2 et ai

set -e

if [ ! -x "${JAVA_HOME}/bin/jlink" ]; then
  echo Please set JAVA_HOME
  exit 1
fi

TEST_DIR=$(dirname $0)
OUT_DIR="${TEST_DIR}/jdkImage"


rm -rf "${OUT_DIR}"

"${JAVA_HOME}/bin/java" -version

"${JAVA_HOME}/bin/jlink" \
  --module-path "${TEST_DIR}"/jmod \
  --add-modules java.base \
  --output "${OUT_DIR}" \
  --disable-plugin system-modules

ls -Rl "${OUT_DIR}"
