import java.lang.Math;

public class Ellipse extends ObjectCAD{
    Point center;
    Point end;
    float ratio, startAngle, endAngle;
    float x1 = 0, y1 = 0, x2 = 0, y2 = 0;
    float major = 0;
    float minor = 0;
    double majorAngle = 0;
    boolean fullEllipse;

    public Ellipse(Point _center, Point _end, float _ratio, float _startAngle, float _endAngle){
        center = _center;
        end = _end;
        ratio = _ratio;
        startAngle = _startAngle;
        endAngle = _endAngle;

        if(startAngle == 0 && endAngle == 0)    endAngle =(float) Math.PI * 2;
        if ((endAngle - startAngle) >= 6.28) fullEllipse = true;
        else fullEllipse = false;

        major = (float) Math.sqrt(Math.pow((double)(end.x - center.x), 2.0)+ Math.pow((double)(end.y - center.y), 2.0) + Math.pow((double)(end.z - center.z), 2.0));
        minor = (major*ratio);

        majorAngle = (Math.atan((double) ((center.y - end.y)/(center.x - end.x))));

        calculateEllipseParms();
    }

    public Ellipse(Point _center, float _major, float _minor, float _majorAngle, float _startAngle, float _endAngle){
        center = _center;
        major = _major;
        minor = _minor;
        majorAngle = _majorAngle;
        startAngle = _startAngle;
        endAngle = _endAngle;
        
        calculateEllipseParms();
    }

    public void calculateEllipseParms(){
        if(startAngle == 0 && endAngle == 0)    endAngle =(float) Math.PI * 2;
        if ((endAngle - startAngle) >= 6.28) fullEllipse = true;
        else fullEllipse = false;

        points.clear();
        if(majorAngle == 0.0){
            points.add(new Point(center.x - major, center.y + minor, center.z));
            points.add(new Point(center.x + major, center.y - minor, center.z));
        } else {
            x1 = (float) Math.sqrt(Math.pow(major*Math.cos(majorAngle), 2.0) + Math.pow(minor*Math.sin(majorAngle), 2.0));
            y1 = (float) Math.sqrt(Math.pow(major*Math.sin(majorAngle), 2.0) + Math.pow(minor*Math.cos(majorAngle), 2.0));
            points.add(new Point(center.x - x1, center.y + y1, center.z));
            points.add(new Point(center.x + x1, center.y - y1, center.z));
        }
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

    public Ellipse getIsometricShape(){
        x1 = center.x + (float) (MyCAD.COS30 * center.z);
        y1 = center.y + (float) (MyCAD.SIN30 * center.z);

        return (new Ellipse(new Point(x1, y1), end, ratio, startAngle, endAngle));
    }

    public Ellipse clone(){
        return new Ellipse(center, end, ratio, startAngle, endAngle);
    }

    public ObjectCAD transformShape(Point insertPoint, Point basePoint, Point scale, float rotAngle){
        center.scale(scale);
        center.rotate(basePoint, rotAngle);
        center = center.add(insertPoint).substract(basePoint);
        end.scale(scale);
        end.rotate(basePoint, rotAngle);
        end = end.add(insertPoint).substract(basePoint);
        major = major * scale.x;
        minor = minor * scale.y;
        ratio = scale.y / scale.x;
        majorAngle = majorAngle + rotAngle;

        calculateEllipseParms();

        return this;
    }

    public String toString(){
        return "ELLIPSE : Center=" + center + " major=" + major + "; minor=" + minor + " major angle=" + majorAngle +" start Angle= " + startAngle + " end Angle= " + endAngle + " fullEllispse= " + fullEllipse;
    }
}
