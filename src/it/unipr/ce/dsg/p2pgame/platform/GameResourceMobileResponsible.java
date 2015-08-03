package it.unipr.ce.dsg.p2pgame.platform;

/**
 *
 * Describe a mobile resource that is maintained by a resource responsible
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class GameResourceMobileResponsible extends GameResourceMobile {

	/**
	 * The timestamp of last movement. It's useful for check the validity
	 * of a movement
	 */
	private long timestamp;

	/**
	 * The hash of position
	 */
	private String positionHash;

	/**
	 * The old position of the mobile resource
	 */
	private String oldPos;

	/**
	 *
	 * Constructor for a resource maintained by a responsible peer
	 *
	 * @param id the identifier of resource
	 * @param description the the mobile resource
	 * @param owner the name owner of mobile resource
	 * @param ownerId the owner identifier of mobile resource
	 * @param quantity the quantity value of mobile resource
	 * @param x the x coordinate of position
	 * @param y the y coordinate of position
	 * @param z the z coordinate of position
	 * @param vel the maximum velocity associated to mobile resource
	 * @param vis the vision range of the resource
	 * @param time the time of last movement
	 * @param pos the resource's current position
	 * @param oldPos the resource's old position
	 */
	public GameResourceMobileResponsible(String id, String description,
			String owner, String ownerId, double quantity, double x, double y,
			double z, double vel, double vis,
			long time, String pos, String oldPos) {
		super(id, description, owner, ownerId, quantity, x, y, z, vel, vis);


		this.timestamp = time;
		this.positionHash = pos;
		this.oldPos = oldPos;
	}

	/**
	 *
	 * Get the timestamp of resouce's last movement
	 *
	 * @return the timestamp movement
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 *
	 * Set a new timestamp for resource's last movement
	 *
	 * @param timestamp the new timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 *
	 * Get the position hash of mobile resource
	 *
	 * @return the hash of mobile resource position
	 */
	public String getPositionHash() {
		return positionHash;
	}

	/**
	 *
	 * Set a new position hash for mobile resource
	 *
	 * @param positionHash the new mobile resource position hash
	 */
	public void setPositionHash(String positionHash) {
		this.positionHash = positionHash;
	}

	/**
	 *
	 * Get the old position of resource
	 *
	 * @return the old position of mobile resource
	 */
	public String getOldPos() {
		return oldPos;
	}

	/**
	 *
	 * Change the old position of mobile resource
	 *
	 * @param oldPos the new value for old position of mobile resource
	 */
	public void setOldPos(String oldPos) {
		this.oldPos = oldPos;
	}




}
