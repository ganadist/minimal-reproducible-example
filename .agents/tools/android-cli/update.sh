#!/bin/bash
# vim: ts=2 sw=2 sts=2 et ai
set -e

DIRNAME=$(dirname "${0}")
BINARY_NAME="android"
BINARY_BIN="android.bin"

# 1. Check if curl is installed
if ! command -v curl &> /dev/null; then
    echo "Error: curl is required to install or upgrade this software."
    exit 1
fi

TMP_FILE=$(mktemp)
PREVIOUS_VERSION=$("${DIRNAME}"/android -V) || true

echo "Downloading..."

for URL_OS in linux_x86_64 darwin_arm64
do
  DOWNLOAD_URL="https://dl.google.com/android/cli/latest/${URL_OS}/${BINARY_NAME}"
  INSTALL_DIR=${DIRNAME}/bin/${URL_OS}

  curl -fsSL "${DOWNLOAD_URL}" -o "${TMP_FILE}"
  mv -f "${TMP_FILE}" "${INSTALL_DIR}/${BINARY_BIN}"
  chmod +x "${INSTALL_DIR}/${BINARY_BIN}"

done

CURRENT_VERSION=$("${DIRNAME}"/android -V)

if [ "${PREVIOUS_VERSION}" = "${CURRENT_VERSION}" ]; then
  echo "${DIRNAME}/android was already up to date with ${PREVIOUS_VERSION} version."
else
  echo "----------------------------------------"
  echo "✅ Success! ${DIRNAME}/android is ready to use with ${CURRENT_VERSION} from ${PREVIOUS_VERSION} version."
  echo "Please create a commit to apply on git repository"
  git status "${DIRNAME}"
  echo
  echo "----------------------------------------"
fi
