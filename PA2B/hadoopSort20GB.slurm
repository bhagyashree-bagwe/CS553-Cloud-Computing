#! /bin/bash 
 
#SBATCH --nodes=1
#SBATCH --output=HadoopSort20GB.log
#SBATCH --wait-all-nodes=1

hadoop jar HadoopSort-1.0.jar /input/data-20GB > ./output20.log
