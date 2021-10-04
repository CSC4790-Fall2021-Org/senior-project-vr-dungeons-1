import java.io.IOException;
import java.util.Random;
import java.util.Stack;

public class BetterRandomWalk extends Dungeon {
    
    static int firstX, firstY;
    
    public BetterRandomWalk(int seed) {
        super(seed);
    }

    public BetterRandomWalk(int seed, int x, int y) {
        super(seed,x,y);
    }

    public BetterRandomWalk(int seed, boolean[][] dungeon) {
        super(seed, dungeon);
    }

    
        //use this template file to make your dungeon layout randomizer
        //be sure to try to implement the seed so the randomizer consistently outputs the same thing using any given seed
        public static boolean[][] randomize(boolean[][] in, int seed) {
                //d is the temporary array that you'll use to make the layout, currently initialized as all False values.
                boolean[][] d = new boolean[in.length][in[0].length];
                
                //randomize the dungeon here
                for(int a = 0; a < d.length; a++) {
                    for(int b = 0; b < d[0].length; b++) {
                        d[a][b] = true;
                    }
                }
                
                final int MAX_X = d.length-1;
                final int MAX_Y = d[0].length-1;
                int leftmost = 0, rightmost = 0, topmost = 0, botmost = 0;
                //boolean leftWall, rightWall, topWall, botWall;
                int x = 0, y = 0;
                Random rand = new Random(seed);
                Stack<Integer> path_x = new Stack<Integer>();
                Stack<Integer> path_y = new Stack<Integer>();
                int modX, modY;
                
                while(rightmost-leftmost < MAX_X && topmost - botmost < MAX_Y) {
                    path_x.add(x); path_y.add(y);
                    double r = rand.nextDouble();
                    if      (r < 0.25) x--;
                    else if (r < 0.50) x++;
                    else if (r < 0.75) y--;
                    else if (r < 1.00) y++;
                    leftmost = (x < leftmost) ? x : leftmost;
                    rightmost = (x > rightmost) ? x : rightmost;
                    topmost = (y > topmost) ? y : topmost;
                    botmost = (y < botmost) ? y : botmost;
                }
                                                
                //questionable
                modX = -1*leftmost;
                modY = -1*botmost;
                
                firstX = path_x.pop()+modX;
                firstY = path_y.pop()+modY;
                int currX = 0;
                int currY = 0;
                
                d[firstX][firstY] = false;
                
                while(!path_x.empty() && !path_y.empty()) {
                    currX = path_x.pop()+modX;
                    currY = path_y.pop()+modY;
                    d[currX][currY] = false;
                }
                
//                lastX = currX;
//                lastY = currY;
                
                return d;
        }
        
        public static void main(String args[]) {
                //this is the test method, it prints out the random dungeon with a seed of 1234 at the default size
                Dungeon dun = new Dungeon(42069,100,100);
                dun.firstX = firstX;
                dun.firstY = firstY;
                dun.setLayout(randomize(dun.d, dun.SEED));
                DungeonViewer dv = new DungeonViewer(dun,5);
                dv.setVisible(true);
                try {
                    dun.outputCSV("GeneratorCode\\output2.csv");
                    System.out.println("somehow, it worked");
                }
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("didn't work");
                }
        }
        
}
