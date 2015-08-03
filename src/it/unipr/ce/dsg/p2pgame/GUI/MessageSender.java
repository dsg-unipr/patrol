

package it.unipr.ce.dsg.p2pgame.GUI;

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
import it.unipr.ce.dsg.p2pgame.util.CheckOutput;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jose murga
 */
public class MessageSender{

    private Socket socket;
    private Socket socket1;
    private CheckOutput check;
    

    public MessageSender()
    {
    	check=new CheckOutput("output.txt");

    }

    /*
     * sendMessage
     * Sends a generic message by socket and returns the response string
     * Parameters:
     * message: Message
     */
    private String sendMessage(Message message) throws UnknownHostException, IOException
    {
        this.socket=new Socket("127.0.0.1",1235); // TODO: edit name and port

        socket.setSoTimeout(0);
        socket.setReuseAddress(true);
	    socket.setSoLinger(true,0);

        DataInputStream is = new DataInputStream(socket.getInputStream());
	    DataOutputStream os = new DataOutputStream(socket.getOutputStream());

        os.write(message.generateXmlMessageString().getBytes());
        
       
	    
	        String response="";
	   

		    int current = 0;
			byte[] buffer = new byte[100000];

			while (current < 1) {

				int reader = is.read(buffer);

				if (reader != -1){
					//response=new String(buffer,0,buffer.length);
					response=new String(buffer);
					current++;
				}
			}
		
        is.close();
        os.close();
        socket.close();

        
        return response.trim();
    }

    /*
     * sendMessage
     * Sends a generic message by socket to an specific port number, and returns the response string
     * Parameters:
     * message: Message
     * port: int
     */
    private String sendMessage(Message message,int port) throws UnknownHostException, IOException
    {
        this.socket1=new Socket("127.0.0.1",port); // TODO: edit name

        socket1.setSoTimeout(0);
        socket1.setReuseAddress(true);
	    socket1.setSoLinger(true,0);

        DataInputStream is = new DataInputStream(socket1.getInputStream());
	    DataOutputStream os = new DataOutputStream(socket1.getOutputStream());

        os.write(message.generateXmlMessageString().getBytes());
        
       
	    
	    String response="";
		   

	    int current = 0;
		byte[] buffer = new byte[100000];

		while (current < 1) {

			int reader = is.read(buffer);

			if (reader != -1){
				
				response=new String(buffer);
				current++;
			}
		}

        is.close();
        os.close();
        socket1.close();

        
        return response;
    }

    /*
     * getGamePeerId
     * Sends a id request message by socket, then returns the game peer id string
     */
    public String getGamePeerId() 
    {


        Message message=new IDRequestMessage();

        String response = null;
      
        response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString());

        MessageReader messageReader = new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());
        if (receivedMessage.getMessageType().equals("PEERID"))
        {
           
            IDMessage idmessage=new IDMessage(receivedMessage);
            return idmessage.getID();
        }


        return null;

    }

   /*
    *  getGamePeerDescription
    *  Sends a game peer description request message by socket.
    *  Then returns the game peer description string
    */
    public String getGamePeerDescription()
    {
        Message message=new GamePeerDescriptionRequest();

        String response = null;
      
    response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString());
        MessageReader messageReader = new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());

        if (receivedMessage.getMessageType().equals("GPDESC"))
        {
            
            GamePeerDescription description=new GamePeerDescription(receivedMessage);

            return description.getDescription();
        }


        return null;
    }

    /*
     * moveResourceMobile
     * Sends a move resource message. Then returns the success (true or false)
     * Parameters:
     * resId: String
     * movX: double
     * movY: double
     * movZ: double
     * threadId: String
     */
    public boolean moveResourceMobile(String resId,double movX,double movY,double movZ,String threadId)
    {
        Message message=new MoveResourceMessage(resId,movX,movY,movZ,threadId);

        String response = null;
       
    response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString());

        MessageReader messageReader = new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());


         if (receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
         {
            
             SuccessMessage success=new SuccessMessage(receivedMessage);

            return success.getSuccess();

         }

         return false;

    }

    /*
     * getGamePlayerPosition
     * Sends a player postiion message by socket.
     * Then returns an instance of Point class, with the current position of the game player (x,y)
     * 
     */
    public Point getGamePlayerPosition()
    {
        Message message=new PositionRequestMessage();

        String response = null;
       
    response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString());

        MessageReader messageReader=new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());

        Point point=null;
        if(receivedMessage.getMessageType().equals("POSITION"))
        {
            
            PositionMessage position=new PositionMessage(receivedMessage);
            point=new Point(position.getX(),position.getY());

        }

        return point;

    }

    /*
     * registerOnServer
     * Sends a register server message by socket, with the username and password
     * Parameters:
     * username: String
     * password: String
     */
    public void registerOnServer(String username, String password)
    {
        Message message=new RegisterRequestMessage(username,password);


        String response = it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString()); // FIXME era 1235

        MessageReader messageReader=new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());
        
        System.out.println("receivedMessage = " + receivedMessage.getMessageType());
        
        if(receivedMessage.getMessageType().equals("REGISTERRESPONSE"))
        {
            //....
        	System.out.println("receivedMessage = " + receivedMessage.getMessageType());
        }


    }


    /*
     * startGame
     * Sends an Start Message by socket,
     * with the world informations (minX,maxX,minY,maxY,minZ,maxZ,velocity,visibility, granularity)
     * Parameters:
     * minX: double
     * maxX: double
     * minY: double
     * maxY: double
     * minZ: double
     * maxZ: double
     * vel: double
     * vis: double
     * gran: double
     */
    public void startGame(double minX, double maxX, double minY, double maxY, double minZ, double maxZ, double vel, double vis, double gran) // throws UnknownHostException, IOException
    {
        Message message=new StartMessage(minX,maxX,minY,maxY,minZ,maxZ, vel,vis,gran);

        String response = null;
      
    response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString());

        MessageReader messageReader=new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());

         if(receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
        {
            //....

        }


    }

    /*
     * Multilog
     * sends a multilog message
     * Parameters:
     * id: String
     * text: String
     */
    public void Multilog(String id,String text) 
    {
        Message message=new MultilogMessage(id,text);
        String response = null;
      
    response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString());

        MessageReader messageReader=new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());

         if(receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
        {
            //....

        }

    }

    /*
     * addResource
     * Sends a Create resource message by socket, with the resource informations
     * (id, description, quantity).
     * Parameters:
     * id: String
     * description: String
     * quantity: double
     */
    public void addResource(String id, String description, double quantity) 
    {
        Message message=new CreateResourceMessage(id, description,quantity);
        String response = null;
       
    response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString());
        MessageReader messageReader=new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());

         if(receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
        {
            //....

        }


    }

    /*
     * addResourceEvolve
     * Sends a Create resource evolve message by socket, with the resource evolve informations
     * (id, description, quantity, period, offset)
     * Parameters:
     * id: String
     * description: String
     * quantity: double
     * period: long
     * offset: double
     */
    public void addResourceEvolve(String id, String description, double quantity, final long period, double offset) 
    {
         Message message=new CreateResourceEvolveMessage(id,description,quantity,period,offset);

         String response = null;
      
     response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString());

         MessageReader messageReader=new MessageReader();
         Message receivedMessage = messageReader.readMessageFromString(response.trim());


          if(receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
        {
            //....

        }

    }

    /*
     *createMobileResource
     *Sends a create mobile resource message by socket
     * Parameters:
     * type: String
     * qt: double (quantity)
     */
    public void createMobileResource(String type, double qt)
    {
        Message message=new CreateMobileResourceMessage(type,qt);
        String response = null;
      
    response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString());

        MessageReader messageReader=new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());

         if(receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
        {
            //....

        }

    }
    /*
     * getResources
     * Sends a GamePeer request message. Then rebuild the object resource list of the String received.
     * Returns an Arraylist of the game peer resources     *
     */
    public ArrayList<Object> getResources() 
    {

        Message message=new GamePeerResourcesRequest();
        String response = null;
      
    response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString());

        MessageReader messageReader=new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());

        if(receivedMessage.getMessageType().equals("GPRESOURCES"))
        {
            
            ArrayList<Object> Resources = new ArrayList<Object>();            
            GamePeerResources gpr=new GamePeerResources(receivedMessage);
            String resources=gpr.getResources();

            String []Array_resources=resources.split("\\:");
            int n=Array_resources.length;

            /*
             * This loop rebuilds the game peer resources received as String
             */
            for(int i=0;i<n;i++)
            {

                String []tmp=Array_resources[i].split("\\#");

                 if(tmp[0].equals("GameResourceEvolve")) // rebuilds a Game resource evolve object, then add it to the ArrayList
                {
                    String id=tmp[1];
                    String description=tmp[2];
                    double quantity=Double.parseDouble(tmp[3]);
                    double period=Double.parseDouble(tmp[4]);
                    double offset=Double.parseDouble(tmp[5]);

                    GameResourceEvolve gre=new GameResourceEvolve(id,description,quantity,(long)period,offset);

                    Resources.add(gre);
                }
                else if(tmp[0].equals("GameResourceMobile")) // rebuilds a Game resource mobile object, then add it to the ArrayList
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

                        /*
                         * This loop rebuilds the vision information object list of the game resource mobile
                         */
                        for(int j=0;j<nv;j++)
                        {
                            String [] Array_vision=Array_v[j].split("\\;");
                            String type=Array_vision[0];

                            if(type.equals("GamePlayerResponsible")) // rebuilds a GamePlayer Responsible object and adds it to the resource vision obejct list of the game resource mobile
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
                            else if(type.equals("GameResourceMobileResponsible")) // rebuilds a Game resource mobile Responsible object and adds it to the resource vision obejct list of the game resource mobile
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
                            else if(type.equals("null"))
                            {
                            	int pos=Integer.parseInt(Array_vision[1]);
                            	grm.addToResourceVision(null, pos);
                            	
                            }


                        }
                    }



                    Resources.add(grm);

                }
                else if(tmp[0].equals("GameResource")) // rebuilds a Game resource object, then add it to the ArrayList
                {
                    String id=tmp[1];
                    String description=tmp[2];
                    double quantity=Double.parseDouble(tmp[3]);

                    GameResource gr=new GameResource(id,description,quantity);

                    Resources.add(gr);

                }

            }

           

            return Resources;

        }

        return null;
    }


    /*
     * getGamePeerInfo
     * Sends an info gamepeer request message by socket.
     * then creates an GamePeerInfo object with the information retrieved
     * Finally, returns this object
     */
    public GamePeerInfo getGamePeerInfo()
    {
        Message message=new InfoGamePeerRequestMessage();

        String response = null;
        
    response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString());

        MessageReader messageReader=new MessageReader();
        Message receivedMessage = messageReader.readMessageFromString(response.trim());

        if(receivedMessage.getMessageType().equals("GAMEPEERINFO"))
        {
            
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

/*
 * getMobileResource
 * Sends a message request asking a mobile resource by id. Then builds a Mobile Resource object
 * with the received informations, and returns it
 * Parameters:
 * resource_id: String
 */
public GameResourceMobile getMobileResource(String resource_id) 
{
    Message message=new MobileResourceFromIDRequestMessage(resource_id);

    String response = null;
    
response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString());

    MessageReader messageReader=new MessageReader();
    Message receivedMessage = messageReader.readMessageFromString(response.trim());

    if(receivedMessage.getMessageType().equals("GPRESOURCEMOBILE"))
    {
        
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

        if(tmp.length==12) // if the resource mobile has a vision information list
        {
            String rvision=tmp[11];
            //System.out.println("RESOURCE_VISION SENDER"+ rvision);
            String []Array_v=rvision.split("\\$");
            int nv=Array_v.length;

            for(int j=0;j<nv;j++) //this loop rebuilds the vision information list
            {
                String [] Array_vision=Array_v[j].split("\\;");
                String type=Array_vision[0];

                        if(type.equals("GamePlayerResponsible")) //builds an GamePlayerResponsibleObject
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
                        else if(type.equals("GameResourceMobileResponsible"))//builds an GameResourceMobileResponsibleObject
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
                        else if(type.equals("null"))
                        {
                        	int pos=Integer.parseInt(Array_vision[1]);
                        	grm.addToResourceVision(null, pos);
                        	
                        }


            }

        }




            return grm;

    }



    return null;
}

/*
 * getMyResourceFromId
 * Sends a message request asking a resource by id. Then builds a Resource object
 * with the received informations, and returns it
 * Parameters:
 * r_id: String 
 */
public GameResource getMyResourceFromId(String r_id) 
{
    Message message=new ResourceFromIDRequestMessage(r_id);

    String response = null;
   
response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString());

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
                        else if(type.equals("null"))
                        {
                        	int pos=Integer.parseInt(Array_vision[1]);
                        	grm.addToResourceVision(null, pos);
                        	
                        }


                    }

                   }

                   game_resource=grm;

                }

                return game_resource;
    }


    return null;
}


/*
 *  getVision
 *  Sends a Game peer vision request message.
 *  Then builds the game peer vision's list using the received String
 *  
 */
public ArrayList<Object> getVision() 
{
    Message message=new GamePeerVisionRequest();


    String response = null;
   
response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString());  // FIXME

    MessageReader messageReader=new MessageReader();
    Message receivedMessage = messageReader.readMessageFromString(response.trim());

    if(receivedMessage.getMessageType().equals("GPVISION"))
    {
        
        GamePeerVision gpv=new GamePeerVision(receivedMessage);
        String str_vision=gpv.getVision();

        ArrayList<Object> vision=new ArrayList<Object>();

        String []Array_v=str_vision.split("\\$");
        int nv=Array_v.length;
        /*
         * Rebuilds the vision information list of the game peer 
         */
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

/*
 * getResourcesSize
 * Sends a resourceSizeRequest message 
 * Then returns the resources list's size
 **/
public int getResourcesSize()
{

    Message message=new ResourcesSizeRequestMessage();


    String response = null;
   
response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString()); // FIXME

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

/*
 * getGamePlayer()
 * Sends a GamePlayerRequest message. then builds and returns an GamePlayers objects with 
 * the arrived informations
 * */
public GamePlayer getGamePlayer()  
{
    Message message=new GamePlayerRequestMessage();

    String response = null;
   
response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString());  // FIXME leggi variabile
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

/*
 * getGranularity
 * Sends a granularity request message by socket. Then returns the game world's granularity
 * */
public double getGranularity()
{

    Message message=new GranularityRequestMessage();

    String response = null;
    
response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString()); // FIXME
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

/*
 *  UpdateResourceEvolve
 *  Sends an Update resource evolve message with the quantity to update
 *  parameters:
 *  quantity: double
 * */
public void UpdateResourceEvolve(double quantity)
{
    Message message=new UpdateResourceEvolveMessage(quantity);

    String response = null;
    
response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1", 1235, message.generateXmlMessageString()); // FIXME
    MessageReader messageReader=new MessageReader();
    Message receivedMessage = messageReader.readMessageFromString(response.trim());

    if(receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
    {
     //............
    }


}

/*
 * MovementRequest
 * Sends a movement request message with the target informations for perform the movement
 * Parameters:
 * targetx: double
 * targety: double
 * resId: String
 * threadId: String
 * */
public void MovementRequest(double targetx,double targety,String resId,String threadId)
{
    Message message=new MovementRequestMessage(targetx,targety,resId,threadId);
    String response = null;
    
response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString()); // FIXME

    MessageReader messageReader=new MessageReader();
    Message receivedMessage = messageReader.readMessageFromString(response.trim());

    if(receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
    {
     //............
    }



}

/*
 * CreateGamePeer
 * Sends a create game peer request message by socket, with information required to perform the gamepeer's creation
 * Parameters:
 * inPort: int
 * outPort: int
 * idBitLength: int
 * id: String
 * serverAddr: String
 * serverPort: int
 * gameInPort: int
 * gameOutPort: int
 * gameServerAddr: String
 * gameServerPort: int
 * stab: int
 * fix: int
 * check: int
 * pub: int 
 * */
public void CreateGamePeer(int inPort, int outPort, int idBitLength, String id, String serverAddr, int serverPort, int gameInPort, int gameOutPort, String gameServerAddr, int gameServerPort,
			int stab, int fix, int check, int pub)
{

    Message message=new CreateGamePeerRequestMessage(inPort,outPort,idBitLength,id,serverAddr,serverPort,gameInPort,gameOutPort,gameServerAddr,gameServerPort,stab,fix,check, pub);
    String response = null;
   
response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",9998, message.generateXmlMessageString()); // FIXME era 9999

    MessageReader messageReader=new MessageReader();
    Message receivedMessage = messageReader.readMessageFromString(response.trim());

    if(receivedMessage.getMessageType().equals("SUCCESSMESSAGE"))
    {
     //............
    }



}

/*
 * GamePeerExist
 * sends a GamePeerExistRequest message, asking if the game peer exist or not. Then returns the success of the request (true or false)
 * */
public boolean GamePeerExist()
{
    Message message=new GamePeerExistRequestMessage();
    String response = null;
   
response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",9998, message.generateXmlMessageString()); // FIXME era 9999

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

/*
 * getIpAddress
 * sends an IpAddressRequest message by socket. Then returns
 * the game peer's ip address 
 */

public String getIpAddress()
{
    Message message=new IpAddressRequestMessage();
    String response = null;
    
response=it.simplexml.sender.MessageSender.sendMessage("127.0.0.1",1235, message.generateXmlMessageString()); // FIXME

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
