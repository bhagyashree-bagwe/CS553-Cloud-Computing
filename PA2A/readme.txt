bbagwe A20399761
CC PA2
------------------------------------

There are two different executables, one for 2GB sorting program and one for 20GB program namely:
1. External20GBSortExe.jar
2. ExternalSortExe.jar

The programs are excuted using SLURM job scheduler on a neutron cluster.

Steps of execution ~ 2GB data file
------------------------------------

1. Navigate to /bbagwe directory of code base

2. Schedule the job using following command from the command line
sbatch mysort2GB.slurm

The slurm job contains command to execute the jar file: java -cp ExternalSortExe.jar ExternalMergeSort -Xms2g -Xmx3g

java :			This runs the Java interpreter
ExternalSortExe.jar :   Name of executable
ExternalMergeSort :     Name of Java Class containing main method(Start point of the program)
-Xms2g -Xmx3g :         cmd flags to increase the java heap memory to ensure successful execution


3. You can check status of the job using squeue command
squeue

4. You can cancel the submitted job usinf scancel command
scancel job _id

5. Once the job finishes execution, the sorted data will be written to /tmp/FinalSortedOutput0 file

7. Output of script will be written to mysort2GB.log
The log file mentions the total time of execution and results of valsort on the file FinalSortedOutput0

Steps of execution ~ 20GB data file
------------------------------------

1. Navigate to /bbagwe directory of code base

2. Schedule the job using following command from the command line
sbatch mysort20GB.slurm

The slurm job contains command to execute the jar file: java -cp ExternalSortExe.jar ExternalMergeSort -Xms2g -Xmx3g

java :			This runs the Java interpreter
External20GBSortExe.jar Name of executable
ExternalMerge20GB :     Name of Java Class containing main method(Start point of the program)
-Xms2g -Xmx3g :         cmd flags to increase the java heap memory to ensure successful execution


3. You can check status of the job using squeue command
squeue

4. You can cancel the submitted job usinf scancel command
scancel job _id

5. Once the job finishes execution, the sorted data will be written to /tmp/FinalSortedOutput file

7. Output of script will be written to mysort20GB.log
The log file mentions the total time of execution and results of valsort on the file FinalSortedOutput


Steps of execution ~ 2GB data file ~ linux sort
------------------------------------

1. Navigate to /bbagwe directory of code base

2. Schedule the job using following command from the command line
sbatch linsort2GB.slurm

The slurm job contains command to execute the jar file: time sort /input/data-2GB.in > /tmp/linsort2GBOutput.txt

time :			returns execution time of the script
sort			command that sorts the contents of a text file, line by line assuming ASCII content
data-2GB.in :           Name of input data file
linsort2GBOutput        Name of sorted output file


3. You can check status of the job using squeue command
squeue

4. You can cancel the submitted job usinf scancel command
scancel job _id

5. Once the job finishes execution, the sorted data will be written to /tmp/linsort2GBOutput.txt

7. Output of script will be written to linsort2GB.log
The log file mentions the total time of execution and results of valsort on the file linsort2GBOutput



Steps of execution ~ 20GB data file ~ linux sort
------------------------------------

1. Navigate to /bbagwe directory of code base

2. Schedule the job using following command from the command line
sbatch linsort20GB.slurm

The slurm job contains command to execute the jar file: time sort /input/data-20GB.in > /tmp/linsort20GBOutput.txt

time :			returns execution time of the script
sort			command that sorts the contents of a text file, line by line assuming ASCII content
data-20GB.in :           Name of input data file
linsort20GBOutput        Name of sorted output file


3. You can check status of the job using squeue command
squeue

4. You can cancel the submitted job usinf scancel command
scancel job _id

5. Once the job finishes execution, the sorted data will be written to /tmp/linsort20GBOutput.txt

7. Output of script will be written to linsort20GB.log
The log file mentions the total time of execution and results of valsort on the file linsort20GBOutput

