#!/bin/sh
javac -cp . Tool.java
java -cp . Tool examples output
