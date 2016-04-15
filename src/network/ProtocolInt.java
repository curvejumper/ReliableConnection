/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.InputStream;

/**
 *
 * @author michael
 */
public interface ProtocolInt {
    public boolean status();
    
    public void send(String msg);
    
    public InputStream receive();
    
    public Object connect();
    
    public void Close();
}
