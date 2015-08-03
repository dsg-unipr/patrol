package it.unipr.ce.dsg.p2pgame.platform.bot;

import java.util.ArrayList;

import it.unipr.ce.dsg.p2pgame.GUI.prolog.MovementEngine;

public class testmovement {
	
	public static void main(String []arg)
	{
		
		

		
		
		Thread move=new Thread(new Runnable(){

			
			public void run() {
				
				MovementEngine me=new MovementEngine("rules/movementTheory.pl");
				
				int cx=10;
				int cy=0;
				
				int tx=5;
				
				int ty=5;
				
				//previous mov
				int px=0;
				int py=0;
				
				//visibility
				ArrayList<Integer> vis=new ArrayList<Integer>();
				vis.add(new Integer(0));
				vis.add(new Integer(0));
				vis.add(new Integer(0));
				vis.add(new Integer(0));
				vis.add(new Integer(0));
				vis.add(new Integer(0));
				vis.add(new Integer(0));
				vis.add(new Integer(0));
				
				//in questo punto devo ricavare le coordinate di inizio e destinazio
				System.out.println("Current position x: "+cx+" y: "+cy);
				System.out.println("Target position x: "+tx+" y: "+ty);
				
				while((cx!=tx)||(cy!=ty))
				{
					try {
						Thread.sleep(1000);
						
						me.clearParameters("rules/movementTheory.pl");
						me.createMovementTheory(cx, cy, tx, ty, px, py, vis);
						
						int m=me.nextMovement();
						
						if(m==1)//nord
						{
							cx--;
							px=-1;
							py=0;
						}
						else if(m==2)
						{
							cx++;
							px=1;
							py=0;
							
						}
						else if(m==3)
						{
							cy++;
							py=1;
							px=0;
							
						}
						else if(m==4)
						{
							cy--;
							py=-1;
							px=0;
							
						}
							
						System.out.println("\nCurrent position x: "+cx+" y: "+cy);
						
						
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
					
				}
				System.out.println("Arrived");
			}
			
			
			
		}
				
		);
		move.start();
		
	}

}
