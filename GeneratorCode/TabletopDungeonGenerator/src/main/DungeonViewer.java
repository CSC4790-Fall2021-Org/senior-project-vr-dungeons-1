//https://zetcode.com/gfx/java2d/introduction/

package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

class Surface extends JPanel {

	Dungeon dun;
	int scale;
	
	public Surface(Dungeon d, int scal) {
		dun = d;
		scale = scal;
	}
	
    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
//        g2d.drawString("Java 2D", 50, 50);
        for(int x = 0; x < dun.X; x++) {
        	for(int y = 0; y < dun.Y; y++) {
        		if(dun.d[x][y]) { g2d.setColor(Color.BLACK);}
        		else { g2d.setColor(Color.WHITE);
        		g2d.fillRect(x*scale, y*scale, scale, scale);
        		}
        	}
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }
}

public class DungeonViewer extends JFrame {

    public DungeonViewer(Dungeon d, int scale) {

        initUI(d, scale);
    }

    private void initUI(Dungeon d, int scale) {

        add(new Surface(d, scale));

        setTitle("DungeonViewer");
        setSize(d.X*scale+15,d.Y*scale+40);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

//    public static void main(String[] args) {
//
//        EventQueue.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
//            	Dungeon dun = new Dungeon(123);
//                DungeonViewer ex = new DungeonViewer(dun);
//                ex.setVisible(true);
//            }
//        });
//    }
}