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
}
