package simulation;

import Data.Group;
import Data.DataController;
import Data.tilemap.TileMap;
import javafx.animation.AnimationTimer;
import org.jfree.fx.FXGraphics2D;

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

public class Simulation {
    private BufferedImage[] sprites;
    private ArrayList<Actor> actors;
    private ArrayList<Location> locations;
    private TimeControl timeControl;
    private TileMap tileMap;

    public Simulation(DataController dataController) {
        this.timeControl = new TimeControl();

        locations = new ArrayList<>();
        actors = new ArrayList<>();
        tileMap = new TileMap("resources/tilemaps/TI1.3-tiledmap-poging1.1.json");

        for (Group group : dataController.getAllGroups()) {
            System.out.println(group.getAmountOfStudents());
            for (int i = 0; i < group.getAmountOfStudents(); i++) {
                Student newStudent = new Student(group, dataController);
                boolean hasCollision = false;
                for(Actor a : actors)
                    if(a.hasCollision(newStudent))
                        hasCollision = true;
                if(!hasCollision)
                    actors.add(newStudent);
            }
        }

        System.out.println("Created " + actors.size() + " students in simulation");
        createSprite();
    }

    public void draw(Graphics2D graphics) {
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, 1200, 900);
        graphics.setColor(Color.RED);

        tileMap.draw(graphics);

        for (Actor actor : actors) {
            AffineTransform tx = new AffineTransform();
            tx.translate(actor.getLocation().getX()+16, actor.getLocation().getY()+16);
            tx.translate(-16,-16);
            graphics.drawImage(sprites[actor.getSpriteIndex()], tx, null);
            graphics.draw(new Line2D.Double(actor.getLocation().getX(), actor.getLocation().getY(), actor.destination.getX(), actor.destination.getY()));
            graphics.draw(new Line2D.Double(actor.getLocation().getX(), actor.getLocation().getY(), actor.getLocation().getX() + Math.cos(actor.getAngle()) * 10, actor.getLocation().getY() + Math.sin(actor.getAngle()) * 10));
        }
    }

    public void update(double deltaTime) {
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
