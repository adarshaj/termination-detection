package cs632;

import cs632.DijkstraScholten;
import cs632.ActivationMessage;
import peersim.transport.Transport;
import peersim.config.*;
import peersim.core.*;

public class DijkstraScholtenInitializer implements Control {

    // ------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------

    private static final String PAR_VALUE = "value";

    private static final String PAR_PROT = "protocol";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    private final double value;
    private final int pid;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    /**
     * Creates a new instance and read parameters from the config file.
     */
    public DijkstraScholtenInitializer(String prefix) {
        value = Configuration.getDouble(prefix + "." + PAR_VALUE);
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------

    public boolean execute() {
        int rootNodeIndex = 0;
        Node rootNode = Network.get(rootNodeIndex);
        ((DijkstraScholten)rootNode.getProtocol(pid)).parentIndex = -1;
        ((DijkstraScholten)rootNode.getProtocol(pid)).isActivated = true;
        Linkable linkable = (Linkable) rootNode.getProtocol(FastConfig.getLinkable(pid));
        int maxDegree = linkable.degree();
        for (int i = 0; i < maxDegree; i++) {
            Node childNode = linkable.getNeighbor(i);

            if (childNode.isUp()) {
                ((Transport)rootNode.getProtocol(FastConfig.getTransport(pid))).
                    send(
                            rootNode,
                            childNode,
                            new ActivationMessage(rootNodeIndex),
                            pid);
            }
        }
        return false;
    }
}
