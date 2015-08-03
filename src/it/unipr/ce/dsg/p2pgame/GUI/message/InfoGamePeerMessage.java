/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unipr.ce.dsg.p2pgame.GUI.message;

/**
 *
 * @author pelito
 */
import it.simplexml.message.Message;
import it.simplexml.message.Parameter;
public class InfoGamePeerMessage extends Message{

    private String id;
    private String name;
    private double posx;
    private double posy;
    private double posz;
    private double velocity;
    private double visibility;

    public InfoGamePeerMessage(String id,String name,double posx,double posy,double posz,double vel,double vis)
    {
         super("","",0);

        this.setMessageType("GAMEPEERINFO");
        this.PARAMETERS_NUM=10;

        this.id=id;
        this.name=name;
        this.posx=posx;
        this.posy=posy;
        this.posz=posz;
        this.velocity=vel;
        this.visibility=vis;

        this.getParametersList().add(new Parameter("playerid",id));
        this.getParametersList().add(new Parameter("playername",name));
        this.getParametersList().add(new Parameter("posx",Double.toString(posx)));
        this.getParametersList().add(new Parameter("posy",Double.toString(posy)));
        this.getParametersList().add(new Parameter("posz",Double.toString(posz)));
        this.getParametersList().add(new Parameter("velocity",Double.toString(vel)));
        this.getParametersList().add(new Parameter("visibility",Double.toString(vis)));




    }
    
    public InfoGamePeerMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("GAMEPEERINFO");
        this.PARAMETERS_NUM=10;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.id=this.getParametersList().get(3).getValue();
        this.name=this.getParametersList().get(4).getValue();
        this.posx=Double.parseDouble(this.getParametersList().get(5).getValue());
        this.posy=Double.parseDouble(this.getParametersList().get(6).getValue());
        this.posz=Double.parseDouble(this.getParametersList().get(7).getValue());
        this.velocity=Double.parseDouble(this.getParametersList().get(8).getValue());
        this.visibility=Double.parseDouble(this.getParametersList().get(9).getValue());


    }




    public String getPlayerID()
    {
        return this.id;
    }

    public void setPlayerID(String id)
    {
        this.id=id;
    }

    public String getPlayerName()
    {
        return this.name;
    }

    public void setPlayerName(String name)
    {
        this.name=name;
    }

    public double getPosX()
    {
        return this.posx;
    }

    public void setPosX(double x)
    {
        this.posx=x;
    }

    public double getPosY()
    {
        return this.posy;
    }

    public void setPosY(double y)
    {
        this.posy=y;
    }

     public double getPosZ()
    {
        return this.posz;
    }

     public void setPosZ(double z)
    {
        this.posz=z;
    }

    public double getVel()
    {
        return this.velocity;
    }

    public void getVel(double vel)
    {
        this.velocity=vel;
    }


    public double getVis()
    {
        return this.visibility;
    }

    public void setVis(double vis)
    {
        this.visibility=vis;
    }


}
