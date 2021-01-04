package master;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class ToolBehaviour {

    public static void rectangleToolDrag(Point2D initialPoint, MouseEvent currentPoint, Pane simulationScene, boolean bypass){

        if(bypass)
            return;


        //limit to scene bounds
        double initX = initialPoint.getX();
        double initY = initialPoint.getY();
        double currX = Math.max(currentPoint.getX(),0);
        double currY = Math.max(currentPoint.getY(),0);
        currX = Math.min(currX, simulationScene.getWidth());
        currY = Math.min(currY, simulationScene.getHeight());


        //verify all edges aren't in contact
/*
@TODO: Repair the shitte /

        Point2D point;

        if(initX < currX & & initY < currY)
        {
            for( point = new Point2D(initX, currY+1); point.getX() <= currX; point.add(1,0) )
                for(int i=0;i<simulationScene.getChildren().size(); i++){
                    if(simulationScene.getChildren().get(i).contains(point))
                    {
                        System.out.println(point + " |  i = " + i);
                    }
                    else
                        System.out.println(point + " |  i = " + i);
                }
        }

*/

            //delete the previous rectangle

        for(int i=0;i<simulationScene.getChildren().size(); i++)
            if(simulationScene.getChildren().get(i).contains(initialPoint))
            {
                simulationScene.getChildren().remove(i);
                break;
            }


        //draw the new rectangle, taking into account the mouse movement
        if(initX < currX && initY < currY) {
            simulationScene.getChildren().add(new Rectangle(initX, initY, currX - initX, currY - initY));
            //System.out.println("Created Rectangle: width: " + (currX - initX) + " height: "+ (currY-initY));
        }
        else if (initX > currX && initY < currY) {
            simulationScene.getChildren().add(new Rectangle(currX, initY, initX - currX + 1, currY - initY + 1));
            //System.out.println("Created Rectangle: width: " + (initX - currX + 1) + " height: "+ (currY - initY + 1));
        }
        else if (initX < currX && initY > currY) {
            simulationScene.getChildren().add(new Rectangle(initX, currY, currX - initX + 1, initY - currY + 1));
            //System.out.println("Created Rectangle: width: " + (currX - initX + 1) + " height: "+ (initY - currY + 1));
        }
        else {
            simulationScene.getChildren().add(new Rectangle(currX, currY, initX - currX + 1, initY - currY + 1));
            //System.out.println("Created Rectangle: width: " + (initX - currX + 1) + " height: "+ (initY - currY + 1));
        }



    }

}
