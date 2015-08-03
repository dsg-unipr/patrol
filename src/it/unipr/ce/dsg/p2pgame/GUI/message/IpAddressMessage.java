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
public class IpAddressMessage extends Message{

    private String ipAddress;
    public IpAddressMessage(String ipAddress)
    {
        super("","",0);
        this.setMessageType("IPADDRESS");
        this.PARAMETERS_NUM=4;

        this.ipAddress=ipAddress;

        this.getParametersList().add(new Parameter("ipaddress",ipAddress));
    }

    public IpAddressMessage(Message message)
    {
        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("IPADDRESS");
        this.PARAMETERS_NUM=4;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.ipAddress=this.getParametersList().get(3).getValue();
    }

    public String getIpAddress()
    {
        return this.ipAddress;
    }

    public void setIpAddress(String ip)
    {
        this.ipAddress=ip;
    }



}
