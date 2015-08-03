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
public class RegisterResponseMessage extends Message{

    private String response;
    public RegisterResponseMessage(String response)
    {
         super("","",0);
        this.setMessageType("REGISTERRESPONSE");
        this.PARAMETERS_NUM=4;

        this.response=response;
        this.getParametersList().add(new Parameter("response",this.response));
    }

    public RegisterResponseMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("REGISTERRESPONSE");
        this.PARAMETERS_NUM=4;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.response=this.getParametersList().get(3).getValue();



    }

    public void setResponse(String response)
    {
    this.response=response;
    }


    public String getResponse()
    {
        return this.response;
    }

}
