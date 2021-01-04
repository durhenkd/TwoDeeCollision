package master;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;

public class SimulationClass {

    private Canvas canvas;
    private GraphicsContext gc;
    private Timeline timeline;

    private ObservableList<Node> childElements;
    private SimBody []bodies;
    private Pane simulationScene;

    private ArrayList<Point2D> immovableObjectsEdges;

    public SimulationClass(Pane simulationScene){
        this.simulationScene = simulationScene;
        double width = simulationScene.getWidth();
        double height = simulationScene.getHeight();

        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        immovableObjectsEdges = new ArrayList<>();

        //adding the edges of the canvas

        for(double i=0; i<width; i++)
        {
            immovableObjectsEdges.add(new Point2D(i, 0.0));
            immovableObjectsEdges.add(new Point2D(i, height));
        }
        for(double i = 0; i < height; i++)
        {
            immovableObjectsEdges.add(new Point2D(0.0, i));
            immovableObjectsEdges.add(new Point2D(width, i));
        }


        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        childElements = simulationScene.getChildren();
        bodies = new SimBody[childElements.size()];

        for(int i=0; i < childElements.size(); i++)
        {
            childElements.get(i).managedProperty().bind(childElements.get(i).visibleProperty());
            childElements.get(i).setVisible(false);
            if(childElements.get(i) instanceof Rectangle)
                bodies[i] = new SimBody((Rectangle)childElements.get(i));
        }

        simulationScene.getChildren().add(canvas);

    }

    public void simulate(){

        final long timeStart = System.currentTimeMillis();
        PixelWriter pw = gc.getPixelWriter();

        KeyFrame kf = new KeyFrame(Duration.seconds(0.01),
                  actionEvent -> {
            double t = (System.currentTimeMillis() - timeStart) / 1000.0;


            gc.clearRect(0,0, simulationScene.getWidth(), simulationScene.getHeight());

            //we check for collisions using collision boxes
            for (int i = 0; i < bodies.length; i++) {
                CollisionBox box1 = bodies[i].getCollisionBox();

                //next we check for collision with screen edges
                int width = (int)simulationScene.getWidth();
                int height = (int)simulationScene.getHeight();

                //we check for every screen edge separately

                if(box1.isIntersecting(0, height, width, height)){          //lower edge of screen
                    ArrayList<SimUnit> tempEdge = bodies[i].getEdgePoints();
                    for(SimUnit simUnit: tempEdge){
                        if(simUnit.getPosY() == height)
                        {
                            //System.out.println("COLLISION DETECTED");
                            simUnit.immovableObjectCollisionHandler(0, -1);
                        }
                    }
                    bodies[i].resetCollisionState();
                }
                if(box1.isIntersecting(0, 0, width, 0)){//upper edge of the screen
                    ArrayList<SimUnit> tempEdge = bodies[i].getEdgePoints();
                    for(SimUnit simUnit: tempEdge){
                        if(simUnit.getPosY() == 0)
                         {
                             //System.out.println("COLLISION DETECTED");
                             simUnit.immovableObjectCollisionHandler(0, 1);
                         }
                    }
                    bodies[i].resetCollisionState();
                }
                if(box1.isIntersecting(0, 0 ,0, height)){//left edge of the screen
                    ArrayList<SimUnit> tempEdge = bodies[i].getEdgePoints();
                    for(SimUnit simUnit: tempEdge){
                        if(simUnit.getPosX() == 0)
                        {
                            //System.out.println("COLLISION DETECTED");
                            simUnit.immovableObjectCollisionHandler(1, 0);

                        }
                    }
                    bodies[i].resetCollisionState();
                }
                if(box1.isIntersecting(width, 0, width, height)){           //right edge of the screen
                    ArrayList<SimUnit> tempEdge = bodies[i].getEdgePoints();
                    for(SimUnit simUnit: tempEdge){
                        if(simUnit.getPosX() == width)
                        {
                            //System.out.println("COLLISION DETECTED");
                            simUnit.immovableObjectCollisionHandler(-1, 0);

                        }
                    }
                    bodies[i].resetCollisionState();
                }

                //next for is for checking collision between objects
                for(int j = i+1; j<bodies.length; j++) {
                    CollisionBox box2 = bodies[j].getCollisionBox();

                    //checking for collisions between objects
                    if(box1.isIntersecting(box2)) {
                        //boxes intersect, time to check for every point
                        ArrayList<SimUnit> tempEdge1 = bodies[i].getEdgePoints();
                        ArrayList<SimUnit> tempEdge2 = bodies[j].getEdgePoints();

                        for (SimUnit simUnit : tempEdge1) {
                            int X = simUnit.getPosX();
                            int Y = simUnit.getPosY();

                            for (SimUnit check4collision : tempEdge2) {
                                if (X == check4collision.getPosX() && Y == check4collision.getPosY())
                                {
                                    simUnit.movableObjectCollisionHandler(check4collision);
                                    check4collision.movableObjectCollisionHandler(simUnit);

                                    simUnit.resetCollisionHandling();
                                    check4collision.resetCollisionHandling();
                                }
                            }
                        }
                    }
                }

                /*
                ArrayList<SimUnit> tempEdge = body.getEdgePoints();

                for (SimUnit simUnit : tempEdge) {
                    int X = simUnit.getPosX();
                    int Y = simUnit.getPosY();

                    //next is collision checking between the body and immovable objects
                    for(Point2D point: immovableObjectsEdges)
                        if(X == point.getX() && Y == point.getY());
                            //resolve collision
                    }
                }*/

                bodies[i].nextStep(0.017);
                bodies[i].drawBody(pw);
            }
        });

        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    public void stopSimulate(){
        timeline.stop();

        childElements.remove(canvas);
        for(int i=0; i < childElements.size(); i++) {
            childElements.get(i).setVisible(true);
        }
    }

}
