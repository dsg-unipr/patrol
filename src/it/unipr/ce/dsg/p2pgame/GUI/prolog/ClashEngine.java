package it.unipr.ce.dsg.p2pgame.GUI.prolog;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import alice.tuprolog.NoSolutionException;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Var;
import it.unipr.ce.dsg.p2pgame.platform.prolog.PrologEngine;

public class ClashEngine extends PrologEngine {

	public ClashEngine() {
		super();
		
	}

	public ClashEngine(String theory) {
		super(theory);
		
	}
	
	
	public void createClashTheory(String id1,double q1,String id2,double q2 )
	{
		this.appendTheory("player1("+id1+","+q1+").");
		this.appendTheory("player2("+id2+","+q2+").");
		
		
		
	}
	
	public String getClashWinner()
	{
		String winner="";
		
		String query="winner(P).";
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
	
	public boolean isDraw()
	{
		String query="draw.";
        ArrayList<SolveInfo > arrayinfo=this.solveQuery(query);
        
        if(!arrayinfo.isEmpty())
        {
        	return true;
        	
        }
		
		return false;
	}
	
	
	public static void main(String []arg)
	{
		ClashEngine engine=new ClashEngine("rules/clashTheory.pl");
		
		engine.createClashTheory("id1", 50, "id2", 40);
		
		if(engine.isDraw())
		{
			System.out.println("draw");
		}
		else
		{
			String winner=engine.getClashWinner();
			System.out.println("winner: "+winner);
			
		}
		
		
		
		
		
	}
	
	
	
	
	

}

