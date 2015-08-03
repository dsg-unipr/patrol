package it.unipr.ce.dsg.p2pgame.platform.test;

import java.io.IOException;

import it.unipr.ce.dsg.p2pgame.platform.GameServer;

public class GameTest {

	public static void main(String[] args) {

		System.out.println("GAME SERVER");

		try {
			//ServerMonitor sm = new ServerMonitor();

//			 //Schedule a job for the event-dispatching thread:
//	        //creating and showing this application's GUI.
//	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//	            public void run() {
//	                createAndShowGUI();
//	            }
//	        });


			final GameServer gs = new GameServer(1234, 1235, 5, 160, 1111, 2222, 0,0,0, 10,10,10, 1,2,1);
			//GameServer gs = new GameServer(1234, 1235, 5, 4, 1111, 2222);

			gs.getRegisteredUser();
//			javax.swing.SwingUtilities.invokeLater(new Runnable() {
//				public void run() {
//					ServerMonitor sm = new ServerMonitor(gs);
//				}
//			});

		} catch (IOException e) {

			e.printStackTrace();
		}


	}
}
