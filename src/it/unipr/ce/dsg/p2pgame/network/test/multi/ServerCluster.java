package it.unipr.ce.dsg.p2pgame.network.test.multi;

import it.unipr.ce.dsg.p2pgame.network.BootstrapServer;
import it.unipr.ce.dsg.p2pgame.util.SHA1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class ServerCluster {

	/**
	 *
	 * Test class for bootstrap server of Chord-like network.
	 * Allow peers to access to an existing ring near to it's correct position.
	 *
	 * @param args
	 * @exception IOException for socket open on bootstrap server
	 * @exception NoSuchAlgorithmException on SHA-1 hash calculation
	 * @exception UnsupportedEncodingException on parameter that is passed on hash function
	 *
	 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
	 *
	 */
	public static void main(String[] args) {

		try {
			System.out.println("SERVER");
			System.out.println("l'hash in hex e': " + SHA1.convertToHex(SHA1.calculateSHA1("ciao")));

			try {
				//BootstrapServer bs = new BootstrapServer(1234, 1235, 5, 4);
				/*BootstrapServer bs = */new BootstrapServer(1234, 1235, 5, 160);
			} catch (IOException e) {

				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

}
