/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author micha
 */
public class Network implements Observer{

    LinkedList<Protocol> protocolList;
    PrintWriter printWriter = null;
    BufferedReader bufferedReader = null;
    
    public Network(){
        protocolList = new LinkedList();
    }
    
    private boolean lock = false;
    
    public PrintWriter getOutputStream(){
        if(getBestProtocol().hasChanged() || printWriter == null){
            printWriter = new PrintWriter(getBestProtocol().getOutputStream(), true);
            return printWriter;
        } else {
            return printWriter;
        }
    }
    
    public BufferedReader getInputStream(){
        if(getBestProtocol().hasChanged() || bufferedReader == null){
            bufferedReader = new BufferedReader(new InputStreamReader(
                        getBestProtocol().getInputStream()));
            return bufferedReader;
        } else {
            return bufferedReader;
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {
        add((Protocol) arg);
    }

    private Protocol getBestProtocol() {
        for(Protocol protocol : protocolList){
            if(protocol.status()){
                System.out.println("Using: " + protocol.toString());
                return protocol;
            }
        }
        //no protocol found, must mean none are connected
        //need to connect to protocol
        protocolList.stream().forEach((protocol) -> {
            protocol.connect();
        });
        
        //connection has been attempted again, try to connect to one
        if(!lock){
           lock = !lock; 
           getBestProtocol(); 
        }
        return null;
    }

    private void add(Protocol protocol) {
        if(!protocolList.contains(protocol)){
            protocolList.add(protocol);
        }
    }
    
}
