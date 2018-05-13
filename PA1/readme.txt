
bbagwe A20399761

Readme.txt
-----------------------------------------

Benchmarks are run using SLURM job scheduler


Running CPU Benchmark:

1. Navigate to /cpu directory of code base

2. Execuet following script
./create_CPU_slurm_job.sh

3. It creates slurm job files for all possible inputs having folloewing format:
throughput_job9.slurm

4. Submit each job file using sbatch command:
Sbatch throughput_job9.slurm

5. You can check status of the job using squeue command
squeue

6. You can cancel the submitted job usinf scancel command
scancel job _id

7. Output of the program will be written mainXX_X.out file by job
mainQP_4.out

--------------------------------------------------

Running MemoryBenchmark:

1. Navigate to /memory directory of code base

2. Execuet following script
./create_MEMORY_slurm_job.sh 

3. It creates slurm job files for all possible inputs having folloewing format:
throughput_job_20.slurm

4.Submit each job file using sbatch command:
sbatch main-RWS-10000000-1.out 

5. You can check status of the job using squeue command
squeue

6. You can cancel the submitted job usinf scancel command
scancel job _id

7. Output of the program will be written mainXX_X.out file by job
-------------------------------------------------

Running disk benchmark:

1. Navigate to /disk directory of code base

2.Execuet following scripts
./create_DISK_latency_slurm_job.sh
./create_DISK_throughput_slurm_job.sh

3.It creates slurm job files for all possible inputs having folloewing format:
latency_job13.slurm
throughput_job20.slurm

4.Submit each job file using sbatch command:
sbatch throughput_job20.slurm

5. You can check status of the job using squeue command
squeue

6. You can cancel the submitted job usinf scancel command
scancel job _id

7. Output of the program will be written mainXX_X.out file by job
main-WS-1000-1.out
mainRR-1-1.out

-----------------------------------------------

Running Network benchmark:

1. Navigate to /network directory of code base

2.Execuet following scripts
create_NETWORK_TCP_C_slurm_job.sh
create_NETWORK_TCP_S_slurm_job.sh
create_NETWORK_UDP_C_slurm_job.sh
create_NETWORK_UDP_S_slurm_job.sh


3.It creates slurm job files for all possible inputs having folloewing format:
network_TCP_C_job10.slurm
network_TCP_S_job10.slurm

4.Submit each job file using sbatch command:
sbatch throughput_job20.slurm

5. You can check status of the job using squeue command
squeue

6. You can cancel the submitted job usinf scancel command
scancel job _id

7. Output of the program will be written mainXX_X.out file by job
