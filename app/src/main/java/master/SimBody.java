package master;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class SimBody {

    private double totalMass;
    private Paint fill;
    private ArrayList<SimUnit> node;
    private ArrayList<SimUnit> edge;
    private CollisionBox collisionBox;

    public SimBody(Rectangle shape) {
        int xPoint = (int)shape.getX();
        int yPoint = (int)shape.getY();
        int width = (int)Math.abs(shape.getWidth());
        int height = (int)Math.abs(shape.getHeight());

        edge = new ArrayList<>();
        //System.err.println("local stored -> xPoint: " + xPoint + " yPoint: " + yPoint + " width: " + width +" height: " + height + "---------");
        fill = shape.getFill();

        node = new ArrayList<>();

        if(width <= 1 || height <=1) {
            //uneori width-ul si height-ul sunt 1.0 sau valori negative, ceea ce face partea asta a programului
            //sa se comporte imprevizibil si pana azi nu stiu de ce
            width = (int)shape.getWidth();
            height = (int)shape.getHeight();
        }

        //filling the bodies with simUnit
        for(int j= yPoint; j< (yPoint + height); j++)
            for(int i = xPoint; i < (xPoint + width); i++)
            {
                SimUnit temp = new SimUnit(i, j);
                node.add(temp);
            }


        //adding the neighbours of every SimUnit node
        for(int i=0; i<node.size(); i++)
        {
            if((i + width) < node.size()) {
                node.get(i).setLowerNeigh(node.get(i + width));

            }
            if(i % width != 0) {
                node.get(i).setLeftNeigh(node.get(i - 1));
            }
            if(i % width != width-1) {
                node.get(i).setRightNeigh(node.get(i + 1));
            }
            if((i - width) > 0) {
                node.get(i).setUpperNeigh(node.get(i - width));
            }
        }

        collisionBox = new CollisionBox();
        updateCollisionBox();
    }

    public void nextStep(double delta){

        for (SimUnit simUnit : node) simUnit.calculateNewUnitPosition(delta);
        for (SimUnit unit : node) unit.calculateNewUnitInnerForce(delta);
        for (SimUnit value : node) value.calculateNewUnitVelocity(delta);

    }

    public void resetCollisionState(){
        for(SimUnit simUnit: node) simUnit.resetCollisionHandling();
    }

    public void drawBody(PixelWriter pw){
        //System.out.println("Node: " + node.size());
        for (SimUnit unit : node) {
            //System.out.println(" Drawn x :" + unit.getPosX() +  "| y :" +  unit.getPoxY());
            pw.setArgb(unit.getPosX(), unit.getPosY(), -16777216);
        }

        // in order to show bodies' outlines, uncomment
        // for(int i=0 ;i < edge.size(); i++)
        //    pw.setArgb(edge.get(i).getPosX(), edge.get(i).getPosY(), -16777216 );
    }

    public ArrayList<SimUnit> getEdgePoints(){
       return edge;
   }

   public CollisionBox getCollisionBox(){
        updateCollisionBox();
        return collisionBox;
   }

    private void updateCollisionBox(){

        int left = 9999;
        int right = -1;
        int up = 9999;
        int down = -1;

        calculateEdges();
        for(SimUnit unit: edge)
        {
            int X = unit.getPosX();
            int Y = unit.getPosY();
            if(X < left)
                left = X;
            if(X > right)
                right = X;
            if(Y < up)
                up  = Y;
            if(Y > down)
                down = Y;
        }

        collisionBox.updateEdges(left, up, right, down);
    }

    private void calculateEdges(){
        edge.clear();

        for(SimUnit u: node)
            if(u.isEdge())
            {
                edge.add(u);
            }
    }
}


