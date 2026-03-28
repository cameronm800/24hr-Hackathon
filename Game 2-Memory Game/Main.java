import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Memory memory = new Memory(5, 10);
        int[][] grid = memory.getGrid();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }

    }
}