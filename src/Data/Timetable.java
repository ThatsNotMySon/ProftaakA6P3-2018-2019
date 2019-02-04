package Data;

import java.io.Serializable;
import java.util.ArrayList;

public class Timetable implements Serializable {

    private ArrayList<Event> events;

    public Timetable(){
        this.events = new ArrayList<Event>();
    }

    public void addEvent(Event event){
        this.events.add(event);
    }

    public ArrayList<Room> getAllRooms(){
        ArrayList<Room> rooms = new ArrayList<>();
        for (Event event : events){
            rooms.add(event.getRoom());
        }
        return rooms;
    }

    public void loadTimetableFromFila(String filepath){

    }

    public void saveTimetableToFile(String filepath){

    }
}
