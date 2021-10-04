import java.io.IOException;
import java.util.Random;
import java.util.Stack;

public class BetterRandomWalk extends Dungeon {
            
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
    public boolean[][] randomize(int seed) {
            //d is the temporary array that you'll use to make the layout, currently initialized as all False values.
            boolean[][] dun = new boolean[d.length][d[0].length];
            
            //randomize the dungeon here
            for(int a = 0; a < dun.length; a++) {
                for(int b = 0; b < dun[0].length; b++) {
                    dun[a][b] = true;
                }
            }
            
            final int MAX_X = dun.length-1;
            final int MAX_Y = dun[0].length-1;
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
            dun[firstX][firstY] = false;
            
            while(!path_x.empty() && !path_y.empty()) {
                dun[path_x.pop()+modX][path_y.pop()+modY] = false;
            }
            
            return dun;
    }
        
//    public static void main(String args[]) {
            //this is the test method, it prints out the random dungeon with a seed of 1234 at the default size
//            Dungeon dun = new Dungeon(123,100,100);
//            dun.setLayout(randomize(dun.d, dun.SEED));
//            int[][] n = dun.numberRooms(dun.d);
//            DungeonViewer dv = new DungeonViewer(dun,10,n);
//            dv.setVisible(true);
//            try {
//                dun.outputCSV("GeneratorCode\\output.csv");
//                System.out.println("somehow, it worked");
//            }
//            catch (IOException e) {
                // TODO Auto-generated catch block
//                e.printStackTrace();
//                System.out.println("didn't work");
//            }
//        }
        
}
