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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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

        TabPane tabPane = new TabPane();

        Tab agendaTab = new Tab("Agenda");
        Tab tableTab = new Tab("Tabel");
        Tab roomTab = new Tab("Lokalen");
        Tab groupTab = new Tab("Klassen");
        Tab lessonTab = new Tab("Lessen");
        Tab simulationTab = new Tab("Simulatie");

        tabPane.getTabs().addAll(agendaTab, tableTab, roomTab, groupTab, lessonTab, simulationTab);

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(tabPane);
        borderPane.setCenter(canvas);

        Scene scene = new Scene(borderPane);

        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(scene);


        primaryStage.setTitle("Agenda");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) {
    int time = 300;
    int pixelVertical = (int)this.canvas.getHeight()/27;
    int pixelHorizontal = 0;
    int hours = 0;
    int minuten = 0;

    Timetable timetable = new Timetable();
        timetable.addLesson(new Lesson(LocalTime.of(9,30), 60, "Johan Talboom", "JavaFX", new Room("ld120", 15), new Group("A6", 6)));
        timetable.addLesson(new Lesson(LocalTime.of(10,30), 60, "Johan Talboom", "JavaFX", new Room("ld121", 15), new Group("A5", 6)));
        timetable.addLesson(new Lesson(LocalTime.of(11,30), 60, "Johan Talboom", "JavaFX", new Room("ld122", 15), new Group("A4", 6)));



        graphics.draw(new Line2D.Double(50, 0, 50, this.canvas.getHeight()));

        int whidthRoom = (int) (this.canvas.getWidth() - 50) / timetable.getAllRooms().size();

        for (int i = 0; i < timetable.getAllRooms().size()-1 ; i++) {
   //         graphics.draw(new Line2D.Double(i,0,i,900));
            pixelHorizontal = 50 + whidthRoom + whidthRoom*i;
            graphics.draw(new Line2D.Double(pixelHorizontal, 0,  pixelHorizontal, this.canvas.getHeight()));
        }


        for (int i = 0; i < timetable.getAllRooms().size(); i++) {
            graphics.drawString(timetable.getAllRooms().get(i).getName(), 75 + whidthRoom * i, pixelVertical-10);
        }



        for (int i = 0; i <27-1; i++) {
            time += 30;
            pixelVertical = ((int)this.canvas.getHeight()/27) + i*((int)this.canvas.getHeight()/27); //gedeeld door de maximum i

            if (time%60 == 0){
                hours = time/60;
                minuten = 00;
            } else {
                hours = time/60;
                minuten = 30;
            }

            graphics.draw(new Line2D.Double(0,pixelVertical,this.canvas.getWidth(),pixelVertical));
            graphics.drawString(LocalTime.of(hours, minuten).toString(), 0, pixelVertical+23);
        }
    }
}
