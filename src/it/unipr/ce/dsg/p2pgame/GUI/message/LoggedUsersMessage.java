package it.unipr.ce.dsg.p2pgame.GUI.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;
public class LoggedUsersMessage extends Message{

	private String content;
	
	public LoggedUsersMessage(String content)
	{
		super("","",0);
        this.setMessageType("LOGGEDUSERS");
        this.PARAMETERS_NUM=4;
        
        this.content=content;
        
        this.getParametersList().add(new Parameter("content",this.content));
		
	}
	
	public LoggedUsersMessage(Message message)
	{
		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("LOGGEDUSERS");
        this.PARAMETERS_NUM=4;
        
        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}
        
        this.content=this.getParametersList().get(3).getValue();
		
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
