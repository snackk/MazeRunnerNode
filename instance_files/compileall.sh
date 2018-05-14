#!/bin/bash
cd instrumentation/
./compileinstr.sh
cd ../worker
./cleanworker.sh
./compileworker.sh
cd ../nodeMazeRunner/
./compile.sh
