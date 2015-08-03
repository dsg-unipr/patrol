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
public class UpdateResourceEvolveMessage extends Message{

    private double quantity;
    public UpdateResourceEvolveMessage(double qt)
    {
        super("","",0);
        this.setMessageType("UPDATERESEVOLVEMESSAGE");
        this.PARAMETERS_NUM=4;

        this.quantity=qt;
        this.getParametersList().add(new Parameter("quantity",Double.toString(qt)));

    }

    public UpdateResourceEvolveMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("UPDATERESEVOLVEMESSAGE");
        this.PARAMETERS_NUM=4;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.quantity=Double.parseDouble(this.getParametersList().get(3).getValue());
    }


    public double getQuantity()
    {
        return this.quantity;
    }

    public void setQuantity(double qt)
    {
        this.quantity=qt;
    }

}


