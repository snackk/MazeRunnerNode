package com.mazerunner.node.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface MazeRunnerService {

    @WebMethod
    public String solveMaze(String uriQuery);
}
