#!/bin/bash
# vim: ts=2 sw=2 sts=2 et ai

BUNDLETOOL_VERSION=1.15.4
BUNDLETOOL_DOWNLOAD_URL=https://github.com/google/bundletool/releases/download/${BUNDLETOOL_VERSION}/bundletool-all-${BUNDLETOOL_VERSION}.jar
BUNDLETOOL_JAR=$(basename ${BUNDLETOOL_DOWNLOAD_URL})

if [ ! -f ${BUNDLETOOL_JAR} ]; then
  curl -nOL ${BUNDLETOOL_DOWNLOAD_URL}
fi

AAB_FILE=app/build/outputs/bundle/developDebug/app-develop-debug.aab 
OUTPUT_APKS=outputs.apks

rm -f ${OUTPUT_APKS}

bundletool() {
  java -jar ${BUNDLETOOL_JAR} "$@"

}

bundletool dump resources \
  --bundle=${AAB_FILE} \
  --resource=drawable/img1 --values

bundletool dump resources \
  --bundle=${AAB_FILE} \
  --resource=drawable/img2 --values

bundletool build-apks \
  --bundle=${AAB_FILE} \
  --output=${OUTPUT_APKS}

rm -rf tmp
mkdir -p tmp

unzip -d tmp ${OUTPUT_APKS} > /dev/null


for apk in tmp/splits/base-master.apk tmp/splits/base-*dpi.apk
do
  echo $apk
  unzip -t $apk |grep img
  echo
done
