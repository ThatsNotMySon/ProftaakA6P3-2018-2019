package simulation;

import Data.Group;
import Data.Timetable;
import Data.DataController;
import Data.tilemap.TileMap;
import javafx.animation.AnimationTimer;
import org.jfree.fx.FXGraphics2D;

import javax.imageio.ImageIO;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
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
    private TileMap tileMap;



    public Simulation(DataController dataController) {
        this.timeControl = new TimeControl();

        locations = new ArrayList<>();
        actors = new ArrayList<>();
        tileMap = new TileMap("resources/tilemaps/TI13-schoolSimulatieMapMetTiles-Collision.json");


        for (Group group : dataController.getAllGroups()) {

            for (int i = 0; i < group.getAmountOfStudents(); i++)
                actors.add(new Student(group));
        }


        createSprite();
    }

    public void draw(Graphics2D graphics) {
        graphics.setBackground(Color.BLACK);
        graphics.clearRect(0, 0, 1200, 900);
        graphics.setColor(Color.RED);

        tileMap.draw(graphics);

        for (Actor actor : actors) {
            AffineTransform tx = new AffineTransform();
            tx.translate(actor.getLocation().getX()+16, actor.getLocation().getY()+16);
            tx.translate(-16,-16);
            graphics.drawImage(sprites[actor.getSpriteIndex()], tx, null);
            graphics.draw(new Ellipse2D.Double(actor.getLocation().getX(), actor.getLocation().getY(), 10,10));
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
            sprite = ImageIO.read(getClass().getResource("/img/WhiteWizard.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.sprites = new BufferedImage[10];
        for (int i = 0; i < this.sprites.length; i++) {
            this.sprites[i] = sprite.getSubimage(32 * i, 0, 32, 32);
        }
    }
}
