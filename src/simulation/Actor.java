package simulation;

import javafx.geometry.Point2D;

import java.awt.image.BufferedImage;
import java.util.Random;

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
    Point2D destination;
    Double speed;

    public void update(double deltaTime){
        if (this.location.getX() < this.destination.getX()) {
            this.location = new Point2D(this.location.getX() + this.speed, this.location.getY());
        }
        if (this.location.getX() > this.destination.getX()) {
            this.location = new Point2D(this.location.getX() - this.speed, this.location.getY());
        }
        if (this.location.getY() < this.destination.getY()) {
            this.location = new Point2D(this.location.getX(), this.location.getY() + this.speed);
        }
        if (this.location.getY() > this.destination.getY()) {
            this.location = new Point2D(this.location.getX(), this.location.getY() - this.speed);
        }
        if (this.location.distance(this.destination) < 5.0) {
            this.destination = new Point2D(new Random().nextInt(1200), new Random().nextInt(900));
        }
    }


    public abstract void chooseDestination();

}
