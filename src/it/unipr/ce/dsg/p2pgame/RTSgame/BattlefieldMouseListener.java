package it.unipr.ce.dsg.p2pgame.RTSgame;

import it.unipr.ce.dsg.p2pgame.RTSgame.KnowledgeSpace.SpaceType;
import it.unipr.ce.dsg.p2pgame.platform.GamePeer;
import it.unipr.ce.dsg.p2pgame.platform.GamePlayer;
import it.unipr.ce.dsg.p2pgame.platform.GamePlayerResponsible;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobile;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobileResponsible;
import it.unipr.ce.dsg.p2pgame.util.MultiLog;

import java.awt.Choice;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;




public class BattlefieldMouseListener extends MouseAdapter implements
MouseMotionListener {


		public BattlefieldMouseListener(JLabel x, JLabel y, int g, JPanel panel, GamePeer gp, BattlefieldPanel bf){

			this.xLab = x;
			this.yLab = y;
			this.gran = g;

			this.panel = panel;
			this.gp = gp;
			this.bf = bf;



//		        resType.setName("resType");
//
//		        info.setName("Info");
//
//		        qtInfo.setName("qtInfo");
//
//		        choiceAttack.setName("Choice");
//
//		        moveButton.setName("move");

		}

		private JPanel panel;
		private GamePeer gp;
		private static BattlefieldPanel bf;

		private JButton moveButton;
		private JLabel resType;
		private JLabel info;
		private JLabel qt;
		private Choice choice;

		private GameResourceMobile starship;

		private int selection = 0;
		private ArrayList<String> attack = new ArrayList<String>();

		private JLabel xLab;
		private JLabel yLab;
		private int gran;

		private int X = 0;
		private int Y = 0;

		//punti da utilizzare nel calcolo del percorso
		private int x_a = -1;
		private int y_a = -1;
		private int x_b = -1;
		private int y_b = -1;
		private String mobRes = null;

		//TODO: aggiungere una variabile per indicare la parte di click che si sta considerando

		private void initComponent(){
			MultiLog.println(BattlefieldMouseListener.class.toString(), "####  Get Component Name");
			//System.out.println("####  Get Component Name");
			for (int i=0; i < this.panel.getComponentCount(); i++){

				MultiLog.println(BattlefieldMouseListener.class.toString(), i +  ":  " + this.panel.getComponent(i).getName());
				//System.out.println(i +  ":  " + this.panel.getComponent(i).getName());

				if (this.panel.getComponent(i).getName() != null ){

					if (this.panel.getComponent(i).getName().compareTo("resType") == 0){
						MultiLog.println(BattlefieldMouseListener.class.toString(), "Found RESTYPE");
						//System.out.println("Found RESTYPE");
						this.resType = (JLabel) this.panel.getComponent(i);
					}
					else if (this.panel.getComponent(i).getName().compareTo("Info") == 0){
						this.info = (JLabel) this.panel.getComponent(i);
					}
					else if (this.panel.getComponent(i).getName().compareTo("qtInfo") == 0) {
						this.qt = (JLabel) this.panel.getComponent(i);
					}
					else if (this.panel.getComponent(i).getName().compareTo("Choice") == 0){
						this.choice = (Choice) this.panel.getComponent(i);
					}
					else if (this.panel.getComponent(i).getName().compareTo("move") == 0){
						this.moveButton = (JButton) this.panel.getComponent(i);
					}

				}
			}

			this.moveButton.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                moveButtonActionPerformed(evt);
	                }
			});
		}


		private void setAllInvisible(){
			this.moveButton.setVisible(false);
			this.resType.setVisible(false);
			this.info.setVisible(false);
			this.qt.setVisible(false);
			this.choice.setVisible(false);

		}

		//il primo punto selezionato dev'essere il nostro pianeta o una nostra navicella
		////il secondo punto qualsiasi cosa ma se Ã¨ un altro pianeta o una navicella nemica attaccarla alla fine
		public void mousePressed(MouseEvent ev) {
			this.initComponent();
			this.setAllInvisible();
			//TODO: fare la funzione di spostamento o attacco
			MultiLog.println(BattlefieldMouseListener.class.toString(), "Mouse Pressed Called !");
			//System.out.println("Mouse Pressed Called !");

//			int x_pos = (int) Math.round( (double) X / this.gran - 0.5);
//			int y_pos = (int) Math.round( (double) Y / this.gran - 0.5);

			int x_pos = (int) ( (double) X / this.gran );
			int y_pos = (int) ( (double) Y / this.gran );

			MultiLog.println(BattlefieldMouseListener.class.toString(), "Mouse pressed:: offset position " + x_pos + ", " + y_pos);
			//System.out.println("Mouse pressed:: offset position " + x_pos + ", " + y_pos);
			MultiLog.println(BattlefieldMouseListener.class.toString(), "Calculating path...");
			//System.out.println("Calculating path...");

			if (this.x_a == -1 && this.y_a == -1){
				//se ï¿½ il primo punto selezionato si occupa soltanto di caricare le informazioni

				//TODO: vedere il tipo di risorsa selezionata
				this.x_a = x_pos;
				this.y_a = y_pos;

				KnowledgeSpace kl = this.bf.getKnowledgeFor(this.x_a, this.y_a);
				if (kl.getType() == SpaceType.UNKNOW || kl.getType() == SpaceType.SPACE ){
					this.x_a = -1;
					this.y_a = -1;
					MultiLog.println(BattlefieldMouseListener.class.toString(), "Selected Nothing!");
					//System.out.println("Selected Nothing!");
				}
				else if(kl.getType() == SpaceType.STARSHIP){
					MultiLog.println(BattlefieldMouseListener.class.toString(), "Setting icon...");
					//System.out.println("Setting icon...");
					this.resType.setIcon(new javax.swing.ImageIcon("res/starship_trasp.png"));
					this.resType.setVisible(true);

					GameResourceMobileResponsible ship = (GameResourceMobileResponsible) kl.getElement();

					//TODO: fornire l'opzione per poter attaccare

					if (ship.getOwnerId().compareTo(this.gp.getMyId()) == 0){
						this.info.setText("MY STARSHIP");
						this.info.setVisible(true);

						this.qt.setText(Double.toString(ship.getQuantity()));
						this.qt.setVisible(true);

						this.starship = ship;
						MultiLog.println(BattlefieldMouseListener.class.toString(), "Nome  " + this.starship.getId() + " qt " + this.starship.getQuantity());
						//System.out.println("Nome  " + this.starship.getId() + " qt " + this.starship.getQuantity());
					}
					else {
						this.info.setText(ship.getOwner());
						this.info.setVisible(true);
						this.x_a = -1;
						this.y_a = -1;
					}
				}
				else if (kl.getType() == SpaceType.PLANET){
					MultiLog.println(BattlefieldMouseListener.class.toString(), "Setting icon...");
					//System.out.println("Setting icon...");
					this.resType.setIcon(new javax.swing.ImageIcon("res/planet.jpg"));
					this.resType.setVisible(true);

					GamePlayerResponsible player = (GamePlayerResponsible) kl.getElement();
					if (kl.getElement() == null){
						MultiLog.println(BattlefieldMouseListener.class.toString(), "ELEMENTO PLANET NULL ");
						//System.out.println("ELEMENTO PLANET NULL ");
					}

					System.out.println(this.gp);
					MultiLog.println(BattlefieldMouseListener.class.toString(),this.gp.getMyId());
					//System.out.println(this.gp.getMyId());
					MultiLog.println(BattlefieldMouseListener.class.toString(),player.getName());
					//System.out.println(player.getName());
					MultiLog.println(BattlefieldMouseListener.class.toString(),player.getId());
					//System.out.println(player.getId());
					//Vede se il pianeta selezionato Ã¨ il mio
					if (player.getId().compareTo(this.gp.getMyId()) == 0){

						MultiLog.println(BattlefieldMouseListener.class.toString(), "Create my planet info");
						//System.out.println("Create my planet info");
						System.out.println(this.panel);

						//System.out.println("#### PANEL SIZE  " + this.panel.getComponentCount());

						this.info.setText("MY PLANET");
						this.info.setVisible(true);

						//TODO: riempire il choice-box con le informazioni degli elementi di attacco

						ArrayList<Object> res = gp.getMyResources();
						int k=0;
						for (int i = 0; i < res.size(); i++){

							if (res.get(i) instanceof GameResourceMobile){
								GameResourceMobile mob = (GameResourceMobile) res.get(i);

								//Visualizza soltanto gli elementi che sono alla base
								if (mob.getX() == player.getPosX() && mob.getY() == player.getPosY()){
									attack.add(mob.getId());
									this.choice.add(Double.toString(mob.getQuantity()) + "-" + mob.getId().substring(0, 31));
									//TODO:aggiungere un listener che cambia la quantitÃ  della label

									this.choice.addItemListener(new java.awt.event.ItemListener() {

										public void itemStateChanged(ItemEvent e) {
										    MultiLog.println(BattlefieldMouseListener.class.toString(), "selected  " + choice.getSelectedIndex());
										    //System.out.println("selected  " + choice.getSelectedIndex());
										    selection = choice.getSelectedIndex();

										    qt.setText(Double.toString(gp.getMyResourceFromId(attack.get(selection)).getQuantity()));

										}
									});

									this.choice.setVisible(true);

									if (!this.qt.isVisible())
										this.qt.setText(Double.toString(mob.getQuantity()));
									this.qt.setVisible(true);
									k++;
									this.moveButton.setVisible(true);
								}

							}
						}
						if (k == 0){
							this.x_a = -1;
							this.y_a = -1;
						}
					}
					else {
						MultiLog.println(BattlefieldMouseListener.class.toString(), "Selected another planet");
						//System.out.println("Selected another planet");
						this.info.setText(player.getName());
						this.info.setVisible(true);
						this.x_a = -1;
						this.y_a = -1;
					}
				}

			}
			///############### PUNTO B
			//selected B point
			else if (this.x_b == -1 && this.y_b == -1){
				this.x_b = x_pos;
				this.y_b = y_pos;

				MultiLog.println(BattlefieldMouseListener.class.toString(),"Point B " + this.x_b + "," + this.y_b);
				//System.out.println("Point B " + this.x_b + "," + this.y_b);

				//TODO: calcolo della traiettoria e resetta i punti a-b
				//muovere la risorsa prima tutta in una direzione e poi nell'altra
				//dev'essere giï¿½ stata selezionata altrimanti non fa nienete
				MultiLog.println(BattlefieldMouseListener.class.toString(), "Movimento della navicella !!!!!!!!!!!!!!!!!!!!!!!!!!!!! --- PUNTO B");
				//System.out.println("Movimento della navicella !!!!!!!!!!!!!!!!!!!!!!!!!!!!! --- PUNTO B");
		//		this.starship.moveResource(this.x_b, this.y_b, 0);

				if (this.starship != null){

					//final GameResourceMobile res = this.starship;
					//final String resId = this.starship.getId();
					mobRes = this.starship.getId();

					final int gran = this.gran;
					MultiLog.println(BattlefieldMouseListener.class.toString(), "LUNCHING THREAD for move  resource");
					//System.out.println("LUNCHING THREAD for move  resource");
					Thread resMov = new Thread( new Runnable() {
						//return (posX - posX % this.granularity);
						double x_target = X - X % gran;
						//double x_target = X;
						double y_target = Y - Y % gran;
						//double y_target = Y;

						String threadId = new Long(Thread.currentThread().getId()).toString();
						
						String resId = mobRes;
						public void run() {
							try {

								GameResourceMobile res = gp.getMyMobileResourceFromId(resId);

								while(res.getX() != x_target || res.getY() != y_target) {
									Thread.sleep(500);
									//TODO: muove il giocatore di una posizione
									double movX  = x_target - res.getX();
									double movY = y_target - res.getY();

									//TODO: andrebbe messo anche un controllo per ottimizzare il movimento
									if (movX != 0.0){
										if (movX > 0.0 && movX > res.getVelocity() / 2.0){
												movX = res.getVelocity() / 2.0;
										}
										else if (movX < 0.0 && Math.abs(movX) > res.getVelocity() / 2.0){
												movX = - (res.getVelocity() / 2.0);
										}
									}

									if (movY != 0.0){
										if (movY > 0.0 && movY > res.getVelocity() / 2.0){
												movY = res.getVelocity() / 2.0;
										}
										else if (movY < 0.0 && Math.abs(movY) > res.getVelocity() / 2.0){
												movY = - (res.getVelocity() / 2.0);
										}
									}

									MultiLog.println(BattlefieldMouseListener.class.toString(), "RISORSA MOSSA di " + movX + " , " + movY);
									//System.out.println("RISORSA MOSSA di " + movX + " , " + movY);

									if( (movX + movY) > res.getVelocity() ){
										MultiLog.println(BattlefieldMouseListener.class.toString(), "FINE PER MOVIMENTO ECCESSIVO ------------------------------------------------" + res.getVelocity());
										//System.out.println("FINE PER MOVIMENTO ECCESSIVO ------------------------------------------------" + res.getVelocity());
										System.exit(1);
									}
									threadId=gp.getMyThreadId();

									gp.moveResourceMobile(resId, movX, movY, 0, threadId);

									//res.moveResourceMobile(movX, movY, 0.0);

									//TODO: caricare tutta la vision dell'intorno con space e poi
									// aggiungere anche gli elementi che sono nella resource vision
//									for (int i_x = (int) (res.getX() - res.getVision()); i_x < res.getX() + res.getVision(); i_x++){
//										for (int i_y = (int) (res.getY() - res.getVision()); i_y < res.getY() + res.getVision(); i_y++){
//
//											bf.addNewInfo(i_x, i_y, null);
//										}
//									}
//
//									GameResourceMobileResponsible ship = new GameResourceMobileResponsible(res.getId(), res.getDescription(), res.getOwner(), res.getOwnerId(),
//											res.getQuantity(), res.getX(), res.getY(), 0, res.getVelocity(), res.getVision(), 0, "", "");
//									bf.addNewInfo((int) res.getX(), (int) res.getY(), ship);

									//System.out.println("AAAAAA      " + res.getResourceVision().size());

									bf.repaint();
								}

								MultiLog.println(BattlefieldMouseListener.class.toString(), "ARRIVATOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
								//System.out.println("ARRIVATOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");

								//bf.repaint();
							} catch (InterruptedException e) {

								e.printStackTrace();
							}
						}
					}
					);//.start();
					resMov.setPriority(Thread.MAX_PRIORITY);
					resMov.start();
				}

				this.x_a = -1;
				this.y_a = -1;
				this.x_b = -1;
				this.y_b = -1;

				//System.out.println("pos " + this.starship.getX() + " - " + this.starship.getY());
			}

		}

		   private static void setStarShipVision(GameResourceMobile mob, double gran, GamePlayer player){

		    	for (int i_x = (int) (mob.getX() - mob.getVision()); i_x < mob.getX() + mob.getVision(); i_x += gran){
					for (int i_y = (int) (mob.getY() - mob.getVision()); i_y < mob.getY() + mob.getVision(); i_y += gran ){


						if ( !(i_x == player.getPosX() && i_y == player.getPosY()) ){
							bf.addNewInfo(i_x, i_y, null);

							//System.out.println("pos " + i_x + " ,  " + i_y);

						}
					}
				}

				GameResourceMobileResponsible ship = new GameResourceMobileResponsible(mob.getId(), mob.getDescription(), mob.getOwner(), mob.getOwnerId(),
						mob.getQuantity(), mob.getX(), mob.getY(), 0, mob.getVelocity(), mob.getVision(), 0, "", "");

			    		if ( !(mob.getX() == player.getPosX() && mob.getY() == player.getPosY()) ){

			    			bf.addNewInfo((int) mob.getX(), (int) mob.getY(), ship);

			    		}

				ArrayList<Object> mobVis = mob.getResourceVision();

				for (int k = 0; k < mobVis.size(); k++){

					if (mobVis.get(k) instanceof GamePlayerResponsible){
						GamePlayerResponsible playerResp = (GamePlayerResponsible) mobVis.get(k);
						MultiLog.println(BattlefieldMouseListener.class.toString(), "**********************************************");
						//System.out.println("**********************************************");
						MultiLog.println(BattlefieldMouseListener.class.toString(), k + " VISION un PLAYER " + playerResp.getPosX() + ", " + playerResp.getPosY());
						//System.out.println(k + " VISION un PLAYER " + playerResp.getPosX() + ", " + playerResp.getPosY());

						bf.addNewInfo( (int) playerResp.getPosX() , (int) playerResp.getPosY(), playerResp);

					}
					else if (mobVis.get(k) instanceof GameResourceMobileResponsible){
						GameResourceMobileResponsible resource = (GameResourceMobileResponsible) mobVis.get(k);
						MultiLog.println(BattlefieldMouseListener.class.toString(),"**********************************************");
						//System.out.println("**********************************************");
						MultiLog.println(BattlefieldMouseListener.class.toString(), k + " VISION un MOBILE " + resource.getX() + ", " + resource.getY());
						//System.out.println(k + " VISION un MOBILE " + resource.getX() + ", " + resource.getY());

						bf.addNewInfo((int) resource.getX(), (int) resource.getY(), resource);

					}

				}
		    }



		private void moveButtonActionPerformed(java.awt.event.ActionEvent evt){
			MultiLog.println(BattlefieldMouseListener.class.toString(), "move button pressed...");
			//System.out.println("move button pressed...");
			MultiLog.println(BattlefieldMouseListener.class.toString(), "ATTACCO CON " + this.selection + " -> " + this.attack.get(this.selection));
			//System.out.println("ATTACCO CON " + this.selection + " -> " + this.attack.get(this.selection));

			this.starship = this.gp.getMyMobileResourceFromId(this.attack.get(this.selection));
			MultiLog.println(BattlefieldMouseListener.class.toString(), "Nome  " + this.starship.getId() + " qt " + this.starship.getQuantity());
			//System.out.println("Nome  " + this.starship.getId() + " qt " + this.starship.getQuantity());

		}


		public void setPeer(GamePeer gp){
			this.gp = gp;
			this.initComponent();
		}

		public int getX() {
			return X;
		}

		public void setX(int x) {
			X = x;
		}

		public int getY() {
			return Y;
		}

		public void setY(int y) {
			Y = y;
		}

		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void mouseMoved(MouseEvent ev) {
				//System.out.println("Mouse Moved Called !");

				X = (ev.getX());
				Y = (ev.getY());

				int x_pos = (int) Math.round( X / this.gran - 0.5);
				int y_pos = (int) Math.round( Y / this.gran - 0.5 );

			//	System.out.println("Mouse moved:: offset position " + x_pos + ", " + y_pos);
				this.xLab.setText(Integer.toString(x_pos));
				this.yLab.setText(Integer.toString(y_pos));

				//repaint();
		}

}
