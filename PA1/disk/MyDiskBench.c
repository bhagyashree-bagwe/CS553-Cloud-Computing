#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>
#include <sys/time.h>
#include <dirent.h> 
#include <string.h>
#include <math.h>

long NUM_ITERATIONS = 1;
int THREAD_COUNT=1;
int BLOCK_SIZE=1;
long BLOCK_COUNT=1;
long ONE_GB = 10000000000;
//long ONE_GB = 10000000;
long WORKLOAD_PER_THREAD=1;
char* ACCESS_PATTERN;

double bytes_accessed, throughput_per_byte, throughput_per_MB=0.0;

void* sequential_read(void* arg) {
	FILE *fp = (FILE *)arg;
	fp = fopen("file.txt", "rb");

	char *buffer=(char *)malloc(BLOCK_SIZE);
	int i,j;
	for(j=0;j<NUM_ITERATIONS;j++) {
	for(i=0; i<BLOCK_COUNT; i++) {
	fread(buffer, BLOCK_SIZE, 1, fp);
	}
	}
	fclose(fp);
	pthread_exit(NULL);
}


void* sequential_write(void* arg) {
	FILE *fp = (FILE *)arg;
	fp = fopen("file.txt", "rb");
	char *buffer ="A";
	int i,j;
	for(j=0;j<NUM_ITERATIONS;j++){
	for(i=0; i<BLOCK_COUNT; i++) {
	fwrite(buffer, 1, 1, fp);
	}
	}
	fclose(fp);
	pthread_exit(NULL);
}


void* random_read(void* arg) {
	FILE *fp = (FILE *)arg;
	fp = fopen("file.txt", "rb");
	char *buffer=(char *)malloc(BLOCK_SIZE);
	int i,j;
	for(j=0;j<NUM_ITERATIONS;j++){
	for(i=0; i<BLOCK_COUNT; i++) {
	fseek(fp,rand()%BLOCK_SIZE+1,SEEK_SET);
	fread(buffer, BLOCK_SIZE, 1, fp);
	}
	}
	fclose(fp);
	pthread_exit(NULL);
}


void* random_write(void* arg) {
	FILE *fp = (FILE *)arg;
	fp = fopen("file.txt", "rb");
	char *buffer ="A";
	int i,j;
	for(j=0;j<NUM_ITERATIONS;j++){
	for(i=0; i<BLOCK_COUNT; i++) {
	fseek(fp,rand()%BLOCK_SIZE+1,SEEK_SET);
	fwrite(buffer, 1, 1, fp);
	}
	}
	fclose(fp);
	pthread_exit(NULL);
}


void perform_disk_benchmark() {
	FILE *fp;
	char *buffer = (char *)malloc(ONE_GB);
	memset(buffer, 'A', ONE_GB);
	fp = fopen("file.txt", "w+");
	fwrite(buffer, 1, ONE_GB, fp);
	//fseek(fp, 0, SEEK_SET);
	//char c= (char)fgetc(fp);
	//printf("%c",c);
	//fclose(fp);


	pthread_t thread_id[THREAD_COUNT];
	int thread[THREAD_COUNT]; int i;

	time_t start_t, end_t;
	double diff_t;

	time(&start_t);


	if (strncmp(ACCESS_PATTERN, "RS",2) == 0){
	for (i=0;i<THREAD_COUNT;i++) { 
		  thread[i] = pthread_create(&(thread_id[i]),NULL,sequential_read,fp);
	}
	}
	else if(strncmp(ACCESS_PATTERN, "WS",2) == 0){ 
	for (i=0;i<THREAD_COUNT;i++) {
		  thread[i] = pthread_create(&(thread_id[i]),NULL,sequential_write,fp);
	}
	}
	else if(strncmp(ACCESS_PATTERN, "RR",2) == 0){
	for (i=0;i<THREAD_COUNT;i++) {
		  thread[i] = pthread_create(&(thread_id[i]),NULL,random_read,fp);
	}
	}
	else if(strncmp(ACCESS_PATTERN, "WR",2) == 0){
	for (i=0;i<THREAD_COUNT;i++) {
		  thread[i] = pthread_create(&(thread_id[i]),NULL,random_write,fp);
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
	throughput_per_MB=throughput_per_byte/1000000.0;
	printf("Throughput = %f\n GB/sec", throughput_per_MB);
	printf("\n");
}

int main(int argc, char *argv[]) {
	FILE *file = fopen(argv[1], "r");
	if (file == NULL){
		printf ("Error opening %s",argv[1]);
		exit(0);
	}else{
		char *inputs[5];
		char line[10];
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
                perform_disk_benchmark();
		//printf("\n%s\n%d",PRECISION,THREAD_COUNT);
	}
	return 0;
}
