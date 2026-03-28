import java.util.Random;

public class Memory {
    private int[][] grid;
    private int[][] colors;
     
    public Memory(int k, int r) {
        this.grid = new int[k][k];
        this.colors = new int[k][3];
        this.generateColors(k); 
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

    public void generateColors(int k) {
        Random rand = new Random();

        for (int i = 0; i < k; i++) {
            int r = rand.nextInt(255);
            int g = rand.nextInt(255);
            int b = rand.nextInt(255);

            this.colors[i][0] = r;
            this.colors[i][1] = g;
            this.colors[i][2] = b;
        }
    }

    public void addNum(int i, int j, int n) {
        this.grid[i][j] = n;
    }

    public int[][] getColors() {
        return this.colors;
    }

    public int[][] getGrid() {
        return this.grid;
    }
}