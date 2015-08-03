/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unipr.ce.dsg.p2pgame.GUI.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;

/**
 *
 * @author pelito
 */
public class MobileResourceFromIDMessage extends Message{

    String resource;

    public MobileResourceFromIDMessage(String resource)
    {

        super("","",0);
        this.setMessageType("GPRESOURCEMOBILE");
        this.PARAMETERS_NUM=4;

        this.resource=resource;
        this.getParametersList().add(new Parameter("resource", this.resource));

    }
    
    public MobileResourceFromIDMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("GPRESOURCEMOBILE");
        this.PARAMETERS_NUM=4;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.resource=this.getParametersList().get(3).getValue();
    }



    public String getResource()
    {
        return resource;
    }

    public void setResource(String res)
    {
        this.resource=res;
    }

}
