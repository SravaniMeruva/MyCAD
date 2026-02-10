import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.stream.*;
import java.util.*;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.Color;

public class MyCAD {
    public static final int ISOMETRIC = 1;
    public static final int FRONTVIEW = 11;
    public static final int TOPVIEW = 12;
    public static final int LEFTVIEW = 13;
    public static final int RIGHTVIEW = 14;
    public static final double COS30 = Math.cos(Math.toRadians(30.0));
    public static final double SIN30 = Math.sin(Math.toRadians(30.0));

    Map<String, Block> blocks = new HashMap<String, Block>();
    Map<String, Layer> layers = new HashMap<String, Layer>();
    Map<String, Ltype> ltypes = new HashMap<String, Ltype>();

    DefaultListModel<String> listModel = new DefaultListModel<String>();

    public static int debug = 0;
    JFrame f;
    JLabel viewLabel = new JLabel();
    JLabel fileLabel = new JLabel();
    JTextField statusText = new JTextField();
    DrawingSheet drawing;
    String[] data;

    public MyCAD(){

    }    

    public static void main(String[] args){
        if(args.length > 0){
            debug = 1;
        }
        MyCAD myCAD = new MyCAD();
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                myCAD.createAndShowGUI();
            }
        });
    }
    
    private void createAndShowGUI(){
        GridBagConstraints gbc = new GridBagConstraints();

        System.out.println("Created GUI on EDT?" + SwingUtilities.isEventDispatchThread());
        f = new JFrame("MyCAD");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // f.setSize(110000, 800);
        f.setJMenuBar(prepareMenuBar());
        f.setLayout(new GridBagLayout());
        f.setMinimumSize(new Dimension(1000, 800));
        drawing = new DrawingSheet();
        viewLabel.setText("Front Veiw");

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        // gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.BOTH;
        
        f.add(drawing, gbc);    //**************************************************************************

        JList<String> listShapes = new JList<String>(listModel);
        listShapes.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                  System.out.println(listShapes.getSelectedValue().toString());
                }
            }
        });
/////////////////////////////////
        JButton button = new JButton("test");
        button.setPreferredSize(new Dimension(80, 50));
        // button.setOnClickListener(new OnClickListener(){
        //     public void onClick(View view){
        //         setStatus(0, "Clicked");
        //     }
        // });
        
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                // setStatus(0, "Button Clicked");
                // populate drawing.shapes
                drawing.shapes.clear();
                drawing.shapes.add(new Line(new Point(10, 20, 0), new Point(20, 30, 0)));
                drawing.repaint();


            }
        });


        // listShapes.setSize(100,800);
        // listShapes.setVisibleRowCount(10);
        JScrollPane scrollPane = new JScrollPane(listShapes);
        scrollPane.setPreferredSize(new Dimension(150, (int) f.getHeight() / 3));
        // scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.VERTICAL;

        f.add(scrollPane, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        f.add(statusText, gbc);

        JScrollPane scrollCommand =  new JScrollPane(button);
        scrollCommand.setPreferredSize(new Dimension(100, 150));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        f.add(scrollCommand, gbc);

        //f.setJMenuBar(menuBar);
        f.pack();
        f.setVisible(true);
    }

    void initialiseDrawing (Integer viewType){
        System.out.println("Hello!");
    }

    private JMenuBar prepareMenuBar(){    
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
    
        JMenuItem item = new JMenuItem("Open Drawing");
        item.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));       
        item.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                data = null;
                getFileContents();
                if (data == null){
                    System.out.println("WARNING: getFileContents::data is null.");
                    setStatus(1, "getFileContents::data is null.");
                    return;
                }
                extractShapesData();
                System.out.println("shapes = " + drawing.shapes);

                listModel.clear();
                for (ObjectCAD element : drawing.shapes){
                    listModel.addElement(element.getType());
                }

                // drawing.calculateDrawingParms();
                drawing.repaint();
                setStatus(0, "shapes ("+ drawing.shapes.size() +") extracted and displayed!!!!!!");
            }
        });
        menu.add(item);

        item = new JMenuItem("Exit");
        item.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        item.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
        menu.add(item);

        menu = new JMenu("View");
        menuBar.add(menu);
        item = new JMenuItem("Isometric");
        item.setAccelerator(KeyStroke.getKeyStroke('I', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        item.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                handleEvents(ISOMETRIC);
                setStatus(0, "Isometric view displayed");
            }
        });
        menu.add(item);
        item = new JMenuItem("Front View");
        item.setAccelerator(KeyStroke.getKeyStroke('F', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        item.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                handleEvents(FRONTVIEW);
                setStatus(0, "Front view displayed");
            }
        });
        menu.add(item);
        item = new JMenuItem("Top View");
        item.setAccelerator(KeyStroke.getKeyStroke('T', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        item.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                handleEvents(TOPVIEW);
                setStatus(0, "Top view displayed");
            }
        });
        menu.add(item);

        item = new JMenuItem("Left View");
        item.setAccelerator(KeyStroke.getKeyStroke('L', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        item.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                handleEvents(LEFTVIEW);
                setStatus(0, "Left view displayed");
            }
        });
        menu.add(item);

        item = new JMenuItem("Right View");
        item.setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        item.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                handleEvents(RIGHTVIEW);
                setStatus(0, "Right view displayed");
            }
        });
        menu.add(item);

//        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(Box.createRigidArea(new Dimension(50,0)));
        menuBar.add(viewLabel);
        menuBar.add(Box.createRigidArea(new Dimension(50,0)));
        menuBar.add(fileLabel);
        return menuBar;

    }

    public void setStatus(int type, String message){
        switch(type){
            case 0:
                statusText.setText("INFO: " + message);
                statusText.setForeground(Color.BLACK);
                break;
            case 1:
                statusText.setText("WARNING: " + message);
                statusText.setForeground(Color.orange);
                break;
            case -1:
                statusText.setText("ERROR: " + message);
                statusText.setForeground(Color.RED);
                break;
        }
    }

    private void handleEvents(int vType){
        drawing.viewType = vType;
        switch(vType){
            case FRONTVIEW:
                viewLabel.setText("Front View");
                break;
            case ISOMETRIC:
                viewLabel.setText("Isometric View");
                break;
            case RIGHTVIEW:
                viewLabel.setText("Right View");
                break;
            case LEFTVIEW:
                viewLabel.setText("Left View");
                break;
            case TOPVIEW:
                viewLabel.setText("Top View");
                break;
        }
        drawing.calculateDrawingParms(); 
        drawing.repaint();
    }

    private void getFileContents(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("DXF File" , "dxf");
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new File("/mnt/5A9694069693E135/sources/java/my_cad/Sem2project/"));
        int returnVal = fileChooser.showOpenDialog(f);
        if (returnVal == JFileChooser.APPROVE_OPTION){
            System.out.println("==================================================");
            System.out.println("You chose to open this file " + fileChooser.getSelectedFile().getName());
            System.out.println("file path = " + fileChooser.getSelectedFile().getAbsolutePath());
            fileLabel.setText(fileChooser.getSelectedFile().getName());
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
                Stream<String> lines = br.lines();
                data = lines.toArray(String[]::new);
                br.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void extractShapesData(){
        int i, value290 = 0, value370 = 0, value71 = 0, value70 = 0, value44 = 0, value45 = 0, value72 = 0, value73 = 0;
        int size = data.length;
        int blockTypeFlag = 0;
        boolean inProcess = false, inEntities = false, inBlocks = false, inLayers = false, inLtypes = false, resetValues = false;
        float  value10 = 0, value20 = 0, value30 = 0, value11 = 0, value21 = 0, value31 = 0, value40 = 0,value41 = 0, value42 = 0, value43 = 0, value50 = 0, value51 = 0 , value13 = 0, value14 = 0, value23 = 0, value24 = 0, value34 = 0, value33 = 0;
        String shape = "";
        String value2 = "";
        String mainShape = "";
        String value1 = "";
        String value3 = "";
        String value7 = "";
        String value6 = "";
        Point basePoint = null;
        List<Float> knots = null;
        List<Point> vertices = null;
        List<Point> fitPoints = null;
        List<ObjectCAD> extractShapes = null;        

        drawing.shapes.clear();

        for(i = 0; i < size; i = i + 2){
            if(inProcess){
                switch (data[i].trim()){
                    case "1":
                        value1 = data[i+1];
                        break;
                    case "2":
                        value2 = data[i+1];
                        break;
                    case "3":
                        value3 = data[i+1];
                        break;
                    case "6":
                        value6 = data[i+1];
                        break;
                    case "7":
                        value7 = data[i+1];
                        break;
                    case "10":
                        value10 = Float.parseFloat(data[i+1]);
                        break;
                    case "20":
                        value20 = Float.parseFloat(data[i+1]);
                        if(shape.equals("LWPOLYLINE") && !data[i+2].trim().equals("30")){
                            vertices.add(new Point(value10, value20));
                        }
                        break;
                    case "30":
                        value30 = Float.parseFloat(data[i+1]);
                        if(shape.equals("SPLINE")){
                            vertices.add(new Point(value10, value20, value30));
                        }
                        if(shape.equals("LWPOLYLINE")){
                            vertices.add(new Point(value10, value20, value30));
                        }
                        break;
                    case "11":
                        value11 = Float.parseFloat(data[i+1]);
                        break;
                    case "21":
                        value21 = Float.parseFloat(data[i+1]);
                        break;                    
                    case "31":
                        value31 = Float.parseFloat(data[i+1]);
                        if(shape.equals("SPLINE")){
                            fitPoints.add(new Point(value11, value21, value31));
                        }
                        break;
                    case "13":
                        value13 = Float.parseFloat(data[i+1]);
                        break;
                    case "23":
                        value23 = Float.parseFloat(data[i+1]);
                        break;
                    case "33":
                        value33 = Float.parseFloat(data[i+1]);
                        break;
                    case "14":
                        value14 = Float.parseFloat(data[i+1]);
                        break;
                    case "24":
                        value24 = Float.parseFloat(data[i+1]);
                        break;
                    case "34":
                        value34 = Float.parseFloat(data[i+1]);
                        break;
                    case "40":
                        value40 = Float.parseFloat(data[i+1]);
                        if(shape.equals("SPLINE")){
                            knots.add(Float.parseFloat(data[i+1]));
                        }
                        break;
                    case "41":
                        value41 = Float.parseFloat(data[i+1]);
                        break;
                    case "42":
                        value42 = Float.parseFloat(data[i+1]);
                        break;
                    case "43":
                        value43 = Float.parseFloat(data[i+1]);
                        break;
                    case "44":
                        value44 = (int) Float.parseFloat(data[i+1]);
                        break;
                    case "45":
                        value45 = (int) Float.parseFloat(data[i+1]);
                        break;
                    case "50":
                        value50 = Float.parseFloat(data[i+1]);
                        break;
                    case "51":
                        value51 = Float.parseFloat(data[i+1]);
                    case "70":
                        value70 = (int) Float.parseFloat(data[i+1]);
                        break;
                    case "71":
                        value71 = (int) Float.parseFloat(data[i+1]);
                        break;
                    case "72":
                        value72 = (int) Float.parseFloat(data[i+1]);
                        break;
                    case "73":
                        value73 = (int) Float.parseFloat(data[i+1]);
                        break;
                    case "290":
                        value290 = (int) Float.parseFloat(data[i+1]);
                        break;
                    case "370":
                        value370 = (int) Float.parseFloat(data[i+1]);
                        break;
                    case "0":
                        if(inBlocks || inEntities || inLayers || inLtypes){
                            switch (shape){
                            case "LINE":
                                extractShapes.add(new Line(new Point(value10, value20, value30), new Point(value11, value21, value31)));
                                break;
                            case "CIRCLE":
                                extractShapes.add(new Circle(new Point(value10, value20, value30), value40));
                                break;
                            case "VERTEX":
                                vertices.add(new Point(value10, value20, value30));
                                if(mainShape == "POLYLINE" && ! data[i+1].equals("VERTEX")){
                                    if(value70 == 1){
                                        vertices.add(vertices.get(0));
                                    }
                                    extractShapes.add(new PolyLine(vertices, (int) value70));
                                }
                                break;
                            case "ARC":
                                extractShapes.add(new Arc(new Point(value10, value20, value30), value40, (double) value50,(double) value51));
                                break;
                            case "ELLIPSE":
                                extractShapes.add(new Ellipse(new Point(value10, value20, value30), new Point(value11, value21, value31),value40, value41, value42));
                                break;
                            case "SPLINE":
                                extractShapes.add(new Spline(value70, value71, vertices, fitPoints, knots));
                                break;
                            case "INSERT":
                                extractShapes.add(new Insert(value2, new Point(value10, value20, value30), value50, value70, value71, value44, value45, value41, value42, value43, blocks.get(value2)));
                                break;
                            case "BLOCK":
                                basePoint = new Point(value10, value20, value30);
                                blockTypeFlag = value70;
                                break;
                            case "MTEXT":
                                extractShapes.add(new Mtext(new Point(value10, value20, value30), new Point(value11, value21, value31), value1 + " " + value3, value7, value73, value72, value71, value40, value41, value50));
                                break;
                            case "POINT":
                                extractShapes.add(new PointCAD(value10, value20, value30));
                                break;
                            case "TEXT":
                                extractShapes.add(new Text(new Point(value10, value20, value30), new Point(value11, value21, value31), value1, value7, value40, value50, value71, value72, value73));
                                break;
                            case "LAYER":       //ADD PREVIOUS LAYER, last LAYER will be added at ENDTAB
                                layers.put(value2, new Layer(value2, "", 0, 0, 0));
                                break;
                            case "LTYPE":       //ADD PREVIOUS LTYPE, last LTYPE will be added at ENTAB
                                ltypes.put(value2, new Ltype(value2));
                                break;
                            case "LWPOLYLINE":
                                extractShapes.add(new LwPolyLine(vertices, value70, (int) value43));
                                break;
                            case "DIMENSION":
                                extractShapes.add(new CadDimension(new Point(value11, value21, value31), new Point(value10, value20, value30), new Point(value13, value23, value33), new Point(value14, value24, value34), value70, value71, value42));
                                break;
                            }                            
                        }

                        inProcess = false;
                        break;
                    default: 
                }
            }

            // SET/RESET inEnties, inLayers, inLtypes, inBlocks ......
            if(data[i].trim().equals("2") && data[i+1].trim().equals("ENTITIES")){
                inEntities = true;
                extractShapes = drawing.shapes;
            }
            if(inEntities  && data[i].trim().equals("0") && data[i+1].trim().equals("ENDSEC")){
                inEntities = false;
                extractShapes = null;
            }
            if(data[i].trim().equals("2") && data[i+1].trim().equals("BLOCKS"))
                inBlocks = true;
            if(inBlocks  && data[i].trim().equals("0") && data[i+1].trim().equals("ENDSEC"))
                inBlocks = false;

            // NEEDS inEntities, inBlocks, inLayers, inLtypes SET/RESET before the following code ...........
            if(data[i].trim().equals("0") ){            
                switch(data[i+1].trim()){
                    case "SPLINE":
                        vertices = new ArrayList<Point>();
                        fitPoints = new ArrayList<Point>();
                        knots = new ArrayList<Float>();
                        shape = data[i+1];
                        inProcess = true;
                        resetValues = true;
                        break;
                    case "POLYLINE":
                        mainShape = "POLYLINE"; 
                        vertices = new ArrayList<Point>();    
                        shape = data[i+1];
                        inProcess = true;
                        resetValues = true;
                        break;
                    case "LWPOLYLINE":
                        vertices = new ArrayList<Point>();    
                        shape = data[i+1];
                        inProcess = true;
                        resetValues = true;
                        break;
                    case "LINE":
                    case "CIRCLE":
                    case "VERTEX":
                    case "ARC":
                    case "ELLIPSE":
                    case "MLINE":
                    case "INSERT":
                    case "MTEXT":
                    case "POINT":
                    case "TEXT":
                    case "LAYER":
                    case "LTYPE":
                    case "DIMENSION":
                        shape = data[i+1];
                        inProcess = true;
                        resetValues = true;
                        break;
                    case "SEQEND":          // KEEP THIS, otherwise this will be added to UNKNOWN_SHAPES ........
                        break;
                    case "BLOCK":
                        extractShapes = new ArrayList<ObjectCAD>();
                        basePoint = new Point(0, 0, 0);
                        shape = data[i+1];
                        inProcess = true;
                        resetValues = true;
                        break;
                    case "ENDBLK":
                        blocks.put(value2, new Block(basePoint, blockTypeFlag, value2, extractShapes));
                        break;
                    case "ENDTAB":          //ADD LAST LAYER/LTYPE TO MAP.......................
                        if (inLayers)   
                            layers.put(value2, new Layer(value2, "", 0, 0, 0));
                        if (inLtypes)
                            ltypes.put(value2, new Ltype(value2));
                    default:
                        if (inEntities){
                            System.out.println("Unknown shape: " + data[i+1]);
                            drawing.unknownShapes.add(data[i+1]); 
                        }
                        break;
                }
            }

            if(data[i].trim().equals("2") && data[i+1].trim().equals("LAYER"))
                inLayers = true;
            if(inLayers  && data[i].trim().equals("0") && data[i+1].trim().equals("ENDTAB"))
                inLayers = false;
            
            if(data[i].trim().equals("2") && data[i+1].trim().equals("LTYPE"))
                inLtypes = true;
            if(inLtypes  && data[i].trim().equals("0") && data[i+1].trim().equals("ENDTAB"))
                inLtypes = false;

            // FOLLOWING SHOULD BE THE LAST PART IN ... FOR LOOP .....
            if (resetValues){
                value3 = "";
            }
        }
        //System.out.println("blocks = " + blocks.toString());
        System.out.println("layers = " + layers.toString());
        System.out.println("ltypes = " + ltypes.toString());
    }
}