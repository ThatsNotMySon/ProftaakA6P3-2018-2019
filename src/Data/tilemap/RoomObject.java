package Data.tilemap;

import javax.json.JsonObject;

public class RoomObject {
    String name;
    private int x;
    private int y;
    private int width;
    private int height;

    public RoomObject(JsonObject jo){
        this.name = jo.getString("name");
        this.x = jo.getInt("x");
        this.y = jo.getInt("y");
        this.width = jo.getInt("width");
        this.height = jo.getInt("height");
        System.out.println("new RoomObject");
    }
}
