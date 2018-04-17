package com.mazerunner.node;

import com.mazerunner.node.ws.MazeRunnerImpl;
import com.mazerunner.node.ws.MazeRunnerProxyConnection;

import javax.xml.ws.Endpoint;

public class Publisher {

    private static MazeRunnerProxyConnection mazeRunnerProxyConnection;
    private static final String port = "8888";

    /* Defined on /etc/hosts */
    public static String loadBalancerIP = "localhost";//"loadbalancer.local";

    public static void main(String[] args) {
	if(mazeRunnerProxyConnection == null)
            mazeRunnerProxyConnection = new MazeRunnerProxyConnection();

        String myIp = "";

        try{
	    String defaultIp;
	    if(args.length > 0)
		defaultIp = args[0];
	    else
		defaultIp = "";
            myIp = mazeRunnerProxyConnection.sendIpBeacon(defaultIp);
        }catch(Exception e){
            System.err.println(e.toString());
        }

        System.out.println("Publishing on: http://" + myIp + ":" + port + "/MazeRunnerNodeWS?wsdl");
        Endpoint.publish("http://" + myIp + ":" + port + "/MazeRunnerNodeWS", new MazeRunnerImpl());
    }

}
