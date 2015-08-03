package it.unipr.ce.dsg.p2pgame.GUI.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;
public class ResourceMobileStatusRequestMessage extends Message{
	
	String id;
	public ResourceMobileStatusRequestMessage(String id)
	{
		super("","",0);
        this.setMessageType("GRMSTATUSREQUEST");
        this.PARAMETERS_NUM=4;
        
        this.id=id;
        this.getParametersList().add(new Parameter("id",this.id));
		
	}
	
	public ResourceMobileStatusRequestMessage(Message message)
	{
		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("GRMSTATUSREQUEST");
        this.PARAMETERS_NUM=4;
        
        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}
        
        this.id=this.getParametersList().get(3).getValue();
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
	

}
