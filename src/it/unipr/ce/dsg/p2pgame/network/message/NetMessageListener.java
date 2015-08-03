package it.unipr.ce.dsg.p2pgame.network.message;

import it.simplexml.message.AckMessage;
import it.simplexml.message.Message;
import it.simplexml.message.MessageReader;
import it.unipr.ce.dsg.p2pgame.network.InfoPassing;
import it.unipr.ce.dsg.p2pgame.network.NetPeer;
import it.unipr.ce.dsg.p2pgame.network.NetPeerInfo;
import it.unipr.ce.dsg.p2pgame.network.NetResourceInfo;
import it.unipr.ce.dsg.p2pgame.util.MultiLog;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * Thread that handle incoming message for peer on Chord-like network.
 * The listener handle message of Ping, Find Successor, Get Predecessor,
 * Notify, Publish, Get Resource, Info New Successor and Info New Predecessor request
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class NetMessageListener implements Runnable {

	/**
	 * The TAG that identifier Chord message (only or debug)
	 */
	private final String LOG_TAG = "Chord-like net MESSAGE LISTENER: ";

	/**
	 * The name which identifier the listener
	 */
	private String listenerId = null;

	/**
	 * The IP address of Chord message listener
	 */
	private String listenerAddr = null;

	/**
	 * The TCP port of Chord message listener
	 */
	private int listenerPort;

	/**
	 * Peer who use this listener
	 */
	private NetPeer netPeer;

	private ServerSocket serverSocket = null;

	/**
	 * Identifier of this thread
	 */
	private String threadId = new Long(Thread.currentThread().getId()).toString();


	/**
	 *
	 * Constructor for Peer Message Listener
	 *
	 * @param np the peer whose belong
	 * @param listenerId id of listener
	 * @param listenerAddr IP address by which listen
	 * @param listenerPort port number of TCP connection by which listen
	 *
	 */
	public NetMessageListener(NetPeer np, String listenerId, String listenerAddr, int listenerPort){

		super();
		this.netPeer = np;

		this.listenerId = listenerId;
		this.listenerAddr = listenerAddr;
		this.listenerPort = listenerPort;
		
		this.threadId=np.getMyThreadId();
	}

	/**
	 *
	 * Main loop of listener for reading incoming request
	 *
	 */
	public void run() {

		Socket clientSocket = null;

		MultiLog.println(NetMessageListener.class.toString(), "Creating ServerSocket...");
		//System.out.println("Creating ServerSocket...");

		try {
			if (this.serverSocket == null)
				this.serverSocket = new ServerSocket(this.listenerPort);

		} catch (IOException e) {
			e.printStackTrace();
		}

		while(true){

			MultiLog.println(NetMessageListener.class.toString(), LOG_TAG + "waiting connection...");
			//System.out.println(LOG_TAG + "waiting connection...");

			try {
				clientSocket = serverSocket.accept();
				String message = null;

				DataInputStream is = new DataInputStream(clientSocket.getInputStream());
				DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());

				while(true){

					int current = 0;
					byte[] buf = new byte[100000];

					while (current < 1) {

						int reader = is.read(buf);

						if (reader != -1){
							message = new String(buf);

							current++;
						}
					}

					try {
						checkIncomingMessage(message, os);
					} catch (InterruptedException e) {
						System.err.println("NetMessageListener InterruptException");
						e.printStackTrace();
					}

					is.close();
					os.close();
					clientSocket.close();
					break;

				}

			} catch (IOException e) {
				System.err.println("Connection aborted");
				e.printStackTrace();
			}

		}

	}

	/**
	 *
	 * Control function: reconstruct Message from correct type and
	 * run relative handle function.
	 * DataOutputStream is used for reply.
	 *
	 * @param messageString
	 * @param os stream for reply to receiver
	 * @throws IOException from reading to socket
	 * @throws InterruptedException 
	 *
	 */
	public void checkIncomingMessage(String messageString, DataOutputStream os) throws IOException, InterruptedException {

		MessageReader messageReader = new MessageReader();
		Message receivedMessage = messageReader.readMessageFromString(messageString.trim());

		MultiLog.println(NetMessageListener.class.toString(), LOG_TAG + "Received a Message of type: " + receivedMessage.getMessageType());
		//System.out.println(LOG_TAG + "Received a Message of type: " + receivedMessage.getMessageType());

		//handle received Message - dispatch request from other peer
		if (receivedMessage.getMessageType().equals("PING"))
			this.pingMessageAction(receivedMessage, os);

		if (receivedMessage.getMessageType().equals("FINDSUCC"))
			this.findSuccessorMessageAction(receivedMessage, os);
		if (receivedMessage.getMessageType().equals("GETPRED"))
			this.getPredecessorMessageAction(receivedMessage, os);
		if (receivedMessage.getMessageType().equals("NOTIFY"))
			this.notifyMessageAction(receivedMessage, os);

		if (receivedMessage.getMessageType().equals("PUBLISH"))
			this.publishMessageAction(receivedMessage, os);
		if (receivedMessage.getMessageType().equals("GETRESOURCE"))
			this.getResourceMessageAction(receivedMessage, os);

		if (receivedMessage.getMessageType().equals("INFONEWSUCC"))
			this.setNewSuccessorMessageAction(receivedMessage, os);
		if (receivedMessage.getMessageType().equals("INFONEWPRED"))
			this.setNewPredecessorMessageAction(receivedMessage, os);
	}

	/**
	 *
	 * Handler function for incoming Ping Messages. Respond with an Acknowledge Message.
	 *
	 * @param receivedMessage received message
	 * @param os stream for reply to receiver
	 * @throws IOException form reading to socket
	 *
	 */
	private void pingMessageAction(Message receivedMessage, DataOutputStream os) throws IOException{

		MultiLog.println(NetMessageListener.class.toString(), LOG_TAG + "Handler for PING MESSAGE");
		//System.out.println(LOG_TAG + "Handler for PING MESSAGE");

		MultiLog.println(NetMessageListener.class.toString(), LOG_TAG + "Send Ack");
		//System.out.println(LOG_TAG + "Send Ack");

		os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());

	}

	/**
	 *
	 * Handler function for incoming Find Successor Messages. Respond with an Info Peer Message
	 * containing the highest successor.
	 *
	 * @param receivedMessage received message
	 * @param os stream for reply to receiver
	 * @throws IOException form reading to socket
	 * @throws InterruptedException 
	 *
	 */
	private void findSuccessorMessageAction(Message receivedMessage, DataOutputStream os) throws IOException, InterruptedException {

		MultiLog.println(NetMessageListener.class.toString(), LOG_TAG + "Handler for FINDSUCC MESSAGE");
		//System.out.println(LOG_TAG + "Handler for FINDSUCC MESSAGE");

		FindSuccMessage findMessage = new FindSuccMessage(receivedMessage);

		String id = findMessage.getPeerId();

		MultiLog.println(NetMessageListener.class.toString(), "Received findSuccessor request for " + id);
		//System.out.println("Received findSuccessor request for " + id);

		InfoPassing successor = this.netPeer.findSuccessor(id, this.threadId);
		String succ = successor.getPeerID();
		String destAddr = "";
		int destPort = -1;
		//read where are stored info for 'succ' peer
		if (this.netPeer.getFingerTable() != null && this.netPeer.getFingerTable().containsKey(succ)) {
			destAddr = this.netPeer.getFingerTable().get(succ).getIpAddress();
			destPort = this.netPeer.getFingerTable().get(succ).getPortNumber();
		} else if (this.netPeer.getSuccessorId() != null && this.netPeer.getSuccessorId().compareTo(succ) == 0) {
			destAddr = this.netPeer.getSuccessor().getIpAddress();
			destPort = this.netPeer.getSuccessor().getPortNumber();
		} else if (this.netPeer.getPredecessorId() != null && this.netPeer.getPredecessorId().compareTo(succ)== 0) {
			destAddr = this.netPeer.getPredecessor().getIpAddress();
			destPort = this.netPeer.getPredecessor().getPortNumber();
		//} else if (this.netPeer.getSharedInfos().getIdFor(this.threadId) != null
		} else if (successor != null
				&& successor.getPeerID().compareTo(succ) == 0) {
			destAddr = successor.getPeerData().getIpAddress();
			destPort = successor.getPeerData().getPortNumber();

		} else {
			MultiLog.println(NetMessageListener.class.toString(), "Unable to contact " + succ);
			//System.out.println("Unable to contact " + succ);
			System.out.println("Use info of THIS peer...");
			destAddr = this.netPeer.getMyPeer().getIpAddress();
			destPort = this.netPeer.getMyPeer().getPortNumber();
		}
		//System.out.println("PEER "+this.netPeer.getMyId()+"FINDSUCCESSORMESSAGE");

		os.write((new InfoPeerMessage(this.listenerId, this.listenerAddr, this.listenerPort, succ, destAddr, destPort)).generateXmlMessageString().getBytes());
	}

	/**
	 *
	 * Handler function for incoming Get Predecessor Messages. Respond with an Info Peer Message
	 * containing the predecessor of this peer.
	 *
	 * @param receivedMessage received message
	 * @param os stream for reply to receiver
	 * @throws IOException form reading to socket
	 *
	 */
	private void getPredecessorMessageAction (Message receivedMessage, DataOutputStream os) throws IOException {

		MultiLog.println(NetMessageListener.class.toString(), LOG_TAG + "Handler for GETPRED MESSAGE");
		//System.out.println(LOG_TAG + "Handler for GETPRED MESSAGE");

		String succ = "";
		String addr = "";
		int port = -1;

		if (this.netPeer.getPredecessor() != null) {

			succ = this.netPeer.getPredecessorId();
			addr = this.netPeer.getPredecessor().getIpAddress();
			port = this.netPeer.getPredecessor().getPortNumber();

		}

		os.write((new InfoPeerMessage(this.listenerId, this.listenerAddr, this.listenerPort, succ, addr, port)).generateXmlMessageString().getBytes());
	}

	/**
	 *
	 * Handler function for incoming Notify Messages. Respond with an Acknowledge Message
	 * for inform of correct reception.
	 *
	 * @param receivedMessage received message
	 * @param os stream for reply to receiver
	 * @throws IOException form reading to socket
	 *
	 */
	private void notifyMessageAction (Message receivedMessage, DataOutputStream os) throws IOException {

		MultiLog.println(NetMessageListener.class.toString(), LOG_TAG + "Handler for NOTIFY MESSAGE");
		//System.out.println(LOG_TAG + "Handler for NOTIFY MESSAGE");

		NotifyMessage notifyMessage = new NotifyMessage(receivedMessage);
		NetPeerInfo np = new NetPeerInfo(notifyMessage.getSourceSocketAddr(), notifyMessage.getSourcePort(), "");
		String npId = notifyMessage.getSourceName();
		this.netPeer.notify(npId, np);

		MultiLog.println(NetMessageListener.class.toString(), LOG_TAG + "Send Ack");
		//System.out.println(LOG_TAG + "Send Ack");

		os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());

	}

	/**
	 *
	 * Handler function for incoming Publish Messages. Add received resource
	 * on cache and respond with an Acknowledge Message for inform of correct reception.
	 *
	 * @param receivedMessage received message
	 * @param os stream for reply to receiver
	 * @throws IOException form reading to socket
	 *
	 */
	private void publishMessageAction (Message receivedMessage, DataOutputStream os) throws IOException {

		MultiLog.println(NetMessageListener.class.toString(), LOG_TAG + "Handler for PUBLISH MESSAGE");
		//System.out.println(LOG_TAG + "Handler for PUBLISH MESSAGE");

		PublishResourceMessage publishMessage = new PublishResourceMessage(receivedMessage);

		MultiLog.println(NetMessageListener.class.toString(), "Responsible for RESOURCE " + publishMessage.getKey() + " owned by " + publishMessage.getOwnerId());
		//System.out.println("Responsible for RESOURCE " + publishMessage.getKey() + " owned by " + publishMessage.getOwnerId());
		NetPeerInfo owner = new NetPeerInfo(publishMessage.getSourceSocketAddr(),  publishMessage.getSourcePort(), "");
		String ownerId = publishMessage.getOwnerId();

		NetResourceInfo resource = new NetResourceInfo(publishMessage.getKey(), owner, ownerId);

		this.netPeer.addResourceOnCache(resource);

		MultiLog.println(NetMessageListener.class.toString(), LOG_TAG + "Send Ack");
		//System.out.println(LOG_TAG + "Send Ack");

		os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());
	}

	/**
	 *
	 * Handler function for incoming Get Resource Messages. Respond with a Publish Resource Message
	 * containing the resource if that is on cache (an empty message otherwise).
	 *
	 * @param receivedMessage received message
	 * @param os stream for reply to receiver
	 * @throws IOException form reading to socket
	 *
	 */
	private void getResourceMessageAction (Message receivedMessage, DataOutputStream os) throws IOException {

		MultiLog.println(NetMessageListener.class.toString(), LOG_TAG + "Handler for GETRESOURCE MESSAGE");
		//System.out.println(LOG_TAG + "Handler for GETRESOURCE MESSAGE");

		GetResourceMessage searchMessage = new GetResourceMessage(receivedMessage);

		String resourceId = searchMessage.getResourceId();
		MultiLog.println(NetMessageListener.class.toString(), "Search resource: " + resourceId);
		//System.out.println("Search resource: " + resourceId);
		PublishResourceMessage sendResource;

		if (this.netPeer.getResourceOnCache().containsKey(resourceId)){
			MultiLog.println(NetMessageListener.class.toString(), "Resource is on cache and will be send");
			//System.out.println("Resource is on cache and will be send");
			NetResourceInfo resource = this.netPeer.getResourceOnCache().get(resourceId);
			NetPeerInfo owner = resource.getOwner();
			String ownerId = resource.getOwnerId();
			sendResource = new PublishResourceMessage(this.listenerId, owner.getIpAddress(), owner.getPortNumber(), resourceId, ownerId);

		} else {
			MultiLog.println(NetMessageListener.class.toString(), "Resource not available");
			//System.out.println("Resource not available");
			sendResource = new PublishResourceMessage("", "", -1, "", "");

		}

		os.write(sendResource.generateXmlMessageString().getBytes());
	}

	/**
	 *
	 * Handler function for incoming Set New Successor Messages. Set new received successor and
	 * respond with an Acknowledge Message for inform of correct reception.
	 *
	 * @param receivedMessage received message
	 * @param os stream for reply to receiver
	 * @throws IOException form reading to socket
	 *
	 */
	private void setNewSuccessorMessageAction(Message receivedMessage, DataOutputStream os) throws IOException{

		MultiLog.println(NetMessageListener.class.toString(), LOG_TAG + "Handler for INFONEWSUCC MESSAGE");
		//System.out.println(LOG_TAG + "Handler for INFONEWSUCC MESSAGE");

		InfoNewSuccMessage newSuccMessage = new InfoNewSuccMessage(receivedMessage);

		this.netPeer.setSuccessorId(newSuccMessage.getPeerId());
		NetPeerInfo npi = new NetPeerInfo(newSuccMessage.getPeerAddr(), newSuccMessage.getPeerPort(), "");
		this.netPeer.setSuccessor(npi);
		MultiLog.println(NetMessageListener.class.toString(), "NEW SUCCESSOR setted: " + this.netPeer.getSuccessorId() + " from info by " + newSuccMessage.getSourceName());
		//System.out.println("NEW SUCCESSOR setted: " + this.netPeer.getSuccessorId() + " from info by " + newSuccMessage.getSourceName());

		MultiLog.println(NetMessageListener.class.toString(), LOG_TAG + "Send Ack");
		//System.out.println(LOG_TAG + "Send Ack");

		os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());

	}

	/**
	 *
	 * Handler function for incoming Set New Predecessor Messages. Set new received predecessor and
	 * respond with an Acknowledge Message for inform of correct reception.
	 *
	 * @param receivedMessage received message
	 * @param os stream for reply to receiver
	 * @throws IOException form reading to socket
	 *
	 */
	private void setNewPredecessorMessageAction(Message receivedMessage, DataOutputStream os) throws IOException{

		MultiLog.println(NetMessageListener.class.toString(), LOG_TAG + "Handler for INFONEWPRED MESSAGE");
		//System.out.println(LOG_TAG + "Handler for INFONEWPRED MESSAGE");

		InfoNewPredMessage newPredMessage = new InfoNewPredMessage(receivedMessage);

		this.netPeer.setPredecessorId(newPredMessage.getPeerId());
		NetPeerInfo npi = new NetPeerInfo(newPredMessage.getPeerAddr(), newPredMessage.getPeerPort(), "");
		this.netPeer.setPredecessor(npi);
		MultiLog.println(NetMessageListener.class.toString(), "NEW PREDECESSOR setted: " + this.netPeer.getPredecessorId() + " from info by " + newPredMessage.getSourceName());
		//System.out.println("NEW PREDECESSOR setted: " + this.netPeer.getPredecessorId() + " from info by " + newPredMessage.getSourceName());

		MultiLog.println(NetMessageListener.class.toString(), LOG_TAG + "Send Ack");
		//System.out.println(LOG_TAG + "Send Ack");

		os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());

	}

	/**
	 *
	 * Get the listener id of message to peer
	 *
	 * @return the listener id of peer listener
	 *
	 */
	public String getListenerId() {
		return listenerId;
	}

	/**
	 *
	 * Set a new listener id of message to peer
	 *
	 * @param listenerId new id of peer listener
	 *
	 */
	public void setListenerId(String listenerId) {
		this.listenerId = listenerId;
	}

	/**
	 *
	 * Get the IP address of peer listener
	 *
	 * @return the address of peer listener
	 *
	 */
	public String getListenerAddr() {
		return listenerAddr;
	}

	/**
	 *
	 * Set a new IP address of peer listener
	 *
	 * @param listenerAddr new address of peer listener
	 *
	 */
	public void setListenerAddr(String listenerAddr) {
		this.listenerAddr = listenerAddr;
	}

	/**
	 *
	 * Get the TCP port number of peer listener
	 *
	 * @return the port number of peer listener
	 *
	 */
	public int getListenerPort() {
		return listenerPort;
	}

	/**
	 *
	 * Set a new TCP port number of peer listener
	 *
	 * @param listenerPort new port number of peer listener
	 *
	 */
	public void setListenerPort(int listenerPort) {
		this.listenerPort = listenerPort;
	}

	/**
	 *
	 * Get the peer by which this listener belong
	 *
	 * @return the peer
	 *
	 */
	public NetPeer getNetPeer() {
		return netPeer;
	}

	/**
	 *
	 * Set a new peer by which this listener belong
	 *
	 * @param server new peer
	 *
	 */
	public void setNetPeer(NetPeer netPeer) {
		this.netPeer = netPeer;
	}



}
