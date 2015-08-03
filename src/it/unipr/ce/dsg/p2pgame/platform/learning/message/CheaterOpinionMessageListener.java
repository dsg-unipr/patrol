package it.unipr.ce.dsg.p2pgame.platform.learning.message;

import it.simplexml.message.AckMessage;
import it.simplexml.message.Message;
import it.simplexml.message.MessageReader;
import it.unipr.ce.dsg.p2pgame.network.BootstrapServer;
import it.unipr.ce.dsg.p2pgame.network.message.InfoPeerMessage;
import it.unipr.ce.dsg.p2pgame.platform.learning.BehaviourAnalysis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CheaterOpinionMessageListener implements Runnable {


	private final String LOG_TAG = "Anti-Cheater MESSAGE LISTENER: ";
	private String listenerId = null;
	private String listenerAddr = null;
	private int listenerPort;

	private BehaviourAnalysis analysis;

	public CheaterOpinionMessageListener(BehaviourAnalysis analysis, String listenerId, String listenerAddr, int listenerPort) {

		super();

		this.analysis = analysis;

		this.listenerId = listenerId;
		this.listenerAddr = listenerAddr;
		this.listenerPort = listenerPort;
	}


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


	public void checkIncomingMessage(String messageString, DataOutputStream os) throws IOException {

		MessageReader messageReader = new MessageReader();
		Message receivedMessage = messageReader.readMessageFromString(messageString.trim());

		System.out.println(LOG_TAG + "Received a Message of type: " + receivedMessage.getMessageType());

		//handle received Message
		if (receivedMessage.getMessageType().equals("PING"))
			this.pingMessageAction(receivedMessage, os);
		if (receivedMessage.getMessageType().equals("CONNECT"))
			this.opinionMessageAction(receivedMessage, os);

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

	private void opinionMessageAction(Message receivedMessage, DataOutputStream os) throws IOException{

		System.out.println(LOG_TAG + "Handler for CHEATER OPINION MESSAGE");

		CheaterAnalysisMessage analysisMessage = new CheaterAnalysisMessage(receivedMessage);

		double resp = this.analysis.getCheaterProb(analysisMessage.getIdAnalyzed());

		os.write((new CheaterOpinionMessage(this.listenerId, this.listenerAddr, this.listenerPort, resp)).generateXmlMessageString().getBytes());
	}


}
