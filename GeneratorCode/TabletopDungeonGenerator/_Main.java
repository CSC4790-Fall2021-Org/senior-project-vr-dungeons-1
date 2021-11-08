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
			bw.write("\n3. Voronoi");
			bw.write("\n");
			bw.flush();
					
			try {
				dunType = Integer.parseInt(br.readLine());
				if(dunType > 3 || dunType < 1) { throw new Exception(); }
				done = true;
			} catch(Exception e) {
				bw.write("\nInvalid input, try again.\n");
				bw.flush();
			}
			
		}
		
		done = false;
		
		//get seed
		while(!done) {
			bw.write("\nEnter the seed of the dungeon you want (or leave it blank if you want a random one): ");
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
		        int cells = -1;
		        done = false;
		        while(!done) {
		            try {
                                bw.write("\n\nEnter the number of cells you want in the Voronoi diagram: ");
                                bw.flush();
                                
                                cells = Integer.parseInt(br.readLine());
                                if(cells<1) {throw new Exception();}
                                done = true;
                            } catch(Exception e) {
                                bw.write("\nInvalid input, try again.\n");
                                bw.flush();
                            }
                        }
		        d = new Voronoi(seed,width,height,cells);
			break;
		}
		
		bw.write("\nGenerating Dungeon...");
		bw.flush();
		
		d.randomize();
		d.setLayout(d.connectRooms());
		
		int scale = (1366/width <= 743/height) ? 1366/width : 743/height;
		
		DungeonViewer dv = new DungeonViewer(d,scale);
		dv.setVisible(true);
		
		bw.write("\nDungeon Generated!\n");
		bw.flush();
		
		String path = "";
                done = false;
                while(!done) {
                    try {
                        bw.write("\n\nIf you want to output a CSV, enter the filename (WARNING: WILL OVERWRITE A FILE WITH THE SAME NAME). Leave blank if you don't want a CSV generated: ");
                        bw.flush();
                        
                        path = br.readLine();
                        done = true;
                    } catch(Exception e) {
                        bw.write("\nInvalid input, try again.\n");
                        bw.flush();
                    }
                }
                
                if(!"".equals(path)) {
                    try {
                        d.outputCSV("GeneratorCode/" + path);
                        bw.write("\n\nCSV output worked, check file at " + path);
                        bw.flush();
                    } catch(Exception e) {
                        bw.write("\n\nCSV output didn't work, filepath \'" + path + "\' was bad");
                        bw.flush();
                    }
                }
		
		bw.close();
		br.close();
		
	}
	
}
