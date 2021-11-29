import java.util.ArrayList;

public class test {

    public static void main(String[] args) {
        
        Dungeon d = new CellularAutomata(123,100,100);
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
        
        Dungeon d1;
        try {
            d1 = (Dungeon) d.clone();
            d1.d = d1.connectRooms();
            DungeonViewer dv2 = new DungeonViewer(d1,5);
            dv2.setVisible(true);
            System.out.println("step 4");
            
            DungeonViewer dv3 = new DungeonViewer(d1,5,d.numberRoomsMap());
//            dv3.setVisible(true);
        }
        catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("done");
    }
    
}
