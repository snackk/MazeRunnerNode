package com.mazerunner.node.ws;

import com.mazerunner.node.Publisher;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;


public class MazeRunnerProxyConnection {

    public String sendIpBeacon(String defaultIP) throws Exception {

        String url = "http://" + Publisher.loadBalancerIP + ":8080/r_ip.html";
        String myIp = !defaultIP.equals("") ? defaultIP : getMyIp();

        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("ip", myIp));
        String paramString = URLEncodedUtils.format(params, "UTF-8");

        URLConnection connection = new URL(url + "?" + paramString).openConnection();

        connection.setRequestProperty("Accept-Charset", "UTF-8");
        if ( connection instanceof HttpURLConnection)
        {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            if(httpConnection.getResponseCode() == 200)
                System.out.println(myIp + " successfully registered.");
        }
        return myIp;
    }

    private String getMyIp() throws Exception{

        String myIp = "";

        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (!networkInterface.isUp()) {
                continue;
            }

            if(!networkInterface.toString().contains("lo")){
                //System.out.println(String.format("networkInterface: %s", networkInterface.toString()));

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    int npf = interfaceAddress.getNetworkPrefixLength();
                    InetAddress address = interfaceAddress.getAddress();
                    InetAddress broadcast = interfaceAddress.getBroadcast();

                    if (broadcast != null && npf != 8) {
                        myIp = address.toString().replaceAll("/", "");
                    }
                }
            }
        }
        return myIp;
    }
}