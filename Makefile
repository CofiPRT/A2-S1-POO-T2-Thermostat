JFLAGS = -g
JC = javac
JVM= java

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = timerecord/Entry.java \
		timerecord/TimeInterval.java \
		timerecord/DataRecord.java \
		thermostat/Room.java \
		thermostat/House.java \
		thermostat/MainClass.java
		
MAIN = thermostat/MainClass

default: classes

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	$(JVM) $(MAIN)
	
clean:
	$(RM) *.class