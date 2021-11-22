import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class _MainGUI extends JPanel implements ActionListener {

    //two panels, left & right
    //left uses gbc, right uses default
    //main frame uses default layout
    
    JComboBox<String> type;
    JLabel typeText;
    String typeValue;
    JEditorPane seed;
    JLabel seedText;
    int seedValue;
    JEditorPane x;
    JLabel xText;
    int xValue;
    JEditorPane y;
    JLabel yText;
    int yValue;
    JEditorPane cells;
    JLabel cellsText;
    int cellsValue;
    JCheckBox connect;
    JLabel connectText;
    boolean connectValue;
    JEditorPane csv;
    JLabel csvText;
    String csvValue;
    JButton outputCSV;
    JButton generate;
    Surface dungeonView;
//    JButton dungeonView;
    Dungeon dun;
    int scale;
    
    static JFrame jf;
    
    GridBagConstraints c;
    
    public _MainGUI() {
        setLayout(new GridBagLayout());
    
        c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
    
        type = new JComboBox<String>(new String[] {"Random Walk", "Cellular Automata", "Voronoi"});
        typeText = new JLabel("Type: ");
        typeValue = "Random Walk";
        type.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                typeValue = (String) type.getSelectedItem();
            }
        });
    
        seed = new JEditorPane("text/plain", "0");
        seedText = new JLabel("Seed: ");
        seedValue = 0;
    
        x = new JEditorPane("text/plain", "100");
        xText = new JLabel("Width: ");
        xValue = 100;
    
        y = new JEditorPane("text/plain", "100");
        yText = new JLabel("Height: ");
        yValue = 100;
    
        cells = new JEditorPane("text/plain", "400");
        cellsText = new JLabel("Cells (Voronoi-specific): ");
        cellsValue = 400;
    
        connect = new JCheckBox();
        connectText = new JLabel("Connect Rooms?");
        connectValue = false;
        
        csv = new JEditorPane("text/plain", "output.csv");
        csvText = new JLabel("CSV Filepath: ");
        csvValue = "output.csv";
    
        outputCSV = new JButton("Output CSV");
        outputCSV.addActionListener(this);
        generate = new JButton("Generate");
        generate.addActionListener(this);
    
        dun = new BetterRandomWalk(seedValue,xValue,yValue);
        dun.randomize();
        scale = 5;
        dungeonView = new Surface(dun,scale);
        dungeonView.setPreferredSize(new Dimension(500,500));
    
        c.anchor = c.EAST;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 3;
        c.fill = c.HORIZONTAL;
        add(type,c);
    
        c.gridx = 0;
        c.gridy = 0;
        //    c.gridwidth = 3;
        //    c.fill = c.HORIZONTAL;
        add(typeText,c);
    
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 3;
        c.fill = c.HORIZONTAL;
        add(seed,c);
    
        c.gridx = 0;
        c.gridy = 1;
        //    c.gridwidth = 2;
        //    c.fill = c.HORIZONTAL;
        add(seedText,c);
    
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        c.fill = c.HORIZONTAL;
        add(x,c);
    
        c.gridx = 0;
        c.gridy = 3;
        //    c.gridwidth = 1;
        //    c.fill = c.HORIZONTAL;
        add(xText,c);
    
        c.gridx = 3;
        c.gridy = 3;
        c.gridwidth = 1;
        c.fill = c.HORIZONTAL;
        add(y,c);
    
        c.gridx = 2;
        c.gridy = 3;
        //    c.gridwidth = 1;
        //    c.fill = c.HORIZONTAL;
        add(yText,c);
    
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 1;
        c.fill = c.HORIZONTAL;
        add(cells,c);
    
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        c.fill = c.HORIZONTAL;
        add(cellsText,c);
        
        c.gridx = 2;
        c.gridy = 4;
        c.gridwidth = 1;
        c.fill = c.HORIZONTAL;
        add(connectText,c);
        
        c.gridx = 3;
        c.gridy = 4;
        c.gridwidth = 1;
        c.fill = c.HORIZONTAL;
        add(connect,c);
    
        c.gridx = 1;
        c.gridy = 5;
        c.gridwidth = 3;
        c.fill = c.HORIZONTAL;
        add(csv,c);
    
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        c.fill = c.HORIZONTAL;
        add(csvText,c);
    
        c.anchor = c.CENTER;
        c.gridx = 1;
        c.gridy = 6;
        c.gridwidth = 1;
        c.fill = c.HORIZONTAL;
        add(outputCSV,c);
    
        c.gridx = 3;
        c.gridy = 6;
        c.gridwidth = 1;
        c.fill = c.HORIZONTAL;
        add(generate,c);
    
        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 4;
        c.gridheight = 7;
        c.fill = c.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(dungeonView,c);
    
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    
        System.out.println("Working...");
    
        //    if(e.getSource() == type) {
        typeValue = (String) type.getSelectedItem();
        //    } else if (e.getSource() == seed) {
        seedValue = Integer.parseInt(seed.getText());
        //    System.out.println("Seed = " + seedValue);
        //    } else if (e.getSource() == x) {
        xValue = Integer.parseInt(x.getText());
        //    System.out.println("x = " + xValue);
        //    } else if (e.getSource() == y) {
        yValue = Integer.parseInt(y.getText());
        //    System.out.println("y = " + yValue);
        //    } else if (e.getSource() == cells) {
        cellsValue = Integer.parseInt(cells.getText());
        
        connectValue = connect.isSelected();
        
        //    } else if (e.getSource() == csv) {
        csvValue = csv.getText();
        //    } else if (e.getSource() == outputCSV) {
        //        System.out.println("you're supposed to output a CSV here");
        //    } else if (e.getSource() == generate) {
        scale = (xValue > 500 || yValue > 500) ? 1 : (xValue >= yValue) ? 500/xValue : 500/yValue;
        //        DungeonViewer dv = new DungeonViewer(dun,scale);
        //    }
    
        switch(typeValue) {
            case "Random Walk":
                dun = new BetterRandomWalk(seedValue,xValue,yValue);
                break;
            case "Cellular Automata":
                dun = new CellularAutomata(seedValue,xValue,yValue);
                break;
            case "Voronoi":
                dun = new Voronoi(seedValue,xValue,yValue,cellsValue);
                break;
        }
    
        dun.randomize();
        if(connectValue) { dun.setLayout(dun.connectRooms()); }
    
        remove(dungeonView);
        
        dungeonView = new Surface(dun,scale);
        
        dungeonView.setSize(500, 500);
        
        dungeonView.repaint();
        
        
        
        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 4;
        c.gridheight = 7;
        c.fill = c.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(dungeonView,c);
        
        update();
        
        if(e.getSource().equals(outputCSV)) {
            
            try {
                dun.outputCSV("GeneratorCode/dungeonCSV/" + csvValue);
                System.out.println("\n\nCSV output worked, check file at " + "GeneratorCode/dungeonCSV/" + csvValue);
            } catch(Exception ex) {
                System.out.println("\n\nCSV output didn't work, filepath \'" + "GeneratorCode/dungeonCSV/" + csvValue + "\' was bad");
            }
            
        }
    
        System.out.println("Done.");
    }
    
    public static void main(String[] args) throws IOException {
        
        _MainGUI gui = new _MainGUI();
        
        jf = new JFrame("Project Stygia Dungeon Generator");
        
        jf.add(gui);
        
        jf.setSize(1000,750);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        jf.pack();
        
        jf.setVisible(true);
    }
    
    public void update() {
        jf.revalidate();
        jf.repaint();
    }
    
//    public void paint(Graphics g) {
//        dungeonView.paintComponent(g);
//    }
    
}
