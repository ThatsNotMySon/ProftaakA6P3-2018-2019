package simulation;

import java.awt.geom.Point2D;

public class Chair {
    private boolean reserved;
    private Point2D location;
    private boolean leftFacingChair = false;

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

    public boolean isReserved() {
        return reserved;
    }

    public Point2D getLocation() {
        return location;
    }
}
