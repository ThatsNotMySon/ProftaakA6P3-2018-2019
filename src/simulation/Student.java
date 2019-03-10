package simulation;

import Data.Group;
import javafx.geometry.Point2D;

import java.util.Random;

public class Student extends Actor {

    private Group group;

    public Student(Group group)
    {
        this.group = this.group;
        this.position = new Point2D(new Random().nextInt(1200),new Random().nextInt(900));

    }


    @Override
    public void chooseDestination() {

    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
