@echo off
title Tres en Raya - IA Minimax
echo.
echo ========================================
echo        TRES EN RAYA CON IA MINIMAX
echo ========================================
echo.
echo Compilando el proyecto...

REM Compilar el proyecto
javac -cp "src/main/java" -d "bin" src/main/java/main/Main.java src/main/java/logica/*.java src/main/java/presentacion/*.java

if %errorlevel% neq 0 (
    echo ERROR: No se pudo compilar el proyecto
    echo Verifica que tienes Java instalado correctamente
    pause
    exit /b 1
)

echo Compilacion exitosa!
echo.
echo Iniciando el juego...
echo.

REM Ejecutar el juego
java -cp "bin" main.Main

if %errorlevel% neq 0 (
    echo ERROR: No se pudo ejecutar el juego
    pause
    exit /b 1
)

echo.
echo Juego terminado. Gracias por jugar!
pause
