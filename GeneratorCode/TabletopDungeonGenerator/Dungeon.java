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
	
	public boolean[][] randomize() {
            //abstract method
            return d;
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
        
        //returns an ArrayList<ArrayList<Integer[]>> that lists the coordinates of every corner point.
        //ret[roomNumber][cornerNumber] = {x,y}
        public ArrayList<ArrayList<Integer[]>> getCornersList() {
            
            ArrayList<ArrayList<Integer[]>> ret = new ArrayList<ArrayList<Integer[]>>();
            
            int[][] nums = numberRoomsMap();
            boolean[][] wire = getWireframe();
            
            for(int a = 0; a <= numberOfRooms; a++) {
                ret.add(new ArrayList<Integer[]>());
            }
            
//            System.out.println("ret = " + ret.toString());
        
            for(int c = 0; c < nums.length; c++) {
                for(int r = 0; r < nums[0].length; r++) {
                    if(wire[c][r]) { nums[c][r] = 0; }
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
                            if(nums[c][r]!=0) {ret.get(nums[c][r]).add(new Integer[] {c,r});}
                        }
                    }
                }
            }
//            System.out.println("Done with getCornersList");
            return ret;
        }
        
        private int getManhattanDistance(int x1, int x2, int y1, int y2) {
            return Math.abs(x1 - x2) + Math.abs(y1 - y2); //Manhattan Distance looks cool
        }
        
//        private double getEuclidianDistance(int x1, int x2, int y1, int y2) {
//            return Math.sqrt(((x1-x2)*(x1-x2))+((y1-y2)*(y1-y2)));
//        }
        
        public boolean[][] connectRooms() {
            
            numberRoomsMap();
            
            int startNumRooms = numberOfRooms;
            boolean[][] temp = d;
            
            for(int a = 0; a < startNumRooms; a++) {
            
                int dist;
    //            double dist;
                
                ArrayList<ArrayList<Integer[]>> cornerList = getCornersList();
                cornerList.remove(0);
                
                //for every room in the dungeon
//                for(ArrayList<Integer[]> room : cornerList) {
                ArrayList<Integer[]> room = cornerList.get(0);
                int shortest = Integer.MAX_VALUE;
//                double shortest = Double.MAX_VALUE;
                int startX = -1;
                int startY = -1;
                int endX = -1;
                int endY = -1;
                
                
                //can't tell if this is O(n^2) or O(n^3) but whatever it is I don't think it's very optimal LOL
                
                //for every point in the current room
                for(Integer[] point : room) {
                    int count = 1;
                    //if the room has corners (which it should, unless it's the 0 room)
                    if(point.length>0) {
                        //for every room with room number > current room number
                        for(int i = count; i < numberOfRooms; i++) {
                            //for every point in the second room
                            for(Integer[] point2 : cornerList.get(i)) {
                                //check to see if the distance between the current point and the checking point is shorter than the current shortest distance
                                dist = getManhattanDistance(point[0],point2[0],point[1],point2[1]);
//                                dist = getEuclidianDistance(point[0],point2[0],point[1],point2[1]);
                                //if yes, record shortest distance and first and last points to connect
                                if(dist < shortest) {
                                    shortest = dist;
                                    startX = point[0];
                                    startY = point[1];
                                    endX = point2[0];
                                    endY = point2[1];
                                }
                            }
                        }
                    }
                    count++;
                }
                //connect the two points
                int xDist = Math.abs(endX-startX);
                int yDist = Math.abs(endY-startY);
                //RASTERIZATION (I found this algorithm on Wikipedia lmao)
                //https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm#Line_equation
                if(startX!=-1 && endX!=-1 && startY!=-1 && endY!=-1) {
                    if(yDist<xDist) {
                        if(startX>endX) {
                            temp = plotLineLow(temp,endX,startX,endY,startY);
                        } else {
                            temp = plotLineLow(temp,startX,endX,startY,endY);
                        }
                    } else {
                        if(startY>endY) {
                            temp = plotLineHigh(temp,endX,startX,endY,startY);
                        } else {
                            temp = plotLineHigh(temp,startX,endX,startY,endY);
                        }
                    }
                }
            }
            
            return temp;
        }
        
        private boolean[][] plotLineLow(boolean[][] in, int x0, int x1, int y0, int y1) {
            
            boolean[][] ret = in;
            int dx = x1-x0;
            int dy = y1-y0;
            int yi = 1;
            if(dy<0) {
                yi = -1;
                dy = -dy;
            } 
            int D = (2*dy) - dx;
            int y = y0;
            
            for(int x = x0; x <= x1; x++) {
                ret[x][y] = false;
                if(D>0) {
                    y += yi;
                    ret[x][y] = false;
                    D += (2*(dy-dx));
                } else {
                    D += 2*dy; 
                }
            }
            
            return ret;
            
        }
        
        private boolean[][] plotLineHigh(boolean[][] in, int x0, int x1, int y0, int y1) {
            
            boolean[][] ret = in;
            int dx = x1-x0;
            int dy = y1-y0;
            int xi = 1;
            
            if(dx<0) {
                xi = -1;
                dx = -dx;
            }
            int D = (2*dx) - dy;
            int x = x0;
            
            for(int y = y0; y <= y1; y++) {
                ret[x][y] = false;
                if(D>0) {
                    x += xi;
                    ret[x][y] = false;
                    D += (2*(dx-dy));
                } else {
                    D += 2*dx;
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
