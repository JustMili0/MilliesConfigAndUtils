@echo off
cd /D "C:\Users\%username%\AppData\Roaming\ModrinthApp\profiles\Forge 1.20.1\mods"
echo Deleting old test jar...
del /q MilliesCoreLibs*.jar >nul

echo Building...
cd /D "%~dp0.."
powershell -ExecutionPolicy Bypass -c "./gradlew :forge:build"

echo Moving files...
move "%~dp0..\forge\build\libs\*.jar" "C:\Users\%username%\AppData\Roaming\ModrinthApp\profiles\Forge 1.20.1\mods" >nul

echo Deleting shadow jars...
cd /D "C:\Users\%username%\AppData\Roaming\ModrinthApp\profiles\Forge 1.20.1\mods"
del /q *dev-shadow.jar >nul

echo Done.
timeout 5 >nul
exit