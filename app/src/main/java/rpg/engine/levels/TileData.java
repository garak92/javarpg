package rpg.engine.levels;

class TileData {
    private final int row;
    private final int column;
    private final boolean isSolid;

    public TileData(int row, int column, boolean isSolid) {
        this.row = row;
        this.column = column;
        this.isSolid = isSolid;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isSolid() {
        return isSolid;
    }

    @Override
    public String toString() {
        return "TileData{" +
                "row=" + row +
                ", column=" + column +
                ", isSolid=" + isSolid +
                '}';
    }
}

