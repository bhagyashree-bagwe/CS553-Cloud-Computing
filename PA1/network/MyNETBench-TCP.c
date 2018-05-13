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

long ONE_GB = 1000000000;
//long ONE_GB = 1000000;
char * client_location;
char * server_location;
char * CONNECTION_TYPE;
char * NODE_TYPE;
int THREAD_COUNT=1;
int BLOCK_SIZE=1;
long BLOCK_COUNT=1;
long WORKLOAD_PER_THREAD=1;

struct timeval start_time, end_time;

void* start_TCP_Server(void* arg) {

	server_location = (char *)malloc(ONE_GB);
	memset(server_location, 0, ONE_GB);
	char buffer[BUFF_SIZE];

	int sockfd, newfd, rc;

	struct addrinfo hints, *res;
	struct sockaddr_storage clt;

	socklen_t addrlen;

	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_flags = AI_PASSIVE; 

	memset(&hints, 0, sizeof hints);

	rc = getaddrinfo("127.0.0.1", "11156", &hints, &res);

	if (rc != 0) {
	printf("Could not get address information!\n");
	return ERROR;
	}

	sockfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);

	if (sockfd < 0) {
	printf("Could not create socket!\n");
	return ERROR;
	}

	if (bind(sockfd, res->ai_addr, res->ai_addrlen) < 0) {
	printf("Could not bind socked!");
	close(sockfd);
	return ERROR;
	}

	if (listen(sockfd, 10) == -1) {
	printf("Could not listen to socket!");
	close(sockfd);
	return ERROR;
	}

	newfd = accept(sockfd, (struct sockaddr *) &clt, &addrlen);

	if (newfd < 0) {
	printf("Could not accept client!\n");
	close(sockfd);
	return ERROR;
	}

	for(int j=0;j<100;j++){
	for(int i=0;i<BLOCK_COUNT;i++){
	//printf("server receiving.. %d \n", j);
	rc = recv(newfd, server_location, BLOCK_SIZE, 0);
	strcpy(buffer, "OK");
	if(rc>0){
	send(newfd, &buffer[0], BUFF_SIZE, 0);//sending acknowledgement
	}  
	}
	}

	freeaddrinfo(res);
	close(newfd);
	close(sockfd);
	pthread_exit(NULL);
}


void* start_TCP_client(void* arg) {
	
	client_location = (char *)malloc(ONE_GB);
	memset(client_location, 'A', ONE_GB);
	char buffer[BUFF_SIZE];

	int sockfd, rc;
	struct addrinfo hints, *res;
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_flags = AI_PASSIVE; 

	memset(&hints, 0, sizeof hints);

	rc = getaddrinfo("127.0.0.1", "11156", &hints, &res);
	if (rc != 0) {
	printf("Could not get address information!\n");
	return ERROR;
	}
	sockfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);
	if (sockfd < 0) {
	printf("Could not create socket! \n");
	return ERROR;
	}
	rc = connect(sockfd, res->ai_addr, res->ai_addrlen);
	if (rc < 0) {
	printf("Could not connect to server!\n");
	return ERROR;
	}

	for(int j=0;j<100;j++){
	for(int i=0;i<BLOCK_COUNT;i++){
	//printf("client sending.. %d\n",j);
	rc = send(sockfd, client_location, BLOCK_SIZE, 0);
	memset(buffer, 0, BUFF_SIZE);
	//printf("client receiving.. %d\n",j);
	recv(sockfd, &buffer[0], BUFF_SIZE, 0);
	//printf("%s", buffer);
	}
	}
	
	freeaddrinfo(res);
	close(sockfd);
	pthread_exit(NULL);
}



int main(int argc, char *argv[]) { 
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

		if(strcmp(NODE_TYPE,"S")==0 && strcmp(CONNECTION_TYPE,"TCP")==0) {
			for(int i=0;i<THREAD_COUNT;i++){
			thread[i] = pthread_create(&(thread_id[i]),NULL,start_TCP_Server,NULL);
			}
			for (i=0;i<THREAD_COUNT;i++) {
			pthread_join(thread_id[i], NULL);
			}		
		}
		else if(strcmp(NODE_TYPE,"C")==0 && strcmp(CONNECTION_TYPE,"TCP")==0) {
			gettimeofday(&start_time, NULL );
			for(int i=0;i<THREAD_COUNT;i++){
			thread[i] = pthread_create(&(thread_id[i]),NULL,start_TCP_client,NULL);
			}
			for (i=0;i<THREAD_COUNT;i++) {
			pthread_join(thread_id[i], NULL);
			}
			gettimeofday(&end_time, NULL);	
			double TOTAL_TIME = (end_time.tv_sec+(end_time.tv_usec/1000000.0))-(start_time.tv_sec +(start_time.tv_usec/1000000.0));	

			double throughput = (ONE_GB * 100 * 8) /(1000000.0 * TOTAL_TIME);
			double latency = (TOTAL_TIME * 1000) / (ONE_GB * 100);
			printf("%d threads: the latency is %10.9f ms and the throughput is %10f Mb/s\n", THREAD_COUNT,  latency, throughput);	
		}
		
	}  
	return 0; 
}
