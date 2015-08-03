package it.unipr.ce.dsg.p2pgame.network.message;

import it.simplexml.message.Message;

/**
 *
 * This class provide the structure for message of request to connect on an
 * existing Chord-like network
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class ConnectMessage extends Message {

	/**
	 *
	 * The constructor for message to send with parameters from arguments.
	 * Input argument characterize sender of request.
	 *
	 * @param sourceName the id of source peer
	 * @param sourceSocketAddr the IP address of source
	 * @param sourcePort the source port
	 *
	 */
	public ConnectMessage(String sourceName, String sourceSocketAddr, int sourcePort) {

		super(sourceName,sourceSocketAddr,sourcePort);

		this.setMessageType("CONNECT");
		this.PARAMETERS_NUM = 3;

	}

	/**
	 *
	 * The constructor from parameter message.
	 * Is used for reconstruct Connect message on reception
	 *
	 * @param message the message by which reconstruct
	 *
	 */
	public ConnectMessage(Message message){

		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());

		this.setMessageType("CONNECT");
		this.PARAMETERS_NUM = 3;

		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

	}


}
