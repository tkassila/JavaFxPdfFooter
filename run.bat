@ echo off
call \java\jdk11fxcp.bat
java -version
ho %0
set dt_exec_drive=%~d0
set dt_exec_path=%~p0
set dt_exec_path2=%dt_exec_drive%%dt_exec_path%
echo ajohakemisto: %dt_exec_path2%
set dtcp=%dt_exec_path2%;%dt_exec_path2%dtbook2asciimath.jar;.
rem SET PATH=%ESPEAK_HOME%;%PATH%
rem echo %ESPEAK_HOME%
echo java -cp %dt_exec_path2%lib\com.lowagie.text-2.1.7.jar;%dt_exec_path2%lib\bcprov-ext-jdk15on-1.70.jar;%dt_exec_path2%JavaFxPdfFooter.jar;%CLASSPATH% com.metait.javafxpdffooter.JavaFxPdfFooterApplication
java -cp %dt_exec_path2%lib\com.lowagie.text-2.1.7.jar;%dt_exec_path2%lib\bcprov-ext-jdk15on-1.70.jar;%dt_exec_path2%JavaFxPdfFooter.jar;%CLASSPATH% com.metait.javafxpdffooter.JavaFxPdfFooterApplication
rem com.metait.javafxplayer.testapp.HelloWorld
rem pause
