package GUI;

import Data.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;

public class GUIMain extends Application {


    private Canvas canvas;
    private DataController dataController;
    private Timetable timetable;
    private Point2D offset;
    private ArrayList<LessonBlock> lessonBlocks;
    private DraggedBlock dragged;
    private ArrayList<Lesson> lessons;

    private void onStart() {
        this.dataController = new DataController();
    }

    @Override
    public void start(Stage primaryStage) {

        this.onStart();
        
        this.canvas = new Canvas(1200  ,900);
        timetable = dataController.getTimeTable();
        lessonBlocks = new ArrayList<>();
        dragged = null;
        lessons = dataController.getAllLessons();

        TableView tableViewTableTab = new TableView();
        ObservableList<Lesson> tableData = FXCollections.observableArrayList(lessons);

        ListView listGroups = new ListView();
        ListView listRooms = new ListView();

        this.createLessonBlocks();
//Auteur : Sebastiaan
        canvas.setOnMousePressed(e ->
        {
            for (LessonBlock lessonBlock : lessonBlocks) {
                if (lessonBlock.getTransformedShape().contains(e.getX(), e.getY())) {
                    dragged = lessonBlock.getDraggedBlock();
                    offset = new Point2D(lessonBlock.getPosition().getX() - e.getX(), lessonBlock.getPosition().getY() - e.getY());
                }
            }

        });

        canvas.setOnMouseReleased(e -> {

            if(dragged != null){

            LocalTime newTime = getTimeAtMouse(offset.add(new Point2D(e.getX(),e.getY())));
            Room newRoom = getRoomAtMouse(offset.add(new Point2D(e.getX(),e.getY())));
                if(!dataController.checkAvailableTime(newRoom.getName(),newTime,dragged.getLesson().getDuration()))
                    System.out.println("No free time found");
            if(dataController.checkAvailableTime(newRoom.getName(),newTime,dragged.getLesson().getDuration()))
                    //!dragged.getLesson().getStartTime().equals(newTime) && !dragged.getLesson().getRoom().equals(newRoom))
            {
                System.out.println("Free time found!");
                dragged.getLesson().setStartTime(newTime);
                dragged.getLesson().setRoom(newRoom);
                System.out.println(dragged.getLesson());
                createLessonBlocks();
                tableData.removeAll(lessons);
                tableData.addAll(lessons);
                tableViewTableTab.setItems(tableData);
            }}
            dragged = null;
            draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        });

        canvas.setOnMouseDragged(e ->
        {
            Point2D position = new Point2D(e.getX(), e.getY());
            if (dragged != null) {
                dragged.setPosition(offset.add(position));
            }
            draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        });


        TabPane tabPane = new TabPane();

        Tab agendaTab = new Tab("Agenda");
        Tab tableTab = new Tab("Table");
        Tab roomTab = new Tab("Rooms");
        Tab groupTab = new Tab("Groups");
        Tab lessonTab = new Tab("Lesson");
        Tab simulationTab = new Tab("Simulation");

        tabPane.getTabs().addAll(agendaTab, tableTab, roomTab, groupTab, lessonTab, simulationTab);

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(tabPane);
        Scene scene = new Scene(borderPane);

        /*
        De volgende code hoort bij het tabje Lesson
        * Als het stuk is moet je bij Tom zijn
        * */
        
        ListView lessonGroupsListView = new ListView();
        ListView lessonRoomsListView = new ListView();

        TextField lessonTeacherInput = new TextField();
        TextField lessonSubjectInput = new TextField();
        TextField lessonStartTimeInput = new TextField();
        TextField lessonStartTimeInput2 = new TextField();
        TextField lessonLengthTimeInput = new TextField();

        Label lessonTeacherLabel = new Label("Teacher");
        Label lessonSubjectLabel = new Label("Subject");
        Label lessonStartTimeLabel = new Label("Start time");
        Label lessonLengthTimeLabel = new Label("Length");
        Label errorLabel = new Label("");

        Button confirmLesson = new Button("Create Lesson");

        HBox hBoxLessonsTeacher = new HBox(lessonTeacherInput, lessonTeacherLabel);
        HBox hBoxLessonsSubject = new HBox(lessonSubjectInput, lessonSubjectLabel);
        HBox hBoxLessonsStartTime = new HBox(lessonStartTimeInput, lessonStartTimeInput2, lessonStartTimeLabel);
        HBox hBoxLessonsLengthTime = new HBox(lessonLengthTimeInput, lessonLengthTimeLabel);

        VBox vBoxLessonsInput = new VBox(hBoxLessonsTeacher, hBoxLessonsSubject, hBoxLessonsStartTime, hBoxLessonsLengthTime, errorLabel);

        VBox vBoxLessons = new VBox(vBoxLessonsInput, confirmLesson);
        HBox hBoxLessons = new HBox(lessonGroupsListView, lessonRoomsListView, vBoxLessons);
        vBoxLessons.setSpacing(5);
        hBoxLessons.setSpacing(25);

        Button openFile = new Button("Open File");
        Button saveFile = new Button("Save File");

        TableColumn columnGroups = new TableColumn("Group");
        TableColumn columnRooms = new TableColumn("Room");
        TableColumn columnStartTime = new TableColumn("Start time");
        TableColumn columnLengthTime = new TableColumn("Length");

        /*
        De volgende code hoort bij het tabje table
        Als het stuk is moet je bij Rümeysa en Tom zijn
         */

        columnGroups.setCellValueFactory(new PropertyValueFactory<Lesson, Group>("Group"));
        columnRooms.setCellValueFactory(new PropertyValueFactory<Lesson, Room>("Room"));
        columnStartTime.setCellValueFactory(new PropertyValueFactory<Lesson, LocalTime>("startTime"));
        columnLengthTime.setCellValueFactory(new PropertyValueFactory<Lesson, Integer>("duration"));

        tableViewTableTab.setItems(tableData);
        tableViewTableTab.getColumns().addAll(columnGroups, columnRooms, columnStartTime, columnLengthTime);

        JFileChooser chooser = new JFileChooser();
        openFile.setOnAction(e -> {
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == chooser.APPROVE_OPTION) {
                File fileChosen = chooser.getSelectedFile();
                tableData.removeAll(lessons);
                this.dataController.getTimeTable().loadTimetableFromFile(fileChosen.getAbsolutePath());
                tableData.addAll(lessons);
                tableViewTableTab.setItems(tableData);
                listGroups.getItems().clear();
                listGroups.getItems().addAll(this.dataController.getTimeTable().getAllGroups());
                listRooms.getItems().clear();
                listRooms.getItems().addAll(this.dataController.getTimeTable().getAllRooms());
                lessonGroupsListView.getItems().clear();
                lessonGroupsListView.getItems().addAll(this.dataController.getTimeTable().getAllGroups());
                lessonRoomsListView.getItems().clear();
                lessonRoomsListView.getItems().addAll(this.dataController.getTimeTable().getAllRooms());
                createLessonBlocks();
                draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
            }
        });

        saveFile.setOnAction(e -> {
            int returnVal = chooser.showSaveDialog(null);
            if (returnVal == chooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                this.dataController.getTimeTable().saveTimetableToFile(file.getAbsolutePath());
            }
        });

        HBox hBoxTableFiles = new HBox(openFile, saveFile);
        VBox vBoxTable = new VBox(hBoxTableFiles, tableViewTableTab);
        hBoxTableFiles.setSpacing(50);
        vBoxTable.setSpacing(25);

        /*
        * De volgende code laat de knoppen op de Lesson Tab
        * Als deze code stuk is moet je bij Marleen zijn.
        * Voor het tekengedeelde moet je bij Sebastiaan zijn.
        * */

        lessonGroupsListView.getItems().addAll(this.dataController.getTimeTable().getAllGroups());
        lessonRoomsListView.getItems().addAll(this.dataController.getTimeTable().getAllRooms());

        confirmLesson.setOnAction(event -> {
            try {
                if(lessonRoomsListView.getSelectionModel().getSelectedItem() != null && lessonGroupsListView.getSelectionModel().getSelectedItem() != null &&
                        lessonStartTimeInput != null && lessonStartTimeInput2 != null & lessonLengthTimeInput != null && lessonTeacherInput != null && lessonSubjectInput != null) {
                    errorLabel.setText("");
                    Lesson lessonToAdd = new Lesson((LocalTime.of(Integer.parseInt(
                            lessonStartTimeInput.getText()), Integer.parseInt(lessonStartTimeInput2.getText()))),
                            Integer.parseInt(lessonLengthTimeInput.getText()), lessonTeacherInput.getText(),
                            lessonSubjectInput.getText(), (Room) lessonRoomsListView.getSelectionModel().getSelectedItem(),
                            (Group) lessonGroupsListView.getSelectionModel().getSelectedItem());

                    if (dataController.checkAvailableTime(lessonToAdd.getRoom().getName(), lessonToAdd.getStartTime(), lessonToAdd.getDuration())) {
                        this.dataController.getTimeTable().addLesson(lessonToAdd);
                        tableData.add(lessons.get(lessons.size() - 1));
                        tableViewTableTab.setItems(tableData);
                        createLessonBlocks();
                        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
                        System.out.println("Lesson added");
                    } else {
                        errorLabel.setText("Room not available at selected time");
                    }
                } else {
                    errorLabel.setText("Check input");
                }

            } catch (Exception e) {
                errorLabel.setText("Check input");
                e.printStackTrace();

            }

        });

        /*
        Meer algemene code
        * Als deze code stuk is moet je bij Marleen en Rümeysa zijn
        * */

        BorderPane agendaPane = new BorderPane(canvas);
        BorderPane tablePane = new BorderPane(vBoxTable);
        GridPane roomPane = new GridPane();
        GridPane groupPane = new GridPane();
        BorderPane lessonPane = new BorderPane(hBoxLessons);
        BorderPane simulationPane = new BorderPane(new Label("Work in progress :)"));

        /*
        Deze code hoogt bij het tabje Class
        * Als deze code stuk is moet je bij Marleen en Rümeysa zijn
        * */

        Label nameClassLabel = new Label("Name class: ");
        Label amountOfStudentsLabel = new Label("Amount of students: ");

        TextField nameClassField = new TextField("class");
        TextField amountOfStudentsField = new TextField("amount");

        Button buttonAddClass = new Button("Add class");
        Button buttonDeleteClass = new Button("Delete Class");

        groupPane.add(nameClassLabel, 1, 1);
        groupPane.add(nameClassField, 2, 1);
        groupPane.add(amountOfStudentsLabel, 1, 2);
        groupPane.add(amountOfStudentsField, 2, 2);
        groupPane.add(buttonAddClass, 2, 3);
        groupPane.add(listGroups, 1, 4);
        groupPane.add(buttonDeleteClass, 1, 5);

        /*
         * De volgende code laat de knoppen op de Lesson Tab
         * Als deze code stuk is moet je bij Marleen zijn
         * */

        listGroups.getItems().addAll(this.dataController.getTimeTable().getAllGroups());

        buttonAddClass.setOnAction(event -> {
            try {
                this.dataController.getTimeTable().addGroup(new Group(nameClassField.getText(), Integer.parseInt(amountOfStudentsField.getText())));
                listGroups.getItems().clear();
                listGroups.getItems().addAll(this.dataController.getTimeTable().getAllGroups());
                lessonGroupsListView.getItems().clear();
                lessonGroupsListView.getItems().addAll(this.dataController.getTimeTable().getAllGroups());
            } catch (Exception e){
                System.out.println("Please input valid value");
                e.printStackTrace();
            }
        });

        buttonDeleteClass.setOnAction(event -> {
            listGroups.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            this.dataController.getTimeTable().removeGroup((Group) listGroups.getSelectionModel().getSelectedItem());
            listGroups.getItems().clear();
            listGroups.getItems().addAll(this.dataController.getTimeTable().getAllGroups());
            lessonGroupsListView.getItems().clear();
            lessonGroupsListView.getItems().addAll(this.dataController.getTimeTable().getAllGroups());
        });

        /*
        De volgende code hoort bij het tabje Room
        * Als deze code stuk is moet je bij Marleen en Rümeysa zijn
        * */

        Label nameRoomLabel = new Label("Room name: ");
        TextField nameRoom = new TextField("LA111");

        Label capacityRoomLabel = new Label("Capacity Room");
        TextField capacityRoom = new TextField("0");

        Button addRoom = new Button("Add Room");
        Button deleteRoom = new Button("Delete Room");

        roomPane.add(nameRoomLabel, 1, 1);
        roomPane.add(nameRoom, 2, 1);
        roomPane.add(capacityRoomLabel, 1, 2);
        roomPane.add(capacityRoom, 2, 2);
        roomPane.add(addRoom, 2, 3);
        roomPane.add(listRooms, 1, 4);
        roomPane.add(deleteRoom, 1, 5);

        listRooms.getItems().addAll(this.dataController.getTimeTable().getAllRooms());

        addRoom.setOnAction(event -> {
            this.dataController.getTimeTable().addRoom(new Room(nameRoom.getText(), Integer.parseInt(capacityRoom.getText())));
            listRooms.getItems().clear();
            listRooms.getItems().addAll(this.dataController.getTimeTable().getAllRooms());
            lessonRoomsListView.getItems().clear();
            lessonRoomsListView.getItems().addAll(this.dataController.getTimeTable().getAllRooms());
        });

        deleteRoom.setOnAction(event -> {
            listRooms.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

            this.dataController.getTimeTable().removeRoom((Room) listRooms.getSelectionModel().getSelectedItem());
            listRooms.getItems().clear();
            listRooms.getItems().addAll(this.dataController.getTimeTable().getAllRooms());
            lessonRoomsListView.getItems().clear();
            lessonRoomsListView.getItems().addAll(this.dataController.getTimeTable().getAllRooms());
        });

        /*Meer algeme code
        * Als deze code stuk is moet je bij Marleen en Rümeysa*/

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

        /*
        De volgende code tekent het rooster template
         * Als deze code stuk is moet je bij Marleen zijn of huilen.
          * */

        int time = 300;
        int pixelVertical = (int)this.canvas.getHeight()/27;
        int pixelHorizontal = 0;
        int hours = 0;
        int minutes = 0;

    Timetable timetable = dataController.getTimeTable();
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, (int) canvas.getHeight() * 2, (int) canvas.getWidth() * 2);


//Auteur: Marleen
        //Hieronder wordt het rooster getekend
        graphics.draw(new Line2D.Double(50, 0, 50, this.canvas.getHeight()));

        int widthRoom = (int) (this.canvas.getWidth() - 50) / timetable.getAllRooms().size();

        for (int i = 0; i < timetable.getAllRooms().size() - 1; i++) {
            //         graphics.draw(new Line2D.Double(i,0,i,900));
            pixelHorizontal = 50 + widthRoom + widthRoom * i;
            graphics.draw(new Line2D.Double(pixelHorizontal, 0, pixelHorizontal, this.canvas.getHeight()));
        }


        for (int i = 0; i < timetable.getAllRooms().size(); i++) {
            graphics.drawString(timetable.getAllRooms().get(i).getName(), 75 + widthRoom * i, pixelVertical - 10);
        }


        for (int i = 0; i < 27 - 1; i++) {
            time += 30;
            pixelVertical = ((int) this.canvas.getHeight() / 27) + i * ((int) this.canvas.getHeight() / 27); //gedeeld door de maximum i

            if (time % 60 == 0) {
                hours = time / 60;
                minutes = 00;
            } else {
                hours = time / 60;
                minutes = 30;
            }

            graphics.draw(new Line2D.Double(0, pixelVertical, this.canvas.getWidth(), pixelVertical));
            graphics.drawString(LocalTime.of(hours, minutes).toString(), 0, pixelVertical + 23);

            //auteur : Sebastiaan
            for (LessonBlock lessonBlock : lessonBlocks) {
                graphics.fill(lessonBlock.getTransformedShape());
            }
            if (dragged != null) {
                graphics.setColor(Color.GRAY);
                graphics.fill(dragged.getTransformedShape());
                graphics.setColor(Color.BLACK);
                graphics.draw(dragged.getTransformedShape());

            }

        }
    }

    //Auteur: Sebastiaan
    // Deze methode kreert de lesblokken voor in d e
    private void createLessonBlocks() {

        int pixelVertical = (int) this.canvas.getHeight() / 27;
        int widthRoom = (int) (this.canvas.getWidth() - 50) / timetable.getAllRooms().size();
        lessonBlocks.clear();
        lessons = timetable.getLessons();

        for (Room room : timetable.getAllRooms()) {

            for (Lesson lesson : lessons) {
                if (lesson.getRoom().equals(room)) {
                    Point2D lessonOriginPoint = new Point2D(50 + widthRoom * timetable.getAllRooms().indexOf(room), (lesson.getStartTime().getHour() - 5) * 2 * (double) pixelVertical + 2 * (double) pixelVertical * lesson.getStartTime().getMinute() / 60);
                    lessonBlocks.add(new LessonBlock(new Rectangle2D.Double(0, 0, widthRoom, 2 * (double) pixelVertical * lesson.getDuration() / 60), lessonOriginPoint, 0, 1, lesson));
                }
            }
        }
    }

    private LocalTime getTimeAtMouse(Point2D mousePosition){
        double hours = (mousePosition.getY()/2/(canvas.getHeight()/27))+5;
        double minutes = 60*(hours%1);
        return LocalTime.of((int) hours, (int) minutes);
    }

    private Room getRoomAtMouse(Point2D mousePosition){
        double roomIndex = 0.5 + dataController.getAllRooms().size()*(mousePosition.getX()-50)/(canvas.getWidth()-50);
        System.out.println(roomIndex);
        return timetable.getAllRooms().get((int)roomIndex);
    }
}
