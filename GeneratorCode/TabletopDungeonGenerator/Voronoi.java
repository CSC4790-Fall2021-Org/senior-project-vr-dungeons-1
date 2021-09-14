package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Voronoi {
	
	static double distance(int x1, int x2, int y1, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2); //Manhattan Distance looks cool
	}
	
	//use this template file to make your dungeon layout randomizer
	//be sure to try to implement the seed so the randomizer consistently outputs the same thing using any given seed
	public static boolean[][] randomize(boolean[][] in, int seed, int cells, boolean connect) {
		//d is the temporary array that you'll use to make the layout, currently initialized as all False values.
		boolean[][] d = new boolean[in.length][in[0].length];
		
		//randomize the dungeon here
		//chooses random points to be nodes
		int n = 0;
		Random rand = new Random(seed);
		int[] px = new int[cells];
		int[] py = new int[cells];
		for (int i = 0; i < cells; i++) {
			px[i] = rand.nextInt(in[0].length);
			py[i] = rand.nextInt(in.length);
 
		}
		
		//for each point x,y in the dungeon, it finds the nearest node. If the node number is n%3==2, it becomes open space, if not it becomes a wall.
		for (int x = 0; x < in[0].length; x++) {
			for (int y = 0; y < in.length; y++) {
				n = 0;
				for (int i = 0; i < cells; i++) {
					if (distance(px[i], x, py[i], y) < distance(px[n], x, py[n], y)) {
						n = i; //find the closest point to x,y
					}
				}
				if(n%3==0||n%3==1) {
					d[x][y]= true;
				}
			}
		}
		
		//connect rooms with 3-wide hallways
		if(connect) {
			boolean vert = rand.nextBoolean();
			for(int o = 2; o < cells-3; o+=3) {
				int x1 = px[o];
				int x2 = px[o+3];
				int y1 = py[o];
				int y2 = py[o+3];
				if(vert && y1 < y2) {
					for(int i = y1; i <= y2; i++) {
						d[x1][i]=false;
						d[x1-1][i]=false;
						d[x1+1][i]=false;
					}
					for(int j = x1; j <= x2; j++) {
						d[j][y2]=false;
						d[j][y2-1]=false;
						d[j][y2+1]=false;
					}
				} else if(vert && y1 > y2) {
					for(int i = y2; i <= y1; i++) {
						d[x2][i]=false;
						d[x2-1][i]=false;
						d[x2+1][i]=false;
					}
					for(int j = x2; j <= x1; j++) {
						d[j][y1]=false;
						d[j][y1-1]=false;
						d[j][y1+1]=false;
					}
				} else if(!vert && x1 < x2) {
					for(int i = x1; i <= x2; i++) {
						d[i][y1]=false;
						d[i][y1-1]=false;
						d[i][y1+1]=false;
					}
					for(int j = y1; j <= y2; j++) {
						d[x2][j]=false;
						d[x2-1][j]=false;
						d[x2+1][j]=false;
					}
				} else if(!vert && x1 > x2) {
					for(int i = x2; i <= x1; i++) {
						d[i][y2]=false;
						d[i][y2-1]=false;
						d[i][y2+1]=false;
					}
					for(int j = y2; j <= y1; j++) {
						d[x1][j]=false;
						d[x1-1][j]=false;
						d[x1+1][j]=false;
					}
				}
				vert = rand.nextBoolean();
			}
		}
			
		return d;
	}
	
	public static void main(String args[]) {
		//this is the test method, it prints out the random dungeon with a seed of 1234 at the default size
		Dungeon dun = new Dungeon(1234, 500, 500);
		dun.setLayout(randomize(dun.d, dun.SEED, 50, true)); 
		DungeonViewer dv = new DungeonViewer(dun,2);
		dv.setVisible(true);
		
		
	}
	
//	
//	//then we connect the individual rooms with corridors
//	public static Dungeon[] findRooms(Dungeon d) {
//			
//		//find each individual room segment and put it into roomSegments
//		ArrayList<ArrayList<ArrayList<Integer>>> roomSegments = new ArrayList<ArrayList<ArrayList<Integer>>>();
//		
////		boolean done = false;
////		int x1 = 0;
////		int y1 = 0;
////		while(!done) {
////			if(!d.d[x1][y1]) {ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>(); temp.add(new ArrayList<Integer>(Arrays.asList(x1,y1))); roomSegments.add(temp); done = true; /*System.out.println("first appearance is" + x1 + ", " + y1);*/}
////			else if(x1+1 >= d.X) { x1=0; y1++; }
////			else if(y1+1 >= d.Y) {done = true;}
////			else { x1++; }
////		}
//				
//		/*int count = 0;
//		for(int x = 0; x < d.X; x++) { //this is REALLY SLOW but if it works, I guess its fine
//			for(int y = 0; y < d.Y; y++) {
//				if(!d.d[x][y]) {
////					System.out.println("xy = " + x + ", " + y);
////					for(ArrayList<ArrayList<Integer>> al : roomSegments) {
//					boolean contained = false;
//					for(int q = 0; q < roomSegments.size(); q++) {
//						ArrayList<ArrayList<Integer>> al = roomSegments.get(q);
////						System.out.println("al.get(0) = " + (al.get(0)));
////						System.out.println("contains " + Arrays.toString(new int[]{x,y-1}) + "? " + al.contains(new ArrayList<Integer>(Arrays.asList(x,y-1))));
////						System.out.println("equals? " + al.get(0).equals(new ArrayList<Integer>(Arrays.asList(x,y-1))));
//						if(al.contains(new ArrayList<Integer>(Arrays.asList(x-1,y))) || al.contains(new ArrayList<Integer>(Arrays.asList(x,y-1))) || al.contains(new ArrayList<Integer>(Arrays.asList(x,y)))) {
//							al.add(new ArrayList<Integer>(Arrays.asList(x,y)));
//							contained = true;
//						}
//					}
//					if(!contained) {
//						ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
//						temp.add(new ArrayList<Integer>(Arrays.asList(x,y)));
//						roomSegments.add(temp);
//					}
////					System.out.print("\t roomSegments.size() = " + roomSegments.size());
//				}
//			}
//		}
//		*/
//		
//		ArrayList<ArrayList<Integer>> testAL = new ArrayList<ArrayList<Integer>>();
//		int[][] tempIntArr = d.to2DIntArr();
//		for(int[] a : tempIntArr) {
//			ArrayList<Integer> t = new ArrayList<Integer>();
//			for(int n : a) {
//				t.add(n);
//			}
//			testAL.add(t);
//		}
//		ArrayList<ArrayList<Integer>> testAL2 = (ArrayList<ArrayList<Integer>>) testAL.clone();
////		boolean done = false;
//		ArrayList<ArrayList<Integer>> inter = intermediate(testAL);
//		
//		
//		
////		System.out.println("room segments = " + roomSegments.size());
////		for(ArrayList<ArrayList<Integer>> rs : roomSegments) {
////			for(ArrayList<Integer> ia : rs) {
////				System.out.print((ia).toString() + ", ");
////			}
////		}
//		
//		//join the room segments that connect
//		//wow this is like O(n^4)
//		//this is also REALLY SLOW but its 1am and I dont want to think of a more elegant solution
////		for(ArrayList<ArrayList<Integer>> a : roomSegments) { 
//		for(int c = 0; c < roomSegments.size(); c++) {
//			ArrayList<ArrayList<Integer>> a = roomSegments.get(c);
//			for(int z = 0; z < a.size(); z++) {
//				ArrayList<Integer> i = a.get(z);
////			for(ArrayList<Integer> i : a) {
//				for(int u = 0; u < roomSegments.size(); u++) {
////				for(ArrayList<ArrayList<Integer>> b : roomSegments) {
//					ArrayList<ArrayList<Integer>> b = roomSegments.get(u);
//					if(!a.equals(b) && b.contains(i)) {
//						if(a.size() <= b.size()) {
//							for(ArrayList<Integer> n : a) {
//								if(!b.contains(n)) {b.add(n);}
//							}
//							roomSegments.remove(a);
//						} else {
//							for(ArrayList<Integer> n : b) {
//								if(!a.contains(n)) {a.add(n);}
//							}
//							roomSegments.remove(b);
//						}
//					}
//				}
//			}
//		}
//		
//		ArrayList<Dungeon> rooms = new ArrayList<Dungeon>();
////		count = 0;
//		for(ArrayList<ArrayList<Integer>> l : roomSegments) {
//			boolean[][] arr = new boolean[d.X][d.Y];
////			for(int x = 0; x < d.X; x++) {
////				for(int y = 0; y < d.Y; y++) {
////					arr[x][y] = true;
////				}
////			}
//			for(int j = 0; j < d.X; j++) {
//				for(int k = 0; k < d.Y; k++) {
//					if(l.contains(new ArrayList<Integer>(Arrays.asList(j,k)))) {arr[j][k] = true;}
//				}
//			}
//			Dungeon temp = new Dungeon(0, d.X,d.Y);
//			temp.setLayout(arr);
//			
////			DungeonViewer dv = new DungeonViewer(temp,20);
////			dv.setVisible(true);
//			
//			rooms.add(temp);
//		}
//		
//		Dungeon[] ret = new Dungeon[rooms.size()-2];
//		
//		for(int g = 0; g < rooms.size()-2; g++) {
//			ret[g] = rooms.get(g);
//		}
//		
//		return ret;
//	}
//	
//	public static ArrayList<ArrayList<Integer>> intermediate(ArrayList<ArrayList<Integer>> al) {
//		ArrayList<ArrayList<Integer>> al2 = (ArrayList<ArrayList<Integer>>) al.clone();
//		for(int x1 = 0; x1 < al2.size(); x1++) {
//			for(int y1 = 0; y1 < al2.get(0).size(); y1++) {
//				if(al2.contains(new ArrayList<Integer>(Arrays.asList(x1,y1)))) {
////					for(int x2 = 0; x2)
//				}
//			}
//		}
//		return al2;
//	}
//	
//	public static Dungeon connectRooms(Dungeon[] d, Dungeon original, int seed) throws CloneNotSupportedException {
//		
//		System.out.println("recursion!");
//		
//		Random rand = new Random(seed);
//		
//		int big = -1;
//		
////		ArrayList<Dungeon> tempDun = new ArrayList<Dungeon>(Arrays.asList(d));
//		
//		for(Dungeon a : d) {
////			a = a.getOutlines();
////			System.out.println(a);
//			int b = a.countSpaces();
//			if(b>big) {big=b;}
//		}	
//		
////		int[][][] arrs = new int[d.length][big][2];
////		for(int p = 0; p < d.length; p++) {
////			arrs[p] = d[p].to2DIntArr();
////		}
//		
//		// this is also O(n^6), wow
//		double smallestDist = Double.MAX_VALUE;
//		ArrayList<ArrayList<Integer>> connections = new ArrayList<ArrayList<Integer>>();
//		int tempx1 = 0;
//		int tempx2 = 0;
//		int tempy1 = 0;
//		int tempy2 = 0;
//		int tempRoomIndex = -1;
//		//find closest other room
////		for(int k = i+1; k < d.length; k++) {
////			if(!connections.get(i).contains(k)) {
////				for(int x1 = 0; x1 < arrs[k].length; x1++) {
////					for(int y1 = 0; y1 < arrs[k][0].length; y1++) {
////						for(int x2 = 0; x2 < arrs[i].length; x2++) {
////							for(int y2=0; y2 < arrs[i][0].length; y2++) {
////								double temp = Math.sqrt(((x1-x2)*(x1-x2))-((y1-y2)*(y1-y2)));
////								if(temp < smallestDist) {
////									smallestDist = temp;
////									tempx1 = x1;
////									tempx2 = x2;
////									tempy1 = y1;
////									tempy2 = y2;
////									tempRoomIndex = k;
////								}
////							}
////						}
////					}
////				}
////			}
////		}
////		tempDun.set(i, connect(tempDun.get(i),tempDun.get(tempRoomIndex),tempx1,tempx2,tempy1,tempy2,rand.nextBoolean()));
////		tempDun.remove(tempRoomIndex);
////		connections.get(tempRoomIndex).add(i);
//		Dungeon notDI = (Dungeon)original.clone();
//		for(int x = 0; x < d[0].X; x++) {
//			for(int y = 0; y < d[0].Y; y++) {
//				if(!d[0].d[x][y]) { notDI.changeXY(x, y, true);}
//			}
//		}
//		
////		DungeonViewer dv = new DungeonViewer(d[0],20);
////		dv.setVisible(true);
//		
//		int[][] notDIArr = notDI.to2DIntArr();
//		int[][] tempArr = d[0].to2DIntArr();
//		for(int x1 = 0; x1 < tempArr.length; x1++) {
//			for(int y1 = 0; y1 < tempArr[0].length; y1++) {
//				for(int x2 = 0; x2 < notDIArr.length; x2++) {
//					for(int y2=0; y2 < notDIArr[0].length; y2++) {
//						double temp = Math.sqrt(((x1-x2)*(x1-x2))-((y1-y2)*(y1-y2)));
//						if(temp < smallestDist) {
//							smallestDist = temp;
//							tempx1 = x1;
//							tempx2 = x2;
//							tempy1 = y1;
//							tempy2 = y2;
//						}
//					}
//				}
//			}
//		}
//		
//		Dungeon newOrig = connect(d[0],original,tempx1,tempy1,tempx2,tempy2,rand.nextBoolean());
//		Dungeon[] newD = new Dungeon[d.length-1];
//		for(int p = 1; p <= d.length-1; p++) {
//			newD[p-1] = d[p];
//		}
//		
////		while(tempDun.size() > 1) {
////			tempDun.set(0, connect(tempDun.get(0),tempDun.get(1)));
////		}
//		if(d.length == 1) {
////			DungeonViewer dv = new DungeonViewer(d[0],20);
////			dv.setVisible(true);
////			dv = new DungeonViewer(original,20);
////			dv.setVisible(true);
//			return newOrig;}
//		else {return connectRooms(newD, newOrig, seed+1);}
//	}
//	
//	public static Dungeon connect(Dungeon a, Dungeon b, int x1, int y1, int x2, int y2, boolean vert) throws CloneNotSupportedException {
//		
//		Dungeon t = (Dungeon) a.clone();
//		
//		for(int x = 0; x < b.X; x++) {
//			for(int y = 0; y < b.Y; y++) {
//				if(!b.d[x][y]) {t.changeXY(x, y, false);}
//			}
//		}
//		
//		if(vert && y1 < y2) {
//			for(int i = y1; i <= y2; i++) {
//				t.changeXY(x1, i, false);
//			}
//			for(int j = x1; j <= x2; j++) {
//				t.changeXY(j, y2, false);
//			}
//		}else if(vert && y1 > y2) {
//			for(int i = y2; i <= y1; i++) {
//				t.changeXY(x2, i, false);
//			}
//			for(int j = x2; j <= x1; j++) {
//				t.changeXY(j, y1, false);
//			}
//		} else if(!vert && x1 < x2) {
//			for(int i = x1; i <= x2; i++) {
//				t.changeXY(i, y1, false);
//			}
//			for(int j = y1; j <= y2; j++) {
//				t.changeXY(x2, j, false);
//			}
//		}else if(!vert && x1 > x2) {
//			for(int i = x2; i <= x1; i++) {
//				t.changeXY(i, y2, false);
//			}
//			for(int j = y2; j <= y1; j++) {
//				t.changeXY(x1, j, false);
//			}
//		}
//		
//		return t;
//		
//	}
//	
	
}
