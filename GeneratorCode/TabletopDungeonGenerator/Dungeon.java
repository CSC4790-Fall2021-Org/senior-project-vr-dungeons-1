import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Dungeon implements Cloneable {

	private final int SIDELENGTH = 500; //this is currently the default side length of the dungeon, can be changed at pretty much any time
	public final int SEED; //SEED is the seed to be used in random generation
	public final int X; //X is the horizontal side length of the map
	public final int Y; //Y is the vertical side length of the map 
	public boolean[][] d; //The dungeon is stored in a 2D array of booleans, where True is a wall and False is open air
	public int firstX;
	public int firstY;
	public int numberOfRooms;
	
	//this is the default constructor, it automatically gives the dungeon a default size of 500x500
	public Dungeon(int seed) {
		SEED = seed;
		X = SIDELENGTH;
		Y = SIDELENGTH;
		d = new boolean[X][Y];
	}
	
	public boolean[][] randomize(int seed) {
		//temporary method
		return d;
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
        public boolean[][] getWireframe () {
            
            boolean [][] ret = new boolean[d.length][d[0].length];
            
            for(int a = 0; a < ret.length; a++) {
                for(int b = 0; b < ret[0].length; b++) {
                    ret[a][b] = true;
                }
            }
            
            for(int c = 0; c < d.length; c++) {
                for(int r = 0; r < d[0].length; r++) {
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
                    ret[c][r] = d[c][r] || (!d[c][r] && ((c!=0 && !d[c-1][r]) && (c != d.length-1 && !d[c+1][r]) && (r != 0 && !d[c][r-1]) && (r != d[0].length-1 && !d[c][r+1])));
                }
            }
            return ret;
        }
        
        //individually numbers each "room" (a room is a group of connected open spaces)
        public int[][] numberRoomsMap() {
            int[][] ret = new int[d.length][d[0].length];
            
            for(int c = 0; c < ret.length; c++) {
                for(int r = 0; r < ret[0].length; r++) {
                    //if it's a wall, set the number to 0
                    if(d[c][r]){ret[c][r]=0;}
                    //if its an open space, set temporarily set it to -1
                    else{ret[c][r]=-1;}
                }
            }
            
            int roomNum = 0;
            Stack<Integer[]> unchecked = new Stack<Integer[]>();
            Stack<Integer[]> checked = new Stack<Integer[]>();
            boolean done = false;
            
            while(!done) {
                roomNum++;
                int tempC = 0; int tempR = 0;
                //find the next unchecked tile
                while(tempR != ret[0].length-1) {
                    //scan left-right and then down
                    if(tempC == ret.length-1) {tempC=0;tempR++;}
                    else{tempC++;}
                    if(!checked.contains(new Integer[] {tempC,tempR}) && ret[tempC][tempR] == -1) {break;}
                    if(tempC == ret.length-2 && tempR == ret[0].length-2 && ret[tempC][tempR] != -1) {done = true; break;}
                }
                //using new unchecked tile, find every tile connected to it
                unchecked.add(new Integer[]{tempC,tempR});
                int currX = -1; int currY = -1;
                
                while(!unchecked.isEmpty()) {
                    Integer[] temp = unchecked.pop();
                    currX = temp[0];
                    currY = temp[1];
                    ret[currX][currY] = roomNum;
                    checked.add(new Integer[] {tempC,tempR});
                    //if left space is empty
                    if(currX != 0 && ret[currX-1][currY] == -1) {
                    	unchecked.add(new Integer[]{currX-1,currY});
                    }
                    //if right space is empty
                    if(currX != d.length-1 && ret[currX+1][currY] == -1) {
                    	unchecked.add(new Integer[]{currX+1,currY});
                    }
                    //if top space is empty
                    if(currY != 0 && ret[currX][currY-1] == -1) {
                    	unchecked.add(new Integer[]{currX,currY-1});
                    }
                    //if bottom space is empty
                    if(currY != d[0].length-1 && ret[currX][currY+1] == -1) {
                    	unchecked.add(new Integer[]{currX,currY+1});
                    }
                }
            }
            numberOfRooms = roomNum;
            return ret;
        }
        
        public int[][] getCornersMap() {
            
        	int[][] ret = new int[d.length][d[0].length];
            int[][] nums = numberRoomsMap();
            boolean[][] wire = getWireframe();
            
            for(int c = 0; c < nums.length; c++) {
                for(int r = 0; r < nums[0].length; r++) {
                    if(wire[c][r]) { ret[c][r] = 0; }
                    /*
                     * 
                     * if(c!=0 && c!=ret.length-1 && r!=0 && r!= ret[0].length-1
                     * && !(ret[c+1][r]==0 && ret[c][r+1]==0)
                     * && !(ret[c][r+1]==0 && ret[c-1][r]==0)
                     * && !(ret[c-1][r]==0 && ret[c][r-1]==0)
                     * && !(ret[c][r-1]==0 && ret[c+1][r]==0)
                     * )
                     */
                    else if((c!=0 && c!=nums.length-1 && r!=0 && r!= nums[0].length-1)) {
                    	if(!((nums[c+1][r]==0 && nums[c][r+1]==0) || (nums[c][r+1]==0 && nums[c-1][r]==0) || (nums[c-1][r]==0 && nums[c][r-1]==0) || (nums[c][r-1]==0 && nums[c+1][r]==0))) {
                    		ret[c][r]=0;
                    	} else {
                    		ret[c][r]=nums[c][r];
                    	}
                    }
                }
            }
            
            return ret;
        }
        
        public ArrayList<ArrayList<Integer[]>> getCornersList() {
            
            ArrayList<ArrayList<Integer[]>> ret = new ArrayList<ArrayList<Integer[]>>(numberOfRooms);
            
            for(ArrayList al : ret) {
                al.add(new ArrayList<Integer[]>());
            }
            
            int[][] nums = numberRoomsMap();
            boolean[][] wire = getWireframe();
        
            for(int c = 0; c < nums.length; c++) {
                for(int r = 0; r < nums[0].length; r++) {
                    /*
                     * 
                     * if(c!=0 && c!=ret.length-1 && r!=0 && r!= ret[0].length-1
                     * && !(ret[c+1][r]==0 && ret[c][r+1]==0)
                     * && !(ret[c][r+1]==0 && ret[c-1][r]==0)
                     * && !(ret[c-1][r]==0 && ret[c][r-1]==0)
                     * && !(ret[c][r-1]==0 && ret[c+1][r]==0)
                     * )
                     */
                    if((c!=0 && c!=nums.length-1 && r!=0 && r!= nums[0].length-1)) {
                        if(((nums[c+1][r]==0 && nums[c][r+1]==0) || (nums[c][r+1]==0 && nums[c-1][r]==0) || (nums[c-1][r]==0 && nums[c][r-1]==0) || (nums[c][r-1]==0 && nums[c+1][r]==0))) {
                            ret.get(nums[c][r]).add(new Integer[] {c,r});
                        }
                    }
                }
            }
        
            return ret;
        }
        
        
        
        
        public void outputCSV(String path) throws IOException {
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            StringBuilder sb = new StringBuilder();
            sb.append(X);
            sb.append(",");
            sb.append(Y);
            sb.append(",");
            sb.append(Integer.toString(firstX));
            sb.append(",");
            sb.append(Integer.toString(firstY));
            sb.append(",");
            for(boolean[] c : d) {
                for(boolean r : c) {
                    sb.append(r);
                    sb.append(",");
                }
                sb.append("\n");
            }
            bw.write(sb.toString());
            bw.close();
        }
 	
	@Override
    protected Object clone()
        throws CloneNotSupportedException
    {
        return super.clone();
    }
	
}
