package simulation;

import javafx.geometry.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

abstract class Actor {

    Point2D position;
    Point2D destination;
    private double speed = 20;
    private double angle;
    BufferedImage sprite;
    double turnTimer;
    private final int animationStep = 0;

    /**
     * Auteur: Sebastiaan
     */
    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public Point2D getLocation() {
        return position;
    }

    public void setLocation(Point2D position) {
        this.position = position;
    }

    /**
     * Auteur: Marleen, Rümeysa, Tom & Sebastiaan
     * De som van this.setLocation is van Sebastiaan
     * De code voor beweging en angles is van Tom
     * De overige code over collision zijn gemaakt door Marleen en Rümeysa
     */
    public void update(double deltaTime, ArrayList<Actor> actors) {
        turnTimer += deltaTime;
        Point2D nextLocation = new Point2D(this.getLocation().getX() + deltaTime * speed * Math.cos(this.angle), this.getLocation().getY() + deltaTime * speed * Math.sin(this.angle));

        Boolean hasCollision = false;
        for (Actor act : actors) {
            if (act != this && act.hasCollision(nextLocation)) {
                hasCollision = true;
                break;
            }
        }
        Point2D difference = new Point2D(this.destination.getX() - nextLocation.getX(), this.destination.getY() - nextLocation.getY());
        double targetAngle = Math.atan2(difference.getY(), difference.getX());

        double differenceAngle = targetAngle - this.angle;
        if (!hasCollision) {

            this.setLocation(nextLocation);

            while (differenceAngle > Math.PI) {
                differenceAngle -= 2 * Math.PI;
            }
            while (differenceAngle < -Math.PI) {
                differenceAngle += 2 * Math.PI;
            }

            if (differenceAngle < -0.1) {
                this.angle -= 0.1;
            } else if (differenceAngle > 0.1) {
                this.angle += 0.1;
            } else {
                this.angle = targetAngle;
            }

//        if(turnTimer > 0.25){
//
//            this.angle += 0.1;
//        turnTimer=0;}
//        this.angle = this.angle%(2*Math.PI);
        } else {
            this.angle += 0.1;
        }
    }


    /**
     * Auteur: Sebastiaan
     */
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

    /**
     * Auteur: Marleen
     */
    public boolean hasCollision(Point2D otherPosition){
        return otherPosition.distance(position) < 32;
    }

    public boolean hasCollision(Actor otherPerson){
        return otherPerson.position.distance(position) < 32;
    }

    /**
     * Auteur: Rümeysa
     */
    public void playPauseActor() {

        if (this.speed != 0) {

            this.speed = 0;
        } else {

            this.speed = 20;
        }
    }
}
