#! /bin/bash
#SBATCH --nodes=1
#SBATCH --output=mysort20GB.log
#SBATCH --wait-all-nodes=1
java -cp External20GBSortExe.jar ExternalMerge20GB -Xms1g -Xmx2g
./valsort /tmp/FinalSortedOutput
