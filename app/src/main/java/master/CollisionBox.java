package master;

public class CollisionBox {
    private int leftEdge;
    private int rightEdge;
    private int upperEdge;
    private int lowerEdge;

    public CollisionBox(int leftEdge, int upperEdge, int rightEdge, int lowerEdge){
        this.leftEdge = Math.max(0, leftEdge);
        this.rightEdge = Math.max(0, rightEdge);
        this.upperEdge = Math.max(0, upperEdge);
        this.lowerEdge = Math.max(0, lowerEdge);
    }
    public CollisionBox(){
        this.lowerEdge= -1;
        this.leftEdge= -1;
        this.upperEdge= -1;
        this.rightEdge= -1;
    }

    public int getLeftEdge() {
        return leftEdge;
    }
    public int getLowerEdge() {
        return lowerEdge;
    }
    public int getRightEdge() {
        return rightEdge;
    }
    public int getUpperEdge() {
        return upperEdge;
    }

    public void updateEdges(int leftEdge, int upperEdge, int rightEdge, int lowerEdge)
    {
        this.lowerEdge = lowerEdge;
        this.upperEdge = upperEdge;
        this.leftEdge = leftEdge;
        this.rightEdge = rightEdge;
    }

    public boolean isIntersecting(CollisionBox box){
        int nrOfIntersections = 0;

        if(rightEdge >= box.getLeftEdge() && rightEdge <= box.getRightEdge())
            nrOfIntersections++;
        if(leftEdge >= box.getLeftEdge() && leftEdge <= box.getRightEdge())
            nrOfIntersections++;
        if(upperEdge >= box.getUpperEdge() && upperEdge <= box.getLowerEdge())
            nrOfIntersections++;
        if(lowerEdge >= box.getUpperEdge() && lowerEdge <= box.getLowerEdge())
            nrOfIntersections++;

        if(box.getRightEdge() >= leftEdge && box.getRightEdge() <= rightEdge)
            nrOfIntersections++;
        if(box.getLeftEdge() >= leftEdge && box.getLeftEdge() <= rightEdge)
            nrOfIntersections++;
        if(box.getUpperEdge() >= upperEdge && box.getUpperEdge() <= lowerEdge)
            nrOfIntersections++;
        if(box.getLowerEdge() >= upperEdge && box.getLowerEdge() <= lowerEdge)
            nrOfIntersections++;

        return nrOfIntersections>=4;
    }

    public boolean isIntersecting(int leftEdge, int upperEdge, int rightEdge, int lowerEdge)
    {
        int nrOfIntersections = 0;

        if(this.rightEdge >= leftEdge && this.rightEdge <= rightEdge)
            nrOfIntersections++;
        if(this.leftEdge >= leftEdge && this.leftEdge <= rightEdge)
            nrOfIntersections++;
        if(this.upperEdge >= upperEdge && this.upperEdge <= lowerEdge)
            nrOfIntersections++;
        if(this.lowerEdge >= upperEdge && this.lowerEdge <= lowerEdge)
            nrOfIntersections++;

        if(rightEdge >= this.leftEdge && rightEdge <= this.rightEdge)
            nrOfIntersections++;
        if(leftEdge >= this.leftEdge && leftEdge <= this.rightEdge)
            nrOfIntersections++;
        if(upperEdge >= this.upperEdge && upperEdge <= this.lowerEdge)
            nrOfIntersections++;
        if(lowerEdge >= this.upperEdge && lowerEdge <= this.lowerEdge)
            nrOfIntersections++;

        return nrOfIntersections>=4;
    }

}
