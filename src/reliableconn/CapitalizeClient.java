/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reliableconn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import network.BluetoothProtocol;
import network.Network;
import network.WifiProtocol;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.BluetoothConnectionException;

/**
 * A simple Swing-based client for the chat server. Graphically it is a frame
 * with a text field for entering messages and a textarea to see the whole
 * dialog.
 *
 * The client follows the Chat Protocol which is as follows. When the server
 * sends "SUBMITNAME" the client replies with the desired screen name. The
 * server will keep sending "SUBMITNAME" requests as long as the client submits
 * screen names that are already in use. When the server sends a line beginning
 * with "NAMEACCEPTED" the client is now allowed to start sending the server
 * arbitrary strings to be broadcast to all chatters connected to the server.
 * When the server sends a line beginning with "MESSAGE " then all characters
 * following this string should be displayed in its message area.
 */
public class CapitalizeClient implements DiscoveryListener {

    BufferedReader in;
    PrintWriter out;
    Network network;
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);

    //object used for waiting
    private static Object lock = new Object();

//vector containing the devices discovered
    private static ArrayList arrDevices = new ArrayList();
    private static String connectionURL = null;

    /**
     * Constructs the client by laying out the GUI and registering a listener
     * with the textfield so that pressing Return in the listener sends the
     * textfield contents to the server. Note however that the textfield is
     * initially NOT editable, and only becomes editable AFTER the client
     * receives the NAMEACCEPTED message from the server.
     */
    public CapitalizeClient() {

        // Layout GUI
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();

        // Add Listeners
        textField.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the textfield by sending
             * the contents of the text field to the server. Then clear the text
             * area in preparation for the next message.
             */
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
//                network.getOutputStream(textField.getText());
                textField.setText("");
            }
        });
    }

    /**
     * Prompt for and return the wifiAddress of the server.
     */
    private String getWifiServerAddress() {
        return JOptionPane.showInputDialog(
                frame,
                "Enter IP Address of the Server:",
                "Welcome to the Chatter",
                JOptionPane.QUESTION_MESSAGE);
    }

    private RemoteDevice getBtServerAddress() {

        LocalDevice localDevice;
        try {
            localDevice = LocalDevice.getLocalDevice();

            System.out.println("Address: " + localDevice.getBluetoothAddress());
            System.out.println("Name: " + localDevice.getFriendlyName());
            //find devices
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();

            System.out.println("Starting device inquiry…");
            agent.startInquiry(DiscoveryAgent.GIAC, this);

            try {
                synchronized (lock) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Device Inquiry Completed. ");

        } catch (BluetoothStateException ex) {
            Logger.getLogger(CapitalizeClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CapitalizeClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        //print all devices in arrDevices
        int deviceCount = arrDevices.size();

        if (deviceCount <= 0) {
            System.out.println("No Devices Found .");
            return null;
        } else { //print bluetooth device addresses and names in the format [ No. address (name) ] 
            System.out.println("Bluetooth Devices: ");
            String[] btDeviceNames = new String[arrDevices.size()];
            for(int i = 0; i < arrDevices.size(); i++){
                RemoteDevice btDevice = (RemoteDevice) arrDevices.get(i);
                try {
                    btDeviceNames[i] = (i + 1) + ". " + btDevice.getBluetoothAddress() + " (" + btDevice.getFriendlyName(true) + ")";
                } catch (IOException ex) {
                    Logger.getLogger(CapitalizeClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String btDevice = (String) JOptionPane.showInputDialog(frame,
                    "Alternate Protocol\n"
                    + "Choose bluetooth device:",
                    "Customized Dialog",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    btDeviceNames,
                    "");
            for(int i = 0; i < btDeviceNames.length; i++){
                if(btDeviceNames[i].equals(btDevice)){
                    return (RemoteDevice) arrDevices.get(i);
                }
            }
        }
        return null;
    }

    /**
     * Prompt for and return the desired screen name.
     */
    private String getName() {
        return JOptionPane.showInputDialog(
                frame,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Connects to the server then enters the processing loop.
     */
    private void run() throws IOException {

        // Make connection and initialize streams
        System.out.println("Generating network");

        network = new Network();
        System.out.println("Observe protocols");

        BluetoothProtocol bP = new BluetoothProtocol();
        WifiProtocol wP = new WifiProtocol();

        wP.addObserver(network);
        bP.addObserver(network);

        System.out.println("Attemting to connect...");
        String wifiAddress = getWifiServerAddress();
        RemoteDevice btDevice = getBtServerAddress();
        System.out.println("Address: " + wifiAddress);
        wP.connect(wifiAddress, 8888);
        try {
            if(btDevice != null){
                bP.connect(btDevice);
            }
        } catch (BluetoothConnectionException ex){
            Logger.getLogger(CapitalizeClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Connected to server!");


        // Process all messages from server, according to the protocol.
        while (true) {
            String line = network.getInputStream().readLine();
            if (line.startsWith("SUBMITNAME")) {
                network.getOutputStream().println(getName());
//                network.getOutputStream(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            } else if (line.startsWith("MESSAGE")) {
                messageArea.append(line.substring(8) + "\n");
            }
        }
    }

    /**
     * Runs the client as an application with a closeable frame.
     */
    public static void main(String[] args) throws Exception {
        CapitalizeClient client = new CapitalizeClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }

    @Override
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
//add the device to the vector
        if (!arrDevices.contains(btDevice)) {
            arrDevices.add(btDevice);
        }
    }
//implement this method since services are not being discovered

    @Override
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        if (servRecord != null && servRecord.length > 0) {
            connectionURL = servRecord[0].getConnectionURL(0, false);
        }
        synchronized (lock) {
            lock.notify();
        }
    }

//implement this method since services are not being discovered
    @Override
    public void serviceSearchCompleted(int transID, int respCode) {
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public void inquiryCompleted(int discType) {
        synchronized (lock) {
            lock.notify();
        }

    }//end method
}
