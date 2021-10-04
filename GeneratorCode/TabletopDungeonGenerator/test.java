import java.util.ArrayList;

public class test {

    public static void main(String[] args) {
        
        Dungeon d = new Voronoi(123,100,100,200);
        
        ArrayList<ArrayList<Integer[]>> al = d.getCornersList();
        
        System.out.println(al.toString());
        
    }
    
}
