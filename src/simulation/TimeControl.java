package simulation;


import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.*;
import java.time.LocalTime;

//Auteur :Sebastiaan
public class TimeControl {

    private LocalTime time =  LocalTime.of(6,30);
    private double minutesPerSecond = 5;
    private double timer = 0;
    private boolean timeIsPaused;

    public void TimeControl(){

    }


    public void update(double deltaTime){
        if(!timeIsPaused) {
            timer += deltaTime;
            if (timer > 1 / minutesPerSecond) {
                timer -= 1 / minutesPerSecond;
                time = time.plusMinutes(1);
            }
        }
    }

    public void playPause(){
        this.timeIsPaused = !this.timeIsPaused;
    }

    public void setTime(){

    }

    public void setSpeedFactor(double factor){
        this.minutesPerSecond = factor;
    }

    public void forward(){

    }

    public void reverse(){

    }


    public double getHour(){
        return this.time.getHour();
    }

    public double getMinute(){
        return this.time.getMinute();
    }
}
