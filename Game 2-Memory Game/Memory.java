import java.util.ArrayList;
import java.util.Random;

public class Memory {
    private int[][] grid;
     
    public Memory(int k, int r) {
        this.grid = new int[k][k]; 
        this.generateGrid(k, r);
    }

    public void generateGrid(int k, int r) {
        Random rand = new Random();

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                int randomInt = rand.nextInt(r);
                this.addNum(i , j, randomInt);
            }
        }
    }

    public void addNum(int i, int j, int n) {
        this.grid[i][j] = n;
    }

    public int[][] getGrid() {
        return this.grid;
    }
}