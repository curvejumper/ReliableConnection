/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import protocol.Protocol;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public void println(String msg){
        for(Protocol protocol : protocolList){
            PrintWriter tempPrintWriter = new PrintWriter(protocol.getOutputStream(), true);
            tempPrintWriter.println(msg);
        }
    }
    
    public PrintWriter getBestOutputStream(){
        if(getBestProtocol().hasChanged() || printWriter == null){
            printWriter = new PrintWriter(getBestProtocol().getOutputStream(), true);
            return printWriter;
        } else {
           return checkPrintWriter(printWriter);
        }
    }
    
    
    public String readLine() {
        for(Protocol protocol : protocolList){
            BufferedReader tempBufferedReader = new BufferedReader(new InputStreamReader(
                        protocol.getInputStream()));
            try {
                    return tempBufferedReader.readLine();
            } catch (IOException ex) {
                System.err.println("Caught IOException on " + protocol.toString() + " msg: " + ex.getMessage());
//                Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    /**
     * 
     * @return Best protocol for sending data from client to server
     */
    public BufferedReader getBestInputStream(){
        if(getBestProtocol().hasChanged() || bufferedReader == null){
            bufferedReader = new BufferedReader(new InputStreamReader(
                        getBestProtocol().getInputStream()));
            return bufferedReader;
        } else {
            //make sure stream is still connected
            return checkBufferedReader(bufferedReader);
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
        //protocolList.stream().forEach((protocol) -> {
        //protocol.connect();
        //});
        for(int i = 0; i < protocolList.size(); i++){
            protocolList.get(i).connect();
        }
        
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

    private PrintWriter checkPrintWriter(PrintWriter printWriter) {
        return printWriter.checkError() ? new PrintWriter(getBestProtocol().getOutputStream(), true) : printWriter;
    }

    private BufferedReader checkBufferedReader(BufferedReader bufferedReader) {
        try { 
             if(bufferedReader.ready()){
                 return bufferedReader;
             } else {
                 return new BufferedReader(new InputStreamReader(
                        getBestProtocol().getInputStream()));
             }
        } catch (IOException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
            return new BufferedReader(new InputStreamReader(
                        getBestProtocol().getInputStream()));
        }
    }
    
}
