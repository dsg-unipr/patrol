package it.unipr.ce.dsg.p2pgame.network.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;

/**
 *
 * This class provide the structure for message of resource request on an
 * existing peer in a Chord-like network
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class GetResourceMessage extends Message {

	/**
	 * The identifier of peer just in the network
	 */
	private String resourceId = null;

	/**
	 *
	 * The constructor for message to send with parameters from arguments.
	 *
	 * @param sourceName the id of source peer
	 * @param sourceSocketAddr the IP address of source
	 * @param sourcePort the source port
	 * @param resId id of required resource
	 *
	 */
	public GetResourceMessage(String sourceName, String sourceSocketAddr, int sourcePort, String resId) {

		super(sourceName,sourceSocketAddr,sourcePort);

		this.setMessageType("GETRESOURCE");
		this.PARAMETERS_NUM = 4;

		this.getParametersList().add(new Parameter("resourceId", resId));

		this.resourceId = resId;

	}

	/**
	 *
	 * The constructor of GetResourceMesssage from parameter message.
	 * Is used for reconstruct message on reception
	 *
	 * @param message the message to reconstruct
	 *
	 */
	public GetResourceMessage(Message message){

		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());

		this.setMessageType("GETRESOURCE");
		this.PARAMETERS_NUM = 4;

		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

		this.resourceId = this.getParametersList().get(3).getValue();

	}

	/**
	 *
	 * Get the id of resource on message
	 *
	 * @return the id of resource
	 *
	 */
	public String getResourceId() {
		return resourceId;
	}


	/**
	 *
	 * Set the id of resource on message
	 *
	 * @param resId the new id of resource
	 *
	 */
	public void setResourceId(String resId) {
		this.resourceId = resId;
	}


}
