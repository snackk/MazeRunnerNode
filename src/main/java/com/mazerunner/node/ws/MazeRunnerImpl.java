package com.mazerunner.node.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;

@WebService(endpointInterface = "com.mazerunner.node.ws.MazeRunnerService")
public class MazeRunnerImpl implements MazeRunnerService {

    private Map<String, String> mapQuery;
    /*TODO Hardcoded to work on AWS*/
    private final String raytracerLocation = "/home/ec2-user/cnuv/MazeRunnerNode/src/main/java/com/mazerunner/node/worker";

    /*
     * x_start
     * y_start
     * x_final
     * y_final
     * velocity
     * strategy
     * maze_file_input
     * maze_file_output_html
     */
    private enum paramsType {x_start, y_start, x_final, y_final, velocity, strategy, maze_file_input, maze_file_output_html}

    @Override
    public String solveMaze(String uriQuery) {
        queryToMap(uriQuery);

        System.out.println("Request to render query: " + uriQuery);

        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("java -Djava.awt.headless=true -jar " +
                    raytracerLocation + "MazeRunner.jar" + " " +
                    mapQuery.get(paramsType.x_start.toString()) + " " +
                    mapQuery.get(paramsType.y_start.toString()) + " " +
                    mapQuery.get(paramsType.x_final.toString()) + " " +
                    mapQuery.get(paramsType.y_final.toString()) + " " +
                    mapQuery.get(paramsType.velocity.toString()) + " " +
                    mapQuery.get(paramsType.strategy.toString()) + " " +
                    raytracerLocation + mapQuery.get(paramsType.maze_file_input.toString()) + " " +
                    raytracerLocation + mapQuery.get(paramsType.maze_file_output_html.toString()) + " ");

        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = null;
        String responseData = "";

        try {
            while((line = br.readLine()) != null) {
                responseData += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("========================================================");
        System.out.println(responseData);
        System.out.println("========================================================");

        return uriQuery;
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
