all:
	mkdir -p bin
	javac -d bin src/databases/SQLiteJavaDemo.java

run:
	java -cp bin:lib/sqlite-jdbc-3.8.11.2.jar:lib/mariadb-java-client-2.1.1.jar databases.SQLiteJavaDemo $(db)
	
clean:
	rm -rf bin
