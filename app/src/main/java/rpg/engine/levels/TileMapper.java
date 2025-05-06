package rpg.engine.levels;

import java.util.HashMap;
import java.util.Map;

class TileMapper {

    private static final Map<String, TileData> tileDataMap = new HashMap<>();

    static {
        // Initialize the HashMap with the tile data
        tileDataMap.put("0", new TileData(0, 0, false));
        tileDataMap.put("1", new TileData(0, 1, false));
        tileDataMap.put("2", new TileData(0, 2, true));
        tileDataMap.put("3", new TileData(0, 3, true));
        tileDataMap.put("4", new TileData(0, 4, true));
        tileDataMap.put("5", new TileData(0, 5, true));
        tileDataMap.put("6", new TileData(0, 6, true));
        tileDataMap.put("7", new TileData(0, 7, true));
        tileDataMap.put("8", new TileData(0, 8, true));
        tileDataMap.put("9", new TileData(1, 0, true));
        tileDataMap.put("a", new TileData(1, 1, true));
        tileDataMap.put("b", new TileData(1, 2, true));
        tileDataMap.put("c", new TileData(1, 3, true));
        tileDataMap.put("d", new TileData(1, 4, true));
        tileDataMap.put("e", new TileData(1, 5, true));
        tileDataMap.put("f", new TileData(1, 6, true));
        tileDataMap.put("g", new TileData(1, 7, true));
        tileDataMap.put("h", new TileData(1, 8, true));
        tileDataMap.put("i", new TileData(2, 0, true));
        tileDataMap.put("j", new TileData(2, 1, true));
        tileDataMap.put("k", new TileData(2, 2, true));
        tileDataMap.put("l", new TileData(2, 3, true));
        tileDataMap.put("m", new TileData(2, 4, true));
        tileDataMap.put("n", new TileData(2, 5, true));
        tileDataMap.put("o", new TileData(2, 6, true));
        tileDataMap.put("p", new TileData(2, 7, true));
        tileDataMap.put("q", new TileData(2, 8, true));
        tileDataMap.put("r", new TileData(3, 0, true));
        tileDataMap.put("s", new TileData(3, 1, true));
        tileDataMap.put("t", new TileData(3, 2, true));
        tileDataMap.put("u", new TileData(3, 3, true));
        tileDataMap.put("v", new TileData(3, 4, true));
        tileDataMap.put("w", new TileData(3, 5, true));
        tileDataMap.put("x", new TileData(3, 6, false));
        tileDataMap.put("y", new TileData(3, 7, true));
        tileDataMap.put("z", new TileData(3, 8, true));
        tileDataMap.put("{", new TileData(4, 0, true));
        tileDataMap.put("|", new TileData(4, 1, true));
        tileDataMap.put("}", new TileData(4, 2, true));
        tileDataMap.put("~", new TileData(4, 3, true));
        tileDataMap.put("€", new TileData(4, 4, true));
        tileDataMap.put("©", new TileData(4, 5, false));
        tileDataMap.put("°", new TileData(4, 6, true));
        tileDataMap.put("µ", new TileData(4, 7, false));
        tileDataMap.put("π", new TileData(4, 8, true));
        tileDataMap.put("Ω", new TileData(5, 0, true));
        tileDataMap.put("∞", new TileData(5, 1, true));
        tileDataMap.put("≈", new TileData(5, 2, true));
        tileDataMap.put("≠", new TileData(5, 3, true));
        tileDataMap.put("≤", new TileData(5, 4, true));
        tileDataMap.put("≥", new TileData(5, 5, true));
        tileDataMap.put("¥", new TileData(5, 6, true));
        tileDataMap.put("∆", new TileData(5, 7, true));
        tileDataMap.put("♂", new TileData(5, 8, true));
        tileDataMap.put("♀", new TileData(6, 0, true));
        tileDataMap.put("•", new TileData(6, 1, true));
        tileDataMap.put("♦", new TileData(6, 2, true));
        tileDataMap.put("♣", new TileData(6, 3, true));
        tileDataMap.put("♠", new TileData(6, 4, true));
        tileDataMap.put("♪", new TileData(6, 5, true));
        tileDataMap.put("♫", new TileData(6, 6, true));
        tileDataMap.put("✌", new TileData(6, 7, true));
        tileDataMap.put("✿", new TileData(6, 8, true));
    }

    public static TileData getTileData(String symbol) {
        return tileDataMap.get(symbol);
    }
}
