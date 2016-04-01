/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author micha
 */
public class Network implements Observer{

    List<Protocol> protocolList;
    
    private boolean lock = false;
    
    public void send(String msg){
         getBestProtocol().send(msg);
    }
    
    public String recieve(){
        String msg = null;
        
        return msg;
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
