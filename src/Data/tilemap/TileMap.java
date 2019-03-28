package Data.tilemap;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.awt.*;
import java.util.ArrayList;

public class TileMap {

    private JsonIO jsonIO;

    private int height;
    private boolean infinite;
    private ArrayList<Layer> layers;
    private int nextLayerID;
    private int nextObjectID;
    private String orientation;
    private String renderOrder;
    private String tiledVersion;
    private int tileHeight;
    private ArrayList<TileSet> tileSets;
    private int tileWidth;
    private String type;
    private double version;
    private int width;
    private Layer collision;
    private Layer target;

    public TileMap(String fileName) {
        JsonIO jsonIO = new JsonIO(fileName);

        this.jsonIO = jsonIO;
        this.height = jsonIO.getIntFromTag("height");
        this.infinite = jsonIO.getBooleanFromTag("infinite");


        this.nextLayerID = jsonIO.getIntFromTag("nextlayerid");
        this.nextObjectID = jsonIO.getIntFromTag("nextobjectid");
        this.orientation = jsonIO.getStringFromTag("orientation");
        this.renderOrder = jsonIO.getStringFromTag("renderorder");
        this.tiledVersion = jsonIO.getStringFromTag("tiledversion");
        this.tileHeight = jsonIO.getIntFromTag("tileheight");

        JsonArray tilesetsJsonArray = jsonIO.getJsonArrayFromTag("tilesets");
        ArrayList<TileSet> tileSetArrayList = new ArrayList<>();
        for (JsonValue jsonValue : tilesetsJsonArray){
            JsonObject jsonObject = (JsonObject) jsonValue;
            tileSetArrayList.add(new TileSet(jsonObject));

        }
        this.tileSets = tileSetArrayList;

        JsonArray layers = jsonIO.getJsonArrayFromTag("layers");
        ArrayList<Layer> layerArrayList = new ArrayList<>();
        for (JsonValue jsonValue : layers){
            JsonObject jsonObject = (JsonObject) jsonValue;
            Layer newLayer = new Layer(jsonObject);
            if (newLayer.isVisible()){
                newLayer.setTileSets(this.tileSets);
            }
            if (newLayer.getName().equalsIgnoreCase("collision")){
                this.collision = newLayer;
            } else if (newLayer.getName().equalsIgnoreCase("Lokaal")){
                this.target = newLayer;
            }
            layerArrayList.add(newLayer);

        }
        this.layers = layerArrayList;
        this.tileWidth = jsonIO.getIntFromTag("tilewidth");
        this.type = jsonIO.getStringFromTag("type");
        this.version = jsonIO.getDoubleFromTag("version");
        this.width = jsonIO.getIntFromTag("width");

    }
    public TileMap(JsonIO jsonIO) {

        this.jsonIO = jsonIO;
        this.height = jsonIO.getIntFromTag("height");
        this.infinite = jsonIO.getBooleanFromTag("infinite");

        JsonArray layers = jsonIO.getJsonArrayFromTag("layers");
        ArrayList<Layer> layerArrayList = new ArrayList<>();


        this.nextLayerID = jsonIO.getIntFromTag("nextlayerid");
        this.nextObjectID = jsonIO.getIntFromTag("nextobjectid");
        this.orientation = jsonIO.getStringFromTag("orientation");
        this.renderOrder = jsonIO.getStringFromTag("renderorder");
        this.tiledVersion = jsonIO.getStringFromTag("tiledversion");
        this.tileHeight = jsonIO.getIntFromTag("tileheight");

        JsonArray tilesetsJsonArray = jsonIO.getJsonArrayFromTag("tilesets");
        ArrayList<TileSet> tileSetArrayList = new ArrayList<>();
        for (JsonValue jsonValue : tilesetsJsonArray){

            JsonObject jsonObject = (JsonObject) jsonValue;
            tileSetArrayList.add(new TileSet(jsonObject));
        }
        this.tileSets = tileSetArrayList;

        for (JsonValue jsonValue : layers){
            JsonObject jsonObject = (JsonObject) jsonValue;
            Layer newLayer = new Layer(jsonObject);
            if (newLayer.isVisible()){
                newLayer.setTileSets(this.tileSets);
            }
            layerArrayList.add(newLayer);

        }
        this.layers = layerArrayList;

        this.tileWidth = jsonIO.getIntFromTag("tilewidth");
        this.type = jsonIO.getStringFromTag("type");
        this.version = jsonIO.getDoubleFromTag("version");
        this.width = jsonIO.getIntFromTag("width");

    }

    public void draw(Graphics2D g2d){
        for (Layer layer : layers) {
            if(layer.isVisible()){
                layer.draw(g2d);
            }
        }
    }

    public int getTileSize(){
        return tileWidth;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public Layer getCollisionLayer(){
        return this.collision;
    }

    public boolean tileIsWallInCollisionLayer(int x, int y){
        ArrayList<Integer> dataInCollision = getCollisionLayer().getData();
        int dataInTile = dataInCollision.get(y * this.width + x);
        return !(dataInTile == 0);
    }

    public Layer getTargetLayer(){
        return this.target;
    }
}
