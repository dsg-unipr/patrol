package it.unipr.ce.dsg.p2pgame.GUI.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;

public class LoggedUsersRequestMessage extends Message{

	public LoggedUsersRequestMessage()
	{
		super("","",0);
        this.setMessageType("LOGGEDUSERSREQUEST");
        this.PARAMETERS_NUM=3;
		
	}
	
	public LoggedUsersRequestMessage(Message message)
	{
		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("LOGGEDUSERSREQUEST");
        this.PARAMETERS_NUM=3;
	}
}
