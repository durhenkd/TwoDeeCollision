package master;

public class SceneSize {
    private int height;
    private int width;

    public SceneSize(int height, int width){
        this.height = height;
        this.width = width;
    }

    public SceneSize(){}

    public void setHeight(int height){this.height = height;}
    public void setWidth(int width){this.width = width;}

    public int getHeight(){return height;}
    public int getWidth(){return width;}
}
