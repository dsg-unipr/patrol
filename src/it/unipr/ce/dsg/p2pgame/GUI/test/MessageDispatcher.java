/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unipr.ce.dsg.p2pgame.GUI.test;

import it.simplexml.message.Message;
import it.simplexml.message.MessageReader;
import it.unipr.ce.dsg.p2pgame.GUI.message.*;
import it.unipr.ce.dsg.p2pgame.GUI.message.content.GamePeerInfo;
import it.unipr.ce.dsg.p2pgame.GUI.message.content.Point;
import it.unipr.ce.dsg.p2pgame.platform.GamePlayer;
import it.unipr.ce.dsg.p2pgame.platform.GamePlayerResponsible;
import it.unipr.ce.dsg.p2pgame.platform.GameResource;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceEvolve;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobile;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobileResponsible;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageDispatcher{

    private Socket socket;

    public MessageDispatcher()
    {


    }

    private String sendMessage(Message message) throws UnknownHostException, IOException
    {
        this.socket=new Socket("jose",2424); // da modificare il nome del pc

        socket.setSoTimeout(0);
        socket.setReuseAddress(true);
	socket.setSoLinger(true,0);

        DataInputStream is = new DataInputStream(socket.getInputStream());
	DataOutputStream os = new DataOutputStream(socket.getOutputStream());

        os.write(message.generateXmlMessageString().getBytes());
        System.out.println("MessageDispatcher: messaggio inviato");
        byte buffer[]=new byte[100000];

	is.read(buffer);

        is.close();
        os.close();
        socket.close();

        String response=new String(buffer);

        return response;
    }

    private String sendMessage(Message message,int port) throws UnknownHostException, IOException
    {
        this.socket=new Socket("jose",port); // da modificare il nome del pc

        socket.setSoTimeout(0);
        socket.setReuseAddress(true);
	socket.setSoLinger(true,0);

        DataInputStream is = new DataInputStream(socket.getInputStream());
	DataOutputStream os = new DataOutputStream(socket.getOutputStream());

        os.write(message.generateXmlMessageString().getBytes());
        System.out.println("MessageDispatcher: messaggio inviato");
        byte buffer[]=new byte[100000];

	is.read(buffer);

        is.close();
        os.close();
        socket.close();

        String response=new String(buffer);

        return response;
    }

    public String getGamePeerId() //throws UnknownHostException, IOException
    {


        Message message=new IDRequestMessage();

        String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }



        MessageReader messageReader = new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());
        if (receivedMessage.getMessageType().equals("PEERID"))
        {
            //IDMessage idmessage=(IDMessage)receivedMessage;
            IDMessage idmessage=new IDMessage(receivedMessage);

            System.out.println("MessageDispatcher: GamePeerID: "+idmessage.getID());

            return idmessage.getID();
        }


        return null;

    }

    public String getGamePeerDescription()// throws UnknownHostException, IOException
    {
        Message message=new GamePeerDescriptionRequest();

        String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        MessageReader messageReader = new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());

        if (receivedMessage.getMessageType().equals("GPDESC"))
        {
            //GamePeerDescription description=(GamePeerDescription)receivedMessage;
            GamePeerDescription description=new GamePeerDescription(receivedMessage);


            return description.getDescription();
        }



        return null;
    }

    public boolean moveResourceMobile(String resId,double movX,double movY,double movZ,String threadId)// throws UnknownHostException, IOException
    {
        Message message=new MoveResourceMessage(resId,movX,movY,movZ,threadId);

        String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        MessageReader messageReader = new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());


         if (receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
         {
            //SuccessMessage success=(SuccessMessage)receivedMessage;
             SuccessMessage success=new SuccessMessage(receivedMessage);

            return success.getSuccess();

         }

         return false;

    }

    public Point getGamePlayerPosition()// throws UnknownHostException, IOException
    {
        Message message=new PositionRequestMessage();

        String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        MessageReader messageReader=new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());

        Point point=null;
        if(receivedMessage.getMessageType().equals("POSITION"))
        {
            //PositionMessage position=(PositionMessage)receivedMessage;
            PositionMessage position=new PositionMessage(receivedMessage);

            point=new Point(position.getX(),position.getY());

        }

        return point;

    }

    public void registerOnServer(String username, String password)// throws UnknownHostException, IOException
    {
        Message message=new RegisterRequestMessage(username,password);


        String response = null;
        try {
            response = this.sendMessage(message,9999);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        MessageReader messageReader=new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());
        if(receivedMessage.getMessageType().equals("REGISTERRESPONSE"))
        {
            //....

        }


    }



    public void startGame(double minX, double maxX, double minY, double maxY, double minZ, double maxZ, double vel, double vis, double gran) // throws UnknownHostException, IOException
    {
        Message message=new StartMessage(minX,maxX,minY,maxY,minZ,maxZ, vel,vis,gran);

        String response = null;
        try {
            response = this.sendMessage(message,9999);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        MessageReader messageReader=new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());

         if(receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
        {
            //....

        }


    }

    public void Multilog(String id,String text) // throws UnknownHostException, IOException
    {
        Message message=new MultilogMessage(id,text);
        String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        MessageReader messageReader=new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());

         if(receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
        {
            //....

        }

    }

    public void addResource(String id, String description, double quantity) //throws UnknownHostException, IOException
    {
        Message message=new CreateResourceMessage(id, description,quantity);
        String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        MessageReader messageReader=new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());

         if(receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
        {
            //....

        }


    }

    public void addResourceEvolve(String id, String description, double quantity, final long period, double offset) // throws UnknownHostException, IOException
    {
         Message message=new CreateResourceEvolveMessage(id,description,quantity,period,offset);

         String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

         MessageReader messageReader=new MessageReader();
         Message receivedMessage = messageReader.readMessageFromString(response.trim());


          if(receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
        {
            //....

        }


    }

    public void createMobileResource(String type, double qt) // throws UnknownHostException, IOException
    {
        Message message=new CreateMobileResourceMessage(type,qt);
        String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        MessageReader messageReader=new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());

         if(receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
        {
            //....

        }

    }

    public ArrayList<Object> getResources() // throws UnknownHostException, IOException
    {

        Message message=new GamePeerResourcesRequest();
        String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        MessageReader messageReader=new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());

        if(receivedMessage.getMessageType().equals("GPRESOURCES"))
        {
            //....
            ArrayList<Object> Resources = new ArrayList<Object>();
            //GamePeerResources gpr=(GamePeerResources)receivedMessage;
            GamePeerResources gpr=new GamePeerResources(receivedMessage);
            String resources=gpr.getResources();



            String []Array_resources=resources.split("\\:");
            int n=Array_resources.length;

            for(int i=0;i<n;i++)
            {

                String []tmp=Array_resources[i].split("\\#");

                 if(tmp[0].equals("GameResourceEvolve"))
                {
                    String id=tmp[1];
                    String description=tmp[2];
                    double quantity=Double.parseDouble(tmp[3]);
                    double period=Double.parseDouble(tmp[4]);
                    double offset=Double.parseDouble(tmp[5]);

                    GameResourceEvolve gre=new GameResourceEvolve(id,description,quantity,(long)period,offset);

                    Resources.add(gre);
                }
                else if(tmp[0].equals("GameResourceMobile"))
                {
                    String id=tmp[1];
                    String description=tmp[2];
                    double quantity=Double.parseDouble(tmp[3]);
                    double x=Double.parseDouble(tmp[4]);
                    double y=Double.parseDouble(tmp[5]);
                    double z=Double.parseDouble(tmp[6]);
                    double velocity=Double.parseDouble(tmp[7]);
                    double vision=Double.parseDouble(tmp[8]);
                    String owner=tmp[9];
                    String ownerid=tmp[10];
                    

                    GameResourceMobile grm=new GameResourceMobile(id,description,owner,ownerid,quantity,x,y,z,velocity,vision);

                    if(tmp.length==12)
                    {
                         String rvision=tmp[11];
                         String []Array_v=rvision.split("\\$");
                         int nv=Array_v.length;

                        for(int j=0;j<nv;j++)
                        {
                            String [] Array_vision=Array_v[j].split("\\;");
                            String type=Array_vision[0];

                            if(type.equals("GamePlayerResponsible"))
                            {
                                int pos=Integer.parseInt(Array_vision[1]);
                                String vid=Array_vision[2];
                                String vname=Array_vision[3];
                                double vx=Double.parseDouble(Array_vision[4]);
                                double vy=Double.parseDouble(Array_vision[5]);
                                double vz=Double.parseDouble(Array_vision[6]);
                                double vvel=Double.parseDouble(Array_vision[7]);
                                double vvis=Double.parseDouble(Array_vision[8]);
                                long vtime=Long.parseLong(Array_vision[9]);
                                String poshash=Array_vision[10];
                                String oldpos=Array_vision[11];

                                GamePlayerResponsible gpresp=new GamePlayerResponsible(vid,vname,vx,vy,vz,vvel,vvis,vtime,poshash,oldpos);
                                grm.addToResourceVision(gpresp, pos);
                            }
                            else if(type.equals("GameResourceMobileResponsible"))
                            {
                                int pos=Integer.parseInt(Array_vision[1]);
                                String vid=Array_vision[2];
                                String vdesc=Array_vision[3];
                                String vowner=Array_vision[4];
                                String vownerid=Array_vision[5];
                                double vq=Double.parseDouble(Array_vision[6]);
                                double vx=Double.parseDouble(Array_vision[7]);
                                double vy=Double.parseDouble(Array_vision[8]);
                                double vz=Double.parseDouble(Array_vision[9]);
                                double vvel=Double.parseDouble(Array_vision[10]);
                                double vvis=Double.parseDouble(Array_vision[11]);
                                long vtime=Long.parseLong(Array_vision[12]);
                                String poshash=Array_vision[13];
                                String oldpos=Array_vision[14];

                                GameResourceMobileResponsible grmresp=new GameResourceMobileResponsible(vid,vdesc,vowner,vownerid,vq,vx,vy,vz,vvel,vvis,vtime,poshash,oldpos);

                                grm.addToResourceVision(grmresp, pos);
                            }


                        }
                    }

   

                    Resources.add(grm);

                }
                else if(tmp[0].equals("GameResource"))
                {
                    String id=tmp[1];
                    String description=tmp[2];
                    double quantity=Double.parseDouble(tmp[3]);

                    GameResource gr=new GameResource(id,description,quantity);

                    Resources.add(gr);

                }
                
            }

            System.out.println("MessageDispatcher: getResources");

            return Resources;

        }

        return null;
    }



    public GamePeerInfo getGamePeerInfo() //throws UnknownHostException, IOException
    {
        Message message=new InfoGamePeerRequestMessage();

        String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        MessageReader messageReader=new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());

        if(receivedMessage.getMessageType().equals("GAMEPEERINFO"))
        {
            //InfoGamePeerMessage infogp=(InfoGamePeerMessage)receivedMessage;
            InfoGamePeerMessage infogp=new InfoGamePeerMessage(receivedMessage);

            String id=infogp.getPlayerID();
            String name=infogp.getPlayerName();
            double posx=infogp.getPosX();
            double posy=infogp.getPosY();
            double posz=infogp.getPosZ();
            double vel=infogp.getVel();
            double vis=infogp.getVis();

            GamePeerInfo info=new GamePeerInfo(id,name,posx,posy,posz,vel,vis);

            return info;

        }

        return null;
    }


public GameResourceMobile getMobileResource(String resource_id) //throws UnknownHostException, IOException
{
    Message message=new MobileResourceFromIDRequestMessage(resource_id);

    String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

    MessageReader messageReader=new MessageReader();
    Message receivedMessage = messageReader.readMessageFromString(response.trim());

    if(receivedMessage.getMessageType().equals("GPRESOURCEMOBILE"))
    {
        //MobileResourceFromIDMessage resid=(MobileResourceFromIDMessage)receivedMessage;
        MobileResourceFromIDMessage resid=new MobileResourceFromIDMessage(receivedMessage);

        String resource=resid.getResource();
        String []tmp=resource.split("\\#");

        String id=tmp[1];
        String description=tmp[2];
        double quantity=Double.parseDouble(tmp[3]);
        double x=Double.parseDouble(tmp[4]);
        double y=Double.parseDouble(tmp[5]);
        double z=Double.parseDouble(tmp[6]);
        double velocity=Double.parseDouble(tmp[7]);
        double vision=Double.parseDouble(tmp[8]);
        String owner=tmp[9];
        String ownerid=tmp[10];




        GameResourceMobile grm=new GameResourceMobile(id,description,owner,ownerid,quantity,x,y,z,velocity,vision);

        if(tmp.length==12)
        {
            String rvision=tmp[11];
            String []Array_v=rvision.split("\\$");
            int nv=Array_v.length;

            for(int j=0;j<nv;j++)
            {
                String [] Array_vision=Array_v[j].split("\\;");
                String type=Array_vision[0];

                        if(type.equals("GamePlayerResponsible"))
                        {
                            int pos=Integer.parseInt(Array_vision[1]);
                            String vid=Array_vision[2];
                            String vname=Array_vision[3];
                            double vx=Double.parseDouble(Array_vision[4]);
                            double vy=Double.parseDouble(Array_vision[5]);
                            double vz=Double.parseDouble(Array_vision[6]);
                            double vvel=Double.parseDouble(Array_vision[7]);
                            double vvis=Double.parseDouble(Array_vision[8]);
                            long vtime=Long.parseLong(Array_vision[9]);
                            String poshash=Array_vision[10];
                            String oldpos=Array_vision[11];

                            GamePlayerResponsible gpresp=new GamePlayerResponsible(vid,vname,vx,vy,vz,vvel,vvis,vtime,poshash,oldpos);
                            grm.addToResourceVision(gpresp, pos);
                        }
                        else if(type.equals("GameResourceMobileResponsible"))
                        {
                            int pos=Integer.parseInt(Array_vision[1]);
                            String vid=Array_vision[2];
                            String vdesc=Array_vision[3];
                            String vowner=Array_vision[4];
                            String vownerid=Array_vision[5];
                            double vq=Double.parseDouble(Array_vision[6]);
                            double vx=Double.parseDouble(Array_vision[7]);
                            double vy=Double.parseDouble(Array_vision[8]);
                            double vz=Double.parseDouble(Array_vision[9]);
                            double vvel=Double.parseDouble(Array_vision[10]);
                            double vvis=Double.parseDouble(Array_vision[11]);
                            long vtime=Long.parseLong(Array_vision[12]);
                            String poshash=Array_vision[13];
                            String oldpos=Array_vision[14];

                            GameResourceMobileResponsible grmresp=new GameResourceMobileResponsible(vid,vdesc,vowner,vownerid,vq,vx,vy,vz,vvel,vvis,vtime,poshash,oldpos);

                             grm.addToResourceVision(grmresp, pos);
                        }


            }

        }




            return grm;

    }



    return null;
}

public GameResource getMyResourceFromId(String r_id) //throws UnknownHostException, IOException
{
    Message message=new ResourceFromIDRequestMessage(r_id);

    String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

    MessageReader messageReader=new MessageReader();
    Message receivedMessage = messageReader.readMessageFromString(response.trim());

    if(receivedMessage.getMessageType().equals("GPRESOURCE"))
    {
        ResourceFromIDMessage res=new ResourceFromIDMessage(receivedMessage);

        String str_resource=res.getResource();
        
        GameResource game_resource=null;

       String []tmp=str_resource.split("\\#");

                if(tmp[0].equals("GameResource"))
                {
                    String id=tmp[1];
                    String description=tmp[2];
                    double quantity=Double.parseDouble(tmp[3]);

                    GameResource gr=new GameResource(id,description,quantity);

                    game_resource=gr;

                }
                else if(tmp[0].equals("GameResourceEvolve"))
                {
                    String id=tmp[1];
                    String description=tmp[2];
                    double quantity=Double.parseDouble(tmp[3]);
                    double period=Double.parseDouble(tmp[4]);
                    double offset=Double.parseDouble(tmp[5]);

                    GameResourceEvolve gre=new GameResourceEvolve(id,description,quantity,(long)period,offset);

                   game_resource=gre;
                }
                else if(tmp[0].equals("GameResourceMobile"))
                {
                    String id=tmp[1];
                    String description=tmp[2];
                    double quantity=Double.parseDouble(tmp[3]);
                    double x=Double.parseDouble(tmp[4]);
                    double y=Double.parseDouble(tmp[5]);
                    double z=Double.parseDouble(tmp[6]);
                    double velocity=Double.parseDouble(tmp[7]);
                    double vision=Double.parseDouble(tmp[8]);
                    String owner=tmp[9];
                    String ownerid=tmp[10];
                    

                    GameResourceMobile grm=new GameResourceMobile(id,description,owner,ownerid,quantity,x,y,z,velocity,vision);

                  if(tmp.length==12)
                  {
                        String rvision=tmp[11];
   

                    String []Array_v=rvision.split("\\$");
                    int nv=Array_v.length;

                    for(int j=0;j<nv;j++)
                    {
                        String [] Array_vision=Array_v[j].split("\\;");
                        String type=Array_vision[0];

                        if(type.equals("GamePlayerResponsible"))
                        {
                            int pos=Integer.parseInt(Array_vision[1]);
                            String vid=Array_vision[2];
                            String vname=Array_vision[3];
                            double vx=Double.parseDouble(Array_vision[4]);
                            double vy=Double.parseDouble(Array_vision[5]);
                            double vz=Double.parseDouble(Array_vision[6]);
                            double vvel=Double.parseDouble(Array_vision[7]);
                            double vvis=Double.parseDouble(Array_vision[8]);
                            long vtime=Long.parseLong(Array_vision[9]);
                            String poshash=Array_vision[10];
                            String oldpos=Array_vision[11];

                            GamePlayerResponsible gpresp=new GamePlayerResponsible(vid,vname,vx,vy,vz,vvel,vvis,vtime,poshash,oldpos);
                            grm.addToResourceVision(gpresp, pos);
                        }
                        else if(type.equals("GameResourceMobileResponsible"))
                        {
                            int pos=Integer.parseInt(Array_vision[1]);
                            String vid=Array_vision[2];
                            String vdesc=Array_vision[3];
                            String vowner=Array_vision[4];
                            String vownerid=Array_vision[5];
                            double vq=Double.parseDouble(Array_vision[6]);
                            double vx=Double.parseDouble(Array_vision[7]);
                            double vy=Double.parseDouble(Array_vision[8]);
                            double vz=Double.parseDouble(Array_vision[9]);
                            double vvel=Double.parseDouble(Array_vision[10]);
                            double vvis=Double.parseDouble(Array_vision[11]);
                            long vtime=Long.parseLong(Array_vision[12]);
                            String poshash=Array_vision[13];
                            String oldpos=Array_vision[14];

                            GameResourceMobileResponsible grmresp=new GameResourceMobileResponsible(vid,vdesc,vowner,vownerid,vq,vx,vy,vz,vvel,vvis,vtime,poshash,oldpos);

                             grm.addToResourceVision(grmresp, pos);
                        }


                    }

                   }
                    
                   game_resource=grm;

                }

                return game_resource;
    }


    return null;
}



public ArrayList<Object> getVision() //throws UnknownHostException, IOException
{
    Message message=new GamePeerVisionRequest();


    String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

    MessageReader messageReader=new MessageReader();
    Message receivedMessage = messageReader.readMessageFromString(response.trim());

    if(receivedMessage.getMessageType().equals("GPVISION"))
    {
        //GamePeerVision gpv=(GamePeerVision)receivedMessage;
        GamePeerVision gpv=new GamePeerVision(receivedMessage);
        String str_vision=gpv.getVision();

        ArrayList<Object> vision=new ArrayList<Object>();

        String []Array_v=str_vision.split("\\$");
        int nv=Array_v.length;

        for(int j=0;j<nv;j++)
        {
            String [] Array_vision=Array_v[j].split("\\;");
            String type=Array_vision[0];

                        if(type.equals("GamePlayerResponsible"))
                        {
                            int pos=Integer.parseInt(Array_vision[1]);
                            String vid=Array_vision[2];
                            String vname=Array_vision[3];
                            double vx=Double.parseDouble(Array_vision[4]);
                            double vy=Double.parseDouble(Array_vision[5]);
                            double vz=Double.parseDouble(Array_vision[6]);
                            double vvel=Double.parseDouble(Array_vision[7]);
                            double vvis=Double.parseDouble(Array_vision[8]);
                            long vtime=Long.parseLong(Array_vision[9]);
                            String poshash=Array_vision[10];
                            String oldpos=Array_vision[11];

                            GamePlayerResponsible gpresp=new GamePlayerResponsible(vid,vname,vx,vy,vz,vvel,vvis,vtime,poshash,oldpos);

                            vision.add(pos,gpresp);
                        }
                        else if(type.equals("GameResourceMobileResponsible"))
                        {
                            int pos=Integer.parseInt(Array_vision[1]);
                            String vid=Array_vision[2];
                            String vdesc=Array_vision[3];
                            String vowner=Array_vision[4];
                            String vownerid=Array_vision[5];
                            double vq=Double.parseDouble(Array_vision[6]);
                            double vx=Double.parseDouble(Array_vision[7]);
                            double vy=Double.parseDouble(Array_vision[8]);
                            double vz=Double.parseDouble(Array_vision[9]);
                            double vvel=Double.parseDouble(Array_vision[10]);
                            double vvis=Double.parseDouble(Array_vision[11]);
                            long vtime=Long.parseLong(Array_vision[12]);
                            String poshash=Array_vision[13];
                            String oldpos=Array_vision[14];

                            GameResourceMobileResponsible grmresp=new GameResourceMobileResponsible(vid,vdesc,vowner,vownerid,vq,vx,vy,vz,vvel,vvis,vtime,poshash,oldpos);

                             vision.add(pos,grmresp);

                        }

            }


            return vision;

    }

    return null;
}


public int getResourcesSize() //throws UnknownHostException, IOException
{

    Message message=new ResourcesSizeRequestMessage();


    String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

    MessageReader messageReader=new MessageReader();
    Message receivedMessage = messageReader.readMessageFromString(response.trim());

    if(receivedMessage.getMessageType().equals("RESOURCESSIZE"))
    {
        ResourcesSizeMessage res_msg=new ResourcesSizeMessage(receivedMessage);

        int size=res_msg.getSize();

        return size;

    }


    return 0;
}

public GamePlayer getGamePlayer()  //throws UnknownHostException, IOException
{
    Message message=new GamePlayerRequestMessage();

    String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

    MessageReader messageReader=new MessageReader();
    Message receivedMessage = messageReader.readMessageFromString(response.trim());

     if(receivedMessage.getMessageType().equals("GPLAYER"))
     {
         GamePlayerMessage gplayer=new GamePlayerMessage(receivedMessage);

         String id=gplayer.getID();
         String name=gplayer.getName();
         double posx=gplayer.getPosX();
         double posy=gplayer.getPosY();
         double posz=gplayer.getPosZ();
         double vel=gplayer.getVelocity();
         double vis=gplayer.getVisibility();

         GamePlayer gameplayer=new GamePlayer(id,name,posx,posy,posz,vel,vis);

         return gameplayer;

     }




    return null;
}


public double getGranularity()
{

    Message message=new GranularityRequestMessage();

    String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

    MessageReader messageReader=new MessageReader();
    Message receivedMessage = messageReader.readMessageFromString(response.trim());

    if(receivedMessage.getMessageType().equals("GRANULARITY"))
    {
        GranularityMessage granmsg=new GranularityMessage(receivedMessage);

        double gran=granmsg.getGranularity();
        return gran;
    }




    return 0;
}

public void UpdateResourceEvolve(double quantity)
{
    Message message=new UpdateResourceEvolveMessage(quantity);

    String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

    MessageReader messageReader=new MessageReader();
    Message receivedMessage = messageReader.readMessageFromString(response.trim());

    if(receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
    {
     //............
    }


}

public void MovementRequest(double targetx,double targety,String resId,String threadId)
{
    Message message=new MovementRequestMessage(targetx,targety,resId,threadId);
    String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

    MessageReader messageReader=new MessageReader();
    Message receivedMessage = messageReader.readMessageFromString(response.trim());

    if(receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
    {
     //............
    }



}


public void CreateGamePeer(int inPort, int outPort, int idBitLength, String id, String serverAddr, int serverPort, int gameInPort, int gameOutPort, String gameServerAddr, int gameServerPort,
			int stab, int fix, int check, int pub)
{

    Message message=new CreateGamePeerRequestMessage(inPort,outPort,idBitLength,id,serverAddr,serverPort,gameInPort,gameOutPort,gameServerAddr,gameServerPort,stab,fix,check, pub);
    String response = null;
        try {
            response = this.sendMessage(message,9999);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

    MessageReader messageReader=new MessageReader();
    Message receivedMessage = messageReader.readMessageFromString(response.trim());

    if(receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
    {
     //............
    }



}

public boolean GamePeerExist()
{
    Message message=new GamePeerExistRequestMessage();
    String response = null;
        try {
            response = this.sendMessage(message,9999);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

    MessageReader messageReader=new MessageReader();
    Message receivedMessage = messageReader.readMessageFromString(response.trim());

    if(receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
    {
      SuccessMessage success=new SuccessMessage(receivedMessage);
      boolean suc=success.getSuccess();

      return suc;
    }


    return false;
}

public String getIpAddress()
{
    Message message=new IpAddressRequestMessage();
    String response = null;
        try {
            response = this.sendMessage(message);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

    MessageReader messageReader=new MessageReader();
    Message receivedMessage = messageReader.readMessageFromString(response.trim());

    if(receivedMessage.getMessageType().equals("IPADDRESS"))
    {
        IpAddressMessage ipAddress=new IpAddressMessage(receivedMessage);

        return ipAddress.getIpAddress();
    }



    return null;
}



}
