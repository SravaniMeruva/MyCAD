import java.util.*;

public class LwPolyLine  extends ObjectCAD{
    int polyLineType;
    int visible;
    
    public LwPolyLine(List<Point> _points, int _polyLineType, int _visible){
        points =  new ArrayList<Point>(_points);        
        polyLineType = _polyLineType;
        visible = _visible;
    }

    public LwPolyLine getIsometricShape(){
        float x1 = 0, y1 = 0;
        List<Point> newPoints = new ArrayList<Point>();

        for(Point point : points){
            x1 = (point.x + (float) (MyCAD.COS30 * point.z));
            y1 = (point.y + (float) (MyCAD.SIN30 * point.z));
            newPoints.add(new Point(x1, y1));
        }
        return (new LwPolyLine(newPoints, polyLineType, visible));
    } 
    
    public LwPolyLine clone(){
        return new LwPolyLine(points, polyLineType, visible);
    }

    public ObjectCAD transformShape(Point inserPoint, Point basePoint, Point scale, float rotAngle){
        for(Point point : points){
            point.scale(scale);
            point.rotate(basePoint, rotAngle);
            point = point.add(inserPoint).substract(basePoint);         
        }
        return this;
    }

    public String toString(){

        switch (polyLineType) {
            case 1:
            return "LWPOLYLINE-Closed, points = " + points.toString();
            case 2:
            return "LWPOLYLINE-Curve-fit Vertices, points = " + points.toString();
        }
        return "LWPOLYLINE-" + polyLineType + " points = " + points.toString();
    }
}
