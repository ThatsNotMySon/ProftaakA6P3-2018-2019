package simulation;

import Data.Group;
import Data.DataController;
import Data.tilemap.TileMap;
import javafx.animation.AnimationTimer;

import javax.imageio.ImageIO;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;

public class Simulation {
    private BufferedImage[] sprites;
    private ArrayList<Actor> actors;
    private ArrayList<Location> locations;
    private TimeControl timeControl;
    private Clock clock;
    private TileMap tileMap;
    private boolean showDirection = false;

    public Simulation(DataController dataController) {
        this.timeControl = new TimeControl();
        this.clock = new Clock(timeControl);

        locations = new ArrayList<>();
        actors = new ArrayList<>();
        tileMap = new TileMap("resources/tilemaps/TI1.3-tiledmap-poging1.1.json");
        createSprite();

        for (Group group : dataController.getAllGroups()) {
            System.out.println(group.getAmountOfStudents());
            for (int i = 0; i < group.getAmountOfStudents(); i++) {
                Student newStudent = new Student(group, dataController, sprites);
                boolean hasCollision = false;
                for(Actor a : actors)
                    if(a.hasCollision(newStudent))
                        hasCollision = true;
                if(!hasCollision)
                    actors.add(newStudent);
            }
        }

        System.out.println("Created " + actors.size() + " students in simulation");

    }

    public void draw(Graphics2D graphics) {
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, 1200, 900);
        graphics.setColor(Color.RED);

        tileMap.draw(graphics);

        for (Actor actor : actors) {
            actor.draw(graphics, showDirection);
             }
        this.clock.draw(graphics);

    }

    public void update(double deltaTime) {
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
                Student newStudent = new Student(group, dataController, sprites);
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
