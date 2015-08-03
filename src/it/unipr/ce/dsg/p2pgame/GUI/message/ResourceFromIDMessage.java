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
public class ResourceFromIDMessage extends Message{

//    public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}

	String resource;
    //String id; // da togliere

    public ResourceFromIDMessage(String resource)
    {
    	
    	super("","",0);
        //super(Long.toString(System.currentTimeMillis()),"",0);
        this.setMessageType("GPRESOURCE");
        this.PARAMETERS_NUM=4;

        this.resource=resource;
      //  this.id=id;
        this.getParametersList().add(new Parameter("resource", this.resource));
       // this.getParametersList().add(new Parameter("id", this.id));
    }

    public ResourceFromIDMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("GPRESOURCE");
        this.PARAMETERS_NUM=4;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.resource=this.getParametersList().get(3).getValue();
   //     this.id=this.getParametersList().get(4).getValue();
    }



    public String getResource()
    {
        return resource;
    }

    public void setResource(String res)
    {
        this.resource=res;
    }

}
