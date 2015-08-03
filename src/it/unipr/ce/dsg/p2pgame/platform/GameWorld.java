package it.unipr.ce.dsg.p2pgame.platform;

import java.util.Random;

public class GameWorld {

	private double minX = 0;
	private double maxX = 0;
	private double minY = 0;
	private double maxY = 0;
	private double minZ = 0;
	private double maxZ = 0;

	private double granularity;

	public GameWorld(double minX, double maxX, double minY, double maxY, double minZ, double maxZ, double gran) {
		super();
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.minZ = minZ;
		this.maxZ = maxZ;

		this.granularity = gran;
	}

	//verify that position is admissible for the world
	public boolean isOnWorld(double x, double y, double z){

		if (x >= this.minX && x <= this.maxX && y >= this.minY && y <= this.maxY && z >= this.minZ && z <= this.maxZ )
			return true;

		else
			return false;
	}

	public double getRandomX(){

		if (this.minX == this.maxX)
			return this.minX;
		else{

			Random rand = new Random();
			//TODO: fare in modo di avere la precisione desiderata e non un intero

			double posX = Math.round( rand.nextDouble()*(this.maxX - this.minX) + this.minX );
			return (posX - posX % this.granularity);

			//return ( rand.nextDouble()*(this.maxX - this.minX) + this.minX );
			//TODO: usata fino a 091011
			//return Math.round( rand.nextDouble()*(this.maxX - this.minX) + this.minX );
		}
	}

	public double getRandomY(){

		if (this.minY == this.maxY)
			return this.minY;
		else {
			Random rand = new Random();
			//return ( rand.nextDouble()*(this.maxY - this.minY) + this.minY );
			double posY = Math.round( rand.nextDouble()*(this.maxY - this.minY) + this.minY );
			return (posY - posY % this.granularity);

			//return Math.round( rand.nextDouble()*(this.maxY - this.minY) + this.minY );


		}

	}

	public double getRandomZ(){

		if (this.minZ == this.maxZ)
			return this.minZ;
		else {
			Random rand = new Random();
			//return ( rand.nextDouble()*(this.maxZ - this.minZ) + this.minZ );
			//return Math.round( rand.nextDouble()*(this.maxZ - this.minZ) + this.minZ );
			return 0.0;
		}

	}

	public double getGranularity() {
		return granularity;
	}

	public double getMinX() {
		return minX;
	}

	public double getMaxX() {
		return maxX;
	}

	public double getMinY() {
		return minY;
	}

	public double getMaxY() {
		return maxY;
	}

	public double getMinZ() {
		return minZ;
	}

	public double getMaxZ() {
		return maxZ;
	}


}
