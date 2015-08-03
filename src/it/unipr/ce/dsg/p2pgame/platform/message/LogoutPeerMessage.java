package it.unipr.ce.dsg.p2pgame.platform.message;

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
public class LogoutPeerMessage extends Message {

	/**
	 * The identifier of peer just in the network
	 */
	private String userName = null;

	/**
	 * The IP address of peer just in the network
	 */
	private String password = null;

	private String userId = null;

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
	public LogoutPeerMessage(String sourceName, String sourceSocketAddr, int sourcePort, String username, String password, String id) {

		super(sourceName,sourceSocketAddr,sourcePort);

		this.setMessageType("LOGOUT");
		this.PARAMETERS_NUM = 6;

		this.getParametersList().add(new Parameter("username", username));
		this.getParametersList().add(new Parameter("password", password));
		this.getParametersList().add(new Parameter("id", id));


		this.userName = username;
		this.password = password;
		this.userId = id;
		System.out.println("usn " + this.userName + " pwd " + this.password );
		System.out.println("size of param " + this.getParametersList().size());
	}

	/**
	 *
	 * The constructor from parameter message.
	 * Is used for reconstruct InfoPeer message on reception
	 *
	 * @param message the message by which reconstruct
	 *
	 */
	public LogoutPeerMessage(Message message){

		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());

		this.setMessageType("LOGOUT");
		this.PARAMETERS_NUM = 6;

		//System.out.println("par size received " + this.getParametersList().size());

		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

		this.userName = this.getParametersList().get(3).getValue();
		System.out.println("username " + this.userName);
		this.password = this.getParametersList().get(4).getValue();
		System.out.println("password " + this.password);
		this.password = this.getParametersList().get(5).getValue();
		System.out.println("id " + this.userId);

	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


}
