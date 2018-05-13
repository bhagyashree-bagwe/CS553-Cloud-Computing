#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <pthread.h>
#include <sys/time.h>
#include <dirent.h>
#include <immintrin.h> 

#define TRILLION 1000000000000

char * PRECISION;
//long TRILLION = 1000000000000L;
//long TRILLION = 10000000;
unsigned long long OPERATIONS_PER_THREAD=1000000000000;
double TOTAL_TIME;
long double PROCESSOR_SPEED;
int THREAD_COUNT;
long double MyCPUBenchValue;
double efficiency;
double TheoreticalValue=588.8;

pthread_mutex_t mutex;
struct timeval start_time, end_time;

void* calculate_QPOPS(void *arg) {
//printf("hiii %llu \n",OPERATIONS_PER_THREAD);
char ch1 = 'A', ch2 = 'B', ch3 = 'C', ch4 = 'D';
char ch5, ch6, ch7, ch8;
int i;
pthread_mutex_lock(&mutex);
for(i=0;i<OPERATIONS_PER_THREAD;i++) //2 operations, 1 comparison and 1 addition
{
ch5 = ch1 + 1; //2 operations, 1 addition and 1 assignment
ch6 = ch2 + 1; //2 operations, 1 addition and 1 assignment
ch7 = ch3 + 1; //2 operations, 1 addition and 1 assignment
ch8 = ch4 + 1; //2 operations, 1 addition and 1 assignment
}
printf("%c, %c, %c, %c\n",ch5,ch6,ch7,ch8);
pthread_mutex_unlock(&mutex);
pthread_exit(NULL);

//Total no of operations = 10
}

void* calculate_HPOPS(void *arg) {	
short a=1,b=2,c=3,d=4;
short sr1,sr2,sr3,sr4;
int i;
pthread_mutex_lock(&mutex);
for(i=0;i<OPERATIONS_PER_THREAD;i++)  //2 operations, 1 comparison and 1 addition
{
sr1=a*2+b*2+c*2+d*2;  //8 operations, 4 multiplications, 3 additions, 1 assignment
sr2=a*3+b*3+c*3+d*3;  //8 operations, 4 multiplications, 3 additions, 1 assignment
sr3=a*4+b*4+c*4+d*4;  //8 operations, 4 multiplications, 3 additions, 1 assignment
sr4=a*5+b*5+c*5+d*5;  //8 operations, 4 multiplications, 3 additions, 1 assignment
}
printf("%d, %d, %d, %d\n",sr1,sr2,sr3,sr4);
pthread_mutex_unlock(&mutex);
pthread_exit(NULL);

//Total no of operations = 34
}


void* calculate_SPOPS(void *arg) {
int a=10,b=20,c=30,d=40;
int ir1,ir2,ir3,ir4;
int i;
pthread_mutex_lock(&mutex);
for(i=0;i<OPERATIONS_PER_THREAD;i++) //2 operations, 1 comparison and 1 addition
{
ir1=a*2+b*2+c*2+d*2;  //8 operations, 4 multiplications, 3 additions, 1 assignment
ir2=a*3+b*3+c*3+d*3;  //8 operations, 4 multiplications, 3 additions, 1 assignment
ir3=a*4+b*4+c*4+d*4;  //8 operations, 4 multiplications, 3 additions, 1 assignment
ir4=a*5+b*5+c*5+d*5;  //8 operations, 4 multiplications, 3 additions, 1 assignment
}
printf("%d, %d, %d, %d\n",ir1,ir2,ir3,ir4);
pthread_mutex_unlock(&mutex);
pthread_exit(NULL);

//Total no of operations = 34
}


void* calculate_DPOPS(void *arg) {
double a=10.0,b=20.0,c=30.0,d=40.0;
double dr1,dr2,dr3,dr4;
int i;
pthread_mutex_lock(&mutex);
for(i=0;i<OPERATIONS_PER_THREAD;i++)  //2 operations, 1 comparison and 1 addition
{
dr1=a*2.1+b*2.2+c*2.3+d*2.4;  //8 operations, 4 multiplications, 3 additions, 1 assignment
dr2=a*3.1+b*3.2+c*3.3+d*3.4;  //8 operations, 4 multiplications, 3 additions, 1 assignment
dr3=a*4.1+b*4.2+c*4.3+d*4.4;  //8 operations, 4 multiplications, 3 additions, 1 assignment
dr4=a*5.1+b*5.2+c*5.3+d*5.4;  //8 operations, 4 multiplications, 3 additions, 1 assignment
}
printf("%f, %f, %f, %f\n",dr1,dr2,dr3,dr4);
pthread_mutex_unlock(&mutex);
pthread_exit(NULL);

//Total no of operations = 34
}

void get_operations_per_second()
{
pthread_t thread_id[THREAD_COUNT];
int thread[THREAD_COUNT]; int i;
/* note time before starting the threads */
gettimeofday(&start_time, NULL );
/* start threads */
if(strcmp("QP", PRECISION)==0){
for (i=0;i<THREAD_COUNT;i++) {
          thread[i] = pthread_create(&(thread_id[i]),NULL,calculate_QPOPS,NULL);
}
}
else if(strcmp("HP", PRECISION)==0){
for (i=0;i<THREAD_COUNT;i++) {
          thread[i] = pthread_create(&(thread_id[i]),NULL,calculate_HPOPS,NULL);
}
}
else if(strcmp("SP", PRECISION)==0){
for (i=0;i<THREAD_COUNT;i++) {
          thread[i] = pthread_create(&(thread_id[i]),NULL,calculate_SPOPS,NULL);
}
}
else if(strcmp("DP", PRECISION)==0){
for (i=0;i<THREAD_COUNT;i++) {
          thread[i] = pthread_create(&(thread_id[i]),NULL,calculate_DPOPS,NULL);
}
}

/* join threads */
for (i=0;i<THREAD_COUNT;i++) {
      pthread_join(thread_id[i], NULL);
}

/* note time after joining the threads*/
gettimeofday(&end_time, NULL);

/*Total time of execution in seconds*/
TOTAL_TIME = (end_time.tv_sec+(end_time.tv_usec/1000000.0))-(start_time.tv_sec+(start_time.tv_usec/1000000.0));
//printf("TOTAL_TIME %f \n",TOTAL_TIME);
if (strcmp("QP", PRECISION)==0) {
PROCESSOR_SPEED = (TRILLION*10)/TOTAL_TIME;
} else if (strcmp("HP", PRECISION)==0){
PROCESSOR_SPEED = (TRILLION*34)/TOTAL_TIME;
}else if (strcmp("SP", PRECISION)==0){
PROCESSOR_SPEED = (TRILLION*34)/TOTAL_TIME;
}else  if (strcmp("DP", PRECISION)==0){
PROCESSOR_SPEED = (TRILLION*34)/TOTAL_TIME;}
//printf("PROCESSOR_SPEED %Lf \n",PROCESSOR_SPEED);
MyCPUBenchValue = PROCESSOR_SPEED/1000000000.0;
//printf("MyCPUBenchValue %Lf \n", MyCPUBenchValue);
efficiency = (MyCPUBenchValue*100)/TheoreticalValue;

printf("\n Workload: %s Concurrency: %d MyCPUBenchValue: %.2Lf GigaOPS TheoreticalValue: %.2f Efficiency: %.2f\n",PRECISION,THREAD_COUNT,MyCPUBenchValue,TheoreticalValue,efficiency);
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
                PRECISION=inputs[0];
		THREAD_COUNT = atoi(inputs[1]);
		OPERATIONS_PER_THREAD = TRILLION/THREAD_COUNT;
                get_operations_per_second();
	}
	return 0;
}

