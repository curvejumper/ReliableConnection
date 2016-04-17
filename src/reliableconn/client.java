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
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;
import static java.lang.Thread.sleep;

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
        
        BluetoothProtocol bP2 = new BluetoothProtocol();
        WifiProtocol wP2 = new WifiProtocol();
        
        wP.addObserver(network1);
        bP.addObserver(network1);
        
        wP2.addObserver(network2);
        bP2.addObserver(network2);
        
        System.out.println("Attemting to connect...");
        wP.connect("localhost", 8888);
        wP2.connect("localhost", 8888);
        System.out.println("Connected to server!");
        
        //start two new threads for receving messages
        
    }
    
    
    
    //    private class msgCheck extends Thread{
//
//        public msgCheck() {
//        }
//        
//        @Override
//        public void run(){
//            try {
//                while(getInputStream.available() > 0){
//                    if(messages[index] == null){
//                        messages[index] = getInputStream.readUTF();
//                        index++;
//                    }
//                }
//            } catch (IOException ex) {
//                Logger.getLogger(WifiProtocol.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
    
}
