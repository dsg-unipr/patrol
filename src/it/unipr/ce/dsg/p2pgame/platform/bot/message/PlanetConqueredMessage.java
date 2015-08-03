package it.unipr.ce.dsg.p2pgame.platform.bot.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;

public class PlanetConqueredMessage extends Message{
	
	private String idPlayer;
	private String idPlanet;
	private String namePlayer;
	
	public PlanetConqueredMessage(String sourceName, String sourceSocketAddr,
			int sourcePort,String idPlayer,String namePlayer,String idPlanet) {
		super(sourceName, sourceSocketAddr, sourcePort);
		this.setMessageType("PLANETCONQUERED");
		this.PARAMETERS_NUM=6;
		
		this.getParametersList().add(new Parameter("idplayer", idPlayer));
		this.getParametersList().add(new Parameter("nameplayer", idPlayer));
		this.getParametersList().add(new Parameter("idplanet", idPlanet));
		
		this.idPlayer=idPlayer;
		this.idPlanet=idPlanet;
		this.namePlayer=namePlayer;
		
		
	}
	
	
	
	public PlanetConqueredMessage(Message message) {
		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
		this.setMessageType("PLANETCONQUERED");
		this.PARAMETERS_NUM=6;
		
		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}
		
		this.idPlayer=this.getParametersList().get(3).getValue();
		this.namePlayer=this.getParametersList().get(4).getValue();
		this.idPlanet=this.getParametersList().get(5).getValue();
		
	}
	
	public String getIdPlayer() {
		return idPlayer;
	}
	public void setIdPlayer(String idPlayer) {
		this.idPlayer = idPlayer;
	}
	public String getIdPlanet() {
		return idPlanet;
	}
	public void setIdPlanet(String idPlanet) {
		this.idPlanet = idPlanet;
	}
	public String getNamePlayer()
	{
		return namePlayer;
	}
	
	public void setNamePlayer(String namePlayer)
	{
		this.namePlayer=namePlayer;
	}
	
	
	
	

}
