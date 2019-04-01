package Data.tilemap;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private ArrayList<Point2D> chairPositions = new ArrayList<>();
    private Map<String, RoomObject> rooms = null;

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
            if(jsonObject.getString("type").equals("tilelayer")){
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
        }
        this.layers = layerArrayList;


        this.tileWidth = jsonIO.getIntFromTag("tilewidth");
        this.type = jsonIO.getStringFromTag("type");
        this.version = jsonIO.getDoubleFromTag("version");
        this.width = jsonIO.getIntFromTag("width");

        for (JsonValue layer : layers) {
            JsonObject jsonObject = (JsonObject) layer;
            if (jsonObject.getString("type").equals("objectgroup")) {
                JsonArray roomsArray = jsonObject.getJsonArray("objects");

                for (JsonValue room : roomsArray) {
                    if (this.rooms == null) {
                        this.rooms = new HashMap<>();
                    }
                    JsonObject jo = (JsonObject) room;
                    RoomObject roomObject = new RoomObject(jo, this);

                    rooms.put(((JsonObject) room).getString("name"), roomObject);
                }
            }
        }


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

        for (JsonValue jsonValue : layers) {
            JsonObject jsonObject = (JsonObject) jsonValue;
            System.out.println(jsonObject.getString("type"));
            if (jsonObject.getString("type").equals("objectgroup")) {
                JsonArray roomsArray = jsonObject.getJsonArray("objects");

                for (JsonValue room : roomsArray) {
                    if (this.rooms == null) {
                        this.rooms = new HashMap<>();
                    }
                    JsonObject jo = (JsonObject) room;
                    RoomObject roomObject = new RoomObject(jo, this);

                    rooms.put(((JsonObject) room).getString("name"), roomObject);
                }
            }
            else{

                Layer newLayer = new Layer(jsonObject);
                if (newLayer.isVisible()) {
                    newLayer.setTileSets(this.tileSets);
                }

                layerArrayList.add(newLayer);
                this.layers = layerArrayList;

            }


        }
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

    public ArrayList<ArrayList<Integer>> getTargetsFromTargetLayer(){
        int counter = 0;
        ArrayList<Integer> dataList = this.target.getData();
        ArrayList<ArrayList<Integer>> xyList = new ArrayList<>();
        for (int i = 0 ; i < dataList.size(); i++){
            int data = dataList.get(i);
            if (!(data == 0)){
                ArrayList<Integer> coords = new ArrayList<>();
                coords.add(i%width);
                coords.add(i/width);
                coords.add(counter);
                counter++;
                xyList.add(coords);
            }
        }
        return xyList;
    }

    public ArrayList<Point2D> getChairPositions(){
        if (this.chairPositions.isEmpty()){
            ArrayList<Integer> data = layers.get(1).getData();
            for (int i = 0; i < data.size(); i++){
                int id = data.get(i);
                if (id == 150 || id == 151){

                    this.chairPositions.add(new Point2D.Double(i%this.width, Math.ceil((double)i/this.width)));

                }
            }
            return this.chairPositions;
        } else {
            return this.chairPositions;
        }
    }

    public ArrayList<Point2D> getChairPositionsFromBoundries(Point2D upperLeftCorner, Point2D lowerRightCorner){
        Rectangle2D rect = new Rectangle2D.Double(upperLeftCorner.getX(), upperLeftCorner.getY(), lowerRightCorner.getX() - upperLeftCorner.getX(),lowerRightCorner.getY() - upperLeftCorner.getY());
        ArrayList<Point2D> points = new ArrayList<>();
        for (Point2D point : getChairPositions()){
         //   System.out.println(point.getX() + " - "  + point.getY() + " | " +  ((Rectangle2D.Double) rect).x + " - " + ((Rectangle2D.Double) rect).width + " | " + ((Rectangle2D.Double) rect).y + " | " + ((Rectangle2D.Double) rect).width);
            if (rect.contains(point)){
                points.add(point);
            }
        }
        System.out.println(points.size());
        return points;
    }

    public RoomObject getRoomObject(String name){
        return rooms.get(name);
    }
}
