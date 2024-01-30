javac -cp lib/Pokemon.jar -d classes src/*.java src/*/*.java
echo -e "Main-Class: Main\nClass-Path: lib/Pokemon.jar\n" > MANIFEST.mf
jar -cvfm app.jar MANIFEST.mf -C classes .
java -jar app.jar