package it.unipr.ce.dsg.p2pgame.GUI.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;

/**
 *@author jose murga 
 * */
public class SetMobileResourceStatusRequestMessage extends Message{
	
	private String id;
	private String status;
	
	public SetMobileResourceStatusRequestMessage(String id,String status)
	{
		super("","",0);

        this.setMessageType("SETMOBILERESOURCESTATUSREQUEST");
        this.PARAMETERS_NUM=5;

		this.id=id;
		this.status=status;
		
		this.getParametersList().add(new Parameter("id",id));
		this.getParametersList().add(new Parameter("status",status));
        
		
	}
	
	public SetMobileResourceStatusRequestMessage(Message message)
	{
		
		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
		
		this.setMessageType("SETMOBILERESOURCESTATUSREQUEST");
        this.PARAMETERS_NUM=5;
        
        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}
        
        this.id=this.getParametersList().get(3).getValue();
        this.status=this.getParametersList().get(4).getValue();
        
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
