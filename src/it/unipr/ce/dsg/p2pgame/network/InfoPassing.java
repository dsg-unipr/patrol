package it.unipr.ce.dsg.p2pgame.network;

public class InfoPassing {

	private NetPeerInfo peerData;
	private String peerID;
	
	
	public InfoPassing(NetPeerInfo peerData, String peerId) {
		super();
		this.peerData = peerData;
		this.peerID = peerId;
	}


	public NetPeerInfo getPeerData() {
		return peerData;
	}


	public void setPeerData(NetPeerInfo peerData) {
		this.peerData = peerData;
	}


	public String getPeerID() {
		return peerID;
	}


	public void setPeerID(String peerID) {
		this.peerID = peerID;
	}


	
	
	
	
	
}
