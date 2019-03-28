package simulation;

import Data.Group;
import Data.DataController;
import Data.tilemap.Layer;
import Data.tilemap.TileMap;
import javafx.animation.AnimationTimer;
import javafx.scene.transform.Transform;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.Resizable;
import simulation.pathfinding.DijkstraMap;
import simulation.pathfinding.PathFindingTile;

import javax.imageio.ImageIO;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Simulation implements Resizable {
    private BufferedImage[] sprites;
    private ArrayList<Actor> actors;
    private ArrayList<PathFindingTile> pathFindingTiles;
    private ArrayList<Location> locations;
    private TimeControl timeControl;
    private TileMap tileMap;
    private Camera camera;

    public Simulation(DataController dataController, Camera camera) {
        this.timeControl = new TimeControl();

        locations = new ArrayList<>();
        actors = new ArrayList<>();
        tileMap = new TileMap("resources/tilemaps/TI13-schoolSimulatieMapMetTiles-Collision.json");
        pathFindingTiles = new ArrayList<>();
        this.camera = camera;
//        for (Group group : dataController.getAllGroups()) {
//            System.out.println(group.getAmountOfStudents());
//            for (int i = 0; i < group.getAmountOfStudents(); i++) {
//                Student newStudent = new Student(group, dataController);
//                boolean hasCollision = false;
//                for(Actor a : actors)
//                    if(a.hasCollision(newStudent))
//                        hasCollision = true;
//                if(!hasCollision)
//                    actors.add(newStudent);
//            }
//        }

        int tileSize = tileMap.getTileSize();

        for (int y = 0; y < 100; y++){
            for(int x = 0; x < 100; x++){
                if (tileMap.tileIsWallInCollisionLayer(x,y)){
                    pathFindingTiles.add(new PathFindingTile(tileSize, x * tileSize, y*tileSize, true));
                } else {
                    pathFindingTiles.add(new PathFindingTile(tileSize, x * tileSize, y*tileSize, false));
                }
            }
        }

        DijkstraMap dijkstraMap = new DijkstraMap(pathFindingTiles, tileMap.getWidth(), tileMap.getHeight(), tileSize, 24, 26);
        System.out.println("Created " + actors.size() + " students in simulation");
        createSprite();
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(-20, -20, SimulationPane.WIDTH*2, SimulationPane.HEIGHT*2);
        graphics.setColor(Color.RED);

        graphics.setTransform(camera.getTransform( SimulationPane.WIDTH, SimulationPane.HEIGHT));

        tileMap.draw(graphics);

        for (Actor actor : actors) {
            AffineTransform tx = new AffineTransform();
            tx.translate(actor.getLocation().getX()+16, actor.getLocation().getY()+16);
            tx.translate(-16,-16);
            graphics.drawImage(sprites[actor.getSpriteIndex()], tx, null);
            graphics.draw(new Line2D.Double(actor.getLocation().getX(), actor.getLocation().getY(), actor.destination.getX(), actor.destination.getY()));
            graphics.draw(new Line2D.Double(actor.getLocation().getX(), actor.getLocation().getY(), actor.getLocation().getX() + Math.cos(actor.getAngle()) * 10, actor.getLocation().getY() + Math.sin(actor.getAngle()) * 10));
        }

        graphics.setColor(Color.BLACK);
        for (PathFindingTile tile : pathFindingTiles){
            tile.draw(graphics);
        }


    }

    public void update(double deltaTime) {
        for (Actor actor : actors) {
            actor.update(deltaTime, actors);
        }
    }

    public void playPause() {
        timeControl.playPause();
    }

    public void setTime() {
        timeControl.setTime();
    }

    public void setSpeedFactor() {
        timeControl.setSpeedFactor();
    }


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
    }
}
