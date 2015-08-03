package it.unipr.ce.dsg.p2pgame.network.test.multi;

import it.simplexml.message.AckMessage;
import it.simplexml.message.Message;
import it.simplexml.message.MessageReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * Thread that handle incoming message for Peer on cluster.
 * The listener handle message of Create new Resource, Search for
 * a random resource and Print all Resources on cache
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class ClusterPeerListener implements Runnable {

	/**
	 * The TAG that identifier Chord message (only or debug)
	 */
	private final String LOG_TAG = "Cluster Chord-like net MESSAGE LISTENER: ";

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
	 * The name of thread
	 */
	private String threadId = new Long(Thread.currentThread().getId()).toString();

	/**
	 *
	 * Constructor for Bootstrap Message Listener
	 *
	 * @param listenerId id of listener
	 * @param listenerAddr IP address by which listen
	 * @param listenerPort port number of TCP connection by which listen
	 *
	 */
	public ClusterPeerListener( String listenerId, String listenerAddr, int listenerPort){

		super();

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

					try {
						checkIncomingMessage(message, os);
					} catch (InterruptedException e) {
						System.err.println("ClusterPeerListener InterruptedException");
						e.printStackTrace();
					}

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
	 * @param messageString  message received
	 * @param os stream for reply to receiver
	 * @throws IOException from reading to socket
	 * @throws InterruptedException 
	 *
	 */
	public void checkIncomingMessage(String messageString, DataOutputStream os) throws IOException, InterruptedException {

		MessageReader messageReader = new MessageReader();
		Message receivedMessage = messageReader.readMessageFromString(messageString.trim());

		System.out.println(LOG_TAG + "Received a Message of type: " + receivedMessage.getMessageType());

		//handle received Message
		if (receivedMessage.getMessageType().equals("CREATERESOURCE"))
			this.createResourceMessageAction(receivedMessage, os);
		if (receivedMessage.getMessageType().equals("SEARCH"))
			this.searchMessageAction(receivedMessage, os);
		if (receivedMessage.getMessageType().equals("RESOURCES"))
			this.printResourcesMessageAction(receivedMessage, os);

	}

	/**
	 *
	 * Handler function for incoming 'Create Resource'. Respond with an Acknowledge Message.
	 *
	 * @param receivedMessage received message
	 * @param os stream for reply to receiver
	 * @throws IOException form reading to socket
	 *
	 */
	private void createResourceMessageAction(Message receivedMessage, DataOutputStream os) throws IOException{

		System.out.println(LOG_TAG + "Handler for CREATERESOURCE");

		System.out.println(LOG_TAG + "Send Ack");

		CreateResourceMessage createMessage = new CreateResourceMessage(receivedMessage);

		PeerCluster.createResource(createMessage.getTime());

		os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());

	}


	/**
	 *
	 * Handler function for incoming 'Print Resources on cache' Messages.
	 * Respond with an Acknowledge Message.
	 *
	 * @param receivedMessage received message
	 * @param os stream for reply to receiver
	 * @throws IOException form reading to socket
	 *
	 */
	private void printResourcesMessageAction(Message receivedMessage, DataOutputStream os) throws IOException {

		System.out.println(LOG_TAG + "Handler for RESOURCES MESSAGE");

		System.out.println(LOG_TAG + "Send Ack");

		CacheResourceMessage cacheMessage = new CacheResourceMessage(receivedMessage);

		PeerCluster.printCache(cacheMessage.getTime());

		os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());
	}


	/**
	 *
	 * Handler function for incoming 'Search Resource' Messages.
	 * Respond with an Acknowledge Message.
	 *
	 * @param receivedMessage received message
	 * @param os stream for reply to receiver
	 * @throws IOException form reading to socket
	 * @throws InterruptedException 
	 *
	 */
	private void searchMessageAction(Message receivedMessage, DataOutputStream os) throws IOException, InterruptedException {

		System.out.println(LOG_TAG + "Handler for SEARCH MESSAGE");

		System.out.println(LOG_TAG + "Send Ack");

		SearchResourceMessage searchMessage = new SearchResourceMessage(receivedMessage);

		PeerCluster.search(searchMessage.getTime(), this.threadId);

		os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());
	}
}
