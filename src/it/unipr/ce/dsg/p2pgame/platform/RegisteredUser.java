package it.unipr.ce.dsg.p2pgame.platform;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import it.unipr.ce.dsg.p2pgame.util.SHA1;

/**
 *
 * Define the info of registered user
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class RegisteredUser {

	/**
	 * The username of user
	 */
	private String username = null;

	/**
	 * The password of user
	 */
	private String password = null;
	//private String nickname = null;

	/**
	 * The identifier of user on network
	 */
	private String id = null; //id with hash

	/**
	 * Create a container for all information about an user.
	 * The exception could be throws on identifier calculation (with SHA1)
	 *
	 * @param username the username for user
	 * @param password the passwrod for user
	 *
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public RegisteredUser(String username, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		super();
		this.username = username;
		this.password = password;
		//this.nickname = nickname;
		this.id = SHA1.convertToHex(SHA1.calculateSHA1(this.username));
	}

	/**
	 * Retrieve the username
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Retrieve the password
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Retrieve the identifier of user on network
	 *
	 * @return the identifier of user
	 */
	public String getId() {
		return id;
	}

//	public String getNickname() {
//		return nickname;
//	}

}
