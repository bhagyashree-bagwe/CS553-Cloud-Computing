#! /bin/bash 
 
#SBATCH --nodes=1
#SBATCH --output=HadoopSort8GB.log
#SBATCH --wait-all-nodes=1

hadoop jar HadoopSort-1.0.jar /input/data-8GB > ./output.log
