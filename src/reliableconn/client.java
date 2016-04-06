/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reliableconn;

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
        
        Network network = new Network();
        
        System.out.println("Observe protocols");
        
        BluetoothProtocol bP = new BluetoothProtocol();
        WifiProtocol wP = new WifiProtocol();
        
        wP.addObserver(network);
        bP.addObserver(network);
        
        System.out.println("Attemting to connect...");
        wP.connect("localhost", 8888);

        System.out.println("Connected to server!");
        
        network.send("Hello world!");
        
    }
}
