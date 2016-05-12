/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author michael
 */
public interface ProtocolInt {
    /**
     * This method is used to check if a <code>Protocol</code> is
     * active and connected to a host. 
     * 
     * @return status of the <code>Procol</code> class
     */
    public boolean status();
    
    public OutputStream getOutputStream();
    
    public InputStream getInputStream();
    
    public Object connect();
    
    public void Close();
}
