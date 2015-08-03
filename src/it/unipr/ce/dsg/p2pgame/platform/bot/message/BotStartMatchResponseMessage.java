package it.unipr.ce.dsg.p2pgame.platform.bot.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;
public class BotStartMatchResponseMessage extends Message{
	
	private String id;
	private String resId;
	private String otherresId;
	private String timeStamp;
	private boolean inMatch;
	private boolean decision;
	


	public BotStartMatchResponseMessage(String id,String resId,String otherresId,String timeStamp,boolean inMatch,boolean decision){
		
		super("","",0);
		
		this.setMessageType("BOTSTARTMATCH");
		this.PARAMETERS_NUM=9;
		
		this.getParametersList().add(new Parameter("id", id));
		this.getParametersList().add(new Parameter("resId", resId));
		this.getParametersList().add(new Parameter("otherresId", otherresId));
		this.getParametersList().add(new Parameter("timeStamp", timeStamp));
		this.getParametersList().add(new Parameter("inMatch", Boolean.toString(inMatch)));
		this.getParametersList().add(new Parameter("decision", Boolean.toString(decision)));
		
		this.id=id;
		this.resId=resId;
		this.otherresId=otherresId;
		this.timeStamp=timeStamp;
		this.inMatch=inMatch;
		this.decision=decision;
		
	}
	
	public BotStartMatchResponseMessage(Message message)
	{
		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
		
		this.setMessageType("BOTSTARTMATCH");
		this.PARAMETERS_NUM=9;
		
		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}
		
		this.id=this.getParametersList().get(3).getValue();
		this.resId=this.getParametersList().get(4).getValue();
		this.otherresId=this.getParametersList().get(5).getValue();
		this.timeStamp=this.getParametersList().get(6).getValue();
		this.inMatch=Boolean.parseBoolean(this.getParametersList().get(7).getValue());
		this.decision=Boolean.parseBoolean(this.getParametersList().get(8).getValue());
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	public String getOtherresId() {
		return otherresId;
	}
	public void setOtherresId(String otherresId) {
		this.otherresId = otherresId;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public boolean getInMatch() {
		return inMatch;
	}

	public void setInMatch(boolean decision) {
		this.inMatch = decision;
	}
	

	public boolean getDecision() {
		return decision;
	}

	public void setDecision(boolean decision) {
		this.decision = decision;
	}
	
}
