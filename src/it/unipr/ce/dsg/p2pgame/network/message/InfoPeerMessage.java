package it.unipr.ce.dsg.p2pgame.network.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;

/**
 *
 * This class provide the structure for message that describe a peer on an
 * existing peer in a Chord-like network
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class InfoPeerMessage extends Message {

	/**
	 * The identifier of peer just in the network
	 */
	private String peerId = null;

	/**
	 * The IP address of peer just in the network
	 */
	private String peerAddr = null;

	/**
	 * The port of peer just in the network
	 */
	private int peerPort;

	/**
	 *
	 * The constructor for message to send with parameters from arguments.
	 *
	 * @param sourceName the id of source peer
	 * @param sourceSocketAddr the IP address of source
	 * @param sourcePort the source port
	 * @param peerId info with id of Peer in the network
	 * @param peerAddr info with the IP address of peer
	 * @param peerPort info with the port of peer
	 *
	 */
	public InfoPeerMessage(String sourceName, String sourceSocketAddr, int sourcePort, String peerId, String peerAddr, int peerPort) {

		super(sourceName,sourceSocketAddr,sourcePort);

		this.setMessageType("INFOPEER");
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
	 * Is used for reconstruct InfoPeer message on reception
	 *
	 * @param message the message by which reconstruct
	 *
	 */
	public InfoPeerMessage(Message message){

		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());

		this.setMessageType("INFOPEER");
		this.PARAMETERS_NUM = 6;

		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

		this.peerId = this.getParametersList().get(3).getValue();
		//System.out.println("peerId " + this.peerId);
		this.peerAddr = this.getParametersList().get(4).getValue();
		//System.out.println("peerAddr " + this.peerAddr);
		this.peerPort = Integer.parseInt(this.getParametersList().get(5).getValue());
		//System.out.println("peerPort " + this.peerPort);

	}

	/**
	 *
	 * Get the peer id by which message inform
	 *
	 * @return the id of peer
	 *
	 */
	public String getPeerId() {
		return peerId;
	}

	/**
	 *
	 * Set a new id for peer described on message
	 *
	 * @param peerId the new peer id
	 *
	 */
	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}

	/**
	 *
	 * Get the IP address of peer described on message
	 *
	 * @return the address of peer
	 *
	 */
	public String getPeerAddr() {
		return peerAddr;
	}

	/**
	 *
	 * Set a new IP address for peer described on message
	 *
	 * @param peerAddr the new address of peer
	 *
	 */
	public void setPeerAddr(String peerAddr) {
		this.peerAddr = peerAddr;
	}

	/**
	 *
	 * Get the TCP port of peer described on message
	 *
	 * @return the port number of peer
	 *
	 */
	public int getPeerPort() {
		return peerPort;
	}

	/**
	 *
	 * Set a new TCP port for peer described on messasge
	 *
	 * @param peerPort the new port of peer
	 *
	 */
	public void setPeerPort(int peerPort) {
		this.peerPort = peerPort;
	}



}
