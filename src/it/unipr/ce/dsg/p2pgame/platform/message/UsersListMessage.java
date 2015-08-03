package it.unipr.ce.dsg.p2pgame.platform.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;

public class UsersListMessage extends Message{

	private String strUsersList;

	public UsersListMessage(String sourceName, String sourceSocketAddr,
			int sourcePort,String strUsersList) {
		super(sourceName, sourceSocketAddr, sourcePort);
		this.setMessageType("USERSLIST");
		this.PARAMETERS_NUM=4;
		this.getParametersList().add(new Parameter("userslist", strUsersList));
		
		this.strUsersList=strUsersList;
		
		
	}
	
	public UsersListMessage(Message message) {
		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
		this.setMessageType("USERSLIST");
		this.PARAMETERS_NUM=4;
		
		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}
		
		this.strUsersList=this.getParametersList().get(3).getValue();
		
	}

	public String getStrUsersList() {
		return strUsersList;
	}

	public void setStrUsersList(String strUsersList) {
		this.strUsersList = strUsersList;
	}
	
	

}
