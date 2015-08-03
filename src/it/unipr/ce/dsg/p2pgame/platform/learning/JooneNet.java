package it.unipr.ce.dsg.p2pgame.platform.learning;

import java.util.ArrayList;

import org.joone.engine.Layer;
import org.joone.helpers.factory.JooneTools;
import org.joone.io.MemoryInputSynapse;
import org.joone.io.MemoryOutputSynapse;
import org.joone.io.StreamInputSynapse;
import org.joone.net.NeuralNet;
import org.joone.net.NeuralNetLoader;

import weka.core.Instance;

import it.unipr.ce.dsg.p2pgame.platform.GamePeer;

public class JooneNet extends BehaviourAnalysis implements CheaterNet {


	public NeuralNet net;

	public JooneNet(GamePeer peer, int window, int inPort, int outPort, String filename) {
		super(peer, window, inPort, outPort);


		this.restoreNeuralNet(filename);
	}

	public void restoreNeuralNet(String filename) {
		/* We need just to provide the serialized NN file name */ //estensione .snet
		NeuralNetLoader loader = new NeuralNetLoader(filename);
		this.net = loader.getNeuralNet();



		/*
		* After that, we can restore all the internal variables to manage
		* the neural network and, finally, we can run it.
		*/
//		/* The main application registers itself as a NNï¿½s listener */
//		this.net.getMonitor().addNeuralNetListener(this);
		/* Now we can run the restored net */
		this.net.start();
		this.net.getMonitor().Go();
	}


	public double calculateCheaterProb(String id) {
		double cheater = 0;

		if (this.getProfiles().containsKey(id)){

			Profile profile = this.getProfiles().get(id);

			//ho le info sufficenti a creare l'istanza da sottomettere alla rete
			if (profile.getTime().size() >= this.getTemporalWindow()){

				double[][] inputArray = new double[0][];
				ArrayList<Double> inputList = new ArrayList<Double>();

				for (int t = profile.getTime().size()-1; t > (profile.getTime().size()-1) - this.getTemporalWindow() ; t--){

					inputList.add(Double.parseDouble(profile.getTime().get(t)));

				}

				for (int t = profile.getEntity().size()-1; t > (profile.getEntity().size()-1) - this.getTemporalWindow() ; t--){
					inputList.add(Double.parseDouble(profile.getTime().get(t)));
				}

				//converte da ArrayList a double[][];

				Double[] inputData = (Double[]) inputList.toArray();
				for (int i = 0; i < inputData.length; i++){

					inputArray[0][i] = inputData[i].doubleValue();

				}



				if (this.net != null) {
					/* We get the first layer of the net (the input layer),
				then remove all the input synapses attached to it
				and attach a MemoryInputSynapse */
					Layer input = this.net.getInputLayer();
					input.removeAllInputs();
					MemoryInputSynapse memInp = new MemoryInputSynapse();
					memInp.setFirstRow(1);
					//non usato il selector delle colonne dovendo usare tutto l'input
					//memInp.setAdvancedColumnSelector("1,2");
					input.addInputSynapse(memInp);
					memInp.setInputArray(inputArray);
					/* We get the last layer of the net (the output layer),
				then remove all the output synapses attached to it
				and attach a MemoryOutputSynapse */
					Layer output = this.net.getOutputLayer();
					// Remove all the output synapses attached to it...
					output.removeAllOutputs();
					//...and attach a MemoryOutputSynapse
					MemoryOutputSynapse memOut = new MemoryOutputSynapse();
					output.addOutputSynapse(memOut);
					// Now we interrogate the net
					this.net.getMonitor().setTotCicles(1);
					this.net.getMonitor().setTrainingPatterns(1);
					this.net.getMonitor().setLearning(false);
					this.net.go();
					//for (int i=0; i < 4; ++i) {
						// Read the next pattern and print out it
					double[] pattern = memOut.getNextPattern();

						//System.out.println("Output Pattern #"+(i+1)+" = "+pattern[0]);
					//}
					this.net.stop();
					//System.out.println("Finished");

					cheater = pattern[0];
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
