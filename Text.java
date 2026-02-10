public class Text extends ObjectCAD{
    Point firstAlign;
    Point secAlign;
    String text;
    String style;
    float height, rotAngle;
    int textFlag, justifyHoriz, justifyVert;

    public Text(Point _firstAlign, Point _secAlign, String _text, String _style, float _height, float _rotAngle, int _textFlag, int _justifyHoriz, int _justifyVert){
        firstAlign = _firstAlign;
        secAlign = _secAlign;
        text = _text;
        textFlag = _textFlag;
        style = _style;
        height = _height;
        rotAngle = _rotAngle;
        justifyHoriz = _justifyHoriz;
        justifyVert = _justifyVert;

        points.add(firstAlign);
        points.add(secAlign);

    }
    
    public Text getIsometricShape(){
        return this;
    }

public Text clone(){
    return new Text(new Point(firstAlign), new Point(secAlign), text, style, height,  rotAngle,(int) textFlag, (int) justifyHoriz, (int) justifyVert);
}

    public String toString(){
        return "TEXT: first align= " + firstAlign.toString() + " second= " + secAlign.toString() + "rotation angle= " + rotAngle;
    }    
}
