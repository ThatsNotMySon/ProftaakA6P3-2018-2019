package Data;

import java.time.LocalTime;
import java.util.ArrayList;

public class DataController {

    private Timetable timeTable;

    public DataController(){

        this.timeTable = new Timetable("src/Data/standaardLessen.txt");
    }

    public DataController(Timetable timeTable) {
        this.timeTable = timeTable;
    }

    public Timetable getTimeTable() {
        return timeTable;
    }

    public void clear(){
        this.timeTable.clear();
    }

    public ArrayList<Lesson> getAllLessons(){
        return timeTable.getLessons();
    }

    public ArrayList<Room> getAllRooms(){
        return timeTable.getAllRooms();
    }

    public ArrayList<Group> getAllGroups(){
        return timeTable.getAllGroups();
    }

    public void addNewRoomToTimetable(String name, int capacity){
        timeTable.addRoom(new Room(name, capacity));
    }

    public void addNewGrouptoTimetable(String name, int amountOfStudents){
        timeTable.addGroup(new Group(name, amountOfStudents));
    }

    public void addNewLessonToTimetable(LocalTime startTime, int duration, String teacher, String subject, Room room, Group group){
        timeTable.addLesson(new Lesson(startTime,duration,teacher,subject,room,group));
    }

    public void deleteRoomFromTimetable(Room room){
        timeTable.removeRoom(room);
    }

    public void deleteGroupFromTimetable(Group group){
        timeTable.removeGroup(group);
    }

    public void deleteLessonFromTimetable(Lesson lesson){
        timeTable.removeLesson(lesson);
    }

    public ArrayList<String> getAllRoomNames(){
        ArrayList<String> roomNames = new ArrayList<>();
        for (Room room : timeTable.getAllRooms()){
            roomNames.add(room.getName());
        }
        return roomNames;
    }

    public ArrayList<String> getAllGroupNames(){
        ArrayList<String> groupNames = new ArrayList<>();
        for (Group group : timeTable.getAllGroups()){
            groupNames.add(group.getName());
        }
        return groupNames;
    }

    public ArrayList<String> getAllLessonSubjects(){
        ArrayList<String> subjectNames = new ArrayList<>();
        for (Lesson lesson : timeTable.getLessons()){
            subjectNames.add(lesson.getSubject());
        }
        return subjectNames;
    }

    public ArrayList<String> getAllTeachers(){
        ArrayList<String> teacherNames = new ArrayList<>();
        for (Lesson lesson : timeTable.getLessons()){
            teacherNames.add(lesson.getTeacher());
        }
        return teacherNames;
    }

    public ArrayList<LocalTime> getAllStartingTimes(){
        ArrayList<LocalTime> startingTimes = new ArrayList<>();
        for (Lesson lesson : timeTable.getLessons()){
            startingTimes.add(lesson.getStartTime());
        }
        return startingTimes;
    }

    public LocalTime endTimeCalculations(LocalTime startTime, int duration){

        int totalMinutes = (startTime.getHour() * 60) + startTime.getMinute() + duration;

        return LocalTime.of(totalMinutes/60, totalMinutes%60);
    }

    public boolean checkAvailableTime(String roomName, LocalTime startTime, int duration){
        for (Lesson lesson : timeTable.getLessons()){
            if (lesson.getRoom().getName().equals(roomName)){
                int startingMinutes = getMinutesOfLocalTime(lesson.getStartTime());
                int endingMinutes = startingMinutes + lesson.getDuration();
                int startingMinutesAvailable = getMinutesOfLocalTime(startTime);
                int endingMinutesAvailable = startingMinutesAvailable + duration;

                if (endingMinutesAvailable > startingMinutes && startingMinutesAvailable < endingMinutes) {
                    return false;
                }
            }
        }
        return true;
    }

    private int getMinutesOfLocalTime(LocalTime localTime){
        return (localTime.getHour()*60) + localTime.getMinute();
    }
}
