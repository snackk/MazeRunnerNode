package com.mazerunner.node;

import com.mazerunner.node.ws.MazeRunnerImpl;
import com.mazerunner.node.ws.MazeRunnerProxyConnection;

import javax.xml.ws.Endpoint;
import java.util.concurrent.Executors;

public class Publisher {

    private static MazeRunnerProxyConnection mazeRunnerProxyConnection;
    private static final String port = "8888";

    /* Defined on /etc/hosts */
    public static String loadBalancerIP = "loadbalancer.local";
    private static final int threadsNumber = 20;

    public static void main(String[] args) {
    	Endpoint endpoint = Endpoint.create(new MazeRunnerImpl());

        if(mazeRunnerProxyConnection == null)
                mazeRunnerProxyConnection = new MazeRunnerProxyConnection();

        String myIp = "";

        if(args.length > 0)
            myIp = args[0];

        try{
            myIp = mazeRunnerProxyConnection.sendIpBeacon(myIp);
        }catch(Exception e){
            System.err.println(e.toString());
        }

        System.out.println("Publishing on: http://" + myIp + ":" + port + "/MazeRunnerNodeWS?wsdl");
        endpoint.setExecutor(Executors.newFixedThreadPool(threadsNumber));
        endpoint.publish("http://" + myIp + ":" + port + "/MazeRunnerNodeWS");
    }

}
