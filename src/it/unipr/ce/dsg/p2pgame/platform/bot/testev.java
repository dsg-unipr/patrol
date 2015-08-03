package it.unipr.ce.dsg.p2pgame.platform.bot;

import java.util.ArrayList;

import it.unipr.ce.dsg.p2pgame.GUI.prolog.GameEvolutionEngine;

public class testev {
	
	public static void main(String [] arg)
	{
		GameEvolutionEngine engine=new GameEvolutionEngine("rules/evolutionTheory.pl");
		
		int currentmoney=1;
		ArrayList<String> currentres=new ArrayList<String>();
		currentres.add("GameResource");
		currentres.add("GameResourceMobile");
		
		ArrayList<Integer> currentqres=new ArrayList<Integer>();
		currentqres.add(new Integer(2));
		currentqres.add(new Integer(5));
		
		int req_exp_money=0; 
		ArrayList<String> req_exp_res=new ArrayList<String>();
		req_exp_res.add("GameResource");
		req_exp_res.add("GameResourceMobile");
		
		ArrayList<Integer> req_exp_qres=new ArrayList<Integer>();
		req_exp_qres.add(new Integer(1));
		req_exp_qres.add(new Integer(3));
		int req_conq_money=0;
		ArrayList<String> req_conq_res=new ArrayList<String>();
		req_conq_res.add("GameResource");
		req_conq_res.add("GameResourceMobile");
		ArrayList<Integer> req_conq_qres=new ArrayList<Integer>();
		req_conq_qres.add(new Integer(3));
		req_conq_qres.add(new Integer(5));
		
		engine.createGameEvolutionTheory(currentmoney, currentres, currentqres, req_exp_money, req_exp_res, req_exp_qres, req_conq_money, req_conq_res, req_conq_qres);
		
		
		System.out.println("Ho creato la teoria");
		
		int pos=engine.getFase();
		
		System.out.println(pos);
		
		
		engine.printTheory();
		
		
		
	}

}
