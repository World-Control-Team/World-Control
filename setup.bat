echo "Setting up IntelliJ development environment with gradle..."
rmdir /s /q .\build
call gradlew.bat wrapper --gradle-version 4.4.1
call gradlew.bat setupDecompWorkspace idea --refresh-dependencies --scan

echo "Setup Complete."
pause