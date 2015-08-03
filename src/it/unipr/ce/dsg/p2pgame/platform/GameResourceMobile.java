package it.unipr.ce.dsg.p2pgame.platform;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import it.unipr.ce.dsg.p2pgame.util.MultiLog;
import it.unipr.ce.dsg.p2pgame.util.SHA1;

public class GameResourceMobile extends GameResource {

	private double x;
	private double y;
	private double z;

	private double velocity;
	private double vision;

	private String owner;
	private String ownerId;


	private Thread update;
	//final long period = 3000;
	private ArrayList<Object> resourceVision = null;

	private String threadId = new Long(Thread.currentThread().getId()).toString();
	private GameWorld world;
	private GamePeer peer;
//	private double relMovX;//direzione che stï¿½ seguendo cosï¿½ da mantenerla automaticamente
//	private double relMovY;
//	private double relMoxZ;
	
	//status
	private boolean status;

	public GameResourceMobile(String id, String description, String owner, String ownerId, double quantity, double x, double y, double z, double vel, double vis) {

		super(id, description, quantity);

		this.x = x;
		this.y = y;
		this.z = z;
		this.velocity = vel;
		this.vision = vis;


		this.owner = owner;
		this.ownerId = ownerId;
		this.threadId=ownerId;
		this.resourceVision = new ArrayList<Object>();


		this.status=false;

//		this.relMovX = relMovX;
//		this.relMovY = relMovY;
//		this.relMoxZ = relMovZ;

	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void initializeSearch(final long period, GameWorld world, GamePeer peer){

		this.world = world;
		this.peer = peer;

		this.update = new Thread( new Runnable() {
			public void run() {
				try {
					while(true) {
						//TODO: prova forzando il periodo
						Thread.sleep(2000);
						//Thread.sleep(period);

						searchObjectNearToResource();
						//publishResources();

						MultiLog.println(GameResourceMobile.class.toString(), "Dopo while");
					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
		);
		this.update.setPriority(Thread.MAX_PRIORITY);
		this.update.start();

	}
	
	private void publishResources()
	{
		try {
			this.peer.publishResourceMobile(this.peer.getMyThreadId());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	private void searchObjectNearToResource() throws InterruptedException{
		//TODO:: ricercare gli oggetti in un intorno
		//double vis = this.vision;
		MultiLog.println(GameResourceMobile.class.toString(), "Lunched SEARCH Object Near To RESOURCE MOBILE");
		//System.out.println("Lunched SEARCH Object Near To RESOURCE MOBILE");
		int i=0;
		
		for (double z = this.z - this.vision; z <= this.z + this.vision; z += this.world.getGranularity()) {

			if (this.world.getMaxZ() == this.world.getMinZ()){
				z = this.z;
			}

			for(double y = this.y - this.vision; y <= this.y + this.vision; y += this.world.getGranularity()) {

				for (double x = this.x - this.vision; x <= this.x + this.vision; x += this.world.getGranularity()) {

					try {

						//TODO: si deve saltare la posizione del giocatore attuale!!!
						if ( !(x == this.x && y == this.y && z == this.z)){


							String pos = new Double(x).toString() + new Double(y).toString() + new Double(z).toString();
							String position = SHA1.convertToHex(SHA1.calculateSHA1(pos));
							//String position = SHA1.convertToHex(SHA1.calculateSHA1(String.valueOf(x) + "+" + String.valueOf(y) + "+" + String.valueOf(z)));

							MultiLog.println(GameResourceMobile.class.toString(), "RICERCA MOBILE per " + x + ", " + y + ", " + z + " hash " + position);
							//System.out.println("RICERCA MOBILE per " + x + ", " + y + ", " + z + " hash " + position);

							//TODO: chiedi di poter avere le info su tale posizione
							//System.out.println("GameResourceMobile-->searchToNearResource "+x+","+y+","+z);
							
							
							Object resp = this.peer.requestResource(position, x, y, z, this.threadId);
							//Object resp = this.peer.requestResource(position, x, y, z, this.threadId);
							MultiLog.println(GameResourceMobile.class.toString(), "dopo ricerca mobile");
							if (resp instanceof GamePlayerResponsible){
								//System.out.println("RICERCA MOBILE Ricevuto un giocatore per " + x + "," + y + "," + z);
								
								
								//this.peer.addToVision(resp, i);
								this.addToResourceVision(resp, i);

							}
							else if (resp instanceof GameResourceMobileResponsible){
								//System.out.println("RICERCA MOBILE Ricevuta una risorsa MOBILE per " + x + "," + y + "," + z);
								
								//this.peer.addToVision(resp, i);
								this.addToResourceVision(resp, i);

							}
							else {
								//System.out.println("RICERCA MOBILE Tipo ricevuto object");

								//this.peer.addToVision(null, i);
								this.addToResourceVision(null, i);
							}

							i++;
							//TODO:tolto per prova per velocizzare 091013
//							try {
//								Thread.sleep(1000);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}

						}
						else{
							MultiLog.println(GameResourceMobile.class.toString(), "Is MY POSITION - RESOURCE MOBILE");
							//System.out.println("Is MY POSITION - RESOURCE MOBILE");
						}
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}

				}
			}

			if (this.world.getMaxZ() == this.world.getMinZ()){
				z = this.z + this.vision;
			}
		}
	}


	public void moveResource(double relX, double relY, double relZ){
		this.x += relX;
		this.y += relY;
		this.z += relZ;
	}

	public String getSpatialPosition(){

		String pos = new Double(this.x).toString() + new Double(this.y).toString() + new Double(this.z).toString();

		try {
			return (SHA1.convertToHex(SHA1.calculateSHA1(pos)) );// + "-" + this.x + "," + this.y + "," + this.z ) ;
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

		return null;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public double getVelocity() {
		return velocity;
	}

//	public double getRelMovX() {
//		return relMovX;
//	}
//
//	public double getRelMovY() {
//		return relMovY;
//	}
//
//	public double getRelMoxZ() {
//		return relMoxZ;
//	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

//	public void setRelMovX(double relMovX) {
//		this.relMovX = relMovX;
//	}
//
//	public void setRelMovY(double relMovY) {
//		this.relMovY = relMovY;
//	}
//
//	public void setRelMoxZ(double relMoxZ) {
//		this.relMoxZ = relMoxZ;
//	}

	public double getVision() {
		return vision;
	}

	public void setVision(double vision) {
		this.vision = vision;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}


	public Object clone() {
		GameResourceMobile clone = (GameResourceMobile) super.clone();


		clone.x = this.x;
		clone.y = this.y;
		clone.z = this.z;

		clone.velocity = this.velocity;
		clone.vision = this.vision;

		clone.owner = this.owner;
		clone.ownerId = this.ownerId;

		return clone;
	}

	public /*synchronized*/ ArrayList<Object> getResourceVision() {
		return resourceVision;
	}

	public void addToResourceVision(Object obj, int pos) {
		if (this.resourceVision.size() > pos)
			this.resourceVision.set(pos, obj);
		else
			this.resourceVision.add(pos, obj);

	}
	
	//aggiunto da jose' murga 15/08/2011
	public String getThreadId()
	{
		return this.threadId;
	}
	
}
