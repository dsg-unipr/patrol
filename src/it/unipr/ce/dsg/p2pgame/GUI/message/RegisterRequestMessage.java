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

public class RegisterRequestMessage extends Message{

    private String username;
    private String password;
    public RegisterRequestMessage(String user,String pass)
    {
        super("","",0);
        this.setMessageType("REGISTERREQUEST");
        this.PARAMETERS_NUM=5;

        this.username=user;
        this.password=pass;

        this.getParametersList().add(new Parameter("username",this.username));
        this.getParametersList().add(new Parameter("password",this.password));


    }

    public RegisterRequestMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("REGISTERREQUEST");
        this.PARAMETERS_NUM=5;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.username=this.getParametersList().get(3).getValue();
        this.password=this.getParametersList().get(4).getValue();

    }

    public String getUserName()
    {
        return this.username;
    }

    public void setUserName(String user)
    {
        this.username=user;
    }

    public String getPassword()
    {
        return this.password;

    }

    public void setPassword(String pass)
    {
        this.password=pass;
    }

}
