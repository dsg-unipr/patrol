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
public class GamePeerResources extends Message{

    String resources;

    public GamePeerResources(String resources)
    {
         super("","",0);
        this.setMessageType("GPRESOURCES");
        this.PARAMETERS_NUM=4;
        
        this.resources=resources;
        this.getParametersList().add(new Parameter("resources", this.resources));


    }
    
    public GamePeerResources(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("GPRESOURCES");
        this.PARAMETERS_NUM=4;
        
         for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}
        
        this.resources=this.getParametersList().get(3).getValue();
        
    }



    public String getResources()
    {
        return this.resources;

    }

    public void setResources(String res)
    {
        this.resources=res;
    }


}
