#! /bin/bash
k=1
for bs in {1,1000,32000}
do
		for qw in {1,2,4,8}
		do
			touch network_TCP_S_job$k.slurm
			echo "#!/bin/bash">>network_TCP_S_job$k.slurm
			echo " ">>network_TCP_S_job$k.slurm
			echo "#SBATCH --nodes=1">>network_TCP_S_job$k.slurm
			echo "#SBATCH --output=main-TCP-"$bs"-"$qw".out">>network_TCP_S_job$k.slurm
			echo "#SBATCH --wait-all-nodes=1">>network_TCP_S_job$k.slurm
			echo " ">>network_TCP_S_job$k.slurm
			echo "echo \$SLURM_JOB_NODELIST">>network_TCP_S_job$k.slurm
			echo " ">>network_TCP_S_job$k.slurm
			echo "./MyNETBench-TCP /$(pwd)/network-TCP-"$bs"-"$qw"-thread.dat S">>network_TCP_S_job$k.slurm
			k=$((k+1))
		done
done	
