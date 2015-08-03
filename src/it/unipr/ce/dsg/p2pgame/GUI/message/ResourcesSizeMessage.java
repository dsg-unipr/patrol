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
public class ResourcesSizeMessage extends Message{

    int size;
    public ResourcesSizeMessage(int size)
    {
        super("","",0);

        this.setMessageType("RESOURCESSIZE");
        this.PARAMETERS_NUM=4;

        this.size=size;
        this.getParametersList().add(new Parameter("size",Integer.toString(size)));

    }

    public ResourcesSizeMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("RESOURCESSIZE");
        this.PARAMETERS_NUM=4;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.size=Integer.parseInt(this.getParametersList().get(3).getValue());


    }


    public int getSize(){
        return this.size;

    }

    public void setSize(int size)
    {
        this.size=size;
    }


}
