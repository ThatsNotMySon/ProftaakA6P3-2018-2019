package simulation;

import Data.DataController;
import Data.Group;
import Data.tilemap.TileMap;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.Resizable;
import simulation.pathfinding.DijkstraMap;
import simulation.pathfinding.PathFindingTile;
import simulation.simulationgui.ChooseLocationUpdate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Simulation implements Resizable, ChooseLocationUpdate {
    private BufferedImage[] sprites;
    private ArrayList<Actor> actors;
    private ArrayList<Location> locations;
    private TimeControl timeControl;
    private Clock clock;
    private TileMap tileMap;
    private Camera camera;
    private ArrayList<ArrayList<PathFindingTile>> pathFindingLists;
    private ArrayList<DijkstraMap> dijkstraMapArrayList;
    private boolean showDirection = false;
    private Map<String, DijkstraMap> dijkstraMaps = new HashMap<String, DijkstraMap>();
    private ArrayList<Actor> spawnwaitlist;
    private boolean spedUp = false;
    private int speedfactor = 3;

    public Simulation(DataController dataController, Camera camera) {
        this.timeControl = new TimeControl(this);
        this.pathFindingLists = new ArrayList<>();
        this.dijkstraMapArrayList = new ArrayList<>();
        this.clock = new Clock(timeControl);

        locations = new ArrayList<>();
        actors = new ArrayList<>();
        tileMap = new TileMap("resources/tilemaps/TI13-schoolSimulatieMapMetTiles-Versie4.5.json");
        this.camera = camera;
        createSprite();


        int tileSize = tileMap.getTileSize();
        int roomCounter = 0;
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
            dijkstraMaps.put(dataController.getAllRooms().get(roomCounter).getName(), dijkstraMap);
            System.out.println(dijkstraMaps);
            roomCounter++;
//            this.pathFindingLists.add(setPathFindingTiles);
//            dijkstraMapArrayList.add(dijkstraMap);
        }

//        // TODO: 3/30/2019  loop through destinations
//        //for(each destination received from tilemap {
//        DijkstraMap dijkstraMap = new DijkstraMap(pathFindingTiles, tileMap.getWidth(), tileMap.getHeight(), tileSize, 24, 26);
//        dijkstraMapArrayList.put("LA666", dijkstraMap);
//        //}


        spawnwaitlist = new ArrayList<>();

        // TODO: fix students goeie dijkstraMap krijgen

        for (Group group : dataController.getAllGroups()) {
            System.out.println(group.getAmountOfStudents());
            for (int i = 0; i < group.getAmountOfStudents(); i++) {
                Student newStudent;
                newStudent = new Student(group, dataController, sprites, null, 30);
                newStudent.chooseDestination(timeControl.getTime(),dijkstraMaps);
                boolean hasCollision = false;
                for (Actor a : actors){
                    if (a.hasCollision(newStudent))
                        hasCollision = true;}
                if (!hasCollision) {
                    actors.add(newStudent);

                } else {
                    spawnwaitlist.add(newStudent);
                }
            }


        }

        System.out.println("Creating " + (actors.size() + spawnwaitlist.size()) + " students in simulation");

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
        ArrayList<Actor> spawnedList = new ArrayList<>();
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
                    spawnedList.add(spawn);
                }
            }
        }

        for(Actor spawn: spawnedList)
        {
            spawnwaitlist.remove(spawn);

        }

        for (Actor actor : actors) {
            actor.update(deltaTime, actors);
        }
        timeControl.update(deltaTime);
    }

    /**
     * Auteur: Rümeysa
     */
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

    /**
     * Auteur: Rümeysa
     */
    public void setSpeedFactor(double factor) {

        timeControl.setSpeedFactor(factor);

        for (Actor actor : actors) {

            actor.forward(factor);
        }
    }

    public void setNormalSpeed() {

        timeControl.setNormalSpeed();

        for (Actor actor : actors) {

            actor.setNormalSpeed();
        }
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
        this.timeControl = new TimeControl(this);
        this.clock = new Clock(timeControl);
        //
        for (Group group : dataController.getAllGroups()) {
            for (int i = 0; i < group.getAmountOfStudents(); i++) {
                Student newStudent = new Student(group, dataController);
                boolean hasCollision = false;
                for (Actor a : actors)
                    if (a.hasCollision(newStudent))
                        hasCollision = true;
                if (!hasCollision)
                    actors.add(newStudent);
            }
        }
        for(Actor actor: spawnwaitlist){
            actor.chooseDestination(now,dijkstraMaps);
        }
    }

    public void speedUp() {
        if (spawnwaitlist.isEmpty()) {
            this.spedUp = !this.spedUp;
            if (this.spedUp) {
                this.timeControl.setSpeedFactor(speedfactor);
                for (Actor actor : actors) {
                    actor.setSpeed(actor.getSpeed() * speedfactor);
                }
            } else {
                this.timeControl.setSpeedFactor(1);
                for (Actor actor : actors) {
                    actor.setSpeed(actor.getSpeed() / speedfactor);
                }
            }
        }
    }
}
