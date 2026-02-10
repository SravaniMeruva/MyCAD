import java.util.*;

public class Block {
    Point basePoint;
    int typeFlag;
    List<ObjectCAD> blockShapes;
    String blockName;

    public Block(Point _basePoint, int _typeFlag, String _blockName, List<ObjectCAD> _blockShapes){
        basePoint = _basePoint;
        typeFlag = _typeFlag;
        blockShapes = _blockShapes;
        blockName = _blockName;
    }

    public String toString(){
        return " Base Point = " + basePoint.toString() + " typeFlag = " + typeFlag + " blockName = " + blockName + " blockShapes = " + blockShapes; 
    }
}
