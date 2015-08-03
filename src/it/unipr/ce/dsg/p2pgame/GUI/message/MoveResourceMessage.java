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

public class MoveResourceMessage extends Message{


    private double x;
    private double y;
    private double z;
    private String resourceId;
    private String threadId;
    public MoveResourceMessage(String resourceID,double x,double y,double z,String threadID)
    {
         super("","",0);
        this.setMessageType("MOVERESOURCE");
        this.PARAMETERS_NUM=8;

        this.x=x;
        this.y=y;
        this.z=z;
        this.resourceId=resourceID;
        this.threadId=threadID;
        this.getParametersList().add(new Parameter("p_x",Double.toString(x)));
        this.getParametersList().add(new Parameter("p_y",Double.toString(y)));
        this.getParametersList().add(new Parameter("p_z",Double.toString(z)));
        this.getParametersList().add(new Parameter("resourceid",this.resourceId));
        this.getParametersList().add(new Parameter("threadid",this.threadId));




    }

    public MoveResourceMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("MOVERESOURCE");
        this.PARAMETERS_NUM=8;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.x=Double.parseDouble(this.getParametersList().get(3).getValue());
        this.y=Double.parseDouble(this.getParametersList().get(4).getValue());
        this.z=Double.parseDouble(this.getParametersList().get(5).getValue());
        this.resourceId=this.getParametersList().get(6).getValue();
        this.threadId=this.getParametersList().get(7).getValue();


    }

    public double getX()
    {
        return this.x;
    }

    public void setX(double x)
    {
        this.x=x;
    }


    public double getY()
    {
        return this.y;
    }

    public void setY(double y)
    {
        this.y=y;
    }

     public double getZ()
    {
        return this.z;
    }

    public void setZ(double z)
    {
        this.z=z;
    }

    public String getResID()
    {
        return this.resourceId;
    }

    public void setResID(String id)
    {
        this.resourceId=id;
    }

    public String getThreadID()
    {
        return this.threadId;
    }

    public void setThreadID(String id)
    {
        this.threadId=id;
    }
}
