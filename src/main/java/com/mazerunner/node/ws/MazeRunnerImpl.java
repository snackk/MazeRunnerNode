package com.mazerunner.node.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.jws.WebService;

@WebService(endpointInterface = "com.mazerunner.node.ws.MazeRunnerService")
public class MazeRunnerImpl implements MazeRunnerService {

    private Map<String, String> mapQuery;
    private final String mazeRunnerJarLocation = System.getProperty("user.dir").toString() + "/src/main/java/com/mazerunner/node/worker/";

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

        System.out.println("========================================================");
        System.out.println("Request to render query: " + uriQuery);

        try {
            Files.createFile(Paths.get(mazeRunnerJarLocation + mapQuery.get(paramsType.maze_file_output_html.toString()) + ".html"));
        } catch (IOException ignore) {
        }

        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("java -Djava.awt.headless=true -jar " +
                    mazeRunnerJarLocation + "MazeRunner.jar" + " " +
                    mapQuery.get(paramsType.x_start.toString()) + " " +
                    mapQuery.get(paramsType.y_start.toString()) + " " +
                    mapQuery.get(paramsType.x_final.toString()) + " " +
                    mapQuery.get(paramsType.y_final.toString()) + " " +
                    mapQuery.get(paramsType.velocity.toString()) + " " +
                    mapQuery.get(paramsType.strategy.toString()) + " " +
                    mazeRunnerJarLocation + mapQuery.get(paramsType.maze_file_input.toString()) + ".maze " +
                    mazeRunnerJarLocation + mapQuery.get(paramsType.maze_file_output_html.toString()) + ".html ");

        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(mazeRunnerJarLocation + mapQuery.get(paramsType.maze_file_output_html.toString()) + ".html"))) {

            lines = br.lines().collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        String responseData = "";
        for(String line : lines) {
            responseData += "\n" + line;
        }

        System.out.println("Finished rendering: " + uriQuery);
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
