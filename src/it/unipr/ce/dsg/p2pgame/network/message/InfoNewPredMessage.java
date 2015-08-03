package it.unipr.ce.dsg.p2pgame.network.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;

/**
 *
 * This class provide the structure for message that inform of new Predecessor
 * on a Chord-like network. This message is used from right procedure of leaving
 * node from network which inform of new Predecessor for let consistency on ring
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class InfoNewPredMessage extends Message {

	/**
	 * The identifier of Predecessor peer just in the network
	 */
	private String peerId = null;

	/**
	 * The IP address of Predecessor peer just in the network
	 */
	private String peerAddr = null;

	/**
	 * The port of Predecessor peer just in the network
	 */
	private int peerPort;

	/**
	 *
	 * The constructor for message to send with parameters from arguments.
	 *
	 * @param sourceName the id of source peer
	 * @param sourceSocketAddr the IP address of source
	 * @param sourcePort the source port
	 * @param peerId info with id of Predecessor Peer in the network
	 * @param peerAddr info with the IP address of Predecessor peer
	 * @param peerPort info with the port of Predecessor peer
	 *
	 */
	public InfoNewPredMessage(String sourceName, String sourceSocketAddr, int sourcePort, String peerId, String peerAddr, int peerPort) {

		super(sourceName,sourceSocketAddr,sourcePort);

		this.setMessageType("INFONEWPRED");
		this.PARAMETERS_NUM = 6;

		this.getParametersList().add(new Parameter("peerId", peerId));
		this.getParametersList().add(new Parameter("peerAddr", peerAddr));
		this.getParametersList().add(new Parameter("peerPort", new Integer(peerPort).toString()));

		this.peerId = peerId;
		this.peerAddr = peerAddr;
		this.peerPort = peerPort;

	}

	/**
	 *
	 * The constructor from parameter message.
	 * Is used for reconstruct Info New Predecessor message on reception
	 *
	 * @param message the message by which reconstruct
	 *
	 */
	public InfoNewPredMessage(Message message){

		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());

		this.setMessageType("INFONEWPRED");
		this.PARAMETERS_NUM = 6;

		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

		this.peerId = this.getParametersList().get(3).getValue();
		System.out.println("peerId " + this.peerId);
		this.peerAddr = this.getParametersList().get(4).getValue();
		//System.out.println("peerAddr " + this.peerAddr);
		this.peerPort = Integer.parseInt(this.getParametersList().get(5).getValue());
		System.out.println("peerPort " + this.peerPort);

	}

	/**
	 *
	 * Get the id of new Predecessor from message
	 *
	 * @return the id of new predecessor peer
	 *
	 */
	public String getPeerId() {
		return peerId;
	}

	/**
	 *
	 * Set the new predecessor peer on message
	 *
	 * @param peerId the new predecessor peer id
	 *
	 */
	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}

	/**
	 *
	 * Get the IP address of Predecessor peer
	 *
	 * @return the IP address of predecessor peer
	 *
	 */
	public String getPeerAddr() {
		return peerAddr;
	}

	/**
	 *
	 * Set a new IP address for predecessor peer
	 *
	 * @param peerAddr the new IP address for predecessor peers
	 *
	 */
	public void setPeerAddr(String peerAddr) {
		this.peerAddr = peerAddr;
	}

	/**
	 *
	 * Get the TCP port of Predecessor peer
	 *
	 * @return the peer port of predecessor
	 *
	 */
	public int getPeerPort() {
		return peerPort;
	}

	/**
	 *
	 * Set new TCP port for Predecessor peer
	 *
	 * @param peerPort the new port of predecessor
	 *
	 */
	public void setPeerPort(int peerPort) {
		this.peerPort = peerPort;
	}



}
