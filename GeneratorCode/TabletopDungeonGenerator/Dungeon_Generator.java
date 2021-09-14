import java.util.Arrays;
import java.util.Scanner;

public class Dungeon_Generator {
        public static void main(String[] args) {  
            // All number values of rooms indicate enemy type and difficulty. 0 Means the room is empty
            hardRoom x = new hardRoom();
            easyRoom y = new easyRoom();
            mediumRoom z = new mediumRoom();
            secretRoom q = new secretRoom();
            basicRoom a = new basicRoom();
            int randomizer;
            int hardrandomizer;         // Establishes hard rooms as rarer than the others
            int secretrandomizer;       // Establishes secret rooms as the rarest rooms of all
            int size = 0;
            Boolean valid = true;
            Scanner input = new Scanner(System.in);
            System.out.print("Please give the total size of your dungeon: ");  
            String line = input.nextLine().trim();
            try {
                size = Integer.parseInt(line);
            }
            catch (NumberFormatException Exception) {
                valid = false;
            }
            if(valid == false){
                System.out.println("Please only type integers in for the size of the dungeon");
            }
            else {
                int[][] dungeon = new int[size][size];
                for(int i = 0; i < size; i++) {
                    for(int j = 0; j < size; j++) {
                        randomizer = (int)(Math.random() * 6);
                        hardrandomizer = (int)(Math.random() * 3);
                        secretrandomizer = (int)(Math.random() * 11);
                        if(randomizer == 1) {
                            dungeon[i][j] = a.getRoom();
                        }
                        if(randomizer == 2) {
                            dungeon[i][j] = y.getRoom();
                        }
                        if(randomizer == 3) {
                            dungeon[i][j] = z.getRoom();
                        }
                        if(randomizer == 4 && hardrandomizer == 1) {                            
                            dungeon[i][j] = a.getRoom();
                        }
                        if(randomizer == 4 && hardrandomizer == 2) {                            
                            dungeon[i][j] = x.getRoom();
                        }                                                  
                        if(randomizer == 5 && secretrandomizer != 10) {
                            dungeon[i][j] = a.getRoom();
                        }   
                        if(randomizer == 5 && secretrandomizer == 10) {
                            dungeon[i][j] = q.getRoom();
                        } 
                    }
                }
                System.out.println(Arrays.deepToString(dungeon).replace("], ", "]\n"));
            }
        }
        
}