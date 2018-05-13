CC = gcc
CFLAGS = -Wall -lpthread -O0 -mavx2

clean:
	rm -rf *.o MyCPUBench

all: clean
	$(CC) $(CFLAGS) -pthread MyCPUBench.c -o MyCPUBench

