import java.util.*;

public class Spline extends ObjectCAD {
    List<Point> controlPoints;
    List<Point> fitPoints;
    List<Float> knots;
    int splineDegree;
    int splineFlag;

    public Spline(int _splineFlag, int _splineDegree, List<Point> _controlPoints, List<Point> _fitPoints, List<Float> _knots){
        splineFlag = _splineFlag;
        splineDegree = _splineDegree;
        controlPoints = _controlPoints;
        fitPoints = _fitPoints;
        knots = _knots;
    }
    
    public Spline getIsometricShape(){
        return this;
    }

    public Spline clone(){
        return new Spline(splineFlag, splineDegree, controlPoints, fitPoints, knots);
    }

    public String toString(){
        return "SPLINE splineFlag = " + splineFlag + " splineDegree = " + splineDegree + " No of ctrlPoints = " + controlPoints.size() + " controlPoints = " + controlPoints.toString() + " No of fitPoints = " + fitPoints.size() + " fitPoints = " + fitPoints.toString() + " No of Knots = " + knots.size() + " knots = " + knots.toString();
    }
}
