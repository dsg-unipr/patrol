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
public class GamePeerInfo implements Serializable{

        private String name = null;
	private String id = null;
	private double posX = 0;
	private double posY = 0;
	private double posZ = 0;
	private double velocity = 0; // dalla velocita' dipende l'aggiornamento della posizione
	private double visibility;

        public GamePeerInfo(String id,String name,double x,double y,double z,double vel,double vis)
        {
               this.name=name;
               this.id=id;
               this.posX=x;
               this.posY=y;
               this.posZ=z;
               this.velocity=vel;
               this.visibility=vis;
        }

        public GamePeerInfo(GamePeerInfo info)
        {
            this.name=info.name;
            this.id=info.id;
            this.posX=info.posX;
            this.posY=info.posY;
            this.posZ=info.posZ;
            this.velocity=info.velocity;
            this.visibility=info.visibility;

        }

        public String getName()
        {
            return this.name;
        }

        public String getID()
        {
            return this.id;

        }

        public double getPosX()
        {
            return this.posX;
        }

        public double getPosY()
        {
            return this.posY;
        }

        public double getPosZ()
        {
            return this.posZ;
        }

        public double getVelocity()
        {
            return this.velocity;
        }

        public double getVisibility()
        {
            return this.visibility;
        }

        public void setName(String name)
        {
            this.name=name;
        }

        public void setID(String id)
        {
            this.id=id;
        }

        public void setPosX(double x)
        {
            this.posX=x;
        }

        public void setPosY(double y)
        {
            this.posY=y;
        }

        public void setPosZ(double z)
        {
            this.posZ=z;
        }

        public void setVelocity(double vel)
        {
            this.velocity=vel;
        }

        public void setVisibility(double vis)
        {
            this.visibility=vis;
        }



}
