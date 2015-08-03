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
public class GamePeerVisionRequest extends Message{

    public GamePeerVisionRequest()
    {
        super("","",0);
        this.setMessageType("GPVISIONSREQ");
        this.PARAMETERS_NUM=3;
    }
    
    public  GamePeerVisionRequest(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("GPVISIONSREQ");
        this.PARAMETERS_NUM=3;
             
    
    }



}
