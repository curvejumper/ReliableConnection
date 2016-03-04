/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reliableconn;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.ConnectLinux;
import network.Network;

/**
 *
 * @author michael
 */
public class ReliableConn {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         ConnectLinux connectLinux = new ConnectLinux();
         connectLinux.connectWifi("localhost", 2222);
         Network network = null;
        try {
            network = new Network(connectLinux);
        } catch (IOException ex) {
            System.out.println("Could not get network object working");
            Logger.getLogger(ReliableConn.class.getName()).log(Level.SEVERE, null, ex);
        }
        //test network closure
        System.out.println("Force closing network");
        network.close();
    }
    
}
