public class Mtext extends ObjectCAD{
    Point insertPoint;
    Point direction;
    String text;
    String style; 
    int spacing, align, attachPoint;
    float height, width, rotAngle;   

    public Mtext(Point _insertPoint, Point _direction, String _text, String _style, int _spacing, int _align, int _attachPoint, float _height, float _width, float _rotAngle){
        insertPoint = _insertPoint;
        direction = _direction;
        text = _text;
        style = _style;
        spacing = _spacing;
        align = _align;
        attachPoint = _attachPoint;
        height = _height;
        width = _width;
        rotAngle = _rotAngle;           //RADIANS

        points.add(insertPoint);
        points.add(new Point(insertPoint.x + width, insertPoint.y + height));
    }

    public Mtext getIsometricShape(){
        return this;
    }

    public Mtext clone(){
        return new Mtext(new Point(insertPoint), new Point(direction), text, style, spacing, align, attachPoint, height, width, rotAngle);
    }

    public String toString(){
        return "MTEXT: Insert Point = " + insertPoint + " Text " + text ; 
    }
}