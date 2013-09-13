package cs632;

import peersim.config.*;
import peersim.core.*;
import peersim.vector.*;
import peersim.util.IncrementalStats;

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

    // /////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////

    public DijkstraScholtenObserver(String name) {
        this.name = name;
        accuracy = Configuration.getDouble(name + "." + PAR_ACCURACY, -1);
        pid = Configuration.getPid(name + "." + PAR_PROT);
        cycles = 0;
    }

    // /////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////
    public int cycles;

    public boolean execute() {
        long time = peersim.core.CommonState.getTime();

        // IncrementalStats is = new IncrementalStats();

         for (int i = 0; i < Network.size(); i++) {
            DijkstraScholten protocol = (DijkstraScholten) Network.get(i)
                .getProtocol(pid);
// 
//             SingleValue protocol = (SingleValue) Network.get(i)
//                     .getProtocol(pid);
//             is.add(protocol.getValue());

        /* Printing statistics */
            if(protocol.isActivated)
                System.out.println("["+cycles+"]"+i + ": " + time + "  isActivated->" + protocol.isActivated + " by " + protocol.parentIndex);
         }

        cycles++;
        /* Terminate if accuracy target is reached */
        return (cycles>6);
    }
}
