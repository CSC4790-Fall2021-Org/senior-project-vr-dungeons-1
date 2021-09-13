
public class DrunkenUmberhulk {

	    public static void main(String[] args) {
	        int n = 20;
	        StdDraw.setScale(-n - 0.5, n + 0.5);
	        StdDraw.clear(StdDraw.GRAY);
	        StdDraw.enableDoubleBuffering();

	        int x = 0, y = 0;
	        int steps = 0;
	        while (Math.abs(x) < n && Math.abs(y) < n) {
	            StdDraw.setPenColor(StdDraw.WHITE);
	            StdDraw.filledSquare(x, y, 0.45);
	            double r = Math.random();
	            if      (r < 0.25) x--;
	            else if (r < 0.50) x++;
	            else if (r < 0.75) y--;
	            else if (r < 1.00) y++;
	            steps++;
	            StdDraw.setPenColor(StdDraw.BLUE);
	            StdDraw.filledSquare(x, y, 0.45);
	            StdDraw.show();
	            StdDraw.pause(40);
	        }
	        StdOut.println("Total steps = " + steps);
	    }

	}


