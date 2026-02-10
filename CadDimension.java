public class CadDimension extends ObjectCAD{
    Point midPoint;
    Point defPoint;
    Point defPoint1;
    Point defPoint2;
    int dimensionType, attachPoint;
    float value;

    public CadDimension(Point _midPoint, Point _defPoint, Point _defPoint1, Point _defPoint2, int _dimensionType, int _attachPoint, float _value){
        midPoint = _midPoint;
        defPoint = _defPoint;
        defPoint1 = _defPoint1;
        defPoint2 = _defPoint2;
        dimensionType = _dimensionType;
        attachPoint = _attachPoint;
        value = _value;
        points.add(midPoint);
        points.add(defPoint);
        points.add(defPoint1);
        points.add(defPoint2); 
    }

    public CadDimension clone(){
        return this;
    }

    public CadDimension getIsometricShape(){
        return this;
    }

    public String getType(){
        return "Dimension";
    }

    public String toString(){
        return "DIMENSION  value = " + value + " midPoint = " + midPoint.toString() + " defPoint = " + defPoint.toString() +" defPoint1 = " + defPoint1.toString() + " defPoint2 = " + defPoint2.toString();
    }
}
