package it.unipr.ce.dsg.p2pgame.network.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;

/**
 *
 * This class provide the structure for message of Publish resource
 * on an existing peer in a Chord-like network
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class PublishResourceMessage extends Message{

	/**
	 * The key which identify the resource on network
	 */
	private String key;

	/**
	 * The id of owner peer for resource
	 */
	private String ownerId;


	/**
	 *
	 * The constructor for message to send with parameters from arguments.
	 * Arguments contains information of key and owner for resource
	 *
	 * @param sourceName the id of source peer
	 * @param sourceSocketAddr the IP address of source
	 * @param sourcePort the source port
	 * @param key the key of resource
	 * @param owner the peer that owned resource
	 *
	 */
	public PublishResourceMessage(String sourceName, String sourceSocketAddr, int sourcePort, String key, String owner){
		super(sourceName,sourceSocketAddr,sourcePort);
		this.setMessageType("PUBLISH");

		this.PARAMETERS_NUM = 5;

		this.getParametersList().add(new Parameter("key", key));
		this.getParametersList().add(new Parameter("owner", owner));

		this.key = key;
		this.ownerId = owner;
	}

	/**
	 *
	 * The constructor from parameter message.
	 * Is used for reconstruct Publish Resource message on reception
	 *
	 * @param message the message by which reconstruct
	 *
	 */
	public PublishResourceMessage(Message message){
		super(message.getSourceName(),message.getSourceSocketAddr(),message.getSourcePort());
		this.setMessageType("PUBLISH");
		this.PARAMETERS_NUM = 5;

		for(int index = 3 ; index < message.getParametersList().size(); index++ )
			this.getParametersList().add(message.getParametersList().get(index));

		this.key = this.getParametersList().get(3).getValue();
		this.ownerId = this.getParametersList().get(4).getValue();
	}

	/**
	 *
	 * Get the key that identify resource on network
	 *
	 * @return the key of resource
	 *
	 */
	public String getKey() {
		return key;
	}

	/**
	 *
	 * Set the key of resource by which message inform
	 *
	 * @param key the new key of resource
	 *
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 *
	 * Get peer that owned resource
	 *
	 * @return the owner of resource
	 *
	 */
	public String getOwnerId() {
		return ownerId;
	}

	/**
	 *
	 * Set a new owner for the resource
	 *
	 * @param ownerId the new owner for resource
	 *
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}


}
