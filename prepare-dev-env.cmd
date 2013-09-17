@echo off
@REM --------------------------------------------------------
@REM prepare-dev-env - Skrypt Win32 do przygotowania srodowiska
@REM    programisty. Powinien byc uruchomiony:
@REM     - po sciagnieciu swiezego projektu z CVS
@REM     - po zmianie listy bibliotek w web/pom.xml
@REM     - po zmianie zasobow filtrowanych w web/src/main/config
@REM --------------------------------------------------------

SETLOCAL

echo.
echo prepare-dev-env - Skrypt przygotowuje srodowisko programisty
echo.
echo.

echo.
echo Czyszcze srodowisko programisty...
echo.
cmd /c "mvn clean && if errorlevel 1 echo. && echo Blad podczas czyszczenia. Ignorujemy... && echo."

echo.
echo Buduje projekt...
echo.
cmd /c "mvn install -U -Pjrebel && if errorlevel 1 echo. && echo UWAGA: Blad podczas budowania projektu. Nie uruchamiaj aplikacji jrebel && echo Sprobujemy TYLKO skompletowac biblioteki... && echo. && exit 1"
if errorlevel 1 set bledy=tak && cmd /c "echo. & echo Generuje pliki konfiguracyjne i kompletuje biblioteki & echo. & mvn -fn install -Pinplace"


if a%bledy% == atak echo. && echo UWAGA: Bledy podczas budowania projektu. Zerknij do gory... 

ENDLOCAL