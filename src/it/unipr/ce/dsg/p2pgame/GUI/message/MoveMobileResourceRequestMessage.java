package it.unipr.ce.dsg.p2pgame.GUI.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;
public class MoveMobileResourceRequestMessage extends Message{
	
	private int movX;
	private int movY;
	private String resID;
	
	
	public MoveMobileResourceRequestMessage(String resid,int movX,int movY)
	{
		super("","",0);
        this.setMessageType("MOVEMOBILERESOURCEREQUEST");
        this.PARAMETERS_NUM=6;
        
        this.resID=resid;
        
        this.movX=movX;
        this.movY=movY;
        
        this.getParametersList().add(new Parameter("resId",resID));
        
        this.getParametersList().add(new Parameter("movX",Integer.toString(movX)));
        this.getParametersList().add(new Parameter("movY",Integer.toString(movY)));
        
        
		
		
	}
	
	public MoveMobileResourceRequestMessage(Message message)
	{
		
		super("","",0);
        this.setMessageType("MOVEMOBILERESOURCEREQUEST");
        this.PARAMETERS_NUM=6;
        
        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}
        
        this.resID=this.getParametersList().get(3).getValue();
        
		this.movX=Integer.parseInt(this.getParametersList().get(4).getValue());
		this.movY=Integer.parseInt(this.getParametersList().get(5).getValue());
	}

	public int getMovX() {
		return movX;
	}

	public void setMovX(int movX) {
		this.movX = movX;
	}

	public int getMovY() {
		return movY;
	}

	public void setMovY(int movY) {
		this.movY = movY;
	}

	public String getResID() {
		return resID;
	}

	public void setResID(String resID) {
		this.resID = resID;
	}

		
	

}
