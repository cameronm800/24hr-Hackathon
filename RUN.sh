#!/bin/bash
export CLASSPATH=${CLASSPATH}:./Database/sqlite-jdbc-3.51.3.0.jar
pip install matplotlib
javac */*.java
java GameUI.Main