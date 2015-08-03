package it.unipr.ce.dsg.p2pgame.network;

/**
 *
 * Info of peer in Chord-like network. Peer are characterized by IP Address,
 * TCP port number and optionally an host name.
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class NetPeerInfo {

	/**
	 * IP address of peer
	 */
	private String ipAddress = null;

	/**
	 * TCP port number of peer
	 */
	private int portNumber;

	/**
	 * Host name of peer (optionally)
	 */
	private String hostName = null;

	/**
	 *
	 * Constructor of a new Peer Info from his IP Address, TCP port number and host name.
	 *
	 * @param ip the IP address of peer
	 * @param port the port number of peer
	 * @param name the host name of peer
	 *
	 */
	public NetPeerInfo(String ip, int port, String name){

		this.ipAddress = ip;
		this.portNumber = port;

		this.hostName = name;

	}

	public NetPeerInfo( NetPeerInfo npi){
		
		this.ipAddress = npi.ipAddress;
		this.portNumber = npi.portNumber;
		this.hostName = npi.hostName;
		
	}
	
	
	/**
	 *
	 * Get IP Address of this peer
	 *
	 * @return the string with IP address
	 *
	 */
	public String getIpAddress() {
		
		//System.out.println("#######NET PEER INFO GETIPADDRESS###########");
		return ipAddress;
	}

	/**
	 *
	 * Get the TCP port number of this peer
	 *
	 * @return the port number
	 *
	 */
	public int getPortNumber() {
		return portNumber;
	}

	/**
	 *
	 * Get the host name of this peer
	 *
	 * @return the host name
	 *
	 */
	public String getHostName() {
		return hostName;
	}

}
