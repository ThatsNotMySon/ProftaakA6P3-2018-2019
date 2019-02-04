package Data;

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

    public void loadTimetableFromFila(String filepath){

    }

    public void saveTimetableToFile(String filepath){

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
