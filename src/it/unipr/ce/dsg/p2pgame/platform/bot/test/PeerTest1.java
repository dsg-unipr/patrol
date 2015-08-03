package it.unipr.ce.dsg.p2pgame.platform.bot.test;

import it.unipr.ce.dsg.p2pgame.platform.GamePeer;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobile;

import java.util.ArrayList;

public class PeerTest1 {
	
	public static void main(String [] arg)
	{
		int portMin = 6891;
    	int serverPort = 1235;
    	String serverAdd="127.0.0.1";
		
		GamePeer gp=new GamePeer(portMin+ 1 , portMin, 160, "", serverAdd, serverPort, portMin + 3, portMin + 2, serverAdd, serverPort+2, 4000,1000,64000,2000);
		
		gp.registerOnServer("jose", "password");
		
		gp.startGame(0,575,0,575,0,0, 1,10, 5);
		
		//creo risorsa mobile
		String timestamp = Long.toString(System.currentTimeMillis());
		gp.createMobileResource("Attack" + timestamp, 40);
		
		ArrayList<Object> res=gp.getMyResources(); 
		
		GameResourceMobile grm=(GameResourceMobile)res.get(0);
		
		
		String ipAdd="";
		int port=0;
		
		
		
		//do coordinte a risorsa mobile
		
		//scontro con risorsa nemica
		
		//vedo esito del match
	}

}
