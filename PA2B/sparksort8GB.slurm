#! /bin/bash 
 
#SBATCH --nodes=1
#SBATCH --output=SparkSort8GB.log
#SBATCH --wait-all-nodes=1
 
echo 

spark-submit --master yarn --deploy-mode client --driver-memory 1g --executor-memory 1g --executor-cores 1 --num-executors 4 SparkSort-1.0.jar /input/data-8GB /user/bbagwe/output-spark
./valsort /user/bbagwe/output-spark
