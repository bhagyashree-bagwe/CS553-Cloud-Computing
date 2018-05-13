bbagwe A20399761
CC PA2
------------------------------------

There are two different executables, one for Hadoop sort and one for Spark sort namely:
1. HadoopSort.jar
2. SparkSort.jar

The programs are excuted using SLURM job scheduler on a neutron cluster.

Steps of execution ~ Hadoop sort
------------------------------------

1. Navigate to /bbagwe directory of code base

2. Schedule the job using following command from the command line
sbatch hadoopsort8GB.slurm

The slurm job contains command to execute the jar file: hadoop jar HadoopSort.jar HadoopSort -D mapred.reduce.tasks=1 /input/data-8GB /user/bbagwe/output-hadoop

3. You can check status of the job using squeue command
squeue

4. You can cancel the submitted job usinf scancel command
scancel job _id

5. Once the job finishes execution, the sorted data will be written to /tmp/

7. Output of script will be written to HadoopSort8GB.log
The log file mentions the total time of execution and results of valsort on the file FinalSortedOutput0

Steps of execution ~ Spark sort
------------------------------------

1. Navigate to /bbagwe directory of code base

2. Schedule the job using following command from the command line
sbatch sparksort8GB.slurm

The slurm job contains command to execute the jar file: park-submit --master yarn --deploy-mode client --driver-memory 1g --executor-memory 1g --executor-cores 1 --num-executors 1 SparkSort-1.0.jar /input/data-8GB /user/bbagwe/output-spark

3. You can check status of the job using squeue command
squeue

4. You can cancel the submitted job usinf scancel command
scancel job _id

5. Once the job finishes execution, the sorted data will be written to /tmp/

7. Output of script will be written to SparkSort8GB.log
The log file mentions the total time of execution and results of valsort on the output file


