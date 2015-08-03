package it.unipr.ce.dsg.p2pgame.platform.learning.message;

import it.simplexml.message.Message;
import it.simplexml.message.Parameter;

/**
 *
 * This class provide the structure for message of Notify with a possible predecessor
 * on an existing peer in a Chord-like network
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class CheaterOpinionMessage extends Message {


	private double opinion = -1;

	/**
	 *
	 * The constructor for message to send with parameters from arguments.
	 * Arguments contains information of possible predecessor on ring
	 *
	 * @param sourceName the id of source peer
	 * @param sourceSocketAddr the IP address of source
	 * @param sourcePort the source port
	 *
	 */
	public CheaterOpinionMessage(String sourceName, String sourceSocketAddr, int sourcePort, double opinion) {

		super(sourceName,sourceSocketAddr,sourcePort);

		this.setMessageType("CHEATER_OP");
		this.PARAMETERS_NUM = 4;

		this.getParametersList().add(new Parameter("opinion", new Double(opinion).toString()));

		this.opinion = opinion;

	}

	/**
	 *
	 * The constructor from parameter message.
	 * Is used for reconstruct Notify message on reception
	 *
	 * @param message the message by which reconstruct
	 *
	 */
	public CheaterOpinionMessage(Message message){

		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());

		this.setMessageType("CHEATER_OP");
		this.PARAMETERS_NUM = 4;

		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}


		this.opinion = Double.parseDouble(this.getParametersList().get(3).getValue());
	}

	public double getOpinion() {
		return opinion;
	}

	public void setOpinion(double opinion) {
		this.opinion = opinion;
	}


}
