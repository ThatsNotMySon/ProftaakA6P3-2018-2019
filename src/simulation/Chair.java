package simulation;

import java.awt.geom.Point2D;

public class Chair {
    boolean reserved;
    Point2D location;

    public Chair(Point2D location){
        this.location = location;
        this.reserved = false;
    }

    public void reserve(){
        this.reserved = true;
    }

    public void leave(){
        this.reserved = false;
    }


}
