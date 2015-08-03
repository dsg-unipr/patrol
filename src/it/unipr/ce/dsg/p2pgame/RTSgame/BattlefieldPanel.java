package it.unipr.ce.dsg.p2pgame.RTSgame;

import it.unipr.ce.dsg.p2pgame.RTSgame.KnowledgeSpace.SpaceType;
import it.unipr.ce.dsg.p2pgame.platform.GamePeer;
import it.unipr.ce.dsg.p2pgame.platform.GamePlayerResponsible;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobileResponsible;
import it.unipr.ce.dsg.p2pgame.util.MultiLog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class BattlefieldPanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private BattlefieldMouseListener list;// = new Image_MouseListener();
	private BufferedImage imm;

	private int granularity;

	private int height;
	private int width;

	private ArrayList<KnowledgeSpace> knowledges;
	private ArrayList<Integer> toUpdate = new ArrayList<Integer>();

	private int maxX;
	private int maxY;

	private Graphics2D g2;
	private boolean init = false;

	private JLabel xLabel;
	private JLabel yLabel;

//	private int per_size;

	//private String image_info;

	public BattlefieldPanel(String file_name, double gran, JLabel xLabel, JLabel yLabel, JPanel pan, GamePeer peer) {
		MultiLog.println(BattlefieldPanel.class.toString(), "ENTRO IN BATTLEFIELD PANEL");
		this.xLabel = xLabel;
		this.yLabel = yLabel;

//		per_size = per_value;
		//image_info = image_info_value;

		this.granularity = (int) Math.round(gran);

		this.list = new BattlefieldMouseListener(this.xLabel, this.yLabel, this.granularity, pan, peer, this);

		//this.initializeKnowledge();
		addMouseListener(list);
		addMouseMotionListener(list);

		try {
			imm = ImageIO.read(new File(file_name));
			height = imm.getHeight();
			width = imm.getWidth();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			MultiLog.println(BattlefieldPanel.class.toString(), "ERROR CAN'T READ INPUT FILE !");
			//System.out.println("ERROR CAN'T READ INPUT FILE !");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			MultiLog.println(BattlefieldPanel.class.toString(), "ERROR CAN'T READ INPUT FILE !");
			//System.out.println("ERROR CAN'T READ INPUT FILE !");
		}


	}


	public void setPeerToList(GamePeer gp){
		this.list.setPeer(gp);
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		//Graphics2D g2 = (Graphics2D) g;
		g2 = (Graphics2D) g;

//		g2.drawImage(imm, 0, 0, imm.getWidth() / per_size, imm.getWidth()
//				/ per_size, this);
		g2.drawImage(imm, 0, 0, imm.getWidth(), imm.getWidth(), this);


//		if (!this.init){
//			this.initializeKnowledge();
			//this.repaintAllKnowledge();
//			this.init = true;
//		}
//
		//if ((list.getX() != 0 || list.getY() != 0)) {

//			int x_offset = 0;
//			int y_offset = 0;
//
//			if (list.getX() > imm.getWidth() - 50)
//				x_offset = -50;
//
//			if (list.getX() < 16)
//				x_offset = +16;
//
//			if (list.getY() < 32)
//				y_offset = +40;


			repaintAllKnowledge();

		//	int x_pos = (int) Math.round( list.getX() / this.granularity - 0.5);
		//	int y_pos = (int) Math.round( list.getY() / this.granularity - 0.5 );

		//	System.out.println("Paint offset position " + x_pos + ", " + y_pos);
			//this.xLabel.setText(Integer.toString(x_pos));
			//this.yLabel.setText(Integer.toString(y_pos));

			//this.repaintAllKnowledge();
			//String output = "";
//			String output = "(" + x_pos + "," + y_pos + ")";

			//output = "(" + list.getX() + "," + list.getY() + ")";
//			g2.setColor(Color.ORANGE);
//			g2.drawString(output, list.getX() + x_offset, list.getY() - 10
//					+ y_offset);
//			g2.setColor(Color.GREEN);

			//g2.drawRect( list.getX() - this.granularity*x_pos, list.getY() - this.granularity*x_pos, this.granularity, this.granularity);
//			g2.drawRect( this.granularity*x_pos, this.granularity*y_pos, this.granularity, this.granularity);
			//g2.drawRect( list.getX(), list.getY(), this.granularity, this.granularity);

			//091004
			//this.repaintNews();
		//}

//		g2.setColor(Color.ORANGE);
//		g2.drawString(image_info, 5, 15);

	}

	public int getWidth() {
		return imm.getWidth();
	};

	public int getHeight() {
		return imm.getHeight();
	}

	public void printKnowledges(){
		for (int i = 0; i < this.knowledges.size(); i++){
			if (this.knowledges.get(i).getType() != SpaceType.UNKNOW){
				MultiLog.println(BattlefieldPanel.class.toString(), i + " " + this.knowledges.get(i).getType());
				//System.out.println(i + " " + this.knowledges.get(i).getType());
			}
		}
	}

	public void initializeKnowledge(){
		this.maxX = this.width / this.granularity;
		this.maxY = this.height / this.granularity;

		this.knowledges = new ArrayList<KnowledgeSpace>();

		for (int y=0; y < this.maxY; y++){
			for (int x=0; x < this.maxX; x++){
				KnowledgeSpace known = new KnowledgeSpace();
				this.knowledges.add(known);
				//this.toUpdate.add(y*this.maxX + x);
			}
		}
	}

	public void repaintAllKnowledge(){
		int pos = 0;
		for (int y=0; y < this.maxY; y++) {
			for (int x=0; x < this.maxX; x++){

				KnowledgeSpace element = this.knowledges.get(pos);

				if (element.getType() == SpaceType.SPACE){
					//non occorre fare niente

				}
				else if (element.getType() == SpaceType.UNKNOW){
				//	System.out.println("DISEGNO grigio ( " + x + ", " + y + " )");
					g2.setColor(Color.GRAY);
					g2.fillRect(x*this.granularity, y*this.granularity, this.granularity, this.granularity);
//
				}
				else if (element.getType() == SpaceType.PLANET){
					g2.setColor(Color.RED);
					g2.fillRect(x*this.granularity, y*this.granularity, this.granularity, this.granularity);

				}
				else if (element.getType() == SpaceType.STARSHIP){
					g2.setColor(Color.GREEN);
					g2.fillRect(x*this.granularity, y*this.granularity, this.granularity, this.granularity);
				}


				pos++;
			}
		}

	}

	public void addMyPlanetPosition(int x, int y, GamePlayerResponsible myPlanet){
		int x_pos = (int) Math.round( x / this.granularity - 0.5);
		int y_pos = (int) Math.round( y / this.granularity - 0.5);

		//System.out.println("POSIZIONE PIANETA PERSONALE " + x_pos*this.granularity + ", " + y_pos*this.granularity);
//		if(g2==null)
//			System.out.println("G2 NULL");
//		g2.fillRect(x_pos*this.granularity, y_pos*this.granularity, this.granularity, this.granularity);
//		g2.setColor(Color.RED);
		KnowledgeSpace planet = new KnowledgeSpace();
		planet.setToPlanet(myPlanet);
		//TODO: verificare se ha senso
		//System.out.println("DIMENSIONE " + this.knowledges.size());
		this.knowledges.set(y_pos*this.maxX + x_pos, planet);

		//

		//this.repaint();


	//	this.toUpdate.add(y_pos*this.maxX + x_pos);
		//System.out.println("AGGIUNTA la conoscenza per " + y_pos*this.maxX + "  " + x_pos);
	}

	public void addNewInfo(int x, int y,Object obj){

		//System.out.println("(X,Y):"+x+","+y);

		int x_pos = (int) Math.round( (double) x/(double) this.granularity - 0.5);
		int y_pos = (int) Math.round( (double) y/(double) this.granularity - 0.5);

		//System.out.println("(X_POS,Y_POS):"+x_pos+","+y_pos);

		//vedere di che tipo ï¿½ l'istanza ricevuta e creare la relativa Knowledge
		if (obj == null){

			KnowledgeSpace space = new KnowledgeSpace();
			space.setToSpace();
			//System.out.println("info added  " + y_pos*this.maxX );
			this.knowledges.set(y_pos*this.maxX + x_pos, space);
		}
		else if (obj instanceof GameResourceMobileResponsible){
			KnowledgeSpace ship = new KnowledgeSpace();
			ship.setToShip( (GameResourceMobileResponsible) obj);
			//System.out.println("info added  " + y_pos*this.maxX );
			this.knowledges.set(y_pos*this.maxX + x_pos, ship);

		}
		else if (obj instanceof GamePlayerResponsible){
			KnowledgeSpace planet = new KnowledgeSpace();
			planet.setToPlanet( (GamePlayerResponsible) obj);
			//System.out.println("info added  " + y_pos*this.maxX );
			this.knowledges.set(y_pos*this.maxX + x_pos, planet);
		}

		//this.repaint();

	}


	public KnowledgeSpace getKnowledgeFor(int x, int y){
		return this.knowledges.get(y*this.maxX + x);
	}

	public void repaintNews(){
		//Periodicamente vede se ci sono nuove informazioni da visualizzare
		while(this.toUpdate.size() > 0){
			int pos = this.toUpdate.remove(0);

			KnowledgeSpace element = this.knowledges.get(pos);

			int x = (int) Math.round(pos/this.maxX -0.5);
			int y = pos%this.maxX;

			if (element.getType() == SpaceType.SPACE){
				//non occorre fare niente

			}
			else if (element.getType() == SpaceType.UNKNOW){
				MultiLog.println(BattlefieldPanel.class.toString(), "DISEGNO grigio ( " + x + ", " + y + " )");
				//System.out.println("DISEGNO grigio ( " + x + ", " + y + " )");
//				Rectangle rect = new Rectangle();
//				rect.setBounds(x*this.granularity, y*this.granularity, this.granularity, this.granularity);
				//g2.draw(rect);
				g2.setColor(Color.GRAY);
				g2.fillRect(x*this.granularity, y*this.granularity, this.granularity, this.granularity);


			}
			else if (element.getType() == SpaceType.PLANET){
				g2.setColor(Color.RED);
				g2.fillRect(x*this.granularity, y*this.granularity, this.granularity, this.granularity);

			}
			else if (element.getType() == SpaceType.STARSHIP){
				g2.setColor(Color.GREEN);
				g2.fillRect(x*this.granularity, y*this.granularity, this.granularity, this.granularity);
			}
		}
	}

	///////////////// JPANEL LISTENER /////////////////////////////////////

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(imm.getWidth(),imm.getHeight());
	}


}
