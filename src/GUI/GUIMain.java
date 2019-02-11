package GUI;

import Data.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Line2D;
import java.time.LocalTime;

public class GUIMain  extends Application {

    private Canvas canvas;
    private DataController dataController;

    private void onStart() {
        this.dataController = new DataController();
    }

    @Override
public void start(Stage primaryStage){

        this.onStart();

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
    int pixelVertical = (int)this.canvas.getHeight()/27;
    int pixelHorizontal = 0;
    int hours = 0;
    int minuten = 0;

    Timetable timetable = dataController.getTimeTable();

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
