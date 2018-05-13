#! /bin/bash
k=1
for seek in {RR,RS,WR,WS}
do
	for bs in {1000,10000,100000}
	do
		for qw in {1,2,4}
		do
			touch throughput_job$k.slurm
			echo "#!/bin/bash">>throughput_job$k.slurm
			echo " ">>throughput_job$k.slurm
			echo "#SBATCH --nodes=1">>throughput_job$k.slurm
			echo "#SBATCH --output=main-"$seek"-"$bs"-"$qw".out">>throughput_job$k.slurm
			echo "#SBATCH --wait-all-nodes=1">>throughput_job$k.slurm
			echo " ">>throughput_job$k.slurm
			echo "echo \$SLURM_JOB_NODELIST">>throughput_job$k.slurm
			echo " ">>throughput_job$k.slurm
			echo "./MyDiskBench /$(pwd)/disk-"$seek"-"$bs"-"$qw"thread.dat">>throughput_job$k.slurm
			k=$((k+1))
		done	
	done
done	
