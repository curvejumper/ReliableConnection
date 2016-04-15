/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author micha
 */
public class Server extends Thread{
    
    ServerSocket server;
    
    @Override
    public void run(){
        try {
            System.out.println("Waiting for server...");
            server = new ServerSocket(8888);
            Socket dataSocket = server.accept();
            System.out.println("Connected! ");
            DataOutputStream outputStream = new DataOutputStream(dataSocket.getOutputStream());
            DataInputStream inputStream = new DataInputStream(dataSocket.getInputStream());
            String msg = inputStream.readUTF();
            System.out.println("Server: " + msg);
            outputStream.writeUTF(msg);
            outputStream.flush();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
            System.out.println("Waiting for server...");
    }
    
    public static void main(String[] args){
            (new Server()).start();
            
            WifiProtocol wifi1 = new WifiProtocol();
            
            
            wifi1.connect("localhost", 8888);
            System.out.println("Wifi1 connected");
            
            System.out.println("Wifi1 sending 'hello world'");
            wifi1.send("Hello World");
            //String message = wifi1.receive();
            //System.out.println("wifi1: " + message);
            
            WifiProtocol wifi2 = new WifiProtocol();
            wifi2.connect("localhost", 8888);
            System.out.println("Wifi2 connected");
        
            System.out.println("Wifi1 sending 'hello world'");
            wifi1.send("Hello World");
            //String message2 = wifi2.receive();
            //System.out.println("wifi2: " + message2);
    }
}
