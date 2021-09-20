import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.nio.file.Path;
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
	
	//returns the dungeon with only the outlines of open space (open spaces with walls connected)
        public static boolean[][] getWireframe (boolean[][] in) {
            
            boolean [][] ret = new boolean[in.length][in[0].length];
            
            for(int a = 0; a < ret.length; a++) {
                for(int b = 0; b < ret[0].length; b++) {
                    ret[a][b] = true;
                }
            }
            
            for(int c = 0; c < in.length; c++) {
                for(int r = 0; r < in[0].length; r++) {
                    /*this acts as an explanation to future me for what the heck the next statement is
                    
                    //if c,r is an open space
                    if(in[c][r]) {return true}
                    else if(!in[c][r])
                        //and the left space is empty
                        if(c != 0 && in[c-1][r])
                        //and the right space is empty
                        if(c != in.length-1 && in[c+1][r]) 
                        //and the top space is empty
                        if(r != 0 && in[c][r-1])
                        //and the bottom space is empty
                        if(r != in[0].length-1 && in[c][r+1])
                        {return true} */
                    ret[c][r] = in[c][r] || (!in[c][r] && ((c!=0 && !in[c-1][r]) && (c != in.length-1 && !in[c+1][r]) && (r != 0 && !in[c][r-1]) && (r != in[0].length-1 && !in[c][r+1])));
                }
            }
            return ret;
        }
        
        //individually numbers each "room" (a room is a group of connected open spaces)
        public int[][] numberRooms(boolean[][] in) {
            int[][] ret = new int[in.length][in[0].length];
            
            int unchecked = 0;
            
            for(int c = 0; c < ret.length; c++) {
                for(int r = 0; r < ret[0].length; r++) {
                    //if it's a wall, set the number to 0
                    if(in[c][r]){ret[c][r]=0;}
                    //if its an open space, set temporarily set it to -1
                    else{ret[c][r]=-1;unchecked++;}
                }
            }
            
            int roomNum = 0;
            
            while(unchecked > 0) {
                roomNum++;
                int tempC = 0; int tempR = 0;
                //find the next unchecked tile
                while(ret[tempC][tempR] == -1 || ret[tempC][tempR] == 0) {
                    //scan left-right and then down
                    if(tempC == ret.length-1) {tempC=0;tempR++;}
                    else{tempC++;}
                }
                //using new unchecked tile, find every tile connected to it
            }
            
            return ret;
        }
        
        public void outputCSV(String path) throws IOException {
            
            File file = new File(path);
            if(!file.exists()) {
                file.createNewFile();
            }
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            StringBuilder sb = new StringBuilder();
            sb.append(X);
            sb.append(",");
            sb.append(Y);
            sb.append(",");
            for(boolean[] c : d) {
                for(boolean r : c) {
                    sb.append(r);
                    sb.append(",");
                }
            }
            bw.write(sb.toString());
            bw.close();
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
