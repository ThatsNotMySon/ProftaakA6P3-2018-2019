package Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;

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

        

    }

    public void saveTimetableToFile(String filepath){
        File file = new File(filepath);

        String output = "Timetable" +
                "\n";
        for (Lesson lesson : lessons){
            output += "\nLesson#" + "startTime#" + lesson.getStartTime() +
                    "#duration#" + lesson.getDuration() +
                    "#teacher#" + lesson.getTeacher() +
                    "#subject#" + lesson.getSubject() +
                    "#\nRoom#name#" + lesson.getRoom().getName() +
                    "#capacity#" + lesson.getRoom().getCapacity();

            for (Group group: lesson.getGroup()){
                output += "#\nGroup" +
                        "#name#" + group.getName() + '#' +
                        "#amountOfStudents#" + group.getAmountOfStudents() + "#";
            }
            output += "\n";
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
