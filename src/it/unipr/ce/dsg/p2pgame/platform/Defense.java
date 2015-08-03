package it.unipr.ce.dsg.p2pgame.platform;

/**
 * Defense describe the defense resource used by player.
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class Defense {

	/**
	 * The time in which the resource is created
	 */
	private long time;

	/**
	 * The value of defense resource
	 */
	private double quantity;

	/**
	 * The definition of defense type
	 */
	private String type;

	/**
	 * The constructor for defense resource
	 *
	 * @param quantity the value of defense resource
	 * @param type the type of defense resource
	 */
	public Defense(/*long time,*/ double quantity, String type) {
		super();
		this.time = System.currentTimeMillis();
		this.quantity = quantity;
		this.type = type;
	}


	/**
	 * Give the timestamp of resource creation
	 *
	 * @return the resource build date
	 */
	public long getTime() {
		return time;
	}


	/**
	 * Set a new time for resource creation
	 *
	 * @param time the new timestamp for resource creation
	 */
	public void setTime(long time) {
		this.time = time;
	}


	/**
	 * Give the value corresponding to defense resource
	 *
	 * @return the entity of defense resource
	 */
	public double getQuantity() {
		return quantity;
	}


	/**
	 * Set a new quantity for defense resource
	 *
	 * @param quantity the new entity for defense resource
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}


	/**
	 * Give the type of the defense resource
	 *
	 * @return the string which define the defense resource
	 */
	public String getType() {
		return type;
	}


	/**
	 * Set a new type which identify the defense resource
	 *
	 * @param type the new name for defense resource
	 */
	public void setType(String type) {
		this.type = type;
	}



}
