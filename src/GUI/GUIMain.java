package GUI;

import Data.Group;
import Data.Lesson;
import Data.Room;
import Data.Timetable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.time.LocalTime;

public class GUIMain  extends Application {

    private Canvas canvas;

@Override
public void start(Stage primaryStage){

        this.canvas = new Canvas(1200  ,900);


        MenuBar menubar = new MenuBar();
        VBox vBox = new VBox(menubar);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(vBox);
        borderPane.setCenter(canvas);

        Scene scene = new Scene(borderPane);

        Menu file = new Menu("File");
        Menu rooms = new Menu("Rooms");
        Menu group = new Menu("Group");
        Menu simulation = new Menu("Simulation");

        menubar.getMenus().add(file);
 //       menubar.getMenus().add(rooms);
 //       menubar.getMenus().add(group);
    //      menubar.getMenus().add(simulation);

        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(scene);


        primaryStage.setTitle("Agenda");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) {
    int time = 300;
    int pixelVertical = 0;
    int pixelHorizontal = 0;
    int hours = 0;
    int minuten = 0;
    Timetable timetable = new Timetable();
        timetable.addLesson(new Lesson(LocalTime.of(9,30), 60, "Johan Talboom", "JavaFX", new Room("ld120", 15), new Group("A6", 6)));
        timetable.addLesson(new Lesson(LocalTime.of(10,30), 60, "Johan Talboom", "JavaFX", new Room("ld121", 15), new Group("A5", 6)));
        timetable.addLesson(new Lesson(LocalTime.of(11,30), 60, "Johan Talboom", "JavaFX", new Room("ld122", 15), new Group("A4", 6)));



        for (int i = 0; i < timetable.getAllRooms().size() ; i++) {
   //         graphics.draw(new Line2D.Double(i,0,i,900));
            pixelHorizontal = i* ((int)(this.canvas.getWidth()/timetable.getAllRooms().size())+1);
            graphics.drawString(timetable.getAllRooms().get(i).getName(), pixelHorizontal+50, 10);
            graphics.draw(new Line2D.Double(pixelHorizontal, 0,  pixelHorizontal, 900));
        }



        for (int i = 0; i <26; i++) {
            time = time + 30;
            pixelVertical = i*((int)this.canvas.getHeight()/26); //gedeeld door de maximum i


            if (time%60 == 0){
                hours = time/60;
                minuten = 00;
            } else {
                hours = time/60;
                minuten = 30;
            }

            graphics.draw(new Line2D.Double(0,pixelVertical,1200,pixelVertical));
            graphics.drawString(LocalTime.of(hours, minuten).toString(), 0, pixelVertical);
        }
    }
}
