package it.unipr.ce.dsg.p2pgame.platform.bot;

public class VirtualResource {
	
	private String resType;
	private String ownerID;
	private String ownerName;
	private String id;	
	private double x;
	private double y;
	private double z;
	
	public VirtualResource(String resType,String id, String ownerID,String ownerName, double x, double y,double z) {
		
		this.resType = resType;
		this.ownerID = ownerID;
		this.ownerName=ownerName;
		this.id=id;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public String getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

public VirtualResource() {
		
		
	}
public String getOwnerName() {
	return ownerName;
}

public void setOwnerName(String ownerName) {
	this.ownerName = ownerName;
}

	
	
	
	
	
	
	

}
