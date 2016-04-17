/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author michael
 */
public interface ProtocolInt {
    public boolean status();
    
    public OutputStream getOutputStream();
    
    public InputStream getInputStream();
    
    public Object connect();
    
    public void Close();
}
