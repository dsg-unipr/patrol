package it.unipr.ce.dsg.p2pgame.platform.bot;

public class RTSGameMainBot {
	
	public static void main(String [] arg)
	{
		
		Thread th=new Thread(new RTSGameBot2("profiles/profile1.txt","conf/gameconf.txt",6891,"jose",9999));
		th.start();
		
	}

}
