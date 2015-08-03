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
public class GranularityMessage extends Message{

    private double granularity;
    public GranularityMessage(double gran)
    {
        super("","",0);
        this.setMessageType("GRANULARITY");
        this.PARAMETERS_NUM = 4;

        this.granularity=gran;
        this.getParametersList().add(new Parameter("granularity", Double.toString(gran)));
    }

    public GranularityMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("GRANULARITY");
        this.PARAMETERS_NUM = 4;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.granularity=Double.parseDouble(this.getParametersList().get(3).getValue());

    }

    public double getGranularity()
    {
        return this.granularity;
    }

    public void setGranularity(double gran)
    {
        this.granularity=gran;
    }

}
