//Please run this one

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

public class _Main {
	
	public static void main(String[] args) throws IOException {
		
		Random rand = new Random();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FileDescriptor.out), "ASCII"), 512);

		boolean done = false;
		
		int dunType = 0;
		int seed = 0;
		int width = 0;
		int height = 0;
		Dungeon d = null;
		
		//get dungeon type
		while(!done) {
			bw.write("Enter the type of dungeon you want: ");
			bw.write("\n1. BetterRandomWalk");
			bw.write("\n2. CellularAutomata");
			bw.write("\n3. SpacePartition");
			bw.write("\n4. Voronoi");
			bw.write("\n\n");
			bw.flush();
					
			try {
				dunType = Integer.parseInt(br.readLine());
				if(dunType > 4 || dunType < 1) { throw new Exception(); }
				done = true;
			} catch(Exception e) {
				bw.write("\nInvalid input, try again.\n");
				bw.flush();
			}
			
		}
		
		done = false;
		
		//get seed
		while(!done) {
			bw.write("\n\nEnter the seed of the dungeon you want (or leave it blank if you want a random one): ");
			bw.flush();
			
			try {
				String in = br.readLine();
				if ("".equals(in)) {
					seed = rand.nextInt();
				} else {
					seed = Integer.parseInt(in);
				}
				done = true;
			} catch(Exception e) {
				bw.write("\nInvalid input, try again.\n");
				bw.flush();
			}
		}
		
		done = false;
		
		//get width of the dungeon
		while(!done) {
			try {
				bw.write("\n\nEnter the width of the dungeon you want: ");
				bw.flush();
				
				width = Integer.parseInt(br.readLine());
				done = true;
			} catch(Exception e) {
				bw.write("\nInvalid input, try again.\n");
				bw.flush();
			}
		}
		
		done = false;
		
		//get height of the dungeon
		while(!done) {
			try {
				bw.write("\n\nEnter the height of the dungeon you want: ");
				bw.flush();
				
				height = Integer.parseInt(br.readLine());
				done = true;
			} catch(Exception e) {
				bw.write("\nInvalid input, try again.\n");
				bw.flush();
			}
		}
		
		switch(dunType) {
		case 1:
			d = new BetterRandomWalk(seed,width,height);
			break;
		case 2:
			d = new CellularAutomata(seed,width,height);
			break;
		case 3:
			d = new SpacePartition(seed,width,height);
			break;
		case 4:
			d = new Voronoi(seed,width,height);
			break;
		}
		
		boolean[][] dun = d.randomize(seed);
		d.setLayout(dun);
		
		int scale = (1980/width >= 1055/height) ? 1980/width : 1055/height;
		
//		bw.write(Integer.toString(scale));
//		bw.flush();
		
		DungeonViewer dv = new DungeonViewer(d,scale);
		dv.setVisible(true);
		
		bw.close();
		br.close();
		
	}
	
}
