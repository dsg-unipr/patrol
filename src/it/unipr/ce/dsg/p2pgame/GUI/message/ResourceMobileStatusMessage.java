package it.unipr.ce.dsg.p2pgame.GUI.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;
public class ResourceMobileStatusMessage extends Message{

	private String status;
	
	public ResourceMobileStatusMessage(String status)
	{
		super("","",0);
        this.setMessageType("GRMSTATUS");
        this.PARAMETERS_NUM=4;
        
        this.status=status;
        this.getParametersList().add(new Parameter("status",this.status));
		
	}
	
	public ResourceMobileStatusMessage(Message message)
	{
		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("GRMSTATUS");
        this.PARAMETERS_NUM=4;
        
        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}
        
        this.status=this.getParametersList().get(3).getValue();
		
		
	}
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
