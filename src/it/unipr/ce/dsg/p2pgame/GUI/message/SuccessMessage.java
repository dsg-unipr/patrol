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
public class SuccessMessage extends Message{

    private boolean success;

    public SuccessMessage(boolean success)
    {
         super("","",0);
        this.setMessageType("SUCCESSMESSAGE");
        this.PARAMETERS_NUM=4;

        this.success=success;
        this.getParametersList().add(new Parameter("success",Boolean.toString(success)));

    }

    public SuccessMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("SUCCESSMESSAGE");
        this.PARAMETERS_NUM=4;

         for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.success=Boolean.parseBoolean(this.getParametersList().get(3).getValue());

    }


    public boolean getSuccess()
    {
        return this.success;
    }

    public void setSuccess(boolean succ)
    {
        this.success=succ;
    }
    



}
