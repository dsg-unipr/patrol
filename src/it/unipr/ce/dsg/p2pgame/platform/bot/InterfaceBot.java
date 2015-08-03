package it.unipr.ce.dsg.p2pgame.platform.bot;

import java.util.ArrayList;
import java.util.HashMap;

import it.unipr.ce.dsg.p2pgame.platform.GamePeer;
import it.unipr.ce.dsg.p2pgame.platform.GameResource;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobile;

public interface InterfaceBot {

	GameResourceMobile getResourceMobilebyID(String resid);

	void setMovStatus(String resid, boolean b);

	int getProbattack();

	VirtualResource getVResourcebyCoordinates(int k, int j);

	String getOwnerid();

	
	GamePeer getMyGamePeer();
	
	ArrayList<VirtualResource> getPlanets();
	
	void setPlanetOwner(String idPlanet,String idOwner,String nameOwner);
	
	VirtualResource getPlanetbyID(String idPlanet);

	boolean createResource(String idRes);
	
	GameResource getLastGameResource();
	
	void UpdateLoggedUsers();
	
	HashMap<String,UserInfo> getLoggedUsers();
	
	UserInfo getLoggedUserInfo(String id);
	
	void setResourceStatus(String id,boolean status);
	
	void moveResourceMobile(String resid, int movX,int  movY, int movZ);
	
	boolean getGameBand();
	void setGameBand(boolean band);
	
}
