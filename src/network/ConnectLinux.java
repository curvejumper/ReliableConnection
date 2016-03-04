/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
public class ConnectLinux implements ConnectionManager{

    private Socket socket = null;
    private String hostAddr;
    private int port;
    
    @Override
    public boolean connectWifi(String server, int port) {
        try {
            socket = new Socket(server, port);
            hostAddr = socket.getInetAddress().getHostAddress();
            this.port = port;
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ConnectLinux.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public Socket socket() {
        return socket;
    }

    @Override
    public Object connectBluetooth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    //try to reconnet to either bluetooth or wifi
    @Override
    public void reConnect() {
        try {
            Socket tempSocket = new Socket(hostAddr, this.port);
            if(tempSocket.isConnected()){
                socket = tempSocket;
                tempSocket.close();
                System.out.println("Sucessfully reconnected");
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectLinux.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
