find -name "*.java" > javafiles.txt
javac -cp /home/ec2-user/instrumentation/:/home/ec2-user/worker/src/main/java/:/home/ec2-user/aws-java-sdk-1.11.328/lib/aws-java-sdk-1.11.328.jar:/home/ec2-user/aws-java-sdk-1.11.328/third-party/lib/*:httpclient-4.5.5.jar:httpcore-4.4.9.jar:. @javafiles.txt
