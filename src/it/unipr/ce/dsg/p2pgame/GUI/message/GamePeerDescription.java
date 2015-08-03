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
public class GamePeerDescription extends Message{

    private String description;

    public GamePeerDescription(String desc)
    {
        super("","",0);

        this.setMessageType("GPDESC");
        this.PARAMETERS_NUM=4;

        this.description=desc;
        this.getParametersList().add(new Parameter("gamepeerdesc",desc));



    }


    public GamePeerDescription(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());

        this.setMessageType("GPDESC");
        this.PARAMETERS_NUM=4;

         for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.description=this.getParametersList().get(3).getValue();

    }


    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String desc)
    {
        this.description=desc;

    }

}
