package cs632;

import peersim.vector.SingleValueHolder;
import peersim.config.*;
import peersim.core.*;
import peersim.transport.Transport;
import peersim.cdsim.CDProtocol;
import peersim.edsim.EDProtocol;

public class DijkstraScholten extends SingleValueHolder
implements CDProtocol, EDProtocol {

//--------------------------------------------------------------------------
// Initialization
//--------------------------------------------------------------------------

public DijkstraScholten(String prefix) { super(prefix); isActivated=false; }

public long parentIndex;
public boolean isActivated;

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
	Linkable linkable = 
		(Linkable) node.getProtocol( FastConfig.getLinkable(pid) );
    int maxDegree = linkable.degree();
    for (int i = 0; i < maxDegree; i++) {
        Node childNode = linkable.getNeighbor(i);

        if (childNode.isUp()) {
            ((Transport)node.getProtocol(FastConfig.getTransport(pid))).
                send(
                        node,
                        childNode,
                        new ActivationMessage(currentNodeIndex),
                        pid);
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
	ActivationMessage aem = (ActivationMessage)event;
    ((DijkstraScholten)node.getProtocol(pid)).parentIndex = aem.senderIndex;
    ((DijkstraScholten)node.getProtocol(pid)).isActivated = true;
}

}

//--------------------------------------------------------------------------
//--------------------------------------------------------------------------

/**
* The type of a message. It contains a value of type double and the
* sender node of type {@link peersim.core.Node}.
*/
class ActivationMessage {

	final long senderIndex;
    public ActivationMessage()
    {
        this.senderIndex = -1;
    }
	public ActivationMessage(long senderNodeIndex)
	{
		this.senderIndex = senderNodeIndex;
	}
}

