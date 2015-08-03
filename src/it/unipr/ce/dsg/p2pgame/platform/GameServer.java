package it.unipr.ce.dsg.p2pgame.platform;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import it.simplexml.message.AckMessage;
import it.simplexml.message.Message;
import it.simplexml.message.MessageReader;
import it.simplexml.sender.MessageSender;
import it.unipr.ce.dsg.p2pgame.network.BootstrapServer;
import it.unipr.ce.dsg.p2pgame.network.NetPeerInfo;
import it.unipr.ce.dsg.p2pgame.platform.message.GameServerMessageListener;
import it.unipr.ce.dsg.p2pgame.platform.message.PositionPlayerMessage;
import it.unipr.ce.dsg.p2pgame.util.SHA1;

public class GameServer extends BootstrapServer {

	/**
	 *
	 * Record the info for all registered user (will be saved on a file)
	 *
	 */
	private HashMap<String, RegisteredUser> registeredUser = new HashMap<String, RegisteredUser>();

	//TODO: per evitare di riconnettersi... key IDUsername, value IDSessione
	private HashMap<String, String> loggedInUser = new HashMap<String, String>();

	private HashMap<String, GamePlayer> respPlayer = new HashMap<String, GamePlayer>();
	//private HashMap<String, GameResourceMobile> mobileResource = new HashMap<String, GameResourceMobile>();

	private int gameOutPort;
	private int gameInPort;


	private double defaultVision;
	private double defalutVelocity;
	//private double defaultGranularity;


	private GameWorld world;
	
	
	//aggiunto da Jose' Murga 09/06/2011
	private HashMap<String, AddressInfo> addressinfo=new HashMap<String, AddressInfo>();
	

	public GameServer(int outputPort, int inputPort, int sizeOfPeerCache,
			int idBitLength, int gameOutPort, int gameInPort, double minX, double minY, double minZ,
			double maxX, double maxY, double maxZ, double vis, double vel, double gran) throws IOException {
		super(outputPort, inputPort, sizeOfPeerCache, idBitLength);
		// TODO Auto-generated constructor stub


		this.gameInPort = gameInPort;
		this.gameOutPort = gameOutPort;

		this.world = new GameWorld(minX, maxX, minY, maxY, minZ, maxZ, gran);


		this.defalutVelocity = vel;
		this.defaultVision = vis;
		//this.defaultGranularity = gran;

		System.out.println("Game server lunch thread listener ...");
		Thread messageListener = new Thread(new GameServerMessageListener(this, "Game server", "127.0.0.1", this.gameInPort), "GameServer Listener Thread");
		messageListener.start();


	}

	public String registerNewUser(String user, String pwd ) {

		try {
			String id = SHA1.convertToHex(SHA1.calculateSHA1(user));

			if (this.registeredUser.containsKey(id)){

				System.out.println("This user already exist. Select another username");

				return null;
			}
			else {

				RegisteredUser registeredUser = new RegisteredUser(user, pwd);

				this.registeredUser.put(registeredUser.getId(), registeredUser);

				System.out.println("Total number of registered user: " + this.registeredUser.size());

				//return registeredUser.getId();
				return this.loginUser(user, pwd, true);

			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	//viene creata una nuova posizione casuale per il giocatore soltanto se ï¿½ la prima connessione
	public String loginUser (String user, String pwd, boolean newUser){

		try {

			String id = SHA1.convertToHex(SHA1.calculateSHA1(user));

			if (this.registeredUser.containsKey(id)){

				//Verify that user isn't already online
				if (!this.loggedInUser.containsKey(id)){

					System.out.println("Login for " + user + " completed with successfull");


					//TODO: nel secondo campo un id temporale per vedere che non sia online
					//cosï¿½ da poter restituire un'altro id (es con validitï¿½ temporale)
					this.loggedInUser.put(id, id);

					//creazione e pubblicazione di una nuova posizione
					if (newUser){
						System.out.println("Creating new player Position");
						this.createNewRandomPlayer(user, id);

					}
					return id;

				}
				else {
					System.out.println("User already logged");
					return null;
				}
			}
			else {

				System.out.println("Access not allowed");
				return null;
			}

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}


	public void logoutUser(String sessionId) {

		if(this.loggedInUser.containsValue(sessionId)){
			System.out.println("User was logged");

			Set<String> key_set = this.loggedInUser.keySet();
			Iterator<String> iter = key_set.iterator();
			String key = "";
			while (iter.hasNext()){

				key = iter.next();

				String sessionUser = this.loggedInUser.get(key);
				if (sessionUser.compareTo(sessionId) == 0) {

					System.out.println("User " + this.registeredUser.get(key) + " is logged out");

					this.loggedInUser.remove(key);
					return;

				}
			}

		}

	}

	public GameWorld getWorld() {
		return world;
	}

	public HashMap<String, RegisteredUser> getRegisteredUser() {
		return registeredUser;
	}

	public HashMap<String, String> getLoggedInUser() {
		return loggedInUser;
	}

	private void createNewRandomPlayer(String user, String id){

		GamePlayer player = new GamePlayer(id, user, this.world.getRandomX(), this.world.getRandomY(), this.world.getRandomZ(), this.defalutVelocity, this.defaultVision );
		if (this.respPlayer.containsKey(user)){
			this.respPlayer.remove(user);
		}

		this.respPlayer.put(user, player);


//		//TODO: e pubblica sulla rete
//		String succ = this.closetSuccessor(player.getSpatialPosition());
//		NetPeerInfo peer = this.getLastConnectedUser().get(succ);
//
//		this.publishPosition(succ, peer, player);

	}


//	public void mobileInitialResource(NetPeerInfo peer, GameResourceMobile res) {
//		String destAddr = peer.getIpAddress();
//		int destPort = peer.getPortNumber() + 2;
//
//		System.out.println("Sending info specific of Resource Mobile");
//
//		MobileResourceMessage resourceMessage = new MobileResourceMessage("", "", -1, res.getId(), res.getDescription(),
//				res.getSpatialPosition(), res.getX(), res.getY(), res.getZ(),
//				res.getVelocity(), res.getVision(), "");
//
//		System.out.println("Sending resource publish to " + destAddr + ":" + destPort);
//
//		String responseMessage = MessageSender.sendMessage(destAddr, destPort, resourceMessage.generateXmlMessageString());
//
//		if (responseMessage.contains("ERROR")) {
//
//			System.err.println("Sending Resource Mobile Error");
//
//		}
//		else {
//
//			MessageReader messageReader = new MessageReader();
//			Message receivedMessage = messageReader.readMessageFromString(responseMessage.trim());
//
//			AckMessage ackMessage = new AckMessage(receivedMessage);
//
//			if (ackMessage.getAckStatus() == 0) {
//				System.out.println("Message sent...");
//
//			}
//			else {
//				System.out.println("Error on publish mobile resource");
//			}
//		}
//
//	}

	public void publishPosition(String successor, NetPeerInfo peer, GamePlayer player){

		String destAddr = peer.getIpAddress();
		int destPort = peer.getPortNumber() + 2;

		System.out.println("Sending info specific of position");

		PositionPlayerMessage positionMessage = new PositionPlayerMessage("", "", -1, player.getId(), player.getName(),
				player.getSpatialPosition(), player.getPosX(), player.getPosY(), player.getPosZ(),
				player.getVelocity(), player.getVisibility(), /*player.getGranularityVision(),*/ "");

		System.out.println("Sending position publish to " + destAddr + ":" + destPort);

		String responseMessage = MessageSender.sendMessage(destAddr, destPort, positionMessage.generateXmlMessageString());

		if (responseMessage.contains("ERROR")) {

			System.err.println("Sending Publish Resource Error");

		}
		else {

			MessageReader messageReader = new MessageReader();
			Message receivedMessage = messageReader.readMessageFromString(responseMessage.trim());

			AckMessage ackMessage = new AckMessage(receivedMessage);

			if (ackMessage.getAckStatus() == 0) {
				System.out.println("Message sent...");


			}
			else {
				System.out.println("Error on publish player position");
			}
		}


	}


	public GamePlayer getResPlayer(String user){

		if (this.respPlayer.containsKey(user)){
			return this.respPlayer.get(user);
		}
		else
			return null;

	}
	
	public HashMap<String, GamePlayer> getResPlayers()
	{
		return this.respPlayer;
	}


//	public void addMobileResource(GameResourceMobile res) {
//		this.mobileResource.put(res.getSpatialPosition(), res);
//
//	}
//
//	public GameResourceMobile getMobileResource(String pos) {
//		if (this.mobileResource.containsKey(pos)){
//			return this.mobileResource.get(pos);
//		}
//		else
//			return null;
//	}



	//TODO: fare un messaggio sul peer che quando vede che la posizione non ï¿½ oldPos piï¿½ vuota lo dice al server
	private void leaveResp(String user){
		System.out.println("Release responsibility for position of player " + user);
		if (this.respPlayer.containsKey(user)){
			this.respPlayer.remove(user);
		}
	}
	
	
	//aggiunto da jose' murga 09/06/2011
	
	public void addAddressInfo(String user,String ipAdd,int port)
	{
		try {
			String id =SHA1.convertToHex(SHA1.calculateSHA1(user));
			
			if(!this.addressinfo.containsKey(id))
			{
				AddressInfo addInfo=new AddressInfo(id,ipAdd,port);
				
				this.addressinfo.put(id, addInfo);
				System.out.println("USER "+id);//+" "+user);
				
			}
			else
			{
				System.out.println("Already exist");
				
			}
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public HashMap<String, AddressInfo> getAddressInfo()
	{
		return this.addressinfo;
		
	}

//	//TODO: analogamente a quanto da fare per i giocatori
//	private void leaveResourceResp(String oldPos){
//		System.out.println("Release Resource Mobile ");
//		if (this.mobileResource.containsKey(oldPos)){
//			this.mobileResource.remove(oldPos);
//		}
//	}
}
