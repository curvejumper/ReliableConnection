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
    public void send(String msg) {
        try {
            outStream = connection.openOutputStream();
            PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));
            pWriter.write(msg + "\r\n");
            pWriter.flush();
        } catch (IOException ex) {
            Logger.getLogger(BluetoothProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String receive() {
        String lineRead = "";
        try {
            inStream = connection.openInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            lineRead = bReader.readLine();
        } catch (IOException ex) {
            Logger.getLogger(BluetoothProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lineRead;
    }

    @Override
    public Object connect() {
        LocalDevice localDevice;
        try {
            localDevice = LocalDevice.getLocalDevice();
            System.out.println("Address: " + localDevice.getBluetoothAddress());
            System.out.println("Name: " + localDevice.getFriendlyName());

            //Create the servicve url
            String connectionString = "btspp://localhost:" + uuid + ";name=Sample SPP Server";

            //open server url
            StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier) Connector.open(connectionString);

            //Wait for client connection
            System.out.println("\nServer Started. Waiting for clients to connect…");
            connection = streamConnNotifier.acceptAndOpen();

        } catch (BluetoothStateException ex) {
            Logger.getLogger(BluetoothProtocol.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BluetoothProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    public void connect(String ID) {
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
