package simulation;

import Data.DataController;
import Data.Group;
import Data.Lesson;

import simulation.pathfinding.DijkstraMap;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;

public class Student extends Actor {

    private Group group;
    private DataController dataController;
    private ArrayList<Lesson> lessons;
    private int color = 0;

    //for testing purposes only
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
        color = (dataController.getAllGroups().indexOf(group)%5);
        System.out.println(color);


    }




    @Override
    public void chooseDestination(LocalTime time, Map<String,DijkstraMap> dijkstraMaps) {
        //get first lesson
        lessons.sort(new Comparator<Lesson>() {
            @Override
            public int compare(Lesson o1, Lesson o2) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        });
        Lesson nextLesson = lessons.get(0);

        //check if lesson already has passed - if so, take next lesson as target
        while(nextLesson.getEndTime().isBefore(time) && lessons.indexOf(nextLesson)+1 < lessons.size())
        {
            nextLesson = lessons.get(lessons.indexOf(nextLesson)+1);

        }
        if(this.dijkstra != dijkstraMaps.get(nextLesson.getRoom().getName()))
            System.out.println("Student going to lesson " + nextLesson.getSubject() + " in " + nextLesson.getRoom() + " at " + nextLesson.getStartTime());
        this.dijkstra = dijkstraMaps.get(nextLesson.getRoom().getName());
        if(dijkstra == null) System.out.println("Error: no map found for " + nextLesson.getRoom().getName());
    }


    //Gives students colors per group (max of 5 different)
    //TODO set proper formula
    @Override
    public int getSpriteIndex()
    {
        return super.getSpriteIndex() + color*8;
    }
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }


}
