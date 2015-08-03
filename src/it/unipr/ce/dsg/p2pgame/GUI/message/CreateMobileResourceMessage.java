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
public class CreateMobileResourceMessage extends Message{

    private String type;
    private double quantity;
    public CreateMobileResourceMessage(String type, double qt)
    {
        super("","",0);
        this.setMessageType("CREATEMOBILERESOURCE");
        this.PARAMETERS_NUM=5;

        this.type=type;
        this.quantity=qt;
        this.getParametersList().add(new Parameter("type",type));
        this.getParametersList().add(new Parameter("quantity",Double.toString(qt)));

    }

    public CreateMobileResourceMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("CREATEMOBILERESOURCE");
        this.PARAMETERS_NUM=5;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.type=this.getParametersList().get(3).getValue();
        this.quantity=Double.parseDouble(this.getParametersList().get(4).getValue());


    }

    public String getType()
    {
        return this.type;
    }

    public double getQuantity()
    {
        return this.quantity;
    }

    public void setType(String type)
    {
        this.type=type;
    }

    public void setQuantity(double q)
    {
        this.quantity=q;
    }

}
