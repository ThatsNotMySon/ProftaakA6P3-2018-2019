package Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Timetable implements Serializable {

    private ArrayList<Lesson> lessons;
    private ArrayList<Room> rooms;
    private ArrayList<Group> groups;



    public Timetable(){
        this.lessons = new ArrayList<>();
        this.rooms = new ArrayList<>();
        this.groups = new ArrayList<>();
    }

    public Timetable(String filepath){
        this.lessons = new ArrayList<>();
        this.rooms = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.loadTimetableFromFile(filepath);
    }



    public void loadTimetableFromFile(String filepath){
        this.lessons.clear();
        File file = new File(filepath);

        LocalTime time = LocalTime.of(0,0);
        int duration = 0;
        String teacher = "";
        String subject = "";
        String roomName = "";
        int capacity = 0;
        String groupName = "";
        int amountOfStudents = 0;

        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("#");
            String firstLine = scanner.nextLine();
            if (firstLine.equals("Timetable")){
                while (scanner.hasNext()){
                    String line = scanner.nextLine();
                    String[] lineParts = line.split("#");
                    if (lineParts[0].equals("Lesson")){
                        String[] timeParts = lineParts[1].split(":");
                        time = LocalTime.of(Integer.parseInt(timeParts[0]),Integer.parseInt(timeParts[1]));
                        duration = Integer.parseInt(lineParts[2]);
                        teacher = lineParts[3];
                        subject = lineParts[4];
                    } else if (lineParts[0].equals("Room")){
                        roomName = lineParts[1];
                        capacity = Integer.parseInt(lineParts[2]);
                    } else if (lineParts[0].equals("Group")){
                        groupName = lineParts[1];
                        amountOfStudents = Integer.parseInt(lineParts[2]);
                    } else if (lineParts[0].equals("endLesson")){
                        addLesson(new Lesson(time, duration,teacher,subject, addRoom(roomName,capacity), addGroup(groupName, amountOfStudents)));
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void saveTimetableToFile(String filepath){
        File file = new File(filepath);

        String output = "Timetable";
        for (Lesson lesson : lessons){
            output += "\nLesson#" + "" + lesson.getStartTime() +
                    "#" + lesson.getDuration() +
                    "#" + lesson.getTeacher() +
                    "#" + lesson.getSubject() +
                    "#\nRoom#" + lesson.getRoom().getName() +
                    "#" + lesson.getRoom().getCapacity();

            for (Group group: lesson.getGroup()){
                output += "#\nGroup" +
                        "#" + group.getName() + '#' +
                        group.getAmountOfStudents() + "#";
            }
            output += "\nendLesson";
        }

        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.print(output);
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Room addRoom(String name, int capacity){

        for (Room room : rooms) {
            if(room.getName().equals(name)){
                return room;
            }
        }
        Room newRoom = new Room(name, capacity);
        this.rooms.add(newRoom);
        return newRoom;

    }

    public Group addGroup(String name, int amountOfStudents){
        for (Group group : groups) {
            if(group.getName().equals(name)){
                return group;
            }
        }

        Group newGroup = new Group(name, amountOfStudents);
        this.groups.add(newGroup);
        return newGroup;
    }


    @Override
    public String toString() {
        String output = "Timetable:" +
                "\n";
        for (Lesson lesson : lessons){
            int amountOfStudents = 0;


            output += "\nLesson: " + "startTime = " + lesson.getStartTime() +
                    ", duration = " + lesson.getDuration() +
                    ", teacher = " + lesson.getTeacher() +
                    ", subject = " + lesson.getSubject() +
                    "\nRoom: name = " + lesson.getRoom().getName() +
                    ", capacity = " + lesson.getRoom().getCapacity();

            for (Group group: lesson.getGroup()){
                output += group;
            }
            output += "\n";
        }
        return output;
    }






    //
    // Getters and Setters
    //

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public ArrayList<Room> getAllRooms(){
        return this.rooms;
    }

    public ArrayList<Group> getAllGroups(){
        return this.groups;
    }



    public void addLesson(Lesson lesson){
        lessons.add(lesson);
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }

    public void addGroup(Group groups) {
        this.groups.add(groups);
    }

    //
    // Removing
    //

    public boolean removeLesson(Lesson lesson){
        if(this.lessons.contains(lesson)) {
            this.lessons.remove(lesson);
            System.out.println("Lesson removed!");
            return true;
        }else{
            System.out.println("Lesson not found!");
            return false;
        }
    }

    public boolean removeRoom(Room room){
        if(this.rooms.contains(room)) {
            this.rooms.remove(room);
            System.out.println("Room removed!");
            return true;
        }else{
            System.out.println("Room not found!");
            return false;
        }
    }

    public boolean removeRoom(String name){
        for (Room room : rooms) {
            if(room.getName().equals(name)){
                System.out.println("Room " + room.getName() + " removed!");
                rooms.remove(room);
                return true;
            }
        }
        return false;
    }

    public boolean removeGroup(Group group){
        if(this.groups.contains(group)) {
            this.groups.remove(group);
            System.out.println("Group removed!");
            return true;
        }else{
            System.out.println("Group not found!");
            return false;
        }
    }

    public boolean removeGroup(String name){
        for (Group group : groups) {
            if(group.getName().equals(name)){
                System.out.println("Group " + group.getName() + " removed!");
                groups.remove(group);
                return true;
            }
        }
        return false;
    }

    public void clear(){
        this.lessons.clear();
        this.groups.clear();
        this.rooms.clear();
    }
}
