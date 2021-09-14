package main;

import java.util.Arrays;

public class _template {
	
	//use this template file to make your dungeon layout randomizer
	//be sure to try to implement the seed so the randomizer consistently outputs the same thing using any given seed
	public static boolean[][] randomize(boolean[][] in, int seed) {
		//d is the temporary array that you'll use to make the layout, currently initialized as all False values.
		boolean[][] d = new boolean[in.length][in[0].length];
		
		//randomize the dungeon here
		
		return d;
	}
	
	public static void main(String args[]) {
		//this is the test method, it prints out the random dungeon with a seed of 1234 at the default size
		Dungeon dun = new Dungeon(1234);
		dun.setLayout(randomize(dun.d, dun.SEED));
//		System.out.println(Arrays.toString(dun.d));
		DungeonViewer dv = new DungeonViewer(dun,10);
		dv.setVisible(true);
	}
	
}
