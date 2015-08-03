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
public class CreateGamePeerRequestMessage extends Message{

    private int inPort;
    private int outPort;
    private int idBitLength;
    private String id;
    private String serverAddr;
    private int serverPort;
    private int gameInPort;
    private int gameOutPort;
    private String gameServerAddr;
    private int gameServerPort;
    private int stab;
    private int fix;
    private int check;
    private int pub;

    public CreateGamePeerRequestMessage(int inPort, int outPort, int idBitLength, String id, String serverAddr, int serverPort, int gameInPort, int gameOutPort, String gameServerAddr, int gameServerPort,
			int stab, int fix, int check, int pub)
    {
        super("","",0);
        this.setMessageType("CREATEGAMEPEERREQUEST");
        this.PARAMETERS_NUM=17;

        this.inPort=inPort;
        this.outPort=outPort;
        this.idBitLength=idBitLength;
        this.id=id;
        this.serverAddr=serverAddr;
        this.serverPort=serverPort;
        this.gameInPort=gameInPort;
        this.gameOutPort=gameOutPort;
        this.gameServerAddr=gameServerAddr;
        this.gameServerPort=gameServerPort;
        this.stab=stab;
        this.fix=fix;
        this.check=check;
        this.pub=pub;

        this.getParametersList().add(new Parameter("inPort",Integer.toString(inPort)));
        this.getParametersList().add(new Parameter("outPort",Integer.toString(outPort)));
        this.getParametersList().add(new Parameter("idBitLength",Integer.toString(idBitLength)));
        this.getParametersList().add(new Parameter("id",id));
        this.getParametersList().add(new Parameter("serverAddr",serverAddr));
        this.getParametersList().add(new Parameter("serverPort",Integer.toString(serverPort)));
        this.getParametersList().add(new Parameter("gameInPort",Integer.toString(gameInPort)));
        this.getParametersList().add(new Parameter("gameOutPort",Integer.toString(gameOutPort)));
        this.getParametersList().add(new Parameter("gameServerAddr",gameServerAddr));
        this.getParametersList().add(new Parameter("gameServerPort",Integer.toString(gameServerPort)));
        this.getParametersList().add(new Parameter("stab",Integer.toString(stab)));
        this.getParametersList().add(new Parameter("fix",Integer.toString(fix)));
        this.getParametersList().add(new Parameter("check",Integer.toString(check)));
        this.getParametersList().add(new Parameter("pub",Integer.toString(pub)));



    }


    public CreateGamePeerRequestMessage(Message message)
    {

        super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());
        this.setMessageType("CREATEGAMEPEERREQUEST");
        this.PARAMETERS_NUM=17;

        for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}

        this.inPort=Integer.parseInt(this.getParametersList().get(3).getValue());
        this.outPort=Integer.parseInt(this.getParametersList().get(4).getValue());
        this.idBitLength=Integer.parseInt(this.getParametersList().get(5).getValue());
        this.id=this.getParametersList().get(6).getValue();
        this.serverAddr=this.getParametersList().get(7).getValue();
        this.serverPort=Integer.parseInt(this.getParametersList().get(8).getValue());
        this.gameInPort=Integer.parseInt(this.getParametersList().get(9).getValue());
        this.gameOutPort=Integer.parseInt(this.getParametersList().get(10).getValue());
        this.gameServerAddr=this.getParametersList().get(11).getValue();
        this.gameServerPort=Integer.parseInt(this.getParametersList().get(12).getValue());
        this.stab=Integer.parseInt(this.getParametersList().get(13).getValue());
        this.fix=Integer.parseInt(this.getParametersList().get(14).getValue());
        this.check=Integer.parseInt(this.getParametersList().get(15).getValue());
        this.pub=Integer.parseInt(this.getParametersList().get(16).getValue());

        
    }

    public int getInPort()
    {
        return this.inPort;
    }

    public int getOutPort()
    {
        return this.outPort;
    }

    public int getIdBitLength()
    {
        return this.idBitLength;
    }

    public String getId()
    {
        return this.id;
    }

    public String getServerAddr()
    {
        return this.serverAddr;
    }

    public int getServerPort()
    {
        return this.serverPort;
    }

    public int getGameInPort()
    {
        return this.gameInPort;
    }

    public int getGameOutPort()
    {
        return this.gameOutPort;
    }

    public String getGameServerAddr()
    {
        return this.gameServerAddr;
    }

    public int getGameServerPort()
    {
        return this.gameServerPort;
    }

    public int getStab()
    {
        return this.stab;
    }

    public int getFix()
    {
        return this.fix;
    }

    public int getCheck()
    {
        return this.check;
    }

    public int getPub()
    {
        return this.pub;
    }


    public void setInPort(int inport)
    {
        this.inPort=inport;
    }

    public void setOutPort(int outport)
    {
        this.outPort=outport;
    }

    public void setIdBitLength(int idbl)
    {
        this.idBitLength=idbl;
    }

    public void setId(String id)
    {
        this.id=id;
    }

    public void setServerAddr(String address)
    {
        this.serverAddr=address;
    }

    public void setServerPort(int port)
    {
        this.serverPort=port;
    }

    public void setGameInPort(int port)
    {
        this.gameInPort=port;
    }

    public void setGameOutPort(int port)
    {
        this.gameOutPort=port;
    }

    public void setGameServerAddr(String address)
    {
        this.gameServerAddr=address;
    }

    public void setGameServerPort(int port)
    {
        this.gameServerPort=port;
    }

    public void getStab(int stab)
    {
        this.stab=stab;
    }

    public void getFix(int fix)
    {
        this.fix=fix;
    }

    public void getCheck(int check)
    {
        this.check=check;
    }

    public void getPub(int pub)
    {
        this.pub=pub;
    }






    











}
