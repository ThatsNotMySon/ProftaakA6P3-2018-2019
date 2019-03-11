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
import simulation.SimulationPane;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;

public class GUIMain extends Application {


    public LessonTab lessonTab1;
    private Canvas agendaCanvas;
    private DataController dataController;
    private Timetable timetable;
    private Point2D offset;
    private ArrayList<LessonBlock> lessonBlocks;
    private DraggedBlock dragged;
    private ArrayList<Lesson> lessons;
    private ArrayList<Color> groupColors;

    private void onStart() {
        this.dataController = new DataController();
    }

    @Override
    public void start(Stage primaryStage) {

        this.onStart();
        
        this.agendaCanvas = new Canvas(1200  ,900);
        timetable = dataController.getTimeTable();
        lessonBlocks = new ArrayList<>();
        dragged = null;
        lessons = dataController.getAllLessons();

        //Testcode om groepen in Agenda kleurtjes te geven
        //this.groupColors = new ArrayList<>();
        //Color[] data = {Color.RED, Color.YELLOW, Color.BLUE,Color.CYAN,Color.GREEN, Color.MAGENTA};
        //groupColors.addAll(data{1,2,3,4,5});

        TableView tableViewTableTab = new TableView();
        ObservableList<Lesson> tableData = FXCollections.observableArrayList(lessons);

        ListView listGroups = new ListView();
      //  ListView listRooms = new ListView();

        this.createLessonBlocks();
//Auteur : Sebastiaan
        agendaCanvas.setOnMousePressed(e ->
        {
            for (LessonBlock lessonBlock : lessonBlocks) {
                if (lessonBlock.getTransformedShape().contains(e.getX(), e.getY())) {
                    dragged = lessonBlock.getDraggedBlock();
                    offset = new Point2D(lessonBlock.getPosition().getX() - e.getX(), lessonBlock.getPosition().getY() - e.getY());
                }
            }

        });

        agendaCanvas.setOnMouseReleased(e -> {

            if(dragged != null){

            LocalTime newTime = getTimeAtMouse(offset.add(new Point2D(e.getX(),e.getY())));
            Room newRoom = getRoomAtMouse(offset.add(new Point2D(e.getX(),e.getY())));
                if(!dataController.checkAvailableTime(newRoom.getName(),newTime,dragged.getLesson().getDuration(), dragged.getLesson()))
                    System.out.println("No free time found");
            if(dataController.checkAvailableTime(newRoom.getName(),newTime,dragged.getLesson().getDuration(), dragged.getLesson()))
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
            draw(new FXGraphics2D(agendaCanvas.getGraphicsContext2D()));
        });

        agendaCanvas.setOnMouseDragged(e ->
        {
            Point2D position = new Point2D(e.getX(), e.getY());
            if (dragged != null) {
                dragged.setPosition(offset.add(position));
            }
            draw(new FXGraphics2D(agendaCanvas.getGraphicsContext2D()));
        });

        lessonTab1 = new LessonTab(dataController, lessons, tableData, tableViewTableTab, agendaCanvas, this);
        RoomTab roomTab1 = new RoomTab(dataController, this, agendaCanvas);

        TabPane tabPane = new TabPane();

        Tab agendaTab = new Tab("Agenda");
        Tab tableTab = new Tab("Table");
        Tab roomTab = new Tab("Rooms", roomTab1);
        Tab groupTab = new Tab("Groups");
        Tab lessonTab = new Tab("Lesson", lessonTab1);
        Tab simulationTab = new Tab("Simulation");

        tabPane.getTabs().addAll(agendaTab, tableTab, roomTab, groupTab, lessonTab, simulationTab);

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(tabPane);
        Scene scene = new Scene(borderPane);


            /*
        De volgende code hoort bij het tabje table
        Als het stuk is moet je bij Rümeysa en Tom zijn
         */

        Button openFile = new Button("Open File");
        Button saveFile = new Button("Save File");
        Button deleteLesson = new Button("Remove Lesson");


        TableColumn columnGroups = new TableColumn("Group");
        TableColumn columnRooms = new TableColumn("Room");
        TableColumn columnStartTime = new TableColumn("Start time");
        TableColumn columnLengthTime = new TableColumn("Length");



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
                //TODO: Room update
                //TODO: lesson update
                createLessonBlocks();
                draw(new FXGraphics2D(agendaCanvas.getGraphicsContext2D()));
            }
        });

        saveFile.setOnAction(e -> {
            int returnVal = chooser.showSaveDialog(null);
            if (returnVal == chooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                this.dataController.getTimeTable().saveTimetableToFile(file.getAbsolutePath());
            }
        });

        deleteLesson.setOnAction(e -> {
            tableViewTableTab.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            this.dataController.getTimeTable().removeLesson((Lesson)tableViewTableTab.getSelectionModel().getSelectedItem());
            tableData.clear();
            tableData.addAll(this.dataController.getAllLessons());
            createLessonBlocks();
            draw(new FXGraphics2D(agendaCanvas.getGraphicsContext2D()));
        });

        HBox hBoxTableFiles = new HBox(openFile, saveFile);
        VBox vBoxTable = new VBox(hBoxTableFiles, tableViewTableTab, deleteLesson);
        hBoxTableFiles.setSpacing(50);
        vBoxTable.setSpacing(25);



        /*
        Meer algemene code
        * Als deze code stuk is moet je bij Marleen en Rümeysa zijn
        * */


        BorderPane agendaPane = new BorderPane(agendaCanvas);
        BorderPane tablePane = new BorderPane(vBoxTable);
        GridPane groupPane = new GridPane();
        BorderPane simulationPane = new SimulationPane();

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

        Label errorInGroup = new Label("");

        groupPane.add(nameClassLabel, 1, 1);
        groupPane.add(nameClassField, 2, 1);
        groupPane.add(amountOfStudentsLabel, 1, 2);
        groupPane.add(amountOfStudentsField, 2, 2);
        groupPane.add(buttonAddClass, 2, 4);
        groupPane.add(listGroups, 1, 4);
        groupPane.add(buttonDeleteClass, 1, 5);
        groupPane.add(errorInGroup,2,3 );

        /*
         * De volgende code laat de knoppen op de Lesson Tab
         * Als deze code stuk is moet je bij Marleen zijn
         * */

        listGroups.getItems().addAll(this.dataController.getTimeTable().getAllGroups());

        buttonAddClass.setOnAction(event -> {
            try {
                if (nameClassField.getText() != null && amountOfStudentsField != null && !this.dataController.getAllGroupNames().contains(nameClassField.getText()) && Integer.parseInt(amountOfStudentsField.getText()) > 0) {
                    this.dataController.getTimeTable().addGroup(new Group(nameClassField.getText(), Integer.parseInt(amountOfStudentsField.getText())));
                    listGroups.getItems().clear();
                    listGroups.getItems().addAll(this.dataController.getTimeTable().getAllGroups());
                    lessonTab1.LessonUpdate();
                    errorInGroup.setText("Group added");
                } else {
                    errorInGroup.setText("Check input");
                }
            } catch (Exception e){
                System.out.println("Check input");
                errorInGroup.setText("Check input");
                e.printStackTrace();
            }
        });

        buttonDeleteClass.setOnAction(event -> {
            ArrayList<String> groupDelete = new ArrayList<>();
            for (int i = 0; i < this.dataController.getAllLessons().size(); i++) {
                groupDelete.add(this.dataController.getAllLessons().get(i).getGroup().get(0).getName());
            }
            listGroups.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            Group selectedGroup = (Group)listGroups.getSelectionModel().getSelectedItem();
            if (!groupDelete.contains(selectedGroup.getName())){
                listGroups.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                this.dataController.getTimeTable().removeGroup((Group) listGroups.getSelectionModel().getSelectedItem());
                listGroups.getItems().clear();
                listGroups.getItems().addAll(this.dataController.getTimeTable().getAllGroups());
                lessonTab1.LessonUpdate();
                errorInGroup.setText("Group deleted");
        } else {
                errorInGroup.setText("Cannot delete group being used by lesson");
            }
        });

        /*
        De volgende code hoort bij het tabje Room
        * Als deze code stuk is moet je bij Marleen en Rümeysa zijn
        * */

        /*Meer algeme code
        * Als deze code stuk is moet je bij Marleen en Rümeysa*/

        agendaTab.setContent(agendaPane);
        tableTab.setContent(tablePane);
        groupTab.setContent(groupPane);
        simulationTab.setContent(simulationPane);

        draw(new FXGraphics2D(agendaCanvas.getGraphicsContext2D()));
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
        int pixelVertical = (int)this.agendaCanvas.getHeight()/27;
        int pixelHorizontal = 0;
        int hours = 0;
        int minutes = 0;

    Timetable timetable = dataController.getTimeTable();
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, (int) agendaCanvas.getHeight() * 2, (int) agendaCanvas.getWidth() * 2);


        //Auteur: Marleen
        //Hieronder wordt het rooster getekend
        graphics.draw(new Line2D.Double(50, 0, 50, this.agendaCanvas.getHeight()));

        int widthRoom = (int) (this.agendaCanvas.getWidth() - 50) / timetable.getAllRooms().size();

        for (int i = 0; i < timetable.getAllRooms().size() - 1; i++) {
            //         graphics.draw(new Line2D.Double(i,0,i,900));
            pixelHorizontal = 50 + widthRoom + widthRoom * i;
            graphics.draw(new Line2D.Double(pixelHorizontal, 0, pixelHorizontal, this.agendaCanvas.getHeight()));
        }


        for (int i = 0; i < timetable.getAllRooms().size(); i++) {
            graphics.drawString(timetable.getAllRooms().get(i).getName(), 75 + widthRoom * i, pixelVertical - 10);
        }


        for (int i = 0; i < 27 - 1; i++) {
            time += 30;
            pixelVertical = ((int) this.agendaCanvas.getHeight() / 27) + i * ((int) this.agendaCanvas.getHeight() / 27); //gedeeld door de maximum i

            if (time % 60 == 0) {
                hours = time / 60;
                minutes = 00;
            } else {
                hours = time / 60;
                minutes = 30;
            }


            graphics.draw(new Line2D.Double(0, pixelVertical, this.agendaCanvas.getWidth(), pixelVertical));
            graphics.drawString(LocalTime.of(hours, minutes).toString(), 0, pixelVertical + 23);

            //auteur : Sebastiaan
            //Het volgende blok tekent lesblokken en lestekst in blokken
            Font font = new Font(Font.SANS_SERIF,Font.PLAIN, 20);

            for (LessonBlock lessonBlock : lessonBlocks) {
                graphics.setColor(new Color(0,150,255));
                //graphics.setColor(groupColors.get(lessonBlocks.indexOf(lessonBlock)));
                graphics.fill(lessonBlock.getTransformedShape());
                graphics.setColor(Color.BLACK);
                graphics.draw(lessonBlock.getTransformedShape());
                graphics.setColor(Color.WHITE);

                Lesson lesson = lessonBlock.getLesson();
                String lessonString = lesson.getGroup() + " " + lesson.getSubject() + ", " + lesson.getTeacher();
                Shape fontShape = font.createGlyphVector(graphics.getFontRenderContext(),lessonString).getOutline();
                AffineTransform fontTransform = new AffineTransform();
                fontTransform.translate(lessonBlock.getPosition().getX()+4,lessonBlock.getPosition().getY()+ 20);
                graphics.fill(fontTransform.createTransformedShape(fontShape));
                graphics.setColor(Color.BLACK);
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
    // Deze methode creert de lesblokken voor in de
    public void createLessonBlocks() {

        int pixelVertical = (int) this.agendaCanvas.getHeight() / 27;
        int widthRoom = (int) (this.agendaCanvas.getWidth() - 50) / timetable.getAllRooms().size();
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

    //Auteur: Sebastiaan
    //Deze methode vertaalt de coordinaten naar een tijd. Hardcoded
    private LocalTime getTimeAtMouse(Point2D mousePosition){
        double hours = (mousePosition.getY()/2/(agendaCanvas.getHeight()/27))+5;
        double minutes = 60*(hours%1);
        return LocalTime.of((int) hours, (int) minutes);
    }

    //Auteur: Sebastiaan
    //Deze methode vertaalt de coordinaten naar een room. Hardcoded.
    private Room getRoomAtMouse(Point2D mousePosition){
        double roomIndex = 0.5 + dataController.getAllRooms().size()*(mousePosition.getX()-50)/(agendaCanvas.getWidth()-50);
        System.out.println(roomIndex);
        return timetable.getAllRooms().get((int)roomIndex);
    }
}
