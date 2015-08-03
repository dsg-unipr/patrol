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
public class GamePlayerMessage extends Message{
    
    private String id;
    private String name;
    private double posX;
    private double posY;
    private double posZ;
    private double velocity;
    private double visibility;

    public GamePlayerMessage(String id, String name, double posX, double posY, double posZ, double vel, double visibility)
    {

        super("","",0);
        this.setMessageType("GPLAYER");
        this.PARAMETERS_NUM=10;

        this.id=id;
        this.name=name;
        this.posX=posX;
        this.posY=posY;
        this.posZ=posZ;
        this.velocity=vel;
        this.visibility=visibility;

        this.getParametersList().add(new Parameter("id", this.id));
        this.getParametersList().add(new Parameter("name", this.name));
        this.getParametersList().add(new Parameter("posx", Double.toString(this.posX)));
        this.getParametersList().add(new Parameter("posy", Double.toString(this.posY)));
        this.getParametersList().add(new Parameter("posz", Double.toString(this.posZ)));
        this.getParametersList().add(new Parameter("velocity", Double.toString(this.velocity)));
        this.getParametersList().add(new Parameter("visibility", Double.toString(this.visibility)));

         

    }

    public GamePlayerMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("GPLAYER");
        this.PARAMETERS_NUM=10;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.id=this.getParametersList().get(3).getValue();
        this.name=this.getParametersList().get(4).getValue();
        this.posX=Double.parseDouble(this.getParametersList().get(5).getValue());
        this.posY=Double.parseDouble(this.getParametersList().get(6).getValue());
        this.posZ=Double.parseDouble(this.getParametersList().get(7).getValue());
        this.velocity=Double.parseDouble(this.getParametersList().get(8).getValue());
        this.visibility=Double.parseDouble(this.getParametersList().get(9).getValue());

    }

    public String getID()
    {
        return this.id;

    }

    public void setID(String id)
    {
        this.id=id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name=name;
    }

    public double getPosX()
    {
        return this.posX;
    }

    public void setPosX(double posx)
    {
        this.posX=posx;
    }

    public double getPosY()
    {
        return this.posY;
    }

    public void setPosY(double posy)
    {
        this.posY=posy;
    }

    public double getPosZ()
    {
        return this.posZ;
    }

    public void setPosZ(double posz)
    {
        this.posZ=posz;
    }

    public double getVelocity()
    {
        return this.velocity;
    }

    public void setVelocity(double vel)
    {
        this.velocity=vel;
    }

    public double getVisibility()
    {
        return this.visibility;
    }

    public void setVisibility(double vis)
    {
        this.visibility=vis;
    }


}
