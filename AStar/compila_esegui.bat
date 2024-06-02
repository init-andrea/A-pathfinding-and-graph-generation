@echo off
javac *.java
timeout 1
jar cfm AStar.jar MANIFEST.MF *.class
timeout 1
java -jar AStar.jar