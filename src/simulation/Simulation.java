package simulation;

import Data.DataController;
import Data.Group;
import Data.tilemap.TileMap;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.Resizable;
import simulation.pathfinding.DijkstraMap;
import simulation.pathfinding.PathFindingTile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Simulation implements Resizable {
    private BufferedImage[] sprites;
    private ArrayList<Actor> actors;
    private ArrayList<Location> locations;
    private TimeControl timeControl;
    private Clock clock;
    private TileMap tileMap;
    private Camera camera;
    private ArrayList<ArrayList<PathFindingTile>> pathFindingLists;
    private ArrayList<DijkstraMap> dijkstraMaps;
    private boolean showDirection = false;
    private Map dijkstraMaps = new HashMap();
    private ArrayList<Actor> spawnwaitlist;

    public Simulation(DataController dataController, Camera camera) {
        this.timeControl = new TimeControl();
        this.pathFindingLists = new ArrayList<>();
        this.dijkstraMaps = new ArrayList<>();
        this.clock = new Clock(timeControl);

        locations = new ArrayList<>();
        actors = new ArrayList<>();
        tileMap = new TileMap("resources/tilemaps/TI13-schoolSimulatieMapMetTiles-Versie4.5.json");
        this.camera = camera;
        createSprite();


        int tileSize = tileMap.getTileSize();

        for (ArrayList<Integer> coords : tileMap.getTargetsFromTargetLayer()){
            ArrayList<PathFindingTile> setPathFindingTiles = new ArrayList<>();
            for (int y = 0; y < 100; y++){
                for(int x = 0; x < 100; x++){
                    if (tileMap.tileIsWallInCollisionLayer(x,y)){
                        setPathFindingTiles.add(new PathFindingTile(tileSize, x * tileSize, y*tileSize, true));
                    } else {
                        setPathFindingTiles.add(new PathFindingTile(tileSize, x * tileSize, y*tileSize, false));
                    }
                }
            }

            DijkstraMap dijkstraMap = new DijkstraMap(setPathFindingTiles, tileMap.getWidth(), tileMap.getHeight(), tileSize, coords.get(0), coords.get(1));
            this.pathFindingLists.add(setPathFindingTiles);
            dijkstraMaps.add(dijkstraMap);
        }

        // TODO: 3/30/2019  loop through destinations
        //for(each destination received from tilemap {
        DijkstraMap dijkstraMap = new DijkstraMap(pathFindingTiles, tileMap.getWidth(), tileMap.getHeight(), tileSize, 24, 26);
        dijkstraMaps.put("LA666", dijkstraMap);
        //}


        System.out.println("Created " + actors.size() + " students in simulation");
        spawnwaitlist = new ArrayList<>();

        for (Group group : dataController.getAllGroups()) {
            System.out.println(group.getAmountOfStudents());
            for (int i = 0; i < group.getAmountOfStudents(); i++) {
                Student newStudent = new Student(group, dataController, sprites, dijkstraMap);
                boolean hasCollision = false;
                for (Actor a : actors)
                    if (a.hasCollision(newStudent))
                        hasCollision = true;
                        if (!hasCollision) {
                            actors.add(newStudent);

                        }else {
                            spawnwaitlist.add(newStudent);
                        }
            }


            }
        }


    public void draw(FXGraphics2D graphics) {
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(-20, -20, SimulationPane.WIDTH*2, SimulationPane.HEIGHT*2);
        graphics.setColor(Color.RED);

        graphics.setTransform(camera.getTransform( SimulationPane.WIDTH, SimulationPane.HEIGHT));

        tileMap.draw(graphics);

        for (Actor actor : actors) {
            actor.draw(graphics, showDirection);
             }
        this.clock.draw(graphics);

        graphics.setColor(Color.BLACK);


    }

    public void update(double deltaTime) {
        if(!spawnwaitlist.isEmpty()){

            for (Actor spawn : spawnwaitlist) {
                boolean hasCollision = false;
                for (Actor a : actors) {
                    if(a.hasCollision(spawn)){
                    hasCollision = true;
                    }
                }
                if (!hasCollision) {
                    actors.add(spawn);
                    spawnwaitlist.remove(spawn);
                }
            }
        }

        for (Actor actor : actors) {
            actor.update(deltaTime, actors);
        }
        timeControl.update(deltaTime);
    }

    public void playPause() {

        timeControl.playPause();

        for (Actor actor : actors) {

            actor.playPauseActor();
        }
    }

    public void showDirections(){
        this.showDirection = !this.showDirection;
    }

    public void setTime() {

        timeControl.setTime();
    }

    public void setSpeedFactor(double factor) {
        timeControl.setSpeedFactor(factor);
    }

    //Auteur: Sebastiaan
    public void createSprite() {

        BufferedImage sprite = null;
        try {
            sprite = ImageIO.read(getClass().getResource("/img/Wizards.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.sprites = new BufferedImage[40];
        for(int v = 0; v < 5; v++){
        for (int i = 0; i < 8; i++) {
            this.sprites[8*v+i] = sprite.getSubimage(32 * i, 32*v, 32, 32);
        }}
    }

    //Auteur: Marleen

    public void refresh(DataController dataController){
        System.out.println("refresh");
        dataController.getTimeTable();

        this.actors.clear();

        //Auteur: Mark | bug fix: time resets when refreshed
        this.timeControl = new TimeControl();
        this.clock = new Clock(timeControl);
        //

        for (Group group : dataController.getAllGroups()) {
            for (int i = 0; i < group.getAmountOfStudents(); i++) {
                Student newStudent = new Student(group, dataController, sprites, (DijkstraMap)dijkstraMaps.get("LA666"));
                boolean hasCollision = false;
                for (Actor a : actors)
                    if (a.hasCollision(newStudent))
                        hasCollision = true;
                if (!hasCollision)
                    actors.add(newStudent);
            }

        }
    }
}
