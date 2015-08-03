package it.unipr.ce.dsg.p2pgame.platform.test;

import it.unipr.ce.dsg.p2pgame.platform.RegisteredUser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

public class RegisteredUserTableModel extends AbstractTableModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// Array di String che contiene i nomi delle colonne
	protected String columnNames[] = { "Username","Password","Id"};

	// Array che contiene i valori per le diverse righe nel modello della
	// tabella
	protected Object dataValues[][];

	int rowcount;

	int columncount = 3;

	//private ArrayList<GnuPlotFileElement> gnuPlotFileList;
	private HashMap<String, RegisteredUser> registeredUser;

	/**
	 * Permette di settare tramite i valori delle Mail all'iterno del modello
	 * della tabella.
	 *
	 * @param mail_list
	 */
	public void set_FileTableModel(/*ArrayList<GnuPlotFileElement> nodeResourceList*/HashMap<String, RegisteredUser> registeredUserTable) {

		this.registeredUser = registeredUserTable;

		// Create some data
		Object dataValues_app[][] = new Object[registeredUserTable.size()][columncount];

		rowcount = registeredUserTable.size();

		dataValues = dataValues_app;

//		for (int i = 0; i < nodeResourceList.size(); i++) {
//			dataValues[i][0] = nodeResourceList.get(i).getFileName();
//			dataValues[i][1] = nodeResourceList.get(i).getXLabel();
//			dataValues[i][2] = nodeResourceList.get(i).getYLabel();
//		}

		Set<String> key_set = registeredUserTable.keySet();
		Iterator<String> iter = key_set.iterator();
		int i=0;
		while(iter.hasNext()){
			String key = iter.next();
			RegisteredUser reg = registeredUserTable.get(key);

			dataValues[i][0] = reg.getUsername();
			dataValues[i][1] = reg.getPassword();
			dataValues[i][2] = reg.getId();

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
		return getValueAt(0, c).getClass();
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

//		dataValues[row][col] = value;
//
//		//gnuPlotFileList.get(row).setFileName((String)dataValues[row][0]);
//		//gnuPlotFileList.get(row).setXLabel((String)dataValues[row][1]);
//		//gnuPlotFileList.get(row).setYLabel((String)dataValues[row][2]);
//		Set<String> key_set = this.registeredUser.keySet();
//		Iterator<String> iter = key_set.iterator();
//
//		String key;
//		for (int i=0; i < this.registeredUser.size(); i++)
//			key = iter.next();
//
//		RegisteredUser reg = this.registeredUser.get(key);
//
//
//		fireTableCellUpdated(row, col);
		return;
	}

	/**
	 * Stabilisce quali celle del modello sono editabili oppure no.
	 */
	public boolean isCellEditable(int row, int col) {

			return false;
	}

}