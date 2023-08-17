#!/bin/bash
# vim: ts=2 sw=2 sts=2 et ai

set -e

if [ ! -x "${JAVA_HOME}/bin/jlink" ]; then
  echo Please set JAVA_HOME
  exit 1
fi

TEST_DIR=$(dirname $0)
OUT_DIR="${TEST_DIR}/jdkImage"

JAVA_EXE="${JAVA_HOME}"/bin/java
JAVAC="${JAVA_HOME}"/bin/javac
JLINK="${JAVA_HOME}"/bin/jlink
JMOD="${JAVA_HOME}"/bin/jmod


ANDROID_MODULE="${TEST_DIR}/android-33/core-for-system-modules.jar"
MODULE_DESCRIPTION_OUT="${TEST_DIR}/module-description"
JMOD_OUT="${TEST_DIR}/jmod/java.base.jmod"

JLINK_VERSION=$(${JLINK} --version)

rm -rf "${OUT_DIR}" "${MODULE_DESCRIPTION_OUT}"


"${JAVA_EXE}" -version

# https://cs.android.com/android-studio/platform/tools/base/+/mirror-goog-studio-main:build-system/gradle-core/src/main/java/com/android/build/gradle/internal/dependency/JdkImageTransformDelegate.kt;l=180
compileModuleDescriptor() {
  local moduleInfoJava="$1"
  local systemModuleJar="$2"
  local outDir="$3"

  echo "Compiling module-info.java"
  "${JAVAC}" \
    --system=none \
    --patch-module=java.base="${systemModuleJar}" \
    -d "${outDir}" \
    "${moduleInfoJava}"
}

# https://cs.android.com/android-studio/platform/tools/base/+/mirror-goog-studio-main:build-system/gradle-core/src/main/java/com/android/build/gradle/internal/dependency/JdkImageTransformDelegate.kt;l=208
createJmodFromModularJar() {
  local jmodFile="$1"
  local jlinkVersion="$2"
  local moduleJar="$3"

  # clean up old binaries
  rm -f "${jmodFile}"
  mkdir -p $(dirname "${jmodFile}")

  echo "Create java.base.jmod for Android Platform"
  "${JMOD}" \
    create \
    --module-version ${jlinkVersion} \
    --target-platform android \
    --class-path "${moduleJar}" \
    "${jmodFile}"
}

# https://cs.android.com/android-studio/platform/tools/base/+/mirror-goog-studio-main:build-system/gradle-core/src/main/java/com/android/build/gradle/internal/dependency/JdkImageTransformDelegate.kt;l=234
linkJmodsIntoJdkImage() {
  local jmodDir="$1"
  local moduleName="$2"
  local outDir="$3"

  echo "Perform jlink to generate image"
  "${JLINK}" -v \
    --module-path "$(dirname "${JMOD_OUT}")" \
    --add-modules "${moduleName}" \
    --output "${outDir}" \
    --disable-plugin system-modules
}

compileModuleDescriptor \
  "${TEST_DIR}/module-info.java" \
  "${ANDROID_MODULE}" \
  "${MODULE_DESCRIPTION_OUT}"

createJmodFromModularJar \
  "${JMOD_OUT}" \
  "${JLINK_VERSION}" \
  "${ANDROID_MODULE}:${MODULE_DESCRIPTION_OUT}"

linkJmodsIntoJdkImage \
  "$(dirname "${JMOD_OUT}")" \
  java.base \
  "${OUT_DIR}"

echo
echo "list of jmod"
"${JMOD}" describe "${JMOD_OUT}" | head -n 8
