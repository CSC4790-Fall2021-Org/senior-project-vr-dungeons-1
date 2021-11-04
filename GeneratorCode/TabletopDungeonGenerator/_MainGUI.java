import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

public class _MainGUI extends JPanel implements ActionListener {

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
    JEditorPane csv;
    JLabel csvText;
    String csvValue;
    JButton outputCSV;
    JButton generate;
    Surface dungeonView;
    Dungeon dun;
    int scale;
    
    public _MainGUI() throws IOException {
        
        setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
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
        cellsText = new JLabel("Cells: ");
        cellsValue = 400;
        
        csv = new JEditorPane("text/plain", "output.csv");
        csvText = new JLabel("CSV Filepath: ");
        csvValue = "output.csv";
        
        outputCSV = new JButton("Output CSV");
        generate = new JButton("Generate");
        generate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                seedValue = Integer.parseInt(seed.getText());
                System.out.println("Seed = " + seedValue);
                xValue = Integer.parseInt(x.getText());
                yValue = Integer.parseInt(y.getText());
                cellsValue = Integer.parseInt(cells.getText());
                csvValue = csv.getText();
                scale = (xValue >= yValue) ? 500/xValue : 500/yValue;
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
                dun.setLayout(dun.connectRooms());
                
                dungeonView = new Surface(dun,scale);
            }
        });
        
        dungeonView = new Surface(new Dungeon(0,100,100),5);
        
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 3;
        c.fill = c.HORIZONTAL;
        add(type,c);
        
        c.gridx = 0;
        c.gridy = 0;
//        c.gridwidth = 3;
//        c.fill = c.HORIZONTAL;
        add(typeText,c);
        
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 3;
        c.fill = c.HORIZONTAL;
        add(seed,c);
        
        c.gridx = 0;
        c.gridy = 1;
//        c.gridwidth = 2;
//        c.fill = c.HORIZONTAL;
        add(seedText,c);
        
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        c.fill = c.HORIZONTAL;
        add(x,c);
        
        c.gridx = 0;
        c.gridy = 3;
//        c.gridwidth = 1;
//        c.fill = c.HORIZONTAL;
        add(xText,c);
        
        c.gridx = 3;
        c.gridy = 3;
        c.gridwidth = 1;
        c.fill = c.HORIZONTAL;
        add(y,c);
        
        c.gridx = 2;
        c.gridy = 3;
//        c.gridwidth = 1;
//        c.fill = c.HORIZONTAL;
        add(yText,c);
        
        c.gridx = 2;
        c.gridy = 4;
        c.gridwidth = 1;
        c.fill = c.HORIZONTAL;
        add(cells,c);
        
        c.gridx = 1;
        c.gridy = 4;
//        c.gridwidth = 2;
//        c.fill = c.HORIZONTAL;
        add(cellsText,c);
        
        c.gridx = 2;
        c.gridy = 5;
        c.gridwidth = 2;
        c.fill = c.HORIZONTAL;
        add(csv,c);
        
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        c.fill = c.HORIZONTAL;
        add(csvText,c);
        
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
        c.fill = c.HORIZONTAL;
        c.gridheight = 7;
        c.fill = c.VERTICAL;
        add(dungeonView,c);
        
    }
    
    public static void main(String[] args) throws IOException {
        
        _MainGUI gui = new _MainGUI();
        JFrame jf = new JFrame("Project Stygia Dungeon Generator");
        
        jf.setSize(500,500);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        jf.add(gui);
//        jf.pack();
        jf.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == type) {
            typeValue = (String) type.getSelectedItem();
        } else if (e.getSource() == seed) {
            seedValue = Integer.parseInt(seed.getText());
            System.out.println("Seed = " + seedValue);
        } else if (e.getSource() == x) {
            xValue = Integer.parseInt(x.getText());
        } else if (e.getSource() == y) {
            yValue = Integer.parseInt(y.getText());
        } else if (e.getSource() == cells) {
            cellsValue = Integer.parseInt(cells.getText());
        } else if (e.getSource() == csv) {
            csvValue = csv.getText();
        } else if (e.getSource() == outputCSV) {
            System.out.println("you're supposed to output a CSV here");
        } else if (e.getSource() == generate) {
            dun.randomize();
            scale = (xValue >= yValue) ? 500/xValue : 500/yValue;
//            DungeonViewer dv = new DungeonViewer(dun,scale);
        }
        
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
        dun.setLayout(dun.connectRooms());
        
        dungeonView = new Surface(dun,scale);
        
    }
    
}
