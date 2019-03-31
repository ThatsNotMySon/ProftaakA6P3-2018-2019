package simulation;

import simulation.simulationgui.ChooseLocationUpdate;

import java.time.LocalTime;

//Auteur :Sebastiaan
import java.time.LocalTime;

public class TimeControl {

    private int hours;
    private int minutes;

    private LocalTime time = LocalTime.of(6, 30);
    private double minutesPerSecond = 5;
    private double timer = 0;
    private boolean timeIsPaused;
    private ChooseLocationUpdate locationUpdate;

    public TimeControl(ChooseLocationUpdate locationUpdate)
    {
        this.locationUpdate = locationUpdate;
    }

        if(!timeIsPaused) {
            timer += deltaTime;
            if (timer > 1 / minutesPerSecond) {
                timer -= 1 / minutesPerSecond;
                time = time.plusMinutes(1);
            }
        }
    }


    /**
     * Auteur: Sebastiaan
     */
    public void update(double deltaTime){
        if(!timeIsPaused) {
            timer += deltaTime;
            if (timer > 1 / minutesPerSecond) {
                timer -= 1 / minutesPerSecond;
                time = time.plusMinutes(1);
            }
        }
        //Every 5 minutes on the clock, update targets for actors
        if (time.getMinute()%5 == 0)
        {
            locationUpdate.chooseLocations();
        }
    }

    /**
     * Auteur: Rümeysa
     */
    public void playPause(){
        this.timeIsPaused = !this.timeIsPaused;
    }

    public void setTime(){

    }

    public void setSpeedFactor(double factor){
        this.minutesPerSecond = factor;
    }

    public void setSpeedFactor(double factor){

        this.minutesPerSecond *= factor;
    }

    public void setNormalSpeed(){

        this.minutesPerSecond = 5;
    }

    public void setTime(){

    public double getHour(){
        return this.time.getHour();
    }

    public double getMinute(){
        return this.time.getMinute();
    }

    public LocalTime getTime() {return this.time;}
}

