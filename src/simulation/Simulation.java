package simulation;

import Data.Group;
import Data.DataController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

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
            for (int i = 0; i < group.getAmountOfStudents(); i++) {
                Student  newStudent = new Student(group);
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
