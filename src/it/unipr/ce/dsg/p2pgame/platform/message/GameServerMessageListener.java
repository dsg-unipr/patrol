package it.unipr.ce.dsg.p2pgame.platform.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import it.simplexml.message.AckMessage;
import it.simplexml.message.Message;
import it.simplexml.message.MessageReader;
import it.unipr.ce.dsg.p2pgame.network.NetPeerInfo;
import it.unipr.ce.dsg.p2pgame.platform.AddressInfo;
import it.unipr.ce.dsg.p2pgame.platform.GamePlayer;
import it.unipr.ce.dsg.p2pgame.platform.GameServer;

public class GameServerMessageListener implements Runnable {

	private final String LOG_TAG = "Game Server MESSAGE LISTENER: ";

	private String listenerId = null;
	private String listenerAddr = null;
	private int listenerPort;

	private final long delaySendPosition = 3000;

	private GameServer server;

	public GameServerMessageListener(GameServer gs, String listenerId, String listenerAddr, int listenerPort) {

		super();

		this.server = gs;

		this.listenerId = listenerId;
		this.listenerAddr = listenerAddr;
		this.listenerPort = listenerPort;
	}


	public void run() {
		ServerSocket serverSocket = null;
		Socket clientSocket = null;

		System.out.println("Creating ServerSocket for Game Server...");

		try {
			serverSocket = new ServerSocket(this.listenerPort);
		} catch (IOException e) {

			e.printStackTrace();
		}

		while (true) {

			System.out.println(LOG_TAG + "waiting connection on game listener...");

			try {
				clientSocket = serverSocket.accept();
				String message = null;

				DataInputStream is = new DataInputStream(clientSocket.getInputStream());
				DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());

				while(true) {

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
					System.out.println("connection with game server closed");
					os.close();
					clientSocket.close();
					break;
				}

			} catch (IOException e) {
				System.out.println("Connection aborted");
				e.printStackTrace();
			}

		}

	}

	public void checkIncomingMessage(String messageString, DataOutputStream os) throws IOException {

		MessageReader messageReader = new MessageReader();

		Message receivedMessage = messageReader.readMessageFromString(messageString.trim());

		System.out.println(LOG_TAG + "Received a Game Message of type: " + receivedMessage.getMessageType());

		//handle received Message
		if (receivedMessage.getMessageType().equals("PING"))
			this.pingMessageAction(receivedMessage, os);

		if (receivedMessage.getMessageType().equals("REGISTER"))
			this.registerMessageAction(receivedMessage, os);

		if (receivedMessage.getMessageType().equals("LOGIN"))
			this.loginMessageAction(receivedMessage, os);

		if (receivedMessage.getMessageType().equals("LOGOUT"))
			this.logoutMessageAction(receivedMessage, os);

		if (receivedMessage.getMessageType().equals("CHECKPOSITION"))
			this.checkPositionMessageAction(receivedMessage, os);
		if (receivedMessage.getMessageType().equals("USERSLISTREQUEST"))
			this.usersListRequestAction(receivedMessage, os);
//		if (receivedMessage.getMessageType().equals("LEAVERESP"))
//			this.leaveResponsibilityMessageActino(receivedMessage, os);


//		if (receivedMessage.getMessageType().equals("CHECKMOBILERESOURCE"))
//			this.checkResourceMessageAction(receivedMessage, os);
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

	private void registerMessageAction(Message receivedMessage, DataOutputStream os) throws IOException {

		System.out.println(LOG_TAG + "Handler for REGISTER MESSAGE");

		RegisterPeerMessage registerMessage = new RegisterPeerMessage(receivedMessage);


		System.out.println("Verify that username " + registerMessage.getUserName() + " isn't already in use");

		final String id = this.server.registerNewUser(registerMessage.getUserName(), registerMessage.getPassword());
		
		//aggiunto da jose' murga 09/06/2011
		System.out.println("AddAddressInfo");
		this.server.addAddressInfo(registerMessage.getUserName(),registerMessage.getSourceSocketAddr(),registerMessage.getSourcePort());

		UserPeerMessage userPeer;
		GamePlayer player = null;

		if (id == null) {

			userPeer = new UserPeerMessage("", "", -1, "", 0,0,0, -1,-1/*,-1*/);
		}
		else {
			//preleva anche le informazioni sulla posizione se presenti per inviarle
			player = this.server.getResPlayer(registerMessage.getUserName());

			userPeer = new UserPeerMessage(id, registerMessage.getSourceSocketAddr(), registerMessage.getSourcePort(), registerMessage.getUserName(),
					player.getPosX(), player.getPosY(), player.getPosZ(), player.getVelocity(), player.getVisibility()/*, player.getGranularityVision()*/);

		}

		System.out.println("Sending an id: " + id + ".");

		os.write(userPeer.generateXmlMessageString().getBytes());

		final GamePlayer pubPlayer = player;

		if (pubPlayer != null) {

//			if (this.server.getLastConnectedUser().size() == 0){
				new Thread( new Runnable() {
					public void run() {
						try {
							Thread.sleep(delaySendPosition); //attesa per permettere che il primo nodo entri in Chord

							System.out.println("Sending first position on net for: " + id);

							String succ = server.closetSuccessor(pubPlayer.getSpatialPosition());
							NetPeerInfo peer = server.getLastConnectedUser().get(succ);

							server.publishPosition(succ, peer, pubPlayer);
							System.out.println("First position sent correctly");

//							//TODO: spostare se e quante risorse mobili dare da un'altra parte
//							System.out.println("Add mobile resource...");
//							//String id, String description, double quantity, double x, double y, double z, double vel, double vis, double relMovX, double relMovY, double relMovZ
//							GameResourceMobile res = new GameResourceMobile("mobile", "",1, pubPlayer.getPosX(), pubPlayer.getPosY(), pubPlayer.getPosZ(), 1,1, 0,0,0);
//							server.addMobileResource(res);
//							server.mobileInitialResource(peer, res);
//							System.out.println("First mobile resource position sent correctly");

						} catch (InterruptedException e) {

							e.printStackTrace();
						}
					}
				}
				).start();
//			}
//			else {
//				System.out.println("Sending first position on net for: " + id);
//
//				String succ = this.server.closetSuccessor(player.getSpatialPosition());
//				System.out.println("successor for position message " + succ);
//				NetPeerInfo peer = this.server.getLastConnectedUser().get(succ);
//				System.out.println("Start publishing position to " + succ);
//				this.server.publishPosition(succ, peer, player);
//				System.out.println("First position sent correctly");
//			}
		}
	}


	private void loginMessageAction(Message receivedMessage, DataOutputStream os) throws IOException {

		System.out.println(LOG_TAG + "Handler for LOGIN MESSAGE");

		LoginPeerMessage loginMessage = new LoginPeerMessage(receivedMessage);

		System.out.println("Verify that username " + loginMessage.getUserName() + " isn't already in use");
		
		String id = this.server.loginUser(loginMessage.getUserName(), loginMessage.getPassword(), false);
		
		

		UserPeerMessage userPeer;

		if (id == null) {

			userPeer = new UserPeerMessage("", "", -1, "", 0,0,0, -1,-1/*,-1*/);

		}
		else {

			userPeer = new UserPeerMessage(id, loginMessage.getSourceSocketAddr(), loginMessage.getSourcePort(), loginMessage.getUserName(), 0,0,0, -1,-1/*,-1*/);

		}

		System.out.println("Sending an id: " + id + ".");

		os.write(userPeer.generateXmlMessageString().getBytes());
	}

	private void logoutMessageAction(Message receivedMessage, DataOutputStream os) throws IOException {

		System.out.println(LOG_TAG + "Handler for LOGOUT MESSAGE");

		LogoutPeerMessage logoutMessage = new LogoutPeerMessage(receivedMessage);

		System.out.println("Try to logout user " + logoutMessage.getUserId());

		this.server.logoutUser(logoutMessage.getUserId());

		System.out.println(LOG_TAG + "Send Ack");

		os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());
	}


	private void checkPositionMessageAction(Message receivedMessage, DataOutputStream os) throws IOException {

		System.out.println(LOG_TAG + "Handler for CHECK POSITION MESSAGE");

		CheckPositionPlayerMessage posPlayer = new CheckPositionPlayerMessage(receivedMessage);

		if (this.server.getResPlayer(posPlayer.getUserName()) != null){

			System.out.println("There are postion information on cache for this peer");

			GamePlayer savedPlayer = this.server.getResPlayer(posPlayer.getUserName());

			if (savedPlayer.getSpatialPosition().compareTo(posPlayer.getOldPos()) == 0){

				System.out.println(LOG_TAG + "Send Ack");

				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());
			}
			else {
				System.out.println(LOG_TAG + "Send NAck. Player not present or position wrong");

				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
			}

		}

	}
	
	private void usersListRequestAction( Message receivedMessage, DataOutputStream os) throws IOException {
		
		UsersListRequestMessage usersListRequest=new UsersListRequestMessage(receivedMessage); 
		
		HashMap<String, AddressInfo> addressInfo=this.server.getAddressInfo();
		
		//creo la stringa
		Iterator<String> iterator=addressInfo.keySet().iterator(); 
		int s=addressInfo.size();
		int i=0;
		String list="";
		while(iterator.hasNext())
		{
			AddressInfo info=addressInfo.get(iterator.next());
				
			String id=info.getId();
			String ipadd=info.getIpAddress();
			int port=info.getPort();
			
			//devo costruire la stringa
			list+=id+","+ipadd+","+port;
			if(i<(s-1))
			{
				list+="$";
				
			}
			i++;
			
		}
		System.out.println("NUMER OF USERS: "+addressInfo.size());
		System.out.println("USERS: "+list);
		//creo il messaggio con la lista degli indirizzi
		UsersListMessage userlistmsg=new UsersListMessage(this.listenerId, this.listenerAddr, this.listenerPort,list);
		//invio la risposta
		os.write(userlistmsg.generateXmlMessageString().getBytes());
		
	}


//	private void checkResourceMessageAction(Message receivedMessage, DataOutputStream os) throws IOException {
//
//		System.out.println(LOG_TAG + "Handler for CHECK MOBILE RESOURCE MESSAGE");
//
//		CheckMobileResourceMessage posResource = new CheckMobileResourceMessage(receivedMessage);
//
//		if (this.server.getMobileResource(posResource.getOldPos()) != null){
//
//			System.out.println("There are postion information on cache for this resource");
//
//			GameResourceMobile savedResource = this.server.getMobileResource(posResource.getOldPos());
//
//			if (savedResource.getSpatialPosition().compareTo(posResource.getOldPos()) == 0){
//
//				System.out.println(LOG_TAG + "Send Ack");
//
//				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());
//			}
//			else {
//				System.out.println(LOG_TAG + "Send NAck. Mobile Resource not present or position wrong");
//
//				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
//			}
//
//		}
//
//	}

}
