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
public class UserPeerMessage extends Message {

	/**
	 * The identifier of peer just in the network (ID)
	 */
	private String userName = null;

	private double x;
	private double y;
	private double z;
	private double velocity;
	private double vision;
	//private double granularity;

	/**
	 * The IP address of peer just in the network
	 */
	//private String id = null;

//	/**
//	 * The port of peer just in the network
//	 */
//	private int peerPort;

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
	public UserPeerMessage(String sourceName, String sourceSocketAddr, int sourcePort, String username, double x, double y, double z, double vel, double vis/*, double gran*/) {

		super(sourceName,sourceSocketAddr,sourcePort);

		this.setMessageType("USERPEER");
		this.PARAMETERS_NUM = 9;

		this.getParametersList().add(new Parameter("username", username));
		//this.getParametersList().add(new Parameter("id", id));
		this.getParametersList().add(new Parameter("x", new Double(x).toString()));
		this.getParametersList().add(new Parameter("y", new Double(y).toString()));
		this.getParametersList().add(new Parameter("z", new Double(z).toString()));
		this.getParametersList().add(new Parameter("velocity", new Double(vel).toString()));
		this.getParametersList().add(new Parameter("vision", new Double(vis).toString()));
		//this.getParametersList().add(new Parameter("granularity", new Double(gran).toString()));

		this.userName = username;
		this.x = x;
		this.y = y;
		this.z = z;
		this.velocity = vel;
		this.vision = vis;
		//this.granularity = gran;
	//	this.id = id;
		//this.peerPort = peerPort;

	}

	/**
	 *
	 * The constructor from parameter message.
	 * Is used for reconstruct InfoPeer message on reception
	 *
	 * @param message the message by which reconstruct
	 *
	 */
	public UserPeerMessage(Message message){

		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());

		this.setMessageType("USERPEER");
		this.PARAMETERS_NUM = 9;

		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

		this.userName = this.getParametersList().get(3).getValue();
		System.out.println("username " + this.userName);
//		this.id = this.getParametersList().get(4).getValue();
//		System.out.println("id " + this.id);
		this.x = Double.parseDouble(this.getParametersList().get(4).getValue());
		this.y = Double.parseDouble(this.getParametersList().get(5).getValue());
		this.z = Double.parseDouble(this.getParametersList().get(6).getValue());
		this.velocity = Double.parseDouble(this.getParametersList().get(7).getValue());
		this.vision = Double.parseDouble(this.getParametersList().get(8).getValue());
		//this.granularity = Double.parseDouble(this.getParametersList().get(9).getValue());
	}

	public String getUserName() {
		return userName;
	}

//	public String getId() {
//		return id;
//	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public double getVelocity() {
		return velocity;
	}

	public double getVision() {
		return vision;
	}

//	public double getGranularity() {
//		return granularity;
//	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public void setVision(double vision) {
		this.vision = vision;
	}

//	public void setGranularity(double granularity) {
//		this.granularity = granularity;
//	}

//	public void setId(String id) {
//		this.id = id;
//	}




}
