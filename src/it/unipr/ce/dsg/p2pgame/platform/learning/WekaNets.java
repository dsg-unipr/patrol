package it.unipr.ce.dsg.p2pgame.platform.learning;

import java.util.ArrayList;

import weka.classifiers.pmml.consumer.NeuralNetwork;
import weka.core.Instance;
import it.unipr.ce.dsg.p2pgame.platform.GamePeer;


public class WekaNets extends BehaviourAnalysis implements CheaterNet {

	//public String netFile;

	public NeuralNetwork net;

	public WekaNets(GamePeer peer, int window, int inPort, int outPort, String netFile) {

		super(peer, window, inPort, outPort);
		// TODO Auto-generated constructor stub

		//this.netFile = netFile;

		// deserialize model
		 try {
			//Classifier cls = (Classifier) weka.core.SerializationHelper.read(netFile);
			 this.net = (NeuralNetwork) weka.core.SerializationHelper.read(netFile);
		} catch (Exception e) {
			System.out.println("Impossibile deserializzare con weka");
			e.printStackTrace();
		}



	}


	public double calculateCheaterProb(String id) {

		double cheater = 0;

		if (this.getProfiles().containsKey(id)){

			Profile profile = this.getProfiles().get(id);

			//ho le info sufficenti a creare l'istanza da sottomettere alla rete
			if (profile.getTime().size() >= this.getTemporalWindow()){

				Instance instance = new Instance(this.getTemporalWindow());

				int i = 0;
				for (int t = profile.getTime().size()-1; t > (profile.getTime().size()-1) - this.getTemporalWindow() ; t--){
					instance.setValue(i, Long.parseLong(profile.getTime().get(t)));

					i++;
				}

				for (int t = profile.getEntity().size()-1; t > (profile.getEntity().size()-1) - this.getTemporalWindow() ; t--){
					instance.setValue(i, profile.getEntity().get(t));

					i++;
				}

				//TODO: occorre permettere di accedere alle informazioni presenti nel dataset

				try {
					cheater = this.net.classifyInstance(instance);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		//prova a chiedere a tutti gli altri nella finger table la loro opinione
		ArrayList<Double> opinions = this.requestOpinion(id);

		//pesa la propria opinione con quella degli altri
		if (opinions.size() > 0){

			double otherOpinion = 0;

			for (int c =0; c < opinions.size(); c++){

				otherOpinion += opinions.get(c);

			}

			cheater = (cheater + otherOpinion)/(opinions.size()+1);
		}


		//salva il risultato in profile e lo restituisce
		if (this.getProfiles().containsKey(id)){

			Profile profile = this.getProfiles().get(id);
			profile.setCheaterProb(cheater);
			this.getProfiles().put(id, profile);

		}

		return cheater;
	}

}
