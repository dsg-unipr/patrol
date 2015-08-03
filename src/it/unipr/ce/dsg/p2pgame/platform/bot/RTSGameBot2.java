package it.unipr.ce.dsg.p2pgame.platform.bot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


import it.simplexml.message.AckMessage;
import it.simplexml.message.Message;
import it.simplexml.message.MessageReader;
import it.unipr.ce.dsg.p2pgame.GUI.message.content.Point;
import it.unipr.ce.dsg.p2pgame.GUI.prolog.BuyResourceEngine;
import it.unipr.ce.dsg.p2pgame.GUI.prolog.ExtractionEngine;
import it.unipr.ce.dsg.p2pgame.GUI.prolog.GameEngine;
import it.unipr.ce.dsg.p2pgame.GUI.prolog.GameEvolutionEngine;
import it.unipr.ce.dsg.p2pgame.GUI.prolog.MovementEngine;
import it.unipr.ce.dsg.p2pgame.GUI.prolog.VisibilityEngine;
import it.unipr.ce.dsg.p2pgame.GUI.prolog.util.Resource;
import it.unipr.ce.dsg.p2pgame.platform.GamePeer;
import it.unipr.ce.dsg.p2pgame.platform.GamePlayerResponsible;
import it.unipr.ce.dsg.p2pgame.platform.GameResource;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceEvolve;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobile;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobileResponsible;
import it.unipr.ce.dsg.p2pgame.platform.bot.message.*;
import it.unipr.ce.dsg.p2pgame.util.MultiLog;

public class RTSGameBot2 implements Runnable,InterfaceBot{
	
	
	private BuyResourceEngine bre;
	private ExtractionEngine ee;
	private MovementEngine me;
	private VisibilityEngine ve;
	private GameEvolutionEngine gee;
	private String owner="owner";
	private String ownerid="ownerID";
	private double L;
	private int nrmobile;
	private int nres;
	private int portMin;
	private String usr;
	private ArrayList<Object> res;
	private HashMap<String, Boolean> status;
	private ArrayList<VirtualResource> enemies;
	private String profile;
	private String configuration;
	private double resmincost;
	private int period_movement;
	private int period_loop;
	private int prob_buy_mobile_resource;
	private int probattack;
	private int probdefense;
	private double visResource=5;
	private double radiusPlanet=10;
	
	private MessageSender sender;
	
	//attrbuti per la configurazione, mi serviranno dopo???
	
	//minX, maxX, minY, maxY, minZ, maxZ, vel, vis, gran
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private double minZ;
	private double maxZ;
	private double vel;
	private double vis;
	private double gran;
	private int portReq;
	//atruttura dati che rappresenta i pianeti nello spazio
	
	ArrayList<VirtualResource> planets;
	HashMap<String,UserInfo> loggedusers=null;
	
	
	
	//aggiungere oggetto della classe MessageSender
	//MessageSender request;
	GamePeer gp;
	
	//flag scontri
	
	public boolean inClash=false;
	public String resId;
	public String peerId; 
	public long timeStamp;
	
	//file log match
	 FileOutputStream pfile;// = new FileOutputStream("log/.txt");
    PrintStream Output;// = new PrintStream(file);
    
    long time1,time2,time3;
    int countplanets;
    
    
    //flag de 
    private boolean gameband;

	
	public RTSGameBot2(String profile,String conf,int portmin,String usr,int portReq)
	{
		
		gee=new GameEvolutionEngine("rules/evolutionTheory.pl");
		ee=new ExtractionEngine("rules/extractionTheory.pl");
		ve=new VisibilityEngine("rules/visibilityTheory.pl");
		res=new ArrayList<Object>();
		status=new HashMap<String, Boolean>();
		L=1000;
		enemies=new ArrayList<VirtualResource>();
		this.profile=profile;
		this.configuration=conf;
		this.usr=usr;
		
		this.resmincost=0;
       
		planets=new ArrayList<VirtualResource>();
		
		this.nres=0;
		this.nrmobile=0;
		this.portMin=portmin;
		
		
		this.sender=new MessageSender(portReq);
		this.portReq=portReq;
		
		this.countplanets=0;
	}

	@Override
	public void run() {
		
		//////////////////////////INIZIALIZZAZIONE///////////////////////////////////
		
		//inizialmente imposto i parametri delle teorie
		
		//imposto altri parametri e imposto il profilo del bot
		/*
		 * required_exploration_money: 10000
		   required_exploration_resources: [GameResource,GameResourceMobile]
		   required_exploration_qresources: [2,5]

		   required_conquest_money: 20000
		   required_conquest_resources: [GameResource,GameResourceMobile]
		   required_conquest_qresources: [3,10]

		 * */
		
		//GamePeer
		//int portMin = 6891;
		int portMin = this.portMin;
    	int serverPort = 1235;
    	String serverAdd="127.0.0.1";
    	//String serverAdd="160.78.28.72";
    	//String username="user1";
    	String username=this.usr;
    	String password="password";
    	
    	
    	try {
    		//qua apro il file di configurazione e creo lo scenario
        	File fconf=new File(this.configuration);
			FileInputStream fisconf=new FileInputStream(fconf);
			InputStreamReader isrconf=new InputStreamReader(fisconf);
	       	BufferedReader brconf=new BufferedReader(isrconf);
	      //minX, maxX, minY, maxY, minZ, maxZ, vel, vis, gran
	        String straux=brconf.readLine();
	        this.minX=Double.parseDouble(straux);
	        straux=brconf.readLine();
	        this.maxX=Double.parseDouble(straux);
	        straux=brconf.readLine();
	        this.minY=Double.parseDouble(straux);
	        straux=brconf.readLine();
	        this.maxY=Double.parseDouble(straux);
	        straux=brconf.readLine();
	        this.minZ=Double.parseDouble(straux);
	        straux=brconf.readLine();
	        this.maxZ=Double.parseDouble(straux);
	        straux=brconf.readLine();
	        this.vel=Double.parseDouble(straux);
	        straux=brconf.readLine();
	        this.vis=Double.parseDouble(straux);
	        straux=brconf.readLine();
	        this.gran=Double.parseDouble(straux);
	        
	        
	        //lunghezza dello spazio la considero la differenza tra le coordinate massima e minima di x
	        this.L=this.maxX-this.minX;
	        
	        //costo minimo di acquisto di una risorsa
	        straux=brconf.readLine();
	        this.resmincost=Double.parseDouble(straux);
	        //spazio
	        
	        straux=brconf.readLine();
	        int nplanets=Integer.parseInt(straux);
	        //pianeti
	        for(int i=0;i<nplanets;i++)
	        {
	        	straux=brconf.readLine();	        	
	        	String [] str_cord=straux.split(",");
	        	VirtualResource planet=new VirtualResource();
	        	planet.setOwnerID("null");
	        	planet.setOwnerName("null");
	        	planet.setId("planet"+i);
	        	planet.setResType("planet");
	        	planet.setX(Double.parseDouble(str_cord[0]));
	        	planet.setY(Double.parseDouble(str_cord[1]));
	        	planet.setZ(Double.parseDouble(str_cord[2]));
	        	this.planets.add(planet);
	        	
	        }
	        System.out.println("numero pianeti "+this.planets.size());
	        //this.createEnememies();
	        /******
	        for(int i=0;i<this.planets.size();i++)
	        {
	        	VirtualResource planet=planets.get(i);
	        	System.out.println("planet "+planet.getId()+" "+planet.getX()+" , "+planet.getY());
	        	
	        }
	        /******/	
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    	//L � pari alla lunghezza massima fratto due
		
		
		//this.gp = new GamePeer(portMin+ 1 , portMin, 160, "", serverAdd, serverPort, portMin + 3, portMin + 2, serverAdd, serverPort+2, 4000,1000,64000,2000);
		//System.out.println("1");
		sender.CreateGamePeer(portMin+ 1 , portMin, 160, "", serverAdd, serverPort, portMin + 3, portMin + 2, serverAdd, serverPort+2, 4000,500,64000,2000);
		
		//request.CreateGamePeer(portMin+ 1 , portMin, 160, "", serverAddressTextField.getText().trim(), serverPort, portMin + 3, portMin + 2, serverAddressTextField.getText().trim(), serverPort+2, 4000,1000,64000,2000);

		
		//this.gp.registerOnServer(username, password);
		
		sender.registerOnServer(username, password);
		//0,575,0,575,0,0, 1,10, 5
		//minX, maxX, minY, maxY, minZ, maxZ, vel, vis, gran
		//startgame
		//this.gp.startGame(minX, maxX, minY, maxY, minZ, maxZ, vel, vis, gran);
		System.out.println("maxX = " + maxX + ", maxY = " + maxY);
		sender.startGame(minX, maxX, minY, maxY, minZ, maxZ, vel, vis, gran);
		
		
		//this.owner=gp.getMyId();
		//this.ownerid=gp.getMyId();
		this.owner=sender.getGamePeerId();
		this.ownerid=sender.getGamePeerId();
		
		//file log match
		 try {
			pfile= new FileOutputStream("log/planet"+ownerid +".txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	     Output= new PrintStream(pfile);
		//thread d'ascolto
		//Thread botListener=new Thread(new RTSBotMessageListener(this,this.ownerid,this.gp.getMyPeer().getIpAddress(),(this.gp.getMyPeer().getPortNumber()+7)));
		Thread botListener=new Thread(new RTSBotMessageListener(this,this.ownerid,sender.getIpAddress(),((this.portMin+1)+7)));
		botListener.start();
		//System.out.println("STARTGAME POSX "+this.gp.getPlayer().getPosX()+" POSY "+this.gp.getPlayer().getPosY());
		System.out.println("STARTGAME POSX "+this.sender.getGamePlayerPosition().getX()+" POSY "+this.sender.getGamePlayerPosition().getY());
		System.out.println("GAMEPEER ID "+this.ownerid);
		//apro il file di profilo
		//carico profilo del giocatore
		
		File f=new File(profile);
       	FileInputStream fis;
       	ArrayList<String> req_exp_res=new ArrayList<String>();
       	ArrayList<Integer> req_exp_qres=new ArrayList<Integer>();
       	ArrayList<String> req_conq_res=new ArrayList<String>();
       	ArrayList<Integer> req_conq_qres=new ArrayList<Integer>();
       	double req_conq_money=0;
       	double req_exp_money=0;
       	
       	
		try {
			fis = new FileInputStream(f);
			InputStreamReader isr=new InputStreamReader(fis);
	       	BufferedReader br=new BufferedReader(isr);
	    	
	       	//ExtractionTheory
			String str=br.readLine();
			int per=Integer.parseInt(str);
			str=br.readLine();
			int val=Integer.parseInt(str);
			str=br.readLine();
			boolean isinf=Boolean.parseBoolean(str);
			str=br.readLine();
			double current=Double.parseDouble(str);
			
			//create the extraction theory
			ee.createExtractTheory(per,val,isinf,(int)current);
			
			//EvolutionTheory
			str=br.readLine();			
			req_exp_money=Double.parseDouble(str);
			System.out.println("req_exp_money " +req_exp_money);
			
			str=br.readLine();
			String [] array_str=str.split(",");
			System.out.println("req_exp_res "+str);
			
			for(int i=0;i<array_str.length;i++)
			{
				req_exp_res.add(array_str[i]);
				
			}					
			
			str=br.readLine();
			System.out.println("req_exp_qres"+str);
			array_str=str.split(",");
			
			for(int i=0;i<array_str.length;i++)
			{
				int int_aux=Integer.parseInt(array_str[i]);
				req_exp_qres.add(new Integer(int_aux));
			}
			
			
			str=br.readLine();
			req_conq_money=Double.parseDouble(str);
			System.out.println("req_conq_money"+ req_conq_money);
			str=br.readLine();
			
			System.out.println("req_conq_res "+str);
			array_str=str.split(",");			
			
			for(int i=0;i<array_str.length;i++)
			{
				req_conq_res.add(array_str[i]);
				
			}
			
			str=br.readLine();
			
			System.out.println("req_conq_qres "+str);
			array_str=str.split(",");		
			
			for(int i=0;i<array_str.length;i++)
			{
				int int_aux=Integer.parseInt(array_str[i]);
				req_conq_qres.add(new Integer(int_aux));
			}
			
			str=br.readLine();
			this.period_loop=Integer.parseInt(str);
			//period movement
			 str=br.readLine();
			 this.period_movement=Integer.parseInt(str);
			 
			 //probabilta' di comprare risorsa mobile
			 str=br.readLine();
			 this.prob_buy_mobile_resource=Integer.parseInt(str);
						
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double currentmoney=0;//money.getQuantity();
		this.nres=0;
		this.nrmobile=0;
		//creo l'oggetto ResourceEvolve
		double offset=(double)this.ee.getAccValue();
		final long period=(long)this.ee.getPeriod();
		boolean isinf=this.ee.isInfinite();
		//double quantity=(double)this.ee.getCurrentResource();
		
		GameResourceEvolve revolve;
		double money_in_deposit=0;
		if(ee.isInfinite()) // se e' infinito lasciamo che il thread di resource evolve accumuli risorse
		{
			//revolve=new GameResourceEvolve("moneyEvolveble", "Money", 0, period, offset);
			this.sender.addResourceEvolve("moneyEvolveble", "Money", 0, period, offset);
		}
		else //altrimenti l'accumulo lo gestiamo noi
		{
			//revolve=new GameResourceEvolve("moneyEvolveble", "Money", 0, period, 0);
			this.sender.addResourceEvolve("moneyEvolveble", "Money", 0, period, 0);
			money_in_deposit=(double)this.ee.getCurrentResource();
		}
		
		
		//this.gp.addToMyResource(revolve);
		
		
		//////
		ArrayList<Resource> buyresources = new ArrayList<Resource>();
		Resource r1 = new Resource();
        r1.setName("GameResource");
        r1.setCost((int)this.resmincost);
        buyresources.add(r1);
        
        
        Resource r2 = new Resource();
        r2.setName("GameResourceMobile");
        r2.setCost((int)this.resmincost);
		buyresources.add(r2);
		
		
	
		//////////////////////////////////////CICLO INFINITO///////////////////////////////////////
		//poi entro nel ciclo infinito
		int c=0;
		int periodbase=this.period_loop;
		
		this.gameband=true;
		while(gameband)
		{
			try {
				Thread.sleep(this.period_loop);
				c++;
				System.out.println("@@@@@@@@@@@@@@@@@@@ ciclo "+c+"@@@@@@@@@@@@@@@@");
				
				
				
				if(!ee.isInfinite())
				{
					if(!ee.stopExtraction((int)money_in_deposit))
					{
						this.incrementMoney(offset);
						money_in_deposit-=offset;
					}
					
					
				}
				
				ArrayList<String> currentres=new ArrayList<String>();
				
				currentres.add("\"GameResource\"");
				currentres.add("\"GameResourceMobile\"");
				
				ArrayList<Integer> currentqres=new ArrayList<Integer>(); 
				currentqres.add(new Integer(this.nres));
				currentqres.add(new Integer(this.nrmobile));
				
				
				//currentmoney=this.gp.getMyResourceFromId("moneyEvolveble").getQuantity();
				//System.out.println("1");
				currentmoney=this.sender.getMyResourceFromId("moneyEvolveble").getQuantity();
				
				gee=new GameEvolutionEngine("rules/evolutionTheory.pl");
				
				gee.createGameEvolutionTheory(currentmoney, currentres, currentqres, req_exp_money, req_exp_res, req_exp_qres, req_conq_money, req_conq_res, req_conq_qres);
				
								
				// con queste informazioni interrogo al engine in quale fase del gioco mi trovo, e ricavo le prob di attacco e il raggio
				//di spostamento
				int fase=gee.getFase();
				
				System.out.println("&&&&&&&&&& FASE "+fase+" @@@@@@@@@@@");
				
				//GameResourceEvolve evolve=(GameResourceEvolve)gp.getMyResourceFromId("moneyEvolveble");
				double val=currentmoney;//evolve.getQuantity();
				
				System.out.println("Soldi: "+val);
				
				double rad=gee.getRadius();
				
				rad*=L;
				System.out.println("rad: "+rad);
				
				probattack=gee.getProbAttack();
				
				System.out.println("prob attack: "+probattack);
				
				probdefense=gee.getProbDefense();
				
				System.out.println("prob defense: "+probdefense);
				
				//con una probilita' x decido se acquistare o meno una nuova risorsa
				//Questo controllo lo aggiungo in una nuova teoria, associata al profilo di giocatore (Fatto)
				
				int probbuy=gee.getProbBuy();
				
				System.out.println("prob buy: "+probbuy);
				
				//con una probabilita' y decido se spostare o no una risorsa mobile. Questa
				//operazione la effettuo per ogni risorsa mobile che possiedo
				
				int probmove=gee.getProbMovement();
				System.out.println("prob move: "+probmove);
				
				
				//decido se comprare una nuova risorsa
				int randombuy=(int)(Math.random()*100);
				
				this.UpdateLoggedUsers();
				
				System.out.println("probabilit� di acquistare una nuova risorsa: "+randombuy + "%");
				if(randombuy<probbuy)
				{
					
					int random_res=(int)(Math.random()*100);
					System.out.println("random "+random_res);
										
					if(random_res<=this.prob_buy_mobile_resource)	////modifica, aggiungere queste probabilita' al profilo del giocatore
					{
						
						this.bre=new BuyResourceEngine("rules/buyResourceTheory.pl");					
						double qt=this.getCurrentMoney();					
						bre.createBuyResourceTheory(buyresources, "GameResourceMobile", (int)qt, currentres, currentqres);
						
						if(bre.buyResource())
						{
							int multiplicity=(int) (qt/this.resmincost);
							if(multiplicity>0)
							{
								qt=multiplicity*this.resmincost; //uso la stessa variabile
								
								//compro risorsa mobile
								String timestamp = Long.toString(System.currentTimeMillis());
								this.nrmobile++;						
								
								
								//this.gp.createMobileResource("Attack" + timestamp, qt);
								//System.out.println("2");
								this.sender.createMobileResource("Attack" + timestamp, qt);
								//lo status per default è false
								
								
								//status.put("m"+timestamp, new Boolean(false)); // devo salvare anche l'ID
								//currentmoney-=resmobcost;
								this.decrementMoney(qt);
								//this.sender.publishResourceMobile();
								System.out.println("###################nuova risorsa mobile##################");
								
								
							}
						}
						else
						{
							System.out.println("NON HO ABBASTANZA SOLDI");
							
						}
												
						
						
					}
					else
					{
						//compro risorsa di difesa
												
						
						this.bre=new BuyResourceEngine("rules/buyResourceTheory.pl");					
						double qt=this.getCurrentMoney();					
						bre.createBuyResourceTheory(buyresources, "GameResource", (int)qt, currentres, currentqres);
						
						if(bre.buyResource())
						{
							int multiplicity=(int) (qt/this.resmincost);
							if(multiplicity>0)
							{
								qt=multiplicity*this.resmincost; //uso la stessa variabile
								String timestamp = Long.toString(System.currentTimeMillis());
								this.nres++;
								//res.add(new GameResource("id"+this.nres,"defense",1.0));
								
								//GameResource dif = new GameResource("def" + timestamp, "Defense" + timestamp, qt);
								
				                //this.gp.addToMyResource(dif);
				                this.sender.addResource("def" + timestamp, "Defense" + timestamp, qt);
				                
								//currentmoney-=1000;
								this.decrementMoney(qt);
								//this.sender.publishResourceMobile();
								System.out.println("###########nuova risorsa di difesa####################");
								
								
							}
						}
						else
						{
							System.out.println("NON HO ABBASTANZA SOLDI");
							
						}
						
						
					}
					
					
				}
				
				//stampo elenco di risorse
				//this.printResources();
				
				
				//decido se spostare ogni risorsa mobile e determino la destinazione
				
				//ArrayList<Object> res=this.gp.getMyResources();
				//System.out.println("3");
				ArrayList<Object> resaux=this.sender.getResources();
				
				//if(resaux!=null)
				//{
					this.res=resaux;
					
				//}
				
				int sr=0;//res.size();
				//if(this.res==null)
				//{
					sr=res.size();
				//}
				//else
				//{
					//sr=0;
					
				//}
				
				
				//modifico periodo di ciclo
				this.period_loop=periodbase+2000*(sr/10); //aumenta di 2.5 secondi per ogni 10 nuove risorse
				
				
				for(int i=0;i<sr;i++)
				{
					
					if( res.get(i) instanceof GameResourceMobile){
						
						
						
						GameResourceMobile grm=(GameResourceMobile)res.get(i);
						String id=grm.getId();
						//metodo per ottenere lo status
						//System.out.println("1");
						boolean st=sender.getResourceMobileStatus(id);//grm.getStatus();
						//String id=grm.getDescription();
						
						if(!st) // se non � in movimento
						{
														
							// qua devo mettere il tread di spostamento e indicare che la risorsa e' in movimento
							//prima verifico se la risors è attualmente in movimento
							int aux=(int)(Math.random()*100);
							System.out.println("probabilit� di spostare risorsa "+id+" : "+aux+" %");
						    if(aux<probmove)
							{
								//se la risorsa non è attualmente in movimento
								//se decido di spostare una risorsa, devo scegliere in modo aleatorio il punto d'arrivo
								// il punto d'arrivo dipende del raggio massimo di spostamento scelto dalla fase del gioco
								
										
								boolean mband=false;
								double mx=0;
								double my=0;
								while(!mband) //cicla fino a trovare delle coordinate dentro lo scenario del gioco
								{
									mx=(double)(Math.random()*rad);	
									my=(double)(Math.random()*rad);
									
									int mquad=(int)(Math.random()*3+ 1);
									
									// se e' il primo quadrante non faccio niente
									if(mquad==2){
										mx*=-1;
										//y rimane uguale
									}else if(mquad==3){
										mx*=-1;
										my*=-1;
										
									}else{
										my*=-1;
										//x rimane uguale
									}
									//System.out.println("4");
									Point position=this.sender.getGamePlayerPosition();
									mx=mx+position.getX();//this.gp.getPlayer().getPosX();
									my=my+position.getY();//this.gp.getPlayer().getPosY();
									
									if((mx>=this.minX&&mx<=this.maxX)&&(my>=this.minY&&my<=this.maxY))
									{
										mband=true;
									}
							
								}
								
								int tx=(int)mx;
								int ty=(int)my;
								id=grm.getId();
								System.out.println("move "+id+": x=" +tx+" y= "+ty);
								
									//MovementThread move=new MovementThread(this,id,tx,ty,period_movement);
									MovementThread2 move=new MovementThread2(this,id,tx,ty,period_movement,this.portReq);
									Thread movThread=new Thread(move);
									movThread.start();
								
							}
							
						}
						else
						{
							//System.out.println("Resource "+id+" in movimento");
						}
						
					}
						
					
					
					
				}
				//res=this.sender.getResources();
                resaux=this.sender.getResources();
				
				//if(resaux!=null)
				//{
					this.res=resaux;
					
				//}
				
				this.printMyPlanets();
				
				verifyVisibility();
				
				this.verifySpace();
				
				//this.printResources();
				
				
				//pubblico le mie risorse mobili
				
				//this.sender.publishResourceMobile();
				
				//da verificare
				
				
				//devo calcolare quanti pianeti sono stati conquistati da ogni giocatore
				//aggiorno elenco dei giocatori
				//this.UpdateLoggedUsers();
				
				//dopo aver aggiornato l'elenco dei giocatori ottengo il loro numero
				
				System.out.println("##########################STATO DEL GIOCO#############################");
				//ottengo un arrayList di tutti i giocatori loggati
				ArrayList<String> players=new ArrayList<String>();
				
				Set<String> key_set=loggedusers.keySet();
				Iterator<String> iterator=key_set.iterator();
				
				while(iterator.hasNext())
				{
					String iduser=iterator.next();
					
					UserInfo info=loggedusers.get(iduser);
					//devo tener conto che  tuprolog ha problemi con le stringhe, non posso iniziare da un numero, quindi:
					System.out.println("Player "+info.getId());
					players.add("player"+info.getId()); //poi recupero la stringa originale
					
				}
				
				//conto quanti pianeti ha conquistato ogni giocatore
				ArrayList<Integer> numberplanets=new ArrayList<Integer>();
				
				for(int i=0;i<players.size();i++)
				{
					int count=0;
					String playerid=players.get(i).substring(6);
					for(int j=0;j<planets.size();j++)
					{
						VirtualResource planet=planets.get(j);
						
						
						if(playerid.equals(planet.getOwnerID()))
						{
							count++;
						}
					}
					System.out.println("PLAYER "+playerid+" NUM PLANETS= "+count);
					numberplanets.add(new Integer(count));
					
				}

				 GameEngine gameengine=new GameEngine("rules/gameTheory.pl");
				 int nplanets=this.planets.size();
				 System.out.println("TOTAL PLANETS = "+nplanets);
				 
				 gameengine.createGameTheory(players, numberplanets, nplanets);
				//verifico se il goal del gioco e' stato ragiunto!!!
				//se e' cosi' determino il vincitore e finisce il gioco 
				 
				 if(gameengine.gameover())//se il gioco e' finito perche' qualcuno ha raggiunto l'obiettivo
				 {
					 System.out.println("#####################GIOCO FINITO#############################");
					 String winner=gameengine.getGameWinner(); //ricavo l'id del vincitore
					 
					 //devo togliere la parola PLAYER
					 String idwinner=winner.substring(6);
					 
					 //System.out.println("Gioco finito");
					 //if(gp.getMyId().equals(idwinner)) //se sono io il vincitore ....
				     if(this.sender.getGamePeerId().equals(idwinner)) 
					 {
						 System.out.println(" HO VINTO");
						 
						 this.gameband=false;
					 }
					 else
					 {
						 System.out.println("HA VINTO IL GIOCATORE "+idwinner);
						 this.gameband=false;
					 }
					 
					 //meccanismo per uscire del gioco e della rete
				 }
				 
				 //ora devo verificare se ho ancora delle risorse, se non ho più nessuna risorsa vuol dire che sono stato annientato e quindi devo usicre del gioco
				 //faccio una teori per verificarlo in casi piu' complessi????
				 int nresources=this.sender.getResourcesSize();//this.gp.getMyResources().size();
				 
				 if(nresources==0)
				 {
					 System.out.println("SONO STATO ANNIENTATO, HO PERSO");
					 //meccanismo di uscita del gioco
					 this.gameband=false;
				 }
				
				//da  fare
		
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	public void setMovStatus(String id,boolean newstatus)
	{
		this.status.put(id,new Boolean(newstatus));
		
	}
	
	public boolean getMovStatus(String id)
	{
		Boolean st=(Boolean)this.status.get(id);
		return st.booleanValue();
	}
	
	public GameResourceMobile getResourceMobilebyID(String id)
	{
		System.out.println("getResByID");
		GameResourceMobile grm=this.sender.getMobileResource(id);//this.gp.getMyMobileResourceFromId(id);
		return grm;
	}
	
	
	public void printResources()
	{
		
		System.out.println("printRes");
		//ArrayList<Object> res=this.sender.getResources();//this.gp.getMyResources();
		System.out.println("Resource:");
		for(int i=0;i<res.size();i++)
		{
			Object aux=res.get(i);
			
			if((aux instanceof GameResource)&&!(aux instanceof GameResourceMobile))
			{
				if(!(aux instanceof GameResourceEvolve))
				{
					GameResource gr=(GameResource)aux;
					System.out.println("Resource: "+gr.getId());
										
				}
				
			}
			
		}
		
		System.out.println("\n\nResourceMobile:");
		for(int i=0;i<res.size();i++)
		{
			Object aux=res.get(i);
			
			if(aux instanceof GameResourceMobile)
			{
				GameResource gr=(GameResourceMobile)aux;
				System.out.println("Resource: "+gr.getId());
				
			}
			
		}
		
		
	}
	
	public VirtualResource getVResourcebyCoordinates(int x,int y)
	{
		int l=this.enemies.size();
		VirtualResource res=null;
		VirtualResource aux=null;
		for(int i=0;i<l;i++)
		{
			aux=this.enemies.get(i);
			
			if((aux.getX()==x) &&(aux.getY()==y))
			{
				res=aux;
				System.out.println("NEMICO TROVATO POS "+x +" , "+ y );
				break;
			}
			
		}
		
		return res;
		
		
	}
	
	private void incrementMoney(double inc)
	{
		
		//GameResourceEvolve evolve=(GameResourceEvolve)gp.getMyResourceFromId("moneyEvolveble");
		System.out.println("incmoney");
		GameResourceEvolve evolve=(GameResourceEvolve)this.sender.getMyResourceFromId("moneyEvolveble");
		double val=evolve.getQuantity();
			
		double qt=val+inc;
		//evolve.setQuantity(qt);
		//gp.getMyResourceFromId("moneyEvolveble").setQuantity(qt);
		this.sender.UpdateResourceEvolve(qt);
		
	}
	
	private void decrementMoney(double dec)
	{
		
		//GameResourceEvolve evolve=(GameResourceEvolve)gp.getMyResourceFromId("moneyEvolveble");
		
		System.out.println("decmoney");
		GameResourceEvolve evolve=(GameResourceEvolve)this.sender.getMyResourceFromId("moneyEvolveble");
		double val=evolve.getQuantity();
		
		double qt=val-dec;
		//evolve.setQuantity(qt);
		//gp.getMyResourceFromId("moneyEvolveble").setQuantity(qt);
		this.sender.UpdateResourceEvolve(qt);
	}
	
	public double getCurrentMoney()
	{
		//GameResourceEvolve evolve=(GameResourceEvolve)gp.getMyResourceFromId("moneyEvolveble");
		GameResourceEvolve evolve=(GameResourceEvolve)this.sender.getMyResourceFromId("moneyEvolveble");
		double qt=evolve.getQuantity();
		
		return qt;
	}

	


	@Override
	public GamePeer getMyGamePeer() {
		return this.gp;
	}
	
	public ArrayList<VirtualResource> getEnemies() {
		return enemies;
	}

	public void setEnemies(ArrayList<VirtualResource> enemies) {
		this.enemies = enemies;
	}

	
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(String ownerid) {
		this.ownerid = ownerid;
	}

	public int getProbattack() {
		return probattack;
	}

	public void setProbattack(int probattack) {
		this.probattack = probattack;
	}

	public int getProbdefense() {
		return probdefense;
	}

	public void setProbdefense(int probdefense) {
		this.probdefense = probdefense;
	}
	
	public ArrayList<VirtualResource> getPlanets()
	{
		return this.planets;
		
	}
	
	public void setPlanetOwner(String idPlanet,String idOwner,String nameOwner)
	{
		/*VirtualResource planet;
		planet=this.getPlanetbyID(idPlanet);
		if(planet!=null)
		{
			planet.setOwnerID(idOwner);
			planet.setOwnerName(nameOwner);
		}
		else
		{
			
			System.out.println(idPlanet+" non esiste");
		}
		*/
		
		this.getPlanetbyID(idPlanet).setOwnerID(idOwner);
		this.getPlanetbyID(idPlanet).setOwnerName(nameOwner);
		
		
	}
	
	public VirtualResource getPlanetbyID(String idPlanet)
	{
		
		for(int i=0;i<this.planets.size();i++)
		{
			VirtualResource planet=this.planets.get(i);
			if(planet.getId().equals(idPlanet))
			return planet;
		}
		
		return null;
	}
	
	
	public void createEnememies()
	{
		
		int maxx=(int)this.maxX;
		int maxy=(int)this.maxY;
		
		int gran=(int)this.gran;
		
		int x=gran;
		int y=gran;
		
		while(x<maxx)
		{
			y=gran;
			while(y<maxy)
			{
				VirtualResource enemy=new VirtualResource();
				
				enemy.setX(x);
				enemy.setY(y);
				enemy.setZ(0);
				
				enemy.setId("enemyID");
				enemy.setOwnerID("enemyID");
				enemy.setResType("GameResourceMobile");
				
				this.enemies.add(enemy);
				y+=gran;
			}
			
			x+=gran;
		}
		
	}
	
	public boolean createResource(String idRes)
	{
		//compro risorsa di difesa
		double qt=this.getCurrentMoney();
		int multiplicity=(int) (qt/this.resmincost);
		if(multiplicity>0)
		{
			qt=multiplicity*this.resmincost; //uso la stessa variabile
			String timestamp = Long.toString(System.currentTimeMillis());
			this.nres++;
			//res.add(new GameResource("id"+this.nres,"defense",1.0));
			
			//GameResource dif = new GameResource(idRes, "Defense" + timestamp, resmincost);
            //this.gp.addToMyResource(dif);
            this.sender.addResource(idRes, "Defense" + timestamp, resmincost);
			//currentmoney-=1000;
			this.decrementMoney(qt);
			System.out.println("###########nuova risorsa di difesa####################");
			
			return true;
			
		}
		else
		{
			System.out.println("NON HO SOLDI PER COMPRARE DIFFESE");
			return false;
		}
		
	}
	
	public GameResource getLastGameResource()
	{
		GameResource res=null;
		ArrayList<Object> resources=this.res;//this.sender.getResources();//this.gp.getMyResources();
		
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
		
		
		return res;
	}
	
	public void UpdateLoggedUsers()
	{
		this.loggedusers=new HashMap<String,UserInfo>();
		ArrayList<String> usersList=this.sender.getLoggedUsersList();//this.getMyGamePeer().getLoggedUsersList();
		if(!usersList.isEmpty())// se ci sono degli utenti nella lista invio i messaggi
		{
			System.out.println("NUMBER OF USERS: "+usersList.size());
			
			for(int u=0;u<usersList.size();u++)
			{
				String str_user=usersList.get(u);
				
				System.out.println("USERS: "+str_user);
				String[] array_user=str_user.split(",");
				String userid=array_user[0]; // mi serve ???
				String userip=array_user[1];
				String userport=array_user[2];
				
				UserInfo info=new UserInfo(userid,userip,Integer.parseInt(userport));
				loggedusers.put(userid,info);
				
			}
			
		}
	}
	
	public HashMap<String,UserInfo> getLoggedUsers()
	{
		return this.loggedusers;
	}
	
	public UserInfo getLoggedUserInfo(String id)
	{
		return this.loggedusers.get(id);
		
	}
	
	public void printMyPlanets()
	{
		System.out.println("PIANETI CONQUISTATI");
		
		for(int i=0;i<this.planets.size();i++)
		{
			VirtualResource planet=this.planets.get(i);
			
			if(planet.getOwnerID().equals(this.ownerid))
			{
				System.out.println(planet.getId());		
			}
			
		}
		
	}
	
	public void verifySpace()
	{
		ArrayList<Object> res=this.sender.getResources();//this.gp.getMyResources();
		System.out.println("######################verify planets############################");
		for(int i=0;i<res.size();i++)
		{
			if(res.get(i) instanceof GameResourceMobile)
			{
				GameResourceMobile grm=(GameResourceMobile)res.get(i);
				this.verifyPlanets(grm);
				
			}
			
		}
		System.out.println("######################verify enemies############################");
		for(int i=0;i<res.size();i++)
		{
			if(res.get(i) instanceof GameResourceMobile)
			{
				GameResourceMobile grm=(GameResourceMobile)res.get(i);
				
				this.verifyEnemies(grm);
			}
			
		}
	}

	public void verifyVisibility()
	{
		//ArrayList<Object> res=this.sender.getResources();//this.gp.getMyResources();
		
		for(int i=0;i<res.size();i++)
		{
			if(res.get(i) instanceof GameResourceMobile)
			{
				GameResourceMobile grm=(GameResourceMobile)res.get(i);
				//System.out.println("RM"+grm.getId()+" X="+grm.getX()+" Y="+grm.getY());
				//controllo visibilitˆ del grm
				ArrayList<Object> vis=grm.getResourceVision();
				
				for(int j=0;j<vis.size();j++)
				{
					if(vis.get(j) instanceof GamePlayerResponsible)
					{
						
						GamePlayerResponsible gpr=(GamePlayerResponsible)vis.get(j);
						
						System.out.println(j+".  BASE di "+gpr.getId()+" X="+gpr.getPosX()+" Y="+gpr.getPosY());
							
						
						
					}
					if(vis.get(j) instanceof GameResourceMobileResponsible)
					{
						GameResourceMobileResponsible grmr=(GameResourceMobileResponsible)vis.get(j);
						
						
						//System.out.println(j+". RISORSA MOBILE "+grmr.getId()+" di "+grmr.getOwnerId()+" X="+grmr.getX()+" Y="+grmr.getY());
							
					}
					else
					{
					//	System.out.println(j+". NULL");
						
					}
				  }
					
				}
				
				
				
			}
			
			
		}

	@Override
	public void setResourceStatus(String id, boolean status) {
		
		//GameResourceMobile grm=this.gp.getMyMobileResourceFromId(id);
		
		//grm.setStatus(status);
		
		
		this.sender.setMobileReourceStatus(id, status);
	}

	@Override
	public  void moveResourceMobile(String resid, int movX, int movY, int movZ) {
		// TODO Auto-generated method stub
		try {
			this.gp.moveResourceMobile(resid, movX, movY, movZ, this.gp.getMyThreadId());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void verifyPlanets(GameResourceMobile grm)
	{
		//*PIANETI: RICERCA DI PIANETI**//
		//verifica pianeti
		
		//ottengo lista di pianeti
		
		double x=grm.getX();
		double y=grm.getY();
		ArrayList<VirtualResource> planets=this.getPlanets();
		//System.out.println("£££planets "+planets.size());
		
		
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
													
					if(this.createResource(planet.getId())) //se ho abbastanza soldi per creare una diffesa
					{ //conquisto il pianeta
						System.out.println("CONQUISTO IL PIANETA "+planet.getId());
						this.setPlanetOwner(planet.getId(), this.getOwnerid(),this.owner); //lo conquisto
						
						long timestamp=System.currentTimeMillis();
						
						this.writeLog(timestamp);
						
						//invio un messaggio in broadcast a tutti peer nel gioco
						//this.UpdateLoggedUsers();
						HashMap<String,UserInfo> userslist=this.getLoggedUsers();
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
							
								
								if(!this.ownerid.equals(user_id))											
								{
									System.out.println("INVIO MESSAGGIO A "+user_id);	
									PlanetConqueredMessage message=new PlanetConqueredMessage(this.ownerid,
									this.sender.getIpAddress(),
									((this.portMin)+7),this.ownerid,this.owner,planet.getId());
									
									System.out.println("Invio messaggio a "+userip+" , "+((userport+1)+7));
									String responseMessage=it.simplexml.sender.MessageSender.sendMessage(userip,((userport)+7),message.generateXmlMessageString());
									
									//System.out.println(GamePeer.class.toString()+ "Verify response...");
									
									if(responseMessage.contains("ERROR"))
									{
										
										System.out.println(" Sending Message ERROR!");
										
									}
									else
									{
										//ack message
										MessageReader responseStartMessageReader = new MessageReader();
										Message receivedStartMessageReader = responseStartMessageReader.readMessageFromString(responseMessage.trim());

										AckMessage ackMessage = new AckMessage(receivedStartMessageReader);
										if (ackMessage.getAckStatus() == 0){
											System.out.println(" Message received");
											//System.out.println("Now Match is started");
										}
									}
								  }
								  
								 
								  
								
								
							}
							
							

					}
					
										
					
				}
				else if(!planet.getOwnerID().equals(this.ownerid))
				{
					//gestione di clash
					//un pianeta deve avere una risorsa associata. Al conquistare un pianeta devo acquistare una risorsa che difende questo pianeta. Da fare
					//quando trovo un pianeta nemicom inizio uno scontro con questa risorsa
					//ricavo id e port number del nemico
					UserInfo info=this.getLoggedUserInfo(planet.getOwnerID());
					if(info==null)
					{
						this.UpdateLoggedUsers();
						info=this.getLoggedUserInfo(planet.getOwnerID());
					}
					
					//String threadId=new Long(Thread.currentThread().getId()).toString();
					
					
					if(!this.inClash)
					{
						System.out.println("###########SCONTRO#############");
						
						this.inClash=true;
						this.resId=planet.getId();
						this.peerId=planet.getOwnerID();
						
						//invio messaggio
						long currentTime=System.currentTimeMillis();
						this.timeStamp=currentTime;
						 System.out.println("#######################RTSGameBot--->richiesta di scontro#############################");
						Message message=new BotStartMatchRequestMessage(this.ownerid,grm.getId(),planet.getId(),Long.toString(currentTime));
						
						 String responseMessage=it.simplexml.sender.MessageSender.sendMessage(info.getIp(),(info.getPort()+7),message.generateXmlMessageString());
						
						 MessageReader messageReader=new MessageReader();

					     Message receivedMessage = messageReader.readMessageFromString(responseMessage.trim());

					     if(receivedMessage.getMessageType().equals("BOTSTARTMATCH"))
						 {
								System.out.println("##########RTSGameBot-->Risposta############ ");
								BotStartMatchResponseMessage response=new BotStartMatchResponseMessage(receivedMessage);
								
								boolean isInMatch=response.getInMatch();
								boolean decision=response.getDecision();
								
								
								if(isInMatch)
								{
									/*if(decision)
									{	//System.out.println("#############Scontro--->Decisione################");
										//calcolo della differenza di timeStamp
										long otherTimeStamp=Long.parseLong(response.getTimeStamp());
										
										long diff=Math.abs(currentTime-otherTimeStamp);
										
										//devo comparare i due id
										int compare=this.ownerid.compareTo(player.getId());
										
										//se il mio id e maggiore e diff e pari inizio lo scontro se e' minore ed e' dispari inizio lo scontro
										//altrimenti lo inizia l'altro
										if((compare>0 && diff%2==0)||(compare<0 && diff%2!=0))
										{   System.out.println("#############Scontro--->Decisione:INIZIO IO################");
											String result=sender.startMatch(player.getId(), player.getName(),info.getIp(),info.getPort(), player.getId(),grm.getId(),grm.getQuantity() , player.getPosX(), player.getPosY(), player.getPosZ());
											//attendere esito dello scontro
											if(result.equals("win"))
											{
												System.out.println("Ho vinto");
												
											}
											else if(result.equals("lose"))
											{
												System.out.println("Ho perso");
												
												
											}
											
										 }
										else
										{*/
											System.out.println("#############Scontro--->Decisione:INIZIA ALTRO PEER################");
											 try {
													Thread.sleep(500);
												} catch (InterruptedException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												
											
										//}
										
									//}
									
								}
								else //!inMatch
								{	System.out.println("#############Scontro--->POSSO INIZIARE IO################");
									//if(decision)
									//{
										
										
									//}
								
								System.out.println(planet.getOwnerID());
								String result=sender.startMatch(planet.getOwnerID(), planet.getOwnerName(),info.getIp(),info.getPort(), planet.getId(),grm.getId(),grm.getQuantity() , planet.getX(), planet.getY(), planet.getZ());
								// se vinco conquisto il pianeta
								// se perdo perdo la mia risorsa
								
								//if(result)
								System.out.println("TROVATO PIANETA NEMICO");
								if(result.equals("win"))//if(result)
								{
									//ho vinto conquisto pianeta
									this.setPlanetOwner(planet.getId(), "null", "null");//prima di conquistare il pianeta cancello
									  //il proprietario precedente
									
									if(this.createResource(planet.getId()))
									{
										System.out.println("CONQUISTO IL PIANETA "+planet.getId());
										this.setPlanetOwner(planet.getId(), this.getOwnerid(),this.owner); //lo conquisto
										
										long timestamp=System.currentTimeMillis();
										this.writeLog(timestamp);
										
										//		ora devo comunicarlo a gli altri giocatori
										this.UpdateLoggedUsers();
										HashMap<String,UserInfo> userslist=this.getLoggedUsers();

										Set<String> key_set=userslist.keySet();
										Iterator<String> iterator=key_set.iterator();

										while(iterator.hasNext())
										{

											String iduser=iterator.next();
											UserInfo info2=userslist.get(iduser);	
											String user_id=info2.getId();
											String userip=info2.getIp();
											int userport=info2.getPort();


											if(!this.ownerid.equals(user_id))											
											{
												System.out.println("Invio messaggio a "+user_id);
												PlanetConqueredMessage msg=new PlanetConqueredMessage(this.ownerid,
														this.sender.getIpAddress(),
														((this.portMin+1)+7),this.ownerid,this.owner,planet.getId());
																				
												String responseMess=it.simplexml.sender.MessageSender.sendMessage(userip,(userport+7),msg.generateXmlMessageString());
																				
												
												if(responseMess.contains("ERROR"))
												{
													
													System.out.println(GamePeer.class.toString()+ "Sending Message ERROR!");
													
												}	
												else
												{
													//	ack message
													MessageReader responseStartMessageReader = new MessageReader();
													Message receivedStartMessageReader = responseStartMessageReader.readMessageFromString(responseMess.trim());

													AckMessage ackMessage = new AckMessage(receivedStartMessageReader);
													if (ackMessage.getAckStatus() == 0){
														System.out.println("Messaggio ricevuto da "+user_id);
														//	System.out.println("Now Match is started");
													}
												}
											}	





										}

									}

									
								}
								else
								{
									//ho perso elimino risorsa
									//e' stata eliminata la risorsa 
									
								}

									
									
								}
								
								
						 }
					     else if(receivedMessage.getMessageType().equals("ERROR"))
					     {
					    	 
					    	 System.out.println(GamePeer.class.toString()+ "Sending Message ERROR!");
					     }
						
					     this.inClash=false;
					     
					     
					}
					else
					{
						System.out.println("############Sono in uno Scontro#############");
						
					}
						
										
										


					
				}
				// se il pianeta è mio non faccio niente
			
			
			
		   }
		
		}
		/**********/

		
		
	}
		
	
	public void writeLog(long timestamp)
	{
		if(this.countplanets==0)
		{
			this.countplanets++;
			time1=timestamp;
		}
		else if(this.countplanets==1)
		{
			this.countplanets++;
			time2=timestamp;
		}
		else if(this.countplanets==2)
		{
			this.countplanets=0;
			time3=timestamp;
			
			String strlog=time1+";"+time2+";"+time3;
			Output.println(strlog);
		}
		
	}
	
	
	public void verifyEnemies(GameResourceMobile grm)
	{
		double xx,yy,zz;
		
		xx=grm.getX();
		yy=grm.getY();
		zz=grm.getZ();
		
		int x,y;
		x=(int)xx;
		y=(int)yy;
		
		ArrayList<Integer> posx=new ArrayList<Integer>();
		ArrayList<Integer> posy=new ArrayList<Integer>();
		ArrayList<String> owner=new ArrayList<String>();
		ArrayList<String> type=new ArrayList<String>();
		
		ArrayList<Integer> pattack=new ArrayList<Integer>();
		ArrayList<String> restypes=new ArrayList<String>();
		
		restypes.add("GameResourceMobile");
		restypes.add("GameResource");
		
		pattack.add(new Integer(this.getProbattack()));
		pattack.add(new Integer(this.getProbattack()));
		
		//System.out.println("RM "+grm.getId()+" X="+grm.getX()+" Y="+grm.getY());
		//controllo visibilità del grm
		ArrayList<Object> vision=grm.getResourceVision();
		
		double v=grm.getVision()+10;
		//creo un arraylist dove salvo la posizione dentro l'array della visibilita' della risorsa mobile
		ArrayList<Integer> array_pos=new ArrayList<Integer>();
		
		for(int z=0;z<vision.size();z++)
		{
			if(vision.get(z) instanceof GamePlayerResponsible)
			{
				GamePlayerResponsible gpr=(GamePlayerResponsible)vision.get(z);
				//if(((gpr.getPosX()>=xx-v)&&(gpr.getPosX()<=xx-v))&&((gpr.getPosY()>=yy-v)&&(gpr.getPosY()<=yy-v)))
				//{
					int k=(int)gpr.getPosX();
					int j=(int)gpr.getPosY();
					posx.add(new Integer(k));
					posy.add(new Integer(j));
					owner.add("u"+gpr.getId());
					type.add("GameResource");
					array_pos.add(new Integer(z));
					if(!this.ownerid.equals(gpr.getId()))
					{
						System.out.println("@@@@@@@@@@@@@@@@@@@@base nemica@@@@@@@@@@@@@@@@@@@");
						
					}
				//}
			}
			else if(vision.get(z) instanceof GameResourceMobileResponsible)
			{
				
				GameResourceMobileResponsible grmr=(GameResourceMobileResponsible)vision.get(z);
				//if(((grmr.getX()>=xx-v)&&(grmr.getX()<=xx-v))&&((grmr.getY()>=yy-v)&&(grmr.getY()<=yy-v)))
				//{
					int k=(int)grmr.getX();
					int j=(int)grmr.getY();
					posx.add(new Integer(k));
					posy.add(new Integer(j));
					owner.add("u"+grmr.getOwnerId());
					type.add("GameResourceMobile");
					array_pos.add(new Integer(z));
					if(!this.ownerid.equals(grmr.getOwnerId()))
					{
						System.out.println("@@@@@@@@@@@@@@@@@@@@nave nemica@@@@@@@@@@@@@@@@@@@");
						
					}
				//}
			}
			
		}
		
		String myid="u"+this.ownerid;
		VisibilityEngine ve=new VisibilityEngine("rules/visibilityTheory.pl");
		ve.createVisibilityTheory(posx, posy, owner, type, myid, pattack, restypes);
		
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
				
					//String threadID=new Long(Thread.currentThread().getId()).toString();
					GamePlayerResponsible player=(GamePlayerResponsible)res;
					
					//ricavo id e port number del nemico
					UserInfo info=this.getLoggedUserInfo(player.getId());
					if(info==null)
					{
						this.UpdateLoggedUsers();
						info=this.getLoggedUserInfo(player.getId());
					}
					System.out.println("#################TROVATA BASE NEMICA+"+player.getId()+"+ #################");
					
					//inizio match
					if(!this.inClash) // se sono attualmente in uno scontro, ignoro questo scontro
					{
						//faccio lo scontro, altrimento lo ignoro
						this.inClash=true;
						this.resId=player.getId();
						this.peerId=player.getId();
						
						//invio messaggio
						long currentTime=System.currentTimeMillis();
						this.timeStamp=currentTime;
						 System.out.println("#######################RTSGameBot--->richiesta di scontro#############################");
						Message message=new BotStartMatchRequestMessage(this.ownerid,grm.getId(),player.getId(),Long.toString(currentTime));
						
						 String responseMessage=it.simplexml.sender.MessageSender.sendMessage(info.getIp(),(info.getPort()+7),message.generateXmlMessageString());
						
						 MessageReader messageReader=new MessageReader();

					     Message receivedMessage = messageReader.readMessageFromString(responseMessage.trim());
					    
					     if(receivedMessage.getMessageType().equals("BOTSTARTMATCH"))
						 {
								System.out.println("##########RTSGameBot-->Risposta############ ");
								BotStartMatchResponseMessage response=new BotStartMatchResponseMessage(receivedMessage);
								
								boolean isInMatch=response.getInMatch();
								boolean decision=response.getDecision();
								
								
								if(isInMatch)
								{
									/*if(decision)
									{	//System.out.println("#############Scontro--->Decisione################");
										//calcolo della differenza di timeStamp
										long otherTimeStamp=Long.parseLong(response.getTimeStamp());
										
										long diff=Math.abs(currentTime-otherTimeStamp);
										
										//devo comparare i due id
										int compare=this.ownerid.compareTo(player.getId());
										
										//se il mio id e maggiore e diff e pari inizio lo scontro se e' minore ed e' dispari inizio lo scontro
										//altrimenti lo inizia l'altro
										if((compare>0 && diff%2==0)||(compare<0 && diff%2!=0))
										{   System.out.println("#############Scontro--->Decisione:INIZIO IO################");
											String result=sender.startMatch(player.getId(), player.getName(),info.getIp(),info.getPort(), player.getId(),grm.getId(),grm.getQuantity() , player.getPosX(), player.getPosY(), player.getPosZ());
											//attendere esito dello scontro
											if(result.equals("win"))
											{
												System.out.println("Ho vinto");
												
											}
											else if(result.equals("lose"))
											{
												System.out.println("Ho perso");
												
												
											}
											
										 }
										else
										{*/
											System.out.println("#############Scontro--->Decisione:INIZIA ALTRO PEER################");
											 try {
													Thread.sleep(500);
												} catch (InterruptedException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												
											
										//}
										
									//}
									
								}
								else //!inMatch
								{	System.out.println("#############Scontro--->POSSO INIZIARE IO################");
									//if(decision)
									//{
										String result=sender.startMatch(player.getId(), player.getName(),info.getIp(),info.getPort(), player.getId(),grm.getId(),grm.getQuantity() , player.getPosX(), player.getPosY(), player.getPosZ());
										//attendere esito dello scontro
										if(result.equals("win"))
										{
											System.out.println("Ho vinto");
											
										}
										else if(result.equals("lose"))
										{
											System.out.println("Ho perso");
											
											
										}
										
									//}
									
									
								}
								
								
						 }
					     else if(receivedMessage.getMessageType().equals("ERROR"))
					     {
					    	 
					    	 System.out.println(GamePeer.class.toString()+ "Sending Message ERROR!");
					     }
						
					     this.inClash=false;
						
					}
					else
					{
						System.out.println("##########SONO IN CLASH#################");
						
					}
					
					
					
					
				}
				else if(res instanceof GameResourceMobileResponsible)
				{
					//String threadID=new Long(Thread.currentThread().getId()).toString();//devo cambiare questa linea
					GameResourceMobileResponsible res_grm=(GameResourceMobileResponsible)res;
					
					//ricavo id e port number del nemico
					UserInfo info=this.getLoggedUserInfo(res_grm.getOwnerId());
					if(info==null)
					{
						this.UpdateLoggedUsers();
						info=this.getLoggedUserInfo(res_grm.getOwnerId());
					}
					
					System.out.println("###################### TROVATA RISORSA NEMICA +"+res_grm.getOwnerId()+" +  #################");
					//inizio match
					if(!this.inClash) // se sono attualmente in uno scontro, ignoro questo scontro
					{
						//faccio lo scontro, altrimento lo ignoro
					
						this.inClash=true;
						
						//invio messaggio
						long currentTime=System.currentTimeMillis();
																
						Message message=new BotStartMatchRequestMessage(this.ownerid,grm.getId(),res_grm.getId(),Long.toString(currentTime));
						
						 String responseMessage=it.simplexml.sender.MessageSender.sendMessage(info.getIp(),(info.getPort()+7),message.generateXmlMessageString());
						
						 MessageReader messageReader=new MessageReader();

					     Message receivedMessage = messageReader.readMessageFromString(responseMessage.trim());
					     
					     if(receivedMessage.getMessageType().equals("BOTSTARTMATCH"))
						 {
								
					    	 System.out.println("##########RTSGameBot-->Risposta############ ");
								BotStartMatchResponseMessage response=new BotStartMatchResponseMessage(receivedMessage);
								
								boolean isInMatch=response.getInMatch();
								//boolean decision=response.getDecision();
								
								
								if(isInMatch)
								{
									/*if(decision)
									{	//System.out.println("#############Scontro--->Decisione################");
										//calcolo della differenza di timeStamp
										long otherTimeStamp=Long.parseLong(response.getTimeStamp());
										
										long diff=Math.abs(currentTime-otherTimeStamp);
										
										//devo comparare i due id
										int compare=this.ownerid.compareTo(res_grm.getOwnerId());
										
										//se il mio id e maggiore e diff e pari inizio lo scontro se e' minore ed e' dispari inizio lo scontro
										//altrimenti lo inizia l'altro
										if((compare>0 && diff%2==0)||(compare<0 && diff%2!=0))
										{   System.out.println("#############Scontro--->Decisione:INIZIO IO################");
											String result=sender.startMatch(res_grm.getOwnerId(), res_grm.getOwner(),info.getIp(),info.getPort(), res_grm.getId(),grm.getId(),grm.getQuantity() , res_grm.getX(), res_grm.getY(), res_grm.getZ());
											//attendere esito dello scontro
											if(result.equals("win"))
											{
												System.out.println("Ho vinto");
												
											}
											else if(result.equals("lose"))
											{
												System.out.println("Ho perso");
												
												
											}
											
										 }
										else
										{*/
											System.out.println("#############Scontro--->Decisione:INIZIA ALTRO PEER################");
											 try {
													Thread.sleep(500);
												} catch (InterruptedException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												
											
										//}
										
									//}
									
								}
								else //!inMatch
								{	System.out.println("#############Scontro--->POSSO INIZIARE IO################");
									//if(decision)
									//{
										String result=sender.startMatch(res_grm.getOwnerId(), res_grm.getOwner(),info.getIp(),info.getPort(), res_grm.getId(),grm.getId(),grm.getQuantity() , res_grm.getX(), res_grm.getY(), res_grm.getZ());
										//attendere esito dello scontro
										if(result.equals("win"))
										{
											System.out.println("Ho vinto");
											
										}
										else if(result.equals("lose"))
										{
											System.out.println("Ho perso");
											
											
										}
										
									//}
									
									
								}
								
						
						 }
					     else if(receivedMessage.getMessageType().equals("ERROR"))
					     {
					    	 
					    	 System.out.println(GamePeer.class.toString()+ "Sending Message ERROR!");
					     }
						
					     this.inClash=false;
						
					}
					else
					{
						System.out.println("###########SONO IN CLASH#####################");
						
					}
					 
					
					
				}
			
			
			
			//decision
	       	}
		
	}
	
	

	@Override
	public boolean getGameBand() {
		// TODO Auto-generated method stub
		return gameband;
	}

	@Override
	public void setGameBand(boolean gameband) {
		// TODO Auto-generated method stub
		this.gameband = gameband;
	}

	
}
	




