package cs632;

import peersim.vector.SingleValueHolder;
import peersim.config.*;
import peersim.core.*;
import peersim.transport.Transport;
import peersim.cdsim.CDProtocol;
import peersim.edsim.EDProtocol;
import org.ubiety.ubigraph.UbigraphClient;

public class DijkstraScholten extends SingleValueHolder
implements CDProtocol, EDProtocol {

//--------------------------------------------------------------------------
// Initialization
//--------------------------------------------------------------------------

public DijkstraScholten(String prefix) { 
    super(prefix); 
    isActivated=false; 
    activationSent = false; 
    isTerminated = false; 
    parentIndex = -1; 
    computedVal = 0;
    computedMaxVal = 1;
    terminatedChildren = 0;
    activatedChildren = 0;
    graph = new UbigraphClient();
}
public int parentIndex;
// public int selfIndex;
public int terminatedChildren;
public int activatedChildren;
public long computedVal;
public long computedMaxVal;
public boolean isActivated;
public boolean isTerminated;
public boolean activationSent;
UbigraphClient graph;

//--------------------------------------------------------------------------
// methods
//--------------------------------------------------------------------------

/**
 * This is the standard method the define periodic activity.
 * The frequency of execution of this method is defined by a
 * {@link peersim.edsim.CDScheduler} component in the configuration.
 */
public void nextCycle( Node node, int pid )
{
    int currentNodeIndex = node.getIndex();
    boolean isActive = ((DijkstraScholten)node.getProtocol(pid)).isActivated;
    boolean isTerminated = ((DijkstraScholten)node.getProtocol(pid)).isTerminated;
	Linkable linkable = 
		(Linkable) node.getProtocol( FastConfig.getLinkable(pid) );
    int maxDegree = linkable.degree();
    if (isActive && !activationSent) {
        for (int i = 0; i < maxDegree; i++) {
            Node childNode = linkable.getNeighbor(i);

            if (childNode.isUp()) {
                ((DijkstraScholten)node.getProtocol(pid)).activatedChildren++;
                ((Transport)node.getProtocol(FastConfig.getTransport(pid))).
                    send(
                            node,
                            childNode,
                            new ActivationMessage(currentNodeIndex),
                            pid);
            }
        }
        activationSent = true;
        graph.setVertexAttribute(currentNodeIndex, "color", "#aa0000");
        ((DijkstraScholten)node.getProtocol(pid)).computedMaxVal = (long) (Math.random() * 100);
    }
    if (isActive && !isTerminated) {
        if ( ((DijkstraScholten)node.getProtocol(pid)).computedVal < ((DijkstraScholten)node.getProtocol(pid)).computedMaxVal)
            ((DijkstraScholten)node.getProtocol(pid)).computedVal+=1;
        else {
            int terminatedChildren = ((DijkstraScholten)node.getProtocol(pid)).terminatedChildren;
            int parentIndex = ((DijkstraScholten)node.getProtocol(pid)).parentIndex;
            int activatedChildren = ((DijkstraScholten)node.getProtocol(pid)).activatedChildren;
            if (terminatedChildren == activatedChildren) {
                if (parentIndex != -1) {
                Node parentNode = Network.get(parentIndex);
                ((Transport)node.getProtocol(FastConfig.getTransport(pid))).
                    send(
                            node,
                            parentNode,
                            new TerminationMessage(),
                            pid);
                }
                ((DijkstraScholten)node.getProtocol(pid)).isTerminated = true;
                // graph.removeVertex(currentNodeIndex);
                // 
                graph.setVertexAttribute(currentNodeIndex, "color", "#333333");

            } else {
              graph.setVertexAttribute(currentNodeIndex, "color", "#00aa00");
            }
        }
    } 

}

//--------------------------------------------------------------------------

/**
* This is the standard method to define to process incoming messages.
*/
public void processEvent( Node node, int pid, Object event ) {
    int currentNodeIndex = node.getIndex();
		
	Linkable linkable = 
		(Linkable) node.getProtocol( FastConfig.getLinkable(pid) );
	if ( event instanceof ActivationMessage ){
        ActivationMessage aem = (ActivationMessage)event;
        ((DijkstraScholten)node.getProtocol(pid)).parentIndex = aem.senderIndex;
        ((DijkstraScholten)node.getProtocol(pid)).isActivated = true;
        // graph.newVertex(currentNodeIndex);
        // graph.setVertexAttribute(currentNodeIndex, "shape", "sphere");
        graph.newEdge(aem.senderIndex, currentNodeIndex);
        graph.setVertexAttribute(currentNodeIndex, "color", "#00aa00");
    }
    if ( event instanceof TerminationMessage ) {
        ((DijkstraScholten)node.getProtocol(pid)).terminatedChildren++;
    }
}

}

//--------------------------------------------------------------------------
//--------------------------------------------------------------------------

/**
* The type of a message. It contains a value of type double and the
* sender node of type {@link peersim.core.Node}.
*/
class ActivationMessage {

	final int senderIndex;
    public ActivationMessage()
    {
        this.senderIndex = -1;
    }
	public ActivationMessage(int senderNodeIndex)
	{
		this.senderIndex = senderNodeIndex;
	}
}
class TerminationMessage {

}

