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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * 
 * 
 * @author michael
 */
public class WifiProtocol extends Protocol{

    private ServerSocket serverSocket;
    private Socket socket;
    private String ipAddress;
    private int port;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private String[] messages;
    private int index;
    
    
    public WifiProtocol(){
        messages = new String[100];
        index = 0;
    }
    
    @Override
    //returns a socket status when the socket is 
    //connected to the server or not. If the socket is closed
    //then treat it as not active
    public boolean status() {
        return socket.isConnected() && !socket.isClosed();
    }
    
    private static final String CRLF = "\r\n"; // newline

    @Override
    public void send(String msg) {
         try {
            outputStream.writeUTF(msg);
            outputStream.flush();
        } catch (IOException ex) {
            notifyObservers(ex);
        }
    }

    @Override
    public String[] receive() {
        String[] message = new String[index];
        System.arraycopy(messages, 0, message, 0, index);
        Arrays.fill(messages, null);
        return message;
    }

    @Override
    public Object connect() {
        try {
            socket = new Socket(ipAddress, port);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            
            if(status()){
                //creates thread that checks messages in the network
                new msgCheck().start();
                notifyObservers(this);
            }
        } catch (IOException ex) {
            Logger.getLogger(WifiProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        return socket;
    }
    
    public void connect(String ipAddress, int port){
        this.ipAddress = ipAddress;
        this.port = port;
        connect();
    }

    @Override
    public void Close() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(WifiProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class msgCheck extends Thread{

        public msgCheck() {
        }
        
        @Override
        public void run(){
            try {
                while(inputStream.available() > 0){
                    if(messages[index].isEmpty()){
                        messages[index] = inputStream.readUTF();
                        index++;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(WifiProtocol.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
