/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Observable;

/**
 *
 * @author michael
 */
public class Network extends Observable {

    private ConnectionManager connectionManager;
    private OutputStream outputStream;

    @Override
    public void notifyObservers(Object arg) {
        super.setChanged();
        super.notifyObservers(arg);
    }

    //connect to the network and notify if changes occur
    public Network(ConnectionManager cm) throws IOException {
        connectionManager = cm;
        outputStream = connectionManager.socket().getOutputStream();

        Thread receivingThread = new Thread() {
            @Override
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connectionManager.socket().getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        notifyObservers(line);
                    }
                    
                } catch (IOException ex) {
                    notifyObservers(ex);
                    connectionManager.reConnect();
                }
            }
        };
        receivingThread.start();
    }

    private static final String CRLF = "\r\n"; // newline

    /**
     * Send a line of text
     */
    public void send(String text) {
        try {
            outputStream.write((text + CRLF).getBytes());
            outputStream.flush();
        } catch (IOException ex) {
            notifyObservers(ex);
        }
    }

    /**
     * Close the socket
     */
    public void close() {
        try {
            connectionManager.socket().close();
        } catch (IOException ex) {
            notifyObservers(ex);
        }
    }

}
