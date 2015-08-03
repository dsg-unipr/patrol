package it.unipr.ce.dsg.p2pgame.GUI.prolog;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import alice.tuprolog.NoSolutionException;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Var;

import it.unipr.ce.dsg.p2pgame.platform.prolog.PrologEngine;

public class GameEngine extends PrologEngine{

	public GameEngine() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GameEngine(String theory) {
		super(theory);
		// TODO Auto-generated constructor stub
	}
	
	public void createGameTheory(ArrayList<String> players,ArrayList<Integer> conquered_planets, int total_planets)
	{
		this.appendTheory("total_planets("+total_planets+").");
		
		String str_array="[";
		for(int i=0;i<players.size();i++)
		{
			str_array+=players.get(i);
			if(i<(players.size()-1))
			{
				str_array+=",";				
			}
			
		}
		str_array+="]";
		this.appendTheory("list_players("+str_array+").");
		
		str_array="[";
		for(int i=0;i<conquered_planets.size();i++)
		{
			int aux=conquered_planets.get(i).intValue();
			str_array+=Integer.toString(aux);
			if(i<(conquered_planets.size()-1))
			{
				str_array+=",";	
			}
			
		}
		str_array+="]";
		this.appendTheory("list_conquered_planets("+str_array+").");
		
	}
	
	public boolean gameover()
	{
		String query="gameover(W).";
        ArrayList<SolveInfo > arrayinfo=this.solveQuery(query);
        
        if(!arrayinfo.isEmpty())
        {
        	return true;
        	
        }
		
		return false;
	}
	
	public String getGameWinner()
	{
		String winner="";
		
		String query="winner(W).";
        ArrayList<SolveInfo > arrayinfo=this.solveQuery(query);
        
        
        if(!arrayinfo.isEmpty())
        {
        	SolveInfo info=arrayinfo.get(0);
            List l=null;
            try {
                l = info.getBindingVars();
                Var var=(Var) l.get(0);
                winner=var.toStringFlattened();
               
            } catch (NoSolutionException ex) {
                Logger.getLogger(VisibilityEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
		return winner;
		
	}
	
	public static void main(String [] arg)
	{
		GameEngine engine=new GameEngine("rules/gameTheory.pl");
		
		ArrayList<String> players=new ArrayList<String>();
		
		players.add("p1");
		players.add("p2");
		
		
		ArrayList<Integer> conq=new ArrayList<Integer>();
		
		conq.add(new Integer(1));
		conq.add(new Integer(0));
		
		
		int total= 1;
		
		engine.createGameTheory(players, conq, total);
		
		if(engine.gameover())
		{
			String winner=engine.getGameWinner();
			
			System.out.println("Game over!!The Winner is "+winner);
		}
		else
		{
			System.out.println("It's not over!!");
			
		}
			
		
		
		
	}
	
	
	
	

}
