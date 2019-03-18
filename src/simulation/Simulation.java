package simulation;

import Data.Group;
import Data.Timetable;
import Data.DataController;
import javafx.animation.AnimationTimer;
import org.jfree.fx.FXGraphics2D;

import javax.imageio.ImageIO;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Simulation {
    private BufferedImage[] sprites;
    private ArrayList<Actor> actors;
    private ArrayList<Location> locations;
    private TimeControl timeControl;


    public Simulation(DataController dataController) {
        this.timeControl = new TimeControl();

        locations = new ArrayList<>();
        actors = new ArrayList<>();
        for (Group group : dataController.getAllGroups()) {
            System.out.println(group.getAmountOfStudents());
            for (int i = 0; i < group.getAmountOfStudents(); i++)
                actors.add(new Student(group));
        }
        System.out.println("Created " + actors.size() + " students in simulation");
        createSprite();
    }

    public void draw(Graphics2D graphics) {
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, 1200, 900);
        graphics.setColor(Color.RED);
        for (Actor actor : actors) {
            AffineTransform tx = new AffineTransform();
            tx.translate(actor.getLocation().getX()+16, actor.getLocation().getY()+16);
            tx.translate(-16,-16);
            graphics.drawImage(sprites[actor.getSpriteIndex()], tx, null);

        }


    }

    void update(double deltaTime) {
        for (Actor actor : actors) {
            actor.update(deltaTime);
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
}
