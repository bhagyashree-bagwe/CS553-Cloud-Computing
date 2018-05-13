#! /bin/bash
k=1
for seek in {QP,HP,SP,DP}
do
	for qw in {1,2,4}
	do
		touch throughput_job$k.slurm
		echo "#!/bin/bash">>throughput_job$k.slurm
		echo " ">>throughput_job$k.slurm
		echo "#SBATCH --nodes=1">>throughput_job$k.slurm
		echo "#SBATCH --output=main"$seek"_"$qw".out">>throughput_job$k.slurm
		echo "#SBATCH --wait-all-nodes=1">>throughput_job$k.slurm
		echo " ">>throughput_job$k.slurm
		echo "echo \$SLURM_JOB_NODELIST">>throughput_job$k.slurm
		echo " ">>throughput_job$k.slurm
		echo "./MyCPUBench /$(pwd)/cpu_"$seek"_"$qw"thread.dat">>throughput_job$k.slurm
		k=$((k+1))
	done
done	
