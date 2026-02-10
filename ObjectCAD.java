import java.util.*;


public abstract class ObjectCAD{
    List<Point> points = new ArrayList<Point>();

    public ObjectCAD getViewShape(int viewType){
        return this;
    }

    public String getType(){
        return this.getClass().getName();
    }

    public List<Point> getExtremePoints(){
        return  points;
    }

    public ObjectCAD transformShape(Point inserPoint, Point basePoint, Point scale, float rotAngle){
        return this;
    }

    public ObjectCAD clone(){
        return this;
    }

    abstract ObjectCAD getIsometricShape();
}

