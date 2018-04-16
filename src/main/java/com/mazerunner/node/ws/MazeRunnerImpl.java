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
    private enum paramsType {x0, y0, x1, y1, v, s, m}

    @Override
    public String solveMaze(String uriQuery) {
        queryToMap(uriQuery);
        System.out.println("jar location ->" + mazeRunnerJarLocation);
        System.out.println("========================================================");
        System.out.println("Request to render query: " + uriQuery);
        System.out.println(mapQuery);
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
            System.out.println(execString);
            proc = Runtime.getRuntime().exec(execString);

        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(mazeRunnerJarLocation + mapQuery.get(paramsType.m.toString()) + ".html"))) {

            lines = br.lines().collect(Collectors.toList());
            String line;
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String responseData = "";
        for(String line : lines) {
            responseData += "\n" + line;
        }

        System.out.println("Finished rendering: " + uriQuery);
        System.out.println("========================================================");
        System.out.println(responseData);

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
