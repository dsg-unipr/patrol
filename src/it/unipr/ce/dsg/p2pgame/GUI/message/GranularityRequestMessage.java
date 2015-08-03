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
public class GranularityRequestMessage extends Message{

    public GranularityRequestMessage()
    {
         super("","",0);
        this.setMessageType("GRANULARITYREQUEST");
        this.PARAMETERS_NUM=3;
    }

    public GranularityRequestMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("GRANULARITYREQUEST");
        this.PARAMETERS_NUM=3;
    }



}


