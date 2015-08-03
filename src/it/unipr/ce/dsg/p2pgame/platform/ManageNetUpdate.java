package it.unipr.ce.dsg.p2pgame.platform;

import java.util.ArrayList;

import weka.gui.SysErrLog;

public class ManageNetUpdate implements Runnable {

	private int periodStabilize;
	private int periodFixFinger;
	private int periodCheckPredecessor;
	private int periodPublish;


	private String threadId = new Long(Thread.currentThread().getId()).toString();

	private GamePeer peer;

	private ArrayList<String> orderedString;
	private ArrayList<Integer> orderedPeriod;

	//I periodi devono essere l'uno multiplo dell'altro per semplificare la gestione
	public ManageNetUpdate(int periodStabilize, int periodFixFinger,
			int periodCheckPredecessor, int periodPublish, GamePeer gp) {
		super();
		this.periodStabilize = periodStabilize;
		this.periodFixFinger = periodFixFinger;
		this.periodCheckPredecessor = periodCheckPredecessor;
		this.periodPublish = periodPublish;

		this.peer = gp;
		this.threadId=gp.getMyThreadId();
		this.orderedPeriod = new ArrayList<Integer>();
		this.orderedString = new ArrayList<String>();
	}


	public void run() {

		this.orderPeriod();

		this.printOrderedPeriod();

		
		this.peer.stabilize();
		try {
			this.peer.fixFinger(threadId);
		} catch (InterruptedException e1) {
			System.err.println("ManageNetUpdate InterruptedException fixFinger");
			e1.printStackTrace();
		}
		this.peer.checkPredecessor();
		try {
			this.peer.publishResource(threadId);
		} catch (InterruptedException e1) {
			System.err.println("ManageNetUpdate InterruptedException publishResource");
			e1.printStackTrace();
		}


		//load hyper-period
		try { //TODO: ripristinare...il while
			//while(true){


				for(int i=0; i < this.orderedPeriod.get(3) / this.orderedPeriod.get(2); i++){

					for (int j=0; j < this.orderedPeriod.get(2) / this.orderedPeriod.get(1); j++){

						for (int k=0; k < this.orderedPeriod.get(1) / this.orderedPeriod.get(0); k++){

							//Thread.currentThread();
							//System.out.println("PRE-sleep 0");
							Thread.sleep(this.orderedPeriod.get(0));
							//System.out.println("POST-sleep 0");
							this.lunchFunction(0);

						}
						//System.out.println("PRE-sleep 1");
						Thread.sleep(this.orderedPeriod.get(1));
						//System.out.println("POST-sleep 1");
						this.lunchFunction(1);
					}

					//System.out.println("PRE-sleep 2");
					Thread.sleep(this.orderedPeriod.get(2));
					//System.out.println("POST-sleep 2");
					this.lunchFunction(2);
				}

				//System.out.println("PRE-sleep 3");
				Thread.sleep(this.orderedPeriod.get(3));
				//System.out.println("POST-sleep 3");
				this.lunchFunction(3);

			//}
		} catch (InterruptedException e){
			System.out.println("Thread Update net info INTERRUPTED during a sleep");
			//e.printStackTrace();
		}

	}

	private void lunchFunction(int num) throws InterruptedException{

		if (this.orderedString.get(num).compareTo("Stabilize") == 0){
			//System.out.println("Lunching periodic stabilize");
			this.peer.stabilize();
		}
		else if (this.orderedString.get(num).compareTo("FixFinger") == 0) {
			//System.out.println("ManageNetUpdate--->Lunching periodic fixFinger");
			//this.peer.fixFinger(threadId);
		}
		else if (this.orderedString.get(num).compareTo("CheckPredecessor") == 0){
			//System.out.println("Lunching periodic checkPredecessor");
			this.peer.checkPredecessor();
		}
		else if (this.orderedString.get(num).compareTo("Publish") == 0){
			//System.out.println("Lunching periodic publish");
			this.peer.publishPosition(this.threadId);
			//this.peer.publishResourceMobile(threadId);
			//this.peer.publishResource(threadId);

		}
	}

	//ordina in modo crescente
	private void orderPeriod() {

		this.orderedString.add("Stabilize");
		this.orderedPeriod.add(this.periodStabilize);


		if (this.periodFixFinger >= this.periodStabilize){
			this.orderedString.add("FixFinger");
			this.orderedPeriod.add(this.periodFixFinger);
		}
		else {
			this.orderedString.add(0, "FixFinger");
			this.orderedPeriod.add(0,this.periodFixFinger);
		}


		if (this.periodCheckPredecessor >= this.orderedPeriod.get(1) ){
			this.orderedPeriod.add(this.periodCheckPredecessor);
			this.orderedString.add("CheckPredecessor");
		}
		else if (this.periodCheckPredecessor >= this.orderedPeriod.get(0) ) {
			this.orderedPeriod.add(1, this.periodCheckPredecessor);
			this.orderedString.add(1, "CheckPredecessor");
		}
		else {
			this.orderedPeriod.add(0, this.periodCheckPredecessor);
			this.orderedString.add(0, "CheckPredecessor");
		}


		if (this.periodPublish >= this.orderedPeriod.get(2)) {
			this.orderedPeriod.add(this.periodPublish);
			this.orderedString.add("Publish");
		}
		else if (this.periodPublish >= this.orderedPeriod.get(1)){
			this.orderedPeriod.add(2, this.periodPublish);
			this.orderedString.add(2, "Publish");
		}
		else if (this.periodPublish >= this.orderedPeriod.get(0)){
			this.orderedPeriod.add(1, this.periodPublish);
			this.orderedString.add(1, "Publish");
		} else {
			this.orderedPeriod.add(0, this.periodPublish);
			this.orderedString.add(0, "Publish");
		}

	}


	private void printOrderedPeriod(){

		System.out.println("Print of function to execute periodically");

		System.out.println("0  " + this.orderedString.get(0) + "  " + this.orderedPeriod.get(0));
		System.out.println("1  " + this.orderedString.get(1) + "  " + this.orderedPeriod.get(1));
		System.out.println("2  " + this.orderedString.get(2) + "  " + this.orderedPeriod.get(2));
		System.out.println("3  " + this.orderedString.get(3) + "  " + this.orderedPeriod.get(3));

	}
}
