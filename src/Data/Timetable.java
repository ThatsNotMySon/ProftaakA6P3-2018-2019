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

    public Timetable(){
        this.lessons = new ArrayList<Lesson>();
    }

    public ArrayList<Room> getAllRooms(){
        ArrayList<Room> rooms = new ArrayList<>();
        for (Lesson lesson : lessons) {
            rooms.add(lesson.getRoom());
        }
        return rooms;
    }

    public void addLesson(Lesson lesson){
        lessons.add(lesson);
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
                        addLesson(new Lesson(time, duration,teacher,subject, new Room(roomName,capacity), new Group(groupName, amountOfStudents)));
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
}
