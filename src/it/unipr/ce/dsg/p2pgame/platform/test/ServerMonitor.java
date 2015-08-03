package it.unipr.ce.dsg.p2pgame.platform.test;

import it.unipr.ce.dsg.p2pgame.platform.GameServer;
import it.unipr.ce.dsg.p2pgame.platform.RegisteredUser;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class ServerMonitor {

	private JFrame frame;
	//private JTabbedPane tabContainer;

	private GameServer server;

//	private JComponent tab1;
//	private JComponent tab2;

//	JFrame frame = new JFrame("Tabbed Pane Frame");
//    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    JTabbedPane tab = new JTabbedPane();
//    frame.add(tab, BorderLayout.CENTER);
//    JButton button = new JButton("1");
//    tab.add("Tab 1", button);
//    button = new JButton("2");
//    tab.add("Tab 2", button);
//    frame.setSize(400,400);
//    frame.setVisible(true);

	public ServerMonitor(GameServer server) {
		super();

		this.server = server;

		this.frame = new JFrame("Server Monitor");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tab = new JTabbedPane();
		frame.add(tab, BorderLayout.CENTER);

		JComponent compo = this.createNewTab("Tab1");
		tab.addTab("Registered User", null, compo, "show registered user");

		compo = this.createNewTab("Tab2");
		tab.addTab("Logged User", null, compo, "show logged user");

		this.frame.pack();
		this.frame.setVisible(true);
	}

	private JPanel createNewTab(String name){
		JPanel panel = new JPanel(false);
//		TableModel dataModel = new AbstractTableModel() {
//	          public int getColumnCount() { return 10; }
//	          public int getRowCount() { return 10;}
//	          public Object getValueAt(int row, int col) { return new Integer(row*col); }
//	      };
//	      JTable table = new JTable(dataModel);
//	      JScrollPane scrollpane = new JScrollPane(table);


		HashMap<String, String> logged = this.server.getLoggedInUser();
		HashMap<String, RegisteredUser> registered = this.server.getRegisteredUser();

		TableModel dataModelLogUser = new AbstractTableModel() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;
			public int getColumnCount() { return 2; }
			public int getRowCount() { return 2; }
			public Object getValueAt(int row, int col) { return new Integer(row*col); }
		};


		JTable table = new JTable(dataModelLogUser);


		JScrollPane scrollpane = new JScrollPane(table);
		//JLabel label = new JLabel(name);
		panel.add(scrollpane);
		//panel.add(label);
		return panel;
	}


//	 /**
//     * Create the GUI and show it.  For thread safety,
//     * this method should be invoked from the
//     * event-dispatching thread.
//     */
//    private static void createAndShowGUI() {
//        //Create and set up the window.
//        JFrame frame = new JFrame("HelloWorldSwing");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        //Add the ubiquitous "Hello World" label.
//        JLabel label = new JLabel("Hello World");
//        frame.getContentPane().add(label);
//
//        //Display the window.
//        frame.pack();
//        frame.setVisible(true);
//    }

}
