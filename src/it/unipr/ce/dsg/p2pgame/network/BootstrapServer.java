package it.unipr.ce.dsg.p2pgame.network;

import it.simplexml.message.AckMessage;
import it.simplexml.message.Message;
import it.simplexml.message.MessageReader;
import it.simplexml.message.PingMessage;
import it.simplexml.sender.MessageSender;
import it.unipr.ce.dsg.p2pgame.network.message.BootNetMessageListener;
import it.unipr.ce.dsg.p2pgame.util.SHA1;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/**
 *
 * Bootstrap server useful for give an access to an existing
 * Chord-like network
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class BootstrapServer {

	/**
	 * Only for debug
	 */
	public int num=0;

	/**
	 * Cache the last connected user with their address
	 */
	private HashMap<String, NetPeerInfo> lastConnectedUser = new HashMap<String, NetPeerInfo>();

	/**
	 * Size of cache for last connected peer
	 */
	private final int maxSizeOfPeerCache;

	/**
	 * The max distance in bit between two peer in the Chord-like ring
	 */
	private BigInteger maxDist = BigInteger.valueOf(2L);

	/**
	 * The max distance on String between two peer in the Chord-like ring
	 */
	private final int maxDistString;

	/**
	 * Port for incoming communication
	 */
	private int inputPort;

	/**
	 * Port for outcoming communication
	 */
	private int outputPort;

	/**
	 *
	 * Constructor for a Bootstrap server on a Chord-like network.
	 * When peers wants to access to network ask to bootstrap server if there are just
	 * a ring and eventually which is there possible position
	 *
	 * @param outputPort the port for outcoming message
	 * @param inputPort this port for incoming message
	 * @param sizeOfPeerCache the dimension of size of last connected peer
	 * @param idBitLength length of peer identifier
	 * @throws IOException at opening of server socket
	 *
	 */
	public BootstrapServer(int outputPort, int inputPort, int sizeOfPeerCache, int idBitLength) throws IOException {

		this.inputPort = inputPort;
		this.outputPort = outputPort;

		this.maxSizeOfPeerCache = sizeOfPeerCache;

		this.maxDist = (this.maxDist.pow(idBitLength)).subtract(BigInteger.valueOf(1L));
		System.out.println("MAX DIST " + this.maxDist.toString());

		System.out.println("id bit " +  idBitLength);
		int hexLength = idBitLength/4;
		System.out.println("id hex " + hexLength);
		StringBuffer min = new StringBuffer();
		StringBuffer max = new StringBuffer();

		for (int i = 0; i < hexLength; i++)
		{
			min.append('0');
			max.append('f');
		}

		System.out.println("MIN " + min + " with dimension " + min.toString().length());
		System.out.println("MAX " + max);
		this.maxDistString = (max.toString()).compareTo( (min.toString()) );
		System.out.println("Max dist " + this.maxDistString);

		System.out.println("Bootstrap server lunch thread listener ...");
		Thread messageListener = new Thread(new BootNetMessageListener(this, "Bootstrap server", "127.0.0.1", this.inputPort), "Server Listener Thread");
		messageListener.start();

	}

	/**
	 *
	 * Give the closet successor of peer with id 'peerId' reading from last connected
	 * peer cache.
	 * First of all verify that peers are again online.
	 *
	 * @param peerId the id of peer by which is required the successor
	 * @return the closet successor for input peer
	 *
	 */
	public String closetSuccessor(String peerId){

		this.num++;
		System.out.println("Call CLOSET : " + this.num);
		this.updateCache();

		String aa = "abcd03";
		System.out.println(aa + " converted " + SHA1.convertFromStringToBig(aa).toString());

		System.out.println("peerId " + peerId);
		BigInteger peerPos = SHA1.convertFromStringToBig(peerId);
		System.out.println("PeerPos " + peerPos.toString());

		System.out.println("Research on last connected peer for closet successor");

		BigInteger diff = this.maxDist;

		String successor = null;
		//int diffString = this.maxDistString;

		Set<String> key_set = this.lastConnectedUser.keySet();
		Iterator<String> iter = key_set.iterator();

		for (int i=0; i < this.lastConnectedUser.size(); i++){

			String key = iter.next();
			BigInteger onTab = SHA1.convertFromStringToBig(key);
			System.out.println("Compare to onTab " + onTab);

			//System.out.println("test " + i + " with distance " + key.compareTo(peerId));
			System.out.println("test n. " + i + " with distance " + onTab.subtract(peerPos).toString());
			System.out.println("diff " + diff.toString());

			if ( onTab.compareTo(peerPos) > 0  &&  diff.compareTo(onTab.subtract(peerPos)) > 0 ){
				System.out.println("PIU");
				System.out.println("FINDED a SUCCESSOR");
				//System.out.println("old distance " + diffString);
				System.out.println("old distance " + diff.toString());

				diff = onTab.subtract(peerPos);
				//diffString = key.compareTo(peerId);
				//System.out.println("new distance " + diffString);
				System.out.println("new distance " + diff.toString());
				successor = key;
			}
			else if (onTab.compareTo(peerPos) <= 0 && diff.compareTo((onTab.subtract(peerPos)).add(this.maxDist)) > 0 ) {
				System.out.println("MENO");
				System.out.println("FINDED a SUCCESSOR");
				//System.out.println("old distance " + diffString);
				System.out.println("old distance " + diff.toString());

				//diffString = key.compareTo(peerId) + this.maxDistString;
				diff = (onTab.subtract(peerPos)).add(this.maxDist);

				//System.out.println("new distance " + diffString);
				System.out.println("new distance " + diff.toString());
				successor = key;
			}


		}

		return successor;
	}


	/**
	 *
	 * Update cache of last connected peer.
	 * Test for all peer on cache if there are again online with Ping message
	 *
	 */
	private void updateCache(){

		Set<String> key_set = this.lastConnectedUser.keySet();
		Iterator<String> iter = key_set.iterator();

		String myUserName = "Bootstrap Server";
		String myIpAddr = null;
		try {
			myIpAddr = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			System.err.println("Local IP address not available");
			e.printStackTrace();
		}


		int i=0;
		while (iter.hasNext()){
			String key = iter.next();

			System.out.println("Verifying if peer is online( " + i + " ): " + key);

			NetPeerInfo npi = this.lastConnectedUser.get(key);
			String destIpAddr = npi.getIpAddress();
			int destPort = npi.getPortNumber();

			PingMessage pingMessage = new PingMessage(myUserName,myIpAddr, this.outputPort);

			System.out.println("Sending Ping to : " + destIpAddr + ":" + destPort);
			String responseMessage = MessageSender.sendMessage(destIpAddr, destPort, pingMessage.generateXmlMessageString());


			if(responseMessage.contains("ERROR"))
			{
				System.err.println("Sending Ping Message ERROR !");
				this.lastConnectedUser.remove(key);
				key_set = this.lastConnectedUser.keySet();
				iter = key_set.iterator();
			}
			else
			{
				MessageReader messageReader = new MessageReader();
				Message receivedMessage = messageReader.readMessageFromString(responseMessage.trim());

				AckMessage ackMessage = new AckMessage(receivedMessage);

				//If ack message status is 0
				if(ackMessage.getAckStatus() == 0)
					System.out.println("Message sent ... ");
				else {
					this.lastConnectedUser.remove(key);
					key_set = this.lastConnectedUser.keySet();
					iter = key_set.iterator();
				}
			}


			i++;
		}

		System.out.println("exit UPDATE correctly");
	}

	/**
	 *
	 * Add the input peer on cache. If cache is full delete oldest connected peer
	 *
	 * @param hostName host name of last connected peer
	 * @param ipAddr IP address of last connected peer
	 * @param port TCP port number of last connected peer
	 *
	 */
	public void savePeerOnCache(String hostName, String ipAddr, int port){

		System.out.println("PREVIOUS insert : " + this.lastConnectedUser.size());

		this.printLastConnectedUser();

		if (this.lastConnectedUser.size() >= this.maxSizeOfPeerCache)
		{
			System.out.println("DELETE oldest");
			Set<String> key_set = this.lastConnectedUser.keySet();
			Iterator<String> iter = key_set.iterator();
			this.lastConnectedUser.remove(iter.next());
		}
		System.out.println("PREVIOUS insert 2: " + this.lastConnectedUser.size());

			NetPeerInfo npi = new NetPeerInfo(ipAddr, port, null);
			this.lastConnectedUser.put(hostName, npi);
			System.out.println("last connected peer " + this.lastConnectedUser.size());

		this.printLastConnectedUser();
	}

	/**
	 *
	 * Get the last connected peer which are on cache
	 *
	 * @return the cache with last connected peers
	 */
	public HashMap<String, NetPeerInfo> getLastConnectedUser() {
		return lastConnectedUser;
	}

	/**
	 *
	 * Print all peer on cache
	 *
	 */
	private void printLastConnectedUser() {

		System.out.println("PRINT all Connected User on cache");
		Set<String> key_set = this.lastConnectedUser.keySet();
		Iterator<String> iter = key_set.iterator();

		for (int i = 0; i < this.lastConnectedUser.size(); i++){

			String key = iter.next();
			NetPeerInfo np = this.lastConnectedUser.get(key);
			System.out.println(key + " on " + np.getIpAddress() + ":" + np.getPortNumber());

		}

	}

}
