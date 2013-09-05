LIB_JARS=`find -L lib/ -name "*.jar" | tr [:space:] :`

compile:
	mkdir -p classes
	javac -sourcepath src -classpath $(LIB_JARS) -d classes `find -L -name "*.java"`

run:
	java -cp $(LIB_JARS):classes peersim.Simulator example/config-example1.txt

all: compile run

clean: 
	rm -fr classes
