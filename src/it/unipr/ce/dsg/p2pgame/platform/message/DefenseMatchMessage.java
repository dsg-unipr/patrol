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
public class DefenseMatchMessage extends Message {

	/**
	 * The identifier of peer just in the network
	 */
	private String id = null; //informazioni di chi attacca
	private String userName = null;

	/**
	 * The IP address of peer just in the network
	 */
	private String positionHash = null;

	private double posX;  //cosi' da verificare che sia abbastanza vicino da poter sferrare l'attacco
	private double posY;
	private double posZ;

	private double quantity;
	private String resource;
//	private double vel;
//	private double vis;
//
//	//private double gran;
//
//	private String oldPos;

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
	public DefenseMatchMessage(String sourceName, String sourceSocketAddr, int sourcePort, String id, String username,
			String position, double x, double y, double z, String resource, double quantity) {

		super(sourceName,sourceSocketAddr,sourcePort);

		this.setMessageType("DEFENSE");
		this.PARAMETERS_NUM = 11;

		this.getParametersList().add(new Parameter("id", id));
		this.getParametersList().add(new Parameter("username", username));
		this.getParametersList().add(new Parameter("position", position));
		this.getParametersList().add(new Parameter("x", new Double(x).toString()));
		this.getParametersList().add(new Parameter("y", new Double(y).toString()));
		this.getParametersList().add(new Parameter("z", new Double(z).toString()));
		this.getParametersList().add(new Parameter("resource", resource));
		this.getParametersList().add(new Parameter("quantity", new Double(quantity).toString()));

		this.id = id;
		this.userName = username;
		this.positionHash = position;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.resource = resource;
		this.quantity = quantity;
	}

	/**
	 *
	 * The constructor from parameter message.
	 * Is used for reconstruct InfoPeer message on reception
	 *
	 * @param message the message by which reconstruct
	 *
	 */
	public DefenseMatchMessage(Message message){

		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());

		this.setMessageType("DEFENSE");
		this.PARAMETERS_NUM = 11;

		//System.out.println("par size received " + this.getParametersList().size());

		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

		this.id = this.getParametersList().get(3).getValue();
		this.userName = this.getParametersList().get(4).getValue();
		//System.out.println("username " + this.userName);
		this.positionHash = this.getParametersList().get(5).getValue();
		//System.out.println("position hash " + this.positionHash);
		this.posX = Double.parseDouble(this.getParametersList().get(6).getValue());
		this.posY = Double.parseDouble(this.getParametersList().get(7).getValue());
		this.posZ = Double.parseDouble(this.getParametersList().get(8).getValue());
		this.resource = this.getParametersList().get(9).getValue();
		this.quantity = Double.parseDouble(this.getParametersList().get(10).getValue());
	}

	public String getUserName() {
		return userName;
	}

	public String getPosition() {
		return positionHash;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}



}
