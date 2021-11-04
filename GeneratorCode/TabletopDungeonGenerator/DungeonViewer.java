//https://zetcode.com/gfx/java2d/introduction/


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

class Surface extends JPanel {

	/**
	 * 
	 */
//	private static final long serialVersionUID = 5990773779976090772L;
	Dungeon dun;
	int scale;
	int[][] nums = null;
	
	public Surface(Dungeon d, int scal) {
		dun = d;
		scale = scal;
	}
	
	public Surface(Dungeon d, int scal, int[][] num) {
		dun = d;
		scale = scal;
		nums = num;
	}
	
    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
//        g2d.drawString("Java 2D", 50, 50);
        for(int x = 0; x < dun.X; x++) {
        	for(int y = 0; y < dun.Y; y++) {
        		if(dun.d[x][y]) {
        		    g2d.setColor(Color.LIGHT_GRAY);
        		    g2d.fillRect(x*scale, y*scale, scale, scale);
        		}
        		else { 
        			g2d.setColor(Color.WHITE);
        			g2d.fillRect(x*scale, y*scale, scale, scale);
        			if(nums!=null) {
        				g2d.setColor(Color.MAGENTA);
        				g2d.drawString(Integer.toString(nums[x][y]), x*scale, (y*scale)+scale);
        			}
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

@SuppressWarnings("serial")
public class DungeonViewer extends JFrame {

	public DungeonViewer(Dungeon d, int scale) {

        initUI(d, scale);
    }
	
	public DungeonViewer(Dungeon d, int scale, int[][] nums) {
		initUI(d, scale, nums);
	}

    private void initUI(Dungeon d, int scale) {

        add(new Surface(d, scale));

        setTitle("DungeonViewer");
        setSize(d.X*scale+15,d.Y*scale+40);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void initUI(Dungeon d, int scale, int[][] nums) {

        add(new Surface(d, scale, nums));

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