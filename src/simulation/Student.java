package simulation;

import Data.DataController;
import Data.Group;
import Data.Lesson;

import Data.tilemap.TileMap;
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
    private TileMap tileMap;
    private Chair chair = null;
    private boolean hasAlreadyArrived = false;
    private boolean seated = false;

    //for testing purposes only
    public Student(Group group, DataController dataController, BufferedImage[] sprites, DijkstraMap dijkstra, double speed, TileMap tilemap){
        this(group, dataController);
        this.sprites = sprites;
        this.dijkstra = dijkstra;
        this.speed = speed;
        this.tileMap = tilemap;
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


    }


    @Override
    protected void arrivedAtDestination(double deltaTime) {
        if (!hasAlreadyArrived) {
            if (chair != null) {
                chair.leave();
            }

            this.chair = room.getEmptyChair();


            hasAlreadyArrived = true;
        }
        if (!seated) {
            Point2D target;
            target = this.chair.getLocation();


            Point2D nextLocation = new Point2D.Double(this.getLocation().getX() + deltaTime * speed * Math.cos(this.angle), this.getLocation().getY() + deltaTime * speed * Math.sin(this.angle));

            Point2D difference = new Point2D.Double(target.getX() * 16 - nextLocation.getX(), target.getY() * 16 - nextLocation.getY());
            double targetAngle = Math.atan2(difference.getY(), difference.getX());

            double differenceAngle = targetAngle - this.angle;


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
            if (difference.getY() < 5 && difference.getX() < 5) {
                setLocation(new Point2D.Double(target.getX()*16, target.getY()*16));
                System.out.println("seated");
              seated = true;
            }
        }
    }

    @Override
    public void chooseDestination(LocalTime time, Map<String,DijkstraMap> dijkstraMaps, Map<String,Room> roomMap ) {


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
        if (nextLesson.getStartTime().isAfter(time.plusMinutes(30)))
        {
            this.dijkstra = dijkstraMaps.get("Library");
            this.room = roomMap.get("Library");


        }
        else if(this.dijkstra != dijkstraMaps.get(nextLesson.getRoom().getName()))
        {
            this.arrived = false;
            this.hasAlreadyArrived = false;
            this.seated = false;
            System.out.println("Student going to lesson " + nextLesson.getSubject() + " in " + nextLesson.getRoom() + " at " + nextLesson.getStartTime());
        this.dijkstra = dijkstraMaps.get(nextLesson.getRoom().getName());

        this.room = roomMap.get(nextLesson.getRoom().getName());

        if(dijkstra == null) System.out.println("Error: no map found for " + nextLesson.getRoom().getName());}
        this.finalDestination = new Point2D.Double(dijkstra.getStartingTile().getxPos(),dijkstra.getStartingTile().getyPos());
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
