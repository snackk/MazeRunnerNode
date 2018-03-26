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

```sh
$ mvn clean install
```

### AWS

```sh
$ echo "[Private IP of loadbalancer machine on AWS] loadbalancer.local" | sudo tee -a /etc/hosts
```

```sh
$ mvn clean install
```

## n th time building

```sh
$ mvn compile
```

### Run
```sh
$ mvn exec:java
```

  Written by Diogo Santos.
