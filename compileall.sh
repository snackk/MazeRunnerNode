#!/bin/bash
cd instrumentation/
./compileinstr.sh
cd ../worker
./cleanworker.sh
./compileworker.sh
./instrument.sh
cd ../mazerunnernode/
./compile.sh

