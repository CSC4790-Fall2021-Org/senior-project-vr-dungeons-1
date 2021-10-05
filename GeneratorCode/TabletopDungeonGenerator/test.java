import java.util.ArrayList;

public class test {

    public static void main(String[] args) {
        
        Dungeon d = new Voronoi(123,50,50,200);
        d.setLayout(d.randomize());
        
        System.out.println("step 1");
        
        DungeonViewer dv = new DungeonViewer(d,10,d.getCornersMap());
        dv.setVisible(true);
        
        ArrayList<ArrayList<Integer[]>> al = d.getCornersList();
        
        System.out.println("step 2");
        
        System.out.println("al.toString() = " + al.toString());
        
        System.out.println("bruh");
    }
    
}
