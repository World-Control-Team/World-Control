echo "Building Jar"
rmdir /s /q .\build\libs
call gradlew.bat build --refresh-dependencies --scan
echo "Build Complete."
pause
