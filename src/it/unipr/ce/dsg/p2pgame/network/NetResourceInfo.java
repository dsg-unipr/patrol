package it.unipr.ce.dsg.p2pgame.network;

/**
 *
 * Info of resource in Chord-like network. Resource are characterized by
 * owner (the peer that have created the resource), owner id and key (that identify resource).
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class NetResourceInfo {

	/**
	 * Key identifier of resource
	 */
	private String resourceKey = null;

	/**
	 * Informations about peer owner of resource
	 */
	private NetPeerInfo owner = null;

	/**
	 * Identifier of peer owner
	 */
	private String ownerId = null;

	/**
	 *
	 * Constructor of a new Resource from id of resource, owner id and owner information
	 *
	 * @param key the identifier of resource
	 * @param npi the information about owner
	 * @param ownerId the owner identifier
	 *
	 */
	public NetResourceInfo(String key, NetPeerInfo npi, String ownerId){
		this.resourceKey = key;
		this.owner = npi;
		this.ownerId = ownerId;
	}

	/**
	 *
	 * Get the resource key which identify the resource
	 *
	 * @return the key of resource
	 *
	 */
	public String getResourceKey(){
		return this.resourceKey;
	}

	/**
	 *
	 * Get the owner information
	 *
	 * @return the information about owner peer
	 *
	 */
	public NetPeerInfo getOwner(){
		return this.owner;
	}

	/**
	 *
	 * Get the owner identifier for resource
	 *
	 * @return the id of owner peer
	 *
	 */
	public String getOwnerId(){
		return this.ownerId;
	}
}
