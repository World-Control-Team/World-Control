echo "Building Jar"
rmdir /s /q .\build\libs
call gradlew.bat build --refresh-dependencies
echo "Build Complete."
pause
