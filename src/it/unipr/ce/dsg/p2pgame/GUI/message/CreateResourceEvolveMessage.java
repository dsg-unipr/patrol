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
public class CreateResourceEvolveMessage extends Message{

    String id;
    String description;
    double quantity;
    long period;
    double offset;

    public CreateResourceEvolveMessage(String id, String description, double quantity, final long period, double offset)
    {

        super("","",0);
        this.setMessageType("CREATERESOURCEEVOLVE");
        this.PARAMETERS_NUM=8;

        this.id=id;
        this.description=description;
        this.quantity=quantity;
        this.period=period;
        this.offset=offset;

        this.getParametersList().add(new Parameter("res_id",id));
        this.getParametersList().add(new Parameter("description",description));
        this.getParametersList().add(new Parameter("quantity",Double.toString(quantity)));
        this.getParametersList().add(new Parameter("period",Long.toString(period)));
        this.getParametersList().add(new Parameter("offset",Double.toString(offset)));


    }

    public  CreateResourceEvolveMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("CREATERESOURCEEVOLVE");
        this.PARAMETERS_NUM=8;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.id=this.getParametersList().get(3).getValue();
        this.description=this.getParametersList().get(4).getValue();
        this.quantity=Double.parseDouble(this.getParametersList().get(5).getValue());
        this.period=Long.parseLong(this.getParametersList().get(6).getValue());
        this.offset=Double.parseDouble(this.getParametersList().get(7).getValue());

    }

    public String getResourceID()
    {
        return this.id;
    }

    public String getDescription()
    {
        return this.description;
    }

    public double getQuantity()
    {
        return this.quantity;
    }

    public long getPeriod()
    {
        return this.period;
    }

    public double getOffset()
    {
        return this.offset;
    }

    public void setResourceID(String id)
    {
        this.id=id;
    }

    public void setDescription(String desc)
    {
        this.description=desc;
    }

    public void setQuantity(double q)
    {
        this.quantity=q;
    }

    public void setPeriod(long per)
    {
        this.period=per;
    }

    public void setOffset(double offset)
    {
        this.offset=offset;
    }

}


