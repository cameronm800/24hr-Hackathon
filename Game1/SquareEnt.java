private class SquareEnt {
    boolean isRed = true;
    int squareSize;
    int[] coords = new int[2];

    public SquareEnt(int isRed, int squareSize, int[] coords) {
        this.isRed = isRed;
        this.squareSize = squareSize;
        this.coords = coords;
    }
}