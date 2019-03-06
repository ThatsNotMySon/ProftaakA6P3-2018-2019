package simulation;

import javafx.geometry.Point2D;

import java.awt.image.BufferedImage;

abstract class Actor {

    Point2D position;
    private Point2D destination;
    private final double speed = 20;
    private double angle;
    BufferedImage sprite;
    double turnTimer;
    private final int animationStep = 0;

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public Point2D getLocation() {
        return position;
    }

    public void setLocation(Point2D position) {
        this.position = position;
    }



    public void update(double deltaTime){
        turnTimer += deltaTime;
        this.setLocation(new Point2D(this.getLocation().getX() +  deltaTime* speed * Math.cos(this.angle), this.getLocation().getY() +  deltaTime *speed * Math.sin(this.angle)));


        if(turnTimer > 0.25){

            this.angle += 0.1;
        turnTimer=0;}
        this.angle = this.angle%(2*Math.PI);
    }


    public abstract void chooseDestination();

    public double getAngle() {
        return angle;
    }


    //Auteur: Sebastiaan
    //Deze methode geeft de index van de sprite aan, afhankelijk van de huidige hoek
    //Dit heeft de indruk van een karakter dat de kant op kijkt waar het heen loopt.
    public int getSpriteIndex(){
        if(angle > 7*Math.PI/4 || angle <= Math.PI/4)return 5 + animationStep;
        else if(angle > Math.PI/4 && angle <= 3*Math.PI/4)return 1 + animationStep;
        else if(angle > 3*Math.PI/4 && angle <= 5*Math.PI/4) return 7 + animationStep;
        else return 3 + animationStep;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
