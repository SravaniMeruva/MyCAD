import java.util.*;

public class Insert extends ObjectCAD {
    String blockName = "";
    Point insertPoint;
    Point scale;
    Point basePoint;
    float rotAngle;
    int colCount, rowCount, colSpace, rowSpace;
    List<ObjectCAD> insertShapes;
    double cosRot;
    double sinRot;

    public  Insert (String _blockName,Point _insertPoint,float _rotAngle, int _colCount, int _rowCount, int _colSpace, int _rowSpace, float _scaleX, float _scaleY, float _scaleZ, Block _block){
        this(_blockName, _insertPoint, _rotAngle, _colCount, _rowCount, _colSpace, _rowSpace, _scaleX, _scaleY, _scaleZ, _block.basePoint, _block.blockShapes);
    }

    public  Insert (String _blockName,Point _insertPoint,float _rotAngle, int _colCount, int _rowCount, int _colSpace, int _rowSpace, float _scaleX, float _scaleY, float _scaleZ, Point _basePoint, List<ObjectCAD> _blockShapes){
        blockName = _blockName;
        insertPoint = _insertPoint;
        basePoint =  _basePoint;
        rotAngle = _rotAngle;
        colCount = _colCount;
        rowCount = _rowCount;
        colSpace = _colSpace;
        rowSpace = _rowSpace;
        scale = new Point(_scaleX, _scaleY, _scaleZ);
        insertShapes = new ArrayList<ObjectCAD>();

        for(ObjectCAD element : _blockShapes){
            ObjectCAD newElement = element.clone();              //get a new instance of Line, Circle,.....
            newElement = newElement.transformShape(insertPoint, basePoint, scale, rotAngle);         
            insertShapes.add(newElement);
            points.addAll(newElement.getExtremePoints());
        }  
    }

    public Insert getIsometricShape(){
        return this;
    }

    public Insert clone(){
        return new Insert(blockName, insertPoint, rotAngle, colCount, rowCount, colSpace, rowSpace, scale.x, scale.y, scale.z, basePoint, insertShapes);
    }

    public String toString(){
        return "INSERT: BlockName " + blockName + " insert point " + insertPoint + " Rotation Angle " + rotAngle + "Insert Shapes" + insertShapes;
    }

}
