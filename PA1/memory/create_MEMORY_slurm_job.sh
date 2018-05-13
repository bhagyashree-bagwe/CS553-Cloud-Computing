#! /bin/bash
k=1
for seek in {RWR,RWS}
do
	for bs in {1,1000,1000000,10000000}
	do
		for qw in {1,2,4}
		do
			touch throughput_job_$k.slurm
			echo "#!/bin/bash">>throughput_job_$k.slurm
			echo " ">>throughput_job_$k.slurm
			echo "#SBATCH --nodes=1">>throughput_job_$k.slurm
			echo "#SBATCH --output=main-"$seek"-"$bs"-"$qw".out">>throughput_job_$k.slurm
			echo "#SBATCH --wait-all-nodes=1">>throughput_job_$k.slurm
			echo " ">>throughput_job_$k.slurm
			echo "echo \$SLURM_JOB_NODELIST">>throughput_job_$k.slurm
			echo " ">>throughput_job_$k.slurm
			echo "./MyRAMBench /$(pwd)/memory-"$seek"-"$bs"-"$qw"thread.dat">>throughput_job_$k.slurm
			k=$((k+1))
		done	
	done
done	
