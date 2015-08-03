package it.unipr.ce.dsg.p2pgame.network.test;

import it.unipr.ce.dsg.p2pgame.network.NetPeer;
import it.unipr.ce.dsg.p2pgame.network.NetPeerInfo;
import it.unipr.ce.dsg.p2pgame.network.NetResourceInfo;
import it.unipr.ce.dsg.p2pgame.util.SHA1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class TestPeer {

	/**
	 *
	 * Test peer of Chord-like network.
	 * It's possible to select:
	 * 	<b>ACCESS</b>: for connect to Bootstrap server and obtain access to an existing ring or inform to create a new ring
	 * 	<b>STAB</b>: call Stabilize method of Chord algorithm for know a new successor or informing successor of new predecessor
	 * 	<b>FIX</b>: call FixFinger for fix finger table
	 * 	<b>CHECK</b>: call CheckPredecessor for testing if predecessor is online
	 * 	<b>PRINT</b>: print Successor, Predecessor and Finger Table on Chord-like ring
	 * 	<b>CREATE:x</b>: create a new resource 'x' and save it on cache
	 * 	<b>PUB</b>: publish all resource on cache
	 *  <b>PRINTRES</b>: print all resource on cache
	 *  <b>SEARCH:x</b>: search on network for resource 'x' and if find save on cache
	 *  <b>DISCO</b>: disconnect from ring in right manner. All resource are published on successor, and inform Successor and Predecessor to close ring
	 *  <b>RECON</b>: reconnect after a previously disconnection
	 *  <b>EXIT</b>: exit from Test peer
	 *
	 *
	 * @param args
	 * @throws IOException at reading of input from keyboard
	 * @exception NoSuchAlgorithmException on SHA-1 hash calculation
	 * @exception UnsupportedEncodingException on parameter that is passed on hash function
	 *
	 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
	 * @throws InterruptedException 
	 *
	 */
	public static void main(String[] args) throws IOException, InterruptedException {

		try {
			System.out.println("CLIENT");
			String threadId = new Long(Thread.currentThread().getId()).toString();
			Random ran = new Random();
			int port = ran.nextInt(9999);

			System.out.println("l'hash in hex e': " + SHA1.convertToHex(SHA1.calculateSHA1("ciao" + new Integer(port).toString())));

			//NetPeer np = new NetPeer(port, port-1, 160, SHA1.convertToHex(SHA1.CalculateSHA1("ciao" + new Integer(port).toString())), "localhost", 1235 );

			//prova light per testare la finger table
			int idL = ran.nextInt(16);
			System.out.println("id( " + idL + " ): " + BigInteger.valueOf(idL).toString(16) );
			NetPeer np = new NetPeer(port, port-1, 4, BigInteger.valueOf(idL).toString(16), "localhost", 1235 );

			BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			while(true) {
				String input = console.readLine();

				String[] info = input.split(":");
				if (info[0] != null){
					if(info[0].trim().contentEquals("ACCESS")){
						np.getAccessToNetwork();

					} else if(info[0].trim().contentEquals("STAB")){
						np.stabilize();

					} else if (info[0].trim().contentEquals("FIX")){
						np.fixFinger(threadId);

					}else if (info[0].trim().contentEquals("CHECK")){
						np.checkPredecessor();

					}else if (info[0].trim().contentEquals("PRINT")){
						np.printAllInfo();

					} else if (info[0].trim().contentEquals("CREATE")){
						if (info.length == 2){
							String key = info[1].trim();
							System.out.println("CREAZIONE risorsa " + key);
							NetPeerInfo npi = new NetPeerInfo("localhost", port, "");
							NetResourceInfo res = new NetResourceInfo(key, npi, BigInteger.valueOf(idL).toString(16));
							np.addResourceOnCache(res);
							np.printResourceCache();
						}
					}else if (info[0].trim().contentEquals("PUB")){
						//np.publishResource();
						np.publishResource(threadId);

					}else if (info[0].trim().contentEquals("PRINTRES")){
						np.printResourceCache();

					}else if (info[0].trim().contentEquals("SEARCH")){
						if (info.length == 2){
							String key = info[1].trim();
							//np.searchResource(key);
							np.searchResource(key, threadId);
						}
					}else if (info[0].trim().contentEquals("DISCO")){
						//np.disconnectPeer();
						np.disconnectPeer(threadId);

					}else if (info[0].trim().contentEquals("RECON")){
						np.reconnectPeer();

					}else if (info[0].trim().contentEquals("EXIT"))
						System.exit(0);

					else
						System.out.println("generic messsage");
				}
			}


		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}


	}

}
