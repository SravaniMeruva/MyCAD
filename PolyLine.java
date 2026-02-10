import java.util.*;

public class PolyLine  extends ObjectCAD{
    int polyLineType;
    
    public PolyLine(List<Point> _points, int _polyLineType){
        points =  new ArrayList<Point>(_points);        
        polyLineType = _polyLineType;
    }

    public PolyLine getIsometricShape(){
        float x1 = 0, y1 = 0;
        List<Point> newPoints = new ArrayList<Point>();

        for(Point point : points){
            x1 = (point.x + (float) (MyCAD.COS30 * point.z));
            y1 = (point.y + (float) (MyCAD.SIN30 * point.z));
            newPoints.add(new Point(x1, y1));
        }
        return (new PolyLine(newPoints, polyLineType));
    } 
    
    public PolyLine clone(){
        return new PolyLine(points, polyLineType);
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
            return "POLYLINE-Closed, points = " + points.toString();
            case 2:
            return "POLYLINE-Curve-fit Vertices, points = " + points.toString();
        }
        return "POLYLINE-" + polyLineType + " points = " + points.toString();
    }
}
