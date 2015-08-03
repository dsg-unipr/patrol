package it.unipr.ce.dsg.p2pgame.platform;

public class GamePlayerResponsible extends GamePlayer {

	private long timestamp;
	private String positionHash;
	private String oldPos;

	public GamePlayerResponsible(String id, String name, double posX, double posY, double posZ,
			double vel, double visibility, /*double gran,*/
			long time, String pos, String oldPos) {
		super(id, name, posX, posY, posZ, vel, visibility/*, gran*/);


		this.timestamp = time;
		this.positionHash = pos;
		this.oldPos = oldPos;
	}


	public long getTimestamp() {
		return timestamp;
	}


	public String getPositionHash() {
		return positionHash;
	}


	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}


	public void setPositionHash(String positionHash) {
		this.positionHash = positionHash;
	}


	public String getOldPos() {
		return oldPos;
	}


	public void setOldPos(String oldPos) {
		this.oldPos = oldPos;
	}




}
