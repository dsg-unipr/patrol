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

public class GamePeerVision extends Message{

    private String vision;
    public GamePeerVision(String vision)
    {
         super("","",0);
        this.setMessageType("GPVISION");
        this.PARAMETERS_NUM=4;

        this.vision=vision;
        this.getParametersList().add(new Parameter("vision", this.vision));
    }
    
    public GamePeerVision(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("GPVISION");
        this.PARAMETERS_NUM=4;

         for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}
        this.vision=this.getParametersList().get(3).getValue();
        
    }



    public String getVision()
    {
        return this.vision;

    }

    public void setVision(String vis)
    {
        this.vision=vis;

    }

    

}
