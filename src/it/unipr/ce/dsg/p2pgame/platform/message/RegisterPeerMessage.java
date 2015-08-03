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
public class RegisterPeerMessage extends Message {

	/**
	 * The identifier of peer just in the network
	 */
	private String userName = null;

	/**
	 * The IP address of peer just in the network
	 */
	private String password = null;

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
	public RegisterPeerMessage(String sourceName, String sourceSocketAddr, int sourcePort, String username, String password) {

		super(sourceName,sourceSocketAddr,sourcePort);

		this.setMessageType("REGISTER");
		this.PARAMETERS_NUM = 5;

		this.getParametersList().add(new Parameter("username", username));
		this.getParametersList().add(new Parameter("password", password));


		this.userName = username;
		this.password = password;
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
	public RegisterPeerMessage(Message message){

		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());

		this.setMessageType("REGISTER");
		this.PARAMETERS_NUM = 5;

		//System.out.println("par size received " + this.getParametersList().size());

		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

		this.userName = this.getParametersList().get(3).getValue();
		System.out.println("username " + this.userName);
		this.password = this.getParametersList().get(4).getValue();
		System.out.println("password " + this.password);


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

}
