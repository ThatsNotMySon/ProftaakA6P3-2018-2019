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

    BufferedImage cacheImage = null;

    private ArrayList<TileSet> tileSets;

    public int[] getData() {
        int[] intData = new int[data.size()];

        for(int i = 0; i < data.size(); i++){

            int iData = data.getInt(i);
            if(data.getInt(i) >= 9999){
                iData = 353;
            }

            intData[i] = data.getInt(i);
        }

        return intData;
    }

    public int getWidth() {
        return width;
    }

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
        if(cacheImage == null)
        {
            cacheImage = new BufferedImage(width * 16, height * 16, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = cacheImage.createGraphics();

            System.out.println(this.getName());

            for(int i = 0; i < data.size(); i++) {

                int iData = data.getInt(i);
                if(iData >= 9999 || iData<-9999){
                    iData = 353;
                }

                int tile = iData;



                if (tile != 0) {
                    for (TileSet tileSet : tileSets) {
                        if (tileSet.getFirstgid() <= tile) {
                            if (tileSet.containsTile(tile)) {
                                AffineTransform tx = new AffineTransform();
                                tx.translate((i % width) * tileSet.getTileWidth(), (Math.floor(i / height) * tileSet.getTileHeight()));
                                g.drawImage(tileSet.getTileImage(tile), tx, null);
                                break;
                            }
                        }
                    }
                }
            }
        }



        g2d.drawImage(cacheImage, new AffineTransform(), null);


    }

    public void setTileSets(ArrayList<TileSet> tileSets){
     this.tileSets = tileSets;
    }

    public boolean isVisible(){
        return visible;
    }

    public String getName() {return name;}


}


