#! /bin/bash
k=1
for seek in {RR,WR}
do
		for qw in {1,2,4,8,16,32,64,128}
		do
			touch latency_job$k.slurm
			echo "#!/bin/bash">>latency_job$k.slurm
			echo " ">>latency_job$k.slurm
			echo "#SBATCH --nodes=1">>latency_job$k.slurm
			echo "#SBATCH --output=main"$seek"-1-"$qw".out">>latency_job$k.slurm
			echo "#SBATCH --wait-all-nodes=1">>latency_job$k.slurm
			echo " ">>latency_job$k.slurm
			echo "echo \$SLURM_JOB_NODELIST">>latency_job$k.slurm
			echo " ">>latency_job$k.slurm
			echo "./MyDiskBench /$(pwd)/disk-"$seek"-1-"$qw"thread.dat">>latency_job$k.slurm
			k=$((k+1))
		done	
done	
