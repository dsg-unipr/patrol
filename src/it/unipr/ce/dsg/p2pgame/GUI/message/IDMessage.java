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

public class IDMessage  extends Message{

    private String id;
    public IDMessage(String  id)
    {
         super("","",0);
        this.setMessageType("PEERID");
        this.PARAMETERS_NUM = 4;

        this.id=id;
        this.getParametersList().add(new Parameter("peerId", id));


    }
    
    public IDMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("PEERID");
        this.PARAMETERS_NUM = 4;
        
         for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}
        
        this.id=this.getParametersList().get(3).getValue();
        
    }



    public String getID()
    {
        return this.id;
    }

    public void setID(String id)
    {
        this.id=id;
    }

}
