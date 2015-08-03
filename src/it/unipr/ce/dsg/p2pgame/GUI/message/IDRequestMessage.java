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

public class IDRequestMessage extends Message{

    public IDRequestMessage()
    {
         super("","",0);
        this.setMessageType("IDREQUEST");
        this.PARAMETERS_NUM=3;

    }
    
    public IDRequestMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("IDREQUEST");
        this.PARAMETERS_NUM=3;
    
    }



}
