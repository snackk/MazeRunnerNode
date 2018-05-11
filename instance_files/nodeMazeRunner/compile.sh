find -name "*.java" > javafiles.txt
javac -cp httpclient-4.5.5.jar:httpcore-4.4.9.jar:. @javafiles.txt
