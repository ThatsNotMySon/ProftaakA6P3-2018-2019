package simulation;

import java.time.LocalTime;

public class TimeControl {

    private int hours;
    private int minutes;

    private LocalTime time = LocalTime.of(6, 30);
    private double minutesPerSecond = 5;
    private double timer = 0;
    private boolean timeIsPaused;

    public void update(double deltaTime){

        if(!timeIsPaused) {
            timer += deltaTime;
            if (timer > 1 / minutesPerSecond) {
                timer -= 1 / minutesPerSecond;
                time = time.plusMinutes(1);
            }
        }
    }

    /**
     * Auteur: Rümeysa
     */
    public void playPause(){

        if (this.timeIsPaused) {

            this.timeIsPaused = false;
            System.out.println("Play");
        } else {

            this.timeIsPaused = true;
            System.out.println("Pause");
        }
    }

    public void setSpeedFactor(double factor){

        this.minutesPerSecond *= factor;
    }

    public void setNormalSpeed(){

        this.minutesPerSecond = 5;
    }

    public void setTime(){

    }
}
