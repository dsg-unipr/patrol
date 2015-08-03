package it.unipr.ce.dsg.p2pgame.network.test.multi;

import it.unipr.ce.dsg.p2pgame.network.NetPeer;
import it.unipr.ce.dsg.p2pgame.network.NetPeerInfo;
import it.unipr.ce.dsg.p2pgame.network.NetResourceInfo;
import it.unipr.ce.dsg.p2pgame.util.SHA1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 *
 * Class for test Chord peer on network with cluster computer.
 * Lunch periodic thread for network maintenance (Stabilize,
 * Fix Finger, Check Predecessor and Publish resources) and
 * another thread for manage incoming request from control server.
 *
 *
 * @param args information for peer use: port serverAddress serverPort (required);
 * 		and period for: stabilize, fixFinger, checkPredecessor, publish (optionally)
 *
 * @throws IOException at write on output log file
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class PeerCluster {


	public static void main(String[] args) throws IOException {

		int port = 0;
		int portMinus = 0;
		String peerName = "";
		String serverAddress = "";
		int serverPort = 0;

		int periodStabilize = 1000;
		int periodFixFinger = 5000;
		int periodCheckPredecessor = 5000;
		int periodPublish = 30000;

		int periodLogNetwork = 60000;
		int periodLogNWB = 60000;

//		FileWriter outCreate;
//		FileWriter outSearch;
//		FileWriter outResourceCache;
//		FileWriter outNetwork;
//		FileWriter outNWB;

		FileOutputStream fileCreation = new FileOutputStream("ChordCreation.txt");
	    PrintStream outCreation = new PrintStream(fileCreation);
	    PeerCluster.outCreation = outCreation;

	    FileOutputStream fileSearch = new FileOutputStream("ChordSearch.txt");
	    PrintStream outSearch = new PrintStream(fileSearch);
	    PeerCluster.outSearch = outSearch;

	    FileOutputStream fileResources = new FileOutputStream("ChordCacheResources.txt");
	    PrintStream outResources = new PrintStream(fileResources);
	    PeerCluster.outResources = outResources;

	    FileOutputStream fileNetwork = new FileOutputStream("ChordNetwork.txt");
	    final PrintStream outNetwork = new PrintStream(fileNetwork);
//	    FileOutputStream fileNWB = new FileOutputStream("ChordNWB.txt");
//	    PrintStream outNWB = new PrintStream(fileNWB);


		if (args.length < 3 ) {
			System.out.println("USE: port serverAddress serverPort (stabilize fixFinger checkPredecessor publish)");
			System.exit(1);
		}
		else {
			portMinus = Integer.parseInt(args[0].trim());
			port = portMinus + 1;
			portNum = port;
			serverAddress = args[1].trim();
			serverPort = Integer.parseInt(args[2].trim());

			System.out.println("Port used: " + portMinus + " and " + port);
			System.out.println("Server: " + serverAddress + ":" + serverPort);

			if (args.length == 9) {
				periodStabilize = Integer.parseInt(args[3].trim());
				periodFixFinger = Integer.parseInt(args[4].trim());
				periodCheckPredecessor = Integer.parseInt(args[5].trim());
				periodPublish = Integer.parseInt(args[6].trim());

				periodLogNetwork = Integer.parseInt(args[7].trim());
				periodLogNWB = Integer.parseInt(args[8].trim());
			}

		}

		Random ran = new Random();
		int unL = ran.nextInt(160);

		//peerName = BigInteger.valueOf(unL + System.currentTimeMillis()).toString(16);
		try {
			peerName = SHA1.convertToHex(SHA1.calculateSHA1(BigInteger.valueOf(unL + System.currentTimeMillis()).toString(16)));
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		peerId = peerName;
		System.out.println("Peer name " + peerName);
		System.out.println("Ports : " + port + " , " + portMinus + " portNum " + portNum);
		final NetPeer np = new NetPeer(port, portMinus, 160, peerName, serverAddress, serverPort );
		peer = np;

		np.getAccessToNetwork();

		final int stabilize = periodStabilize;

		new Thread( new Runnable() {
			public void run() {
				try {
					while(true) {
						Thread.sleep(stabilize);

						np.stabilize();
					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
		).start();


		final int fixFinger = periodFixFinger;

		new Thread( new Runnable() {
			private String threadId = new Long(Thread.currentThread().getId()).toString();
			public void run() {
				try {
					while(true) {
						Thread.sleep(fixFinger);

						np.fixFinger(this.threadId);
					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
		).start();


		final int checkPredecessor = periodCheckPredecessor;

		new Thread( new Runnable() {
			public void run() {
				try {
					while(true) {
						Thread.sleep(checkPredecessor);

						np.checkPredecessor();
					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
		).start();


		final int publish = periodPublish;

		new Thread( new Runnable() {
			private String threadId = new Long(Thread.currentThread().getId()).toString();
			public void run() {
				try {
					while(true) {
						Thread.sleep(publish);

						np.publishResource(threadId);
					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
		).start();


		final int network = periodLogNetwork;

		new Thread( new Runnable() {
			public void run() {
				try {
					while(true) {
						Thread.sleep(network);

						//TODO: print All On File
						printNetOnFile(outNetwork, np);
					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
		).start();


		final int nwb = periodLogNWB;

		new Thread( new Runnable() {
			public void run() {
				try {
					while(true) {
						Thread.sleep(nwb);

						//TODO: print NWB
						printInfoForNwb(np);
					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
		).start();

		System.out.println("Port number cluseterListener " + (portNum + 2));
		new Thread(new ClusterPeerListener(peerId, InetAddress.getLocalHost().toString(), portNum+2), "Cluster Peer Listener Thread").start();
	}

	/**
	 * Port number of peer
	 */
	public static int portNum;

	/**
	 * Identifier of peer
	 */
	public static String peerId;

	/**
	 * Information about peer
	 */
	public static NetPeer peer;

	/**
	 * Stream used for store output information about creations
	 */
	public static PrintStream outCreation;

	/**
	 * Stream used for store output information about searches
	 */
	public static PrintStream outSearch;

	/**
	 * Stream used for store information about resources
	 */
	public static PrintStream outResources;

	/**
	 * Function for create new resource
	 *
	 * @param time timestamp of resource creation
	 */
	public static void createResource(String time){

		Random rand = new Random();
		int num = rand.nextInt(10000);
		String key = "";
		key = BigInteger.valueOf(num).toString(16);

		outCreation.println(time + ":RESOURCE_CREATION:" + key);

		NetPeerInfo npi = null;
		try {
			npi = new NetPeerInfo(InetAddress.getLocalHost().toString(), portNum, "");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NetResourceInfo res = new NetResourceInfo(key, npi, peerId);
		peer.addResourceOnCache(res);
		peer.printResourceCache();
	}

	/**
	 * Function for search resource
	 *
	 * @param time timestamp of resource search
	 * @param threadId identifier of thread which manage the search
	 * @throws InterruptedException 
	 */
	public static void search(String time, String threadId) throws InterruptedException {
		Random rand = new Random();
		int num = rand.nextInt(10000);
		String key = BigInteger.valueOf(num).toString(16);

		peer.searchResource(key, threadId);

		if (peer.getResourceOnCache().containsKey(key)){
			outSearch.println(time + ":SEARCH:" + key + ":FINDED");
		}
		else{
			outSearch.println(time + ":SEARCH:" + key + ":NOT_FINDED");
		}

	}

	/**
	 * Function for print all resource on cache
	 *
	 * @param time timestamp of cache print
	 */
	public static void printCache(String time){
		outResources.println(time + ":CACHE_RESOURCE:");
		if (peer.getResourceOnCache().size() == 0)
			return;

		Set<String> key_set = peer.getResourceOnCache().keySet();
		Iterator<String> iter = key_set.iterator();
		while(iter.hasNext()){
			String key = iter.next();
			NetResourceInfo res = peer.getResourceOnCache().get(key);
			outResources.println(key + " ( " + res.getResourceKey() + " ) " + "owned by " + res.getOwnerId());
		}

		outResources.println("******************");
	}

	/**
	 * Function for print all other peer know
	 *
	 * @param out the output stream
	 * @param np the peer analyzed
	 */
	public static void printNetOnFile(PrintStream out, NetPeer np){
		out.println(System.currentTimeMillis() + ":" + "NETWORK:");

		out.println("Successor: " + np.getSuccessorId());
		out.println("Predecessor: " + np.getPredecessorId());

		out.println("Print finger entry...");
		for (int i=0; i< np.getFingerEntry().size(); i++){
			System.out.println(np.getFingerEntry().get(i));
		}

		out.println("Ordered keys on finger table...");
		Set<String> key_set = np.getFingerTable().keySet();
		Iterator<String> iter = key_set.iterator();
		while (iter.hasNext()){
			String key = iter.next();
			out.println(key + " on " + np.getFingerTable().get(key).getIpAddress() + ":" + np.getFingerTable().get(key).getPortNumber());
		}

		out.println("******************");
	}

	/**
	 * Function for print information for NWB
	 *
	 * @param np the peer analyzed
	 */
	public static void printInfoForNwb(NetPeer np){
		FileOutputStream fileNWB = null;
		try {
			//fileNWB = new FileOutputStream(portNum + "ChordNWB" + (new Long(System.currentTimeMillis())).toString() + ".txt");
			fileNWB = new FileOutputStream("ChordNWB.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    PrintStream outNWB = new PrintStream(fileNWB);

	    outNWB.println(np.getMyId());
	    if (np.getSuccessorId() == null || np.getSuccessorId().compareTo("null") == 0 || np.getSuccessorId().compareTo("") == 0)
	    	outNWB.println("NULL");
	    else
	    	outNWB.println(np.getSuccessorId());
	    if (np.getPredecessorId() == null || np.getPredecessorId().compareTo("null") == 0 || np.getPredecessorId().compareTo("") == 0)
	    	outNWB.println("NULL");
	    else
	    	outNWB.println(np.getPredecessorId());

	    Set<String> key_set = np.getFingerTable().keySet();
	    Iterator<String> iter = key_set.iterator();
	    while(iter.hasNext()){
	    	String key = iter.next();
	    	outNWB.println(key);
	    }
	}
}
