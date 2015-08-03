/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unipr.ce.dsg.p2pgame.GUI.prolog;

import alice.tuprolog.NoSolutionException;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Var;
import it.unipr.ce.dsg.p2pgame.platform.prolog.PrologEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jose
 */
public class ExtractionEngine extends PrologEngine{
    
    
    private int period;
    private int value;
    private boolean infinite;
    private int current_res;
    
    
    public ExtractionEngine()
    {
    	super();
        /*try {
            this.setTheory(new FileInputStream("rules/extractionTheory.pl"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExtractionEngine.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        
    }

     public ExtractionEngine(String theory)
    {
        super(theory);
        /*try {
            this.setTheory(new FileInputStream(theory));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExtractionEngine.class.getName()).log(Level.SEVERE, null, ex);
        }*/


    }


    public void clearParameters()
    {
        this.clearTheory();
        
        this.setTheory("rules/extractionTheory.pl");
        

    }

     public void clearParameters(String theory)
    {
        this.clearTheory();
        
            this.setTheory(theory);
        

    }

    public void createExtractTheory(int period,int value,boolean infinite,int current)
    {


        
        String str="no";
        if(infinite) str="yes";

        this.infinite=infinite;
        this.period=period;
        this.value=value;
        this.current_res=current;
        

        
        this.appendTheory("infinite_resources("+str+")."); // yes if i am an infinite resoruce's deposite
        this.appendTheory("accumulation_period("+period+")."); //sleep time for acc
        this.appendTheory("accumulation_value("+value+")."); //value of resource extracted every period
        this.appendTheory("current_resources("+current+")."); // current resources in the deposite
        
        
        


    }

    public int getPeriod() // returns ths acc period
    {

        String query="accumulation_period(P).";
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

    public int getAccValue() //returns the acc value
    {
        String query="accumulation_value(V).";
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
    
    public int getCurrentResource()
    {
    	String query="current_resources(C).";
        ArrayList<SolveInfo > arrayinfo=this.solveQuery(query);
        if(!arrayinfo.isEmpty())
        {
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
        	
        }
        
    	
    	return 0;
    }


    public int getRemainingResources() //  returns the remaining resources
    {
        String query="remaining_resources(N).";
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


 

    public boolean stopExtraction(int current) // returns true if the resources are depleted and i have to stop the extraction, othewise returns false
    {
        this.clearParameters();
        createExtractTheory(period,value,infinite,current);

         String query="stop_extraction.";

         ArrayList<SolveInfo> info=this.solveQuery(query);

         if(!info.isEmpty())
             return true;


        return false;
    }

    public boolean isInfinite() // returns true if i have an infinite resource's deposite
    {

        String query="is_infinite.";
        ArrayList<SolveInfo > arrayinfo=this.solveQuery(query);
        if(!arrayinfo.isEmpty())
        {
        	return true;        	
        }
        return false;
    }



    public static void main(String [] arg)
    {
        ExtractionEngine engine=new ExtractionEngine("rules/extractionTheory.pl");
        //setting the initial parameters (period 10, value 100, is not infinite, the current resources are 200)
        engine.createExtractTheory(10, 100, false, 200);
        engine.printTheory();
        
        if(engine.stopExtraction(200)) // stop the extraction ??
        {
            System.out.println("stop extraction: yes");
        }
        else
        {
            System.out.println("stop extraction: no");
        }
        
        
        if(engine.isInfinite()) // verify if is infinite
        {
        	System.out.println("is infinite");
        	
        }
        else
        {
        	System.out.println("is not infinite");
        	
        }


    }
    
    



}
