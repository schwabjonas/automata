# To see commands without executing use "prompt> make --just-print <target>

JCFLAGS = -g
JC = javac
JRFLAGS = -ea
JR = java
.SILENT: clean veryclean

clean:
	$(RM) *.class

veryclean:
	$(RM) *.class *~ *#

%.class: %.java
	$(JC) $(JCFLAGS) $*.java

# Test cases
# -h RM works
# -h MNNLML
# -h LMNUMLS
# -h RSNUSRM  # works as SNUSRLR
# -h RRLLLRLLLRRR # Wikipedia does not show hex grid, but sqr grid
# Sequence of moves NRSULM
# Standard flags -s 100 -c 10 -t 200 -d 100
# Small individual moves -s 10 -c 10 -t 1 -d 100
#$(JR) $(JRFLAGS) $* -h SNUSRLR -s 100 -c 10 -t 300 -d 100
#$(JR) $(JRFLAGS) $* -h NNMLML -s 150 -c 6 -t 300 -d 100
#$(JR) $(JRFLAGS) $* -h MUNR -s 100 -c 6 -t 300 -d 100
#$(JR) $(JRFLAGS) $* -h LNULMSM -s 100 -c 6 -t 300 -d 100
#$(JR) $(JRFLAGS) $* -h SNUSRLR -s 100 -c 10 -t 300 -d 100
#$(JR) $(JRFLAGS) $* -h LNNMLM -s 293 -c 3 -m -b
#$(JR) $(JRFLAGS) $*  -h SLS -s 50 -c 10 -d 100 -e -f

%.x: %.class
	$(JR) $(JRFLAGS) $*  -h SNUMLLMUNSRRLRSRNLNSNMLMSMR -s 50 -c 10 -d 100 -e -f

sqr: Machina.class
	$(JR) $(JRFLAGS) Machina -q

green: Machina.class
	$(JR) $(JRFLAGS) Machina -g

hex: Machina.class
	$(JR) $(JRFLAGS) Machina -h NNN

life: Machina.class
	$(JR) $(JRFLAGS) Machina -l






TESTSOURCES := $(wildcard *Test.java)
TESTS := $(patsubst %.java, %.x, $(TESTSOURCES))
testall: $(TESTS)
	#echo $(TESTS)
	#echo $(TESTSOURCES)

SOURCES := $(wildcard *.java)
ARCHIVEALLFILES := $(SOURCES) Makefile
archiveall:
	tar -cf archiveall.tar $(ARCHIVEALLFILES)

extractall:
	tar -xf archiveall.tar
