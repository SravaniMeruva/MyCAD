public class Arc extends ObjectCAD {
    Point center;
    float radius;
    double startAngle;
    double endAngle;
    float x1 = 0, y1 = 0;

    public Arc(Point _center, float _radius, double _startAngle, double _endAngle){
        
        center = _center;
        radius = _radius;
        startAngle = _startAngle;
        endAngle = _endAngle;

        calculateExtremePoints();
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

    public Arc getIsometricShape(){
        x1 = center.x + (float) (MyCAD.COS30 * center.z);
        y1 = center.y + (float) (MyCAD.SIN30 * center.z);
        return (new Arc(new Point(x1, y1), radius, startAngle, endAngle));
    }

    public ObjectCAD transformShapes(Point insertPoint, Point basePoint, Point scale, float rotAngle){
        
        if(scale.x == scale.y){
            center.scale(scale);
            center.rotate(basePoint, rotAngle);
            center = center.add(insertPoint).substract(basePoint);
            radius = radius * scale.x;
            startAngle = startAngle + rotAngle;
            endAngle = endAngle + rotAngle;

            calculateExtremePoints();
        }
        return this;
    }
    
    public Arc clone(){
        return new Arc(center, radius, startAngle, endAngle);
    }

    public void calculateExtremePoints(){
        double smallAngle = 0.0, bigAngle = 0.0;

        if(endAngle < startAngle){
            smallAngle = endAngle;
            bigAngle = startAngle;            
        } else {
            smallAngle = startAngle;
            bigAngle = endAngle;
        }

        float yMin = 0, yMax = 0, xMax = 0, xMin = 0;
        double sinStart = Math.sin(Math.toRadians((double) (smallAngle)));
        double cosStart = Math.cos(Math.toRadians((double) (smallAngle)));
        double cosEnd = Math.cos(Math.toRadians((double) (bigAngle)));
        double sinEnd = Math.sin(Math.toRadians((double) (bigAngle)));

        if(smallAngle <= 180.0 && bigAngle >= 180.0){
            xMin = center.x - radius;
        } else {
            xMin = ((radius * (float) (cosStart)) < (radius * (float) (cosEnd))) ? center.x + (radius * (float) (cosStart)) : center.x + (radius * (float) (cosEnd));
        }
        if(smallAngle <= 0.0 && bigAngle >= 0.0){
            xMax = center.x + radius;
        } else {
            xMax = ((radius * (float) (cosStart)) > (radius * (float) (cosEnd))) ? center.x + (radius * (float) (cosStart)) : center.x + (radius * (float) (cosEnd));
        }

        if(smallAngle <= 270.0 && bigAngle >= 270.0){
            yMin = center.y - radius;
        } else {
            yMin = ((radius * (float) (sinStart)) < (radius * (float) (sinEnd))) ? center.y + (radius * (float) (sinStart)) : center.y + (radius * (float) (sinEnd));
        }
        if(smallAngle <= 90.0 && bigAngle >= 90.0){
            yMax = center.y + radius;
        } else {
            yMax = ((radius * (float) (sinStart)) > (radius * (float) (sinEnd))) ? center.y + (radius * (float) (sinStart)) : center.y + (radius * (float) (sinEnd));
        }

        points.add(new Point(xMin, yMin, center.z));
        points.add(new Point(xMax, yMax, center.z));
    }

    public String toString(){
        return "ARC" + " center = " + center.toString() + " Radius = " + radius + " StartAngle = " + startAngle + " EndAngle = " + endAngle;
    }

}
