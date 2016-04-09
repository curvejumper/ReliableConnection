/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reliableconn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.BluetoothProtocol;
import network.Network;
import network.WifiProtocol;

/**
 *
 * @author michael
 */
public class client {
    
    public static void main(String args[]){
        
        System.out.println("Generating network");
        
        Network network1 = new Network();
        try {
            sleep((long) 100);
        } catch (InterruptedException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
        Network network2 = new Network();
        
        System.out.println("Observe protocols");
        
        BluetoothProtocol bP = new BluetoothProtocol();
        WifiProtocol wP = new WifiProtocol();
        
        wP.addObserver(network1);
        bP.addObserver(network1);
        
        wP.addObserver(network2);
        bP.addObserver(network2);
        
        System.out.println("Attemting to connect...");
        wP.connect("localhost", 8888);

        System.out.println("Connected to server!");
        
        
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        try {
            //TODO: need to put receive in another thread
            while (network1.recieve().available() > 0) {
                String inputLine = network1.recieve().readUTF();
                if (inputLine.equals("Bye"))
                    break;
                System.out.println(inputLine);
                out.writeUTF(inputLine);
                out.flush();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
