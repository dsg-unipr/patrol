package it.unipr.ce.dsg.p2pgame.GUI.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;
public class StartMatchRequestMessage extends Message{

	
	private String resownerId;
	private String resownerName;
	private String ipAdd;
	private int portNumber;
	private String otherresourceID;
	private String myrousrceID;
	private double resourceQuantity;
	private double posX;
	private double posY;
	private double posZ;
	
	
	public StartMatchRequestMessage(String resourceOwnerID, String resourceOwnerName,String ip,int port,String otherresourceID,String myrousrceID,double resourceQuantity , double posX, double posY, double posZ)
	{
		super("","",0);
        this.setMessageType("STARTMATCHREQUEST");
        this.PARAMETERS_NUM=13;
        
        this.resownerId=resourceOwnerID;
        this.resownerName=resourceOwnerName;
        this.ipAdd=ip;
        this.portNumber=port;
        this.otherresourceID=otherresourceID;
        this.myrousrceID=myrousrceID;
        this.resourceQuantity=resourceQuantity;
        this.posX=posX;
        this.posY=posY;
        this.posZ=posZ;
        
        this.getParametersList().add(new Parameter("resownerId", this.resownerId));
        this.getParametersList().add(new Parameter("resownerName", this.resownerName));
        this.getParametersList().add(new Parameter("ipAdd", this.ipAdd));
        this.getParametersList().add(new Parameter("portNumber", Integer.toString(this.portNumber)));
        this.getParametersList().add(new Parameter("otherresourceID", this.otherresourceID));
        this.getParametersList().add(new Parameter("myrousrceID", this.myrousrceID));
        this.getParametersList().add(new Parameter("resourceQuantity", Double.toString(this.resourceQuantity)));
        this.getParametersList().add(new Parameter("posX", Double.toString(this.posX)));
        this.getParametersList().add(new Parameter("posY", Double.toString(this.posY)));
        this.getParametersList().add(new Parameter("posZ", Double.toString(this.posZ)));
        
		
	}
	
	public StartMatchRequestMessage(Message message)
	{
		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("STARTMATCHREQUEST");
        this.PARAMETERS_NUM=13;
        
        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}
                
        this.resownerId=this.getParametersList().get(3).getValue();;
        this.resownerName=this.getParametersList().get(4).getValue();
        this.ipAdd=this.getParametersList().get(5).getValue();
        this.portNumber=Integer.parseInt(this.getParametersList().get(6).getValue());
        this.otherresourceID=this.getParametersList().get(7).getValue();
        this.myrousrceID=this.getParametersList().get(8).getValue();
        this.resourceQuantity=Double.parseDouble(this.getParametersList().get(9).getValue());
        this.posX=Double.parseDouble(this.getParametersList().get(10).getValue());
        this.posY=Double.parseDouble(this.getParametersList().get(11).getValue());
        this.posZ=Double.parseDouble(this.getParametersList().get(12).getValue());
		
	}


	public String getResownerId() {
		return resownerId;
	}


	public void setResownerId(String resownerId) {
		this.resownerId = resownerId;
	}


	public String getResownerName() {
		return resownerName;
	}


	public void setResownerName(String resownerName) {
		this.resownerName = resownerName;
	}


	public String getIpAdd() {
		return ipAdd;
	}


	public void setIpAdd(String ipAdd) {
		this.ipAdd = ipAdd;
	}


	public int getPortNumber() {
		return portNumber;
	}


	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}


	public String getOtherresourceID() {
		return otherresourceID;
	}


	public void setOtherresourceID(String otherresourceID) {
		this.otherresourceID = otherresourceID;
	}


	public String getMyrousrceID() {
		return myrousrceID;
	}


	public void setMyrousrceID(String myrousrceID) {
		this.myrousrceID = myrousrceID;
	}


	public double getResourceQuantity() {
		return resourceQuantity;
	}


	public void setResourceQuantity(double resourceQuantity) {
		this.resourceQuantity = resourceQuantity;
	}


	public double getPosX() {
		return posX;
	}


	public void setPosX(double posX) {
		this.posX = posX;
	}


	public double getPosY() {
		return posY;
	}


	public void setPosY(double posY) {
		this.posY = posY;
	}


	public double getPosZ() {
		return posZ;
	}


	public void setPosZ(double posZ) {
		this.posZ = posZ;
	}
	
}
