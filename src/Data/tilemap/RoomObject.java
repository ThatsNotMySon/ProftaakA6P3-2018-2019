package Data.tilemap;

import javax.json.JsonObject;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class RoomObject {
    String name;
    private int x;
    private int y;
    private int width;
    private int height;
    private TileMap tileMap;
    private ArrayList<Point2D> chairpositions;

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public ArrayList<Point2D> getChairpositions() {
        return chairpositions;
    }

    public RoomObject(JsonObject jo, TileMap tileMap){
        this.name = jo.getString("name");
        this.x = jo.getInt("x");
        this.y = jo.getInt("y");
        this.width = jo.getInt("width");
        this.height = jo.getInt("height");
        this.tileMap = tileMap;
        this.chairpositions = tileMap.getChairPositionsFromBoundries(new Point2D.Double(x/16, y/16), new Point2D.Double((x+width)/16, (y+height)/16));
    }
}
