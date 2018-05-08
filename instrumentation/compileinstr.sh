#!/bin/sh
javac -cp . OpcodeMix.java
java -cp . OpcodeMix examples output
java -cp .:output/ Fibonacci
