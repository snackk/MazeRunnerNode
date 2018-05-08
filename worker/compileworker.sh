#!/bin/bash

#find -name "*.java" > workerfiles.txt
find -name "*.java" | xargs javac 
