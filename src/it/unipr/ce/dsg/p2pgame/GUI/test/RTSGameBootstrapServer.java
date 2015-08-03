package it.unipr.ce.dsg.p2pgame.GUI.test;

import it.unipr.ce.dsg.p2pgame.RTSgame.*;
import java.io.IOException;

import it.unipr.ce.dsg.p2pgame.platform.GameServer;

public class RTSGameBootstrapServer {

	//static MultiLog multiLog = new MultiLog("configLog.txt", true, true);

	public static void main(String[] args) {


		//MultiLog.println(RTSGameBootstrapServer.class.toString(), "Messagio di prova");



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

//			int outputPort, int inputPort, int sizeOfPeerCache,
//			int idBitLength, int gameOutPort, int gameInPort, double minX, double minY, double minZ,
//			double maxX, double maxY, double maxZ, double vis, double vel, double gran) throws IOException {

			final GameServer gs = new GameServer(1234, 1235, 5, 160, 1111, 1237, 0,0,0, 575,575,0, 10,1,5);
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
