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

public class StartMessage extends Message{

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private double minZ;
    private double maxZ;
    private double vel;
    private double gran;
    private double vis;
    public StartMessage(double minX, double maxX, double minY, double maxY, double minZ, double maxZ, double vel, double vis, double gran)
    {
        super("","",0);
        this.setMessageType("START");
        this.PARAMETERS_NUM=12;

        this.minX=minX;
        this.maxX=maxX;
        this.minY=minY;
        this.maxY=maxY;
        this.minZ=minZ;
        this.maxZ=maxZ;
        this.vel=vel;
        this.vis=vis;
        this.gran=gran;

        this.getParametersList().add(new Parameter("minx",Double.toString(this.minX)));
        this.getParametersList().add(new Parameter("maxx",Double.toString(this.maxX)));
        this.getParametersList().add(new Parameter("miny",Double.toString(this.minY)));
        this.getParametersList().add(new Parameter("maxy",Double.toString(this.maxY)));
        this.getParametersList().add(new Parameter("minz",Double.toString(this.minZ)));
        this.getParametersList().add(new Parameter("maxz",Double.toString(this.maxZ)));        
        this.getParametersList().add(new Parameter("vel",Double.toString(this.vel)));
        this.getParametersList().add(new Parameter("vis",Double.toString(this.vis)));
        this.getParametersList().add(new Parameter("gran",Double.toString(this.gran)));

 

    }

    public StartMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("START");
        this.PARAMETERS_NUM=12;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.minX=Double.parseDouble(this.getParametersList().get(3).getValue());
        this.maxX=Double.parseDouble(this.getParametersList().get(4).getValue());
        this.minY=Double.parseDouble(this.getParametersList().get(5).getValue());
        this.maxY=Double.parseDouble(this.getParametersList().get(6).getValue());
        this.minZ=Double.parseDouble(this.getParametersList().get(7).getValue());
        this.maxZ=Double.parseDouble(this.getParametersList().get(8).getValue());
        this.vel=Double.parseDouble(this.getParametersList().get(9).getValue());
        this.vis=Double.parseDouble(this.getParametersList().get(10).getValue());
        this.gran=Double.parseDouble(this.getParametersList().get(11).getValue());



    }

    public double getMinX()
    {
        return this.minX;
    }

    public void setMinX(double minx)
    {
        this.minX=minx;
    }


    public double getMaxX()
    {
        return this.maxX;
    }

    public void setMaxX(double maxx)
    {
        this.maxX=maxx;
    }

    public double getMinY()
    {
        return this.minY;
    }

    public void setMinY(double miny)
    {
        this.minY=miny;
    }

    public double getMaxY()
    {
        return this.maxY;
    }

    public void setMaxY(double maxy)
    {
        this.maxY=maxy;
    }

    public double getMinZ()
    {
        return this.minZ;
    }

    public void setMinZ(double minz)
    {
        this.minZ=minz;
    }

    public double getMaxZ()
    {
        return this.maxZ;
    }

    public void setMaxZ(double maxz)
    {
        this.maxZ=maxz;
    }

    public double getVel()
    {
        return this.vel;
    }

    public void setVel(double vel)
    {
        this.vel=vel;
    }

    public double getGran()
    {
        return this.gran;
    }

    public void setGran(double gran)
    {
        this.gran=gran;
    }

    public double getVis()
    {
        return this.vis;
    }

    public void setVis(double vis)
    {
        this.vis=vis;
    }

}
