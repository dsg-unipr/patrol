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
public class CheaterAnalysisMessage extends Message {


	private String idAnalyzed = null;

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
	public CheaterAnalysisMessage(String sourceName, String sourceSocketAddr, int sourcePort, String id) {

		super(sourceName,sourceSocketAddr,sourcePort);

		this.setMessageType("CHEATER_ANALYSIS");
		this.PARAMETERS_NUM = 4;

		this.getParametersList().add(new Parameter("idAnalyzed", id));

		this.idAnalyzed = id;

	}

	/**
	 *
	 * The constructor from parameter message.
	 * Is used for reconstruct Notify message on reception
	 *
	 * @param message the message by which reconstruct
	 *
	 */
	public CheaterAnalysisMessage(Message message){

		super(message.getSourceName(), message.getSourceSocketAddr(), message.getSourcePort());

		this.setMessageType("CHEATER_ANALYSIS");
		this.PARAMETERS_NUM = 4;

		for (int index = 3; index < message.getParametersList().size(); index++) {
			this.getParametersList().add(message.getParametersList().get(index));
		}


		this.idAnalyzed = this.getParametersList().get(3).getValue();
	}

	public String getIdAnalyzed() {
		return idAnalyzed;
	}

	public void setIdAnalyzed(String idAnalyzed) {
		this.idAnalyzed = idAnalyzed;
	}


}
