import java.util.Random;
import java.util.Stack;

public class BetterRandomWalk {
        
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
                int currX = 0, currY = 0;
                int leftmost = 0, rightmost = 0, topmost = 0, botmost = 0;
//                boolean leftWall, rightWall, topWall, botWall;
                boolean horiz, vert;
                int x = 0, y = 0;
                Random rand = new Random(seed);
                int steps = 0;
                Stack<Integer> path_x = new Stack<Integer>();
                Stack<Integer> path_y = new Stack<Integer>();
                int modX, modY;
                
                while(rightmost-leftmost <= MAX_X && topmost - botmost <= MAX_Y) {
                    path_x.add(x); path_y.add(y);
                    double r = rand.nextDouble();
                    if      (r < 0.25) x--;
                    else if (r < 0.50) x++;
                    else if (r < 0.75) y--;
                    else if (r < 1.00) y++;
                    currX = x; currY = y;
                    steps++;
                    leftmost = (x < leftmost) ? x : leftmost;
                    rightmost = (x > rightmost) ? x : rightmost;
                    topmost = (y > topmost) ? y : topmost;
                    botmost = (y < botmost) ? y : botmost;
                    System.out.println(steps);
                }
                
                horiz = rightmost-leftmost > MAX_X;
                vert = topmost-botmost > MAX_Y;
                
//                if(horiz) { System.out.println("HORIZ: " + (rightmost-leftmost) + "\nR = " + rightmost + "\tL = " + leftmost);
//                            System.out.println("currX = " + currX + "\tcurrY = " + currY);}
//                else if(vert) { System.out.println("VERT: " + (topmost-botmost) + "\nT = " + topmost + "\tB = " + botmost);
//                            System.out.println("currX = " + currX + "\tcurrY = " + currY);}
//                else { System.out.println("SOMETHING IS VERY WRONG HERE"); }
                
                //questionable
                modX = -1*leftmost;
                modY = -1*botmost;
                
                
                while(!path_x.empty() && !path_y.empty()) {
                    d[path_x.pop()+modX][path_y.pop()+modY] = false;
                }
                
                return d;
        }
        
        public static void main(String args[]) {
                //this is the test method, it prints out the random dungeon with a seed of 1234 at the default size
                Dungeon dun = new Dungeon(1234);
                dun.setLayout(randomize(dun.d, dun.SEED));
//              System.out.println(Arrays.toString(dun.d));
                DungeonViewer dv = new DungeonViewer(dun,1);
                dv.setVisible(true);
        }
        
}
