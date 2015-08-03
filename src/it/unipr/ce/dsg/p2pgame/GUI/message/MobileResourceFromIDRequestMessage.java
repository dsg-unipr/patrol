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
public class MobileResourceFromIDRequestMessage extends Message{

    private String id;

    public MobileResourceFromIDRequestMessage(String id)
    {
         super("","",0);
        this.setMessageType("RESOURCEMOBILEBYIDREQUEST");
        this.PARAMETERS_NUM=4;

        this.id=id;
        this.getParametersList().add(new Parameter("resourceid",id));

    }

    public MobileResourceFromIDRequestMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("RESOURCEMOBILEBYIDREQUEST");
        this.PARAMETERS_NUM=4;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.id=this.getParametersList().get(3).getValue();

    }

     public String getResourceID()
    {
        return this.id;

    }

     public void setResourceID(String id)
     {
        this.id=id;
     }

}
