//Please run this one

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class _Main {
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FileDescriptor.out), "ASCII"), 512);

		boolean done = false;
		
		while(!done) {
			//get dungeon type
			bw.write("Enter the type of dungeon you want: ");
			bw.write("\n1. BetterRandomWalk");
			bw.write("\n2. CellularAutomata");
			bw.write("\n3. SpacePartition");
			bw.write("\n4. Voroni");
			bw.write("\n\n");
			bw.flush();
		
			try {
				int dunType = Integer.parseInt(br.readLine());
				done = true;
			} catch(Exception e) {
				bw.write("\nInvalid input, try again.\n");
				bw.flush();
			}
			
		}
		
		bw.flush();
		bw.close();
		br.close();
		
	}
	
}
