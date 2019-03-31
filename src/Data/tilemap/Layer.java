package Data.tilemap;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
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

    BufferedImage image = null;

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
        if(image == null)
        {
            image = new BufferedImage(width * 16, height * 16, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g = image.createGraphics();

            for(int i = 0; i < data.size(); i++) {
                int tile = data.getInt(i);

                if (tile != 0) {
                    boolean found = false;
                    for (TileSet tileSet : tileSets) {
                        if (tileSet.getFirstgid() < tile && !found) {
                            if (tileSet.containsTile(tile)) {
                                AffineTransform tx = new AffineTransform();
                                tx.translate((i % width) * tileSet.getTileWidth(), (Math.floor(i / height) * tileSet.getTileHeight()));
                                g.drawImage(tileSet.getTileImage(tile), tx, null);
                                found = true;
                                break;
                            }
                        }
                    }
                }
            }
        }



        g2d.drawImage(image, new AffineTransform(), null);

    }

    public void setTileSets(ArrayList<TileSet> tileSets){
     this.tileSets = tileSets;
    }

    public boolean isVisible(){
        return visible;
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<Integer> getData(){
        ArrayList<Integer> newList = new ArrayList<>();
        for (int i = 0 ; i < data.size(); i++){
            newList.add(this.data.getInt(i));
        }
        return newList;
    }
}


