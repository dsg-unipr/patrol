/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unipr.ce.dsg.p2pgame.GUI.test;

import it.simplexml.message.Message;
import it.simplexml.message.MessageReader;
import it.unipr.ce.dsg.p2pgame.GUI.message.CreateGamePeerRequestMessage;
import it.unipr.ce.dsg.p2pgame.GUI.message.GamePeerExistRequestMessage;
import it.unipr.ce.dsg.p2pgame.GUI.message.RegisterRequestMessage;
import it.unipr.ce.dsg.p2pgame.GUI.message.RegisterResponseMessage;
import it.unipr.ce.dsg.p2pgame.GUI.message.StartMessage;
import it.unipr.ce.dsg.p2pgame.GUI.message.SuccessMessage;
import it.unipr.ce.dsg.p2pgame.platform.GamePeer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pelito
 */
public class MainGamePeer extends Thread{

    private GamePeer gp = null;
    ServerSocket server=null;
    private GUIMessageListener message_listener;

    
    @Override
    public void run()
    {
        System.out.println("In attessa");
        Socket clientSocket = null;
        

        try {
		if (server == null)
                	server = new ServerSocket(9999);

            } catch (IOException e) {
		e.printStackTrace();
            }

        while(true){
            try {
                clientSocket = server.accept();
                DataInputStream is = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());
                String message = null;
                while(true){

                    int current = 0;
                    byte[] buf = new byte[100000];

                    while (current < 1) {

			int reader = is.read(buf);

			if (reader != -1){
			message = new String(buf);
			current++;
			}
                    }

                    checkIncomingMessage(message, os);

                    is.close();
		    os.close();
		    clientSocket.close();
		    break;
                }

            } catch (IOException ex) {
                Logger.getLogger(MainGamePeer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void checkIncomingMessage(String messageString, DataOutputStream os) throws IOException {

        MessageReader messageReader = new MessageReader();
	Message receivedMessage = messageReader.readMessageFromString(messageString.trim());
        
        
        if(receivedMessage.getMessageType().equals("CREATEGAMEPEERREQUEST"))
        {
            this.CreateGamePeerAction(receivedMessage, os);
        }
        else if(receivedMessage.getMessageType().equals("REGISTERREQUEST"))
        {
            this.RegisterRequestAction(receivedMessage, os);
        }
        else if(receivedMessage.getMessageType().equals("START"))
        {
            this.StartMessageAction(receivedMessage, os);
        }
         else if(receivedMessage.getMessageType().equals("GPEXISTREQUEST"))
        {
            this.GamePeerExistAction(receivedMessage, os);
        }



    }

    private void CreateGamePeerAction(Message receivedMessage, DataOutputStream os) throws IOException
    {
        CreateGamePeerRequestMessage request=new CreateGamePeerRequestMessage(receivedMessage);

        int inPort=request.getInPort();
        int outPort=request.getOutPort();
        int idBitLength=request.getIdBitLength();
        String id=request.getId();
        String serverAddr=request.getServerAddr();
        int serverPort=request.getServerPort();
        int gameInPort=request.getGameInPort();
        String gameServerAddr=request.getGameServerAddr();
        int gameOutPort=request.getGameOutPort();
        int gameServerPort=request.getGameServerPort();
        int stab=request.getStab();
        int fix=request.getFix();
        int check=request.getCheck();
        int pub=request.getPub();

        this.gp=new GamePeer(inPort,outPort,idBitLength,id,serverAddr,serverPort,gameInPort,gameOutPort,gameServerAddr,gameServerPort, stab,fix,check, pub);

        this.message_listener=new GUIMessageListener(gp);
        this.message_listener.start();

         System.out.println("creato gamepeer e messagelistener");

        SuccessMessage success=new SuccessMessage(true);

        if(this.gp==null){
            success.setSuccess(false);
        }

        os.write(success.generateXmlMessageString().getBytes());

    }

    private void RegisterRequestAction(Message receivedMessage, DataOutputStream os) throws IOException
    {
         //RegisterRequestMessage register=(RegisterRequestMessage)receivedMessage;
            RegisterRequestMessage register=new RegisterRequestMessage(receivedMessage);

            String user=register.getUserName();
            String password=register.getPassword();

            this.gp.registerOnServer(user, password);

            System.out.println("user registrato nel server");

            RegisterResponseMessage response=new RegisterResponseMessage("yes");

            os.write(response.generateXmlMessageString().getBytes());

    }

    private void StartMessageAction(Message receivedMessage, DataOutputStream os) throws IOException
    {
        //StartMessage start =(StartMessage)receivedMessage;
               StartMessage start =new StartMessage(receivedMessage);
               double minx=start.getMinX();
               double maxx=start.getMaxX();
               double miny=start.getMinY();
               double maxy=start.getMaxY();
               double minz=start.getMinZ();
               double maxz=start.getMaxZ();
               double vel=start.getVel();
               double gran=start.getGran();
               double vis=start.getVis();


               this.gp.startGame(minx, maxx, miny, maxy, minz, maxz, vel, vis, gran);

               System.out.println("game started");

                SuccessMessage success=new SuccessMessage(true);

                os.write(success.generateXmlMessageString().getBytes());

    }

    public void GamePeerExistAction(Message receivedMessage, DataOutputStream os) throws IOException
    {
          SuccessMessage success=new SuccessMessage(true);

          

          if(this.gp==null){
            success.setSuccess(false);
          }

          os.write(success.generateXmlMessageString().getBytes());


    }

    public static void main(String [] arg)
    {
        MainGamePeer main=new MainGamePeer();
        main.start();





    }




}

    
    
   




