package it.unipr.ce.dsg.p2pgame.platform.test;

import it.unipr.ce.dsg.p2pgame.platform.GamePlayerResponsible;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobileResponsible;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class VisionTableModel extends AbstractTableModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// Array di String che contiene i nomi delle colonne
	protected String columnNames[] = { "Type","Id","Username","x","y","z"/*,"Time","Position","Old Position"*/};

	// Array che contiene i valori per le diverse righe nel modello della
	// tabella
	protected Object dataValues[][];

	int rowcount;

	int columncount = 6;

	private ArrayList<Object> vision;
	//private ArrayList<GnuPlotFileElement> gnuPlotFileList;

	/**
	 * Permette di settare tramite i valori delle Mail all'iterno del modello
	 * della tabella.
	 *
	 * @param mail_list
	 */
	public void set_FileTableModel(ArrayList<Object> visionList) {

		this.vision = visionList;

		// Create some data
		Object dataValues_app[][] = new Object[visionList.size()][columncount];

		rowcount = visionList.size();

		dataValues = dataValues_app;

		for (int i = 0; i < vision.size(); i++) {
			if (visionList.get(i) instanceof GamePlayerResponsible){
				//"Id","Username","x","y","z","Time","Position","Old Position"
				GamePlayerResponsible player = (GamePlayerResponsible) this.vision.get(i);
				System.out.println(i + ":  VISION UN PLAYER " + player.getPosX() + ", " + player.getPosY());
				dataValues[i][0] = "Player";
				dataValues[i][1] = player.getId();
				dataValues[i][2] = player.getName();
				dataValues[i][3] = player.getPosX();
				dataValues[i][4] = player.getPosY();
				dataValues[i][5] = player.getPosZ();
				//dataValues[i][5] = player.getPositionHash();
				//dataValues[i][6] = player.getOldPos();
			}
			else if (visionList.get(i) instanceof GameResourceMobileResponsible){
				GameResourceMobileResponsible resource = (GameResourceMobileResponsible) this.vision.get(i);
				System.out.println(i + ":  VISION UN MOBILE " + resource.getX() + ", " + resource.getY());
				dataValues[i][0] = "Resource";
				dataValues[i][1] = resource.getId();
				dataValues[i][2] = resource.getDescription();
				dataValues[i][3] = resource.getX();
				dataValues[i][4] = resource.getY();
				dataValues[i][5] = resource.getZ();

			}
			else {
				dataValues[i][0] = "---";
				dataValues[i][1] = "---";
				dataValues[i][2] = "---";
				dataValues[i][3] = "i";
				dataValues[i][4] = "i";
				dataValues[i][5] = "i";
			}
		}
	}

	/**
	 * Restituisce il numero di righe del modello
	 */
	public int getRowCount() {
		// TODO Auto-generated method stub
		return rowcount;
	}

	/**
	 * Restituisce il nome della colonna in base al valore della colonna
	 */
	public String getColumnName(int col) {
		return columnNames[col];
	}

	/**
	 * Restituisce il tipo di oggetto in base al valore della colonna
	 */
	@SuppressWarnings("unchecked")
	public Class getColumnClass(int c) {
		//return getValueAt(0, c).getClass();
		return Object.class;
	}

	/**
	 * Restituisce il numero di colonne presenti nel modello
	 */
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columncount;
	}

	/**
	 * Restituisce l'oggetto presente nel modello alle posizioni passate alla
	 * funzione.
	 */
	public Object getValueAt(int row, int col) {

		return dataValues[row][col];
	}

	/**
	 * Restituisce l'oggetto presente nel modello alle posizioni passate alla
	 * funzione.
	 */
	public Object getRowObject(int row ) {

		return (String)dataValues[row][0];
	}


	/**
	 * Permette di impostare il valore di un oggetto nel modello alla posizione
	 * indicata dai valori passati alla funzione.
	 */
	public void setValueAt(Object value, int row, int col) {

		return;
	}

	/**
	 * Stabilisce quali celle del modello sono editabili oppure no.
	 */
	public boolean isCellEditable(int row, int col) {

			return true;
	}

}