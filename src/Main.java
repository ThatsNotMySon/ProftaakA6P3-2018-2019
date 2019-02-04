import Data.Group;
import Data.Lesson;
import Data.Room;
import Data.Timetable;
import GUI.GUIMain;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.time.LocalTime;

import static javafx.application.Application.launch;

public class Main {

    public static void main(String[] args) {
        launch(GUIMain.class);

        //Timetable timetable = new Timetable();
        //timetable.addLesson(new Lesson(LocalTime.of(9,30), 60, "Johan Talboom", "JavaFX", new Room("ld120", 15), new Group("A6", 6)));
        //timetable.addLesson(new Lesson(LocalTime.of(10,30), 60, "Johan Talboom", "JavaFX", new Room("ld121", 15), new Group("A5", 6)));
        //timetable.addLesson(new Lesson(LocalTime.of(11,30), 60, "Johan Talboom", "JavaFX", new Room("ld122", 15), new Group("A4", 6)));
        //System.out.println(timetable);
    }




}
