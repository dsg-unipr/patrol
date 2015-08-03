package it.unipr.ce.dsg.p2pgame.platform.test;

import it.unipr.ce.dsg.p2pgame.platform.GameResource;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceEvolve;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobile;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class MyResourceTableModel extends AbstractTableModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// Array di String che contiene i nomi delle colonne
	protected String columnNames[] = { "Type","Id","Description","Quantity","x","y","z"};

	// Array che contiene i valori per le diverse righe nel modello della
	// tabella
	protected Object dataValues[][];

	int rowcount;

	int columncount = 7;

	//TODO: vedere se riesce ad accettare tutti i tipi di risorse
	//private ArrayList<GnuPlotFileElement> gnuPlotFileList;
	private ArrayList<Object> myResource;

	/**
	 * Permette di settare tramite i valori delle Mail all'iterno del modello
	 * della tabella.
	 *
	 * @param mail_list
	 */
	public void set_FileTableModel(ArrayList<Object> resourceTable) {

		this.myResource = resourceTable;

		// Create some data
		Object dataValues_app[][] = new Object[resourceTable.size()][columncount];

		rowcount = resourceTable.size();

		dataValues = dataValues_app;

		for (int i = 0; i < resourceTable.size(); i++) {
			if (resourceTable.get(i) instanceof GameResourceMobile){
				GameResourceMobile resourceMobile = (GameResourceMobile) resourceTable.get(i);
			//"Type","Id","Description","Quantity","x","y","z"
				dataValues[i][0] = "Mobile";
				dataValues[i][1] = resourceMobile.getId();
				dataValues[i][2] = resourceMobile.getDescription();
				dataValues[i][3] = resourceMobile.getQuantity();
				dataValues[i][4] = resourceMobile.getX();
				dataValues[i][5] = resourceMobile.getY();
				dataValues[i][6] = resourceMobile.getZ();
			}
			else if (resourceTable.get(i) instanceof GameResourceEvolve){
				GameResourceEvolve resourceEvolve = (GameResourceEvolve) resourceTable.get(i);
				dataValues[i][0] = "Evolve";
				dataValues[i][1] = resourceEvolve.getId();
				dataValues[i][2] = resourceEvolve.getDescription();
				dataValues[i][3] = resourceEvolve.getQuantity();
				dataValues[i][4] = "off" + resourceEvolve.getOffset();
				dataValues[i][5] = "per" + resourceEvolve.getPeriod();
				dataValues[i][6] = "---";
			}
			else if (resourceTable.get(i) instanceof GameResource){
				GameResource resource = (GameResource) resourceTable.get(i);

				dataValues[i][0] = "Resource";
				dataValues[i][1] = resource.getId();
				dataValues[i][2] = resource.getDescription();
				dataValues[i][3] = resource.getQuantity();
				dataValues[i][4] = "---";
				dataValues[i][5] = "---";
				dataValues[i][6] = "---";
			}

		}

//		Set<String> key_set = registeredUserTable.keySet();
//		Iterator<String> iter = key_set.iterator();
//		int i=0;
//		while(iter.hasNext()){
//			String key = iter.next();
//			RegisteredUser reg = registeredUserTable.get(key);
//
//			dataValues[i][0] = reg.getUsername();
//			dataValues[i][1] = reg.getPassword();
//			dataValues[i][2] = reg.getId();
//
//			i++;
//		}

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