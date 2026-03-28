public class Game {
    public static void main(String[] args) {
        Memory memory = new Memory(6, 10);
        int[][] grid = memory.getGrid();
        int[][] colors = memory.getColors();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }

        for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < colors[i].length; j++) {
                System.out.print(colors[i][j] + " ");
            }
            System.out.println();
        }
    }
}