@echo off
title QuizMaster — Online Quiz Platform
echo ===================================================
echo     QuizMaster — Online Quiz Platform (Java Swing)
echo ===================================================
echo Starting application...

if exist "C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot\bin\java.exe" (
    "C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot\bin\java.exe" -cp "target\classes;lib\ojdbc11.jar" quizmaster.Main
) else (
    java -cp "target\classes;lib\ojdbc11.jar" quizmaster.Main
)

pause
