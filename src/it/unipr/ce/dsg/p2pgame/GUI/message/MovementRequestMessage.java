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
public class MovementRequestMessage extends Message{

    private double target_x;
    private double target_y;
    private String resId;
     private String threadId;
    public MovementRequestMessage(double tx,double ty,String resID,String threadID)
    {
        super("","",0);
        this.setMessageType("MOVEMENTREQUEST");
        this.PARAMETERS_NUM=7;

        this.target_x=tx;
        this.target_y=ty;
        this.resId=resID;
        this.threadId=threadID;


        this.getParametersList().add(new Parameter("p_x",Double.toString(tx)));
        this.getParametersList().add(new Parameter("p_y",Double.toString(ty)));
        this.getParametersList().add(new Parameter("resId",resID));
        this.getParametersList().add(new Parameter("threadId",threadID));

    }

    public MovementRequestMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("MOVEMENTREQUEST");
        this.PARAMETERS_NUM=7;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.target_x=Double.parseDouble(this.getParametersList().get(3).getValue());
        this.target_y=Double.parseDouble(this.getParametersList().get(4).getValue());
        this.resId=this.getParametersList().get(5).getValue();
        this.threadId=this.getParametersList().get(6).getValue();


    }


     public double getX()
    {
        return this.target_x;
    }


    public double getY()
    {
        return this.target_y;
    }

    public String getResID()
    {
        return this.resId;
    }

    public String getThreadID()
    {
        return this.threadId;
    }

    public void setX(double x)
    {
        this.target_x=x;
    }

    public void setY(double y)
    {
        this.target_y=y;
    }

    public void setResID(String resId)
    {
        this.resId=resId;
    }

    public void setThreadID(String threadID)
    {
        this.threadId=threadID;
    }




}
