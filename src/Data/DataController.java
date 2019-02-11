package Data;

public class DataController {

    private Timetable timeTable;

    public DataController(){
        this.timeTable = new Timetable();
        this.timeTable.loadTimetableFromFile("src/Data/standaardLessen.txt");
    }

    public DataController(Timetable timeTable) {
        this.timeTable = timeTable;
    }

    public Timetable getTimeTable() {
        return timeTable;
    }


}
