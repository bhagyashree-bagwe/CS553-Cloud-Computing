#! /bin/bash
k=1
for bs in {1,1000,32000}
do
		for qw in {1,2,4,8}
		do
			touch network_UDP_C_job$k.slurm
			echo "#!/bin/bash">>network_UDP_C_job$k.slurm
			echo " ">>network_UDP_C_job$k.slurm
			echo "#SBATCH --nodes=1">>network_UDP_C_job$k.slurm
			echo "#SBATCH --output=main-UDP-"$bs"-"$qw".out">>network_UDP_C_job$k.slurm
			echo "#SBATCH --wait-all-nodes=1">>network_UDP_C_job$k.slurm
			echo " ">>network_UDP_C_job$k.slurm
			echo "echo \$SLURM_JOB_NODELIST">>network_UDP_C_job$k.slurm
			echo " ">>network_UDP_C_job$k.slurm
			echo "./MyNETBench-UDP /$(pwd)/network-UDP-"$bs"-"$qw"thread.dat S">>network_UDP_C_job$k.slurm
			k=$((k+1))
		done
done	
