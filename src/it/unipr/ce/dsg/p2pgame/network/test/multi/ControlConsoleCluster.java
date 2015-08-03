package it.unipr.ce.dsg.p2pgame.network.test.multi;

import it.simplexml.message.AckMessage;
import it.simplexml.message.Message;
import it.simplexml.message.MessageReader;
import it.simplexml.sender.MessageSender;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

/**
 * The control console for testing Chord network on a cluster.
 * Permit to lunch request of resource creation, research and print cache.
 * This request will be done semi-periodically: with base periods (periodX) and a random degree of variability
 *
 *
 * @param args file for know the peer on network for send request: fileConfig (required);
 * 		port used for send request: localPort, configuration of time requests variability periodCreate periodSearch periodCache
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 */
public class ControlConsoleCluster {


	public static void main(String[] args) {

		Random rand = new Random();

		int periodCreate = 60000;
		int periodSearch = 70000;
		int periodCache = 80000;

		int localPort = 8080;

		int variability = 30000;

		String fileConfig = "config.txt";



		if (args.length < 1 ) {
			System.out.println("USE: fileConfig (localPort variability periodCreate periodSearch periodCache)");
			System.exit(1);
		}
		else {

			fileConfig = args[0].trim();

			if (args.length == 6) {
				localPort = Integer.parseInt(args[1].trim());
				variability = Integer.parseInt(args[2].trim());
				periodCreate = Integer.parseInt(args[3].trim());
				periodSearch = Integer.parseInt(args[4].trim());
				periodCache = Integer.parseInt(args[5].trim());

			}

		}

		try {
			FileReader reader = new FileReader(fileConfig);
			BufferedReader input = new BufferedReader(reader);
			in = input;
			//in = inputStream;
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		loadConfigFile();

		final int sourcePort = localPort;

		final int create = periodCreate + rand.nextInt(variability);

		//TODO: aggiungere un random con la size per decidere chi deve fare la create e chi loggare
		new Thread( new Runnable() {
			public void run() {
				try {
					while(true) {
						Thread.sleep(create);

						String time = new Long( System.currentTimeMillis()).toString();
						System.out.println("Sending CREATE RESOURCE...");
						try {
							CreateResourceMessage createMessage = new CreateResourceMessage(InetAddress.getLocalHost().toString(), InetAddress.getLocalHost().toString(), sourcePort, time);
							int i = selectRandom();
							String responseMessage = MessageSender.sendMessage(ipAddress.get(i), portNumber.get(i), createMessage.generateXmlMessageString());
							if (responseMessage.contains("ERROR")){
								System.err.println("Sending create RESOURCE ERROR!");

							}
							else {
								MessageReader messageReader = new MessageReader();
								Message received = messageReader.readMessageFromString(responseMessage.trim());
								AckMessage ackMessage = new AckMessage(received);
								if (ackMessage.getAckStatus() == 0){
									System.out.println("Create RESOURCE OK");

								}
								else {
									System.out.println("Create resource error");
								}
							}


							for (int j = 0; j < ipAddress.size(); j++){
								if (j != i){
									CacheResourceMessage cacheMessage = new CacheResourceMessage(InetAddress.getLocalHost().toString(), InetAddress.getLocalHost().toString(), sourcePort, time);
									String responseMessage2 = MessageSender.sendMessage(ipAddress.get(j), portNumber.get(j), cacheMessage.generateXmlMessageString());
									if (responseMessage.contains("ERROR")){
										System.err.println("Sending create RESOURCE ERROR!");

									}
									else {
										MessageReader messageReader2 = new MessageReader();
										Message received2 = messageReader2.readMessageFromString(responseMessage2.trim());
										AckMessage ackMessage2 = new AckMessage(received2);
										if (ackMessage2.getAckStatus() == 0){
											System.out.println("Create RESOURCE OK");

										}
										else {
											System.out.println("Create resource error");
										}
									}


								}

							}

						System.out.println("Create Resource Terminated");
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						//TODO: per gli altri inviare print cache

					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
		).start();



		final int search = periodSearch + rand.nextInt(variability);

		//TODO: aggiungere un random con la size per decidere chi deve fare la search e chi loggare
		new Thread( new Runnable() {
			public void run() {
				try {
					while(true) {
						Thread.sleep(search);

						String time = new Long( System.currentTimeMillis()).toString();
						System.out.println("Sending SEARCH RESOURCE...");
						try {
							SearchResourceMessage searchMessage = new SearchResourceMessage(InetAddress.getLocalHost().toString(), InetAddress.getLocalHost().toString(), sourcePort, time);

							int i = selectRandom();
							MessageSender.sendMessage(ipAddress.get(i), portNumber.get(i), searchMessage.generateXmlMessageString());

							for (int j = 0; j < ipAddress.size(); j++){
								if (j != i){
									CacheResourceMessage cacheMessage = new CacheResourceMessage(InetAddress.getLocalHost().toString(), InetAddress.getLocalHost().toString(), sourcePort, time);
									MessageSender.sendMessage(ipAddress.get(j), portNumber.get(j), cacheMessage.generateXmlMessageString());
								}

							}

							System.out.println("Search Resource terminated");
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						//TODO: per gli altri inviare print cache

					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
		).start();



		final int print = periodCache + rand.nextInt(variability);

		//TODO: aggiungere un random con la size per decidere chi deve fare la search e chi loggare
		new Thread( new Runnable() {
			public void run() {
				try {
					while(true) {
						Thread.sleep(print);

						String time = new Long( System.currentTimeMillis()).toString();
						System.out.println("Sending PRINT RESOURCES ON CACHE...");
						try {
							//inviare a tutti
							for (int j = 0; j < ipAddress.size(); j++){
								CacheResourceMessage cacheMessage = new CacheResourceMessage(InetAddress.getLocalHost().toString(), InetAddress.getLocalHost().toString(), sourcePort, time);
								MessageSender.sendMessage(ipAddress.get(j), portNumber.get(j), cacheMessage.generateXmlMessageString());
							}

							System.out.println("Print resources terminated");
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
		).start();
	}

	/**
	 * Input buffer for configuration file
	 */
	private static BufferedReader in;

	/**
	 * Information about peers IP address
	 */
	private static ArrayList<String> ipAddress = new ArrayList<String>();

	/**
	 * Information about TCP peers port number
	 */
	private static ArrayList<Integer> portNumber = new ArrayList<Integer>();


	/**
	 * Function for read information on configuration file
	 */
	public static void loadConfigFile(){
		try {

			String line;
			while ( (line = in.readLine()) != null )
			{
				String[] info = line.split(":");
				ipAddress.add(info[0].trim());

				portNumber.add(Integer.parseInt(info[1].trim()));

				System.out.println("Address " + info[0].trim() + " port " + info[1].trim());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Function for select a random peer which send message
	 *
	 * @return a number corresponding to one of know peer
	 */
	public static int selectRandom(){
		Random rand = new Random();
		return rand.nextInt(ipAddress.size());

	}

}
