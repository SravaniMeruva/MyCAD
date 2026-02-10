public class PointCAD extends ObjectCAD{
    float x, y, z;
    Point point;

    public PointCAD(float _x, float _y, float _z){
        x = _x;
        y = _y;
        z = _z;

        points.add(new Point(x, y, z));
    }

    public ObjectCAD getIsometricShape(){
        return this;
    }

    public String getType(){
        return "Point";
    }

    public ObjectCAD transformShape(Point insertPoint, Point basePoint, Point scale, float rotAngle){
        point = new Point(x, y, z);
        point.scale(scale);
        point.rotate(basePoint, rotAngle);
        point = point.add(insertPoint).substract(basePoint);

        points.clear();
        points.add(point);

        return this;
    }

    public String toString(){
        return "POINT: " + points.get(0).toString();
    }

}