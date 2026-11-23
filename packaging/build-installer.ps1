# Builds the Windows installer: target\installer\Tienda POS-<version>.msi
#
# Needs, on a Windows PC:
#   - JDK 21 (jpackage comes with it), JAVA_HOME set
#   - Maven on PATH
#   - WiX Toolset 3.14 on PATH (https://github.com/wixtoolset/wix3/releases), used by jpackage for .msi
#
# Run from the project folder:  powershell -ExecutionPolicy Bypass -File packaging\build-installer.ps1
# Optional signing: set $env:SIGN_PFX and $env:SIGN_PASSWORD (a code-signing certificate) first.

$ErrorActionPreference = "Stop"
Set-Location (Split-Path $PSScriptRoot -Parent)

[xml]$pom = Get-Content pom.xml
$version = $pom.project.version
Write-Host "Building Tienda POS $version"

mvn -B -q clean package -DskipTests
if ($LASTEXITCODE -ne 0) { throw "Maven build failed" }

# Java modules the app uses (from jdeps) + locale data for Kiswahili formatting.
$modules = "java.base,java.compiler,java.desktop,java.instrument,java.management,java.naming,java.net.http,java.prefs,java.scripting,java.sql,jdk.net,java.logging,java.xml,jdk.localedata,jdk.charsets,jdk.zipfs,jdk.crypto.ec,jdk.unsupported"
$out = "target\installer"
New-Item -ItemType Directory -Force -Path $out | Out-Null

# The upgrade UUID must never change: it lets a new .msi replace the old one.
jpackage `
  --type msi `
  --name "Tienda POS" `
  --app-version $version `
  --vendor "Nkanabo" `
  --description "Point of sale and stock management for shops" `
  --copyright "Copyright (c) Nkanabo" `
  --license-file packaging\EULA.txt `
  --icon packaging\tienda.ico `
  --input target\dist `
  --main-jar "Tienda-$version.jar" `
  --main-class com.nkanabo.Tienda.Main `
  --java-options "-Dfile.encoding=UTF-8" `
  --add-modules $modules `
  --jlink-options "--strip-debug --no-header-files --no-man-pages --compress=zip-6" `
  --win-menu --win-menu-group "Tienda POS" `
  --win-shortcut --win-dir-chooser `
  --win-per-user-install `
  --win-upgrade-uuid 0ca52a5e-e703-4736-85b1-dd0d7182f1a5 `
  --dest $out
if ($LASTEXITCODE -ne 0) { throw "jpackage failed" }

$msi = Get-ChildItem $out -Filter *.msi | Sort-Object LastWriteTime | Select-Object -Last 1

if ($env:SIGN_PFX) {
  # signtool comes with the Windows SDK.
  signtool sign /f $env:SIGN_PFX /p $env:SIGN_PASSWORD /fd SHA256 `
    /tr http://timestamp.digicert.com /td SHA256 $msi.FullName
  if ($LASTEXITCODE -ne 0) { throw "Signing failed" }
  Write-Host "Signed."
} else {
  Write-Host "Not signed (set SIGN_PFX / SIGN_PASSWORD to sign). Windows SmartScreen will warn users."
}
Write-Host "Installer: $($msi.FullName)"
