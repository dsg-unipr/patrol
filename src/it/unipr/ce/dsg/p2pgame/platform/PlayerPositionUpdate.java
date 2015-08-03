package it.unipr.ce.dsg.p2pgame.platform;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import it.unipr.ce.dsg.p2pgame.util.MultiLog;
import it.unipr.ce.dsg.p2pgame.util.SHA1;

public class PlayerPositionUpdate implements Runnable {


	private GamePeer peer;
	private GamePlayer player;
	private GameWorld world;

	private String threadId = new Long(Thread.currentThread().getId()).toString();


	public PlayerPositionUpdate(GamePeer peer, GamePlayer player, GameWorld world) {
		super();
		this.peer = peer;
		this.player = player;
		this.world = world;
		
		this.threadId=peer.getMyThreadId();
	}




	public void run() {

		while(true){
			MultiLog.println(PlayerPositionUpdate.class.toString(), "Ricerche nell'intorno del giocatore: " + this.player.getName() + "( "
					+ this.player.getPosX() + ", " + this.player.getPosY() + ", " + this.player.getPosZ());
//			System.out.println("Ricerche nell'intorno del giocatore: " + this.player.getName() + "( "
//					+ this.player.getPosX() + ", " + this.player.getPosY() + ", " + this.player.getPosZ());
			try {
				//TODO: aggoirnarlo secondo la velocitï¿½
				//Thread.sleep(Math.round(this.player.getVelocity()*1000));
				//TODO: modificato il 091013 da 10000  a 1000
				Thread.sleep(2500);//Math.round(2500));


				//ricerca gli oggetti nell'intorno
				this.searchNearObject();

				MultiLog.println(GameResourceMobile.class.toString(), "Dopo while player");

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


	private void searchNearObject() throws InterruptedException{
		//TODO:: ricercare gli oggetti in un intorno
		double vis = this.player.getVisibility();
		MultiLog.println(PlayerPositionUpdate.class.toString(),"Lunched SEARCH Near Object");
		//System.out.println("Lunched SEARCH Near Object" );
               // System.out.println("GRANULARITA "+  this.world.getGranularity());
		int i=0;

		GameResourceMobile res = null;
		for (int s=0; s < this.peer.getMyResources().size(); s++){
			if (this.peer.getMyResources().get(s) instanceof GameResourceMobile){
				res = (GameResourceMobile) this.peer.getMyResources().get(s);
			}
		}
//
//		if (res != null){
//			System.out.println("@@@@@@@@@@@@@@@my pos " + this.player.getPosX() + ", " + this.player.getPosY());
//			System.out.println("@@@@@@@@@@@@@@@ship pos " + res.getX() + ", " + res.getY());
//			System.exit(1);
//		}

		for (double z = this.player.getPosZ() - vis; z <= this.player.getPosZ() + vis; z += this.world.getGranularity()) {

			if (this.world.getMaxZ() == this.world.getMinZ()){
				z = this.player.getPosZ();
			}

			for(double y = this.player.getPosY() - vis; y <= this.player.getPosY() + vis; y += this.world.getGranularity()) {

				for (double x = this.player.getPosX() - vis; x <= this.player.getPosX() + vis; x += this.world.getGranularity()) {

					try {




						//TODO: si deve saltare la posizione del giocatore attuale!!!
						if ( !(x == this.player.getPosX() && y == this.player.getPosY() && z == this.player.getPosZ())){


							String pos = new Double(x).toString() + new Double(y).toString() + new Double(z).toString();
							String position = SHA1.convertToHex(SHA1.calculateSHA1(pos));
							//String position = SHA1.convertToHex(SHA1.calculateSHA1(String.valueOf(x) + "+" + String.valueOf(y) + "+" + String.valueOf(z)));

							MultiLog.println(PlayerPositionUpdate.class.toString(),"RICERCA per " + x + ", " + y + ", " + z + " hash " + position);
							//System.out.println("RICERCA per " + x + ", " + y + ", " + z + " hash " + position);

							//TODO: chiedi di poter avere le info su tale posizione
							Object resp = this.peer.requestResource(position, x, y, z, this.threadId);
							MultiLog.println(GameResourceMobile.class.toString(), "dopo ricerca");
							if (resp instanceof GamePlayerResponsible){
								//System.out.println("Ricevuto un giocatore per " + x + "," + y + "," + z);

								this.peer.addToVision(resp, i);

							}
							else if (resp instanceof GameResourceMobileResponsible){
								MultiLog.println(PlayerPositionUpdate.class.toString(), "Ricevuta una risorsa MOBILE per " + x + "," + y + "," + z);
								//System.out.println("Ricevuta una risorsa MOBILE per " + x + "," + y + "," + z);
							//	System.exit(1);
								this.peer.addToVision(resp, i);

							}
							else {
								//System.out.println("Tipo ricevuto object");
								this.peer.addToVision(null, i);
							}


							if (res != null){
								if (x >= res.getX()  && y  >= res.getY()){
									MultiLog.println(PlayerPositionUpdate.class.toString(), "Player " + this.player.getPosX() + ", " + this.player.getPosY());
									//System.out.println("Player " + this.player.getPosX() + ", " + this.player.getPosY());
									MultiLog.println(PlayerPositionUpdate.class.toString(), "Res " + res.getX() + ", " + res.getY());
									//System.out.println("Res " + res.getX() + ", " + res.getY());
									MultiLog.println(PlayerPositionUpdate.class.toString(), "POSIZIoni > uguALI");
									//System.out.println("POSIZIoni > uguALI");
//									try {
//										Thread.sleep(10000);
//									} catch (InterruptedException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
								}

								if (x == res.getX()  && y  == res.getY()){

									MultiLog.println(PlayerPositionUpdate.class.toString(), "Player " + this.player.getPosX() + ", " + this.player.getPosY());
									//System.out.println("Player " + this.player.getPosX() + ", " + this.player.getPosY());
									MultiLog.println(PlayerPositionUpdate.class.toString(), "Res " + res.getX() + ", " + res.getY());
									//System.out.println("Res " + res.getX() + ", " + res.getY());
									MultiLog.println(PlayerPositionUpdate.class.toString(), "X  " + x + " , Y  " + y + " , Z " + z);
									//System.out.println("X  " + x + " , Y  " + y + " , Z " + z);
									//System.out.println(resp);
									MultiLog.println(PlayerPositionUpdate.class.toString(), "hash " + position);
									//System.out.println("hash " + position);
									MultiLog.println(PlayerPositionUpdate.class.toString(), "Res hash " + res.getSpatialPosition());
									//System.out.println("Res hash " + res.getSpatialPosition());

									MultiLog.println(PlayerPositionUpdate.class.toString(), "" + this.peer.getResResources().size());
									//System.out.println(this.peer.getResResources().size());

									MultiLog.println(PlayerPositionUpdate.class.toString(), "VISION della player position");
									//System.out.println("VISION della player position");
									this.peer.printVision();
									MultiLog.println(PlayerPositionUpdate.class.toString(), "POSIZIoni == uguALI");
									//System.out.println("POSIZIoni == uguALI");
									//System.exit(1);
								}
							}


							i++;
							//TODO: 091013 tolto per prova di velocizzare
//							try {
//								Thread.sleep(1000);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}

						}
						else {
							MultiLog.println(PlayerPositionUpdate.class.toString(), "Is MY POSITION");
							//System.out.println("Is MY POSITION");
						}

					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}

				}

			}
			if (this.world.getMaxZ() == this.world.getMinZ()){
				z = this.player.getPosZ() + vis;
			}

		}
	}
}
