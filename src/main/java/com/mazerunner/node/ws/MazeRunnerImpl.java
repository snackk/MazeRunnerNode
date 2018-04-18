package com.mazerunner.node.ws;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

@WebService(endpointInterface = "com.mazerunner.node.ws.MazeRunnerService")
public class MazeRunnerImpl implements MazeRunnerService {

    private Map<String, String> mapQuery;
    private final String mazeRunnerJarLocation = System.getProperty("user.dir").toString() + "/src/main/java/com/mazerunner/node/worker/";

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
        System.out.println("========================================================");
        System.out.println("Request to solve query: " + uriQuery);
        try {

            Files.createFile(Paths.get(mazeRunnerJarLocation + mapQuery.get(paramsType.m.toString()) + ".html"));
        } catch (IOException ignore) {
        }

        Process proc = null;
        try {
            String execString = "java -jar " +
                    mazeRunnerJarLocation + "MazeRunner.jar" + " " +
                    mapQuery.get(paramsType.x0.toString()) + " " +
                    mapQuery.get(paramsType.y0.toString()) + " " +
                    mapQuery.get(paramsType.x1.toString()) + " " +
                    mapQuery.get(paramsType.y1.toString()) + " " +
                    mapQuery.get(paramsType.v.toString()) + " " +
                    mapQuery.get(paramsType.s.toString()) + " " +
                    mazeRunnerJarLocation + mapQuery.get(paramsType.m.toString()) + ".maze " +
                    mazeRunnerJarLocation + mapQuery.get(paramsType.m.toString()) + ".html ";
            proc = Runtime.getRuntime().exec(execString);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String responseData = "";
        try {
            String filename = mazeRunnerJarLocation + mapQuery.get(paramsType.m.toString()) + ".html";
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
}

