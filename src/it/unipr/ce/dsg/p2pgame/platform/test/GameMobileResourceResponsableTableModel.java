package it.unipr.ce.dsg.p2pgame.platform.test;

import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobileResponsible;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

public class GameMobileResourceResponsableTableModel extends AbstractTableModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// Array di String che contiene i nomi delle colonne
	protected String columnNames[] = { "Id","Name","Owner Id","Owner","Quantity","x","y","z","Time","Position","Old Position"};

	// Array che contiene i valori per le diverse righe nel modello della
	// tabella
	protected Object dataValues[][];

	int rowcount;

	int columncount = 11;

	//String id, String name, double posX, double posY, double posZ,
	//double vel, double visibility,
	//long time, String pos, String oldPos
	private HashMap<String, GameResourceMobileResponsible> resResources;

	/**
	 * Permette di settare tramite i valori delle Mail all'iterno del modello
	 * della tabella.
	 *
	 * @param mail_list
	 */
	public void set_FileTableModel(HashMap<String, GameResourceMobileResponsible> responsableResourceTable) {

		this.resResources = responsableResourceTable;

		// Create some data
		Object dataValues_app[][] = new Object[responsableResourceTable.size()][columncount];

		rowcount = responsableResourceTable.size();

		dataValues = dataValues_app;

		Set<String> key_set = responsableResourceTable.keySet();
		Iterator<String> iter = key_set.iterator();
		int i=0;
		while(iter.hasNext()){
			String key = iter.next();
			GameResourceMobileResponsible resource = responsableResourceTable.get(key);

			//"Id","Name","Owner Id","Owner","Quantity","x","y","z","Time","Position","Old Position"
			dataValues[i][0] = resource.getId();
			dataValues[i][1] = resource.getDescription();
			dataValues[i][2] = resource.getOwnerId();
			dataValues[i][3] = resource.getOwner();
			dataValues[i][4] = resource.getQuantity();
			dataValues[i][5] = resource.getX();
			dataValues[i][6] = resource.getY();
			dataValues[i][7] = resource.getZ();
			dataValues[i][8] = resource.getTimestamp();
			dataValues[i][9] = resource.getSpatialPosition();
			dataValues[i][10] = resource.getOldPos();


			i++;
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

			return false;
	}

}