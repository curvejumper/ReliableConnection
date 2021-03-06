/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reliableconn;

import protocol.BluetoothProtocol;
import protocol.WifiProtocol;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothConnectionException;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import network.Network;
import reliableconn.ChatClient;

/**
 * A multithreaded chat room server. When a client connects the server requests
 a screen name by sending the client the text "SUBMITNAME", and keeps
 requesting a name until a unique one is received. After a client submits a
 unique name, the server acknowledges with "NAMEACCEPTED". Then all messages
 from that client will be broadcast to all other clients that have submitted a
 unique screen name. The broadcast messages are prefixed with "MESSAGE ".

 Because this is just a teaching example to illustrate a simple chat server,
 there are a few features that have been left out. Two are very useful and
 belong in production code:

 1. The protocol should be enhanced so that the client can println
 clean disconnect messages to the server.

 2. The server should do some logging.
 */
public class ChatServer {

    /**
     * The port that the server listens on.
     */
    private static final int PORT = 8888;

    /**
     * The set of all names of clients in the chat room. Maintained so that we
     * can check that new clients are not registering name already in use.
     */
    private static HashSet<String> names = new HashSet<String>();

    /**
     * The set of all the print writers for all the clients. This set is kept so
     * we can easily broadcast messages.
     */
    private static HashSet<Network> writers = new HashSet<Network>();

    /**
     * The application main method, which just listens on a port and spawns
     * handler threads.
     *
     * @param args
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        String selection = serverSelection();

        switch (selection.toLowerCase()) {
            case "bluetooth":
                bluetoothServer();
                break;
            case "wifi":
                wifiServer();
                break;
            case "both":
                btWifiServer();
                break;
            default:
                serverSelection("incorrect selection, try again");
        }

    }

    private static String serverSelection() {
        return serverSelection("");
    }
    
    private static String serverSelection(String msg) {
        String[] servers = {"Bluetooth", "Wifi", "Both"};
        return (String) JOptionPane.showInputDialog(new JFrame(),
                msg + "\n"
                + "Choose Server Type:",
                "Customized Dialog",
                JOptionPane.PLAIN_MESSAGE,
                null,
                servers,
                "");
    }

    private static void btWifiServer() {
        try {
            //Create a UUID for SPP
            UUID uuid = new UUID("1101", true);
            //Create the servicve url
            String connectionString = "btspp://localhost:" + uuid + ";name=Sample SPP Server";

            //open server url
            StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier) Connector.open(connectionString);

            //Wait for client connection
            System.out.println("\nServer Started. Waiting for clients to connect…");
            //StreamConnection connection = streamConnNotifier.acceptAndOpen();

            System.out.println("The chat server is running.");
            ServerSocket listener = new ServerSocket(PORT);

            try {
                while (true) {
                    //For this project, assume that initial connection works
                    new Handler(listener.accept(), streamConnNotifier.acceptAndOpen()).start();
                }
            } finally {
                listener.close();
                streamConnNotifier.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    private static void wifiServer() {
        try {
            ServerSocket listener = new ServerSocket(PORT);
            System.out.println("Server is on and waiting");
            try {
                while (true) {
                    //For this project, assume that initial connection works
                    new Handler(listener.accept()).start();
                }
            } finally {
                listener.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void bluetoothServer() {
        try {
//          Create a UUID for SPP
            UUID uuid = new UUID("1101", true);
//          Create the servicve url
            String connectionString = "btspp://localhost:" + uuid + ";name=Sample SPP Server";

//          open server url
            StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier) Connector.open(connectionString);

//          Wait for client connection
            System.out.println("\nServer Started. Waiting for clients to connect…");
//          StreamConnection connection = streamConnNotifier.acceptAndOpen();

            System.out.println("The chat server is running.");

            try {
                while (true) {
                    //For this project, assume that initial connection works
                    new Handler(streamConnNotifier.acceptAndOpen()).start();
                }
            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                streamConnNotifier.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * A handler thread class. Handlers are spawned from the listening loop and
     * are responsible for a dealing with a single client and broadcasting its
     * messages.
     * 
     * The handler constructor is dependent solely on the network. 
     * This allows for a customizable handler thread without having to create a
     * seperate class. 
     */
    private static class Handler extends Thread {

        private String name;
        private Socket socket;
        private Network network;

        /**
         * Constructs a handler thread, squirreling away the socket. All the
         * interesting work is done in the run method.
         */
        public Handler(Socket socket, StreamConnection connection) {
            this.socket = socket;
            network = new Network();
            BluetoothProtocol bP = new BluetoothProtocol();
            WifiProtocol wP = new WifiProtocol();

            wP.addObserver(network);
            bP.addObserver(network);

            wP.connect(socket);
            try {
                bP.connect(connection);
            } catch (BluetoothConnectionException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Handler for only a server-socket connection. 
         * 
         * Accepts a java Socket client connection using TCP protocol
         * 
         * @param socket - Java socket
         */
        public Handler(Socket socket) {
            this.socket = socket;
            network = new Network();
            WifiProtocol wP = new WifiProtocol();

            wP.addObserver(network);

            wP.connect(socket);
            
            
        }

        /**
         *  Handler for only a Bluetooth connection.
         * 
         * Accepts a StreamConnection client using SPP protocol
         * 
         * @param connection - client Bluetooth device StreamConnection
         */
        public Handler(StreamConnection connection) {
            network = new Network();
            BluetoothProtocol bP = new BluetoothProtocol();

            bP.addObserver(network);
            try {
                bP.connect(connection);
            } catch (BluetoothConnectionException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Services this thread's client by repeatedly requesting a screen name
         * until a unique one has been submitted, then acknowledges the name and
         * registers the output stream for the client in a global set, then
         * repeatedly gets inputs and broadcasts them.
         */
        @Override
        public void run() {
            System.out.println("Starting new handler!");
            try {

                // Create character streams for the socket.
                //in = new BufferedReader(new InputStreamReader(
                //network.getInputStream()));
                //out = new PrintWriter(network.println(), true);
                // Request a name from this client.  Keep requesting until
                // a name is submitted that is not already used.  Note that
                // checking for the existence of a name and adding the name
                // must be done while locking the set of names.
                while (true) {
                    network.println("SUBMITNAME");
                    //System.out.println("N
                    name = network.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                            break;
                        }
                    }
                }

                // Now that a successful name has been chosen, add the
                // socket's print writer to the set of all writers so
                // this client can receive broadcast messages.
                network.println("NAMEACCEPTED");
                //network.println("NAMEACCEPTED");
                writers.add(network);

                // Accept messages from this client and broadcast them.
                // Ignore other clients that cannot be broadcasted to.
                while (true) {
                    String input = network.readLine();
                    if (input == null) {
                        return;
                    }
                    for (Network writer : writers) {
                        writer.println("MESSAGE " + name + ": " + input);
                    }
                }
            } 
//            catch (IOException e) {
//                System.out.println(e);
//            }
            finally {
                // This client is going down!  Remove its name and its print
                // writer from the sets, and close its socket.
                if (name != null) {
                    names.remove(name);
                }
                if (network.getBestOutputStream() != null) {
                    writers.remove(network);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
