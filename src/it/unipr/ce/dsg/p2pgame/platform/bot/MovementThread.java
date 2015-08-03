package it.unipr.ce.dsg.p2pgame.platform.bot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import it.unipr.ce.dsg.p2pgame.platform.Clash.Phase;
import it.unipr.ce.dsg.p2pgame.platform.Clash.Result;


import it.simplexml.message.AckMessage;
import it.simplexml.message.Message;
import it.simplexml.message.MessageReader;
import it.simplexml.sender.MessageSender;
import it.unipr.ce.dsg.p2pgame.GUI.prolog.MovementEngine;
import it.unipr.ce.dsg.p2pgame.GUI.prolog.VisibilityEngine;
import it.unipr.ce.dsg.p2pgame.network.NetPeerInfo;
import it.unipr.ce.dsg.p2pgame.platform.Attack;
import it.unipr.ce.dsg.p2pgame.platform.Clash;
import it.unipr.ce.dsg.p2pgame.platform.GamePeer;
import it.unipr.ce.dsg.p2pgame.platform.GamePlayerResponsible;
import it.unipr.ce.dsg.p2pgame.platform.GameResource;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobile;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobileResponsible;
import it.unipr.ce.dsg.p2pgame.platform.bot.message.PlanetConqueredMessage;
import it.unipr.ce.dsg.p2pgame.platform.message.ClearAttackMatchMessage;
import it.unipr.ce.dsg.p2pgame.platform.message.StartMatchMessage;
import it.unipr.ce.dsg.p2pgame.util.MultiLog;

public class MovementThread implements Runnable{
	
	private Thread runner;
	private String resid;
	private InterfaceBot mybot;
	private double targetx,targety;
	private int period;
	private double xx,yy,zz;
	private double visResource=2;
	private double radiusPlanet=10;
	//FileOutputStream fos;
	//File file;
	//PrintStream ps;
	public MovementThread(InterfaceBot mybot,String resid,double targetx,double targety,int period)
	{   //bisogna aggiungere parametro per area di gioco
		runner=new Thread(this);
		this.resid=resid;
		this.mybot=mybot;
		this.targetx=targetx;
		this.targety=targety;
		this.period=period;
		runner.start();
		/*file=new File("log/"+this.resid+".txt");
		try {
			fos=new FileOutputStream(file,true);
			ps=new PrintStream(fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		
		
	}

	@Override
	public void run() {
		MovementEngine me=new MovementEngine("rules/movementTheory.pl");
		//VisibilityEngine ve=new VisibilityEngine("rules/visibilityTheory.pl");
		GameResourceMobile grm=this.mybot.getResourceMobilebyID(this.resid);
		
		double currentx=grm.getX();
		double currenty=grm.getY();
		
		double previousx=0;
		double previousy=0;
		
		ArrayList<Integer> vis=new ArrayList<Integer>(); // da modificare relativo ad ostacoli per ora non serve
		vis.add(new Integer(0));
		vis.add(new Integer(0));
		vis.add(new Integer(0));
		vis.add(new Integer(0));
		vis.add(new Integer(0));
		vis.add(new Integer(0));
		vis.add(new Integer(0));
		vis.add(new Integer(0));
		
		
		//sto per iniziare lo spostamento
		
		
		//imposto la risorsa mobile in movimento		
		
		grm.setStatus(true); // cambia lo stato della risorsa mobile indicando che e' in movimento
			
		
		int cont=0;
		
		while((currentx!=this.targetx)||(currenty!=this.targety))
		{
			try{
				Thread.sleep(this.period);
				
				me.clearParameters("rules/movementTheory.pl");
				me.createMovementTheory((int)currentx, (int)currenty, (int)this.targetx, (int)this.targety,(int) previousx, (int)previousy, vis);
				
				//System.out.println(grm.getId()+" "+currentx+" "+currenty);
				
				int movx=me.longitudeMovement();
				int movy=me.latitudeMovement();
				
				int movX=0;
				int movY=0;
				
				if(movx==1)//verso ovest
				{
					//currentx--;
					movX=-1;
					previousx=-1;
					
				}
				else if(movx==2) //verso est
				{
					//currentx++;
					movX=1;
					previousx=1;
					
				}
				else if(movx==0)//non mi muovo in orizontale
				{
					movX=0;
					previousx=0;
				
				}
				
				
				if(movy==1)//verso nord
				{
					//currenty--;
					movY=-1;
					previousy=-1;
				}
				else if(movy==2)//verso sud
				{
					//currenty++;
					movY=1;
					previousy=1;
					
				}
				else if(movy==0)//non mi muovo in verticale
				{
					movY=0;
					previousy=0;
					
				}
					
				
				//grm.setX(currentx);
				//grm.setY(currenty);
				this.mybot.getMyGamePeer().moveResourceMobile(grm.getId(), movX, movY, 0, this.mybot.getMyGamePeer().getMyThreadId());
				currentx=grm.getX();
				currenty=grm.getY();
				
			//	System.out.println("\nCurrent position x: "+currentx+" y: "+currenty);
				
				/********************RICERCA DI NEMICI!!!********************************/
				//controllo della visibilita'
				if(cont==10)
				{
					ArrayList<Integer> posx=new ArrayList<Integer>();
		    		ArrayList<Integer> posy=new ArrayList<Integer>();
		    		ArrayList<String> owner=new ArrayList<String>();
		    		ArrayList<String> type=new ArrayList<String>();
		    		
		    		ArrayList<Integer> pattack=new ArrayList<Integer>();
		    		ArrayList<String> restypes=new ArrayList<String>();
		    		
		    		restypes.add("GameResourceMobile");
		    		restypes.add("GameResource");
		    		
		    		pattack.add(new Integer(mybot.getProbattack()));
		    		pattack.add(new Integer(mybot.getProbattack()));
					
					//double xx,yy;
					xx=grm.getX();
					yy=grm.getY();
					zz=grm.getZ();
					
					int x,y;
					x=(int)xx;
					y=(int)yy;
					
					/***
					
					//ottengo il campo visivo della risorsa mobile
					ArrayList<Object> vision=grm.getResourceVision(); 
					
					double v=grm.getVision();
					//creo un arraylist dove salvo la posizione dentro l'array della visibilita' della risorsa mobile
					ArrayList<Integer> array_pos=new ArrayList<Integer>();
					
					for(int z=0;z<vision.size();z++)
					{
						if(vision.get(z) instanceof GamePlayerResponsible)
						{
							GamePlayerResponsible gpr=(GamePlayerResponsible)vision.get(z);
							if(((gpr.getPosX()>=xx-v)&&(gpr.getPosX()<=xx-v))&&((gpr.getPosY()>=yy-v)&&(gpr.getPosY()<=yy-v)))
							{
								int k=(int)gpr.getPosX();
								int j=(int)gpr.getPosY();
								posx.add(new Integer(k));
								posy.add(new Integer(j));
								owner.add("USER"+gpr.getId());
								type.add("GameResource");
								array_pos.add(new Integer(z));
								
							}
						}
						else if(vision.get(z) instanceof GameResourceMobileResponsible)
						{
							
							GameResourceMobileResponsible grmr=(GameResourceMobileResponsible)vision.get(z);
							if(((grmr.getX()>=xx-v)&&(grmr.getX()<=xx-v))&&((grmr.getY()>=yy-v)&&(grmr.getY()<=yy-v)))
							{
								int k=(int)grmr.getX();
								int j=(int)grmr.getY();
								posx.add(new Integer(k));
								posy.add(new Integer(j));
								owner.add("USER"+grmr.getOwnerId());
								type.add("GameResourceMobile");
								array_pos.add(new Integer(z));
							}
						}
						
					}
					
					String id="USER"+mybot.getOwnerid();
					VisibilityEngine ve=new VisibilityEngine("rules/visibilityTheory.pl");
					ve.createVisibilityTheory(posx, posy, owner, type, id, pattack, restypes);
					
					//ottengo la posizione dell'arraylist che corrisponde all'elemento da attacare
					int pos=ve.attack(); 
					
					if(pos!=0) // se c'e' qualcuno da attaccare 
				       {  
							//DEVO OTTENERE L'ID DEL THREAD DEL RESPONSABILE 
							//recupero l'object
							int posres=array_pos.get(pos-1);
						
							Object res=vision.get(posres);
						
							if(res instanceof GamePlayerResponsible)
							{
								//ho trovato la base di un giocatore
							
								String threadID=new Long(Thread.currentThread().getId()).toString();
								GamePlayerResponsible player=(GamePlayerResponsible)res;
								
								//ricavo id e port number del nemico
								UserInfo info=this.mybot.getLoggedUserInfo(player.getId());
								if(info==null)
								{
									this.mybot.UpdateLoggedUsers();
									info=this.mybot.getLoggedUserInfo(player.getId());
								}
								
								
								this.mybot.getMyGamePeer().startMatch(player.getId(),player.getName(),info.getIp(),info.getPort(), player.getId(),grm.getId() ,grm.getQuantity(), threadID,player.getPosX(),player.getPosY(),player.getPosZ());
								//attendere esito dello scontro
								ArrayList<Result> results=this.mybot.getMyGamePeer().getClashes().get(player.getId()).getResults();
								int sz=results.size();
								Result result=results.get(sz-1);
								if(result==Result.WIN)
								{
									System.out.println("Ho vinto");
									
								}
								else
								{
									System.out.println("Ho perso");
									this.mybot.getMyGamePeer().removeToMyResources(grm);
									
								}
								break;
								
							}
							else if(res instanceof GameResourceMobileResponsible)
							{
								String threadID=new Long(Thread.currentThread().getId()).toString();//devo cambiare questa linea
								GameResourceMobileResponsible res_grm=(GameResourceMobileResponsible)res;
								
								//ricavo id e port number del nemico
								UserInfo info=this.mybot.getLoggedUserInfo(res_grm.getOwnerId());
								if(info==null)
								{
									this.mybot.UpdateLoggedUsers();
									info=this.mybot.getLoggedUserInfo(res_grm.getOwnerId());
								}
								 
								this.mybot.getMyGamePeer().startMatch(res_grm.getOwnerId(),res_grm.getOwner(),info.getIp(),info.getPort(), res_grm.getId(),grm.getId() ,grm.getQuantity(), threadID,res_grm.getX(),res_grm.getY(),res_grm.getZ());
								
								do{
								//attendo la fine dello scontro
									Thread.sleep(100);	
								}while(this.mybot.getMyGamePeer().getClashes().get(res_grm.getOwnerId()).getStatusLast()!=Phase.END);
								//quando lo scontro finisco controllo il suo esito
								ArrayList<Result> results=this.mybot.getMyGamePeer().getClashes().get(res_grm.getOwnerId()).getResults();
								int sz=results.size();
								Result result=results.get(sz-1);
								if(result==Result.WIN)
								{
									System.out.println("Ho vinto");
									
								}
								else
								{
									System.out.println("Ho perso");
									this.mybot.getMyGamePeer().removeToMyResources(grm);
									break;
								}
								
							}
						
						
						
						
						
						
						
						
						//decision
				       	}
					
					
					
				    /********/
					
					
					//*PIANETI: RICERCA DI PIANETI**//
					//verifica pianeti
					
					//ottengo lista di pianeti
					
					ArrayList<VirtualResource> planets=this.mybot.getPlanets();
					//System.out.println("£££planets "+planets.size());
					
					/*******
					for(int k=0;k<planets.size();k++)
					{
						
						VirtualResource planet=planets.get(k); // per ogni pianeta, verifico se le sue coordinate sono dentro una finestra visiva della mia risorsa mobile
						//System.out.println("Coordinate: x= "+planet.getX()+ " y= "+planet.getY());
						//System.out.println("My coordinates x = "+x+ "y = "+y);
						if((x>=(planet.getX()-(this.visResource+this.radiusPlanet))) &&(x<=(planet.getX()+(this.visResource+this.radiusPlanet)))&&(y>=(planet.getY()-(this.visResource+this.radiusPlanet)))&&(y<(planet.getY()+(this.visResource+this.radiusPlanet))))
						{
							System.out.println("PIANETA");
							System.out.println("Coordinate: x= "+planet.getX()+ " y= "+planet.getY());
							//ps.println("PIANETA");
							//ps.println("Coordinate: x= "+planet.getX()+ " y= "+planet.getY());
							
							if(planet.getOwnerID().equals("null")) // se il pianeta non è stato conquistato da qualcuno
							{
																
								if(this.mybot.createResource(planet.getId())) //se ho abbastanza soldi per creare una diffesa
								{ //conquisto il pianeta
									System.out.println("CONQUISTO IL PIANETA "+planet.getId());
									this.mybot.setPlanetOwner(planet.getId(), this.mybot.getOwnerid(),this.mybot.getMyGamePeer().getPlayer().getName()); //lo conquisto
									
									//invio un messaggio in broadcast a tutti peer nel gioco
									this.mybot.UpdateLoggedUsers();
									HashMap<String,UserInfo> userslist=this.mybot.getLoggedUsers();
									Set<String> key_set=userslist.keySet();
									Iterator<String> iterator=key_set.iterator();
									System.out.println("USERS "+userslist.size());
									
									while(iterator.hasNext())
									{		
											String iduser=iterator.next();
											UserInfo info=userslist.get(iduser);	
											
										    String user_id=info.getId();
										    String userip=info.getIp();
										    int userport=info.getPort();
										
											
											if(!this.mybot.getMyGamePeer().getMyId().equals(user_id))											
											{
												System.out.println("INVIO MESSAGGIO A "+user_id);	
												PlanetConqueredMessage message=new PlanetConqueredMessage(this.mybot.getMyGamePeer().getMyId(),
												this.mybot.getMyGamePeer().getMyPeer().getIpAddress(),
												(this.mybot.getMyGamePeer().getMyPeer().getPortNumber()+7),this.mybot.getOwnerid(),this.mybot.getMyGamePeer().getPlayer().getName(),planet.getId());
												
												System.out.println("Invio messaggio a "+userip+" , "+(userport+7));
												String responseMessage=MessageSender.sendMessage(userip,(userport+7),message.generateXmlMessageString());
												
												MultiLog.println(GamePeer.class.toString(), "Verify response...");
												
												if(responseMessage.contains("ERROR"))
												{
													
													MultiLog.println(GamePeer.class.toString(), "Sending Message ERROR!");
													
												}
												else
												{
													//ack message
													MessageReader responseStartMessageReader = new MessageReader();
													Message receivedStartMessageReader = responseStartMessageReader.readMessageFromString(responseMessage.trim());

													AckMessage ackMessage = new AckMessage(receivedStartMessageReader);
													if (ackMessage.getAckStatus() == 0){
														MultiLog.println(GamePeer.class.toString(), "Message received");
														//System.out.println("Now Match is started");
													}
												}
											  }
											  
											 
											  
											
											
										}
										
										

								}
								
													
								
							}
							else if(!planet.getOwnerID().equals(this.mybot.getOwnerid()))
							{
								//gestione di clash
								//un pianeta deve avere una risorsa associata. Al conquistare un pianeta devo acquistare una risorsa che difende questo pianeta. Da fare
								//quando trovo un pianeta nemicom inizio uno scontro con questa risorsa
								//ricavo id e port number del nemico
								UserInfo info=this.mybot.getLoggedUserInfo(planet.getOwnerID());
								if(info==null)
								{
									this.mybot.UpdateLoggedUsers();
									info=this.mybot.getLoggedUserInfo(planet.getOwnerID());
								}
								
								String threadId=new Long(Thread.currentThread().getId()).toString();
								this.mybot.getMyGamePeer().startMatch(planet.getOwnerID(), planet.getOwnerName(),info.getIp(),info.getPort(), planet.getOwnerID(),grm.getId(),grm.getQuantity() , threadId, planet.getX(), planet.getY(), planet.getZ());
								// se vinco conquisto il pianeta
								// se perdo perdo la mia risorsa
								do{
									//attendo la fine dello scontro
										Thread.sleep(100);	
									}while(this.mybot.getMyGamePeer().getClashes().get(planet.getOwnerID()).getStatusLast()!=Phase.END);
									//quando lo scontro finisco controllo il suo esito
									ArrayList<Result> results=this.mybot.getMyGamePeer().getClashes().get(planet.getOwnerID()).getResults();
									int sz=results.size();
									Result result=results.get(sz-1);
									if(result==Result.WIN)
									{
										System.out.println("Ho vinto");
										
										this.mybot.setPlanetOwner(planet.getId(), "null", "null");//prima di conquistare il pianeta cancello
																								  //il proprietario precedente
										
										if(this.mybot.createResource(planet.getId()))
										{
											System.out.println("CONQUISTO IL PIANETA "+planet.getId());
											this.mybot.setPlanetOwner(planet.getId(), this.mybot.getOwnerid(),this.mybot.getMyGamePeer().getPlayer().getName()); //lo conquisto
											
											//ora devo comunicarlo a gli altri giocatori
											this.mybot.UpdateLoggedUsers();
											HashMap<String,UserInfo> userslist=this.mybot.getLoggedUsers();
											
											Set<String> key_set=userslist.keySet();
											Iterator<String> iterator=key_set.iterator();
											
											while(iterator.hasNext())
											{
													
													String iduser=iterator.next();
													UserInfo info2=userslist.get(iduser);	
												    String user_id=info2.getId();
												    String userip=info2.getIp();
												    int userport=info2.getPort();
												
													
													if(!this.mybot.getMyGamePeer().getMyId().equals(user_id))											
													{
														PlanetConqueredMessage message=new PlanetConqueredMessage(this.mybot.getMyGamePeer().getMyId(),
														this.mybot.getMyGamePeer().getMyPeer().getIpAddress(),
														this.mybot.getMyGamePeer().getMyPeer().getPortNumber(),this.mybot.getOwnerid(),this.mybot.getMyGamePeer().getPlayer().getName(),planet.getId());
														
														String responseMessage=MessageSender.sendMessage(userip,userport,message.generateXmlMessageString());
														
														MultiLog.println(GamePeer.class.toString(), "Verify response...");
														
														if(responseMessage.contains("ERROR"))
														{
															
															MultiLog.println(GamePeer.class.toString(), "Sending Message ERROR!");
															
														}
														else
														{
															//ack message
															MessageReader responseStartMessageReader = new MessageReader();
															Message receivedStartMessageReader = responseStartMessageReader.readMessageFromString(responseMessage.trim());

															AckMessage ackMessage = new AckMessage(receivedStartMessageReader);
															if (ackMessage.getAckStatus() == 0){
																MultiLog.println(GamePeer.class.toString(), "Message received");
																//System.out.println("Now Match is started");
															}
														}
													  }
													  
													 
													  
													
													
												}

										}
										
									}
									else
									{
										System.out.println("Ho perso");
										this.mybot.getMyGamePeer().removeToMyResources(grm);
										
									}
									break;
								
								
							}
							// se il pianeta è mio non faccio niente
						
						
						
					   }
					
					}
					/**********/
					/*******
					
											
					for(int k=x-2;k<=x+2;k++)
					{
						for(int j=y-2;j<=y+2;j++)
						{
							if((k!=x)||(j!=y))
							{
								posx.add(new Integer(k));
								posy.add(new Integer(j));
								
								//devo fare un metodo per ottenere un nemico nelle coordinate
								VirtualResource aux=mybot.getVResourcebyCoordinates(k, j);
								
								
								if(aux!=null)
								{
									owner.add(aux.getOwnerID());
									type.add(aux.getResType());
									System.out.println("NEMICO POS "+k+" , "+ j);
								}
								else
								{
									owner.add("null");
									type.add("null");
									
								}
							}
						}
						
					}
					
					//qua faccio l'interrogazione sulla visibilita'
					String id=mybot.getOwnerid();
					id="PLAYER"+id;
					//System.out.println("MOVEMENT THREAD ID "+id);
					VisibilityEngine ve=new VisibilityEngine("rules/visibilityTheory.pl");
					ve.createVisibilityTheory(posx, posy, owner, type, id, pattack, restypes);
					
					//ottengo la posizione dell'arraylist che corrisponde all'elemento da attacare
					int pos=ve.attack();
					if(pos!=0)
				       {  System.out.println("***********"+this.resid+"**************");
						  System.out.println("***********ATTACCO!!!**************");
						  System.out.println("***********Nemico Trovato!!!**************");
						  System.out.println("Pos "+pos);
				    	  int ax=ve.posX();
				          int ay=ve.posY();

				          System.out.println("attack: ("+ax+" , "+ay+")");
				          
				          
				          
				          grm.setStatus(false);
				          break;
				       }
				       else
				       {  //otherwise, verifing if i have to defend against a possible attack and the X,Y coordinates
				    	  
				          pos=ve.defense();
				          
				          if(pos!=0)
				          {
				        	  System.out.println("***********"+this.resid+"**************");
				        	  System.out.println("***********DIFFESA!!!**************"); 
					    	  System.out.println("***********Nemico Trovato!!!**************");
					    	  System.out.println("Pos "+pos);
				              int dx=ve.posDX();
				              int dy=ve.posDY();
				              System.out.println("defense: ("+dx+" , "+dy+")");
				              
				              
				              
				        	  ps.println("***********DIFFESA!!!**************"); 
				        	  ps.println("***********Nemico Trovato!!!**************");
				        	  ps.println("Pos "+pos);				              
				        	  ps.println("defense: ("+dx+" , "+dy+")\n");
				        	  
				        	  
				        	  grm.setStatus(false);
				        	  break;
				          }
				          


				       }
					
					 /***/
					
					cont=0;
				}
				cont++;

				
			}catch(InterruptedException e){
				
			}
			
		}
		
		//this.mybot.setMovStatus(resid, false); //non mi muovo più
		grm.setStatus(false);
		
		System.out.println("arrived "+resid+": x=" +targetx+" y= "+targety);
	}
	
	
	
}
