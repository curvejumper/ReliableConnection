package network;

import java.util.Observable;
import java.util.Observer;
//import javax.swing.SwingUtilities;

/**
 *
 */
public class ReliableNetwork implements Observer {

    private Network network;

    public ReliableNetwork(Network network) {
        this.network = network;
        network.addObserver(this);
    }
    /**
     * Updates the UI depending on the Object argument
     */
    public void update(Observable o, Object arg) {
        final Object finalArg = arg;
        
        //update the user with any network issues
        System.out.println("From observable: " + finalArg.toString() + "\n");
        
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                textArea.append(finalArg.toString());
//                textArea.append("\n");
//            }
//        });
    }

}
