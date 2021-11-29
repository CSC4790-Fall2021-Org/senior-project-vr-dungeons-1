package noisegen;

import java.util.Random;

public class NoiseMap {
    
    final int x, y;
    final int vecX, vecY;
    final int SEED;
    final int pp;
    final double density;
    public double[][] map;
    private double[][][] vectorMap;
    private Random rand;
    
    public NoiseMap(int x, int y, int seed, int pixelsPer) {
        this.x = x;
        this.y = y;
        this.SEED = seed;
        this.pp = pixelsPer;
        this.density = 1.0/pixelsPer;
        this.map = new double[x][y];
        this.vecX = (int)Math.ceil(density*x)+3;
        this.vecY = (int)Math.ceil(density*y)+3;
        this.vectorMap = new double[vecX][vecY][2];
        rand = new Random(seed);
        generate();
    }
    
    private void generate() {
        for(int x1 = 0; x1 < vecX; x1++) {
            for(int y1 = 0; y1 < vecY; y1++) {
                double angle = rand.nextDouble()*2*Math.PI;
                vectorMap[x1][y1][0] = Math.cos(angle);
                vectorMap[x1][y1][1] = Math.sin(angle);
            }
        }
        
        for(int x2 = 0; x2 < x; x2++) {
            for(int y2 = 0; y2 < y; y2++) {
                //corners
                //a b
                //c d
                double[] dispA = {(x2/pp)-(x2+0.5),(y2/pp)-(y2+0.5)};
                double[] dispB = {((x2/pp)+1)-(x2+0.5),(y2/pp)-(y2+0.5)};
                double[] dispC = {(x2/pp)-(x2+0.5),((y2/pp)+1)-(y2+0.5)};
                double[] dispD = {((x2/pp)+1)-(x2+0.5),((y2/pp)+1)-(y2+0.5)};
                //dot product of vectorMap[x2/pp][y2/pp] and dispA...
                double a = ((vectorMap[x2/pp][y2/pp][0]*dispA[0])+(vectorMap[x2/pp][y2/pp][1]*dispA[1]));
                double b = ((vectorMap[(x2/pp)+1][y2/pp][0]*dispB[0])+(vectorMap[(x2/pp)+1][y2/pp][1]*dispB[1]));
                double c = ((vectorMap[x2/pp][(y2/pp)+1][0]*dispC[0])+(vectorMap[x2/pp][(y2/pp)+1][1]*dispC[1]));
                double d = ((vectorMap[(x2/pp)+1][(y2/pp)+1][0]*dispD[0])+(vectorMap[(x2/pp)+1][(y2/pp)+1][1]*dispD[1]));
                
//                double value = smoothstep(smoothstep(a,b,(x2/pp)/(double)pp),smoothstep(c,d,(x2/pp)/(double)pp),(y2/pp)/(double)pp);
                double value = sigmoid(bilinearInterp(a,b,c,d,x2/pp,y2/pp));
                
                map[x2][y2] = value;
            }
        }
    }
    
    //https://en.wikipedia.org/wiki/Smoothstep
    //smoothstep interpolation algorithm
    private double clamp(double x, double lowerLim, double upperLim) {
        if(x < lowerLim) { return lowerLim; }
        if(x > upperLim) { return upperLim; }
        else { return x; }
    }
    
    private double smoothstep(double edge1, double edge2, double x) {
        x = sigmoid((x-edge1) / (edge2-edge1));
        
        return x * x * (3-2*x);
    }
    
    private double sigmoid(double x) {
        return 1.0/(1.0+Math.exp(-x));
    }
    
    private double bilinearInterp(double a, double b, double c, double d, double x, double y) {
        return(interp(interp(a,b,x),interp(c,d,x),y));
    }
    
    private double interp(double h, double j, double k) {
        return h + k * (h - j);
    }
    
}
