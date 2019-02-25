package simulation;

import javafx.animation.AnimationTimer;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.util.ArrayList;

public class Simulation
{
    private ArrayList<Actor> actors;
    private ArrayList<Location> locations;
    private TimeControl timeControl;

    public Simulation(){
        this.timeControl = new TimeControl();


    }

    public void draw(Graphics2D graphics){

    }

    private void update(){

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
