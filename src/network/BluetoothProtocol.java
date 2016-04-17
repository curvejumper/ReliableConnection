/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 *
 * @author micha
 */
public class BluetoothProtocol extends Protocol implements DiscoveryListener {

    //Bluetooth info
    UUID uuid;
    StreamConnection connection;
    OutputStream outStream;
    InputStream inStream;
    private boolean server;

    //object used for waiting
    private static Object lock = new Object();

//vector containing the devices discovered
    private static Vector vecDevices = new Vector();

    private static String connectionURL = null;

    @Override
    public boolean status() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OutputStream getOutputStream() {
        try {
            outStream = connection.openOutputStream();
//            PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));
//            pWriter.write(msg + "\r\n");
//            pWriter.flush();
            if(status()){
                notifyObservers(this);
            }
        } catch (IOException ex) {
            Logger.getLogger(BluetoothProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outStream;
    }

    @Override
    public InputStream getInputStream() {
        try {
            inStream = connection.openInputStream();
        } catch (IOException ex) {
            Logger.getLogger(BluetoothProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inStream;
    }

    @Override
    public Object connect() {
        LocalDevice localDevice;
        try {
            localDevice = LocalDevice.getLocalDevice();
            System.out.println("Address: " + localDevice.getBluetoothAddress());
            System.out.println("Name: " + localDevice.getFriendlyName());

            if (server) {
                //Create the servicve url
                String connectionString = "btspp://localhost:" + uuid + ";name=Sample SPP Server";

                //open server url
                StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier) Connector.open(connectionString);

                //Wait for client connection
                System.out.println("\nServer Started. Waiting for clients to connect…");
                connection = streamConnNotifier.acceptAndOpen();
            } else {
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

//print all devices in vecDevices
                int deviceCount = vecDevices.size();

                if (deviceCount <= 0) {
                    System.out.println("No Devices Found .");
                    System.exit(0);
                } else { //print bluetooth device addresses and names in the format [ No. address (name) ] 
                    System.out.println("Bluetooth Devices: ");
                    for (int i = 0; i < deviceCount; i++) {
                        RemoteDevice remoteDevice = (RemoteDevice) vecDevices.elementAt(i);
                        System.out.println((i + 1) + ". " + remoteDevice.getBluetoothAddress() + " (" + remoteDevice.getFriendlyName(true) + ")");
                    }
                }
                System.out.print("Choose Device index: ");
                BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
                String chosenIndex = bReader.readLine();
                int index = Integer.parseInt(chosenIndex.trim());
                //check for spp service 
                RemoteDevice remoteDevice = (RemoteDevice) vecDevices.elementAt(index - 1);
                UUID[] uuidSet = new UUID[1];
                uuidSet[0] = new UUID("1101", true);
                System.out.println("\nSearching for service...");
                agent.searchServices(null, uuidSet, remoteDevice, this);
                try {
                    synchronized (lock) {
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (connectionURL == null) {
                    System.out.println("Device does not support Simple SPP Service.");
                    System.exit(0);
                } //connect to the server and getOutputStream a line of text 
                connection = (StreamConnection) Connector.open(connectionURL);
            }

        } catch (BluetoothStateException ex) {
            Logger.getLogger(BluetoothProtocol.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BluetoothProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    public void connect(String ID, boolean server) {
        //TODO: make sure the ID is four bits
        uuid = new UUID(ID, true);
        connect();
    }

    @Override
    public void Close() {
        try {
            outStream.close();
            inStream.close();
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(BluetoothProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
//add the device to the vector
        if (!vecDevices.contains(btDevice)) {
            vecDevices.addElement(btDevice);
        }
    }
//implement this method since services are not being discovered

    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        if (servRecord != null && servRecord.length > 0) {
            connectionURL = servRecord[0].getConnectionURL(0, false);
        }
        synchronized (lock) {
            lock.notify();
        }
    }

//implement this method since services are not being discovered
    public void serviceSearchCompleted(int transID, int respCode) {
        synchronized (lock) {
            lock.notify();
        }
    }

    public void inquiryCompleted(int discType) {
        synchronized (lock) {
            lock.notify();
        }

    }//end method

}
