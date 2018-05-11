#!/bin/bash
java -cp ../instrumentation/:src/main/java/pt/ulisboa/tecnico/meic/cnv/mazerunner/maze/strategies/ Tool src/main/java/pt/ulisboa/tecnico/meic/cnv/mazerunner/maze/strategies/ output
cp output/* src/main/java/pt/ulisboa/tecnico/meic/cnv/mazerunner/maze/strategies/
