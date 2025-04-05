package rpg.Common;

public class TileData {
    private int row;
    private int column;
    private boolean isPassable;

    public TileData(int row, int column, boolean isPassable) {
        this.row = row;
        this.column = column;
        this.isPassable = isPassable;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isPassable() {
        return isPassable;
    }

    @Override
    public String toString() {
        return "TileData{" +
                "row=" + row +
                ", column=" + column +
                ", isPassable=" + isPassable +
                '}';
    }
}

