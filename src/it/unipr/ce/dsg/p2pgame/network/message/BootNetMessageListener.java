package it.unipr.ce.dsg.p2pgame.network.message;

import it.simplexml.message.AckMessage;
import it.simplexml.message.Message;
import it.simplexml.message.MessageReader;
import it.unipr.ce.dsg.p2pgame.network.BootstrapServer;
import it.unipr.ce.dsg.p2pgame.network.NetPeerInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * Thread that handle incoming message for Bootstrap server.
 * The listener handle message of Ping and Connect request
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class BootNetMessageListener implements Runnable {

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
	 * Bootstrap that use this listener
	 */
	private BootstrapServer server;

	/**
	 *
	 * Constructor for Bootstrap Message Listener
	 *
	 * @param bs the bootstrap server whose belong
	 * @param listenerId id of listener
	 * @param listenerAddr IP address by which listen
	 * @param listenerPort port number of TCP connection by which listen
	 *
	 */
	public BootNetMessageListener(BootstrapServer bs, String listenerId, String listenerAddr, int listenerPort){

		super();

		this.server = bs;

		this.listenerId = listenerId;
		this.listenerAddr = listenerAddr;
		this.listenerPort = listenerPort;
	}

	/**
	 *
	 * Main loop of listener for reading incoming request
	 *
	 */
	public void run() {

		ServerSocket serverSocket = null;
		Socket clientSocket = null;

		System.out.println("Creating ServerSocket...");

		try {
			serverSocket = new ServerSocket(this.listenerPort);
		} catch (IOException e) {
			e.printStackTrace();
		}

		while(true){

			System.out.println(LOG_TAG + "waiting connection...");

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

					checkIncomingMessage(message, os);

					is.close();
					System.out.println("connection closed");
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
	 *
	 */
	public void checkIncomingMessage(String messageString, DataOutputStream os) throws IOException {

		MessageReader messageReader = new MessageReader();
		Message receivedMessage = messageReader.readMessageFromString(messageString.trim());

		System.out.println(LOG_TAG + "Received a Message of type: " + receivedMessage.getMessageType());

		//handle received Message
		if (receivedMessage.getMessageType().equals("PING"))
			this.pingMessageAction(receivedMessage, os);
		if (receivedMessage.getMessageType().equals("CONNECT"))
			this.connectMessageAction(receivedMessage, os);

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

		System.out.println(LOG_TAG + "Handler for PING MESSAGE");

		System.out.println(LOG_TAG + "Send Ack");

		os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());

	}


	/**
	 *
	 * Handler function for incoming 'Connect to Network' Messages. Respond with closet successor
	 * on network
	 *
	 * @param receivedMessage received message
	 * @param os stream for reply to receiver
	 * @throws IOException form reading to socket
	 *
	 */
	private void connectMessageAction(Message receivedMessage, DataOutputStream os) throws IOException {

		System.out.println(LOG_TAG + "Handler for CONNECT MESSAGE");

		System.out.println(LOG_TAG + "Respond with INFOPEER");

		//Receive message information
		System.out.println("Receive message information");
		String id = receivedMessage.getSourceName();
		System.out.println("RESEARCH for id " + id);

		//Find closet successor
		System.out.println("Find closet successor");
		String closetId = this.server.closetSuccessor(id);

		if (closetId == null){
			System.out.println("Create new Net");

			os.write((new InfoPeerMessage(this.listenerId, this.listenerAddr, this.listenerPort, "", "", -1)).generateXmlMessageString().getBytes());

		} else {
			System.out.println("Responde with Successor Peer");
			NetPeerInfo npi = this.server.getLastConnectedUser().get(closetId);


			os.write((new InfoPeerMessage(this.listenerId, this.listenerAddr, this.listenerPort, closetId, npi.getIpAddress(), npi.getPortNumber())).generateXmlMessageString().getBytes());
		}

		//Save new connected peer on cache
		this.server.savePeerOnCache(id, receivedMessage.getSourceSocketAddr(), receivedMessage.getSourcePort());
	}


	/**
	 *
	 * Get the listener id of message to server
	 *
	 * @return the listener id of server listener
	 *
	 */
	public String getListenerId() {
		return listenerId;
	}

	/**
	 *
	 * Set a new listener id of message to server
	 *
	 * @param listenerId new id of server listener
	 *
	 */
	public void setListenerId(String listenerId) {
		this.listenerId = listenerId;
	}

	/**
	 *
	 * Get the IP address of server listener
	 *
	 * @return the address of server listener
	 *
	 */
	public String getListenerAddr() {
		return listenerAddr;
	}

	/**
	 *
	 * Set a new IP address of server listener
	 *
	 * @param listenerAddr new address of server listener
	 *
	 */
	public void setListenerAddr(String listenerAddr) {
		this.listenerAddr = listenerAddr;
	}

	/**
	 *
	 * Get the TCP port number of server listener
	 *
	 * @return the port number of server listener
	 *
	 */
	public int getListenerPort() {
		return listenerPort;
	}

	/**
	 *
	 * Set a new TCP port number of server listener
	 *
	 * @param listenerPort new port number of server listener
	 *
	 */
	public void setListenerPort(int listenerPort) {
		this.listenerPort = listenerPort;
	}

	/**
	 *
	 * Get the server by which this listener belong
	 *
	 * @return the server
	 *
	 */
	public BootstrapServer getServer() {
		return server;
	}

	/**
	 *
	 * Set a new server by which this listener belong
	 *
	 * @param server new server
	 *
	 */
	public void setServer(BootstrapServer server) {
		this.server = server;
	}

}
