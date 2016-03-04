/**
 * This class is used to check if the connection to a network is valid
 * This will also try to reconnect to a network and 
 */
package network;

import java.net.Socket;

/**
 *
 * @author michael
 */
public interface ConnectionManager {
    
    public boolean connectWifi(String server, int port);
    
    public Socket socket();
    
    public Object connectBluetooth();

    public void reConnect();
}
