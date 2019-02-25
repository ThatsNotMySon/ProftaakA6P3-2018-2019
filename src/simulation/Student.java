package simulation;

import Data.Group;
import javafx.geometry.Point2D;

import java.util.Random;

public class Student extends Actor {

    private Group group;

    public Student(Group group)
    {
        this.group = this.group;
        this.location = new Point2D(new Random().nextInt(1200),new Random().nextInt(900));
        this.destination = new Point2D(600, 450);
        this.speed = 2.5;
        System.out.println(location.getX());
        System.out.println(location.getY());

    }


    @Override
    public void chooseDestination() {

    }
}
