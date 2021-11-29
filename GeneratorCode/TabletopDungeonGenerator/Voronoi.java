import java.util.Random;

public class Voronoi extends Dungeon {
	
        int cells;
        final int RATIO = 50;
    
	public Voronoi(int seed) {
		super(seed);
		cells = (X*Y/RATIO);
	}
	
	public Voronoi(int seed, int x, int y) {
		super(seed,x,y);
		cells = (X*Y/RATIO);
	}
	
	public Voronoi(int seed, boolean[][] dungeon) {
		super(seed, dungeon);
		cells = (X*Y/RATIO);
	}
	
	public Voronoi(int seed, int x, int y, int cell) {
            super(seed,x,y);
            cells = cell;
        }

	public Voronoi(int seed, int cell) {
	    super(seed);
	    cells = cell;
	}
	
	public Voronoi(int seed, boolean[][] dungeon, int cell) {
            super(seed,dungeon);
            cells = cell;
        }
	
	static double distance(int x1, int x2, int y1, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2); //Manhattan Distance looks cool
	}
	
	//use this template file to make your dungeon layout randomizer
	//be sure to try to implement the seed so the randomizer consistently outputs the same thing using any given seed
	public void randomize() {
	    randomize(cells);
	}
	
	public void randomize(int cell) {
		//d is the temporary array that you'll use to make the layout, currently initialized as all False values.
		boolean[][] dun = new boolean[X][Y];
		
		//randomize the dungeon here
		//chooses random points to be nodes
		int n = 0;
		Random rand = new Random(SEED);
		int[] px = new int[cell];
		int[] py = new int[cell];
		for (int i = 0; i < cell; i++) {
			px[i] = rand.nextInt(X);
			py[i] = rand.nextInt(Y);
 
		}
		
		//for each point x,y in the dungeon, it finds the nearest node. If the node number is n%3==2, it becomes open space, if not it becomes a wall.
		for (int c = 0; c < X; c++) {
			for (int r = 0; r < Y; r++) {
				n = 0;
				for (int i = 0; i < cell; i++) {
					if (distance(px[i], c, py[i], r) < distance(px[n], c, py[n], r)) {
						n = i; //find the closest point to x,y
					}
				}
				if(n%3==0||n%3==1) {
					dun[c][r]= true;
				}
			}
		}
		
		int x,y;
                Random r = new Random(SEED);
                do {
                    x = r.nextInt(d.length);
                    y = r.nextInt(d[0].length);
                } while(dun[x][y]);
                
                firstX = x;
                firstY = y;
		
		setLayout(dun);
	}
}
