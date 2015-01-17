JFLAGS=-d
JCC=javacc
JC=javac
J=java

all: clean-java clean copy-interfaces generate compile

compile:
	mkdir -p classes
	$(JC) $(JFLAGS) classes *.java

copy-interfaces:
	cp ../FinalProjectInterfaces/*.java . 

default: compile

generate:
	$(JCC) extractor.jj	

run:
	$(J) -classpath ./classes edu.ucsd.QueryParser < input.txt 

run-error:
	$(J) -classpath ./classes edu.ucsd.QueryParser < error-input.txt 


clean-java:
	rm -f *.java

clean:
	rm -rf classes/**
