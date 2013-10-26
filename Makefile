# Makefile 
# Dongpu
# 10/25/2013

JC = javac 
JFLAGS = -g -d .

.SUFFIXES: .java .class 

.java.class: 
	$(JC) $(JFLAGS) $<

CLASSES = \
	src/edu/unl/cse/utils/Operator.java \
	src/edu/unl/cse/utils/Symbol.java \
	src/edu/unl/cse/utils/State.java \
	src/edu/unl/cse/utils/IdManager.java \
	src/edu/unl/cse/driver/InputFileReader.java \
	src/edu/unl/cse/evaluator/StringEvaluator.java \
	src/edu/unl/cse/nfa2dfa/NFA2DFAConverter.java \
	src/edu/unl/cse/postfix2nfa/Postfix2NFAConverter.java \
	src/edu/unl/cse/re2postfix/RE2PostfixConverter.java \
	src/edu/unl/cse/driver/REDriver.java


default: classes 

classes: $(CLASSES:.java=.class)

clean: 
	$(RM) *.class
