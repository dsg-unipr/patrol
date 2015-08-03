package it.unipr.ce.dsg.p2pgame.network.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;

/**
 *
 * This class provide the structure for message Find Successor on an
 * existing peer in a Chord-like network. Is used for ask the successor peer of
 * indicated id.
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class FindSuccMessage extends Message {

	/**
	 * The identifier of peer / resource for which is required the successor node
	 */
	private String peerId = null;

	/**
	 *
	 * The constructor for message to send with parameters from arguments.
	 *
	 * @param sourceName the id of source peer
	 * @param sourceSocketAddr the IP address of source
	 * @param sourcePort the source port
	 * @param peerId info with id of Peer / Resource to compare
	 *
	 */
	public FindSuccMessage(String sourceName, String sourceSocketAddr, int sourcePort, String peerId) {

		super(sourceName,sourceSocketAddr,sourcePort);

		this.setMessageType("FINDSUCC");
		this.PARAMETERS_NUM = 4;

		this.getParametersList().add(new Parameter("peerId", peerId));

		this.peerId = peerId;

	}

	/**
	 *
	 * The constructor from parameter message.
	 * Is used for reconstruct Find Successor Peer message on reception
	 *
	 * @param message the message by which reconstruct
	 *
	 */
	public FindSuccMessage(Message message){

		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());

		this.setMessageType("FINDSUCC");
		this.PARAMETERS_NUM = 4;

		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

		this.peerId = this.getParametersList().get(3).getValue();

	}

	/**
	 *
	 * Get the id by which is required the successor
	 *
	 * @return the id of peer / resource
	 *
	 */
	public String getPeerId() {
		return peerId;
	}

	/**
	 *
	 * Set a new id for Find Successor message
	 *
	 * @param peerId the new id of peer / resource
	 *
	 */
	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}


}
