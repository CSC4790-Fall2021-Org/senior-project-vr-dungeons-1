package main;

import java.util.Arrays;

public class Dungeon implements Cloneable {

	private final int SIDELENGTH = 500; //this is currently the default side length of the dungeon, can be changed at pretty much any time
	public final int SEED; //SEED is the seed to be used in random generation
	public final int X; //X is the horizontal side length of the map
	public final int Y; //Y is the vertical side length of the map 
	public boolean[][] d; //The dungeon is stored in a 2D array of booleans, where True is a wall and False is open air
	
	//this is the default constructor, it automatically gives the dungeon a default size of 500x500
	public Dungeon(int seed) {
		SEED = seed;
		X = SIDELENGTH;
		Y = SIDELENGTH;
		d = new boolean[X][Y];
	}
	
	public Dungeon(int seed, int x, int y) {
		SEED = seed;
		X = x;
		Y = y;
		d = new boolean[X][Y];
	}
	
	public Dungeon(int seed, boolean[][] b) {
		SEED = seed;
		X = b[0].length;
		Y = b.length;
		d = b;
	}
	
	public void setLayout(boolean[][] in) {
		d = in;
	}
	
	public void changeXY(int x, int y, boolean b) {
		d[x][y] = b;
	}
	
	public int countSpaces() { //returns the number of free spaces in d
		int count = 0;
		for(int x = 0; x < X; x++) {
			for(int y = 0; y < Y; y++) {
				if(!d[x][y]) {count++;}
			}
		}
		return count;
	}
	
//	public int[][] to2DIntArr() { //returns the layout as an array of every index where there is an open space
//		
//		int n = countSpaces();
//		
//		int[][] arr = new int[n][2];
//		int count = 0;
//		for(int x = 0; x < X; x++) {
//			for(int y = 0; y < Y; y++) {
//				if(!d[x][y]) {
//					arr[count] = new int[]{x,y};
//					count++;
//				}
//			}
//		}
//				
//		return arr;
//	}
	
	
//	@Override
//	public String toString() {
//		return Arrays.toString(d);
//	}
	
	@Override
    protected Object clone()
        throws CloneNotSupportedException
    {
        return super.clone();
    }
	
}
