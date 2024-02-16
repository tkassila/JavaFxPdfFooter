# java -version
# set ESPEAK_HOME="."
# SET PATH=%ESPEAK_HOME%;%PATH%
# echo %ESPEAK_HOME%
export DISPLAY=:0
echo java -cp ./lib/com.lowagie.text-2.1.7.jar:./lib/bcprov-ext-jdk15on-1.70.jar:./JavaFxPdfFooter.jar:$CLASSPATH com.metait.javafxpdffooter.JavaFxPdfFooterApplication
java -cp ./lib/com.lowagie.text-2.1.7.jar:./lib/bcprov-ext-jdk15on-1.70.jar:./JavaFxPdfFooter.jar:$CLASSPATH com.metait.javafxpdffooter.JavaFxPdfFooterApplication
# com.metait.javafxplayer.testapp.HelloWorld
