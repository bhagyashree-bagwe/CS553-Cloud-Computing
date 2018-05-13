CC=gcc
CFLAGS=-g -Wall -O0 -lpthread

all:
	$(CC) $(CFLAGS) -w -pthread -o MyNETBench-TCP MyNETBench-TCP.c
	$(CC) $(CFLAGS) -w -pthread -o MyNETBench-UDP MyNETBench-UDP.c

clean:
	$(RM) *.o MyNETBench-TCP MyNETBench-UDP
