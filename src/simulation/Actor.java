package simulation;

import javafx.geometry.Point2D;

import java.awt.image.BufferedImage;

abstract class Actor {

    private BufferedImage sprite;
    private Point2D location;

    public void update(){

    }


    public abstract void chooseDestination();

}
