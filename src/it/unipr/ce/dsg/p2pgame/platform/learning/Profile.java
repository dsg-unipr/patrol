package it.unipr.ce.dsg.p2pgame.platform.learning;

import java.util.ArrayList;

public class Profile {

	private ArrayList<Double> entity;

	private ArrayList<String> time;

	private double cheaterProb;

	public Profile(){
		this.entity = new ArrayList<Double>();
		this.time = new ArrayList<String>();
		this.cheaterProb = -1;
	}


	public ArrayList<Double> getEntity() {
		return entity;
	}


	public void setEntity(ArrayList<Double> entity) {
		this.entity = entity;
	}


	public ArrayList<String> getTime() {
		return time;
	}


	public void setTime(ArrayList<String> time) {
		this.time = time;
	}


	public double getCheaterProb() {
		return cheaterProb;
	}


	public void setCheaterProb(double cheaterProb) {
		this.cheaterProb = cheaterProb;
	}


}
