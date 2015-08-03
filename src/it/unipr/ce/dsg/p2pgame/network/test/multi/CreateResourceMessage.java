package it.unipr.ce.dsg.p2pgame.network.test.multi;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;

/**
 *
 * This class provide the structure for message Create Resource on an
 * existing peer in a Chord-like network. Is used for ask the peer to
 * create a resource with random identifier.
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class CreateResourceMessage extends Message {

	/**
	 * The time - stamp of source request
	 */
	private String time = null;

	/**
	 *
	 * The constructor for message to send with parameters from arguments.
	 *
	 * @param sourceName the id of source peer
	 * @param sourceSocketAddr the IP address of source
	 * @param sourcePort the source port
	 * @param time time - stamp of source request
	 *
	 */
	public CreateResourceMessage(String sourceName, String sourceSocketAddr, int sourcePort, String time) {

		super(sourceName,sourceSocketAddr,sourcePort);

		this.setMessageType("CREATERESOURCE");
		this.PARAMETERS_NUM = 4;

		this.getParametersList().add(new Parameter("time", time));

		this.time = time;

	}

	/**
	 *
	 * The constructor from parameter message.
	 * Is used for reconstruct Find Successor Peer message on reception
	 *
	 * @param message the message by which reconstruct
	 *
	 */
	public CreateResourceMessage(Message message){

		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());

		this.setMessageType("CREATERESOURCE");
		this.PARAMETERS_NUM = 4;

		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

		this.time = this.getParametersList().get(3).getValue();

	}

	/**
	 *
	 * Get the time of source request
	 *
	 * @return the time - stamp of request
	 *
	 */
	public String getTime() {
		return time;
	}

	/**
	 *
	 * Set a time - stamp for Print Resources on cache message
	 *
	 * @param time the new time for request
	 *
	 */
	public void setTime(String time) {
		this.time = time;
	}


}
