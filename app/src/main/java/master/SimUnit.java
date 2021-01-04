package master;


import javafx.geometry.Point2D;

import java.util.ArrayList;

abstract class SimUnitWrapper{
    protected final double g = 9.83;
   // protected final double g = 4;
    protected final int MAX_NEIGH = 4;
    protected final double innerSpreadThreshold = 0.00001;
}

public class SimUnit extends SimUnitWrapper {

    private double posX;
    private double posY;

    private SimUnit upperNeigh;
    private SimUnit lowerNeigh;
    private SimUnit rightNeigh;
    private SimUnit leftNeigh;

    private double stiffness;       //ranging from 0 to 1
    private double elasticity;      //ranging from 0 to 1

    private double velX;// in m/s   ;if positive - to right     ;if negative - to left
    private double velY;// in m/s   ;if positive - down         ;if negative - up
    private double m;   // in kg    ; mass of a node

    private double innerForceX;
    private double innerForceY;

    private double gravity;
    private boolean isCollisionHandled;

    public SimUnit(int posX, int posY){
        m = 0.001; //kg
        velY=0.0; //m/s
        velX=0.0; //ms

        upperNeigh = null;
        lowerNeigh = null;
        leftNeigh = null;
        rightNeigh = null;

        gravity = m*g;
        innerForceX = 0.0; //N
        innerForceY = gravity;  //N

        //i would advise not to have this 1 or 0
        stiffness = 0.999;
        elasticity = 0.1;

        this.posX = posX;
        this.posY = posY;

        isCollisionHandled = false;
    }

    public void calculateNewUnitPosition(double delta){

        if(getLeftNeigh()!=null)
            if(posX == getLeftNeigh().getPosX() && posY == getLeftNeigh().getPosY())
                return;
        if(getRightNeigh() != null)
            if(posX == getRightNeigh().getPosX() && posY == getRightNeigh().getPosY())
                return;
        if(getUpperNeigh() != null)
            if(posX == getUpperNeigh().getPosX() && posY == getUpperNeigh().getPosY())
                return;
        if(getLowerNeigh() != null)
            if(posX == getLowerNeigh().getPosX() && posY == getLowerNeigh().getPosY())
                return;

            posX += velX*delta;
            posY += velY*delta;
    }

    public int getPosX() { return (int)posX; }
    public int getPosY() {
        return (int)posY;
    }

    public void calculateNewUnitVelocity(double delta){

        double aX = innerForceX/m;
        double aY = innerForceY/m;

        velX += aX*delta;
        velY += aY*delta;

    }


    public void calculateNewUnitInnerForce(double delta){

        if(getLeftNeigh() != null)
            innerForceX += (velX - getLeftNeigh().getVelX())/delta*m/1000;
        if(getRightNeigh() != null)
            innerForceX += (velX - getRightNeigh().getVelX())/delta*m/1000;
        if(getUpperNeigh() != null)
            innerForceX += (velX - getUpperNeigh().getVelX())/delta*m/1000;
        if(getLowerNeigh() != null)
            innerForceX += (velX - getLowerNeigh().getVelX())/delta*m/1000;

        if(getLeftNeigh() != null)
            innerForceY += (velY - getLeftNeigh().getVelY())/delta*m/1000;
        if(getRightNeigh() != null)
            innerForceY += (velY - getRightNeigh().getVelY())/delta*m/1000;
        if(getUpperNeigh() != null)
            innerForceY += (velY - getUpperNeigh().getVelY())/delta*m/1000;
        if(getLowerNeigh() != null)
            innerForceY += (velY - getLowerNeigh().getVelY())/delta*m/1000;

        innerForceX *= elasticity;
        innerForceY = (innerForceY-gravity)*elasticity + gravity;
    }


    public void immovableObjectCollisionHandler(double axisX, double axisY){
        //the axis show the direction of the reacting force,
        //since the object is immovable only the direction matters


        if(Math.abs(axisX) < innerSpreadThreshold && Math.abs(axisY) < innerSpreadThreshold)
            return;

        //refa astea doua de mai jos
        if(innerForceX*axisX < 0) innerForceX *= axisX;
        innerForceY = axisY * gravity;

        velX -= velX*Math.abs(axisX);
        velY -= velY*Math.abs(axisY);

        if(isCollisionHandled)
            return;
        else
            isCollisionHandled = true;

        axisX *= stiffness;
        axisY *= stiffness;

        if(getRightNeigh() != null) getRightNeigh().immovableObjectCollisionHandler(axisX,axisY);
        if(getLeftNeigh() != null) getLeftNeigh().immovableObjectCollisionHandler(axisX,axisY);
        if(getUpperNeigh() != null) getUpperNeigh().immovableObjectCollisionHandler(axisX,axisY);
        if(getLowerNeigh() != null) getLowerNeigh().immovableObjectCollisionHandler(axisX, axisY);

    }

    public void movableObjectCollisionHandler(SimUnit hitUnit){
               immovableObjectCollisionHandler(0,-1);


    }

    public void setLeftNeigh(SimUnit leftNeigh) { this.leftNeigh = leftNeigh; }
    public void setLowerNeigh(SimUnit lowerNeigh) { this.lowerNeigh = lowerNeigh; }
    public void setRightNeigh(SimUnit rightNeigh) { this.rightNeigh = rightNeigh; }
    public void setUpperNeigh(SimUnit upperNeigh) { this.upperNeigh = upperNeigh; }

    public SimUnit getLeftNeigh() { return leftNeigh; }
    public SimUnit getLowerNeigh() { return lowerNeigh; }
    public SimUnit getUpperNeigh() { return upperNeigh; }
    public SimUnit getRightNeigh() { return rightNeigh; }

    public boolean isEdge(){
        if(lowerNeigh == null)
            return true;
        if(upperNeigh == null)
            return true;
        if(leftNeigh == null)
            return true;
        if(rightNeigh == null)
            return true;

        return false;
    }
    public void resetCollisionHandling(){isCollisionHandled = false;}

    public double getVelX() { return velX; }
    public double getVelY() { return velY; }

}
