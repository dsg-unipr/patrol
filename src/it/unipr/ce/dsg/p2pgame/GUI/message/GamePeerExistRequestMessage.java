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
public class GamePeerExistRequestMessage extends Message{

    public GamePeerExistRequestMessage()
    {
        super("","",0);
        this.setMessageType("GPEXISTREQUEST");
        this.PARAMETERS_NUM=3;

    }

    public GamePeerExistRequestMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("GPEXISTREQUEST");
        this.PARAMETERS_NUM=3;

    }

}
