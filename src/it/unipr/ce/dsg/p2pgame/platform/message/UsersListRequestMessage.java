package it.unipr.ce.dsg.p2pgame.platform.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;

/**
*
* This class provide the structure for message used for
* require the list of the logged users in the game session 
*
* @author Jose' Murga (joseraul.murgavasquez@studenti.unipr.it)
*
*/

public class UsersListRequestMessage extends Message{

	

	public UsersListRequestMessage(String sourceName, String sourceSocketAddr,
			int sourcePort) {
		super(sourceName, sourceSocketAddr, sourcePort);
		this.setMessageType("USERSLISTREQUEST");
		this.PARAMETERS_NUM=3;
		
	}
	
	public UsersListRequestMessage(Message message) {
		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
		this.setMessageType("USERSLISTREQUEST");
		this.PARAMETERS_NUM=3;
		
	}
	
	
	
	

}
