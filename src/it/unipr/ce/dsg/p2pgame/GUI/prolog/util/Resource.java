/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unipr.ce.dsg.p2pgame.GUI.prolog.util;

import java.util.ArrayList;

/**
 *
 * @author jose
 */
public class Resource {

    private String name;
    private int cost;
    private ArrayList<String> req;
    private ArrayList<Integer> qreq;


    public Resource()
    {
        name="";
        cost=0;
        req=new ArrayList<String>();
        qreq=new ArrayList<Integer>();

    }

    public Resource(String n,int c, ArrayList<String> req, ArrayList<Integer> qreq)
    {
        this.name=n;
        this.cost=c;
        this.req=req;
        this.qreq=qreq;
    }

    public void setName(String n)
    {
        this.name=n;
    }

    public String getName()
    {
        return this.name;
    }

    public void setCost(int c)
    {
        this.cost=c;
    }

    public int getCost()
    {
        return this.cost;
    }

    public void setRequirements(ArrayList<String> req)
    {
        this.req=req;
    }

    public ArrayList<String> getRequirements()
    {
        return this.req;
    }

    public void setQRequirements(ArrayList<Integer> qreq)
    {
        this.qreq=qreq;
    }

    public ArrayList<Integer> getQRequirements()
    {
        return this.qreq;
    }

    public void addRequirement(String requirement)
    {
        this.req.add(requirement);

    }

    public void addQRequirement(int q)
    {
        this.qreq.add(new Integer(q));
        
    }




}
