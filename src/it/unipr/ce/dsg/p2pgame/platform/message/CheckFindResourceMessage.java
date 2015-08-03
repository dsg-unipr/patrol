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
public class CheckFindResourceMessage extends Message {

	/**
	 * The identifier of peer just in the network
	 */
	private String userName = null;

	/**
	 * Position requested TO CHECK
	 */
	private String positionHash = null;
	//private String currentPosition = null;

	private double posX;
	private double posY;
	private double posZ;

//	private double vel;
//	private double vis;


	//private String oldPos;

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
	public CheckFindResourceMessage(String sourceName, String sourceSocketAddr, int sourcePort, String username,
			String position, double x, double y, double z/*, String currPos*/) {

		super(sourceName,sourceSocketAddr,sourcePort);

		this.setMessageType("CHECKFINDRESOURCE");
		this.PARAMETERS_NUM = 8;

		this.getParametersList().add(new Parameter("username", username));
		this.getParametersList().add(new Parameter("position", position));
		this.getParametersList().add(new Parameter("x", new Double(x).toString()));
		this.getParametersList().add(new Parameter("y", new Double(y).toString()));
		this.getParametersList().add(new Parameter("z", new Double(z).toString()));
		//this.getParametersList().add(new Parameter("currentPosition", currPos));
		//this.getParametersList().add(new Parameter("velocity", new Double(vel).toString()));
		//this.getParametersList().add(new Parameter("visibility", new Double(vis).toString()));
		//this.getParametersList().add(new Parameter("oldposition", oldPos));

		this.userName = username;
		this.positionHash = position;
		this.posX = x;
		this.posY = y;
		this.posZ = z;

		//this.currentPosition = currPos;
		//this.vel = vel;
		//this.vis = vis;
		//this.oldPos = oldPos;
	}

	/**
	 *
	 * The constructor from parameter message.
	 * Is used for reconstruct InfoPeer message on reception
	 *
	 * @param message the message by which reconstruct
	 *
	 */
	public CheckFindResourceMessage(Message message){

		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());

		this.setMessageType("CHECKFINDRESOURCE");
		this.PARAMETERS_NUM = 8;

		//System.out.println("par size received " + this.getParametersList().size());

		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

		this.userName = this.getParametersList().get(3).getValue();
		//System.out.println("username " + this.userName);
		this.positionHash = this.getParametersList().get(4).getValue();
		//System.out.println("position hash " + this.positionHash);
		this.posX = Double.parseDouble(this.getParametersList().get(5).getValue());
		this.posY = Double.parseDouble(this.getParametersList().get(6).getValue());
		this.posZ = Double.parseDouble(this.getParametersList().get(7).getValue());
//		this.currentPosition = this.getParametersList().get(8).getValue();
		//this.vel = Double.parseDouble(this.getParametersList().get(8).getValue());
		//this.vis = Double.parseDouble(this.getParametersList().get(9).getValue());
		//this.oldPos = this.getParametersList().get(8).getValue();
	}

	public String getUserName() {
		return userName;
	}

	public String getPosition() {
		return positionHash;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPosition(String position) {
		this.positionHash = position;
	}

	public String getPositionHash() {
		return positionHash;
	}

	public double getPosX() {
		return posX;
	}

	public double getPosY() {
		return posY;
	}

	public double getPosZ() {
		return posZ;
	}

	public void setPositionHash(String positionHash) {
		this.positionHash = positionHash;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public void setPosZ(double posZ) {
		this.posZ = posZ;
	}

//	public String getCurrentPosition() {
//		return currentPosition;
//	}
//
//	public void setCurrentPosition(String currentPosition) {
//		this.currentPosition = currentPosition;
//	}

//	public String getOldPos() {
//		return oldPos;
//	}
//
//	public void setOldPos(String oldPos) {
//		this.oldPos = oldPos;
//	}
//


}
