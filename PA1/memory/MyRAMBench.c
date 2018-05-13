#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <pthread.h>
#include <math.h>
#include <sys/time.h>
#include <dirent.h> 

long NUM_ITERATIONS = 1;
int THREAD_COUNT=1;
int BLOCK_SIZE=1;
long BLOCK_COUNT=1;
long ONE_GB = 1000000000;
//long ONE_GB = 10000000;
long WORKLOAD_PER_THREAD=1;
char* ACCESS_PATTERN;
char * src;
char * dest;
double bytes_accessed, throughput_per_byte, throughput_per_GB=0.0, theoreticalValue=12.8, myRAMBenchEfficiency, latency;

pthread_mutex_t mutex;

void* sequential_read_write(void* arg) {
     int * tid = (int *)arg;
     long start;
     for(int j=0;j<100;j++){
     start=(*tid-1)*WORKLOAD_PER_THREAD;
     for(int i=start;i<WORKLOAD_PER_THREAD;i+=BLOCK_SIZE) {
     memcpy(src+i, dest, BLOCK_SIZE);
     }}
     pthread_exit(NULL);
}



void* random_read_write(void* arg) {
     int * tid = (int *)arg;
     long start;
     int random=-1;
     for(int j=0;j<100;j++){
     start=(*tid-1)*WORKLOAD_PER_THREAD;
     for(int i=start;i<WORKLOAD_PER_THREAD;i+=BLOCK_SIZE) {
     while(random>=0 && random<=ONE_GB){
     random = rand() % BLOCK_COUNT;}
     memcpy(src+(i+random), dest, BLOCK_SIZE);
     }}
     pthread_exit(NULL);
}

void perform_memory_benchmark()
{

src = (char *)malloc(ONE_GB);
dest = (char *)malloc(BLOCK_SIZE);

memset(src, 'A', ONE_GB);
memset(dest,'B', BLOCK_SIZE);

pthread_t thread_id[THREAD_COUNT];
int thread[THREAD_COUNT]; int i;

time_t start_t, end_t;
double diff_t;

time(&start_t);
int THREAD_ID1=1;
int THREAD_ID2=2;
int THREAD_ID3=3;
int THREAD_ID4=4;
int *idx;

if (strcmp("RWS", ACCESS_PATTERN)==0){ 
for (i=0;i<THREAD_COUNT;i++) { 
	  if(i==0){idx= &THREAD_ID1;}
	  else if(i==1){idx=&THREAD_ID2;}
	  else if (i==2){idx=&THREAD_ID3;}
	  else if (i==3){idx=&THREAD_ID4;}
          thread[i] = pthread_create(&(thread_id[i]),NULL,sequential_read_write,(void *)idx);
}
}
else if (strcmp("RWR", ACCESS_PATTERN)==0){
for (i=0;i<THREAD_COUNT;i++) {
if(i==0){idx= &THREAD_ID1;}
	  else if(i==1){idx=&THREAD_ID2;}
	  else if (i==2){idx=&THREAD_ID3;}
	  else if (i==3){idx=&THREAD_ID4;}
          thread[i] = pthread_create(&(thread_id[i]),NULL,random_read_write,(void *)idx);
}
}


for (i=0;i<THREAD_COUNT;i++) {
pthread_join(thread_id[i], NULL);
}

time(&end_t);

diff_t = difftime(end_t, start_t);
printf("Execution time = %f\n", diff_t);
bytes_accessed = (double) (ONE_GB*NUM_ITERATIONS);
throughput_per_byte = bytes_accessed/diff_t;
throughput_per_GB=throughput_per_byte/ONE_GB;
myRAMBenchEfficiency=(throughput_per_GB*100)/theoreticalValue;
printf("Workload: 100GB Concurrency: %d BlockSize: %d MyRAMBenchValue: %.2f TheoreticalValue: %.2f MyRAMBenchEfficiency: %.2f",THREAD_COUNT,BLOCK_SIZE,throughput_per_GB,theoreticalValue);
if(BLOCK_SIZE == 1)
{
latency = (diff_t/(100*BLOCK_SIZE))*1000000;
printf("Latency : %.2f \n",latency);
}
}


int main(int argc, char *argv[]) {FILE *file = fopen(argv[1], "r");
	if (file == NULL){
		printf ("Error opening %s",argv[1]);
		exit(0);
	}else{
		char *inputs[5];
		char line[20];
		int idx=0;
		while (fgets(line, sizeof line, file) != NULL)
		{
			line[strlen(line) - 1] = '\0';
			inputs[idx++]=strdup(line);
		}
		fclose (file);
                ACCESS_PATTERN = inputs[0];
		BLOCK_SIZE = atoi(inputs[1]);
		THREAD_COUNT = atoi(inputs[2]);
		WORKLOAD_PER_THREAD=ONE_GB/THREAD_COUNT;
		BLOCK_COUNT = WORKLOAD_PER_THREAD/BLOCK_SIZE;
                perform_memory_benchmark();
	} 
return 0;
}
