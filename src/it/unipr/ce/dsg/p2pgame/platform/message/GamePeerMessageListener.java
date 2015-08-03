package it.unipr.ce.dsg.p2pgame.platform.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

import it.simplexml.message.AckMessage;
import it.simplexml.message.Message;
import it.simplexml.message.MessageReader;
import it.simplexml.sender.MessageSender;
import it.unipr.ce.dsg.p2pgame.network.InfoPassing;
import it.unipr.ce.dsg.p2pgame.platform.Attack;
import it.unipr.ce.dsg.p2pgame.platform.Clash.Result;
import it.unipr.ce.dsg.p2pgame.platform.Defense;
import it.unipr.ce.dsg.p2pgame.platform.GamePeer;
import it.unipr.ce.dsg.p2pgame.platform.GamePlayerResponsible;
import it.unipr.ce.dsg.p2pgame.platform.GameResource;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceEvolve;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobile;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobileResponsible;
import it.unipr.ce.dsg.p2pgame.util.MultiLog;

public class GamePeerMessageListener implements Runnable {

	private final String LOG_TAG = "Game Peer MESSAGE LISTENER: ";

	private String listenerId = null;
	private String listenerAddr = null;
	private int listenerPort;

	private GamePeer peer;

	private String threadId = new Long(Thread.currentThread().getId()).toString();

	public GamePeerMessageListener(String listenerId, String listenerAddr,
			int listenerPort, GamePeer peer) {
		super();
		this.listenerId = listenerId;
		this.listenerAddr = listenerAddr;
		this.listenerPort = listenerPort;
		this.peer = peer;
		
		this.threadId=peer.getMyThreadId();
		
		System.out.println("GamePeerMessageListener port="+this.listenerPort);
	}



	public void run() {

		ServerSocket serverSocket = null;
		Socket clientSocket = null;

		//MultiLog.println(GamePeerMessageListener.class.toString(), "Creating PeerSocket for Game Peer...");
		//System.out.println("Creating PeerSocket for Game Peer...");

		try {
			serverSocket = new ServerSocket(this.listenerPort);
		} catch (IOException e) {

		}

		while (true) {

			//MultiLog.println(GamePeerMessageListener.class.toString(), LOG_TAG + "waiting connection to game peer listener...");
			//System.out.println(LOG_TAG + "waiting connection to game peer listener...");


			try{

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

					try {
						checkIncomingMessage(message, os);
					} catch (InterruptedException e) {
						System.err.println("GamePeerMessageListener InterruptedException");
						e.printStackTrace();
					}

					is.close();
					//MultiLog.println(GamePeerMessageListener.class.toString(), "connection with game peer closed");
					//System.out.println("connection with game peer closed");
					os.close();
					clientSocket.close();
					break;
				}


			} catch (IOException e) {
				//MultiLog.println(GamePeerMessageListener.class.toString(),"Connection aborted");
				//System.out.println("Connection aborted");
				e.printStackTrace();
			}

		}


	}


	public void checkIncomingMessage(String messageString, DataOutputStream os) throws IOException, InterruptedException {

		MessageReader messageReader = new MessageReader();

		Message receivedMessage = messageReader.readMessageFromString(messageString.trim());

		//MultiLog.println(GamePeerMessageListener.class.toString(), LOG_TAG + "Received a Game Message of type: " + receivedMessage.getMessageType());
		//System.out.println(LOG_TAG + "Received a Game Message of type: " + receivedMessage.getMessageType());

		//handle received Message
		if (receivedMessage.getMessageType().equals("PING"))
			{
			//System.out.println("@@@@@@@@@@@@@@GAMEPEER MESSAGE LISTENER--PING@@@@@@@@@@@@@@@");
			
			this.pingMessageAction(receivedMessage, os);
			
			
			}

		if (receivedMessage.getMessageType().equals("POSITION"))
			{
			//System.out.println("@@@@@@@@@@@@@@GAMEPEER MESSAGE LISTENER--POSITION@@@@@@@@@@@@@@@");
			this.positionMessageAction(receivedMessage, os);
			
			}
		if (receivedMessage.getMessageType().equals("CHECKPOSITION"))
			{
			//System.out.println("@@@@@@@@@@@@@@GAMEPEER MESSAGE LISTENER--CHECK POSITION@@@@@@@@@@@@@@@");
			this.checkPositionMessageAction(receivedMessage, os);
			}

		if (receivedMessage.getMessageType().equals("FINDRESOURCE"))
			{
			//System.out.println("@@@@@@@@@@@@@@GAMEPEER MESSAGE LISTENER--FIND RESOURCE@@@@@@@@@@@@@@@");
			this.findResourceMessageAction(receivedMessage, os);
			
			}
		if(receivedMessage.getMessageType().equals("FINDRESOURCE2"))
		{
			//System.out.println("@@@@@@@@@@@@@@GAMEPEER MESSAGE LISTENER--FIND RESOURCE 2@@@@@@@@@@@@@@@");
			this.findResource2MessageAction(receivedMessage, os);
		}
		if (receivedMessage.getMessageType().equals("CHECKFINDRESOURCE"))
			{
			//System.out.println("@@@@@@@@@@@@@@GAMEPEER MESSAGE LISTENER--CHECK FIND RESOURCE@@@@@@@@@@@@@@@");
			this.checkFindResourceMessageAction(receivedMessage, os);
			}


		if (receivedMessage.getMessageType().equals("MOBILERESOURCE"))
			{
			//System.out.println("@@@@@@@@@@@@@@GAMEPEER MESSAGE LISTENER--MOBILE RESOURCE@@@@@@@@@@@@@@@");
			this.mobileResourceMessageAction(receivedMessage, os);
			}
		if (receivedMessage.getMessageType().equals("CHECKMOBILERESOURCE"))
		{
			//System.out.println("@@@@@@@@@@@@@@GAMEPEER MESSAGE LISTENER--CHECK MOBILE RESOURCE@@@@@@@@@@@@@@@");
			this.checkMobileResourceMessageAction(receivedMessage, os);
		}
		if (receivedMessage.getMessageType().equals("STARTMATCH"))
		{
			//System.out.println("@@@@@@@@@@@@@@GAMEPEER MESSAGE LISTENER--START MATCH@@@@@@@@@@@@@@@");
			this.startMatchMessageAction(receivedMessage, os);
		}
		if (receivedMessage.getMessageType().equals("DEFENSE"))
		{
			//System.out.println("@@@@@@@@@@@@@@GAMEPEER MESSAGE LISTENER--DEFENSE@@@@@@@@@@@@@@@");
			this.defenseMessageAction(receivedMessage, os);
		}
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

		//MultiLog.println(GamePeerMessageListener.class.toString(), "Handler for PING MESSAGE");
		//System.out.println(LOG_TAG + "Handler for PING MESSAGE");

		//MultiLog.println(GamePeerMessageListener.class.toString(), LOG_TAG + "Send Ack");
		//System.out.println(LOG_TAG + "Send Ack");

		os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());

	}

	private void positionMessageAction(Message receivedMessage, DataOutputStream os) throws IOException, InterruptedException{

		//MultiLog.println(GamePeerMessageListener.class.toString(), LOG_TAG + "Handler for POSITION MESSAGE");
		//System.out.println(LOG_TAG + "Handler for POSITION MESSAGE");

		PositionPlayerMessage posMessage = new PositionPlayerMessage(receivedMessage);

		GamePlayerResponsible resp = new GamePlayerResponsible(posMessage.getId(),posMessage.getUserName(), posMessage.getPosX(), posMessage.getPosY(), posMessage.getPosZ(),
				posMessage.getVel(), posMessage.getVis(), /*posMessage.getGran(),*/ System.currentTimeMillis(), posMessage.getPositionHash(), posMessage.getOldPos());

		//se manca la prima posizione (oldpos) rivolgersi al server per eseguire la verifica
		if (posMessage.getOldPos().compareTo("") == 0){

			//MultiLog.println(GamePeerMessageListener.class.toString(), "Received a Position Message without OldPosition");
			//System.out.println("Received a Position Message without OldPosition");

			CheckPositionPlayerMessage checkPos = new CheckPositionPlayerMessage("", "", -1, resp.getId(), resp.getName(),
					resp.getPosX(), resp.getPosY(), resp.getPosZ(), resp.getVelocity(), resp.getVisibility(), resp.getPositionHash());

			String responseMessage = MessageSender.sendMessage(this.peer.getGameServerAddr(), this.peer.getGameServerPort(), checkPos.generateXmlMessageString());

			if (responseMessage.contains("ERROR")){
				System.err.println("Sending check position ERROR!");
			}
			else {
				MessageReader messageReader = new MessageReader();
				Message received = messageReader.readMessageFromString(responseMessage.trim());

				AckMessage ackMessage = new AckMessage(received);

				if (ackMessage.getAckStatus() == 0){
					//MultiLog.println(GamePeerMessageListener.class.toString(), "Position possible. Save on cache");
					//System.out.println("Position possible. Save on cache");
					//MultiLog.println(GamePeerMessageListener.class.toString(), "DELLA RISORSA RICEVUTA CHECK SUL SERVER OK");
					//System.out.println("DELLA RISORSA RICEVUTA CHECK SUL SERVER OK");
					this.peer.addRespPlayerOnCache(resp);
					os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());

				}
				else {
					//MultiLog.println(GamePeerMessageListener.class.toString(), "DELLA RISORSA RICEVUTA CHECK SUL SERVER OK");
					//System.out.println("Position impossible. Sending a NACK");
					//MultiLog.println(GamePeerMessageListener.class.toString(), "DELLA RISORSA RICEVUTA CHECK NOOO");
					//System.out.println("DELLA RISORSA RICEVUTA CHECK NOOO");
					os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
				}

			}
			//MultiLog.println(GamePeerMessageListener.class.toString(), "Received a Position from Server");
			//System.out.println("Received a Position from Server");
			return;

		}

		//TODO: prima di salvare la posizione chiedere al precedente responsabile se ï¿½ ammissible e farla cancellare se ok
		//se la posizione non puï¿½ essere aggiornata essendo passato poco tempo cancellare nella cache locale di Chord
		//MultiLog.println(GamePeerMessageListener.class.toString(), "RECEIVED A POSITION MESSAGE");
		//System.out.println("RECEIVED A POSITION MESSAGE");

		//aggiungere il caso in cui il vecchio responsabile ero IO. INUTILE?
		if (this.peer.getResPlayers().containsKey(resp.getOldPos())){
			//MultiLog.println(GamePeerMessageListener.class.toString(), "Have on cache also old position. Checking...");
			//System.out.println("Have on cache also old position. Checking...");

			GamePlayerResponsible oldPlayer = this.peer.getResPlayers().get(resp.getOldPos());

			double scostX = Math.abs(oldPlayer.getPosX() - resp.getPosX());
			double scostY = Math.abs(oldPlayer.getPosY() - resp.getPosY());
			double scostZ = Math.abs(oldPlayer.getPosZ() - resp.getPosZ());

			boolean checkResp = this.peer.checkPosition(oldPlayer, scostX, scostY, scostZ, resp.getVelocity());

			if (checkResp) {
				MultiLog.println(GamePeerMessageListener.class.toString(), "NEW Position OK ");
				//System.out.println("NEW Position OK ");
				this.peer.deleteResPlayer(resp.getOldPos());
				this.peer.addRespPlayerOnCache(resp);
				//MultiLog.println(GamePeerMessageListener.class.toString(),"PUB:CHECK INTERNO OK");
				//System.out.println("PUB:CHECK INTERNO OK");
				//MultiLog.println(GamePeerMessageListener.class.toString(), "Responsible player updated. Sending ACK");
				//System.out.println("Responsible player updated. Sending ACK");
				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());
			}
			else{
				//MultiLog.println(GamePeerMessageListener.class.toString(), "PUB:CHECK INTERNO NOOO");
				//System.out.println("PUB:CHECK INTERNO NOOO");
				//MultiLog.println(GamePeerMessageListener.class.toString(), "Position impossible. Sending a NACK");
				//System.out.println("Position impossible. Sending a NACK");
				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
			}

			return;
		}

		//Richiesta di consistenza all'altro
		InfoPassing respInfo = this.peer.findSuccessor(posMessage.getOldPos(), this.threadId);
		//String responsible = this.peer.findSuccessor(posMessage.getOldPos(), this.threadId);
		String responsible = respInfo.getPeerID();

		//String destAddr = this.peer.getSharedInfos().getInfoFor(this.threadId).getIpAddress();
		String destAddr = respInfo.getPeerData().getIpAddress();
		//int destPort = this.peer.getSharedInfos().getInfoFor(this.threadId).getPortNumber() + 2;
		int destPort = respInfo.getPeerData().getPortNumber() + 2;

		//MultiLog.println(GamePeerMessageListener.class.toString(), "Sending check to responsible " + responsible);
		//System.out.println("Sending check to responsible " + responsible);

		CheckPositionPlayerMessage checkPos = new CheckPositionPlayerMessage("", "", -1, resp.getId(), resp.getName(),
				resp.getPosX(), resp.getPosY(), resp.getPosZ(), resp.getVelocity(), resp.getVisibility(), resp.getOldPos());

		String responseMessage = MessageSender.sendMessage(destAddr, destPort, checkPos.generateXmlMessageString());

		if (responseMessage.contains("ERROR")){
			System.err.println("Sending check position ERROR!");
		}
		else {
			MessageReader messageReader = new MessageReader();
			Message received = messageReader.readMessageFromString(responseMessage.trim());

			AckMessage ackMessage = new AckMessage(received);

			if (ackMessage.getAckStatus() == 0){
				//MultiLog.println(GamePeerMessageListener.class.toString(), "CHECK ESTERNO OK");
				//System.out.println("CHECK ESTERNO OK");
				//MultiLog.println(GamePeerMessageListener.class.toString(), "Position possible. Save on cache");
				//System.out.println("Position possible. Save on cache");
				this.peer.addRespPlayerOnCache(resp);
				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());

			}
			else {
				//MultiLog.println(GamePeerMessageListener.class.toString(), "CHECK ESTERNO FALLITO");
				//System.out.println("CHECK ESTERNO FALLITO");
				//MultiLog.println(GamePeerMessageListener.class.toString(), "Position impossible. Sending a NACK");
				//System.out.println("Position impossible. Sending a NACK");
				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
			}

		}

	}

	//se viene chiesto un check position allora il nodo cancella la risorsa dopo averla verificata
	private void checkPositionMessageAction(Message receivedMessage, DataOutputStream os) throws IOException{

		//MultiLog.println(GamePeerMessageListener.class.toString(), LOG_TAG + "Handler for CHECK POSITION MESSAGE");
		//System.out.println(LOG_TAG + "Handler for CHECK POSITION MESSAGE");

		CheckPositionPlayerMessage checkPos = new CheckPositionPlayerMessage(receivedMessage);

		if (this.peer.getResPlayers().containsKey(checkPos.getOldPos())) {

			//MultiLog.println(GamePeerMessageListener.class.toString(), "Player is on cache. Verifying movement");
			//System.out.println("Player is on cache. Verifying movement");

			GamePlayerResponsible oldPlayer = this.peer.getResPlayers().get(checkPos.getOldPos());

			double scostX = Math.abs(oldPlayer.getPosX() - checkPos.getPosX());
			double scostY = Math.abs(oldPlayer.getPosY() - checkPos.getPosY());
			double scostZ = Math.abs(oldPlayer.getPosZ() - checkPos.getPosZ());

			boolean checkResponse = this.peer.checkPosition(oldPlayer, scostX, scostY, scostZ, checkPos.getVel());

			if (checkResponse){
				//MultiLog.println(GamePeerMessageListener.class.toString(), "Position possible. Sending a ACK");
				//System.out.println("Position possible. Sending a ACK");
				//MultiLog.println(GamePeerMessageListener.class.toString(), "CHECK PASSED");
				//System.out.println("CHECK PASSED");
				this.peer.deleteResPlayer(oldPlayer.getPositionHash());
				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());
			}
			else {
				//MultiLog.println(GamePeerMessageListener.class.toString(), "CHECK NOT PASSED");
				//System.out.println("CHECK NOT PASSED");
				//MultiLog.println(GamePeerMessageListener.class.toString(), "New Position too far. Sending a NACK");
				//System.out.println("New Position too far. Sending a NACK");
				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
			}

		}
		else {
			//MultiLog.println(GamePeerMessageListener.class.toString(), "CHECK NOT PASSED. NOT HAVE POSITION");
			//System.out.println("CHECK NOT PASSED. NOT HAVE POSITION");
			//MultiLog.println(GamePeerMessageListener.class.toString(), "Position not availabe. Sending a NACK");
			//System.out.println("Position not availabe. Sending a NACK");
			os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
		}
	}








	private void findResourceMessageAction(Message receivedMessage, DataOutputStream os) throws IOException, InterruptedException{

		//MultiLog.println(GamePeerMessageListener.class.toString(), LOG_TAG + "Handler FIND RESOURCE MESSAGE");
		//System.out.println(LOG_TAG + "Handler FIND RESOURCE MESSAGE");

		//TODO: prima di rispondere con la risorsa richiesta vedere se chi chiede ha i privilegi sufficienti
		//MultiLog.println(GamePeerMessageListener.class.toString(), "Verify source privileges. Sending check message to reponsible");
		//System.out.println("Verify source privileges. Sending check message to reponsible");

		FindResourceMessage findResourceMessage = new FindResourceMessage(receivedMessage);

		//per efficienza prima di vedere se ha i privilegi vedere se si dispone la risorsa. Altrimenti si invia direttamente NACK
		//verificare anche nella cache delle risorse e non solo dei giocatori
		if ( !this.peer.getResPlayers().containsKey(findResourceMessage.getPositionHash()) &&
				!this.peer.getResResources().containsKey(findResourceMessage.getPositionHash()) )
		{
			os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
			//MultiLog.println(GamePeerMessageListener.class.toString(), "NON ho la risorsa");
			//System.out.println("NON ho la risorsa");
			return;
		}

		//IMPORTANTE:
		//Ho qualcosa nella posizione cercata: RISORSA O PLAYER.
		//Ora occorre verificare che chi cerca abbia i privilegi ciï¿½ deve possedere o un giocatore o una risorsa mobile nell'intorno
		//Tale risorsa\giocatore puï¿½ essere in locale (CHECK INTERNO) o su un altro peer (CHECK ESTERNO)

		//se ho la risorsa Vedo se la richiesta ricevuta ï¿½ ammissibile
		//richiesta al responsabile del richiedente
		InfoPassing respInfo = this.peer.findSuccessor(findResourceMessage.getOldPos(), this.threadId);
		//String resp = this.peer.findSuccessor(findResourceMessage.getOldPos(), this.threadId);
		String resp = respInfo.getPeerID();
		//TODO: modifica per evitare deadlock il caso che chi deve possedere la risorsa e chi esegue il check ï¿½ lo stesso
		//if (resp.compareTo(this.peer.getMyId()) != 0){

		//String destAddr = this.peer.getSharedInfos().getInfoFor(this.threadId).getIpAddress();
		String destAddr = respInfo.getPeerData().getIpAddress();
		//int destPort = this.peer.getSharedInfos().getInfoFor(this.threadId).getPortNumber() + 2;
		int destPort = respInfo.getPeerData().getPortNumber() + 2;

		//In risposta ack o nack. Per chiedere al responsabile di findResource.getOldPos se si hanno i privilegi
		//String sourceName, String sourceSocketAddr, int sourcePort, String username,
		//String position, double x, double y, double z
		CheckFindResourceMessage checkResourceMessage = new CheckFindResourceMessage(findResourceMessage.getSourceName(),destAddr, destPort, findResourceMessage.getUserName(),
				findResourceMessage.getOldPos(), findResourceMessage.getPosX(), findResourceMessage.getPosY(), findResourceMessage.getPosZ());

		//verifica interna (CHECK INTERNO)
		if (resp.compareTo(this.peer.getMyId()) == 0){
			//MultiLog.println(GamePeerMessageListener.class.toString(), "INTERNAL CHECK FIND RESOURCE");
			//System.out.println("INTERNAL CHECK FIND RESOURCE");
			String playerUsername = checkResourceMessage.getUserName();
			double reqX = checkResourceMessage.getPosX();
			double reqY = checkResourceMessage.getPosY();
			double reqZ = checkResourceMessage.getPosZ();

			//prelievo del GIOCATORE in cache che esegue la richiesta - per check
			if (this.peer.getResPlayers().containsKey(checkResourceMessage.getPositionHash())) {

				//confronta la posizione ricevuta con quella dell'utente salvata in cache
				GamePlayerResponsible player = this.peer.getResPlayers().get(checkResourceMessage.getPositionHash());

				//verifica che abbia tutti i privilegi per ottenere la risorsa
				if (player.getPositionHash().compareTo(checkResourceMessage.getPositionHash()) == 0){

					//TODO: verificare che sia in un intorno della poszione dichiarata
//						if(reqX >= player.getPosX() - player.getVisibility() && reqX <= player.getPosY() + player.getVisibility()
//								&& reqY >= player.getPosY() - player.getVisibility() && reqY <= player.getPosY() + player.getVisibility()
//								&& reqZ >= player.getPosZ() - player.getVisibility() && reqZ <= player.getPosZ() + player.getVisibility()){
					if (true){ //solo per prova //TODO::
					//invio delle informazioni richieste
						if (this.peer.getResPlayers().containsKey(findResourceMessage.getPositionHash())){
							//GamePlayerResponsible infoRequested = this.peer.getResPlayers().get(findResourceMessage.getPositionHash());
																//this.peer.getResPlayers().containsKey(findResourceMessage.getPositionHash()
							GamePlayerResponsible infoRequested = this.peer.getResPlayers().get(findResourceMessage.getPositionHash());

							PositionPlayerMessage posPlayer = new PositionPlayerMessage("","",-1, infoRequested.getId(),infoRequested.getName(),
									infoRequested.getPositionHash(), infoRequested.getPosX(), infoRequested.getPosY(), infoRequested.getPosZ(),
									infoRequested.getVelocity(), infoRequested.getVisibility(), infoRequested.getOldPos());

							//MultiLog.println(GamePeerMessageListener.class.toString(), "invio informazioni risorsa richieste");
							//System.out.println("invio informazioni risorsa richieste");
							os.write(posPlayer.generateXmlMessageString().getBytes());

						}
						else if (this.peer.getResResources().containsKey(findResourceMessage.getPositionHash())){

							GameResourceMobileResponsible infoRequested = this.peer.getResResources().get(findResourceMessage.getPositionHash());

							MobileResourceMessage posResource = new MobileResourceMessage("","",-1, infoRequested.getId(), infoRequested.getDescription(),
									infoRequested.getPositionHash(), infoRequested.getX(), infoRequested.getY(), infoRequested.getZ(), infoRequested.getVelocity(), infoRequested.getVision(),
									infoRequested.getOldPos(), infoRequested.getOwner(), infoRequested.getOwnerId(), infoRequested.getQuantity());

							//MultiLog.println(GamePeerMessageListener.class.toString(), "invio informazioni risorsa MOBILE richieste");
							//System.out.println("invio informazioni risorsa MOBILE richieste");
							os.write(posResource.generateXmlMessageString().getBytes());
						}
					}
					else{
						//MultiLog.println(GamePeerMessageListener.class.toString(), "Position out of bound. Sending a NACK");
						//System.out.println("Position out of bound. Sending a NACK");

						os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
					}

				}
				else{
					//MultiLog.println(GamePeerMessageListener.class.toString(), "Position error. Sending a NACK");
					//System.out.println("Position error. Sending a NACK");

					os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
				}

			}//prelievo della RISORSA in cache che esegue la richiesta - per check
			else if (this.peer.getResResources().containsKey(checkResourceMessage.getPositionHash())) {

				//confronta la posizione ricevuta con quella dell'utente salvata in cache
				GameResourceMobileResponsible resource = this.peer.getResResources().get(checkResourceMessage.getPositionHash());

				//verifica che abbia tutti i privilegi per ottenere la risorsa
				if (resource.getPositionHash().compareTo(checkResourceMessage.getPositionHash()) == 0){
					//TODO: TEST nell' intorno per la risorsa presente e la posizione dichiarata
					//TODO: verificare che sia in un intorno della poszione dichiarata
//						if(reqX >= player.getPosX() - player.getVisibility() && reqX <= player.getPosY() + player.getVisibility()
//								&& reqY >= player.getPosY() - player.getVisibility() && reqY <= player.getPosY() + player.getVisibility()
//								&& reqZ >= player.getPosZ() - player.getVisibility() && reqZ <= player.getPosZ() + player.getVisibility()){
					if (true){ //solo per prova //TODO::
					//invio delle informazioni richieste
						//GamePlayerResponsible infoRequested = this.peer.getResPlayers().get(findResourceMessage.getPositionHash());
						if (this.peer.getResPlayers().containsKey(findResourceMessage.getPositionHash())){
							//GamePlayerResponsible infoRequested = this.peer.getResPlayers().get(findResourceMessage.getPositionHash());
																//this.peer.getResPlayers().containsKey(findResourceMessage.getPositionHash()
							GamePlayerResponsible infoRequested = this.peer.getResPlayers().get(findResourceMessage.getPositionHash());

							PositionPlayerMessage posPlayer = new PositionPlayerMessage("","",-1, infoRequested.getId(),infoRequested.getName(),
									infoRequested.getPositionHash(), infoRequested.getPosX(), infoRequested.getPosY(), infoRequested.getPosZ(),
									infoRequested.getVelocity(), infoRequested.getVisibility(), infoRequested.getOldPos());

							//MultiLog.println(GamePeerMessageListener.class.toString(), "invio informazioni risorsa richieste");
							//System.out.println("invio informazioni risorsa richieste");
							os.write(posPlayer.generateXmlMessageString().getBytes());

						}
						else if (this.peer.getResResources().containsKey(findResourceMessage.getPositionHash())){

							GameResourceMobileResponsible infoRequested = this.peer.getResResources().get(findResourceMessage.getPositionHash());

							MobileResourceMessage posResource = new MobileResourceMessage("","",-1, infoRequested.getId(), infoRequested.getDescription(),
									infoRequested.getPositionHash(), infoRequested.getX(), infoRequested.getY(), infoRequested.getZ(), infoRequested.getVelocity(), infoRequested.getVision(),
									infoRequested.getOldPos(), infoRequested.getOwner(), infoRequested.getOwnerId(), infoRequested.getQuantity());

							//MultiLog.println(GamePeerMessageListener.class.toString(), "invio informazioni risorsa MOBILE richieste");
							//System.out.println("invio informazioni risorsa MOBILE richieste");
							os.write(posResource.generateXmlMessageString().getBytes());
						}

					}
					else{
						//MultiLog.println(GamePeerMessageListener.class.toString(), "Position RESOURCE out of bound. Sending a NACK");
						//System.out.println("Position RESOURCE out of bound. Sending a NACK");

						os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
					}

				}
				else{
					//MultiLog.println(GamePeerMessageListener.class.toString(), "Position RESOURCE error. Sending a NACK");
					//System.out.println("Position RESOURCE error. Sending a NACK");

					os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
				}

			} // NACK complessivo
			else {
				//MultiLog.println(GamePeerMessageListener.class.toString(), "Position not availabe. Sending a NACK");
				//System.out.println("Position not availabe. Sending a NACK");

				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
			}
			return;
		}

		//invio del check per verificare i privilegi (CHECK ESTERNO)
		String responseMessage = MessageSender.sendMessage(destAddr, destPort, checkResourceMessage.generateXmlMessageString());

		if (responseMessage.contains("ERROR")){
			//MultiLog.println(GamePeerMessageListener.class.toString(), "Sending Check Resource Message ERROR !");
			//System.err.println("Sending Check Resource Message ERROR !");
			os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
		}
		else {
			MessageReader messageReader = new MessageReader();
			Message recvMessage = messageReader.readMessageFromString(responseMessage.trim());

			//legge l'ack\nack ricevuto
			AckMessage ackMessage = new AckMessage(recvMessage);

			if (ackMessage.getAckStatus() == 0){
				if (this.peer.getResPlayers().containsKey(findResourceMessage.getPositionHash())) {

					//TODO: recupera le informazioni e invia
					//String name, double posX, double posY, double posZ,
					//double vel, double visibility, double gran,
					//long time, String pos, String oldPos

					GamePlayerResponsible infoRequested = this.peer.getResPlayers().get(findResourceMessage.getPositionHash());

					//TODO: vedere se non ï¿½ di un altro tipo di risorsa
					//String sourceName, String sourceSocketAddr, int sourcePort, String username,
					//String position, double x, double y, double z, double vel, double vis, String oldPos
					PositionPlayerMessage posPlayer = new PositionPlayerMessage("","",-1, infoRequested.getId(),infoRequested.getName(),
							infoRequested.getPositionHash(), infoRequested.getPosX(), infoRequested.getPosY(), infoRequested.getPosZ(),
							infoRequested.getVelocity(), infoRequested.getVisibility(), infoRequested.getOldPos());

					//MultiLog.println(GamePeerMessageListener.class.toString(), "invio informazioni risorsa richieste");
					//System.out.println("invio informazioni risorsa richieste");
					os.write(posPlayer.generateXmlMessageString().getBytes());
				}
				else if (this.peer.getResResources().containsKey(findResourceMessage.getPositionHash())){
					GameResourceMobileResponsible infoReqested = this.peer.getResResources().get(findResourceMessage.getPositionHash());

//						String sourceName, String sourceSocketAddr, int sourcePort, String id, String username,
//						String position, double x, double y, double z, double vel, double vis, String oldPos, String owner, String ownerId, double quantity
					MobileResourceMessage posResource = new MobileResourceMessage("","",-1, infoReqested.getId(), infoReqested.getDescription(), infoReqested.getPositionHash(),
							infoReqested.getX(), infoReqested.getY(),infoReqested.getZ(), infoReqested.getVelocity(), infoReqested.getVision(),
							infoReqested.getOldPos(), infoReqested.getOwner(), infoReqested.getOwnerId(), infoReqested.getQuantity());

					//MultiLog.println(GamePeerMessageListener.class.toString(), "invio informazioni sulla RISORSA MOBILE richieste");
					//System.out.println("invio informazioni sulla RISORSA MOBILE richieste");
					os.write(posResource.generateXmlMessageString().getBytes());
				}

			} else {
			//invio nack non avendo ricevuto l'ok da parte del responsabile
				//MultiLog.println(GamePeerMessageListener.class.toString(), "Not enough privileges. Sending a NACK");
				//System.out.println("Not enough privileges. Sending a NACK");

				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());

			}


		}

		//}

	}

	private void findResource2MessageAction(Message receivedMessage, DataOutputStream os) throws IOException, InterruptedException{
		
		FindResourceMessage2 findResourceMessage = new FindResourceMessage2(receivedMessage);
		
		if ( !this.peer.getResPlayers().containsKey(findResourceMessage.getPositionHash()) &&
				!this.peer.getResResources().containsKey(findResourceMessage.getPositionHash()) )
		{
			os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
			
			return;
		}
		else if(this.peer.getResPlayers().containsKey(findResourceMessage.getPositionHash()))
		{
			GamePlayerResponsible infoRequested = this.peer.getResPlayers().get(findResourceMessage.getPositionHash());

			PositionPlayerMessage posPlayer = new PositionPlayerMessage("","",-1, infoRequested.getId(),infoRequested.getName(),
					infoRequested.getPositionHash(), infoRequested.getPosX(), infoRequested.getPosY(), infoRequested.getPosZ(),
					infoRequested.getVelocity(), infoRequested.getVisibility(), infoRequested.getOldPos());

			
			os.write(posPlayer.generateXmlMessageString().getBytes());
			return;
			
		}
		else if(this.peer.getResResources().containsKey(findResourceMessage.getPositionHash()))
		{
			GameResourceMobileResponsible infoRequested = this.peer.getResResources().get(findResourceMessage.getPositionHash());

			MobileResourceMessage posResource = new MobileResourceMessage("","",-1, infoRequested.getId(), infoRequested.getDescription(),
					infoRequested.getPositionHash(), infoRequested.getX(), infoRequested.getY(), infoRequested.getZ(), infoRequested.getVelocity(), infoRequested.getVision(),
					infoRequested.getOldPos(), infoRequested.getOwner(), infoRequested.getOwnerId(), infoRequested.getQuantity());

			
			os.write(posResource.generateXmlMessageString().getBytes());
			
			return;
		}

		
	}
	private void checkFindResourceMessageAction(Message messageReceived, DataOutputStream os) throws IOException {

		//MultiLog.println(GamePeerMessageListener.class.toString(), LOG_TAG + "Handler CHECK FIND RESOURCE MESSAGE");
		//System.out.println(LOG_TAG + "Handler CHECK FIND RESOURCE MESSAGE");

		CheckFindResourceMessage checkResourceMessage = new CheckFindResourceMessage(messageReceived);
		//Verifica dei privilegi
		//TODO: ricerca nella cache locale la posizione di
		String playerUsername = checkResourceMessage.getUserName();
		double reqX = checkResourceMessage.getPosX();
		double reqY = checkResourceMessage.getPosY();
		double reqZ = checkResourceMessage.getPosZ();

		if (this.peer.getResPlayers().containsKey(checkResourceMessage.getPositionHash())) {

			//confronta la posizione ricevuta con quella dell'utente salvata in cache
			GamePlayerResponsible player = this.peer.getResPlayers().get(checkResourceMessage.getPositionHash());

			if (player.getPositionHash().compareTo(checkResourceMessage.getPositionHash()) == 0){

				//TODO: oppure che ci sia una risorsa nella posizione dichiarata e appartenga al proprietario. FARE IL CHECK COME NELL'IF PRECEDENTE
//				if ( this.peer.getResResources().containsKey(checkResourceMessage.getPositionHash()) &&
//						this.peer.getResResources().get(checkResourceMessage.getPositionHash()).getOwnerId().compareTo(checkResourceMessage.getSourceName()) == 0 )
//				//TODO: verificare che sia in un intorno della poszione dichiarata
//				if(reqX >= player.getPosX() - player.getVisibility() && reqX <= player.getPosY() + player.getVisibility()
//						&& reqY >= player.getPosY() - player.getVisibility() && reqY <= player.getPosY() + player.getVisibility()
//						&& reqZ >= player.getPosZ() - player.getVisibility() && reqZ <= player.getPosZ() + player.getVisibility()){
				if (true) { //solo per prova //TODO:

					//MultiLog.println(GamePeerMessageListener.class.toString(), "Position correct. Sending a ACK");
					//System.out.println("Position correct. Sending a ACK");

					os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());

				}
				else{
					//MultiLog.println(GamePeerMessageListener.class.toString(), "Position out of bound. Sending a NACK");
					//System.out.println("Position out of bound. Sending a NACK");

					os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
				}

			}
			else{
				//MultiLog.println(GamePeerMessageListener.class.toString(), "Position error. Sending a NACK");
				//System.out.println("Position error. Sending a NACK");

				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
			}

		}
		else if (this.peer.getResResources().containsKey(checkResourceMessage.getPositionHash())) {

			//confronta la posizione ricevuta con quella dell'utente salvata in cache
			//GamePlayerResponsible player = this.peer.getResPlayers().get(checkResourceMessage.getPositionHash());
			GameResourceMobileResponsible resource = this.peer.getResResources().get(checkResourceMessage.getPositionHash());

			if (resource.getPositionHash().compareTo(checkResourceMessage.getPositionHash()) == 0){

				//TODO: oppure che ci sia una risorsa nella posizione dichiarata e appartenga al proprietario. FARE IL CHECK COME NELL'IF PRECEDENTE
//				if ( this.peer.getResResources().containsKey(checkResourceMessage.getPositionHash()) &&
//						this.peer.getResResources().get(checkResourceMessage.getPositionHash()).getOwnerId().compareTo(checkResourceMessage.getSourceName()) == 0 )
//				//TODO: verificare che sia in un intorno della poszione dichiarata
//				if(reqX >= player.getPosX() - player.getVisibility() && reqX <= player.getPosY() + player.getVisibility()
//						&& reqY >= player.getPosY() - player.getVisibility() && reqY <= player.getPosY() + player.getVisibility()
//						&& reqZ >= player.getPosZ() - player.getVisibility() && reqZ <= player.getPosZ() + player.getVisibility()){
				if (true) { //solo per prova //TODO:

					//MultiLog.println(GamePeerMessageListener.class.toString(), "Position MOBILE RESOURCE correct. Sending a ACK");
					//System.out.println("Position MOBILE RESOURCE correct. Sending a ACK");

					os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());

				}
				else{
					//MultiLog.println(GamePeerMessageListener.class.toString(), "Position MOBILE RESOURCE out of bound. Sending a NACK");
					//System.out.println("Position MOBILE RESOURCE out of bound. Sending a NACK");

					os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
				}

			}
			else{
				//MultiLog.println(GamePeerMessageListener.class.toString(), "Position MOBILE RESOURCE error. Sending a NACK");
				//System.out.println("Position MOBILE RESOURCE error. Sending a NACK");

				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
			}

		}

		else {
			//MultiLog.println(GamePeerMessageListener.class.toString(), "Position not availabe. Sending a NACK");
			//System.out.println("Position not availabe. Sending a NACK");

			os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
		}

	}


	private void mobileResourceMessageAction(Message messageReceived, DataOutputStream os) throws IOException, InterruptedException{
		//MultiLog.println(GamePeerMessageListener.class.toString(), LOG_TAG + "Handler for MOBILE RESOURCE MESSAGE");
		//System.out.println(LOG_TAG + "Handler for MOBILE RESOURCE MESSAGE");

		MobileResourceMessage resMessage = new MobileResourceMessage(messageReceived);

		GameResourceMobileResponsible resp = new GameResourceMobileResponsible(resMessage.getId(), resMessage.getUserName(), resMessage.getOwner(), resMessage.getOwnerId(),
				resMessage.getQuantity(), resMessage.getPosX(), resMessage.getPosY(), resMessage.getPosZ(), resMessage.getVel(), resMessage.getVis(),
				System.currentTimeMillis(), resMessage.getPositionHash(), resMessage.getOldPos());

		//TODO: fare i diversi casi e quello in cui non si ha l'old ma ï¿½ uguale a quello del Player (SUL CHECK)

		if (this.peer.getResResources().containsKey(resp.getOldPos())){
			//MultiLog.println(GamePeerMessageListener.class.toString(), "HAVE ON CACHE ALSO OLD POSITION");
			//System.out.println("HAVE ON CACHE ALSO OLD POSITION");

			GameResourceMobileResponsible oldResource = this.peer.getResResources().get(resp.getOldPos());

			double scostX = Math.abs(oldResource.getX() - resp.getX());
			double scostY = Math.abs(oldResource.getY() - resp.getY());
			double scostZ = Math.abs(oldResource.getZ() - resp.getZ());

			boolean checkResp = this.peer.checkResourceMobile(oldResource, scostX, scostY, scostZ, resp.getVelocity());

			if (checkResp){

				//MultiLog.println(GamePeerMessageListener.class.toString(), "NEW RESOURCE POSITION OK");
				//System.out.println("NEW RESOURCE POSITION OK");

				this.peer.deleteResResource(resp.getOldPos());
				this.peer.addRespResourceOnCache(resp);

				//MultiLog.println(GamePeerMessageListener.class.toString(), "PUB e CHECK INTERNO");
				//System.out.println("PUB e CHECK INTERNO");
				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());
			}
			else {
				//MultiLog.println(GamePeerMessageListener.class.toString(), "PUB e CHECK INTERNO FALLITO");
				//System.out.println("PUB e CHECK INTERNO FALLITO");
				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
			}

			return;
		}
		//se si ha la posizione del player propietario
		if (this.peer.getResPlayers().containsKey(resp.getOldPos()) &&
				resp.getOwnerId().compareTo(this.peer.getResPlayers().get(resp.getOldPos()).getId()) == 0){
			//MultiLog.println(GamePeerMessageListener.class.toString(), "SI ha in cache la posizione del propietario");
			//System.out.println("SI ha in cache la posizione del propietario");

			GamePlayerResponsible oldPlayer = this.peer.getResPlayers().get(resp.getOldPos());
			double scostX = Math.abs(oldPlayer.getPosX() - resp.getX());
			double scostY = Math.abs(oldPlayer.getPosY() - resp.getY());
			double scostZ = Math.abs(oldPlayer.getPosZ() - resp.getZ());

			boolean checkResp = this.peer.checkPosition(oldPlayer, scostX, scostY, scostZ, resp.getVelocity());

			if (checkResp){
				//MultiLog.println(GamePeerMessageListener.class.toString(), "new Resource POsition OK for PLAYER POSITION");
				//System.out.println("new Resource POsition OK for PLAYER POSITION");
				this.peer.addRespResourceOnCache(resp);
				//MultiLog.println(GamePeerMessageListener.class.toString(), "PUB E CHECK interno per OLD PLAYER");
				//System.out.println("PUB E CHECK interno per OLD PLAYER");
				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());
			}
			else {
				//MultiLog.println(GamePeerMessageListener.class.toString(), "PUB e CHECK INTERNO OLD PLAYER FALLITO");
				//System.out.println("PUB e CHECK INTERNO OLD PLAYER FALLITO");
				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
			}
			return;
		}

		//richiesta di consistenza a quello che dovrebbe essere il vecchio propietario
		InfoPassing respInfo = this.peer.findSuccessor(resMessage.getOldPos(), this.threadId);
		//String responsible = this.peer.findSuccessor(resMessage.getOldPos(), this.threadId);
		String responsible = respInfo.getPeerID();
		//String destAddr = this.peer.getSharedInfos().getInfoFor(this.threadId).getIpAddress();
		String destAddr = respInfo.getPeerData().getIpAddress();
		//int destPort = this.peer.getSharedInfos().getInfoFor(this.threadId).getPortNumber() + 2;
		int destPort = respInfo.getPeerData().getPortNumber() + 2;

		//MultiLog.println(GamePeerMessageListener.class.toString(), "SEND ack for RESOURCE to " + responsible);
		//System.out.println("SEND ack for RESOURCE to " + responsible);
		//String sourceName, String sourceSocketAddr, int sourcePort, String id, String username,
		 //double x, double y, double z, double vel, double vis, String oldPos, String owner, String ownerId, double quantity
		CheckMobileResourceMessage checkRes = new CheckMobileResourceMessage("","",-1, resp.getId(),resp.getDescription(),
				resp.getX(), resp.getY(), resp.getZ(), resp.getVelocity(), resp.getVision(), resp.getOldPos(), resp.getOwner(), resp.getOwnerId(), resp.getQuantity() );

		String responseMessage = MessageSender.sendMessage(destAddr, destPort, checkRes.generateXmlMessageString());

		if (responseMessage.contains("ERROR")){
			System.err.println("Sending check resource ERROR!");
		}
		else {
			MessageReader messageReader = new MessageReader();
			Message received = messageReader.readMessageFromString(responseMessage.trim());

			AckMessage ackMessage = new AckMessage(received);
			if (ackMessage.getAckStatus() == 0){
				MultiLog.println(GamePeerMessageListener.class.toString(), "CHECK RISORSA ESTERNA OK");
				//System.out.println("CHECK RISORSA ESTERNA OK");
				this.peer.addRespResourceOnCache(resp);
				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());
			}
			else {
				//MultiLog.println(GamePeerMessageListener.class.toString(), "CHECK RISORSA ESTERNA FALLITO");
				//System.out.println("CHECK RISORSA ESTERNA FALLITO");
				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
			}
		}
	}

	//dï¿½ check ok anche se possiede la posizione del propietario che ï¿½ quella dell'oldpos
	private void checkMobileResourceMessageAction(Message messageReceived, DataOutputStream os) throws IOException {
		//MultiLog.println(GamePeerMessageListener.class.toString(), LOG_TAG + "Handler for CHECK MOBILE RESOURCE MESSAGE");
		//System.out.println(LOG_TAG + "Handler for CHECK MOBILE RESOURCE MESSAGE");

		CheckMobileResourceMessage checkResPos = new CheckMobileResourceMessage(messageReceived);

		if (this.peer.getResResources().containsKey(checkResPos.getOldPos())){

			//MultiLog.println(GamePeerMessageListener.class.toString(), "Resource is on cache");
			//System.out.println("Resource is on cache");

			GameResourceMobileResponsible oldResource = this.peer.getResResources().get(checkResPos.getOldPos());

			double scostX = Math.abs(oldResource.getX() - checkResPos.getPosX());
			double scostY = Math.abs(oldResource.getY() - checkResPos.getPosY());
			double scostZ = Math.abs(oldResource.getZ() - checkResPos.getPosZ());

			boolean checkResponse = this.peer.checkResourceMobile(oldResource, scostX, scostY, scostZ, checkResPos.getVel());

			if (checkResponse) {
				//MultiLog.println(GamePeerMessageListener.class.toString(), "CHECK for RESOURCE position OK");
				//System.out.println("CHECK for RESOURCE position OK");

				this.peer.deleteResResource(oldResource.getPositionHash());

				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());
			}
			else {
				//MultiLog.println(GamePeerMessageListener.class.toString(), "CHECK for RESOURCE position NO");
				//System.out.println("CHECK for RESOURCE position NO");
				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
			}
		}
		else if (this.peer.getResPlayers().containsKey(checkResPos.getOldPos()) &&
				checkResPos.getOwnerId().compareTo(this.peer.getResPlayers().get(checkResPos.getOldPos()).getId()) == 0) {

			//MultiLog.println(GamePeerMessageListener.class.toString(), "CHECK WITH PLAYER OWNER POSITION");
			//System.out.println("CHECK WITH PLAYER OWNER POSITION");
			GamePlayerResponsible player = this.peer.getResPlayers().get(checkResPos.getOldPos());
			double scostX = Math.abs(player.getPosX() - checkResPos.getPosX());
			double scostY = Math.abs(player.getPosY() - checkResPos.getPosY());
			double scostZ = Math.abs(player.getPosZ() - checkResPos.getPosZ());

			boolean checkResponse = this.peer.checkPosition(player, scostX, scostY, scostZ, checkResPos.getVel());

			if (checkResponse) {
				//MultiLog.println(GamePeerMessageListener.class.toString(), "CHECK for RESOURCE position OK");
				//System.out.println("CHECK for RESOURCE position OK");

				//this.peer.deleteResResource(oldResource.getPositionHash());

				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());
			}
			else {
				//MultiLog.println(GamePeerMessageListener.class.toString(), "CHECK for RESOURCE position NO");
				//System.out.println("CHECK for RESOURCE position NO");
				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
			}

		}
		else {

			//MultiLog.println(GamePeerMessageListener.class.toString(), "CHECK for RESOURCE NO mancanza RISORSA");
			//System.out.println("CHECK for RESOURCE NO mancanza RISORSA");

			os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
		}
	}

	//occorre registrare l'hash dell'attacco ricevuto e rispondere ack
	private void startMatchMessageAction(Message messageReceived, DataOutputStream os) throws IOException {
		//this.peer.setInMatch(true);
		//MultiLog.println(GamePeerMessageListener.class.toString(), LOG_TAG + "Handler for START MATCH MESSAGE");
		//System.out.println(LOG_TAG + "Handler for START MATCH MESSAGE");
		System.out.println("#########GamePeerMessageListener-->startMatchMessageAction##########");
		//TODO: mettere controlli per verificare la posizione dell'altro avversario
		StartMatchMessage startMatch = new StartMatchMessage(messageReceived);
		
		System.out.println(".....START MATCH message log.....");
		//StartMatchMessage(String sourceName, String sourceSocketAddr, int sourcePort,String resourceId,  String id, String username,
			//	String position, double x, double y, double z, String hash) 
		System.out.println("sourceName " + startMatch.getSourceName());
		System.out.println("sourceSocketAddr " + startMatch.getSourceSocketAddr());
		System.out.println("sourcePort " + startMatch.getSourcePort());
		System.out.println("resourceID " + startMatch.getResourceId());
		System.out.println("id " + startMatch.getId());
		System.out.println("username " + startMatch.getUserName());
		System.out.println("position " + startMatch.getPosition());
		System.out.println("x " + startMatch.getPosX());
		System.out.println("y " + startMatch.getPosY());
		System.out.println("z " + startMatch.getPosZ());
		System.out.println("hash "+ startMatch.getHash());
		System.out.println("..................................");
		
		if (this.peer.addAttackReceived(startMatch.getId(), startMatch.getUserName(), startMatch.getHash())){

			//MultiLog.println(GamePeerMessageListener.class.toString(), "Attack RECEIVED");
			System.out.println("Attack RECEIVED");
			//jose' murga 01/08/2011
			
			
			//Lo scontro pu� avvenire con un pianeta (primo ramo dell'IF)
			//oppure con una risorsa mobile (ramo ELSE)
			
			String resource=startMatch.getResourceId();
			if(resource.equals(this.peer.getMyId()))
			{
				//ottengo il primo GameResource
				GameResource res=null;
				ArrayList<Object> resources=this.peer.getMyResources();
				
				int i=0;
				
				boolean band=false;
				
				while(!band)
				{
					Object obj=resources.get(i);
					
					if(obj instanceof GameResourceEvolve)
					{
						i++;
						
					}
					else if(obj instanceof GameResourceMobile)
					{
						i++;
						
					}
					else
					{
						//is GameResource
						res=(GameResource)obj;
						band=true;
					}
					if(i==resources.size())
					{
						band=true;
						
					}
					
					
				}
				
				
				if(i==resources.size())
				{
					System.out.println("############BASE-->NON CI SONO DIFESE##############");

					os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
					//this.peer.setInMatch(false);
				}
				else //se ho trovato una risorsa
				{
					os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());
					
					//dopo aver inviato l'ack, inizio la risposta
					//threadId del nemico
					//metto come id della risorsa l'id del peer
					//this.peer.defenseMatch(startMatch.getId(), startMatch.getUserName(),peer.getMyId(),res.getQuantity() , threadId,peer.getPlayer().getPosX(),peer.getPlayer().getPosY(),peer.getPlayer().getPosZ());
					System.out.println("############BASE##############");
					
					long current=System.currentTimeMillis();
										
					String strlog="";
					Calendar now = Calendar.getInstance();
					strlog=now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND)+":"+now.get(Calendar.MILLISECOND)+"-"+res.getId()+"-def";
					this.peer.getOutput().println(strlog);
					
					this.peer.defenseMatch(startMatch.getId(), startMatch.getUserName(),startMatch.getSourceSocketAddr(),startMatch.getSourcePort(),peer.getMyId(),res.getQuantity() , threadId,peer.getPlayer().getPosX(),peer.getPlayer().getPosY(),peer.getPlayer().getPosZ());
					
				}
				
				
				
				
			}
			else
			{
				GameResource objres=this.peer.getMyResourceFromId(resource);
				
				os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 0, "")).generateXmlMessageString().getBytes());
				
				if(objres instanceof GameResourceMobile)
				{
					System.out.println("################GameResourceMobile###############");
					
					
					
					GameResourceMobile res=(GameResourceMobile)objres;
					
					long current=System.currentTimeMillis();
					
					String strlog="";
					Calendar now = Calendar.getInstance();
					strlog=now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND)+":"+now.get(Calendar.MILLISECOND)+"-"+res.getId()+"-def";
					this.peer.getOutput().println(strlog);
					
					
					this.peer.defenseMatch(startMatch.getId(), startMatch.getUserName(),startMatch.getSourceSocketAddr(),startMatch.getSourcePort(),peer.getMyId(),res.getQuantity() , threadId,peer.getPlayer().getPosX(),peer.getPlayer().getPosY(),peer.getPlayer().getPosZ());
					
				}
				else
				{
					System.out.println("############GameResource##############�");
					
					long current=System.currentTimeMillis();
					
					String strlog="";
					Calendar now = Calendar.getInstance();
					strlog=now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND)+":"+now.get(Calendar.MILLISECOND)+"-"+objres.getId()+"-def";
					this.peer.getOutput().println(strlog);
					
					this.peer.defenseMatch(startMatch.getId(), startMatch.getUserName(),startMatch.getSourceSocketAddr(),startMatch.getSourcePort(),peer.getMyId(),objres.getQuantity() , threadId,peer.getPlayer().getPosX(),peer.getPlayer().getPosY(),peer.getPlayer().getPosZ());
					
				}
				
				
				

				
				
				
				//dopo aver inviato l'ack, inizio la risposta
				//threadId del nemico
				//this.peer.defenseMatch(startMatch.getId(), startMatch.getUserName(),res.getId(),res.getQuantity() , threadId,res.getX(),res.getY(),res.getZ());
				/*System.out.println("-----STARTMATCH--------");
				System.out.println(startMatch);
				System.out.println("(startMatch.getId() "+ startMatch.getId());
				System.out.println("startMatch.getUserName() " + startMatch.getUserName());
				System.out.println("startMatch.getSourceSocketAddr() " + startMatch.getSourceSocketAddr());
				System.out.println("startMatch.getSourcePort() " + startMatch.getSourcePort());
				
				System.out.println(peer);
				System.out.println("peer.getMyId() " + peer.getMyId());
				System.out.println("peer.getPlayer() " + peer.getPlayer());
				System.out.println("peer.getPlayer().getPosX() " + peer.getPlayer().getPosX());
				System.out.println("peer.getPlayer().getPosY() " + peer.getPlayer().getPosY());
				System.out.println("peer.getPlayer().getPosZ() " + peer.getPlayer().getPosZ());
				
				System.out.println("res " + res);
				System.out.println("res.getQuantity() " + res.getQuantity());
				
				System.out.println("threadId " + threadId);*/
				//this.peer.defenseMatch(startMatch.getId(), startMatch.getUserName(),startMatch.getSourceSocketAddr(),startMatch.getSourcePort(),peer.getMyId(),res.getQuantity() , threadId,peer.getPlayer().getPosX(),peer.getPlayer().getPosY(),peer.getPlayer().getPosZ());
			}
			
		    
			

		}
		else {
			//MultiLog.println(GamePeerMessageListener.class.toString(), "Attack Refused");
			//System.out.println("Attack Refused");

			os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
		}

	}

	//ricezione della difesa e risposta con l'attacco in chiaro
	private void defenseMessageAction(Message messageReceived, DataOutputStream os) throws IOException {
		//MultiLog.println(GamePeerMessageListener.class.toString(), LOG_TAG + "Handler for DEFENSE MESSAGE");
		//System.out.println(LOG_TAG + "Handler for DEFENSE MESSAGE");
		System.out.println("#########GamePeerMessageListener-->defenseMatchAction###############");
		DefenseMatchMessage defenseMessage = new DefenseMatchMessage(messageReceived);

		Defense defense = new Defense(defenseMessage.getQuantity(), defenseMessage.getResource());

		if (this.peer.addDefenseReceived(defenseMessage.getId(), defenseMessage.getUserName(), defense)){

			Attack clearAttack =  (Attack) this.peer.getAttackClear(defenseMessage.getId());

			ClearAttackMatchMessage clearMessage = new ClearAttackMatchMessage("","",-1, this.peer.getPlayer().getId(), this.peer.getPlayer().getName(),
					this.peer.getPlayer().getSpatialPosition(), this.peer.getPlayer().getPosX(), this.peer.getPlayer().getPosY(), this.peer.getPlayer().getPosZ(),
					clearAttack.getHash(), clearAttack.getType(), clearAttack.getQuantity(), clearAttack.getNonce());

			os.write(clearMessage.generateXmlMessageString().getBytes());

			this.peer.closeMatch(defenseMessage.getId());
			
			//so che lo scontro e' finito, quindi controllo l'esito
			ArrayList<Result> results=this.peer.getClashes().get(defenseMessage.getId()).getResults();
			int sz=results.size();
			Result result=results.get(sz-1);
			if(result==Result.WIN)
			{
				System.out.println("#############Ho vinto##############");
				
			}
			else
			{
				System.out.println("################Ho perso############");
				//tolgo il gameresource coinvolto nello scontro
				/*GameResource res=null;
				ArrayList<Object> resources=this.peer.getMyResources();
				
				int i=resources.size()-1;
				
				while(i>=0)
				{
					Object aux=resources.get(i);
					if(aux instanceof GameResourceMobile)
					{
						i--;
					}
					else if(aux instanceof GameResourceEvolve)
					{
						i--;
					}
					else
					{
						res=(GameResource)aux;
						i=-1;
					}
					
				}
				this.peer.removeToMyResources(res);*/
				
			}

		}
		else {
			//MultiLog.println(GamePeerMessageListener.class.toString(), "Invalid defense");
			//System.out.println("Invalid defense");

			os.write((new AckMessage(this.listenerId, this.listenerAddr, this.listenerPort, 1, "")).generateXmlMessageString().getBytes());
		}
		
		//this.peer.setInMatch(false);
	}
}
