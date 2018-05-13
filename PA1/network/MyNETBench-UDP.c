#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <errno.h>

#define BUFF_SIZE 64
#define ERROR -1

//long ONE_GB = 1000000000;
long ONE_GB = 1000000;
char * client_location;
char * server_location;
char * CONNECTION_TYPE;
char * NODE_TYPE;
int THREAD_COUNT=1;
int BLOCK_SIZE=1;
long BLOCK_COUNT=1;
long WORKLOAD_PER_THREAD=1;

struct timeval start_time, end_time;

//FILL ME IN

void* start_UDP_Server(void* arg) {
	printf("Inside start_UDP_Server...\n");
	server_location = (char *)malloc(ONE_GB);
	memset(server_location, 0, ONE_GB);
	char buffer[BUFF_SIZE];

	int sock_server, s_bind;
	struct sockaddr_in server_addr;
        struct sockaddr_in client_addr;

	server_addr.sin_family = AF_INET;
	server_addr.sin_addr.s_addr = inet_addr("127.0.0.1");
	server_addr.sin_port = htons(4567);
	int size_sockaddr = sizeof(struct sockaddr_in);
	sock_server = socket(AF_INET, SOCK_DGRAM, 0);
	if(sock_server == -1)
	{
		exit(-1);
	}

	s_bind = bind(sock_server, (struct sockaddr *)&server_addr, sizeof(struct sockaddr));
	if(s_bind == -1)
	{
		exit(-1);
	}
	for(int j=0;j<100;j++){
	for(int i=0;i<BLOCK_COUNT;i++){
	int rc = recvfrom(sock_server, server_location, BLOCK_SIZE, 0, (struct sockaddr *)&client_addr, &size_sockaddr);
	if(rc>0){
	strcpy(buffer, "OK");	
	sendto(sock_server, &buffer[0], BUFF_SIZE, 0, (struct sockaddr *)&(client_addr), sizeof(struct sockaddr));
	}	
	}
	}

	pthread_exit(NULL);
}


void* start_UDP_client(void* arg) {
	printf("Inside start_UDP_client..\n");
	client_location = (char *)malloc(ONE_GB);
	memset(client_location, 'A', ONE_GB);
	char buffer[BUFF_SIZE];
	int rc;
	struct sockaddr_in server_addr;
	server_addr.sin_family = AF_INET;
	server_addr.sin_addr.s_addr = inet_addr("127.0.0.1");
	server_addr.sin_port = htons(4567);


	int size_sockaddr = sizeof(struct sockaddr_in);
	int sock_client = socket(AF_INET, SOCK_DGRAM, 0);
	if(sock_client == -1)
	{
		exit(-1);
	}
	for(int j=0;j<100;j++){
	for(int i=0;i<BLOCK_COUNT;i++){
	sendto(sock_client, client_location, BLOCK_SIZE, 0, (struct sockaddr *)&(server_addr), sizeof(struct sockaddr));
	memset(buffer, 0, BUFF_SIZE);	
	recvfrom(sock_client, &buffer[0], BUFF_SIZE, 0, (struct sockaddr *)&server_addr, &size_sockaddr);
	printf("%s", buffer);	
	}
	}	
	pthread_exit(NULL);
}


int main(int argc, char **argv)
{
	printf("main.."); 
	FILE *file = fopen(argv[1], "r");
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

                CONNECTION_TYPE = inputs[0];
		BLOCK_SIZE = atoi(inputs[1]);
		THREAD_COUNT = atoi(inputs[2]);
		WORKLOAD_PER_THREAD=ONE_GB/THREAD_COUNT;
		BLOCK_COUNT = WORKLOAD_PER_THREAD/BLOCK_SIZE;
		NODE_TYPE =argv[2];

		pthread_t thread_id[THREAD_COUNT];
		int thread[THREAD_COUNT]; int i;

		if(strcmp(NODE_TYPE,"S")==0 && strcmp(CONNECTION_TYPE,"UDP")==0) {
			printf("matching if case.. 1\n");
			for(int i=0;i<THREAD_COUNT;i++){
			thread[i] = pthread_create(&(thread_id[i]),NULL,start_UDP_Server,NULL);
			}
			for (i=0;i<THREAD_COUNT;i++) {
			pthread_join(thread_id[i], NULL);
			}
		}
		else if(strcmp(NODE_TYPE,"C")==0 && strcmp(CONNECTION_TYPE,"UDP")==0) {
			printf("matching if case.. 2\n");
			gettimeofday(&start_time, NULL );
			for(int i=0;i<THREAD_COUNT;i++){
			thread[i] = pthread_create(&(thread_id[i]),NULL,start_UDP_client,NULL);
			}
			for (i=0;i<THREAD_COUNT;i++) {
			pthread_join(thread_id[i], NULL);
			}
			gettimeofday(&end_time, NULL);	
			double TOTAL_TIME = (end_time.tv_sec+(end_time.tv_usec/1000000.0))-(start_time.tv_sec +(start_time.tv_usec/1000000.0));	

			double throughput = (ONE_GB * 100 * 8) /(1000000.0 * TOTAL_TIME); //Megabits/sec
			double latency = (TOTAL_TIME * 1000) / (ONE_GB * 100);
			printf("%d threads: the latency is %10.9f ms and the throughput is %10f Mb/s\n", THREAD_COUNT,  latency, throughput);	
		}
	}  
    return 0;
}
