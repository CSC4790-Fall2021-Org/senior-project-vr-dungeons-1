import java.util.ArrayList;

public class test {

    public static void main(String[] args) {
        
        Dungeon d = new Voronoi(1234,100,100,100);
        d.setLayout(d.randomize());
        
        System.out.println("step 1");
        
        DungeonViewer dv1 = new DungeonViewer(d,5);
        dv1.setVisible(true);
        
        DungeonViewer dv = new DungeonViewer(d,5,d.getCornersMap());
//        dv.setVisible(true);
        
        ArrayList<ArrayList<Integer[]>> al = d.getCornersList();
        
        System.out.println("step 2");
        
        System.out.println("al.toString() = " + al.toString());
        
        System.out.println("bruh");
        
        System.out.println("step 3");
        
        d.d = d.connectRooms();
        DungeonViewer dv2 = new DungeonViewer(d,5);
        dv2.setVisible(true);
        
        System.out.println("done");
    }
    
}
