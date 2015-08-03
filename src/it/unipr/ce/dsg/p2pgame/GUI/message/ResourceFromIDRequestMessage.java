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
public class ResourceFromIDRequestMessage extends Message{

    private String id;
   // private String msgid;
    
  //  public String getMsgid() {
	//	return msgid;
//	}

//	public void setMsgid(String msgid) {
//		this.msgid = msgid;
//	}

	public ResourceFromIDRequestMessage(String id)
    {
         super("","",0);
        this.setMessageType("RESOURCEBYIDREQUEST");
        this.PARAMETERS_NUM=4;

        this.id=id;
        //this.msgid=msgid;
        this.getParametersList().add(new Parameter("resourceid",id));
       // this.getParametersList().add(new Parameter("msgid",msgid));

    }

    public ResourceFromIDRequestMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("RESOURCEBYIDREQUEST");
        this.PARAMETERS_NUM=4;

         for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.id=this.getParametersList().get(3).getValue();
       // this.msgid=this.getParametersList().get(4).getValue();
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
