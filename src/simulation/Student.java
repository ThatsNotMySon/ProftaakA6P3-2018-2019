package simulation;

import Data.DataController;
import Data.Group;
import Data.Lesson;

import simulation.pathfinding.DijkstraMap;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

public class Student extends Actor {

    private Group group;
    private DataController dataController;
    private ArrayList<Lesson> lessons;

    public Student(Group group, DataController dataController, BufferedImage[] sprites, DijkstraMap dijkstra){
        this(group, dataController);
        this.sprites = sprites;
        this.dijkstra = dijkstra;
    }

    public Student(Group group, DataController dataController)
    {
        this.lessons = new ArrayList<>();
        this.group = group;
        this.dataController = dataController;
        ArrayList<Lesson> allLessons = this.dataController.getAllLessons();

        for (Lesson lesson : allLessons) {
            for (Group group1 : lesson.getGroup()) {
                if (group1 == this.group) {
                    lessons.add(lesson);
                }
            }
        }

        this.position = new Point2D.Double(30*16, 97*16);

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

    public Data.Room nextRoom(LocalTime time){
        for (Lesson lesson : lessons) {
            if (lesson.getStartTime().minusMinutes(5) == time){
                return lesson.getRoom();
            }
        }
      return null;
    }
}
