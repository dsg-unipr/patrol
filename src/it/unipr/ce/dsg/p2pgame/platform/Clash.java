package it.unipr.ce.dsg.p2pgame.platform;

import java.util.ArrayList;


public class Clash {


//	public final static int HASH = 0;
//	public final static int DEFENSE = 1;
//	public final static int END = 2;
public	enum Phase {
		HASH,
		DEFENSE,
		END
	};

public	enum Result {
		WIN,
		LOSE
	};
//	public final static int WIN = 0;
//	public final static int LOSE = 1;


	private ArrayList<Object> myMoves;
	private ArrayList<Object> otherPlayerMoves;
	private ArrayList<String> hash;
	private ArrayList<Result> results;
	private ArrayList<String> timing;

	private String otherPlayer;
	private String otherPlayerId;

	//private int statusLast;
	private Phase statusLast;

	public Clash(String otherPlayer, String otherPlayerId) {
		super();
		this.otherPlayer = otherPlayer;
		this.otherPlayerId = otherPlayerId;


		this.myMoves = new ArrayList<Object>();
		this.otherPlayerMoves = new ArrayList<Object>();
		this.results = new ArrayList<Result>();
		this.hash = new ArrayList<String>();
		this.timing=new ArrayList<String>();

	}

	public void addMyMove(Object move){
		this.myMoves.add(move);
	}

	public void addOtherPlayerMove(Object move){

		this.otherPlayerMoves.add(move);

		if (this.timing.size() != 0){
			long oldTime = Long.parseLong(this.timing.get(this.timing.size()-1));
			long time = System.currentTimeMillis() -  oldTime;

			this.timing.add(Long.toString(time));
		}

	}

	public void addResult(Result win){
		this.results.add(win);
	}

	public void addHash(String hash){
		this.hash.add(hash);
	}

	public ArrayList<Object> getMyMoves() {
		return myMoves;
	}

	public void setMyMoves(ArrayList<Object> myMoves) {
		this.myMoves = myMoves;
	}

	public ArrayList<Object> getOtherPlayerMoves() {
		return otherPlayerMoves;
	}

	public void setOtherPlayerMoves(ArrayList<Object> otherPlayerMoves) {
		this.otherPlayerMoves = otherPlayerMoves;
	}

	public ArrayList<Result> getResults() {
		return results;
	}

	public void setResults(ArrayList<Result> results) {
		this.results = results;
	}

	public String getOtherPlayer() {
		return otherPlayer;
	}

	public void setOtherPlayer(String otherPlayer) {
		this.otherPlayer = otherPlayer;
	}

	public String getOtherPlayerId() {
		return otherPlayerId;
	}

	public void setOtherPlayerId(String otherPlayerId) {
		this.otherPlayerId = otherPlayerId;
	}



	public ArrayList<String> getTiming() {
		return timing;
	}

	//	public int getStatusLast() {
//		return statusLast;
//	}
	public Phase getStatusLast() {
		return statusLast;
	}

//	public void setStatusLast(int statusLast) {
//		this.statusLast = statusLast;
//	}
	public void setStatusLast(Phase statusLast) {
		this.statusLast = statusLast;
	}

	public ArrayList<String> getHash() {
		return hash;
	}

	public void setHash(ArrayList<String> hash) {
		this.hash = hash;
	}

	//TODO: per calcolare il vincitore
	//Jose' Murga 05/08/2011
	//si calcola in base alla quantità di risorsa coinvolta nello scontro. Se la quantità è uguale entrambi perdono
	public void closeClash(){
		int pos = this.myMoves.size()-1;

		if (this.otherPlayerMoves.get(pos) instanceof Attack){
			Attack other = (Attack) this.otherPlayerMoves.get(pos);
			Defense my = (Defense) this.myMoves.get(pos);

			//TODO : calcolo del vincitore
			double otherquantity=other.getQuantity();
			double myquantity=my.getQuantity();
			
			if(myquantity>otherquantity)
			{
				this.addResult(Result.WIN);
			}
			else
			{
				this.addResult(Result.LOSE);
				
			}
			
			
			

		}
		else{
			Defense other = (Defense) this.otherPlayerMoves.get(pos);
			Attack my = (Attack) this.myMoves.get(pos);

			//TODO: calcolo del vincitore
			double otherquantity=other.getQuantity();
			double myquantity=my.getQuantity();
			
			if(myquantity>otherquantity)
			{
				this.addResult(Result.WIN);
			}
			else
			{
				this.addResult(Result.LOSE);
				
			}
		}
	}

	//TODO: verificare l'attacco che si era ricevuto precedentemente
	public boolean verifyAttack(){
		int pos = this.myMoves.size()-1;
		Attack attack = (Attack) this.otherPlayerMoves.get(pos);
		if (attack.getHash().compareTo(this.getHash().get(pos)) == 0)
		{
			System.out.println("TRUE: "+attack.getHash()+" "+this.getHash().get(pos));
			return true;
			
		}
		else{
			
			System.out.println("FALSE: "+attack.getHash()+" "+this.getHash().get(pos));
			return false;
		}
			
	}
}
