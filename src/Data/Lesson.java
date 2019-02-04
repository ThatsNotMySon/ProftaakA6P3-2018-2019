package Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Lesson implements Serializable {

    private LocalDateTime startTime;
    private int duration;
    private String teacher;
    private String subject;
    private Room room;
    private ArrayList<Group> group;


    public Lesson(LocalDateTime startTime, int duration, String teacher, String subject, Room room, ArrayList<Group> group) {
        this.startTime = startTime;
        this.duration = duration;
        this.teacher = teacher;
        this.subject = subject;
        this.room = room;
        this.group = group;
    }

    public Lesson(LocalDateTime startTime, int duration, String teacher, String subject, Room room, Group group) {
        this.startTime = startTime;
        this.duration = duration;
        this.teacher = teacher;
        this.subject = subject;
        this.room = room;
        this.group = new ArrayList<>();
        this.group.add(group);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "startTime=" + startTime +
                ", duration=" + duration +
                ", teacher='" + teacher + '\'' +
                ", subject='" + subject + '\'' +
                ", room=" + room +
                ", group=" + group +
                '}';
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
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


}
