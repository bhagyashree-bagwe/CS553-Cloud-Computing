#! /bin/bash
#SBATCH --nodes=1
#SBATCH --output=mysort2GB.log
#SBATCH --wait-all-nodes=1
java -cp ExternalSortExe.jar ExternalMergeSort -Xms2g -Xmx3g
./valsort /tmp/FinalSortedOutput0
