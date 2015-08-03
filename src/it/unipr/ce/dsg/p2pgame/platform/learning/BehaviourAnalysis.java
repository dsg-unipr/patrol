package it.unipr.ce.dsg.p2pgame.platform.learning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import it.simplexml.message.AckMessage;
import it.simplexml.message.Message;
import it.simplexml.message.MessageReader;
import it.simplexml.sender.MessageSender;
import it.unipr.ce.dsg.p2pgame.network.NetPeer;
import it.unipr.ce.dsg.p2pgame.network.NetPeerInfo;
import it.unipr.ce.dsg.p2pgame.platform.Clash;
import it.unipr.ce.dsg.p2pgame.platform.GamePeer;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceEvolve;
import it.unipr.ce.dsg.p2pgame.platform.GameResourceMobile;
import it.unipr.ce.dsg.p2pgame.platform.learning.message.CheaterAnalysisMessage;
import it.unipr.ce.dsg.p2pgame.platform.learning.message.CheaterOpinionMessage;
import it.unipr.ce.dsg.p2pgame.platform.learning.message.CheaterOpinionMessageListener;
import it.unipr.ce.dsg.p2pgame.util.MultiLog;

public abstract class BehaviourAnalysis {


	private GamePeer peer;

	//private HashMap<String, Clash> clashes;

	private HashMap<String, Profile> profiles;

	private int temporalWindow;

	private int cheaterInputPort;
	private int cheaterOutputPort;

	public BehaviourAnalysis(GamePeer peer, int window, int cheaterInPort, int cheaterOutPort) {

		super();

		this.cheaterInputPort = cheaterInPort;
		this.cheaterOutputPort = cheaterOutPort;

		(new Thread(new CheaterOpinionMessageListener(this, this.peer.getMyId(), this.peer.getMyPeer().getIpAddress(), this.cheaterInputPort))).start();


		this.peer = peer;

		//this.clashes = this.peer.getClashes();
		this.initializeProfiles(this.peer.getClashes());



		this.temporalWindow = window;
	}


	private void initializeProfiles(HashMap<String, Clash> clashes){

		if (clashes.size() != 0){

			Set<String> key_Set = clashes.keySet();
			Iterator<String> iter = key_Set.iterator();

			while(iter.hasNext()){

				//da usare come id in profiles
				String key = iter.next();

				Clash clash = clashes.get(key);

				Profile profile = new Profile();

				profile.setTime(clash.getTiming());

				ArrayList<Object> res = clash.getOtherPlayerMoves();
				ArrayList<Double> entity = new ArrayList<Double>();

				for (int i = 0; i < res.size(); i++){

					if (res.get(i) instanceof GameResourceMobile){
						entity.add( ((GameResourceMobile)res.get(i)).getQuantity() );
					}
					else if (res.get(i) instanceof GameResourceEvolve){

						entity.add( ((GameResourceEvolve) res.get(i)).getQuantity() * (-1) );

					}
				}

				profile.setEntity(entity);

				this.profiles.put(key, profile);
			}

		}

	}

	//vede per ogni avversario se ora si hanno piï¿½ informazioni
	public void updateProfiles(HashMap<String, Clash> clashes){

		if (clashes.size() != 0){

			Set<String> key_Set = clashes.keySet();
			Iterator<String> iter = key_Set.iterator();

			while(iter.hasNext()){

				//da usare come id in profiles
				String key = iter.next();

				if ((this.profiles.containsKey(key) && this.profiles.get(key).getEntity().size() != clashes.get(key).getOtherPlayerMoves().size())
						|| !this.profiles.containsKey(key)){

					//ricarica le info sul giocatore
					Clash clash = clashes.get(key);

					Profile profile = new Profile();

					profile.setTime(clash.getTiming());

					ArrayList<Object> res = clash.getOtherPlayerMoves();
					ArrayList<Double> entity = new ArrayList<Double>();

					for (int i = 0; i < res.size(); i++){

						if (res.get(i) instanceof GameResourceMobile){
							entity.add( ((GameResourceMobile)res.get(i)).getQuantity() );
						}
						else if (res.get(i) instanceof GameResourceEvolve){

							entity.add( ((GameResourceEvolve) res.get(i)).getQuantity() * (-1) );

						}
					}

					profile.setEntity(entity);

					this.profiles.put(key, profile);
				}

			}

		}
	}

	public double getCheaterProb(String id){

		if (this.profiles.containsKey(id)){

			return this.profiles.get(id).getCheaterProb();

		}

		else
			return -1;
	}



	public GamePeer getPeer() {
		return peer;
	}


	public void setPeer(GamePeer peer) {
		this.peer = peer;
	}


	public HashMap<String, Profile> getProfiles() {
		return profiles;
	}


	public void setProfiles(HashMap<String, Profile> profiles) {
		this.profiles = profiles;
	}


	public int getTemporalWindow() {
		return temporalWindow;
	}


	public void setTemporalWindow(int temporalWindow) {
		this.temporalWindow = temporalWindow;
	}


	//invia i messaggi per://prova a chiedere a tutti gli altri nella finger table la loro opinione
	public ArrayList<Double> requestOpinion(String id){

		ArrayList<Double> resp = new ArrayList<Double>();

		for (int i = 0; i < this.peer.getFingerEntry().size(); i++){

			CheaterAnalysisMessage msg = new CheaterAnalysisMessage(this.peer.getMyId(), this.peer.getMyPeer().getIpAddress(),
					this.cheaterOutputPort, id);


			NetPeerInfo remotePeer = this.peer.getFingerTable().get(this.peer.getFingerEntry().get(i));

			String responseMessage = MessageSender.sendMessage(remotePeer.getIpAddress(), remotePeer.getPortNumber(), msg.generateXmlMessageString());


			if(responseMessage.contains("ERROR")) {

				System.err.println("Sending Request cheater opinion Message ERROR !");

			}
			else {

				MessageReader messageReader = new MessageReader();
				Message receivedMessage = messageReader.readMessageFromString(responseMessage.trim());

				CheaterOpinionMessage opinionMessage = new CheaterOpinionMessage(receivedMessage);

				//If ack message status is 0
				if(opinionMessage.getOpinion() != -1) {

					resp.add(opinionMessage.getOpinion());
				}

			}
		}


		return resp;

	}

	//esegue il calcolo interno con una delle reti disponibili e poi chiede agli altri peer che conosce(dove la lista ï¿½ ottenuta dalla finger table)
	public abstract double calculateCheaterProb(String id);

}
