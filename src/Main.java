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

        Timetable timetable = new Timetable();
        timetable.loadTimetableFromFile("src/Data/standaardLessen.txt");
        System.out.println(timetable);
    }

}
