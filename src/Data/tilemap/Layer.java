package Data.tilemap;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Layer {

    private JsonArray data;
    private int height;
    private int id;
    private String name;
    private int opacity;
    private String type;
    private boolean visible;
    private int width;
    private int x;
    private int y;

    private ArrayList<TileSet> tileSets;

    public Layer(JsonObject layer) {


        this.data = layer.getJsonArray("data");
        this.height = layer.getInt("height");
        this.id = layer.getInt("id");
        this.name = layer.getString("name");
        this.opacity = layer.getInt("opacity");
        this.type = layer.getString("type");
        this.visible = layer.getBoolean("visible");
        this.width = layer.getInt("width");
        this.x = layer.getInt("x");
        this.y = layer.getInt("y");
    }

    public void draw(Graphics2D g2d){
        for(int i = 0; i < data.size(); i++) {
            int tile = data.getInt(i);

            if (tile != 0) {
                boolean found = false;
                for (TileSet tileSet : tileSets) {
                    if (tileSet.getFirstgid() < tile && !found) {
                        if (tileSet.containsTile(tile)) {
                            AffineTransform tx = new AffineTransform();
                            tx.translate((i % width) * tileSet.getTileWidth(), (Math.floor(i / height) * tileSet.getTileHeight()));
                            g2d.drawImage(tileSet.getTileImage(tile), tx, null);
                            found = true;
                        }
                    }
                }
           }
        }
    }

    public void setTileSets(ArrayList<TileSet> tileSets){
     this.tileSets = tileSets;
    }

    public boolean isVisible(){
        return visible;
    }
}


