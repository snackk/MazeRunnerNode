package com.mazerunner.node.ws;

import com.mazerunner.node.mss.MSSManagerNode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.lang.Thread;

import javax.jws.WebService;
import pt.ulisboa.tecnico.meic.cnv.mazerunner.maze.Main;

@WebService(endpointInterface = "com.mazerunner.node.ws.MazeRunnerService")
public class MazeRunnerImpl implements MazeRunnerService {

    private Map<String, String> mapQuery;
    private MSSManagerNode mssmanager = MSSManager.getInstance();

    private final String mazeRunnerLocation = "/home/ec2-user/worker/";
    private final String workerClasspath = "/home/ec2-user/instrumentation:" + mazeRunnerLocation + "src/main/java/";
    private final String mazeRunnerClass = "pt.ulisboa.tecnico.meic.cnv.mazerunner.maze.Main";
    private final String metricsDirectory = "/home/ec2-user/metricfiles/";
    /*
     * x0 -> initial x
     * y0 -> initial y
     * x1 -> final x
     * y1 -> final y
     * v  -> velocity
     * s  -> strategy
     * m  -> maze file name
     */
    private enum paramsType {x0, y0, x1, y1, v, s, m}

    @Override
    public String solveMaze(String uriQuery) {
        queryToMap(uriQuery);
	String mazeName = mapQuery.get(paramsType.m.toString()).split("\\.")[0];
        System.out.println("========================================================");
        System.out.println("Request to solve query: " + uriQuery);
        try {

            Files.createFile(Paths.get(mazeRunnerLocation + mazeName + ".html"));
        } catch (IOException ignore) {
        }
	
        Process proc = null;
        try {/*
            String execString = "java -cp " +
                    workerClasspath + " " + mazeRunnerClass + " " + 
                    mapQuery.get(paramsType.x0.toString()) + " " +
                    mapQuery.get(paramsType.y0.toString()) + " " +
                    mapQuery.get(paramsType.x1.toString()) + " " +
                    mapQuery.get(paramsType.y1.toString()) + " " +
                    mapQuery.get(paramsType.v.toString()) + " " +
                    mapQuery.get(paramsType.s.toString()) + " " +
                    mazeRunnerLocation + mazeName + ".maze " +
                    mazeRunnerLocation + mazeName + ".html ";
		System.out.println(execString);
            proc = Runtime.getRuntime().exec(execString);
            proc.waitFor();*/
	    
	    createMetricsFile("metrics-" + Thread.currentThread().getId(), metricsDirectory);
	    Main.main(new String[] {mapQuery.get(paramsType.x0.toString()),mapQuery.get(paramsType.y0.toString()),mapQuery.get(paramsType.x1.toString()),
				mapQuery.get(paramsType.y1.toString()),mapQuery.get(paramsType.v.toString()),
				mapQuery.get(paramsType.s.toString()), mazeRunnerLocation + mazeName + ".maze", mazeRunnerLocation + mazeName +".html"});
            mssmanager.putMetric(Thread.currentThread().getId());            
	} catch (Exception e) {
            e.printStackTrace();
        }

        String responseData = "";
        try {
            String filename = mazeRunnerLocation + mazeName + ".html";
            FileReader in = new FileReader(filename.toString());
            BufferedReader br = new BufferedReader(in);
            String line = br.readLine();
            
            while(line != null){
                responseData += "\n" + line;
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Finished solving: " + uriQuery);
        System.out.println("========================================================");

        return responseData;
    }

    private void queryToMap(String query){
        mapQuery = new HashMap<String, String>();

        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                mapQuery.put(pair[0], pair[1]);
            }else{
                mapQuery.put(pair[0], "");
            }
        }
    }

    private void createMetricsFile(String filename, String path) {
	try{
		Path file = Paths.get(path + filename + ".txt");
		String params = "";

		for(paramsType param : paramsType.values()) {
			params += param.toString() + "=";
			params += mapQuery.get(param.toString()) + "&";
		}
		params += "bbl=";
		Files.write(file,params.getBytes());
   	} catch(IOException e) {
		e.printStackTrace();
	}
   }
}

