package simulation;


import simulation.pathfinding.DijkstraMap;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract class Actor {

    Point2D position;
    Point2D destination;
    double speed = 30;
    protected double angle = 1.5*Math.PI;
    BufferedImage sprite;
    double turnTimer;
    private final int animationStep = 0;
    protected BufferedImage[] sprites;
    protected DijkstraMap dijkstra = null;
    private int collisionPerimeter = 16;
    protected boolean arrived = false;
    public Point2D finalDestination;
    public Room room;
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

        if (this.dijkstra.getValueFromTile((int)this.position.getX()/16,(int)this.position.getY()/16) <=2){
            this.arrived = true;
        }

        if (arrived){
            arrivedAtDestination(deltaTime);
        } else {
            this.destination = this.dijkstra.getDirection(this.position.getX(), this.position.getY());

            turnTimer += deltaTime;
            Point2D nextLocation = new Point2D.Double(this.getLocation().getX() + deltaTime * speed * Math.cos(this.angle), this.getLocation().getY() + deltaTime * speed * Math.sin(this.angle));

            Boolean hasCollision = false;
            for (Actor act : actors) {
                if (act != this && act.hasCollision(nextLocation)) {
                    hasCollision = true;
                    break;
                }
            }

            if (dijkstra.isLocationAWall(nextLocation.getX(), nextLocation.getY())) {
                hasCollision = true;
            }


            Point2D difference = new Point2D.Double(this.destination.getX() - nextLocation.getX(), this.destination.getY() - nextLocation.getY());
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

                if (differenceAngle < -0.3) {
                    this.angle -= 0.3;
                } else if (differenceAngle > 0.3) {
                    this.angle += 0.3;
                } else {
                    this.angle = targetAngle;
                }
                // keep angle in range 0 to 2pi
                angle += 2 * Math.PI;
                angle %= 2 * Math.PI;
//        if(turnTimer > 0.25){
//
//            this.angle += 0.1;
//        turnTimer=0;}
//        this.angle = this.angle%(2*Math.PI);
            } else {
                this.angle += 0.1;
            }
        }
    }

    protected abstract void arrivedAtDestination(double deltaTime);


    /**
     * Auteur: Sebastiaan
     */
    public abstract void chooseDestination(LocalTime time, Map<String, DijkstraMap> dijkstraMaps, Map<String, Room> roomMap);

    public double getAngle() {
        return angle;
    }



    /**
     * Auteur: Mark
     */


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
        return otherPosition.distance(position) < collisionPerimeter;
    }

    public boolean hasCollision(Actor otherPerson){
        return otherPerson.position.distance(position) < collisionPerimeter;
    }

    public boolean hasCollisionWithWall(Point2D position){
        return dijkstra.isTileAWall(position.getX()/16, position.getY()/16);
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


    public void draw(Graphics2D graphics, boolean showDirection) {
        if(showDirection){
            graphics.draw(new Line2D.Double(getLocation().getX(), getLocation().getY(), destination.getX(), destination.getY()));
            graphics.draw(new Line2D.Double(getLocation().getX(), getLocation().getY(), getLocation().getX() + Math.cos(getAngle()) * 10, getLocation().getY() + Math.sin(getAngle()) * 10));
        }
        this.draw(graphics);

    }

    public void draw(Graphics2D graphics) {
        AffineTransform tx = new AffineTransform();
        tx.translate(getLocation().getX() + 8, getLocation().getY() + 8);
        tx.translate(-16, -16);
        graphics.drawImage(sprites[getSpriteIndex()], tx, null);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
