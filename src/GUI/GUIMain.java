package GUI;

import Data.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Line2D;
import java.time.LocalTime;
import java.util.ArrayList;

public class GUIMain  extends Application {

    private Canvas canvas;
    private DataController dataController;

    private void onStart() {
        this.dataController = new DataController();
    }

    @Override
public void start(Stage primaryStage){

        this.onStart();

        ArrayList<String> roomsArray = new ArrayList<>();
        ArrayList<Room> rooms = this.dataController.getTimeTable().getAllRooms();
        for (int i = 0; i < rooms.size(); i++) {
            roomsArray.add(rooms.get(i).getName());
        }
        ArrayList<String> groupsArray = new ArrayList<>();

        groupsArray.add(new Group("A6", 6).getName());
        groupsArray.add(new Group("B5", 6).getName());
        groupsArray.add(new Group("A3", 6).getName());


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
        
        ListView lessonGroupsListView = new ListView();
        ListView lessonRoomsListView = new ListView();

        lessonGroupsListView.getItems().addAll(groupsArray);
        lessonRoomsListView.getItems().addAll(roomsArray);

        TextField lessonTeacherInput = new TextField();
        TextField lessonSubjectInput = new TextField();
        TextField lessonStartTimeInput = new TextField();
        TextField lessonLengthTimeInput = new TextField();

        Label lessonTeacherLabel = new Label("Docent");
        Label lessonSubjectLabel = new Label("Vak");
        Label lessonStartTimeLabel = new Label("Begintijd");
        Label lessonLengthTimeLabel = new Label("Lengte");

        Button confirmLesson = new Button("Create Lesson");

        HBox hBoxLessonsTeacher = new HBox(lessonTeacherInput, lessonTeacherLabel);
        HBox hBoxLessonsSubject = new HBox(lessonSubjectInput, lessonSubjectLabel);
        HBox hBoxLessonsStartTime = new HBox(lessonStartTimeInput, lessonStartTimeLabel);
        HBox hBoxLessonsLengthTime = new HBox(lessonLengthTimeInput, lessonLengthTimeLabel);

        VBox vBoxLessonsInput = new VBox(hBoxLessonsTeacher, hBoxLessonsSubject, hBoxLessonsStartTime, hBoxLessonsLengthTime);

        VBox vBoxLessons = new VBox(vBoxLessonsInput, confirmLesson);
        HBox hBoxLessons = new HBox(lessonGroupsListView, lessonRoomsListView, vBoxLessons);
        vBoxLessons.setSpacing(5);
        hBoxLessons.setSpacing(25);

        Button openFile = new Button("Open File");
        Button saveFile = new Button("Save File");

        TableView tableViewTableTab = new TableView();

        TableColumn columnGroups = new TableColumn("Klas");
        TableColumn columnRooms = new TableColumn("Lokaal");
        TableColumn columnStartTime = new TableColumn("Starttijd");
        TableColumn columnEndTime = new TableColumn("Eindtijd");
        TableColumn columnLengthTime = new TableColumn("Lengte");

        tableViewTableTab.getColumns().addAll(columnGroups, columnRooms, columnStartTime, columnEndTime, columnLengthTime);

        HBox hBoxTableFiles = new HBox(openFile, saveFile);
        VBox vBoxTable = new VBox(hBoxTableFiles, tableViewTableTab);
        hBoxTableFiles.setSpacing(50);
        vBoxTable.setSpacing(25);


        BorderPane agendaPane = new BorderPane(canvas);
        BorderPane tablePane = new BorderPane(vBoxTable);
        BorderPane roomPane = new BorderPane(new Label("Lokaal"));
        BorderPane groupPane = new BorderPane(new Label("Klas"));
        BorderPane lessonPane = new BorderPane(hBoxLessons);
        BorderPane simulationPane = new BorderPane(new Label("Work in progress :)"));

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
