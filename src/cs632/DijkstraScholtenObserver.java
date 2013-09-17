package cs632;

import peersim.config.*;
import peersim.core.*;
import peersim.vector.*;
import peersim.util.IncrementalStats;
import org.ubiety.ubigraph.UbigraphClient;

public class DijkstraScholtenObserver implements Control {

    // /////////////////////////////////////////////////////////////////////
    // Constants
    // /////////////////////////////////////////////////////////////////////

    private static final String PAR_ACCURACY = "accuracy";
    private static final String PAR_PROT = "protocol";

    // /////////////////////////////////////////////////////////////////////
    // Fields
    // /////////////////////////////////////////////////////////////////////

    private final String name;
    private final double accuracy;
    private final int pid;
    private UbigraphClient graph;
    private int lastVertex;

    // /////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////

    public DijkstraScholtenObserver(String name) {
        this.name = name;
        accuracy = Configuration.getDouble(name + "." + PAR_ACCURACY, -1);
        pid = Configuration.getPid(name + "." + PAR_PROT);
        cycles = 0;
        graph = new UbigraphClient();
        // lastVertex = graph.newVertex(0);
    }

    // /////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////
    public int cycles;

    public boolean execute() {
        long time = peersim.core.CommonState.getTime();
        Node rootNode = Network.get(0);
        DijkstraScholten rootNodeProtocol = (DijkstraScholten) rootNode.getProtocol(pid);
        int nodeId = 0;

        // IncrementalStats is = new IncrementalStats();

         for (int i = 0; i < Network.size(); i++) {
            Node currentNode = Network.get(i);
            DijkstraScholten protocol = (DijkstraScholten) currentNode
                .getProtocol(pid);

        /* Printing statistics */
            if(protocol.isActivated && !protocol.activationSent){
                System.out.println("["+cycles+"]"+i + ": " + protocol.computedVal + "  isActivated->" + protocol.isActivated + " by " + protocol.parentIndex + " terminated children = " + protocol.terminatedChildren);
                nodeId++;
            }
        }

        // int newVertex = graph.newVertex(nodeId);

        // graph.newEdge(lastVertex, newVertex);
        // lastVertex = newVertex;

        cycles++;
        /* Terminate if accuracy target is reached */
        return rootNodeProtocol.isTerminated;
    }
}
