#!/usr/bin/env bash
# Builds Tienda POS with a bundled Java runtime using jpackage.
#
#   packaging/build-installer.sh            -> target/installer/Tienda POS/ (app image, runs anywhere on this OS)
#   packaging/build-installer.sh deb        -> .deb package (needs dpkg-deb, fakeroot)
#
# The Windows .msi is built on Windows with packaging/build-installer.ps1
# (jpackage cannot cross-build installers for another OS).
set -euo pipefail
cd "$(dirname "$0")/.."
TYPE="${1:-app-image}"
# Java modules the app uses (from jdeps) + locale data for Kiswahili formatting.
MODULES="java.base,java.compiler,java.desktop,java.instrument,java.management,java.naming,java.net.http,java.prefs,java.scripting,java.sql,jdk.net,java.logging,java.xml,jdk.localedata,jdk.charsets,jdk.zipfs,jdk.crypto.ec,jdk.unsupported"

VERSION=$(sed -n 's:.*<version>\(.*\)</version>.*:\1:p' pom.xml | head -1)
echo "Building Tienda POS $VERSION ($TYPE)"
mvn -B -q clean package -DskipTests

rm -rf target/installer
mkdir -p target/installer
ARGS=(
  --type "$TYPE"
  --name "Tienda POS"
  --app-version "$VERSION"
  --vendor "Nkanabo"
  --description "Point of sale and stock management for shops"
  --icon packaging/tienda.png
  --input target/dist
  --main-jar "Tienda-$VERSION.jar"
  --main-class com.nkanabo.Tienda.Main
  --java-options "-Dfile.encoding=UTF-8"
  --add-modules "$MODULES"
  --jlink-options "--strip-debug --no-header-files --no-man-pages --compress=zip-6"
  --dest target/installer
)
if [ "$TYPE" = "deb" ]; then
  ARGS+=(--license-file packaging/EULA.txt --linux-shortcut --linux-menu-group Office)
fi
jpackage "${ARGS[@]}"
ls -la target/installer
