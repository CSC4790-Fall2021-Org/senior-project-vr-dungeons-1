import java.io.IOException;
import java.util.Random;

public class GenerateDemoCSVs {

    public static void main(String[] args) throws IOException {
        
        Dungeon dun;
        Random rand = new Random();
        int seed = -1;
        int x = 100;
        int y = 100;
        int cells = 400;
        String path = "GeneratorCode/dungeonCSV/demoCSVs/";
        
        for(int a = 0; a < 50; a++) {
            seed = rand.nextInt();
            dun = new BetterRandomWalk(seed,x,y);
            dun.randomize();
            dun.outputCSV(path+"BRW"+a+".csv");
        }
        for(int b = 0; b < 50; b++) {
            seed = rand.nextInt();
            dun = new CellularAutomata(seed,x,y);
            dun.randomize();
            dun.outputCSV(path+"CA"+b+".csv");
        }
        for(int c = 0; c < 50; c++) {
            seed = rand.nextInt();
            cells = 500-rand.nextInt(101);
            dun = new Voronoi(seed,x,y,cells);
            dun.randomize();
            dun.outputCSV(path+"Voronoi"+c+".csv");
        }
        
    }
    
}
