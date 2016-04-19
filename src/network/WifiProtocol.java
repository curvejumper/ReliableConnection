/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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

//    private ServerSocket serverSocket;
    private Socket socket;
    private String ipAddress;
    private int port;
    private OutputStream outputStream;
    private InputStream inputStream;
//    private String[] messages;
//    private int index;
    
    
    public WifiProtocol(){
//        messages = new String[100];
//        index = 0;
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
    public OutputStream getOutputStream() {
         try {
            outputStream = socket.getOutputStream();
//            PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outputStream));
//            pWriter.write(msg + "\r\n");
//            pWriter.flush();
        } catch (IOException ex) {
            notifyObservers(ex);
        }
        return outputStream;
    }

    @Override
    public InputStream getInputStream() {
        try {
            inputStream = socket.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(BluetoothProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inputStream;
    }

    @Override
    public Object connect() {
//            socket = new Socket(ipAddress, port);
        if(status()){
            //creates thread that checks messages in the network
            System.out.println("notifying network that connection is good");
            notifyObservers(this);
        }
        return socket;
    }
    
    public void connect(String ipAddress, int port){
        this.ipAddress = ipAddress;
        this.port = port;
        try {
            socket = new Socket(ipAddress, port);
        } catch (IOException ex) {
            Logger.getLogger(WifiProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        connect();
    }
    
    public void connect(Socket socket){
        this.socket = socket;
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
    
    @Override
    public String toString(){
        return "Wifi protocol";
    }

//    private class msgCheck extends Thread{
//
//        public msgCheck() {
//        }
//        
//        @Override
//        public void run(){
//            try {
//                while(inputStream.available() > 0){
//                    if(messages[index] == null){
//                        messages[index] = inputStream.readUTF();
//                        index++;
//                    }
//                }
//            } catch (IOException ex) {
//                Logger.getLogger(WifiProtocol.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
    
}
