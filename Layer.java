public class Layer {
    String name;
    String type;
    int standardFlag, plottingFlag, lineWeightEnum;
    
    public Layer(String _name, String _type, int _standardFlag, int _plottingFlag, int _lineWeightEnum){
        name = _name;
        type = _type;
        standardFlag = _standardFlag;
        plottingFlag = _plottingFlag;
        lineWeightEnum = _lineWeightEnum;        
    }

    public String toString(){
        return "LAYER: name= " + name + " type= " + type + " standard flag= " + standardFlag;
    }
}
