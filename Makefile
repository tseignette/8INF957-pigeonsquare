all: source run

source:
	javac -d bin src/*.java

run:
	java -cp bin PigeonSquare

clean:
	rm -R bin/*.class
