package GUI;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Person {
    /**
     * Auteur: Marleen
     */


    private Point2D position;
    private double angle;
    private double speed;
    Boolean hasCollision = false;

    public Person (Point2D position){
        this.position = position;
        this.angle = 0;
        this.speed+= Math.random();
        this.angle = Math.random()*2*Math.PI;
    }

    public void update(ArrayList<Person> persons){
        Point2D newPosition = new Point2D.Double(this.position.getX() + this.speed* Math.cos(angle),
                this.position.getY()+this.speed* Math.signum(angle));

        for (Person person : persons) {
            if (person != this && person.hasCollision(newPosition)){
                hasCollision = true;
                break;
            }
        }


    }

    public boolean hasCollision(Point2D otherPosition){
        return otherPosition.distance(position) < 16;
    }

    public boolean hasCollision(Person otherPerson){
        return otherPerson.position.distance(position) < 16;
    }
}
