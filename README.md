# MazeRunnerNode

## Description

Maze solver with depth first search, breadth first search and A*.
Maze solver operates as a partially simulated compute intensive workload with some observable graphic output.
It serves as the workload to drive the project for the Cloud Computing and Virtualization course @ IST 2018

## 1st time building

### local

```sh
$ echo "127.0.0.1 loadbalancer.local" | sudo tee -a /etc/hosts
```

### AWS

```sh
$ echo "[Private IP of loadbalancer machine on AWS] loadbalancer.local" | sudo tee -a /etc/hosts
```

### Deploy
```sh
$ mvn clean package
$ scp -i path/of/pem target/MazeRunnerNode-1.0-SNAPSHOT-jar-with-dependencies.jar ec2-user@NodeIpOnAWS
```

## Run (localhost)
```sh
$ mvn clean exec:java -Dexec.args=localhost
```

## Run (AWS)
```sh
$ java -jar MazeRunnerNode-1.0-SNAPSHOT-jar-with-dependencies.jar
```

  Written by Diogo Santos.
