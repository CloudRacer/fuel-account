@ECHO OFF

SET PROJECT_FOLDER=%~1

SET BUILD_COMMAND=ant debug

echo Building the project "%PROJECT_FOLDER%".
echo.
echo.
echo.

cd "%PROJECT_FOLDER%"
cmd /C """%BUILD_COMMAND%"""