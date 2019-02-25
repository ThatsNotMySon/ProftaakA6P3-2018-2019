package simulation;

import Data.Group;
import Data.Timetable;
import Data.DataController;
import javafx.animation.AnimationTimer;
import org.jfree.fx.FXGraphics2D;

import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

public class Simulation
{
    private ArrayList<Actor> actors;
    private ArrayList<Location> locations;
    private TimeControl timeControl;


    public Simulation(DataController dataController){
        this.timeControl = new TimeControl();

        locations = new ArrayList<>();
        actors = new ArrayList<>();
        for(Group group: dataController.getAllGroups()){
            System.out.println(group.getAmountOfStudents());
            for (int i = 0; i < group.getAmountOfStudents(); i++)
            actors.add(new Student(group));
        }
        System.out.println("Created " + actors.size() + " students in simulation");

    }

    public void draw(Graphics2D graphics){
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0,0,1200,900);
        graphics.setColor(Color.RED);
        for(Actor actor: actors)
        {
            graphics.fill(new Rectangle2D.Double(actor.getLocation().getX(),actor.getLocation().getY(),15,15));
        }
    }

    void update(double deltaTime){
        for(Actor actor: actors)
        {
            actor.update(deltaTime);
        }
    }

    public void playPause(){
        timeControl.playPause();
    }

    public void setTime(){
        timeControl.setTime();
    }

    public void setSpeedFactor(){
        timeControl.setSpeedFactor();
    }
}
