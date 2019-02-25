package simulation;

import javafx.geometry.Point2D;

import java.awt.image.BufferedImage;

abstract class Actor {
    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public Point2D getLocation() {
        return location;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }

    BufferedImage sprite;
    Point2D location;

    public void update(double deltaTime){

    }


    public abstract void chooseDestination();

}
