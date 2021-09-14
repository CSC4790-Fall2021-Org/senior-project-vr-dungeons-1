package main;

import java.util.Random;
public class helper {
    Random rand = new Random();
    float chanceToStartAlive = 0.43f;
    
    public boolean[][] initialiseMap(boolean[][] map){
        for(int x=0; x<map.length; x++){
            for(int y=0; y<map[0].length; y++){
                if(rand.nextFloat() < chanceToStartAlive){
                    map[x][y] = true;
                }
            }
        }
        return map;
    }
    public int countAliveNeighbours(boolean[][] map, int x, int y){
        int count = 0;
        for(int i=-1; i<2; i++){
            for(int j=-1; j<2; j++){
                int neighbor_x = x+i;
                int neighbor_y = y+j;
                //If we're looking at the middle point
                if(i == 0 && j == 0){
                    //Do nothing, we don't want to add ourselves in!
                }
                //In case the index we're looking at it off the edge of the map
                else if(neighbor_x < 0 || neighbor_y < 0 || neighbor_x >= map.length || neighbor_y >= map[0].length){
                    count = count + 1;
                }
                //Otherwise, a normal check of the neighbour
                else if(map[neighbor_x][neighbor_y]){
                    count = count + 1;
                }
            }
        }
        return count;
    }
    public boolean[][] doSimulationStep(boolean[][] oldMap, int x, int y){
        boolean[][] newMap = new boolean[x][y];
        //Loop over each row and column of the map
        for(int i=0; i<oldMap.length; i++){
            for(int j=0; j<oldMap[0].length; j++){
                int nbs = countAliveNeighbours(oldMap, i, j);
                //The new value is based on our simulation rules
                //First, if a cell is alive but has too few neighbours, kill it.
                if(oldMap[i][j]){
                    if(nbs < 3){
                        newMap[i][j] = false;
                    }
                    else{
                        newMap[i][j] = true;
                    }
                } //Otherwise, if the cell is dead now, check if it has the right number of neighbours to be 'born'
                else{
                    if(nbs > 4){
                        newMap[i][j] = true;
                    }
                    else{
                        newMap[i][j] = false;
                    }
                }
            }
        }
        return newMap;
    }
    public boolean[][] generateMap(int x, int y){
        //Create a new map
        boolean[][] cellmap = new boolean[x][y];
        //Set up the map with random values
        cellmap = initialiseMap(cellmap);
        //And now run the simulation for a set number of steps
        for(int i=0; i<1000; i++){
            cellmap = doSimulationStep(cellmap, x, y);
        }
        return cellmap;
    }
}

