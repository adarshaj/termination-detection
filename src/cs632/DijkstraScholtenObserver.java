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
        lastVertex = graph.newVertex();
    }

    // /////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////
    public int cycles;

    public boolean execute() {
        long time = peersim.core.CommonState.getTime();
        DijkstraScholten rootNodeProtocol = (DijkstraScholten) Network.get(0).getProtocol(pid);

        // IncrementalStats is = new IncrementalStats();

         for (int i = 0; i < Network.size(); i++) {
            DijkstraScholten protocol = (DijkstraScholten) Network.get(i)
                .getProtocol(pid);
// 
//             SingleValue protocol = (SingleValue) Network.get(i)
//                     .getProtocol(pid);
//             is.add(protocol.getValue());

        /* Printing statistics */
            // if(protocol.isTerminated)
                System.out.println("["+cycles+"]"+i + ": " + protocol.computedVal + "  isActivated->" + protocol.isActivated + " by " + protocol.parentIndex + " terminated children = " + protocol.terminatedChildren);
        }

        int newVertex = graph.newVertex();

        graph.newEdge(lastVertex, newVertex);

        cycles++;
        /* Terminate if accuracy target is reached */
        return rootNodeProtocol.isTerminated;
    }
}
