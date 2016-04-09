/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reliableconn;

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
public class TestServer {
    public static void main(String args[]){
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("Server started!");
            while(true){
                new clientThread(serverSocket.accept()).start();
                System.out.println("Accepted client!");
            }
        } catch (IOException ex) {
            Logger.getLogger(TestServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
