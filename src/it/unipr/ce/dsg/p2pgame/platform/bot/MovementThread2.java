package it.unipr.ce.dsg.p2pgame.platform.bot;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import java.io.File;

import it.unipr.ce.dsg.p2pgame.GUI.prolog.MovementEngine;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobile;
import it.unipr.ce.dsg.p2pgame.platform.bot.message.*;


public class MovementThread2 implements Runnable{

	private Thread runner;
	private String resid="";
	private InterfaceBot mybot;
	private double targetx,targety;
	private int period;
	private double xx,yy,zz;
	private double visResource=2;
	private double radiusPlanet=10;
	
	private MessageSender sender;
	
	
	FileOutputStream fos;
	File file;
	PrintStream ps;
	public MovementThread2(InterfaceBot mybot,String resid,double targetx,double targety,int period,int portnumber)
	{
		//runner=new Thread(this);
		this.resid=resid;
		this.mybot=mybot;
		this.targetx=targetx;
		this.targety=targety;
		this.period=period;
		//runner.start();
		//file=new File("log/"+this.resid+".txt");
		//try {
			//fos=new FileOutputStream(file,true);
			//ps=new PrintStream(fos);
		//} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
		
		this.sender=new MessageSender(portnumber);
		
	}
	
	@Override
	public void run() {
		
		String rid=new String(this.resid);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try{
		
		String res_id=this.resid;
		
		GameResourceMobile grm=this.sender.getMobileResource(res_id);
		
		double currentx=grm.getX();
		double currenty=grm.getY();
		
		double previousx=0;
		double previousy=0;
		
		ArrayList<Integer> vis=new ArrayList<Integer>(); // da modificare relativo ad ostacoli per ora non serve
		vis.add(new Integer(0));
		vis.add(new Integer(0));
		vis.add(new Integer(0));
		vis.add(new Integer(0));
		vis.add(new Integer(0));
		vis.add(new Integer(0));
		vis.add(new Integer(0));
		vis.add(new Integer(0));
		
		
		
		
		this.sender.setMobileReourceStatus(res_id, true);
		
		
		
		while((currentx!=this.targetx)||(currenty!=this.targety)&&this.mybot.getGameBand())
		{
			
				Thread.sleep(this.period);
				//ps.println("X="+currentx+" Y="+currenty);
				//ps.println("PRIMA DI RULE");
				MovementEngine me=new MovementEngine("rules/movementTheory.pl");
				//me.clearParameters("rules/movementTheory.pl");
				me.createMovementTheory((int)currentx, (int)currenty, (int)this.targetx, (int)this.targety,(int) previousx, (int)previousy, vis);
				//ps.println("DOPO DI RULE");
				//System.out.println(grm.getId()+" "+currentx+" "+currenty);
				
				int movx=me.longitudeMovement();
				int movy=me.latitudeMovement();
				
				int movX=0;
				int movY=0;
				
				if(movx==1)//verso ovest
				{
					//currentx--;
					movX=-1;
					previousx=-1;
					
				}
				else if(movx==2) //verso est
				{
					//currentx++;
					movX=1;
					previousx=1;
					
				}
				else if(movx==0)//non mi muovo in orizontale
				{
					movX=0;
					previousx=0;
				
				}
				
				
				if(movy==1)//verso nord
				{
					//currenty--;
					movY=-1;
					previousy=-1;
				}
				else if(movy==2)//verso sud
				{
					//currenty++;
					movY=1;
					previousy=1;
					
				}
				else if(movy==0)//non mi muovo in verticale
				{
					movY=0;
					previousy=0;
					
				}
					
				//ps.println("***********THREAD "+this.resid+" movX="+movX+" movY="+movY);
				//grm.setX(currentx);
				//grm.setY(currenty);
				//this.mybot.getMyGamePeer().moveResourceMobile(grm.getId(), movX, movY, 0, this.mybot.getMyGamePeer().getMyThreadId());
				//****metodo del bot
				
				//fare messaggi per richiedere lo spostamento
				
				//this.mybot.moveResourceMobile(resid, movX, movY, 0);
				
				this.sender.moveMobileResource(resid, movX, movY);
				
				//grm=this.mybot.getResourceMobilebyID(this.resid);
				grm=this.sender.getMobileResource(resid);
				currentx=grm.getX();
				currenty=grm.getY();
			
		}
		
		//fine movimento
		//this.mybot.setResourceStatus(this.resid, false);
		
		if(!this.mybot.getGameBand())
		{
			
		}
		else
		{
			this.sender.setMobileReourceStatus(resid, false);
			System.out.println("arrived "+resid+": x=" +targetx+" y= "+targety);

			
		}
		
		}catch(Exception e)
		{
			//qua va metodo di
			System.out.println("EXCEPTION"+rid);
			e.printStackTrace();
			//this.mybot.setResourceStatus(this.resid, false);
			this.sender.setMobileReourceStatus(rid, false);	
			//e.printStackTrace();
			
		}
	}

}
