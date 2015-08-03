package it.unipr.ce.dsg.p2pgame.platform;

/**
 *
 * Describe a resource which evolve on time
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class GameResourceEvolve extends GameResource {

	/**
	 * Update period
	 */
	private long period;

	/**
	 * Update offset of evolution
	 */
	private double offset;

	/**
	 * Update the thread that manage evolution
	 */
	private Thread update = null;

	/**
	 *
	 * Create an evolvable resource
	 *
	 * @param id identifier of resource
	 * @param description description associated to resource
	 * @param quantity associates a quantity value to resource
	 * @param period update period for resource
	 * @param offset the offset of evolution
	 */
	public GameResourceEvolve(String id, String description, double quantity, final long period, double offset) {
		super(id, description, quantity);

		this.offset = offset;
		this.period = period;


		this.update = new Thread( new Runnable() {
			public void run() {
				try {
					while(true) {
						Thread.sleep(period);

						evolveResource();
					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
		);
		this.update.setPriority(Thread.MAX_PRIORITY);
		this.update.start();
	}

	/**
	 *
	 * Get the period of resource evolution
	 *
	 * @return update period
	 */
	public double getPeriod() {
		return period;
	}

	/**
	 *
	 * Get offset of evolution
	 *
	 * @return the offset of evolution
	 */
	public double getOffset() {
		return offset;
	}

	/**
	 *
	 * Set a new update period
	 *
	 * @param period the period of update
	 */
	public void setPeriod(long period) {
		this.period = period;
	}

	/**
	 *
	 * Set a new update offset
	 *
	 * @param offset the offset of evolution
	 */
	public void setOffset(double offset) {
		this.offset = offset;
	}

	/**
	 *
	 * Function that is used for evolve the resource
	 *
	 */
	public void evolveResource() {
		//System.out.println("Evolving resource " + this.getId());
		double newQuant = this.getQuantity() + this.getOffset();
		//System.out.println("old quantity " + this.getQuantity());
		this.setQuantity(newQuant);
		//System.out.println("new quantity " + this.getQuantity());
	}

}
