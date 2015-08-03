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
public class MultilogMessage extends Message{

    private String id;
    private String text;
    public MultilogMessage(String id,String text)
    {
        super("","",0);
        this.setMessageType("MULTILOG");
        this.PARAMETERS_NUM=5;

        this.id=id;
        this.text=text;
        this.getParametersList().add(new Parameter("id",this.id));
        this.getParametersList().add(new Parameter("text",this.text));
    }


    public MultilogMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("MULTILOG");
        this.PARAMETERS_NUM=5;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.id=this.getParametersList().get(3).getValue();
        this.text=this.getParametersList().get(4).getValue();
    }



    public String getID()
    {
        return this.id;
    }

    public String getText()
    {
        return this.text;
    }

    public void setID(String id)
    {
        this.id=id;
    }

    public void setText(String text)
    {
        this.text=text;
    }


}
