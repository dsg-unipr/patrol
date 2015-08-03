package it.unipr.ce.dsg.p2pgame.platform;

public class AddressInfo {
	
	private String id;
	private String ipAddress;
	private int port;
	
	public AddressInfo(String id,String ipAddress, int port) {
		super();
		this.id=id;
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public AddressInfo()
	{
		
		
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getId()
	{
		return this.id;
		
	}
	
	public void setId(String id)
	{
		
		this.id=id;
	}
	

}
