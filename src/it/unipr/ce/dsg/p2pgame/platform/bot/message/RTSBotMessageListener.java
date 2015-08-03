package it.unipr.ce.dsg.p2pgame.platform.bot.message;

import it.simplexml.message.AckMessage;
import it.simplexml.message.Message;
import it.simplexml.message.MessageReader;
import it.unipr.ce.dsg.p2pgame.platform.bot.InterfaceBot;
import it.unipr.ce.dsg.p2pgame.platform.bot.RTSGameBot;
import it.unipr.ce.dsg.p2pgame.platform.bot.RTSGameBot2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RTSBotMessageListener implements Runnable{
	
	private ServerSocket server;
	private int port;
	private String listenerId;
	private String listenerAddr;
	
	private RTSGameBot2 mybot;
	
	public RTSBotMessageListener(InterfaceBot bot,String id,String address, int port)
	{
		this.mybot=(RTSGameBot2)bot;
		this.port=port;
		this.listenerId=id;
		this.listenerAddr=address;
		
		
		System.out.println("ip "+address+" , "+port);
	}

	@Override
	public void run() {
		Socket clientSocket = null;
		
		try {
			if (this.server == null)
	                	this.server = new ServerSocket(this.port);

	    } catch (IOException e) {
			e.printStackTrace();
	    }
	    
	    System.out.println("-----------------RTSBotMessageListener-----------------");
	    while(true)
	    {
	    	
	    	try {
				clientSocket = server.accept();
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
                    os.close();
                    clientSocket.close();
                    break;
                }
		        
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	    	
	    	
	    }
	            
	
	}
	
	 public void checkIncomingMessage(String messageString, DataOutputStream os) throws IOException {
		 
		 MessageReader messageReader = new MessageReader();
		 Message receivedMessage = messageReader.readMessageFromString(messageString.trim());
		 
		 if(receivedMessage.getMessageType().equals("PLANETCONQUERED"))
	        {
	            this.planetConqueredAction(receivedMessage, os);
	        }
		 
		 if(receivedMessage.getMessageType().equals("BOTSTARTMATCHREQUEST"))
		 {
			 this.BotStartMatchRequestAction(receivedMessage, os);
			 
		 }
		 
	 }
	 
	 private void planetConqueredAction(Message receivedMessage, DataOutputStream os) throws IOException
	 {
		 PlanetConqueredMessage planetmessage=new PlanetConqueredMessage(receivedMessage);
		 
		 String playerID=planetmessage.getIdPlayer();
		 String planetID=planetmessage.getIdPlanet();
		 String playerName=planetmessage.getNamePlayer();
		 
		 this.mybot.setPlanetOwner(planetID, playerID, playerName);
		 System.out.println("GIOCATORE "+playerID+" HA CONQUISTATO "+planetID);
		 
			os.write((new AckMessage(this.listenerId, this.listenerAddr, this.port, 0, "")).generateXmlMessageString().getBytes());
		 
		 
	 }
	 
	 private void BotStartMatchRequestAction(Message receivedMessage, DataOutputStream os) throws IOException
	 {
		 BotStartMatchRequestMessage message=new BotStartMatchRequestMessage(receivedMessage);
		 System.out.println("###############RTSBotMessageListener-->BotStartMatchRequestAction###########################");
		 //arriva la richiesta
		 //se sono già in clash
		 if(this.mybot.inClash)
		 {
			 
			 
			 
			 
			 
			 System.out.println("###############SONO IN MATCH###########################");
			 long timeStamp=Long.parseLong(message.getTimeStamp());
			 BotStartMatchResponseMessage response=new BotStartMatchResponseMessage(this.mybot.getOwnerid(),message.getOtherresId(),message.getResId(),Long.toString(this.mybot.timeStamp),true,false);
			 
			 os.write(response.generateXmlMessageString().getBytes());
			 
			
				 /*if(this.mybot.peerId.equals(message.getId())) // se ho iniziato uno scontro con lui
				 {
					 if(this.mybot.resId.equals(message.getResId()))
					 {
						 System.out.println("###############DECISIONE DI CHI INIZIA LO SCONTRO###########################");
						 	//invio il messaggio
						 	BotStartMatchResponseMessage response=new BotStartMatchResponseMessage(this.mybot.getOwnerid(),message.getOtherresId(),message.getResId(),Long.toString(this.mybot.timeStamp),true,true);
							 
							os.write(response.generateXmlMessageString().getBytes());
							
							
						 
					 }
					 else //if(!(this.mybot.resId.equals(message.getResId()))
					 {
						 //decido con quale risorse iniziare uso lo stesso algoritmo che per decidere chi inizia lo scontro
						 //ma invio l'id dell'altra risorsa
						 
							 System.out.println("###############RICHIESTE DI SCONTRO DA DUE RISORSE DIVERSE: DECISIONE###########################");
							 long diff=Math.abs(this.mybot.timeStamp-timeStamp);
								
								//devo comparare i due id
								int compare=this.mybot.resId.compareTo(message.getResId());
								
								//se il mio id e maggiore e diff e pari inizio lo scontro se e' minore ed e' dispari inizio lo scontro
								//altrimenti lo inizia l'altro
								if((compare>0 && diff%2==0)||(compare<0 && diff%2!=0))
								{
									//inizio io con la risorsa che ho in pancia
									System.out.println("###############RISORSA"+this.mybot.resId+"###########################");
									 BotStartMatchResponseMessage response=new BotStartMatchResponseMessage(this.mybot.getOwnerid(),message.getOtherresId(),message.getResId(),message.getTimeStamp(),true,false);
									 
									 os.write(response.generateXmlMessageString().getBytes());
								}
								else
								{
									//inizia lui
									System.out.println("###############RISORSA"+message.getResId()+"###########################");
									BotStartMatchResponseMessage response=new BotStartMatchResponseMessage(this.mybot.getOwnerid(),message.getOtherresId(),message.getResId(),message.getTimeStamp(),false,true);
									 
									 os.write(response.generateXmlMessageString().getBytes());
									 
									 
									
								}	
							 
						 
						 
						 
						 
					 }
					 
				 }
				 else //if(!this.mybot.peerId.equals(message.getId()))
				 {
					 
					 System.out.println("###############MATCH CON ALTRO PEER###########################");
					 //se ho gia iniziato uno scontro con un altro do precedenza allo scontro
					 //quindi sono in match
					 BotStartMatchResponseMessage response=new BotStartMatchResponseMessage(this.mybot.peerId,this.mybot.resId,message.getResId(),message.getTimeStamp(),true,false);
					 
					 os.write(response.generateXmlMessageString().getBytes());
					 
				 }*/
				 
			 
			
			
				 
			 
		 }
		 else
		 {
			 this.mybot.inClash=true;
			 System.out.println("###############NON SONO IN MATCH###########################");
			 
			 //metto id della risorsa nemica e del peer nemico  e controllo 
			 this.mybot.resId=message.getResId();
			 this.mybot.peerId=message.getId();
			 this.mybot.timeStamp=System.currentTimeMillis();
			 
			 //devo inviare la risposta e dire che non sono in clash
			 BotStartMatchResponseMessage response=new BotStartMatchResponseMessage(this.mybot.getOwnerid(),message.getOtherresId(),message.getResId(),message.getTimeStamp(),false,true);
			 
			 os.write(response.generateXmlMessageString().getBytes());
			 
			 //meccanismo per aspettare esito scontro...
			 //sleep per evitare inizio simultaneo di un altro scontro
			 
			 try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.mybot.inClash=false;
		 }
	 }

}
