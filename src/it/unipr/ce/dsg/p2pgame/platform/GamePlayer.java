package it.unipr.ce.dsg.p2pgame.platform;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import it.unipr.ce.dsg.p2pgame.util.SHA1;

/**
 *
 * Describe the game player that represent the game-user on virtual world.
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class GamePlayer {

	/**
	 * The name of game user
	 */
	private String name = null;

	/**
	 * The identifier of game user
	 */
	private String id = null;

	/**
	 * The x position of gamer on virtual world
	 */
	private double posX = 0;

	/**
	 * The y position of gamer on virtual world
	 */
	private double posY = 0;

	/**
	 * The z position of gamer on virtual world
	 */
	private double posZ = 0;

	/**
	 * The velocity of game player. According to velocity there is position update
	 */
	private double velocity = 0;

	/**
	 * The visibility of game player around it's position
	 */
	private double visibility;

	/**
	 *
	 * Create a game player
	 *
	 * @param id the identifier of game player
	 * @param name the name of game player
	 * @param posX the x position of player
	 * @param posY the y position of player
	 * @param posZ the z position of player
	 * @param vel the velocity of player
	 * @param visibility the visibility range of player
	 */
	public GamePlayer(String id, String name, double posX, double posY, double posZ, double vel, double visibility) {
		super();

		this.id = id;

		this.name = name;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.velocity = vel;

		this.visibility = visibility;
	}


	/**
	 *
	 * The movement of a resource of a given quantity
	 *
	 * @param relPosX the relative x position movement
	 * @param relPosY the relative y position movement
	 * @param relPosZ the relative z position movement
	 */
	public void movePlayer(double relPosX, double relPosY, double relPosZ){

		this.posX += relPosX;
		this.posY += relPosY;
		this.posZ += relPosZ;

	}

	/**
	 *
	 * Return the spatial position hash of player
	 *
	 * @return the hash of player position
	 */
	public String getSpatialPosition(){

		String pos = new Double(this.posX).toString() + new Double(this.posY).toString() + new Double(this.posZ).toString();

		try {
			return SHA1.convertToHex(SHA1.calculateSHA1(pos));
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

		return null;
	}


	/**
	 *
	 * Get the name of player
	 *
	 * @return the player name
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * Get the identifier of player
	 *
	 * @return the player identifier
	 */
	public String getId() {
		return id;
	}


	/**
	 *
	 * Set the identifier of player
	 *
	 * @param id the new identifier for player
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 *
	 * Get the x spatial position of player
	 *
	 * @return the x coordinate
	 */
	public double getPosX() {
		return posX;
	}

	/**
	 *
	 * Get the y spatial position of player
	 *
	 * @return the y coordinate
	 */
	public double getPosY() {
		return posY;
	}


	/**
	 *
	 * Get the z spatial position of player
	 *
	 * @return the z coordinate
	 */
	public double getPosZ() {
		return posZ;
	}


	/**
	 *
	 * Get the velocity of player
	 *
	 * @return the player maximum velocity
	 */
	public double getVelocity() {
		return velocity;
	}


	/**
	 *
	 * Set the name of game player
	 *
	 * @param name the name of player
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 * Set the x position for player
	 *
	 * @param posX the x position
	 */
	public void setPosX(double posX) {
		this.posX = posX;
	}


	/**
	 *
	 * Set the y position for player
	 *
	 * @param posY the y position
	 */
	public void setPosY(double posY) {
		this.posY = posY;
	}


	/**
	 *
	 * Set the z position for player
	 *
	 * @param posZ the z position
	 */
	public void setPosZ(double posZ) {
		this.posZ = posZ;
	}

	/**
	 *
	 * Set the maximum velocity for player
	 *
	 * @param vel the player's maximum velocity
	 */
	public void setVelocity(double vel) {
		this.velocity = vel;
	}


	/**
	 *
	 * Get the visibility range for player
	 *
	 * @return the player visibility range
	 */
	public double getVisibility() {
		return visibility;
	}


	/**
	 *
	 * Set a new visibility range for player
	 *
	 * @param visibility the visibility range for player
	 */
	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}


}
