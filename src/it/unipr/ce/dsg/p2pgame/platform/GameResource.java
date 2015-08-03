package it.unipr.ce.dsg.p2pgame.platform;

/**
 *
 * Describe a generic game resource.
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class GameResource {

	/**
	 * The identifier of game resource
	 */
	private String id = null;

	/**
	 * A description of game resource
	 */
	private String description = null;

	/**
	 * The quantity of resource
	 */
	private double quantity = 0;

	/**
	 *
	 * Construct a game resource.
	 *
	 * @param id identifier of resource
	 * @param description description of resource
	 * @param quantity the quantity associated to game resource
	 */
	public GameResource(String id, String description, double quantity) {
		super();
		this.id = id;
		this.description = description;
		this.quantity = quantity;
	}

	/**
	 *
	 * Get the identifier of resource
	 *
	 * @return identifier
	 */
	public String getId() {
		return id;
	}

	/**
	 *
	 * Get the description associated to resource
	 *
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 *
	 * Get quantity value of resource
	 *
	 * @return the value of resource
	 */
	public double getQuantity() {
		return quantity;
	}

	/**
	 *
	 * Set a new identifier for resource
	 *
	 * @param id identifier
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 *
	 * Set a new description for resource
	 *
	 * @param description resource description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 *
	 * Set a new quantity value for resource
	 *
	 * @param quantity the quantity value
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	/**
	 *
	 * Copy the game resource object
	 *
	 */
	public Object clone() {
		GameResource clone = null;
		try {
			clone = (GameResource) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		clone.id = this.id;
		clone.description = this.description;

		clone.quantity = this.quantity;


		return clone;
	}

}
