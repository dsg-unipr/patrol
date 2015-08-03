package it.unipr.ce.dsg.p2pgame.network.message;

import it.simplexml.message.Message;

/**
 *
 * This class provide the structure for message of Notify with a possible predecessor
 * on an existing peer in a Chord-like network
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class NotifyMessage extends Message {


	/**
	 *
	 * The constructor for message to send with parameters from arguments.
	 * Arguments contains information of possible predecessor on ring
	 *
	 * @param sourceName the id of source peer
	 * @param sourceSocketAddr the IP address of source
	 * @param sourcePort the source port
	 *
	 */
	public NotifyMessage(String sourceName, String sourceSocketAddr, int sourcePort) {

		super(sourceName,sourceSocketAddr,sourcePort);

		this.setMessageType("NOTIFY");
		this.PARAMETERS_NUM = 3;

	}

	/**
	 *
	 * The constructor from parameter message.
	 * Is used for reconstruct Notify message on reception
	 *
	 * @param message the message by which reconstruct
	 *
	 */
	public NotifyMessage(Message message){

		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());

		this.setMessageType("NOTIFY");
		this.PARAMETERS_NUM = 3;

		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

	}


}
