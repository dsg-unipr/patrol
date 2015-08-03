package it.unipr.ce.dsg.p2pgame.platform.bot.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;
public class BotStartMatchRequestMessage extends Message {

	private String id;
	private String resId;
	private String otherresId;
	private String timeStamp;
	



	public BotStartMatchRequestMessage(String id,String resId,String otherresId,String timeStamp)
	{
		super("","",0);
		
		this.setMessageType("BOTSTARTMATCHREQUEST");
		this.PARAMETERS_NUM=7;
		
		this.getParametersList().add(new Parameter("id", id));
		this.getParametersList().add(new Parameter("resId", resId));
		this.getParametersList().add(new Parameter("otherresId", otherresId));
		this.getParametersList().add(new Parameter("timeStamp", timeStamp));
		
		this.id=id;
		this.resId=resId;
		this.otherresId=otherresId;
		this.timeStamp=timeStamp;
	}
	
	public BotStartMatchRequestMessage(Message message)
	{
		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
		
		this.setMessageType("BOTSTARTMATCH");
		this.PARAMETERS_NUM=7;
		
		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}
		
		this.id=this.getParametersList().get(3).getValue();
		this.resId=this.getParametersList().get(4).getValue();
		this.otherresId=this.getParametersList().get(5).getValue();
		this.timeStamp=this.getParametersList().get(6).getValue();
		
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
}
