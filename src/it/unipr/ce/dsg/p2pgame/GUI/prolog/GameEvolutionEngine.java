package it.unipr.ce.dsg.p2pgame.GUI.prolog;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import alice.tuprolog.NoSolutionException;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Var;

import it.unipr.ce.dsg.p2pgame.platform.prolog.PrologEngine;

public class GameEvolutionEngine extends PrologEngine{
	
	public GameEvolutionEngine()
	{
		super();
		
	}
	
	public GameEvolutionEngine(String theory)
	{
		super(theory);
		
	}
	
	 public void clearParameters(String theory)
	 {
	        this.clearTheory();
	        
	            this.setTheory(theory);
	        

	 }
	 
	 public void createGameEvolutionTheory(double cmoney, 
			 ArrayList<String> cres,
			 ArrayList<Integer> cqres,
			 double req_exp_money,
			 ArrayList<String> req_exp_res,
			 ArrayList<Integer> req_exp_qres,
			 double req_conq_money,
			 ArrayList<String> req_conq_res,
			 ArrayList<Integer> req_conq_qres)
	 		 
	 {
		 
		 this.appendTheory("current_money("+(int)cmoney+")."); 
		 
		 String str_aux="[";
		 
		 for(int i=0;i<cres.size();i++)
		 {
			 str_aux+=cres.get(i);
			 if(i<(cres.size()-1))
			 {
				 str_aux+=",";
			 }
			 
		 }
		 str_aux+="]";
		 
		 this.appendTheory("current_resources("+str_aux+").");
		 
		 
		 str_aux="[";
		 for(int i=0;i<cqres.size();i++)
		 {
			 str_aux+=cqres.get(i).intValue();
			 if(i<(cqres.size()-1))
			 {
				 str_aux+=",";
			 }
			 
		 }
		 str_aux+="]";
		 
		 this.appendTheory("current_qresources("+str_aux+").");
		 
		 this.appendTheory("required_exploration_money("+(int)req_exp_money+").");
		 
		 str_aux="[";
		 for(int i=0;i<req_exp_res.size();i++)
		 {
			 str_aux+=req_exp_res.get(i);
			 if(i<(req_exp_res.size()-1))
			 {
				 str_aux+=",";
			 }
			 
		 }
		 str_aux+="]";
		 
		 this.appendTheory("required_exploration_resources("+str_aux+").");
		 
		 
		 str_aux="[";
		 
		 for(int i=0;i<req_exp_qres.size();i++)
		 {
			 str_aux+=req_exp_qres.get(i).intValue();
			 if(i<(req_exp_qres.size()-1))
			 {
				 str_aux+=",";
			 }
			 
		 }
		 str_aux+="]";
		 
		 this.appendTheory("required_exploration_qresources("+str_aux+").");
		 
		 
		 this.appendTheory("required_conquest_money("+(int)req_conq_money+").");
		 
		 str_aux="[";
		 for(int i=0;i<req_conq_res.size();i++)
		 {
			 str_aux+=req_conq_res.get(i);
			 if(i<(req_conq_res.size()-1))
			 {
				 str_aux+=",";
			 }
			 
		 }
		 str_aux+="]";
		 
		 this.appendTheory("required_conquest_resources("+str_aux+").");
		 
		 
		 str_aux="[";
		 
		 for(int i=0;i<req_conq_qres.size();i++)
		 {
			 str_aux+=req_conq_qres.get(i).intValue();
			 if(i<(req_conq_qres.size()-1))
			 {
				 str_aux+=",";
			 }
			 
		 }
		 str_aux+="]";
		 
		 this.appendTheory("required_conquest_qresources("+str_aux+").");
		 
		 
		 
		 
	 }
	 
	 
	 public int getFase()
	 {
		 	String query="fase(F).";
		 	ArrayList<SolveInfo > arrayinfo=this.solveQuery(query);
	        SolveInfo info=arrayinfo.get(0);
	        List l=null;
	        try {
	            l = info.getBindingVars();
	            Var var=(Var) l.get(0);
	            String strval=var.toStringFlattened();
	            int val=Integer.parseInt(strval);
	            return val;

	        } catch (NoSolutionException ex) {
	            Logger.getLogger(VisibilityEngine.class.getName()).log(Level.SEVERE, null, ex);
	        }


	        return 0;
		 
		 
		 
	 }
	 
	 public double getRadius()
	 {
		 	String query="radius(R).";
		 	ArrayList<SolveInfo > arrayinfo=this.solveQuery(query);
	        SolveInfo info=arrayinfo.get(0);
	        List l=null;
	        try {
	            l = info.getBindingVars();
	            Var var=(Var) l.get(0);
	            String strval=var.toStringFlattened();
	            double val=Double.parseDouble(strval);
	            return val;

	        } catch (NoSolutionException ex) {
	            Logger.getLogger(VisibilityEngine.class.getName()).log(Level.SEVERE, null, ex);
	        }


	        return 0;
		 
	 }
	 
	 public int getProbAttack()
	 {
		    String query="probability_attack(P).";
	        ArrayList<SolveInfo > arrayinfo=this.solveQuery(query);
	        SolveInfo info=arrayinfo.get(0);
	        List l=null;
	        try {
	            l = info.getBindingVars();
	            Var var=(Var) l.get(0);
	            String strval=var.toStringFlattened();
	            int val=Integer.parseInt(strval);
	            return val;

	        } catch (NoSolutionException ex) {
	            Logger.getLogger(VisibilityEngine.class.getName()).log(Level.SEVERE, null, ex);
	        }


	        return 0;
		 
		 
	 }
	 
	 public int getProbDefense()
	 {
		 
		 return 100 - this.getProbAttack();
	 }
	 
	 public int getProbMovement()
	 {
		    String query="probability_movement(P).";
	        ArrayList<SolveInfo > arrayinfo=this.solveQuery(query);
	        SolveInfo info=arrayinfo.get(0);
	        List l=null;
	        try {
	            l = info.getBindingVars();
	            Var var=(Var) l.get(0);
	            String strval=var.toStringFlattened();
	            int val=Integer.parseInt(strval);
	            return val;

	        } catch (NoSolutionException ex) {
	            Logger.getLogger(VisibilityEngine.class.getName()).log(Level.SEVERE, null, ex);
	        }


	        return 0;
		 
	 }
	 
	 public int getProbBuy()
	 {
		    String query="probability_buy(P).";
	        ArrayList<SolveInfo > arrayinfo=this.solveQuery(query);
	        SolveInfo info=arrayinfo.get(0);
	        List l=null;
	        try {
	            l = info.getBindingVars();
	            Var var=(Var) l.get(0);
	            String strval=var.toStringFlattened();
	            int val=Integer.parseInt(strval);
	            return val;

	        } catch (NoSolutionException ex) {
	            Logger.getLogger(VisibilityEngine.class.getName()).log(Level.SEVERE, null, ex);
	        }


	        return 0;
		 
	 }
	 
	 
	 

}
