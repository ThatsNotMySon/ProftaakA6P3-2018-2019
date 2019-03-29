package simulation;

public class TimeControl {

    private int hours;
    private int minutes;

    private boolean timeIsPaused;

    public void step(){

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

    public void setTime(){

    }

    public void setSpeedFactor(){

    }

    public void forward(){

    }

    public void reverse(){

    }
}
