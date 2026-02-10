public class Point{
    float x;
    float y;
    float z;

    public Point(Point _point){
        x = _point.x;
        y = _point.y;
        z = _point.z;
    }

    public Point(float _x, float _y, float _z){
        x = _x;
        y = _y;
        z = _z;
    }

    public Point(float _x, float _y){
        this(_x, _y, (float) 0);
    }

    public Point(float _x, double _y){
        this(_x, (float)_y, (float) 0);
    }

    public Point(double _x, float _y){
        this((float)_x, _y, (float) 0);
    }

    public Point(double _x, double _y){
        this((float)_x, (float)_y, (float) 0);
    }

    public Point substract(Point _point){
        return new Point(x - _point.x, y - _point.y, z - _point.z);
    }

    public Point add(Point _point){
        return new Point(x + _point.x, y + _point.y, z + _point.z);
    }

    public boolean equals(Point _point){
        return ((x == _point.x) && (y == _point.y) && (z == _point.z));
    }

    public void scale(Point _scale){
        x = x * _scale.x;
        y = y * _scale.y;
        z = z * _scale.z;
    }

    public void rotate(Point _rotAbout, float rotAngle){
        if (this.equals(_rotAbout)){
            return;
        }
        float cosVal = (float) Math.cos(Math.toRadians(rotAngle));
        float sinVal = (float) Math.sin(Math.toRadians(rotAngle));

        float x1 = ((x - _rotAbout.x) * cosVal) - ((y - _rotAbout.y) * sinVal) + _rotAbout.x;
        y = ((y - _rotAbout.y) * cosVal) + ((x - _rotAbout.x) * sinVal) + _rotAbout.y;
        x = x1;

        return;
    }

    public String toString(){
        return "(" + x + ", " + y + ", "+ z + ")";
    }

}