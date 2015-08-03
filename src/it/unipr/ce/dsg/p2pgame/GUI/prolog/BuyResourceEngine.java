/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unipr.ce.dsg.p2pgame.GUI.prolog;

import it.unipr.ce.dsg.p2pgame.GUI.prolog.util.Resource;
import alice.tuprolog.SolveInfo;
import it.unipr.ce.dsg.p2pgame.platform.prolog.PrologEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jose
 */
public class BuyResourceEngine extends PrologEngine {

	public BuyResourceEngine() {
		super();
		/*
		try {
            this.setTheory(new FileInputStream("rules/buyResourceTheory.pl"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MovementEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
	}
	
	public BuyResourceEngine(String theory) {
		super(theory);
	}
	
    public void clearParameters()
    {
        this.clearTheory();
        
        this.setTheory("rules/buyResourceTheory.pl");
        

    }

    public void clearParameters(String theory)
    {
        this.clearTheory();
        
        this.setTheory(theory);
        

    }

    public void createBuyResourceTheory(ArrayList<Resource> resources, 
    		String req_resource, 
    		int current,
    		ArrayList<String> myresources, 
    		ArrayList<Integer> myQresources)
    {
        // create the list of facts that represnt the resources that can be purchased
        for (int i=0; i < resources.size(); i++)
        {
            Resource res = resources.get(i);
            // obtain parameters needed to construct the fact
            String name = res.getName();
            int cost = res.getCost();
            ArrayList<String> req = res.getRequirements();
            ArrayList<Integer> qreq = res.getQRequirements();

            // create rules in the form (resource_name,cost,[req1,re2,..,reqn],[q1,q2,...,qn])
            String strTheory = "";
            strTheory += "resource("+name+","+cost+",";

            String strAux = "[";
            for (int j=0; j<req.size(); j++)
            {
            	strAux += req.get(j);
                if (j < (req.size()-1))
                	strAux += ",";

            }
            strAux += "]";
            strTheory += strAux +",";

            strAux = "[";
            for (int j=0; j < qreq.size(); j++)
            {
            	strAux += qreq.get(j).intValue();
                 if (j < (qreq.size()-1))
                	 strAux += ",";

            }
            strAux += "]";
            strTheory += strAux + ").";

            this.appendTheory(strTheory);

        }

        // create the fact that contains the name of the requested resource
        this.appendTheory("resource_req("+req_resource+").");
        this.appendTheory("current_money("+current+").");
        
        String strAux = "[";
        // obtain the list of own resources
        for (int i=0;i<myresources.size();i++)
        {
        	strAux += myresources.get(i);
            if (i < (myresources.size()-1))
            	strAux += ",";
        }
        strAux += "]";
        
        this.appendTheory("current_resources(" + strAux + ").");
        strAux = "[";
        // obtain the list of quantities of owned resources
        for (int i=0; i < myQresources.size(); i++)
        {
        	strAux += myQresources.get(i).intValue();
           if (i < (myQresources.size()-1))
        	   strAux += ",";
        }
        strAux += "]";

        this.appendTheory("current_resourcesQ(" + strAux + ").");

    }
    
    
    public boolean buyResource()
    {
        String query="buy_resource.";
        ArrayList<SolveInfo> info=this.solveQuery(query);
        if(info.isEmpty())
        {
            return false;
        }
        return true;
    }


    public static void main(String []arg)
    {
        BuyResourceEngine engine = new BuyResourceEngine("rules/buyResourceTheory.pl");
        
      //information about the resources and the requirements for buy
        ArrayList<Resource> res = new ArrayList<Resource>();
        Resource r1 = new Resource();
        r1.setName("res1");
        r1.setCost(20);
        r1.addRequirement("res2");
        r1.addQRequirement(1);
        r1.addRequirement("res3");
        r1.addQRequirement(1);

        res.add(r1);
       //resource(res1,20,[res2,res3],[1,1]).
        
        Resource r2 = new Resource();
        r2.setName("res2");
        r2.setCost(20);
        r2.addRequirement("res4");
        r2.addQRequirement(2);

        res.add(r2);
        //resource(res2,20,[res4],[2]).

        Resource r3 = new Resource();
        r3.setName("res3");
        r3.setCost(20);
               
        res.add(r3);
        //resource(res3,20,[],[]).

        Resource r4 = new Resource();
        r4.setName("res4");
        r4.setCost(20);

        res.add(r4);
       //resource(res4,20,[],[]).

        //list of my current resources and its quantities
        ArrayList<String> myres = new ArrayList<String> ();
        ArrayList<Integer> myqres = new ArrayList<Integer>();

        myres.add("res2");
        myqres.add(3);

        myres.add("res3");
        myqres.add(4);

        myres.add("res4");
        myqres.add(3);

        myres.add("res5");
        myqres.add(2);
        
        //myres([res2,res3,res4,res5]).
        //myqres([3,4,3,2]).

        String reqResource="res1"; // the requested resource
        
        

        int current = 200; // my current money

        //create the facts with the previous information
        engine.createBuyResourceTheory(res, reqResource, current, myres, myqres);

        if (engine.buyResource()) // test, i can buy the resource??
        {
            System.out.println("buy resource");
        }
        else
        {
            System.out.println("no");
        }

        engine.printTheory();
    }
}
