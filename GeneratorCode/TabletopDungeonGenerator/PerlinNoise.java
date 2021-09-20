//https://stackoverflow.com/questions/18279456/any-simplex-noise-tutorials-or-resources

import java.util.Arrays;
import noise.SimplexNoise_octave;

public class PerlinNoise {
	
	//use this template file to make your dungeon layout randomizer
	//be sure to try to implement the seed so the randomizer consistently outputs the same thing using any given seed
	public static boolean[][] randomize(boolean[][] in, int seed) {
		//d is the temporary array that you'll use to make the layout, currently initialized as all False values.
		boolean[][] d = new boolean[in.length][in[0].length];
		
		//randomize the dungeon here
		SimplexNoise_octave sn = new SimplexNoise_octave(seed);
		
		for(int x = 0; x < d.length; x++) {
			for(int y = 0; y < d.length; y++) {
				d[x][y] = (sn.noise((double)x, (double)y) >= 0) ? true : false;
			}
		}
		
		return d;
	}
	
	public static void main(String args[]) {
		//this is the test method, it prints out the random dungeon with a seed of 1234 at the default size
		Dungeon dun = new Dungeon(1234, 100, 100);
		dun.setLayout(randomize(dun.d, dun.SEED));
		DungeonViewer dv = new DungeonViewer(dun,5);
		dv.setVisible(true);
	}
	
}
