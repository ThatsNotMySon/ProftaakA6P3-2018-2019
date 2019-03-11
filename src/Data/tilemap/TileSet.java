package Data.tilemap;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.json.JsonObject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TileSet {

    private int firstgid;
    private String source;
    private int columns;
    private String image;
    private int imageHeight;
    private int imageWidth;
    private int margin;
    private String name;
    private int spacing;
    private int tileCount;
    private String tiledVersion;
    private int tileHeight;
    private int tileWidth;
    private String type;
    private double version;

    private ArrayList<Tile> tiles;

    public TileSet(JsonObject data){

        if(data.containsKey("source")) {
            firstgid = data.getInt("firstgid");
            source = data.getString("source");
            JsonIO reader = new JsonIO("resources/tilemaps/" + source);

            this.columns = reader.getIntFromTag("columns");
            this.image = reader.getStringFromTag("image");
            this.imageHeight = reader.getIntFromTag("imageheight");
            this.imageWidth = reader.getIntFromTag("imagewidth");
            this.margin = reader.getIntFromTag("margin");
            this.name = reader.getStringFromTag("name");
            this.spacing = reader.getIntFromTag("spacing");
            this.tileCount = reader.getIntFromTag("tilecount");
            this.tiledVersion = reader.getStringFromTag("tiledversion");
            this.tileHeight = reader.getIntFromTag("tileheight");
            this.tileWidth = reader.getIntFromTag("tilewidth");
            this.type = reader.getStringFromTag("type");
            this.version = reader.getDoubleFromTag("version");
        }
        else{
            this.columns = data.getInt("columns");
            this.firstgid = data.getInt("firstgid");
            this.image = data.getString("image");
            this.imageHeight = data.getInt("imageheight");
            this.imageWidth = data.getInt("imagewidth");
            this.margin = data.getInt("margin");
            this.name = data.getString("name");
            this.spacing = data.getInt("spacing");
            this.tileCount = data.getInt("tilecount");
            this.tileHeight = data.getInt("tileheight");
            this.tileWidth = data.getInt("tilewidth");
        }


            createTiles();

    }



    public void createTiles(){
        tiles = new ArrayList<>();

        BufferedImage tilesImage = null;
        try {
            tilesImage = ImageIO.read(this.getClass().getResource(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int stepSizeX = this.tileWidth;
        int stepSizeY = this.tileHeight;

        int tID = firstgid;
        for(int i =0; i < columns; i++){
            for(int j = 0; j < this.imageHeight/(tileHeight+spacing); j++){
                BufferedImage tileImage = tilesImage.getSubimage(stepSizeX*i,  stepSizeY*j,  stepSizeX, stepSizeY);
                tiles.add(new Tile(tID, tileImage));
                tID++;
            }
        }
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public BufferedImage getTileImage(int tileID){
        for (Tile tile : tiles) {
            if(tile.getTileID() == tileID){
                return tile.getImage();
            }
        }


        BufferedImage notFound = null;
        try {
            notFound = ImageIO.read(getClass().getResource("img/NotFound.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return notFound;
        }

        public boolean containsTile(int tileID){
            for (Tile tile : tiles) {
                if(tile.getTileID() == tileID){
                    return true;
                }
            }
            return false;
        }
    public int getFirstgid() {
        return firstgid;
    }

}

