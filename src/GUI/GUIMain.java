package GUI;

import Data.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
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
        Scene scene = new Scene(borderPane);

        BorderPane agendaPane = new BorderPane(canvas);
        BorderPane tablePane = new BorderPane(new Label("Tabel"));
        BorderPane roomPane = new BorderPane(new Label("Lokaal"));
        GridPane groupPane = new GridPane();
        BorderPane lessonPane = new BorderPane(new Label("Les"));
        BorderPane simulationPane = new BorderPane(new Label("Work in progress :)"));

        Label nameClassLabel = new Label("Name class: ");
        Label amountOfStudentsLabel = new Label("Amount of students: ");

        TextField nameClassField = new TextField("class");
        TextField amountOfStudentsField = new TextField("amount");

        Button buttonAddClass = new Button("add");

        groupPane.add(nameClassLabel, 1, 1);
        groupPane.add(nameClassField, 2,1);
        groupPane.add(amountOfStudentsLabel, 1,2);
        groupPane.add(amountOfStudentsField, 2, 2);
        groupPane.add(buttonAddClass, 2,3);



        agendaTab.setContent(agendaPane);
        tableTab.setContent(tablePane);
        roomTab.setContent(roomPane);
        groupTab.setContent(groupPane);
        lessonTab.setContent(lessonPane);
        simulationTab.setContent(simulationPane);

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
    int minutes = 0;

    Timetable timetable = dataController.getTimeTable();

        graphics.draw(new Line2D.Double(50, 0, 50, this.canvas.getHeight()));

        int widthRoom = (int) (this.canvas.getWidth() - 50) / timetable.getAllRooms().size();

        for (int i = 0; i < timetable.getAllRooms().size()-1 ; i++) {
   //         graphics.draw(new Line2D.Double(i,0,i,900));
            pixelHorizontal = 50 + widthRoom + widthRoom*i;
            graphics.draw(new Line2D.Double(pixelHorizontal, 0,  pixelHorizontal, this.canvas.getHeight()));
        }


        for (int i = 0; i < timetable.getAllRooms().size(); i++) {
            graphics.drawString(timetable.getAllRooms().get(i).getName(), 75 + widthRoom * i, pixelVertical-10);
        }



        for (int i = 0; i <27-1; i++) {
            time += 30;
            pixelVertical = ((int)this.canvas.getHeight()/27) + i*((int)this.canvas.getHeight()/27); //gedeeld door de maximum i

            if (time%60 == 0){
                hours = time/60;
                minutes = 00;
            } else {
                hours = time/60;
                minutes = 30;
            }

            graphics.draw(new Line2D.Double(0,pixelVertical,this.canvas.getWidth(),pixelVertical));
            graphics.drawString(LocalTime.of(hours, minutes).toString(), 0, pixelVertical+23);
        }
    }
}
