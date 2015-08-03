package it.unipr.ce.dsg.p2pgame.GUI;

import it.unipr.ce.dsg.p2pgame.GUI.test.*;
import it.unipr.ce.dsg.p2pgame.RTSgame.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
			
			String configuration="conf/gameconf.txt";
			double minX=0;
			double maxX=0;
			double minY=0;
			double maxY=0;
			double minZ=0;
			double maxZ=0;
			try {
	    		//qua apro il file di configurazione e creo lo scenario
	        	File fconf=new File(configuration);
				FileInputStream fisconf=new FileInputStream(fconf);
				InputStreamReader isrconf=new InputStreamReader(fisconf);
		       	BufferedReader brconf=new BufferedReader(isrconf);
		      //minX, maxX, minY, maxY, minZ, maxZ, vel, vis, gran
		        String straux=brconf.readLine();
		        minX=Double.parseDouble(straux);
		        straux=brconf.readLine();
		        maxX=Double.parseDouble(straux);
		        straux=brconf.readLine();
		        minY=Double.parseDouble(straux);
		        straux=brconf.readLine();
		        maxY=Double.parseDouble(straux);
		        straux=brconf.readLine();
		        minZ=Double.parseDouble(straux);
		        straux=brconf.readLine();
		        maxZ=Double.parseDouble(straux);
			}
			catch(Exception e)
			{
				
				
			}
			

			final GameServer gs = new GameServer(1234, 1235, 5, 160, 1111, 1237, minX,minY,minZ, maxX,maxY,maxZ, 10,1,5);
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
