/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

/**
 *
 * @author michael
 */
public interface ProtocolInt {
    public boolean status();
    
    public void send(String msg);
    
    public String receive();
    
    public Object connect();
    
    public void Close();
}
