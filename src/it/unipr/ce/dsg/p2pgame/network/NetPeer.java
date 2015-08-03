package it.unipr.ce.dsg.p2pgame.network;

import it.simplexml.message.AckMessage;
import it.simplexml.message.Message;
import it.simplexml.message.MessageReader;
import it.simplexml.message.PingMessage;
import it.simplexml.sender.MessageSender;
import it.unipr.ce.dsg.p2pgame.GUI.RTSGameGUI;
import it.unipr.ce.dsg.p2pgame.network.message.ConnectMessage;
import it.unipr.ce.dsg.p2pgame.network.message.FindSuccMessage;
import it.unipr.ce.dsg.p2pgame.network.message.GetPredMessage;
import it.unipr.ce.dsg.p2pgame.network.message.GetResourceMessage;
import it.unipr.ce.dsg.p2pgame.network.message.InfoNewPredMessage;
import it.unipr.ce.dsg.p2pgame.network.message.InfoNewSuccMessage;
import it.unipr.ce.dsg.p2pgame.network.message.InfoPeerMessage;
import it.unipr.ce.dsg.p2pgame.network.message.NetMessageListener;
import it.unipr.ce.dsg.p2pgame.network.message.NotifyMessage;
import it.unipr.ce.dsg.p2pgame.network.message.PublishResourceMessage;
import it.unipr.ce.dsg.p2pgame.util.MultiLog;
import it.unipr.ce.dsg.p2pgame.util.SHA1;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * Peer of Chord-like network. It's able to publish and search resource.
 * Also have all function of Chord peer.
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class NetPeer {

	/**
	 * Cache for resource
	 */
	private ConcurrentMap<String, NetResourceInfo> resourceOnCache = null;

	/**
	 * Info on predecessor peer
	 */
	private NetPeerInfo predecessor = null;
	private String predecessorId = null;

	/**
	 * Info on successor peer
	 */
	private NetPeerInfo successor = null;
	private String successorId = null;

	/**
	 * Shared resource between all thread
	 */
//	private NetSharedResource sharedInfos = null;

	/**
	 * Identifier of this thread
	 */
	private String myThreadId = new Long(Thread.currentThread().getId()).toString();

	/**
	 * Finger table where key is 'id' and value is the 'peer info'.
	 * Only different peer of finger entry are stored on finger table.
	 */
	private ConcurrentMap<String, NetPeerInfo> fingerTable = null;

	/**
	 * Array of peer id for finger table required by Chord protocol
	 */
	private ArrayList<String> fingerEntry = null;

	/**
	 * The dimension of key space
	 */
	private BigInteger dimKeySpace = BigInteger.valueOf(2L);

	/**
	 * The dimension of finger table.
	 * Is the 'm' parameter on document of Chord protocol.
	 */
	private int m;

	/**
	 * Next element to correct on finger table by periodically request
	 */
	private int next = 0;

	/**
	 * Port number for incoming request
	 */
	private int inputPort;

	/**
	 * Port number used for outcoming operation
	 */
	private int outputPort;

	/**
	 * Info on bootstrap server for network
	 */
	private String serverAddress;
	private int serverPort;

	/**
	 * Thread which manage message listener
	 */
	private Thread messageListener = null;

	/**
	 * The information of peer in Chord-like ring
	 */
	private String myId = null;
	private NetPeerInfo myPeer = null;

	/**
	 *
	 * Constructor of Chord-like peer. Are requested information about TCP in / out port,
	 * id of peer and it's length, and bootstrap server address and port.
	 *
	 * @param inPort TCP port number used by peer for incoming request
	 * @param outPort TCP port number used by peer for outcoming request
	 * @param idBitLength bit length of identifier of peer on Chord-like network
	 * @param id identifier of peer on network
	 * @param serverAddr IP address of bootstrap server
	 * @param serverPort TCP port number of bootstrap server
	 *
	 */
	public NetPeer(int inPort, int outPort, int idBitLength, String id, String serverAddr, int serverPort ) {

		try {

			this.inputPort = inPort;
			this.outputPort = outPort;
			this.serverAddress = serverAddr;
			this.serverPort = serverPort;

			MultiLog.println(NetPeer.class.toString(), "local Address : " + InetAddress.getLocalHost().getHostAddress());
			//System.out.println("local Address : " + InetAddress.getLocalHost().getHostAddress());
			MultiLog.println(NetPeer.class.toString(), "local Name : " + InetAddress.getLocalHost().getHostName());
			//System.out.println("local Name : " + InetAddress.getLocalHost().getHostName());
			this.myPeer = new NetPeerInfo(InetAddress.getLocalHost().getHostAddress(), this.inputPort, InetAddress.getLocalHost().getHostName());
			

		} catch (UnknownHostException e) {
			System.err.println("Peer ins't online.");
			e.printStackTrace();
		}

		this.m = idBitLength;
		this.dimKeySpace = this.dimKeySpace.pow(idBitLength);

		this.predecessor = new NetPeerInfo("", -1, "");
		this.successor = new NetPeerInfo("", -1, "");

		this.fingerTable = new ConcurrentHashMap<String, NetPeerInfo>();
		this.fingerEntry = new ArrayList<String>();
		this.myId = id;

		this.resourceOnCache = new ConcurrentHashMap<String, NetResourceInfo>();

	//	this.sharedInfos = new NetSharedResource();
	}


	/**
	 *
	 * Connect to bootstrap server for obtain information of Chord-like peer
	 * which can be successor and for access to network.
	 *
	 */
	public void getAccessToNetwork() {

		//For verifying if bootstrap-server is online
		boolean resp = this.testIfPeerAlive(this.serverAddress, this.serverPort);
		if (resp){
			MultiLog.println(NetPeer.class.toString(), "Peer is online");
			//System.out.println("Peer is online");
		}

		ConnectMessage connectMessage = new ConnectMessage(this.myId, this.myPeer.getIpAddress(), this.inputPort);

		MultiLog.println(NetPeer.class.toString(), "Sending Connect Message to : " + this.serverAddress + ":" + this.serverPort);
		//System.out.println("Sending Connect Message to : " + this.serverAddress + ":" + this.serverPort);

		String responseMessage = MessageSender.sendMessage(this.serverAddress, this.serverPort, connectMessage.generateXmlMessageString());

		MultiLog.println(NetPeer.class.toString(), "Verify response...");
		//System.out.println("Verify response...");
		if(responseMessage.contains("ERROR")) {

			System.err.println("Sending Connect Message ERROR !");
			MultiLog.println(NetPeer.class.toString(), "Retry later...");
			//System.out.println("Retry later...");
			return;

		}
		else {
			MultiLog.println(NetPeer.class.toString(), "Reading response...");
			//System.out.println("Reading response...");
			MessageReader messageReader = new MessageReader();
			Message receivedMessage = messageReader.readMessageFromString(responseMessage.trim());

			MultiLog.println(NetPeer.class.toString(), "Reconstruct info peer response");
			//System.out.println("Reconstruct info peer response");
			InfoPeerMessage infoPeerMessage = new InfoPeerMessage(receivedMessage);

			MultiLog.println(NetPeer.class.toString(), "id received " + infoPeerMessage.getPeerId());
			//System.out.println("id received " + infoPeerMessage.getPeerId());
			MultiLog.println(NetPeer.class.toString(), "address received " + infoPeerMessage.getPeerAddr() + ":" + infoPeerMessage.getPeerPort());
			//System.out.println("address received " + infoPeerMessage.getPeerAddr() + ":" + infoPeerMessage.getPeerPort());

			//verifying response
			if(infoPeerMessage.getPeerAddr().equals("") ) {
				MultiLog.println(NetPeer.class.toString(), "Must be created another network (NEW)");
				//System.out.println("Must be created another network (NEW)");
				this.create();
			} else {
				MultiLog.println(NetPeer.class.toString(), "Response received.");
				//System.out.println("Response received.");
				NetPeerInfo npi = new NetPeerInfo(infoPeerMessage.getPeerAddr(), infoPeerMessage.getPeerPort(), null);
				String id = infoPeerMessage.getPeerId();
				MultiLog.println(NetPeer.class.toString(), "Try join with " + id);
				//System.out.println("Try join with " + id);
				this.join(npi, id);
				MultiLog.println(NetPeer.class.toString(), "Stabilize for notify predecessor to my for successor");
				//System.out.println("Stabilize for notify predecessor to my for successor");
				this.stabilize();
			}

			MultiLog.println(NetPeer.class.toString(), "Predecessor " + this.predecessorId);
			//System.out.println("Predecessor " + this.predecessorId);
			MultiLog.println(NetPeer.class.toString(), "Successor " + this.successorId);
			//System.out.println("Successor " + this.successorId);

			//the peer is online and must create a listener for incoming message
			this.createMessageListener();
		}

	}

	/**
	 *
	 * Create a new message listener for incoming message request on another
	 * thread
	 *
	 */
	private void createMessageListener(){
		if (this.messageListener != null)
			return;
		MultiLog.println(NetPeer.class.toString(), "Peer in the network lunch thread listener ...");
		//System.out.println("Peer in the network lunch thread listener ...");

		this.messageListener = new Thread(new NetMessageListener(this, this.myId, this.myPeer.getIpAddress(), this.myPeer.getPortNumber()), "Peer Listener Thread");
		this.messageListener.setPriority(Thread.MAX_PRIORITY);
		this.messageListener.start();
	}


	/**
	 *
	 * Find the successor peer with identifier 'id'. Response information will be saved on
	 * shared resource for owner 'respOwner'
	 *
	 * @param id the id of peer / resource for which is required the successor
	 * @param respOwner which peer forward the request
	 * @return the identifier of successor peer
	 * @throws InterruptedException 
	 *
	 */
	public InfoPassing findSuccessor(String id, String respOwner) throws InterruptedException{
		String successor = "";
		InfoPassing succ = null;
		//System.out.println("NetPeer--->findSuccessor id="+id+" respOwner="+respOwner);
		if (id != null && this.successorId != null && id.compareTo("") != 0 && this.successorId.compareTo("") != 0){
			MultiLog.println(NetPeer.class.toString(), "FindSucc test se: " + id + " ï¿½( " + this.myId + " , " + this.successorId + " ]");
			//System.out.println("FindSucc test se: " + id + " ï¿½( " + this.myId + " , " + this.successorId + " ]");
			if (isInInterval(id, this.myId, this.successorId, false, true)) {

				MultiLog.println(NetPeer.class.toString(), "Return id and info of SUCCESSOR node");
				//System.out.println("Return id and info of SUCCESSOR node");
				successor = this.successorId;
				//System.out.println("findSuccesor--Ininterval "+successor+" "+this.successor.getIpAddress()+" , "+this.successor.getPortNumber());
				//System.out.println("findSucessor1");
				//this.saveOnCache(this.successorId, this.successor, respOwner);
				succ = new InfoPassing(this.successor, this.successorId);
				//this.saveOnCache(this.successorId, this.successor, successor);

			} else {

				MultiLog.println(NetPeer.class.toString(), "call closetPrecedingNode");
				//System.out.println("call closetPrecedingNode");
				String cp = this.closetPrecedingNode(id);

				if (cp.compareTo(this.myId) != 0){
					MultiLog.println(NetPeer.class.toString(), "CALL find on node cp: " + cp + " for " + id);
					//System.out.println("CALL find on node cp: " + cp + " for " + id);

					MultiLog.println(NetPeer.class.toString(), "REQUEST to search ID");
					//System.out.println("REQUEST to search ID");
					//System.out.println("findSucessor2");
					
					///XXX: cambio con struttura
					//successor = this.requestToFindSuccessor(cp, id, respOwner);
					succ = this.requestToFindSuccessor(cp, id, respOwner);

				} else {
					successor = this.myId;
					//System.out.println("findSuccesor--IsMyID "+respOwner+" "+this.myPeer.getIpAddress()+" , "+this.myPeer.getPortNumber());
					//System.out.println("findSucessor3");
					//this.saveOnCache(this.myId, this.myPeer, respOwner);
					succ = new InfoPassing(this.myPeer, this.myId);
				}

			}
		}
		//System.out.println("NetPeer--->findSuccessor "+id+" successor= "+successor);
		
		//return successor;
		return succ;
	}

	/**
	 *
	 * Request to another peer ('to') the successor of 'id'. 'reqOwner' define for
	 * which peer must be saved the response
	 *
	 * @param to peer to which is forward the request
	 * @param id identifier by which is requested the successor
	 * @param reqOwner the identifier of thread for which save response
	 * @return id received on response
	 * @throws InterruptedException 
	 *
	 */
	private InfoPassing requestToFindSuccessor(String to, String id, String reqOwner) throws InterruptedException {
		FindSuccMessage findSuccMessage = new FindSuccMessage(this.myId, this.myPeer.getIpAddress(), this.outputPort, id);
		//System.out.println("NetPeer--->RequestToFindSuccessor to="+to+" id="+id+" reqOwner="+reqOwner);
		String destAddr = "";
		int destPort = -1;

		//String resp = "";
		InfoPassing resp = null;

		if (this.fingerTable.containsKey(to)) {
			//System.out.println("--->close Pred in finger table");
			destAddr = this.fingerTable.get(to).getIpAddress();
			destPort = this.fingerTable.get(to).getPortNumber();
		} else if (this.successorId.compareTo(to) == 0) {
			//System.out.println("--->close Pred is the successor");
			destAddr = this.successor.getIpAddress();
			destPort = this.successor.getPortNumber();
		} else if (this.predecessorId.compareTo(to)== 0) {
			//System.out.println("--->close Pred is my predecessor");
			destAddr = this.predecessor.getIpAddress();
			destPort = this.predecessor.getPortNumber();
	/*	} else if (this.sharedInfos.getPeersId().get(reqOwner) != null
				&& this.sharedInfos.getPeersId().get(reqOwner).compareTo(to) == 0) {
			//System.out.println("--->close Pred is in cache");
			destAddr = this.sharedInfos.getPeersInfo().get(reqOwner).getIpAddress();
			destPort = this.sharedInfos.getPeersInfo().get(reqOwner).getPortNumber();*/
		} else {
			//System.out.println("--->this Peer");
			MultiLog.println(NetPeer.class.toString(), "Unable to contact " + to);
			//System.out.println("Unable to contact " + to);
			MultiLog.println(NetPeer.class.toString(), "Use info of THIS peer...");
			//System.out.println("Use info of THIS peer...");
			//resp = id;
			NetPeerInfo np = new NetPeerInfo(this.myPeer.getIpAddress(), this.myPeer.getPortNumber(), "");
			//System.out.println("requesttofindSuccesor--IsMyID "+reqOwner+" "+np.getIpAddress()+" , "+np.getPortNumber());
			//this.saveOnCache(resp, np, reqOwner);
			resp = new InfoPassing(np, id);
			
			return resp;
		}

		//loop avoidance
		if (destAddr.compareTo(this.myPeer.getIpAddress()) == 0 && destPort == this.outputPort){
			//resp = id;
			NetPeerInfo np = new NetPeerInfo(this.myPeer.getIpAddress(), this.myPeer.getPortNumber(), "");
			
			//System.out.println("requesttofindSuccesor--IsMyID2 "+reqOwner+" "+np.getIpAddress()+" , "+np.getPortNumber());
			//this.saveOnCache(resp, np, reqOwner);
			resp = new InfoPassing(np, id);
			return resp;

		}

		MultiLog.println(NetPeer.class.toString(), "Sending successor request to : " + destAddr + ":" + destPort);
		//System.out.println("Sending successor request to : " + destAddr + ":" + destPort);
		//System.out.println(findSuccMessage.generateXmlMessageString());
		String responseMessage = MessageSender.sendMessage(destAddr, destPort, findSuccMessage.generateXmlMessageString());
		//System.out.println("requesToFindSucc Response");
		if(responseMessage.contains("ERROR")) {

			MultiLog.println(NetPeer.class.toString(), "Sending Ping Message ERROR !");
			System.err.println("Sending Ping Message ERROR !");
			MultiLog.println(NetPeer.class.toString(), "Use info of THIS peer...");
			System.out.println("Use info of THIS peer...");
			//resp = id;
			NetPeerInfo np = new NetPeerInfo(this.myPeer.getIpAddress(), this.myPeer.getPortNumber(), "");
			//System.out.println("requesttofindSuccesor--IsMyID3 "+reqOwner+" "+np.getIpAddress()+" , "+np.getPortNumber());
			//this.saveOnCache(resp, np, reqOwner);
			resp = new InfoPassing(np, id);
			return resp;

		}
		else {

			MessageReader messageReader = new MessageReader();
			Message receivedMessage = messageReader.readMessageFromString(responseMessage.trim());

			InfoPeerMessage infoMessage = new InfoPeerMessage(receivedMessage);

			//resp = infoMessage.getPeerId();

			NetPeerInfo np = new NetPeerInfo(infoMessage.getPeerAddr(), infoMessage.getPeerPort(), "");
			//System.out.println("requesttofindSuccesor--anotherID "+reqOwner+" "+np.getIpAddress()+" , "+np.getPortNumber());
			//this.saveOnCache(infoMessage.getPeerId(), np, reqOwner);
			
			resp = new InfoPassing(np, infoMessage.getPeerId());
			//this.saveOnCache(infoMessage.getPeerId(), np, resp);
		}
		return resp;

	}
/*
	/**
	 *
	 * Save the information about peer (id, npi) on relative cache. 'owner'
	 * the owner of this information
	 *
	 * @param id the identifier of peer to be saved
	 * @param npi the information about peer to be saved
	 * @param owner the identifier of thread for which must be saved information
	 * @throws InterruptedException 
	 *
	 */
	/*private*//*public synchronized void saveOnCache(String id, NetPeerInfo npi, String owner) throws InterruptedException {

		//System.out.println("save on cache "+owner+" "+npi.getIpAddress()+" "+npi.getPortNumber());
		this.sharedInfos.saveInfo(owner, npi, id);

	}

*/
	/**
	 *
	 * Search in the local finger table for the highest predecessor of 'id'
	 *
	 * @param id the identifier by which is required the successor
	 *
	 */
	public String closetPrecedingNode (String id){

		String closet = "";

		for (int i = this.fingerEntry.size(); i >= 1; i--) {

			String key = this.fingerEntry.get(i-1);
			MultiLog.println(NetPeer.class.toString(), "Verifying on interval: " + key + " ï¿½ (" + this.myId + "," + id + ")");
			//System.out.println("Verifying on interval: " + key + " ï¿½ (" + this.myId + "," + id + ")");
			if( isInInterval(key, this.myId, id, false, false) ) {

				closet = key;
				MultiLog.println(NetPeer.class.toString(), "SEND id and INFO of " + closet);
				//System.out.println("SEND id and INFO of " + closet);
				return closet;
			}
		}

		MultiLog.println(NetPeer.class.toString(), "RETURN info of this node");
		//System.out.println("RETURN info of this node");
		closet = this.myId;

		return closet;
	}


	/**
	 *
	 * Create a new Chord-like ring.
	 *
	 */
	public void create(){

		this.predecessor = null;
		this.predecessorId = null;
		this.successor = this.myPeer;
		this.successorId = this.myId;

	}


	/**
	 *
	 * Join to a Chord-like ring containing node 's'
	 *
	 * @param p the info of successor peer
	 * @param pId the identifier of successor peer
	 *
	 */
	public /*synchronized*/ void join(NetPeerInfo p, String pId){

		this.predecessor = null;

		FindSuccMessage findSuccMessage = new FindSuccMessage(this.myId, this.myPeer.getIpAddress(), this.outputPort, this.myId);

		String destAddr = p.getIpAddress();
		int destPort = p.getPortNumber();


		MultiLog.println(NetPeer.class.toString(), "Sending successor request to : " + destAddr + ":" + destPort);
		//System.out.println("Sending successor request to : " + destAddr + ":" + destPort);
		String responseMessage = MessageSender.sendMessage(destAddr, destPort, findSuccMessage.generateXmlMessageString());

		if(responseMessage.contains("ERROR")) {

			System.err.println("Sending Ping Message ERROR !");
			MultiLog.println(NetPeer.class.toString(), "Unable to JOIN. Try later");
			//System.out.println("Unable to JOIN. Try later");
			return;

		}
		else {

			MessageReader messageReader = new MessageReader();
			Message receivedMessage = messageReader.readMessageFromString(responseMessage.trim());

			InfoPeerMessage infoMessage = new InfoPeerMessage(receivedMessage);

			this.successor = new NetPeerInfo(infoMessage.getPeerAddr(), infoMessage.getPeerPort(), "");
			this.successorId = infoMessage.getPeerId();

		}

	}


	/**
	 *
	 * Called periodically for stabilize the successor on ring and keep
	 * consistency of network.
	 *
	 */
	public /*synchronized*/ void stabilize(){

		NetPeerInfo x = null;
		String xId = "";

		MultiLog.println(NetPeer.class.toString(), "ask to predecessor for my successor");
		//System.out.println("ask to predecessor for my successor");

		if (this.successor == null){
			MultiLog.println(NetPeer.class.toString(), "no-one successor for asking");
			//System.out.println("no-one successor for asking");
			return;
		}

		GetPredMessage getSuccMessage = new GetPredMessage(this.myId, this.myPeer.getIpAddress(), this.outputPort);

		String destAddr = this.successor.getIpAddress();
		int destPort = this.successor.getPortNumber();

		//loop avoidance
		if (destAddr.compareTo(this.myPeer.getIpAddress()) == 0 && destPort == this.outputPort){
			x = this.predecessor;
			xId = this.predecessorId;
			MultiLog.println(NetPeer.class.toString(), "Info from INNER");
			//System.out.println("Info from INNER");
		}
		else{
			MultiLog.println(NetPeer.class.toString(), "Sending successor request to : " + destAddr + ":" + destPort);
			//System.out.println("Sending successor request to : " + destAddr + ":" + destPort);
			String responseMessage = MessageSender.sendMessage(destAddr, destPort, getSuccMessage.generateXmlMessageString());

			if(responseMessage.contains("ERROR")) {

				System.err.println("Sending Ping Message ERROR !");
				MultiLog.println(NetPeer.class.toString(), "Now successor is null !!!");
				//System.out.println("Now successor is null !!!");
				this.successor = null;
				this.successorId = null;
				this.printAllInfo();
				this.tryToGetSucc();
				return;

			}
			else {

				MessageReader messageReader = new MessageReader();
				Message receivedMessage = messageReader.readMessageFromString(responseMessage.trim());

				InfoPeerMessage infoMessage = new InfoPeerMessage(receivedMessage);

				x = new NetPeerInfo(infoMessage.getPeerAddr(), infoMessage.getPeerPort(), "");
				xId = infoMessage.getPeerId();
			}
		}

		MultiLog.println(NetPeer.class.toString(), "Info Peer received...");
		//System.out.println("Info Peer received...");

		if (xId != null && this.successorId != null && xId.compareTo("") != 0 && this.successorId.compareTo("") != 0){
			MultiLog.println(NetPeer.class.toString(), "Response received isn't null...");
			//System.out.println("Response received isn't null...");
			MultiLog.println(NetPeer.class.toString(), "SuccId " + xId);
			//System.out.println("SuccId " + xId);
			MultiLog.println(NetPeer.class.toString(), "on " + x.getIpAddress() + ":" + x.getPortNumber());
			//System.out.println("on " + x.getIpAddress() + ":" + x.getPortNumber());

			if (isInInterval(xId, this.myId, this.successorId, false, false)){
				this.successorId = xId;
				this.successor = x;
			}

		}

		if ( this.successor == null || this.successor.getPortNumber() == -1){
			MultiLog.println(NetPeer.class.toString(), "Successor NULL");
			//System.out.println("Successor NULL");
			this.printAllInfo();
			this.tryToGetSucc();
			return;
		}

		MultiLog.println(NetPeer.class.toString(), "Send a notify message with this");
		//System.out.println("Send a notify message with this");
		NotifyMessage notifyMessage = new NotifyMessage(this.myId, this.myPeer.getIpAddress(), this.inputPort);

		String notifyDestAddr = this.successor.getIpAddress();
		int notifyDestPort = this.successor.getPortNumber();


		MultiLog.println(NetPeer.class.toString(), "Sending successor request to : " + notifyDestAddr + ":" + notifyDestPort);
		//System.out.println("Sending successor request to : " + notifyDestAddr + ":" + notifyDestPort);
		String notifyResponseMessage = MessageSender.sendMessage(notifyDestAddr, notifyDestPort, notifyMessage.generateXmlMessageString());

		if(notifyResponseMessage.contains("ERROR")) {

			System.err.println("Sending Notify Message ERROR !");
			return;

		}
		else {

			MessageReader notifyMessageReader = new MessageReader();
			Message notifyReceivedMessage = notifyMessageReader.readMessageFromString(notifyResponseMessage.trim());

			AckMessage ackMessage = new AckMessage(notifyReceivedMessage);

			//If ack message status is 0
			if(ackMessage.getAckStatus() == 0) {

				MultiLog.println(NetPeer.class.toString(), "Message sent correctly... ");
				//System.out.println("Message sent correctly... ");

			}
		}

	}

	/**
	 *
	 * Notify that peer from argument might be our predecessor
	 *
	 * @param peerId the identifier of possible predecessor
	 * @param peer the information about possible predecessor
	 *
	 */
	public /*synchronized*/ void notify(String peerId, NetPeerInfo peer){

		MultiLog.println(NetPeer.class.toString(), "Received a NOTIFY");
		//System.out.println("Received a NOTIFY");

		if (this.predecessor == null || this.isInInterval(peerId, this.predecessorId, this.myId, false, false)){
			MultiLog.println(NetPeer.class.toString(), "UPDATED predecessor");
			//System.out.println("UPDATED predecessor");
			this.predecessor = peer;
			this.predecessorId = peerId;
			MultiLog.println(NetPeer.class.toString(), "New Predecessor " + this.predecessorId);
			//System.out.println("New Predecessor " + this.predecessorId);
		}

	}

	/**
	 *
	 * Refresh finger table entries. Verify 'next' element of finger table
	 * and try to fix it.
	 * At position 'next' of finger table should be successor of: id + 2^(next-1)
	 * Must be called periodically.
	 * @throws InterruptedException 
	 *
	 */
	public /*synchronized*/ void fixFinger(String threadId) throws InterruptedException{
		//System.out.println("fixfinger");
		MultiLog.println(NetPeer.class.toString(), "select NEXT entry of finger table and fix it");
		//System.out.println("select NEXT entry of finger table and fix it");

		MultiLog.println(NetPeer.class.toString(), "Pre - FIX");
		//System.out.println("Pre - FIX");
		this.printFingerTable();

		this.next++;
		if (this.next > this.m){
			MultiLog.println(NetPeer.class.toString(), "NEW TURN on Finger Table");
			//System.out.println("NEW TURN on Finger Table");
			this.next = 1;
		}

		MultiLog.println(NetPeer.class.toString(), "NEXT: " + this.next);
		//System.out.println("NEXT: " + this.next);
		BigInteger myNum = SHA1.convertFromStringToBig(this.myId);

		BigInteger toFind = myNum.add(BigInteger.valueOf(2L).pow(this.next - 1));
		MultiLog.println(NetPeer.class.toString(), "research successor for " + toFind.toString());
		//System.out.println("research successor for " + toFind.toString());
		while (toFind.compareTo(this.dimKeySpace) >= 0) {
			toFind = toFind.subtract(this.dimKeySpace);
		}
		MultiLog.println(NetPeer.class.toString(), "MODULE value is " + toFind.toString());
		//System.out.println("MODULE value is " + toFind.toString());



		//String elementId = this.findSuccessor(toFind.toString(16), this.myThreadId);
		//System.out.println("NetPeer---->fixFinger");
		InfoPassing info = this.findSuccessor(toFind.toString(16), threadId);
		String elementId = info.getPeerID();
		if (elementId != null && elementId.compareTo("") != 0){
			MultiLog.println(NetPeer.class.toString(), "elementId for next: " + elementId);
			//System.out.println("elementId for next: " + elementId );

			//System.out.println("info element: " + this.sharedInfos.getInfoFor(this.myThreadId).getIpAddress() + ":" + this.sharedInfos.getInfoFor(this.myThreadId).getPortNumber());
			//MultiLog.println(NetPeer.class.toString(), "info element: " + this.sharedInfos.getInfoFor(threadId).getIpAddress() + ":" + this.sharedInfos.getInfoFor(threadId).getPortNumber());
			MultiLog.println(NetPeer.class.toString(), "info element: " + info.getPeerData().getIpAddress() + ":" + info.getPeerData().getPortNumber());
			//System.out.println("info element: " + this.sharedInfos.getInfoFor(threadId).getIpAddress() + ":" + this.sharedInfos.getInfoFor(threadId).getPortNumber());

			MultiLog.println(NetPeer.class.toString(),"PRE-Finger ENTRY");
			//System.out.println("PRE-Finger ENTRY");
			String oldNext = "";
			this.printFingerEntry();
			if (this.next -1 < this.fingerEntry.size()){
				oldNext = this.fingerEntry.get(this.next-1);
				this.fingerEntry.set(this.next-1, elementId);
			}else
				this.fingerEntry.add(this.next-1, elementId);
			MultiLog.println(NetPeer.class.toString(), "POST-Finger ENTRY");
			//System.out.println("POST-Finger ENTRY");
			this.printFingerEntry();

			if (this.fingerTable.containsKey(oldNext))
				this.fingerTable.remove(oldNext);
			//this.fingerTable.put(elementId, this.sharedInfos.getInfoFor(this.myThreadId));
			//this.fingerTable.put(elementId, this.sharedInfos.getInfoFor(threadId));
			this.fingerTable.put(elementId, info.getPeerData());

			MultiLog.println(NetPeer.class.toString(), "Post - FIX");
			//System.out.println("Post - FIX");
			this.printFingerTable();
		} else {
			MultiLog.println(NetPeer.class.toString(), "Finger Entry " + (this.next-1) + " is empty");
			//System.out.println("Finger Entry " + (this.next-1) + " is empty");
			MultiLog.println(NetPeer.class.toString(), "Test cleaning fingerTable");
			//System.out.println("Test cleaning fingerTable");
			String oldFinger = "";
			if (this.next-1 < this.fingerEntry.size()){
				oldFinger = this.fingerEntry.get(this.next-1);
				this.fingerEntry.set(this.next-1, "");
				if (!this.fingerEntry.contains(oldFinger)){
					if(this.fingerTable.containsKey(oldFinger)){
						this.fingerTable.remove(oldFinger);
					}
				}
			}
		}

	}

	/**
	 *
	 * Print all element on finger table Entry
	 *
	 */
	private void printFingerEntry(){
		MultiLog.println(NetPeer.class.toString(), "Element on Finger ENTRY");
		//System.out.println("Element on Finger ENTRY");
		for (int i=0; i< this.fingerEntry.size(); i++){
			MultiLog.println(NetPeer.class.toString(), this.fingerEntry.get(i));
			//System.out.println(this.fingerEntry.get(i));
		}
	}

	/**
	 *
	 * Print all finger table
	 *
	 */
	private void printFingerTable() {

		MultiLog.println(NetPeer.class.toString(), "PRINT all Finger Table");
		//System.out.println("PRINT all Finger Table");
		if (this.fingerTable.size() == 0) {
			MultiLog.println(NetPeer.class.toString(), "Finger table is empty");
			//System.out.println("Finger table is empty");
			return;
		}
		Set<String> key_set = this.fingerTable.keySet();
		Iterator<String> iter = key_set.iterator();

		while (iter.hasNext()) {
			String key = iter.next();
			NetPeerInfo np = this.fingerTable.get(key);
			if (np == null){
				MultiLog.println(NetPeer.class.toString(), "peer on finger table element: " + key + "  is NULL");
				//System.out.println("peer on finger table element: " + key + "  is NULL");
			}
			else{
				MultiLog.println(NetPeer.class.toString(), key + " on " + np.getIpAddress() + ":" + np.getPortNumber());
				//System.out.println(key + " on " + np.getIpAddress() + ":" + np.getPortNumber());
			}
		}

	}

	/**
	 *
	 * Print all information about finger table and finger entry
	 *
	 */
	public void printAllInfo(){
		MultiLog.println(NetPeer.class.toString(), "Successor: " + this.successorId);
		//System.out.println("Successor: " + this.successorId);
		MultiLog.println(NetPeer.class.toString(), "Predecessor: " + this.predecessorId);
		//System.out.println("Predecessor: " + this.predecessorId);

		MultiLog.println(NetPeer.class.toString(), "Print finger entry...");
		//System.out.println("Print finger entry...");
		this.printFingerEntry();
		MultiLog.println(NetPeer.class.toString(), "Ordered keys on finger table...");
		//System.out.println("Ordered keys on finger table...");

		Set<String> key_set = this.fingerTable.keySet();
		Iterator<String> iter = key_set.iterator();
		while (iter.hasNext()){
			String key = iter.next();
			MultiLog.println(NetPeer.class.toString(), key + " on " + this.fingerTable.get(key).getIpAddress() + ":" + this.fingerTable.get(key).getPortNumber());
			//System.out.println(key + " on " + this.fingerTable.get(key).getIpAddress() + ":" + this.fingerTable.get(key).getPortNumber());
		}

	}

	/**
	 *
	 * Checks whether predecessor has failed.
	 * Must be called periodically
	 *
	 */
	public /*synchronized*/ void checkPredecessor(){
		MultiLog.println(NetPeer.class.toString(), "Ping predecessor for verifying if is alive");
		//System.out.println("Ping predecessor for verifying if is alive");

		if (this.predecessor != null){
			if (!this.testIfPeerAlive(this.predecessor.getIpAddress(), this.predecessor.getPortNumber())){

				this.predecessor = null;
				this.predecessorId = null;
				MultiLog.println(NetPeer.class.toString(), "Predecessor peer isn't online");
				//System.out.println("Predecessor peer isn't online");
			}
		}

		MultiLog.println(NetPeer.class.toString(), "Test predecessor completed");
		//System.out.println("Test predecessor completed");
	}


	/**
	 *
	 * This method is used to determine if an identified is on an specified
	 * interval of the Chord-like ring.
	 *
	 * @param reference the identifier by which is required the test
	 * @param a the left extreme of interval
	 * @param b the right extreme of interval
	 * @param aIsIncluded if left extreme is included or not
	 * @param bIsIncluded if rigth extreme is included or not
	 * @return if identifier is on specified interval or not
	 *
	 */
	private boolean isInInterval(String reference, String a, String b, boolean aIsIncluded, boolean bIsIncluded){

		BigInteger referenceNum = SHA1.convertFromStringToBig(reference);
		BigInteger aNum = SHA1.convertFromStringToBig(a);
		BigInteger bNum = SHA1.convertFromStringToBig(b);

		//the interval is the complete ring
		if (aNum.compareTo(bNum) == 0){
			return true;
		}

		if (aNum.compareTo(bNum) < 0 ) {
			//reference is the left extreme of interval which is included
			if (referenceNum.compareTo(aNum) == 0 && aIsIncluded){
				return true;
			}

			//both extremes are included
			if (aIsIncluded && bIsIncluded){
				//reference is in interval
				if (referenceNum.compareTo(aNum) >= 0 && referenceNum.compareTo(bNum) <= 0){
					return true;
				}

			} else if (aIsIncluded && !bIsIncluded){
				if (referenceNum.compareTo(aNum) >= 0 && referenceNum.compareTo(bNum) < 0) {
					return true;
				}

			} else if (!aIsIncluded && bIsIncluded) {
				if (referenceNum.compareTo(aNum) > 0 && referenceNum.compareTo(bNum) <= 0){
					return true;
				}

			} else if (!aIsIncluded && !bIsIncluded) {
				if (referenceNum.compareTo(aNum) > 0 && referenceNum.compareTo(bNum) < 0) {
					return true;
				}
			}

			//for compare with module
		} else {

			if (referenceNum.compareTo(bNum) == 0 && bIsIncluded){
				return true;
			}

			if (aIsIncluded && bIsIncluded){

				if ( ( referenceNum.compareTo(aNum) >= 0 && referenceNum.compareTo(this.dimKeySpace) <= 0 ) ||
						( referenceNum.compareTo(BigInteger.valueOf(0L)) >=0 && referenceNum.compareTo(bNum) <= 0 ) )  {
					return true;
				}

			} else if (aIsIncluded && !bIsIncluded){

				if ( ( referenceNum.compareTo(aNum) >= 0 && referenceNum.compareTo(this.dimKeySpace) <= 0 ) ||
						( referenceNum.compareTo(BigInteger.valueOf(0L)) >=0 && referenceNum.compareTo(bNum) < 0 ) )  {
					return true;
				}

			} else if (!aIsIncluded && bIsIncluded) {

				if ( ( referenceNum.compareTo(aNum) > 0 && referenceNum.compareTo(this.dimKeySpace) <= 0 ) ||
						( referenceNum.compareTo(BigInteger.valueOf(0L)) >=0 && referenceNum.compareTo(bNum) <= 0 ) )  {
					return true;
				}

			} else if (!aIsIncluded && !bIsIncluded) {

				if ( ( referenceNum.compareTo(aNum) > 0 && referenceNum.compareTo(this.dimKeySpace) <= 0 ) ||
						( referenceNum.compareTo(BigInteger.valueOf(0L)) >=0 && referenceNum.compareTo(bNum) < 0 ) )  {
					return true;
				}
			}

		}

		return false;

	}


	/**
	 *
	 * Get the predecessor of this peer
	 *
	 * @return the information about predecessor peer
	 *
	 */
	public NetPeerInfo getPredecessor() {
		if (predecessor == null){
			MultiLog.println(NetPeer.class.toString(), "predecessor is NULL");
			//System.out.println("predecessor is NULL");
		}
		return predecessor;
	}

	/**
	 *
	 * Set new predecessor for this peer
	 *
	 * @param predecessor information about new predecessor
	 *
	 */
	public /*synchronized*/ void setPredecessor(NetPeerInfo predecessor) {
		this.predecessor = predecessor;
	}

	/**
	 *
	 * Get the identifier of predecessor of this peer
	 *
	 * @return the identifier of predecessor peer
	 *
	 */
	public String getPredecessorId() {
		return predecessorId;
	}

	/**
	 *
	 * Set new predecessor identifier for this peer
	 *
	 * @param predecessorId new identifier for predecessor peer
	 *
	 */
	public /*synchronized*/ void setPredecessorId(String predecessorId) {
		this.predecessorId = predecessorId;
	}

	/**
	 *
	 * Get the successor information about successor peer of this peer
	 *
	 * @return the information about successor peer
	 *
	 */
	public NetPeerInfo getSuccessor() {
		return successor;
	}

	/**
	 *
	 * Set new information about successor of this peer
	 *
	 * @param successor new information for successor peer
	 *
	 */
	public /*synchronized*/ void setSuccessor(NetPeerInfo successor) {
		this.successor = successor;
	}

	/**
	 *
	 * Get identifier of successor for this peer
	 *
	 * @return the identifier of successor peer
	 *
	 */
	public String getSuccessorId() {
		return successorId;
	}

	/**
	 *
	 * Set new successor identifier of this peer
	 *
	 * @param successorId new successor identifier
	 *
	 */
	public /*synchronized*/ void setSuccessorId(String successorId) {
		this.successorId = successorId;
	}


	/**
	 *
	 * For test if peer at IP Address 'destAddr' with port 'destPort' is online
	 *
	 * @param destAddr String with destination IP Address
	 * @param destPort TCP destination port number
	 * @return true if peer is online, false if is off-line
	 *
	 */
	public boolean testIfPeerAlive(String destAddr, int destPort){

		if (destAddr.compareTo(this.myPeer.getIpAddress()) == 0 && destPort == this.outputPort)
			return true;

		PingMessage pingMessage = new PingMessage(this.myId, this.myPeer.getIpAddress(), this.outputPort);

		MultiLog.println(NetPeer.class.toString(), "Sending Ping to : " + destAddr + ":" + destPort);
		//System.out.println("Sending Ping to : " + destAddr + ":" + destPort);
		String responseMessage = MessageSender.sendMessage(destAddr, destPort, pingMessage.generateXmlMessageString());

		if(responseMessage.contains("ERROR")) {

			System.err.println("Sending Ping Message ERROR !");

		}
		else {

			MessageReader messageReader = new MessageReader();
			Message receivedMessage = messageReader.readMessageFromString(responseMessage.trim());

			AckMessage ackMessage = new AckMessage(receivedMessage);

			//If ack message status is 0
			if(ackMessage.getAckStatus() == 0) {

				MultiLog.println(NetPeer.class.toString(), "Message sent ... ");
				//System.out.println("Message sent ... ");
				return true;

			}

		}

		return false;
	}


	/**
	 *
	 * Get finger table
	 *
	 * @return the finger table of this peer
	 *
	 */
	public ConcurrentMap<String, NetPeerInfo> getFingerTable() {
		return fingerTable;
	}


	/**
	 *
	 * Get all resources saved on cache
	 *
	 * @return the resources on cache
	 *
	 */
	public ConcurrentMap<String, NetResourceInfo> getResourceOnCache() {
		return resourceOnCache;
	}

	/**
	 *
	 * Set new cache for resources
	 *
	 * @param resourceOnCache new cache of resources
	 *
	 */
	public /*synchronized*/ void setResourceOnCache(ConcurrentMap<String, NetResourceInfo> resourceOnCache) {
		this.resourceOnCache = resourceOnCache;
	}


	/**
	 *
	 * Publish all resource on cache to successor peer of key of resource.
	 * Must be called periodically for verify that resource are on correct peer.
	 * @throws InterruptedException 
	 *
	 */
	public /*synchronized*/ void publishResource(String threadId) throws InterruptedException{

		       //System.out.println("CACHE1");
		       MultiLog.println(NetPeer.class.toString(),"CACHE1");
               //System.out.println("resource on cache: "+this.resourceOnCache.size());
                if (this.resourceOnCache.size() == 0)
			return;

		Set<String> key_set = this.resourceOnCache.keySet();
		Iterator<String> iter = key_set.iterator();
                 //System.out.println("CACHE1");
		 while (iter.hasNext()){
			String key = iter.next();
                         //System.out.println("CACHE : "+key);
			//String responsible = this.findSuccessor(key, this.myThreadId);
			//System.out.println("NetPeer--->PublishResource");
			InfoPassing resp = this.findSuccessor(key, threadId);
			String responsible = resp.getPeerID();
			

			if (responsible.compareTo(this.myId) != 0){

				//String destAddr = this.sharedInfos.getInfoFor(this.myThreadId).getIpAddress();
				//String destAddr = this.sharedInfos.getInfoFor(threadId).getIpAddress();
				String destAddr = resp.getPeerData().getIpAddress();

				//int destPort = this.sharedInfos.getInfoFor(this.myThreadId).getPortNumber();
				//int destPort = this.sharedInfos.getInfoFor(threadId).getPortNumber();
				int destPort = resp.getPeerData().getPortNumber();

				NetResourceInfo resource = this.resourceOnCache.get(key);
				NetPeerInfo owner = resource.getOwner();
				String ownerId = resource.getOwnerId();

				MultiLog.println(NetPeer.class.toString(), "Responsible " + responsible + " to " + destAddr + ":" + destPort);
				//System.out.println("Responsible " + responsible + " to " + destAddr + ":" + destPort);
				MultiLog.println(NetPeer.class.toString(), "Send message for add resource...");
				//System.out.println("Send message for add resource...");


				PublishResourceMessage publishMessage = new PublishResourceMessage(this.myId, owner.getIpAddress(), owner.getPortNumber(), key, ownerId);

				MultiLog.println(NetPeer.class.toString(), "Sending PublishMessage to : " + destAddr + ":" + destPort);
				//System.out.println("Sending PublishMessage to : " + destAddr + ":" + destPort);
				String responseMessage = MessageSender.sendMessage(destAddr, destPort, publishMessage.generateXmlMessageString());

				if(responseMessage.contains("ERROR")) {

					System.err.println("Sending Publish Resource Message ERROR !");
					MultiLog.println(NetPeer.class.toString(), "Resource is already on cache");
					//System.out.println("Resource is already on cache");
				}
				else {

					MessageReader messageReader = new MessageReader();
					Message receivedMessage = messageReader.readMessageFromString(responseMessage.trim());

					AckMessage ackMessage = new AckMessage(receivedMessage);

					//If ack message status is 0
					if(ackMessage.getAckStatus() == 0) {

						MultiLog.println(NetPeer.class.toString(), "Message sent ... ");
						//System.out.println("Message sent ... ");

						this.resourceOnCache.remove(key);
						if (this.resourceOnCache.size() == 0 ){
							return;
						}
						key_set = this.resourceOnCache.keySet();
						iter = key_set.iterator();

					}

				}
			}
			else {
				MultiLog.println(NetPeer.class.toString(), "Responsible is MYSELF");
				//System.out.println("Responsible is MYSELF");
			}

		}


	}

	/**
	 *
	 * Add new resource on cache
	 *
	 * @param resource resource to add on cache
	 *
	 */
	public /*synchronized*/ void addResourceOnCache(NetResourceInfo resource){
		this.resourceOnCache.put(resource.getResourceKey(), resource);
		MultiLog.println(NetPeer.class.toString(), "Resource " + resource.getResourceKey() + " added on cache");
		//System.out.println("Resource " + resource.getResourceKey() + " added on cache");
	}

	/**
	 *
	 * Print all resource on cache and relative owner
	 *
	 */
	public void printResourceCache(){
		MultiLog.println(NetPeer.class.toString(), "CACHE RESOURCE:");
		//System.out.println("CACHE RESOURCE:");
		if (this.resourceOnCache.size() == 0)
			return;

		Set<String> key_set = this.resourceOnCache.keySet();
		Iterator<String> iter = key_set.iterator();
		while(iter.hasNext()){
			String key = iter.next();
			NetResourceInfo res = this.resourceOnCache.get(key);
			MultiLog.println(NetPeer.class.toString(), key + " ( " + res.getResourceKey() + " ) " + "owned by " + res.getOwnerId());
			//System.out.println(key + " ( " + res.getResourceKey() + " ) " + "owned by " + res.getOwnerId());
		}
	}

	/**
	 *
	 * Search for a resource by key. First on cache and later on ring
	 *
	 * @param key identifier of resource on ring
	 * @return the required resource if is received. Null otherwise
	 * @throws InterruptedException 
	 *
	 */
	public NetResourceInfo searchResource(String key, String threadId) throws InterruptedException {


		if (this.resourceOnCache.containsKey(key)){
			MultiLog.println(NetPeer.class.toString(), "RESOURCE " + key + " is on cache.");
			//System.out.println("RESOURCE " + key + " is on cache.");
			NetResourceInfo res = this.resourceOnCache.get(key);
			MultiLog.println(NetPeer.class.toString(), "res: " + res.getResourceKey() + " owned by " + res.getOwnerId());
			//System.out.println("res: " + res.getResourceKey() + " owned by " + res.getOwnerId());
			return res;

		} else {

			//String responsible = this.findSuccessor(key, this.myThreadId);
			//System.out.println("NetPeer---->searchResource");
			
			InfoPassing resp = this.findSuccessor(key, threadId);
			String responsible = resp.getPeerID();
			
			
			
			MultiLog.println(NetPeer.class.toString(), "RESOURCE " + key + " may be on cache of " + responsible);
			//System.out.println("RESOURCE " + key + " may be on cache of " + responsible);
			if (responsible.compareTo(this.myId) == 0){

				MultiLog.println(NetPeer.class.toString(), "Resource maybe on my cache but isn't present. Retry later");
				//System.out.println("Resource maybe on my cache but isn't present. Retry later");
				return null;
			} else {
				MultiLog.println(NetPeer.class.toString(), "Require resource to responsible...");
				//System.out.println("Require resource to responsible...");
				//return this.reqResource(key, responsible);
				return this.reqResource(key, responsible, threadId, resp);
			}
		}

	}

	/**
	 *
	 * Request a resource with key 'id' to responsible 'to'
	 *
	 * @param id the key of resource
	 * @param to the responsible of wanted resource
	 * @return the resource required
	 * @throws InterruptedException 
	 *
	 */
	private NetResourceInfo reqResource(String id, String to, String threadId, InfoPassing resp) throws InterruptedException{

		GetResourceMessage getResourceMessage = new GetResourceMessage(this.myId, this.myPeer.getIpAddress(), this.outputPort, id);

		String destAddr = "";
		int destPort = -1;


		//if (this.sharedInfos.getIdFor(this.myThreadId).compareTo(to) == 0) {
		//if (this.sharedInfos.getIdFor(threadId).compareTo(to) == 0) {
		if (resp.getPeerID().compareTo(to) == 0) {
			//destAddr = this.sharedInfos.getInfoFor(this.myThreadId).getIpAddress();
			//destAddr = this.sharedInfos.getInfoFor(threadId).getIpAddress();
			destAddr = resp.getPeerData().getIpAddress();
			//destPort = this.sharedInfos.getInfoFor(this.myThreadId).getPortNumber();
			//destPort = this.sharedInfos.getInfoFor(threadId).getPortNumber();
			destPort = resp.getPeerData().getPortNumber();
		} else if (this.fingerTable.containsKey(to)) {
			destAddr = this.fingerTable.get(to).getIpAddress();
			destPort = this.fingerTable.get(to).getPortNumber();
		} else if (this.successorId.compareTo(to) == 0) {
			destAddr = this.successor.getIpAddress();
			destPort = this.successor.getPortNumber();
		} else if (this.predecessorId.compareTo(to)== 0) {
			destAddr = this.predecessor.getIpAddress();
			destPort = this.predecessor.getPortNumber();
		}

		//loop avoidance
		if (destAddr.compareTo(this.myPeer.getIpAddress()) == 0 && destPort == this.outputPort){

			if (this.resourceOnCache.containsKey(id))
			return this.resourceOnCache.get(id);

		}

		MultiLog.println(NetPeer.class.toString(), "Sending get resource to : " + destAddr + ":" + destPort);
		//System.out.println("Sending get resource to : " + destAddr + ":" + destPort);
		String responseMessage = MessageSender.sendMessage(destAddr, destPort, getResourceMessage.generateXmlMessageString());

		if(responseMessage.contains("ERROR")) {

			System.err.println("Sending Get Resource Message ERROR !");
			MultiLog.println(NetPeer.class.toString(), "Resource not finded...");
			//System.out.println("Resource not finded...");
			return null;

		}
		else {

			MessageReader messageReader = new MessageReader();
			Message receivedMessage = messageReader.readMessageFromString(responseMessage.trim());

			PublishResourceMessage resourceMessage = new PublishResourceMessage(receivedMessage);


			if (resourceMessage.getKey() == null || resourceMessage.getKey().compareTo("") == 0) {
				MultiLog.println(NetPeer.class.toString(), "Response received is null. Resource not finded");
				//System.out.println("Response received is null. Resource not finded");
				return null;
			}

			MultiLog.println(NetPeer.class.toString(), "Received RESOURCE " + resourceMessage.getKey() + " owned by " + resourceMessage.getOwnerId() + " from " + resourceMessage.getSourceName());
			//System.out.println("Received RESOURCE " + resourceMessage.getKey() + " owned by " + resourceMessage.getOwnerId() + " from " + resourceMessage.getSourceName());

			NetPeerInfo owner = new NetPeerInfo(resourceMessage.getSourceSocketAddr(),  resourceMessage.getSourcePort(), "");
			String ownerId = resourceMessage.getOwnerId();

			NetResourceInfo resource = new NetResourceInfo(resourceMessage.getKey(), owner, ownerId);

			this.resourceOnCache.put(resourceMessage.getKey(), resource);

			return resource;

		}

	}

	/**
	 *
	 * Publish all resource on successor node.
	 * This function will be call when peer disconnect from network
	 *
	 */
	public /*synchronized*/ void publishResourceTo() {

		if (this.resourceOnCache.size() == 0)
			return;

		Set<String> key_set = this.resourceOnCache.keySet();
		Iterator<String> iter = key_set.iterator();

		 while (iter.hasNext()) {
			String key = iter.next();

			String responsible = "";
			String destAddr = "";
			int destPort = -1;

			if (this.successorId != null && this.successorId.compareTo("") != 0){
				responsible = this.successorId;
				destAddr = this.successor.getIpAddress();
				destPort = this.successor.getPortNumber();
			} else {
				boolean finded = false;
				for (int k = this.fingerEntry.size(); k >= 1 && !finded; k--){
					responsible = this.fingerEntry.get(k);
					destAddr = this.fingerTable.get(responsible).getIpAddress();
					destPort = this.fingerTable.get(responsible).getPortNumber();
					finded = true;
				}
			}

			NetResourceInfo resource = this.resourceOnCache.get(key);
			NetPeerInfo owner = resource.getOwner();
			String ownerId = resource.getOwnerId();

			MultiLog.println(NetPeer.class.toString(), "Responsible " + responsible + " to " + destAddr + ":" + destPort);
			//System.out.println("Responsible " + responsible + " to " + destAddr + ":" + destPort);
			MultiLog.println(NetPeer.class.toString(), "Send message for add resource...");
			//System.out.println("Send message for add resource...");


			PublishResourceMessage publishMessage = new PublishResourceMessage(this.myId, owner.getIpAddress(), owner.getPortNumber(), key, ownerId);

			MultiLog.println(NetPeer.class.toString(), "Sending PublishMessage to : " + destAddr + ":" + destPort);
			//System.out.println("Sending PublishMessage to : " + destAddr + ":" + destPort);
			String responseMessage = MessageSender.sendMessage(destAddr, destPort, publishMessage.generateXmlMessageString());

			if(responseMessage.contains("ERROR")) {

				System.err.println("Sending Publish Resource Message ERROR !");
				MultiLog.println(NetPeer.class.toString(), "Resource is already on cache");
				//System.out.println("Resource is already on cache");
			}
			else {

				MessageReader messageReader = new MessageReader();
				Message receivedMessage = messageReader.readMessageFromString(responseMessage.trim());

				AckMessage ackMessage = new AckMessage(receivedMessage);

				//If ack message status is 0
				if(ackMessage.getAckStatus() == 0) {

					MultiLog.println(NetPeer.class.toString(), "Message sent ... ");
					//System.out.println("Message sent ... ");

					this.resourceOnCache.remove(key);
					key_set = this.resourceOnCache.keySet();
					iter = key_set.iterator();

				}

			}

		}

	}

	/**
	 *
	 * Disconnect peer from Chord-like network.
	 * First of all publish all resource on successor node, close ring
	 * send new successor and predecessor information respectively to predecessor
	 * and successor. Later interrupt thread message listener
	 * @throws InterruptedException 
	 *
	 */
	public void disconnectPeer(String threadId) throws InterruptedException{
		MultiLog.println(NetPeer.class.toString(), "Publish all resource to successor...");
		//System.out.println("Publish all resource to successor...");
		//this.publishResource();
		this.publishResource(threadId);
		this.printResourceCache();
		if (this.resourceOnCache.size() > 0) {
			MultiLog.println(NetPeer.class.toString(), "Send all resource on cache to successor");
			//System.out.println("Send all resource on cache to successor");
			this.publishResourceTo();
		}
		this.printResourceCache();

		MultiLog.println(NetPeer.class.toString(), "Close Ring. Sending a setSucc Message");
		//System.out.println("Close Ring. Sending a setSucc Message");
		this.sendNewSucc();
		this.sendNewPred();

		this.messageListener.interrupt();
		if (this.messageListener.isInterrupted()){
			MultiLog.println(NetPeer.class.toString(), "Thread interrupted");
			//System.out.println("Thread interrupted");
		}
		return;
	}

	/**
	 *
	 * Send information about our successor to our predecessor
	 *
	 */
	private void sendNewSucc(){
		MultiLog.println(NetPeer.class.toString(), "Informing " + this.predecessorId + " who new successor is " + this.successorId);
		//System.out.println("Informing " + this.predecessorId + " who new successor is " + this.successorId);

		InfoNewSuccMessage newSuccMessage = new InfoNewSuccMessage(this.myId, this.myPeer.getIpAddress(), this.myPeer.getPortNumber(), this.successorId, this.successor.getIpAddress(), this.successor.getPortNumber());


		String responseMessage = MessageSender.sendMessage(this.predecessor.getIpAddress(), this.predecessor.getPortNumber(), newSuccMessage.generateXmlMessageString());

		if(responseMessage.contains("ERROR")) {

			System.err.println("Sending Info New Successor Message ERROR !");
			MultiLog.println(NetPeer.class.toString(), "Predecessor isn't available");
			//System.out.println("Predecessor isn't available");
		}
		else {

			MessageReader messageReader = new MessageReader();
			Message receivedMessage = messageReader.readMessageFromString(responseMessage.trim());

			AckMessage ackMessage = new AckMessage(receivedMessage);

			//If ack message status is 0
			if(ackMessage.getAckStatus() == 0) {

				MultiLog.println(NetPeer.class.toString(), "Message sent ... ");
				//System.out.println("Message sent ... ");
				MultiLog.println(NetPeer.class.toString(), "Predecessor is informed of new Successor");
				//System.out.println("Predecessor is informed of new Successor");
			}

		}

	}

	/**
	 *
	 * Send information about our predecessor to our successor
	 *
	 */
	private void sendNewPred(){
		MultiLog.println(NetPeer.class.toString(), "Informing " + this.successorId + " who new predecessor is " + this.predecessorId);
		//System.out.println("Informing " + this.successorId + " who new predecessor is " + this.predecessorId);

		InfoNewPredMessage newPredMessage = new InfoNewPredMessage(this.myId, this.myPeer.getIpAddress(), this.myPeer.getPortNumber(), this.predecessorId, this.predecessor.getIpAddress(), this.predecessor.getPortNumber());


		String responseMessage = MessageSender.sendMessage(this.successor.getIpAddress(), this.successor.getPortNumber(), newPredMessage.generateXmlMessageString());

		if(responseMessage.contains("ERROR")) {

			System.err.println("Sending Info New Predecessor Message ERROR !");
			MultiLog.println(NetPeer.class.toString(), "Successor isn't available");
			//System.out.println("Successor isn't available");
		}
		else {

			MessageReader messageReader = new MessageReader();
			Message receivedMessage = messageReader.readMessageFromString(responseMessage.trim());

			AckMessage ackMessage = new AckMessage(receivedMessage);

			//If ack message status is 0
			if(ackMessage.getAckStatus() == 0) {

				MultiLog.println(NetPeer.class.toString(), "Message sent ... ");
				//System.out.println("Message sent ... ");
				MultiLog.println(NetPeer.class.toString(), "Successor is informed of new Predecessor");
				//System.out.println("Successor is informed of new Predecessor");
			}

		}

	}

	/**
	 *
	 * Reconnect peer to network using information on cache if possible
	 * or ask to bootstrap server otherwise
	 *
	 */
	public void reconnectPeer(){
		MultiLog.println(NetPeer.class.toString(), "Reconnecting " + this.myId + "...");
		//System.out.println("Reconnecting " + this.myId + "...");
		if (this.successorId != null || this.successorId.compareTo("") != 0) {
			if (this.testIfPeerAlive(this.successor.getIpAddress(), this.successor.getPortNumber())){


				if (this.messageListener.isInterrupted()){
					MultiLog.println(NetPeer.class.toString(), "Thread was interrupted");
					//System.out.println("Thread was interrupted");
					this.messageListener.interrupt();
				}

				this.join(this.successor, this.successorId);
				this.stabilize();
			}
		}

		else {
			this.tryToGetSucc();
		}

	}

	/**
	 *
	 * Try to obtain a new successor reading from finger table or
	 * ask to bootstrap server
	 *
	 */
	private /*synchronized*/ void tryToGetSucc(){
		MultiLog.println(NetPeer.class.toString(), "Find NEW SUCCESSOR");
		//System.out.println("Find NEW SUCCESSOR");
		boolean finded = false;
		if (this.fingerTable.size() > 0){
			String oldSucc = this.fingerEntry.get(0);

			for (int i = 0; i < this.fingerEntry.size() && !finded; i++){
				MultiLog.println(NetPeer.class.toString(), "Try successor " + this.fingerEntry.get(i));
				//System.out.println("Try successor " + this.fingerEntry.get(i));
				if (this.fingerEntry.get(i).compareTo(oldSucc) != 0){
					this.successorId = this.fingerEntry.get(i);
					this.successor = this.fingerTable.get(this.successorId);

					if (this.messageListener.isInterrupted()){
			//			MultiLog.println(NetPeer.class.toString(), "Thread was interrupted");
						//System.out.println("Thread was interrupted");
				//		MultiLog.println(NetPeer.class.toString(), "Now restarting thread...");
						//System.out.println("Now restarting thread...");
						this.messageListener.interrupt();
					}

					finded = true;
				}
			}
		}
		if (!finded){
			MultiLog.println(NetPeer.class.toString(), "Request to Server ");
			//System.out.println("Request to Server ");
			this.getAccessToNetwork();
		}
		this.stabilize();
	}

	/**
	 *
	 * Get identifier of this peer
	 *
	 * @return identifier of this peer
	 *
	 */
	public String getMyId() {
		return myId;
	}

	/**
	 *
	 * Set a new identifier for this peer
	 *
	 * @param id the new identifier
	 *
	 */
	public /*synchronized*/ void setMyId(String id) {
		this.myId = id;
		this.myThreadId=id;
	}


	/**
	 *
	 * Get information about this peer
	 *
	 * @return information of this peer
	 *
	 */
	public NetPeerInfo getMyPeer() {
		return myPeer;
	}


	/**
	 *
	 * Get the IP address of Bootstrap server
	 *
	 * @return the IP address of server
	 *
	 */
	public String getServerAddress() {
		return serverAddress;
	}

	/**
	 *
	 * Get TCP port number of bootstrap server
	 *
	 * @return the port number of server
	 *
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 *
	 * Get the array list with all entry on Finger Table
	 *
	 * @return the entry on finger
	 *
	 */
	public ArrayList<String> getFingerEntry() {
		return fingerEntry;
	}

	/**
	 *
	 * Delete the specified resource from cache.
	 *
	 * @param id identifier of resource to delete
	 * @return true if resource is correctly deleted, false otherwise
	 *
	 */
	public /*synchronized*/ boolean deleteChordResourceByCache(String id){
		if (this.resourceOnCache.containsKey(id)){
			this.resourceOnCache.remove(id);
			return true;
		}
		else
			return false;

	}


	/**
	 *
	 * Get the shared resource among all thread on peer
	 *
	 * @return the shared resource
	 *
	 */
	//public /*synchronized*/ NetSharedResource getSharedInfos() {
		
		//System.out.println("###########NETPEER GETSHARED INFOS#############");
		//return sharedInfos;
//	}


	/**
	 *
	 * The identifier of thread
	 *
	 * @return the thread identifier
	 */
	public String getMyThreadId() {
		return myThreadId;
	}


}
