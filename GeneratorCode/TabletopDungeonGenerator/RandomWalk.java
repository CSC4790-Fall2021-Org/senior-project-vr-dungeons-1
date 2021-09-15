import java.util.Arrays;
import java.util.Random;

public class RandomWalk {
        
        //use this template file to make your dungeon layout randomizer
        //be sure to try to implement the seed so the randomizer consistently outputs the same thing using any given seed
        public static boolean[][] randomize(boolean[][] in, int seed) {
                //d is the temporary array that you'll use to make the layout, currently initialized as all False values.
                boolean[][] d = new boolean[in.length][in[0].length];
                
                for(int a = 0; a < d.length; a++) {
                    for(int b = 0; b < d[0].length; b++) {
                        d[a][b] = true;
                    }
                }
                
                final int MAX_X = in.length;
                final int MAX_Y = in[0].length;
                
                //randomize the dungeon here
                int x = in.length/2, y = in[0].length/2;
                int steps = 0;
                Random rand = new Random(seed);
                
                while ((x < MAX_X && x >= 0) && (y < MAX_Y && y >= 0)) {
                    d[x][y] = false; System.out.println(d[x][y]);
                    double r = rand.nextDouble();
                    if      (r < 0.25) x--;
                    else if (r < 0.50) x++;
                    else if (r < 0.75) y--;
                    else if (r < 1.00) y++;
                    steps++;
                    System.out.println(steps);
                }
                System.out.println("Total steps = " + steps);
                return d;
        }
        
        public static void main(String args[]) {
                //this is the test method, it prints out the random dungeon with a seed of 1234 at the default size
                Dungeon dun = new Dungeon(1234, 100, 100);
                dun.setLayout(randomize(dun.d, dun.SEED));
//              System.out.println(Arrays.toString(dun.d));
                DungeonViewer dv = new DungeonViewer(dun,5);
                dv.setVisible(true);
//                for(int a = 0; a < dun.d.length; a++) {
//                    for(int b = 0; b < dun.d[0].length; b++) {
//                        System.out.print(dun.d[a][b]+ " ");
//                    }
//                    System.out.print("\n");
//                }
        }
        
}
