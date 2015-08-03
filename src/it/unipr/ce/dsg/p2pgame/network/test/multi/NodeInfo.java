package it.unipr.ce.dsg.p2pgame.network.test.multi;

import java.util.ArrayList;

/**
 *
 * This class is used for store all information know by a peer to all other.
 * This information are: peer identification, successor peer, predecessor peer
 * and peer on finger table.
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class NodeInfo {

	/**
	 * The identifier of peer
	 */
	private String peerId;

	/**
	 * The identifier of successor peer
	 */
	private String successorId;

	/**
	 * The identifier of predecessor peer
	 */
	private String predecessorId;

	/**
	 * The identifier on finger table
	 */
	private ArrayList<String> fingerEntry = new ArrayList<String>();

	/**
	 *
	 * Constructor for NodeInfo object
	 *
	 * @param peerId identifier of peer
	 * @param successorId identifier of successor
	 * @param predecessorId predecessor of identifier
	 * @param fingerEntry ArrayList of String with peers identifier on finger table
	 *
	 */
	public NodeInfo(String peerId, String successorId, String predecessorId,ArrayList<String> fingerEntry) {
		super();
		this.peerId = peerId;
		this.successorId = successorId;
		this.predecessorId = predecessorId;
		this.fingerEntry = fingerEntry;
	}

	/**
	 * Empty constructor
	 */
	public NodeInfo(){
		super();
	}


	/**
	 * Retrieve identifier of peer
	 *
	 * @return the peer identifier
	 */
	public String getPeerId() {
		return peerId;
	}


	/**
	 * Set new identifier for peer. It will be used with empty constructor
	 *
	 * @param peerId the new peer identifier
	 */
	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}


	/**
	 * Retrieve the identifier of successor peer
	 *
	 * @return the successor peer identifier
	 */
	public String getSuccessorId() {
		return successorId;
	}


	/**
	 * Set new identifier for successor peer
	 *
	 * @param successorId the new successor peer identifier
	 */
	public void setSuccessorId(String successorId) {
		this.successorId = successorId;
	}


	/**
	 * Retrieve the identifier of predecessor peer
	 *
	 * @return the predecessor peer identifier
	 */
	public String getPredecessorId() {
		return predecessorId;
	}


	/**
	 * Set new identifier for predecessor peer
	 *
	 * @param predecessorId the new predecessor peer identifier
	 */
	public void setPredecessorId(String predecessorId) {
		this.predecessorId = predecessorId;
	}


	/**
	 * Retrieve all identifier stored on finger table
	 *
	 * @return the list of all identifier on finger table
	 */
	public ArrayList<String> getFingerEntry() {
		return fingerEntry;
	}


	/**
	 * Set new list of finger table's identifier
	 *
	 * @param fingerEntry the new identifier on finger table
	 */
	public void setFingerEntry(ArrayList<String> fingerEntry) {
		this.fingerEntry = fingerEntry;
	}



}
