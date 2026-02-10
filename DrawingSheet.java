import javax.swing.BorderFactory;
import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.awt.Stroke;
import java.awt.BasicStroke;

public class DrawingSheet extends JComponent{
    int viewType;
    java.util.List<ObjectCAD> shapes;
    java.util.List<ObjectCAD> shapes2Draw;
    java.util.List<ObjectCAD> axes;
    java.util.List<ObjectCAD> dimenShapes;
    java.util.List<String> unknownShapes;
    int sheetWidth ;
    int sheetHeight;
    float scale = 1;
    int xMargin, yMargin;
    int xOffset, yOffset;

    public DrawingSheet(){
        setBorder(BorderFactory.createLineBorder(Color.black));
        viewType = MyCAD.FRONTVIEW;
        shapes = new ArrayList<ObjectCAD>();
        shapes2Draw = new ArrayList<ObjectCAD>();
        axes = new ArrayList<ObjectCAD>();
        unknownShapes = new ArrayList<String>();
        dimenShapes = new ArrayList<ObjectCAD>();
        sheetHeight = 800;
        sheetWidth = 1000;
        xMargin = sheetWidth * 5/100;
        yMargin = sheetHeight * 5/100;
        System.out.println("sheetWidth = " + sheetWidth + ",, sheetHeight = " + sheetHeight + ",, xMargin = " + xMargin+ ",, yMargin = " + yMargin +"...");
    }

    public void updateSheetDimensions(){
        // System.out.println(getWidth() + "; " + getHeight());
        sheetWidth = getWidth();
        sheetHeight = getHeight();
        xMargin = sheetWidth * 5/100;
        yMargin = sheetHeight * 5/100;
        System.out.println("sheetWidth = " + sheetWidth + ",, sheetHeight = " + sheetHeight + ",, xMargin = " + xMargin+ ",, yMargin = " + yMargin +"...");
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        if (shapes.size() > 0){
            updateSheetDimensions();
            calculateDrawingParms();

            Graphics2D g2d = (Graphics2D)g;
            drawShapes(g2d, axes, viewType);          //drawAxes
            drawShapes(g2d, shapes2Draw, viewType);
            g2d.dispose();
        }
    }

    public void drawShapes(Graphics2D g, List<ObjectCAD> listShapes, int vType){

        ObjectCAD element;

        if(listShapes.size() == 0){
            System.out.println("WARNING: drawShapes::listShapes is empty.");
            return;
        }
        for(ObjectCAD oldElement : listShapes){
            element = oldElement.getViewShape(vType);
            if(MyCAD.debug == 1) System.out.println("getViewShape: " + oldElement + "-->" + element);
            switch (element.getType()){
                case "Line":
                    lineDraw(g, viewType, (Line) element);
                    break;
                case "Circle":
                    circleDraw(g, viewType, (Circle) element);
                    break;    
                case "PolyLine":
                    polyLineDraw(g, viewType, (PolyLine) element);
                    break;
                case "LwPolyLine":
                    lwPolyLineDraw(g, viewType, (LwPolyLine) element);
                    break;
                case "Arc":
                    arcDraw(g, viewType, (Arc) element);
                    break;
                case "Ellipse":
                    ellipseDraw(g, viewType, (Ellipse) element);
                    break;
                case "Insert":
                    drawShapes(g, ((Insert) element).insertShapes, vType);
                    break;
                case "Mtext":
                    mtextDraw(g, viewType, (Mtext) element);
                    break;
                case "Point":
                    pointDraw(g, viewType, (PointCAD) element);
                    break;
                case "Text":
                    textDraw(g, viewType, (Text) element);
                    break;
                case "Dimension":
                    dimensionDraw(g, viewType, (CadDimension) element);
                    break;
            }

        }
    }

    public void dimensionDraw(Graphics2D g2d, int viewType, CadDimension dimen){
        dimenShapes = new ArrayList<ObjectCAD>();
        // dimenShapes.add(new Text(dimen.defPoint1, new Point(0, 0, 0), "X", "", 0, 0, 0, 0, 0));
        // dimenShapes.add(new Text(dimen.defPoint2, new Point(0, 0, 0), "X", "", 0, 0, 0, 0, 0));
        dimenShapes.add(new Text(dimen.midPoint, new Point(0, 0, 0), String.valueOf(dimen.value), "", 0, 0, 0, 0, 0));
        // dimenShapes.add(new Line(dimen.defPoint1, dimen.defPoint2));
        drawShapes(g2d, dimenShapes, viewType);
    }

    public void lwPolyLineDraw(Graphics g, int viewType, LwPolyLine lwPolyLine){
        List<Point> vertices = lwPolyLine.getExtremePoints();
        if (MyCAD.debug > 0)  System.out.println(vertices);
        for(int i = 1; i < vertices.size(); i++){        
            lineDraw(g, viewType, vertices.get(i-1), vertices.get(i));
        }
        
    }
    public void textDraw(Graphics g, int viewType, Text text){
        switch (viewType) {
            case MyCAD.FRONTVIEW:
            case MyCAD.ISOMETRIC:
            case MyCAD.LEFTVIEW:
            case MyCAD.RIGHTVIEW:
            case MyCAD.TOPVIEW:
                // g.drawString(mtext.text, (int)(xOffset + mtext.insertPoint.x * scale), sheetHeight - ((int)(yOffset + mtext.insertPoint.y * scale)));
                g.drawString(text.text, (int)(xOffset + text.firstAlign.x * scale), sheetHeight - ((int) (yOffset + text.firstAlign.y * scale)));
                break;
            default:
                break;
        }
    }

    public void mtextDraw(Graphics g, int viewType, Mtext mtext){
        switch (viewType){
            case MyCAD.ISOMETRIC:
            case MyCAD.FRONTVIEW:
            case MyCAD.LEFTVIEW:
            case MyCAD.RIGHTVIEW:
            case MyCAD.TOPVIEW:
                g.drawString(mtext.text, (int)(xOffset + mtext.insertPoint.x * scale), sheetHeight - ((int)(yOffset + mtext.insertPoint.y * scale)));
                break;
        }
    }

    public void pointDraw(Graphics g, int viewType, PointCAD point){
        switch (viewType) {
            case MyCAD.FRONTVIEW:
            case MyCAD.ISOMETRIC:
            case MyCAD.LEFTVIEW:
            case MyCAD.RIGHTVIEW:
            case MyCAD.TOPVIEW:
            // g.drawLine((int) (xOffset + scale * start.x),sheetHeight - (int)(yOffset + scale * start.y),  (int)(xOffset + scale * end.x), sheetHeight - (int)(yOffset + scale * end.y));
            // System.out.println((int) (scale * start.x) +","+ (int)(scale * start.y)+","+ (int)(scale * end.x)+","+ (int)(scale * end.y));
            g.drawLine((int) (xOffset + scale * point.x), sheetHeight - (int)(yOffset + scale * point.y), (int) (xOffset + scale * point.x), sheetHeight - (int)(yOffset + scale * point.y));
            System.out.println((int) (scale * point.x) +","+ (int)(scale * point.y)+","+ (int)(scale * point.x)+","+ (int)(scale * point.y));
                break;
            default:
                break;
        }

    }

    public void ellipseDraw(Graphics2D g, int viewType, Ellipse ellipse){

        AffineTransform old = null;
        if (ellipse.majorAngle != 0.0){
            old = g.getTransform();
            System.out.println(Math.toDegrees(ellipse.majorAngle));
            g.rotate(0.0-ellipse.majorAngle,(double) (xOffset + scale * ellipse.center.x),(double) (sheetHeight - (yOffset + scale * ellipse.center.y)));
            Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9, 9, 1, 9}, 0);
            g.setStroke(dashed);
        }

        switch (viewType){
            case MyCAD.ISOMETRIC:
            case MyCAD.FRONTVIEW:
                if (ellipse.fullEllipse){
                    g.drawOval((int)(xOffset + scale * (ellipse.center.x - ellipse.major)), sheetHeight - (int)(yOffset + scale * (ellipse.center.y + ellipse.minor)), (int)(scale * 2 * ellipse.major),  (int)( scale * 2 * ellipse.minor));
                    System.out.println((int)(xOffset + scale * (ellipse.center.x - ellipse.major)) + ", " + (sheetHeight - (int)(yOffset + scale * (ellipse.center.y + ellipse.minor))) + ", " +  (int)(scale * 2 * ellipse.major) + ", " + (int)(scale * 2 * ellipse.minor));
                } else {
                    g.drawArc((int)(xOffset + scale * (ellipse.center.x - ellipse.major)), sheetHeight - (int)(yOffset + scale * (ellipse.center.y + ellipse.minor)), (int)(scale * 2 * ellipse.major),  (int)( scale * 2 * ellipse.minor), (int) Math.toDegrees(ellipse.startAngle), (int) Math.ceil(Math.toDegrees(ellipse.endAngle-ellipse.startAngle)));
                    System.out.println((int)(xOffset + scale * (ellipse.center.x - ellipse.major)) + ", " + (sheetHeight - (int)(yOffset + scale * (ellipse.center.y + ellipse.minor))) + ", " +  (int)(scale * 2 * ellipse.major) + ", " + (int)(scale * 2 * ellipse.minor) + " startAngle = "+(int) Math.toDegrees(ellipse.startAngle) + " arcAngle = " + Math.ceil(Math.toDegrees(ellipse.endAngle-ellipse.startAngle)));
                }
                break;
            case MyCAD.TOPVIEW:
                break;
            case MyCAD.LEFTVIEW:
                break;
            case MyCAD.RIGHTVIEW:   //not verified
                break;
        }

        if (ellipse.majorAngle != 0.0)
            g.setTransform(old);
        
    }
    public void arcDraw(Graphics g, int viewType, Arc arc){
        switch(viewType){
            case MyCAD.ISOMETRIC:
            case MyCAD.FRONTVIEW:
                g.drawArc((int)(xOffset + scale * (arc.center.x - arc.radius)), sheetHeight - (int)(yOffset + scale * (arc.center.y + arc.radius)), (int)(scale * 2 * arc.radius),  (int)( scale * 2 * arc.radius), (int) (arc.startAngle), (int) (arc.endAngle-arc.startAngle));
                System.out.println((int)(xOffset + scale * (arc.center.x - arc.radius)) + ", " + (sheetHeight - (int)(yOffset + scale * (arc.center.y + arc.radius))) + ", " +  (int)(scale * 2 * arc.radius) + ", " + (int)(scale * 2 * arc.radius) + " startAngle = "+(int) (arc.startAngle) + " arcAngle = " + (int) (arc.endAngle-arc.startAngle));
                break;
            case MyCAD.TOPVIEW:     //NOT TESTED
                g.drawArc((int)(xOffset + scale * (arc.center.x - arc.radius)), sheetHeight - (int)(yOffset + scale * (arc.center.z + arc.radius)), (int)(scale * 2 * arc.radius),  (int)( scale * 2 * arc.radius), (int) (arc.startAngle), (int) (arc.endAngle -arc.endAngle));
                System.out.println((int)(xOffset + scale * (arc.center.x - arc.radius)) + ", " +(sheetHeight - (int)(yOffset + scale * (arc.center.z + arc.radius))) + ", " + (int)(scale * 2 * arc.radius) + ", " + (int)(scale * 2 * arc.radius) + " startAngle = "+(int) (arc.startAngle) + " arcAngle = " + (int) (arc.endAngle-arc.startAngle));
                break;
            case MyCAD.LEFTVIEW:    //NOT TESTED
                g.drawArc((int)(xOffset + scale * (arc.center.z - arc.radius)), sheetHeight - (int)(yOffset + scale * (arc.center.y + arc.radius)), (int)(scale * 2 * arc.radius),  (int)( scale * 2 * arc.radius), (int) (arc.startAngle), (int) (arc.endAngle-arc.startAngle));
                System.out.println((int)(xOffset + scale * (arc.center.z - arc.radius)) + ", " + (sheetHeight - (int)(yOffset + scale * (arc.center.y + arc.radius))) + ", " + (int)(scale * 2 * arc.radius) + ", " + (int)(scale * 2 * arc.radius)  + " startAngle = "+(int) (arc.startAngle) + " arcAngle = " + (int) (arc.endAngle-arc.startAngle));
                break;
            case MyCAD.RIGHTVIEW:   //NOT TESTED
                g.drawArc(sheetWidth - (int)(xOffset + scale * (arc.center.z - arc.radius)), sheetHeight - (int)(yOffset + scale * (arc.center.y + arc.radius)), sheetWidth - (int)(scale * 2 * arc.radius),  (int)( scale * 2 * arc.radius), (int) (arc.startAngle), (int) (arc.endAngle-arc.startAngle));
                System.out.println((int)(xOffset + scale * (arc.center.z - arc.radius)) + ", " + (sheetHeight - (int)(yOffset + scale * (arc.center.y + arc.radius))) + ", " + (int)(scale * 2 * arc.radius) + ", " + (int)(scale * 2 * arc.radius) + " startAngle = "+(int) (arc.startAngle) + " arcAngle = " + (int) (arc.endAngle-arc.startAngle));
                break;

        }
    }
    public void polyLineDraw(Graphics g, int viewType, PolyLine polyLine){
        List<Point> vertices = polyLine.getExtremePoints();
        if (MyCAD.debug > 0)  System.out.println(vertices);
        for(int i = 1; i < vertices.size(); i++){        
            lineDraw(g, viewType, vertices.get(i-1), vertices.get(i));
        }
    }


    public void circleDraw(Graphics g, int viewType, Circle circle){
        switch(viewType){
            case MyCAD.ISOMETRIC:
            case MyCAD.FRONTVIEW:
                g.drawOval((int)(xOffset + scale * (circle.center.x - circle.radius)), sheetHeight - (int)(yOffset + scale * (circle.center.y + circle.radius)), (int)(scale * 2 * circle.radius),  (int)( scale * 2 * circle.radius));
                System.out.println((int)(xOffset + scale * (circle.center.x - circle.radius)) + ", " + (sheetHeight - (int)(yOffset + scale * (circle.center.y + circle.radius))) + ", " +  (int)(scale * 2 * circle.radius) + ", " + (int)(scale * 2 * circle.radius));
                break;
            case MyCAD.TOPVIEW: //NOT TESTED
                g.drawOval((int)(xOffset + scale * (circle.center.x - circle.radius)), sheetHeight - (int)(yOffset + scale * (circle.center.z + circle.radius)), (int)(scale * 2 * circle.radius),  (int)( scale * 2 * circle.radius));
                System.out.println((int)(xOffset + scale * (circle.center.x - circle.radius)) + ", " +(sheetHeight - (int)(yOffset + scale * (circle.center.z + circle.radius))) + ", " + (int)(scale * 2 * circle.radius) + ", " + (int)(scale * 2 * circle.radius));
                break;
            case MyCAD.LEFTVIEW:    //NOT TESTED
                g.drawOval((int)(xOffset + scale * (circle.center.z - circle.radius)), sheetHeight - (int)(yOffset + scale * (circle.center.y + circle.radius)), (int)(scale * 2 * circle.radius),  (int)( scale * 2 * circle.radius));
                System.out.println((int)(xOffset + scale * (circle.center.z - circle.radius)) + ", " + (sheetHeight - (int)(yOffset + scale * (circle.center.y + circle.radius))) + ", " + (int)(scale * 2 * circle.radius) + ", " + (int)(scale * 2 * circle.radius));
                break;
            case MyCAD.RIGHTVIEW:   //NOT TESTED
                g.drawOval(sheetWidth - (int)(xOffset + scale * (circle.center.z - circle.radius)), sheetHeight - (int)(yOffset + scale * (circle.center.y + circle.radius)), sheetWidth - (int)(scale * 2 * circle.radius),  (int)( scale * 2 * circle.radius));
                System.out.println((int)(xOffset + scale * (circle.center.z - circle.radius)) + ", " + (sheetHeight - (int)(yOffset + scale * (circle.center.y + circle.radius))) + ", " + (int)(scale * 2 * circle.radius) + ", " + (int)(scale * 2 * circle.radius));
                break;

        }
    }    

    public void lineDraw(Graphics g, int viewType, Line shape){
        lineDraw(g, viewType, shape.start, shape.end);
    }

    public void lineDraw(Graphics g, int viewType, Point start, Point end){
        switch(viewType){
            case MyCAD.ISOMETRIC:                    
            case MyCAD.FRONTVIEW:              
                g.drawLine((int) (xOffset + scale * start.x),sheetHeight - (int)(yOffset + scale * start.y),  (int)(xOffset + scale * end.x), sheetHeight - (int)(yOffset + scale * end.y));
                System.out.println((int) (scale * start.x) +","+ (int)(scale * start.y)+","+ (int)(scale * end.x)+","+ (int)(scale * end.y));
                break;
            case MyCAD.TOPVIEW:
                g.drawLine((int) (xOffset + scale * start.x), sheetHeight - (int)(yOffset + scale * start.z), (int)(xOffset + scale * end.x), sheetHeight - (int)(yOffset + scale * end.z));
                System.out.println((int) (scale * start.x) +","+ (sheetHeight-(int)(scale * start.z))+","+ (int)(scale * end.x)+","+ (sheetHeight-(int)(scale * end.z)));
                break;
            case MyCAD.LEFTVIEW:
                g.drawLine((int) (xOffset + scale * start.z), sheetHeight - (int)(yOffset + scale * start.y), (int)(xOffset + scale * end.z), sheetHeight - (int)(yOffset + scale * end.y));
                System.out.println((int) (scale * start.z) +","+ (int)(scale * start.y)+","+ (int)(scale * end.z)+","+ (int)(scale * end.y));
                break;
            case MyCAD.RIGHTVIEW:
                g.drawLine(sheetWidth - (int) (xOffset + scale * start.z), sheetHeight - (int)(yOffset + scale * start.y), sheetWidth - (int)(xOffset + scale * end.z), sheetHeight - (int)(yOffset + scale * end.y));
                System.out.println((int) (scale * start.z) +","+ (int)(scale * start.y)+","+ (int)(scale * end.z)+","+ (int)(scale * end.y));
                break;
        }
        
    }

    public void calculateDrawingParms(){

        float xMin = 0;
        float xMax = 0;
        float yMin = 0;
        float yMax = 0;
        float zMin = 0;
        float zMax = 0;
        float xScale, yScale;
        Point point, point1;
        java.util.List<Point> points;

        if(viewType == MyCAD.ISOMETRIC){
            convertShapesData();
        } else {
            shapes2Draw = new ArrayList<ObjectCAD>(shapes);
        }

        System.out.println("shapes2Draw size = " + shapes2Draw.size());
        if(shapes2Draw.size() == 0 ){
            System.out.println("WARNING: calculateDRawingParms::shapes2Draw is empty.");
            return;
        }

        points = new ArrayList<Point>();
        for(ObjectCAD element : shapes2Draw){
//            points = element.getExtremePoints();
            points.addAll(element.getExtremePoints());
            if (MyCAD.debug > 0) System.out.println("points : " + points.toString());
        }
        if(points.size() == 0){
            System.out.println("WARNING: calculateDrawingParms::points is empty.");
            return;
        }
        xMin = points.get(0).x;
        xMax = points.get(0).x;
        yMin = points.get(0).y;
        yMax = points.get(0).y;
        zMin = points.get(0).z;
        zMax = points.get(0).z;
    
        if (MyCAD.debug == 1) System.out.println("points = " + points);
        for(Point smallPoint : points){
            
            xMin = (xMin < smallPoint.x) ? xMin : smallPoint.x;
            xMax = (xMax > smallPoint.x) ? xMax : smallPoint.x;
            yMin = (yMin < smallPoint.y) ? yMin : smallPoint.y;
            yMax = (yMax > smallPoint.y) ? yMax : smallPoint.y;
            zMin = (zMin < smallPoint.z) ? zMin : smallPoint.z;
            zMax = (zMax > smallPoint.z) ? zMax : smallPoint.z;

            switch(viewType){
                case MyCAD.ISOMETRIC:
                case MyCAD.FRONTVIEW:       //horizontal is x axis; vertical is y axis==> no change  
                    break;          
                case MyCAD.TOPVIEW:         //horizontal is x axis; vertical is z axis==> move z --> y
                    yMin = zMin;            
                    yMax = zMax;    
                    break;
                case MyCAD.LEFTVIEW:        //horizontal is z axis; vertical is y axis==> move z --> z
                case MyCAD.RIGHTVIEW:
                    xMin = zMin;            
                    xMax = zMax;
                    break;
            }
        }
                
        xScale = ((sheetWidth - (2*xMargin))/(xMax - xMin));
        yScale = ((sheetHeight- (2*yMargin))/(yMax - yMin));

        scale = (xScale < yScale) ? xScale : yScale;

        xOffset = xMargin -(int) (xMin*scale);
        yOffset = yMargin -(int) (yMin*scale);

        axes.clear();
        
        if(viewType == MyCAD.FRONTVIEW){
            //Vertical, Horizontal axes ....
            point = new Point(xMin - xMargin / scale / 2.0, Math.ceil(yMin));
            axes.add(new Line(point, new Point(xMin - xMargin/scale / 2.0, (sheetHeight + yMargin) / scale)));
            point = new Point(Math.ceil(xMin), yMin - yMargin/scale / 2.0);
            axes.add(new Line(point, new Point((sheetWidth - xMargin) / scale, yMin - yMargin/scale / 2.0)));

            //yMin, yMax
            point =  new Point(xMin - xMargin / scale / 3.0, Math.ceil(yMin));
            axes.add(new Line(new Point(xMin - xMargin/scale / 3.0 * 2.0, Math.ceil(yMin)), point));
            axes.add(new Mtext(new Point(point), new Point(0,0,0), String.valueOf(point.y), "", 0, 0, 0, 0, 0, 0 ));

            point = new Point(xMin - xMargin / scale / 3.0, Math.floor(yMax));
            point1 = new Point(xMin - xMargin/scale / 3.0 * 2.0, Math.floor(yMax));
            axes.add(new Line(point1, point));
            axes.add(new Mtext(new Point(point), new Point(0,0,0), String.valueOf(point.y), "", 0, 0, 0, 0, 0, 0 ));

            axes.add(new Mtext(new Point(point1), new Point(0,0,0), "Y", "", 0, 0, 0, 0, 0, 0 ));
            
            //xMin, xMax
            point = new Point(Math.ceil(xMin), yMin - yMargin / scale / 3.0);
            axes.add(new Line(point, new Point(Math.ceil(xMin), yMin - yMargin / scale / 3.0 * 2.0)));
            axes.add(new Mtext(new Point(point), new Point(0,0,0), String.valueOf(point.x), "", 0, 0, 0, 0, 0, 0 ));

            point = new Point(Math.floor(xMax), yMin - yMargin / scale / 3.0);
            point1 = new Point(Math.floor(xMax), yMin - yMargin / scale / 3.0 * 2.0);
            axes.add(new Line(point, point1));
            axes.add(new Mtext(new Point(point), new Point(0,0,0), String.valueOf(point.x), "", 0, 0, 0, 0, 0, 0 ));

            point1 = new Point(point1.x + xMargin / scale / 3.0, yMin - yMargin / scale );
            axes.add(new Mtext(new Point(point1), new Point(0,0,0), "X", "", 0, 0, 0, 0, 0, 0 ));
            
        }
        
        System.out.println( "xMin = " + xMin + ", xMax = " + xMax + ", yMin = " + yMin + ", yMax = " + yMax + ", xScale = " + xScale + ", yScale = " + yScale + ", Scale = " + scale + ", xOffset=" + xOffset + ", yOffset=" + yOffset);
    }


    public void convertShapesData(){
        shapes2Draw.clear();

        for(ObjectCAD element : shapes){
            shapes2Draw.add(element.getIsometricShape());
        }


        System.out.println("shapes2Draw = " + shapes2Draw);
        
    }

}

