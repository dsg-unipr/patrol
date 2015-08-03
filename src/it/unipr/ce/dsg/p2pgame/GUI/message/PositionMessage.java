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
public class PositionMessage extends Message{

    private double x;
    private double y;
    public PositionMessage(double x,double y)
    {
         super("","",0);
        this.setMessageType("POSITION");
        this.PARAMETERS_NUM=5;

        this.x=x;
        this.y=y;

        this.getParametersList().add(new Parameter("p_x",Double.toString(x)));
        this.getParametersList().add(new Parameter("p_y",Double.toString(y)));


    }

    public PositionMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("POSITION");
        this.PARAMETERS_NUM=5;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.x=Double.parseDouble(this.getParametersList().get(3).getValue());
        this.y=Double.parseDouble(this.getParametersList().get(4).getValue());

    }

      public double getX()
    {
        return this.x;
    }


    public double getY()
    {
        return this.y;
    }

    public void setX(double x)
    {
        this.x=x;
    }

    public void setY(double y)
    {
        this.y=y;
    }

}
