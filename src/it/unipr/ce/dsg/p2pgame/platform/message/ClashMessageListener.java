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

public class ClashMessageListener implements Runnable{
	
	private final String LOG_TAG = "Game Peer MESSAGE LISTENER: ";
	private String listenerId = null;
	private String listenerAddr = null;
	private int listenerPort;
	private GamePeer peer;
	private String threadId = new Long(Thread.currentThread().getId()).toString();
	private long gameStartTimestamp = 0;

	public ClashMessageListener(String listenerId, String listenerAddr,
			int listenerPort, GamePeer peer, long gameStartTimestamp)
	{
		super();
		this.listenerId = listenerId;
		this.listenerAddr = listenerAddr;
		this.listenerPort = listenerPort;
		this.peer = peer;
		this.threadId=peer.getMyThreadId();
		this.gameStartTimestamp = gameStartTimestamp;
		
		System.out.println("GamePeerMessageListener port="+this.listenerPort);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
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
			
			
			//Lo scontro può avvenire con un pianeta (primo ramo dell'IF)
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
										
					this.peer.writeLog(current-gameStartTimestamp, (-1)*res.getQuantity());
					
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
					
					this.peer.writeLog(current-gameStartTimestamp,res.getQuantity());
					
					
					this.peer.defenseMatch(startMatch.getId(), startMatch.getUserName(),startMatch.getSourceSocketAddr(),startMatch.getSourcePort(),peer.getMyId(),res.getQuantity() , threadId,peer.getPlayer().getPosX(),peer.getPlayer().getPosY(),peer.getPlayer().getPosZ());
					
				}
				else
				{
					System.out.println("############GameResource##############à");
					
					long current=System.currentTimeMillis();
					
					this.peer.writeLog(current-gameStartTimestamp, (-1)*objres.getQuantity());
					
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
