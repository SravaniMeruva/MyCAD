
public class Line extends ObjectCAD{
    Point start;
    Point end;
    float x1 = 0, y1 = 0, x2 = 0, y2 = 0;

    public Line(Point _start, Point _end){
        start = _start;
        end = _end;
        points.add(start);
        points.add(end);
    }

    public Line clone(){
        return new Line(new Point(start), new Point(end));
    }

    public Line getIsometricShape(){

        x1 = start.x + (float) (MyCAD.COS30 * start.z);
        y1 = start.y + (float) (MyCAD.SIN30 * start.z);
        x2 = end.x + (float) (MyCAD.COS30 * end.z);
        y2 = end.y + (float) (MyCAD.SIN30 * end.z);

        return (new Line(new Point(x1, y1), new Point(x2, y2)));
    }

    public ObjectCAD transformShape(Point inserPoint, Point basePoint, Point scale, float rotAngle){      
        start.scale(scale);
        end.scale(scale);    

        start.rotate(basePoint, rotAngle);
        end.rotate(basePoint, rotAngle);

        start = start.add(inserPoint).substract(basePoint);
        end = end.add(inserPoint).substract(basePoint);

        points.clear();
        points.add(start);
        points.add(end);

        return this;
    }

    public String toString(){
        return "LINE " + "Start = " + start.toString() + " End = " + end.toString();
    }

}