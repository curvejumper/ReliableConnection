/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reliableconn;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author micha
 */
class clientThread extends Thread {

    private Socket socket = null;
    
    public clientThread(Socket socket) {
        super("clientThread");
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try (
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
        ) {
            System.out.println("Created client thread!");
            String inputLine;
            while (in.available() > 0) {
                inputLine = in.readUTF();
                if (inputLine.equals("Bye"))
                    break;
                System.out.println(inputLine);
                out.writeUTF(inputLine);
                out.flush();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
