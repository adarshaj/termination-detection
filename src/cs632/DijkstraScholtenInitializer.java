package cs632;

import cs632.DijkstraScholten;
import peersim.config.*;
import peersim.core.*;

public class DijkstraScholtenInitializer implements Control {

    // ------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------

    /**
     * The load at the peak node.
     * 
     * @config
     */
    private static final String PAR_VALUE = "value";

    /**
     * The protocol to operate on.
     * 
     * @config
     */
    private static final String PAR_PROT = "protocol";

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /** Value at the peak node.
    * Obtained from config property {@link #PAR_VALUE}. */
    private final double value;

    /** Protocol identifier; obtained from config property {@link #PAR_PROT}. */
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

    /**
    * Initialize an aggregation protocol using a peak distribution.
    * That is, one node will get the peek value, the others zero.
    * @return always false
    */
    public boolean execute() {
        Node rootNode = Network.get(0);
        ((DijkstraScholten)rootNode.getProtocol(pid)).parentID = -1;
        Linkable linkable = (Linkable) rootNode.getProtocol( FastConfig.getLinkable(pid) );

        Node childNode = linkable.getNeighbor(linkable.degree()-1);

        return false;
    }
}
