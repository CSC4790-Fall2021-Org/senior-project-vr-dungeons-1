import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

//with credit to Timothy Hely
//https://gamedevelopment.tutsplus.com/tutorials/how-to-use-bsp-trees-to-generate-game-maps--gamedev-12268

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Vector;

class Leaf extends Object{
	private final int MIN_LEAF_SIZE = 6;

	public int y, x, width, height; // the position and size of this Leaf
	
	public Leaf leftChild; // the Leaf's left child Leaf
	public Leaf rightChild; // the Leaf's right child Leaf
	public Rectangle room; // the room that is inside this Leaf
	public ArrayList<Rectangle> halls; // hallways to connect this Leaf to other Leafs
	
	public Leaf(int X, int Y, int Width, int Height) {
	    // initialize our leaf
	    x = X;
	    y = Y;
	    width = Width;
	    height = Height;
	}
	
	public boolean split() {
	    // begin splitting the leaf into two children
	    if (leftChild != null || rightChild != null)
	        return false; // we're already split! Abort!
	
	    // determine direction of split
	    // if the width is >25% larger than height, we split vertically
	    // if the height is >25% larger than the width, we split horizontally
	    // otherwise we split randomly
	    boolean splitH = Math.random() > 0.5;
	    if (width > height && width / height >= 1.25)
	        splitH = false;
	    else if (height > width && height / width >= 1.25)
	        splitH = true;
	
	    int max = (splitH ? height : width) - MIN_LEAF_SIZE; // determine the maximum height or width
	    if (max <= MIN_LEAF_SIZE)
	        return false; // the area is too small to split any more...
	
	    int split = (int) (Math.random() * (max - MIN_LEAF_SIZE) + MIN_LEAF_SIZE); // determine where we're going to split
	
	    // create our left and right children based on the direction of the split
	    if (splitH)
	    {
	        leftChild = new Leaf(x, y, width, split);
	        rightChild = new Leaf(x, y + split, width, height - split);
	    }
	    else
	    {
	        leftChild = new Leaf(x, y, split, height);
	        rightChild = new Leaf(x + split, y, width - split, height);
	    }
	    return true; // split successful!
	}
	
	public void createRooms() {
		// this function generates all the rooms and hallways for this Leaf and all of its children.
		if (leftChild != null || rightChild != null)
		{
			// this leaf has been split, so go into the children leafs
			if (leftChild != null)
			{
				leftChild.createRooms();
			}
			if (rightChild != null)
			{
				rightChild.createRooms();
			}

			// if there are both left and right children in this Leaf, create a hallway between them
			if (leftChild != null && rightChild != null)
			{
				createHall(leftChild.getRoom(), rightChild.getRoom());
			}

		}
		else
		{
			// this Leaf is the ready to make a room
			Point roomSize;
			Point roomPos;
			// the room can be between 3 x 3 tiles to the size of the leaf - 2.
			roomSize = new Point((int) (Math.random() * (width - 4) + 3), (int) (Math.random() * (height - 4) + 3));
			// place the room within the Leaf, but don't put it right against the side of the leaf (that would merge rooms together)
			roomPos = new Point((int) (Math.random() * (width - roomSize.x - 1) + 1), (int) (Math.random() * (height - roomSize.y - 1) + 1));
			room = new Rectangle(x + roomPos.x, y + roomPos.y, roomSize.x, roomSize.y);
		}
	}
	
	public Rectangle getRoom() {
		// iterate all the way through these leafs to find a room, if one exists.
		if (room != null)
			return room;
		else
		{
			Rectangle lRoom = null;
			Rectangle rRoom = null;
			if (leftChild != null)
			{
				lRoom = leftChild.getRoom();
			}
			if (rightChild != null)
			{
				rRoom = rightChild.getRoom();
			}
			if (lRoom == null && rRoom == null)
				return null;
			else if (rRoom == null)
				return lRoom;
			else if (lRoom == null)
				return rRoom;
			else if (Math.random() > .5)
				return lRoom;
			else
				return rRoom;
		}
	}
	
	public void createHall(Rectangle l, Rectangle r) {
	    // now we connect these two rooms together with hallways.
	    // this looks pretty complicated, but it's just trying to figure out which point is where and then either draw a straight line, or a pair of lines to make a right-angle to connect them.
	    // you could do some extra logic to make your halls more bendy, or do some more advanced things if you wanted.
	 
	    halls = new ArrayList<Rectangle>();
	    
	    Point point1 = new Point((int) (Math.random() * (l.getWidth() - 2) + l.getX()), (int) (Math.random() * (l.getHeight() - 2) + l.getY()));
	    Point point2 = new Point((int) (Math.random() * (r.getWidth() - 2) + r.getX()), (int) (Math.random() * (r.getHeight() - 2) + r.getY()));
	 
	    int w = point2.x - point1.x;
	    int h = point2.y - point1.y;
	    
	    if (w < 0)
	    {
	        if (h < 0)
	        {
	            if (Math.random() < 0.5)
	            {
	                halls.add(new Rectangle(point2.x, point1.y, Math.abs(w), 1));
	                halls.add(new Rectangle(point2.x, point2.y, 1, Math.abs(h)));
	            }
	            else
	            {
	                halls.add(new Rectangle(point2.x, point2.y, Math.abs(w), 1));
	                halls.add(new Rectangle(point1.x, point2.y, 1, Math.abs(h)));
	            }
	        }
	        else if (h > 0)
	        {
	            if (Math.random() < 0.5)
	            {
	                halls.add(new Rectangle(point2.x, point1.y, Math.abs(w), 1));
	                halls.add(new Rectangle(point2.x, point1.y, 1, Math.abs(h)));
	            }
	            else
	            {
	                halls.add(new Rectangle(point2.x, point2.y, Math.abs(w), 1));
	                halls.add(new Rectangle(point1.x, point1.y, 1, Math.abs(h)));
	            }
	        }
	        else // if (h == 0)
	        {
	            halls.add(new Rectangle(point2.x, point2.y, Math.abs(w), 1));
	        }
	    }
	    else if (w > 0)
	    {
	        if (h < 0)
	        {
	            if (Math.random() < 0.5)
	            {
	                halls.add(new Rectangle(point1.x, point2.y, Math.abs(w), 1));
	                halls.add(new Rectangle(point1.x, point2.y, 1, Math.abs(h)));
	            }
	            else
	            {
	                halls.add(new Rectangle(point1.x, point1.y, Math.abs(w), 1));
	                halls.add(new Rectangle(point2.x, point2.y, 1, Math.abs(h)));
	            }
	        }
	        else if (h > 0)
	        {
	            if (Math.random() < 0.5)
	            {
	                halls.add(new Rectangle(point1.x, point1.y, Math.abs(w), 1));
	                halls.add(new Rectangle(point2.x, point1.y, 1, Math.abs(h)));
	            }
	            else
	            {
	                halls.add(new Rectangle(point1.x, point2.y, Math.abs(w), 1));
	                halls.add(new Rectangle(point1.x, point1.y, 1, Math.abs(h)));
	            }
	        }
	        else // if (h == 0)
	        {
	            halls.add(new Rectangle(point1.x, point1.y, Math.abs(w), 1));
	        }
	    }
	    else // if (w == 0)
	    {
	        if (h < 0)
	        {
	            halls.add(new Rectangle(point2.x, point2.y, 1, Math.abs(h)));
	        }
	        else if (h > 0)
	        {
	            halls.add(new Rectangle(point1.x, point1.y, 1, Math.abs(h)));
	        }
	    }
	}
}


public class SpacePartition {
	
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
		
		//randomize the dungeon here
		
		int MAX_LEAF_SIZE = 20;
		
		ArrayList<Leaf> leafs = new ArrayList<>();

		//Leaf l; // helper Leaf

		// first, create a Leaf to be the 'root' of all Leafs.
		Leaf root = new Leaf(0, 0, in.length, in[0].length);
		leafs.add(root);

		boolean did_split = true;
		// we loop through every Leaf in our Vector over and over again, until no more Leafs can be split.
		while (did_split)
		{
			did_split = false;
			for (int i = 0; i < leafs.size(); i++)
			{
			    Leaf l = leafs.get(i);
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
		int count = 1;
		
		for (Leaf t : leafs) {
//		    System.out.println("Leaf #" + count);
		    count++;
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
			for (int c = y; c < y + h; c++) {
				for (int r = x; r < x + w; r++) {
					map[r][c] = false;
				}
			}
		//}
		
		return map;
	}
	
	public static void main(String args[]) {
		//this is the test method, it prints out the random dungeon with a seed of 1234 at the default size
		Dungeon dun = new Dungeon(1234, 100, 100);
		dun.setLayout(randomize(dun.d, dun.SEED));
//		for (boolean[] b : dun.d)
//			System.out.println(Arrays.toString(b));
		DungeonViewer dv = new DungeonViewer(dun,10);
		dv.setVisible(true);
	}
	
}
