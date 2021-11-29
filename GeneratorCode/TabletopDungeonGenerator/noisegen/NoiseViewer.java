package noisegen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

class Surface extends JPanel {

        NoiseMap map;
        int scale;
        
        public Surface(NoiseMap map, int scal) {
                this.map = map;
                scale = scal;
        }
        
        private void doDrawing(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;
            for(int x = 0; x < map.x; x++) {
                for(int y = 0; y < map.y; y++) {
                    float color = (float) map.map[x][y];
                    g2d.setColor(new Color(color,color,color));
                    g2d.fillRect(x*scale, y*scale, scale, scale);
                }
            }
        }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
}

public class NoiseViewer extends JFrame {

        public NoiseViewer(NoiseMap map, int scale) {
            initUI(map, scale);
        }

        private void initUI(NoiseMap map, int scale) {

            add(new Surface(map, scale));

            setTitle("NoiseViewer");
            setSize(map.x*scale+15,map.y*scale+40);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
}