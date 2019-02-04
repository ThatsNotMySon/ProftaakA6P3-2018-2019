package Data;

import java.util.ArrayList;

public class Event {

    private Room room;
    private Lesson lesson;
    private ArrayList<Group> group;

    public Event(Room room, Group group, Lesson lesson) {
        this.room = room;
        this.lesson = lesson;
        this.group = new ArrayList<>();
        this.group.add(group);
    }

    public Event(Room room, ArrayList<Group> groups, Lesson lesson) {
        this.room = room;
        this.lesson = lesson;
        this.group = groups;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public ArrayList<Group> getGroup() {
        return group;
    }

    public void setGroup(ArrayList<Group> group) {
        this.group = group;
    }

    public void addGroup(Group group) {
        this.group.add(group);
    }

    public void removeGroup(Group group) {
        if (this.group.contains(group)){
            this.group.remove(group);
            System.out.println("Succesfully removed");
        } else {
            System.out.println("This group was not in this event");
        }

    }
}
