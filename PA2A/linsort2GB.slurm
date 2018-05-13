#! /bin/bash
#SBATCH --nodes=1
#SBATCH --output=linsort2GB.log
#SBATCH --wait-all-nodes=1
export LC_ALL=C
time sort /input/data-2GB.in > /tmp/linsort2GBOutput.txt
./valsort /tmp/linsort2GBOutput.txt
