package it.unipr.ce.dsg.p2pgame.platform.prolog;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PrologEngine {

	private Theory theory;
	private Prolog engine;
	private ArrayList<SolveInfo> info;

	// default constructor
	public PrologEngine() 
	{
		this.engine = new Prolog();
		this.theory = null;
		this.info = new ArrayList<SolveInfo>();
	}

	// constructor with theory as String parameter
	public PrologEngine(String theory) 
	{
	    this.engine=new Prolog();
	    FileInputStream fis = null;
	    try {
	    	fis = new FileInputStream(theory);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        try {
            this.theory = new Theory(fis);
            this.engine.setTheory(this.theory);
        } catch (InvalidTheoryException ex) {
            Logger.getLogger(PrologEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
			e.printStackTrace();
		}
        this.info=new ArrayList<SolveInfo>();
	}

	// method for setting the theory
	public void setTheory(String theory) 
	{
		FileInputStream fis = null;
	    try {
	    	fis = new FileInputStream(theory);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			this.engine.clearTheory();
			this.theory = new Theory(fis);
			this.engine.setTheory(this.theory);
		} 
		catch (InvalidTheoryException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



    // method for adding a new thoery to the one the engine is currently using
	public void appendTheory(String theory) 
	{
		try {
			Theory appendTheory = new Theory(theory);
                        this.theory = this.engine.getTheory();
                        this.theory.append(appendTheory);
                        this.engine.setTheory(this.theory);
		} catch (InvalidTheoryException e) {
			e.printStackTrace();
		}
	}

	public void appendTheory(Theory append_theory)
	{
		try {
			this.theory = this.engine.getTheory();
                        this.theory.append(append_theory);
                        this.engine.setTheory(this.theory);
		} 
		catch (InvalidTheoryException e) {
			e.printStackTrace();
		}
	}

	// set to null the theory of the engine
	public void clearTheory() 
	{
		this.engine.clearTheory();
        this.theory = null;
	}

	// returns an arraylist with all the solutions related to the passed query
	public ArrayList<SolveInfo> solveQuery(String query) 
	{
		this.info=new ArrayList<SolveInfo>();
		try {
			SolveInfo solveInfo = this.engine.solve(query);
			while (solveInfo.isSuccess()) {
				this.info.add(solveInfo);
				if (engine.hasOpenAlternatives()) {
                    try {
                    	solveInfo = engine.solveNext();
                    } 
                    catch (NoMoreSolutionException e) {
                    }
                }
                else
                {
                    break;
                }
			}
		} 
		catch (MalformedGoalException e) {
		}
		return this.info;
	}

	// returns the solutions of the last query
	public ArrayList<SolveInfo> getQueryResult() 
	{
		return this.info;
	}

	// prints the results of the last query
	public void printResults() 
	{
		for(SolveInfo aux : this.info)
		{
			try {
				System.out.println("solution: " + aux.getSolution() +" - bindings: " + aux);
			} 
			catch (NoSolutionException e) {
				e.printStackTrace();
			}
		}
	}

    public void printTheory() {
    	String strTheory = this.theory.toString();
    	System.out.println(strTheory);
    }
}
