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
public class MovementEngine extends PrologEngine{

    
    public MovementEngine()
    {
    	super();
    	/*
        try {
            this.setTheory(new FileInputStream("rules/movementTheory.pl"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MovementEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }

    public MovementEngine(String theory)
    {
        super(theory);
        /*
        try {
            this.setTheory(new FileInputStream(theory));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MovementEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }

    public void clearParameters()
    {
        this.clearTheory();
        
        this.setTheory("rules/movementTheory.pl");
        
    }

    public void clearParameters(String theory)
    {
        this.clearTheory();
        this.setTheory(theory);
        
    }

    public void createMovementTheory(int cx,int cy,int tx,int ty,int px,int py, ArrayList<Integer> visibility)
    {
        this.appendTheory("current_positionX("+cx+").");
        this.appendTheory("current_positionY("+cy+").");
        this.appendTheory("target_positionX("+tx+").");
        this.appendTheory("target_positionY("+ty+").");
        this.appendTheory("previous_movementX("+px+").");
        this.appendTheory("previous_movementY("+py+").");

        String str_aux="[";
        for(int i=0;i<visibility.size();i++)
        {
            str_aux+=visibility.get(i).intValue();
            if(i<(visibility.size()-1))
                str_aux+=",";
        }

        str_aux+="]";
        this.appendTheory("visibility("+str_aux+").");    
    }

    public int nextMovement()
    {
        String query="movement(X).";
        int mov=0;
        ArrayList<SolveInfo> array_info=this.solveQuery(query);
        if(array_info.isEmpty())
        {
          return 0;
        }
        SolveInfo info=array_info.get(0) ;
        try {
            List binds = info.getBindingVars();
            Var var=(Var)binds.get(0);
            String val=var.toStringFlattened();
            mov=Integer.parseInt(val);

        } catch (NoSolutionException ex) {
            Logger.getLogger(MovementEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mov;
    }
    
    public int latitudeMovement() //1 oppure due
    {
    	int mov=0;
    	String query="latitude_movement(X).";
    	ArrayList<SolveInfo> array_info=this.solveQuery(query);
    	if(!array_info.isEmpty())
    	{
    		SolveInfo info=array_info.get(0) ;
            try {
                List binds = info.getBindingVars();
                Var var=(Var)binds.get(0);
                String val=var.toStringFlattened();
                mov=Integer.parseInt(val);

            } catch (NoSolutionException ex) {
                Logger.getLogger(MovementEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
            return mov;
    		
    	}
    	
    	return 0;
    }
    
    public int longitudeMovement()
    {
    	int mov=0;
    	String query="longitude_movement(X).";
    	ArrayList<SolveInfo> array_info=this.solveQuery(query);
    	if(!array_info.isEmpty())
    	{
    		SolveInfo info=array_info.get(0) ;
            try {
                List binds = info.getBindingVars();
                Var var=(Var)binds.get(0);
                String val=var.toStringFlattened();
                mov=Integer.parseInt(val);

            } catch (NoSolutionException ex) {
                Logger.getLogger(MovementEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
            return mov;
    		
    	}
    	
    	return 0;
    	
    }
    

    public static void main(String [] arg)
    {
        MovementEngine engine = new MovementEngine();
        engine.setTheory("rules/movementTheory.pl");
        
        ArrayList<Integer> vis = new ArrayList<Integer>();
        vis.add(0);
        vis.add(0);
        vis.add(0);
        vis.add(0);
        vis.add(0);
        vis.add(0);
        vis.add(0);
        vis.add(0);
        
        /*
         	visibility:
         	          0 0 0
         			  0 * 0
                      0 0 0
          
         */
        
        
        
        // i want to move from (20,30) to (20,20)
        // my prevoius movement is (0,1) 
        /*
           movements
                  (-1,0)  
            (0,-1)  *   (0,1)
                  (1,0)         
         */
        
        //create the facts with the previous informations 
        engine.createMovementTheory(20,20, 30, 30, 0, 1, vis);
        engine.printTheory();

        //obtain the next movement
        int mov = engine.nextMovement();
        
        // north : 1 , south : 2 , east : 3 , west : 4
        

        System.out.println("next movement: "+ mov);
        
        mov=engine.latitudeMovement();
        
        System.out.println("latitude movement: "+ mov);
        
        mov=engine.longitudeMovement();
        System.out.println("longitude movement: "+ mov);
        
        
    }
}
