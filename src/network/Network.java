/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.DataInputStream;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author micha
 */
public class Network implements Observer{

    LinkedList<Protocol> protocolList;
    
    public Network(){
        protocolList = new LinkedList();
    }
    
    private boolean lock = false;
    
    public void send(String msg){
         getBestProtocol().send(msg);
    }
    
    public DataInputStream recieve(){
        return (DataInputStream) getBestProtocol().receive();
    }
    
    @Override
    public void update(Observable o, Object arg) {
        add((Protocol) arg);
    }

    private Protocol getBestProtocol() {
        for(Protocol protocol : protocolList){
            if(protocol.status()){
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
