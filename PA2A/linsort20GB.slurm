#! /bin/bash
#SBATCH --nodes=1
#SBATCH --output=linsort20GB.log
#SBATCH --wait-all-nodes=1
export LC_ALL=C
time sort /input/data-20GB.in > /tmp/linsort20GBOutput.txt
./valsort /tmp/linsort20GBOutput.txt
