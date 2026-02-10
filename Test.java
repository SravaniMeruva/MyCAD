import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

class Test {
    public static void main(String[] args){
        Test test = new Test();
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                test.createAndShowGUI();
            }
        });
    }
    
    private void createAndShowGUI(){
        GridBagConstraints gbc = new GridBagConstraints();
        
        System.out.println("Created GUI on EDT?" + SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridBagLayout());
        f.setMinimumSize(new Dimension(1100, 800));
        JPanel panel = new JPanel();
        //panel.setPreferredSize(new Dimension(1000, 800));
        panel.setMinimumSize(new Dimension(800, 600));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        f.add(panel, gbc);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        DefaultListModel<String> listModel = new DefaultListModel<String>();
        JList<String> list = new JList<String>(listModel);
        listModel.addElement("First");
        listModel.addElement("Second");
        listModel.addElement("Third");
        listModel.addElement("First");
        listModel.addElement("Second");
        listModel.addElement("Third");
        listModel.addElement("First");
        listModel.addElement("Second");
        listModel.addElement("Third");
        listModel.addElement("First");
        listModel.addElement("Second");
        listModel.addElement("Third");
        listModel.addElement("First");
        listModel.addElement("Second");
        listModel.addElement("Third");
        listModel.addElement("First");
        listModel.addElement("Second");
        listModel.addElement("Third");
        listModel.addElement("First");
        listModel.addElement("Second");
        listModel.addElement("Third");
        listModel.addElement("First");
        listModel.addElement("Second");
        listModel.addElement("Third");

        //list.setVisibleRowCount(2);
        JScrollPane scroll = new JScrollPane(list);
        scroll.setPreferredSize(new Dimension(150,(int) f.getHeight() / 3));
        //scroll.setMinimumSize(new Dimension(150, (int) f.getHeight() / 2));

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        //gbc.fill = GridBagConstraints.VERTICAL;

        f.add(scroll, gbc);

        JTable table = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel();

        

        f.pack();
        f.setVisible(true);
    }
}