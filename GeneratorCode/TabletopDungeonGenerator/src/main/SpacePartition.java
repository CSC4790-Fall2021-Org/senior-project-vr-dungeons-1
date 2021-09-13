package main;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import space.*;

public class SpacePartition {
	
	//use this template file to make your dungeon layout randomizer
	//be sure to try to implement the seed so the randomizer consistently outputs the same thing using any given seed
	public static boolean[][] randomize(boolean[][] in, int seed) {
		//d is the temporary array that you'll use to make the layout, currently initialized as all False values.
		boolean[][] d = new boolean[in.length][in[0].length];
		
		//randomize the dungeon here
		
		int MAX_LEAF_SIZE = 20;
		
		ArrayList<Leaf> leafs = new ArrayList<>();

		//Leaf l; // helper Leaf

		// first, create a Leaf to be the 'root' of all Leafs.
		Leaf root = new Leaf(0, 0, in.length, in[0].length);

		boolean did_split = true;
		// we loop through every Leaf in our Vector over and over again, until no more Leafs can be split.
		while (did_split)
		{
			did_split = false;
			for (Leaf l : leafs)
			{
				if (l.leftChild == null && l.rightChild == null) // if this Leaf is not already split...
				{
					// if this Leaf is too big, or 75% chance...
					if (l.width > MAX_LEAF_SIZE || l.height > MAX_LEAF_SIZE || Math.random() > 0.25)
					{
						if (l.split()) // split the Leaf!
						{
							// if we did split, push the child Leafs to the Vector so we can loop into them next
							leafs.add(l.leftChild);
							leafs.add(l.rightChild);
							did_split = true;
						}
					}
				}
			}
		}

		// next, iterate through each Leaf and create a room in each one.
		root.createRooms();
		
		for (Leaf t : leafs) {
			if (t.room != null)
				d = changeMap(t.room.x, t.room.y, t.room.width, t.room.height, d);
			if (t.halls != null) {
				for (Rectangle r : t.halls) {
					d = changeMap(r.x, r.y, r.width, r.height, d);
				}
			}
		}
		
		return d;
	}
	
	public static boolean[][] changeMap(int x, int y, int w, int h, boolean[][] map) {
		
		/*if (w < 0) {
			if (h < 0) {
				for (int r = x; r < x + w; r--) {
					for (int c = y; c < y + h; c--) {
						map[r][c] = true;
					}
				}
			}
			else if (h > 0) {
				for (int r = x; r < x + w; r--) {
					for (int c = y; c < y + h; c++) {
						map[r][c] = true;
					}
				}
			}
		}
		else if (h < 0) {
			if (w > 0) {
				for (int r = x; r < x + w; r++) {
					for (int c = y; c < y + h; c--) {
						map[r][c] = true;
					}
				}
			}
		}
		
		else {*/
			for (int r = x; r < x + w; r++) {
				for (int c = y; c < y + h; c++) {
					map[r][c] = true;
				}
			}
		//}
		
		return map;
	}
	
	public static void main(String args[]) {
		//this is the test method, it prints out the random dungeon with a seed of 1234 at the default size
		Dungeon dun = new Dungeon(1234);
		dun.setLayout(randomize(dun.d, dun.SEED));
		//for (boolean[] b : dun.d)
			//System.out.println(Arrays.toString(b));
		DungeonViewer dv = new DungeonViewer(dun,10);
		dv.setVisible(true);
	}
	
}
