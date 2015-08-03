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
public class CreateResourceMessage extends Message
{
    private String id;
    private String description;
    private double quantity;

    public CreateResourceMessage(String id, String description, double quantity)
    {
        super("","",0);
        this.setMessageType("CREATERESOURCE");
        this.PARAMETERS_NUM=6;

        this.id=id;
        this.description=description;
        this.quantity=quantity;

        this.getParametersList().add(new Parameter("res_id",id));
        this.getParametersList().add(new Parameter("description",description));
        this.getParametersList().add(new Parameter("quantity",Double.toString(quantity)));
    }

    public CreateResourceMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("CREATERESOURCE");
        this.PARAMETERS_NUM=6;

         for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.id=this.getParametersList().get(3).getValue();
        this.description=this.getParametersList().get(4).getValue();
        this.quantity=Double.parseDouble(this.getParametersList().get(5).getValue());
        
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


}
