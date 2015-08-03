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
public class VisibilityEngine extends PrologEngine{

    
    public VisibilityEngine()
    {
        super();
        /*try {
            this.setTheory(new FileInputStream("rules/visibilityTheory.pl"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VisibilityEngine.class.getName()).log(Level.SEVERE, null, ex);
        }*/

    }

    public VisibilityEngine(String theory)
    {
        super(theory);
        /*try {
            this.setTheory(new FileInputStream(theory));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VisibilityEngine.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

 

    public void clearParameters()
    {
        this.clearTheory();
        
            this.setTheory("rules/visibilityTheory.pl");
        

    }

    public void clearParameters(String theory)
    {
        this.clearTheory();
        
            this.setTheory(theory);
        

    }


    public void createVisibilityTheory(ArrayList<Integer> posx,
    		ArrayList<Integer> posy,
    		ArrayList<String> owner,
    		ArrayList<String> type,
    		String myID,
    		ArrayList<Integer> probattack,
    		ArrayList<String> restypes)
    {




        // create the fact posx(X). X is a list of integer 
        String str_aux="[";
        for(int i=0;i<posx.size();i++)
        {
            str_aux+=posx.get(i).intValue();
            if(i<(posx.size()-1))
            {
                str_aux+=",";
            }
        }
        str_aux+="]";

        this.appendTheory("posx("+str_aux+").");

        // create the fact posy(Y). Y is a list of integer

        str_aux="[";

        for(int i=0;i<posy.size();i++)
        {
           str_aux+=posy.get(i).intValue();
            if(i<(posy.size()-1))
            {
                str_aux+=",";
            }

        }
        str_aux+="]";

        this.appendTheory("posy("+str_aux+").");

        // create the fact owner(O). O is a list of string

        str_aux="[";
        for(int i=0;i<owner.size();i++)
        {
            str_aux+=owner.get(i);
            if(i<(owner.size()-1))
            {
                str_aux+=",";
            }
        }

        str_aux+="]";

        this.appendTheory("owner("+str_aux+").");

        // create the fact type(T). T is a list of string
        str_aux="[";

        for(int i=0;i<type.size();i++)
        {
            str_aux+=type.get(i);
            if(i<(type.size()-1))
            {
                str_aux+=",";
            }
        }
        str_aux+="]";

        this.appendTheory("type("+str_aux+").");

        //myID(ID). contains the user's ID
        
        this.appendTheory("myID("+myID+").");

        //probabilities_attack(Pa). This fact contains a list of integers that rappresent tha probability of attack every type of resources

        str_aux="[";
        for(int i=0;i<probattack.size();i++)
        {
            str_aux+=probattack.get(i).intValue();
            if(i<(probattack.size()-1))
            {
                str_aux+=",";
            }
        }

        str_aux+="]";

        
        this.appendTheory("probabilities_attack("+str_aux+").");

        //probabilities_defense(Pd).  This fact contains a list of integers that rappresent that probability of defense to every type of resources
        str_aux="[";
        for(int i=0;i<probattack.size();i++)
        {
            str_aux+=(100 - probattack.get(i).intValue());
            if(i<(probattack.size()-1))
            {
                str_aux+=",";
            }
        }
        str_aux+="]";

        

        this.appendTheory("probabilities_defense("+str_aux+").");

        //restypes(T). T is a list of String that rappresent the possible types of resoruces

        str_aux="[";

        for(int i=0;i<restypes.size();i++)
        {
            str_aux+=restypes.get(i);
            if(i<(restypes.size()-1))
            {
                str_aux+=",";
            }
        }
        str_aux+="]";


        this.appendTheory("restypes("+str_aux+").");

        int proa=(int)(Math.random()*100) ; //numero random tra 0 e 100

        //random_prob(r). // attack probability

        this.appendTheory("random_prob("+proa+").");

        int prod=(int)(Math.random()*100)  ; //numero random tra 0 e 100

        //random_probD(r). //defense probability
        
        this.appendTheory("random_probD("+prod+").");

        

    }

    public int attack() // return the position to be attacked if doesn't returns 0
    {
        String query="attack(Pos).";
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

    public int posX() //obtain the coordinate x to be attacked, if doesn't returns MINVALUE
    {

        String query="posX(X).";
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


        return Integer.MIN_VALUE;

    }

    public int posY() //obtain the coordinate y to be attacked,if doesn't returns MINVALUE
    {
        String query="posY(Y).";
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


        return Integer.MIN_VALUE;
    }



    public int defense()// return the position to be defended if doesn't returns 0
    {
           String query="defense(Pos).";
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

    public int posDX()//obtain the coordinate x to be defended, if doesn't returns MINVALUE
    {

        String query="posDX(X).";
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


        return Integer.MIN_VALUE;

    }

    public int posDY()//obtain the coordinate y to be defended, if doesn't returns MINVALUE
    {
        String query="posDY(Y).";
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
    

    public static void main(String [] arg)
    {
        VisibilityEngine engine=new VisibilityEngine("rules/visibilityTheory.pl");
        //visibility information:
        ArrayList<Integer> posx=new ArrayList<Integer>();
        posx.add(new Integer(1));
        posx.add(new Integer(3));
        posx.add(new Integer(4));
        posx.add(new Integer(5));
        posx.add(new Integer(8));
        posx.add(new Integer(5));
        posx.add(new Integer(3));
        posx.add(new Integer(7));
        posx.add(new Integer(8));
        posx.add(new Integer(6));
       //posx([1,3,4,5,8,5,3,7,8,6]). X coordinate of only visible element 

        ArrayList<Integer> posy=new ArrayList<Integer>();
        posy.add(new Integer(1));
        posy.add(new Integer(4));
        posy.add(new Integer(5));
        posy.add(new Integer(6));
        posy.add(new Integer(3));
        posy.add(new Integer(6));
        posy.add(new Integer(3));
        posy.add(new Integer(7));
        posy.add(new Integer(8));
        posy.add(new Integer(6));
        //posy([1,4,5,6,3,6,3,7,8,6]). Y coordinate of each visible element 

        ArrayList<String> owner=new ArrayList<String>();

        owner.add("null");
        owner.add("null");
        owner.add("null");
        owner.add("null");
        owner.add("id3");
        owner.add("null");
        owner.add("null");
        owner.add("null");
        owner.add("null");
        owner.add("id1");
        
        //owner([...]). list of owners of each visible element, if the space(x,y) is empty, the owner is null  

        ArrayList<String> type=new ArrayList<String>();

        type.add("null");
        type.add("null");
        type.add("null");
        type.add("null");
        type.add("t1");
        type.add("null");
        type.add("null");
        type.add("null");
        type.add("null");
        type.add("t2");
        
        //type([...]) list of types of each element, if the space(X,Y) is empty, the type is null

        String myID="id1";

        ArrayList<String> restypes=new ArrayList<String>();

        restypes.add("t1");
        restypes.add("t2");
        restypes.add("t3");
        restypes.add("t4");
        // list of different types of resources (mobile ,ecc)
 
        ArrayList<Integer> probattack=new ArrayList<Integer>();

        probattack.add(new Integer(70));
        probattack.add(new Integer(60));
        probattack.add(new Integer(80));
        probattack.add(new Integer(40));
        // list of probabilities of attack related to each type of resources


       //create the facts with the previous informations 
       engine.createVisibilityTheory(posx, posy, owner, type, myID, probattack, restypes);

       int pos=engine.attack();
       //testing the visibility, verifing if i can attack and the X,Y coordinates
       if(pos!=0)
       {
          System.out.println("Pos "+pos);
    	  int x=engine.posX();
          int y=engine.posY();

          System.out.println("attack: ("+x+" , "+y+")");
       }
       else
       {  //otherwise, verifing if i have to defend against a possible attack and the X,Y coordinates
          pos=engine.defense();
          if(pos!=0)
          {
              int x=engine.posDX();
              int y=engine.posDY();
              System.out.println("defense: ("+x+" , "+y+")");
          }
          else
          {    //
               System.out.println("nothing");
          }


       }
       
       engine.printTheory();

    }
    
    
    

    

}
