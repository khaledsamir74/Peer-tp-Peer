package com.za.networking.peertopeer;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class peer {
    private static ArrayList<String> onlineIps = new ArrayList<>();
    private static ArrayList<String> portNumbers = new ArrayList<>();
	public static void main(String[] args) throws Exception {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		//System.out.println("Enter The UserName & His port Number For this Peer");
		//String[] setupValues = bufferedReader.readLine().split(" ");
        System.out.println("Enter Port Number : ");
        Scanner scanner = new Scanner(System.in);
        InetAddress inetAddress = java.net.InetAddress.getLocalHost();
        String myIp = inetAddress.getHostAddress();
        String portNumber = scanner.nextLine();
        connectTServer(myIp, portNumber);
		Serverthread serverThread = new Serverthread(portNumber);
		serverThread.start();
		new peer().updateListenToPeers(bufferedReader, myIp, serverThread);
	}
	public void updateListenToPeers(BufferedReader bufferedReader, String username, Serverthread serverThread) throws Exception{
    System.out.println("Online Ips" + ":" + "Port Number");
	for(int i = 0;i<onlineIps.size();i++){
        System.out.println(onlineIps.get(i) + ":" + portNumbers.get(i));
    }
    System.out.println("Enter Hostname : port num");
	System.out.println("peers to recieve messeges from (s to skip)");
	String input = bufferedReader.readLine();
	String[] inputValues = input.split(" ");
	for(int i = 0;i < inputValues.length; i++){
	    String [] tmp = inputValues[i].split(":");
	    if(onlineIps.contains(tmp[0]))
        {
            if(portNumbers.get(i).equals(tmp[1])){

            }else{
                System.out.println("Wrong Input!");
                return;
            }
        }else
        {
            System.out.println("Wrong Input!");
            return;
        }
    }
	if (!input.equals("s")) for (int  i = 0; i < inputValues.length; i++) {
		String[] address = inputValues[i].split(":");
		Socket socket = null;
		try {
			socket = new Socket(address[0], Integer.valueOf(address[1]));
			new Peerthread(socket).start();
		}
		catch(Exception e) {
			if (socket != null) socket.close();
			else System.out.println("Invalid input, Skipping to next step");
		}
	}
	communication(bufferedReader,username,serverThread);
}


}
