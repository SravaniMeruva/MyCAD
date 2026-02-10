
public class Circle extends ObjectCAD{
    Point center;
    float radius;
    float x1 = 0, y1 = 0;

    public Circle(Point _center, float _radius){
        center = _center;
        radius = _radius;
        points.add(new Point(center.x-radius, center.y+radius, center.z));
        points.add(new Point(center.x+radius, center.y-radius, center.z));
    }

    public ObjectCAD getViewShape(int viewType){
        switch(viewType){
            case MyCAD.TOPVIEW:
            case MyCAD.LEFTVIEW:
            case MyCAD.RIGHTVIEW:
                return new Line(points.get(0), points.get(1));
        }
        return this;
    }

    public Circle getIsometricShape(){
        x1 = center.x + (float) (MyCAD.COS30 * center.z);
        y1 = center.y + (float) (MyCAD.SIN30 * center.z);
        
        return (new Circle(new Point(x1, y1), radius));
    }

    public ObjectCAD transformShape(Point inserPoint, Point basePoint, Point scale, float rotAngle){
    
        if(scale.x == scale.y){
            center.scale(scale);        // CIRCLE SHOULD BECOME ELLIPSE...................................................................................
            center.rotate(basePoint, rotAngle);
            center = center.add(inserPoint).substract(basePoint);
            radius = radius * scale.x;

            points.clear();
            points.add(new Point(center.x-radius, center.y+radius, center.z));
            points.add(new Point(center.x+radius, center.y-radius, center.z));

            return this;
        } else {
            Point point = new Point(center);
            point.scale(scale);
            point.rotate(basePoint, rotAngle);
            point = point.add(inserPoint).substract(basePoint);

            return (new Ellipse(point, radius * scale.x, radius * scale.y, (float) Math.toRadians(rotAngle), (float) 0.0, (float) 6.28));
        }
    }

    public Circle clone(){
        return new Circle(new Point(center), radius);
    }

    public String toString(){
        return "CIRCLE" + " Center = " + center.toString() + " Radius = " + radius;
    }
}