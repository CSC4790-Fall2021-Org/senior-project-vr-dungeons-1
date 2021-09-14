package main;

import space.*;
import java.awt.Rectangle;
import java.util.ArrayList;

public class SpacePartitionDraw{
	
	public static void main(String[] args) {
		int MAX_LEAF_SIZE = 20;
		
		ArrayList<Leaf> leafs = new ArrayList<>();

		//Leaf l; // helper Leaf

		// first, create a Leaf to be the 'root' of all Leafs.
		Leaf root = new Leaf(0, 0, 100, 100);
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
		
		StdDraw.setScale(0, 100);
		StdDraw.clear(StdDraw.BLACK);
		StdDraw.enableDoubleBuffering();
		
		StdDraw.setPenColor(StdDraw.WHITE);
		
		for (int n = 0; n < leafs.size(); n++) {
			Leaf t = leafs.get(n);
			if (t.room != null) {
				double x = (double) t.room.x;
				double y = (double) t.room.y;
				double w = (double) t.room.width;
				double h = (double) t.room.height;
				
				StdDraw.filledRectangle(x + w/2, y + h/2, w/2, h/2);
				//System.out.println(x + " " + y + " " + w  + " " + h);
			}
				
			if (t.halls != null) {
				for (int m = 0; m < t.halls.size(); m++) {
					Rectangle r = t.halls.get(m);
					double x = r.getX();
					double y = r.getY();
					double w = r.getWidth();
					double h = r.getHeight();
					
					StdDraw.filledRectangle(x + w/2, y + h/2, w/2, h/2);
					//System.out.println(x + " " + y + " " + w  + " " + h);
				}
			}
		}
		StdDraw.show();
	}

}
