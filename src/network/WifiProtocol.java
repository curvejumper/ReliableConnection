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
    public String receive() {
        String msg = null;
        try {
            msg = inputStream.readUTF();
        } catch (IOException ex) {
            Logger.getLogger(WifiProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        return msg;
    }

    @Override
    public Object connect() {
        try {
            socket = new Socket(ipAddress, port);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            
            if(status()){
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
    
}
