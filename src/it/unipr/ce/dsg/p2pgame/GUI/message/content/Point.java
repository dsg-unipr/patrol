/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unipr.ce.dsg.p2pgame.GUI.message.content;

import java.io.Serializable;

/**
 *
 * @author pelito
 */
public class Point implements Serializable{


    private double x;
    private double y;

    public Point(double x,double y)
    {
        this.x=x;
        this.y=y;
    }

    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public void setX(double x)
    {
        this.x=x;
    }

    public void setY(double y)
    {
        this.y=y;
    }




}
