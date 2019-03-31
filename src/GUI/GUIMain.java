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
    public GroupTab groupTab1;
    public RoomTab roomTab1;
    public TableTab tableTab1;
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

        this.agendaCanvas = new Canvas(1200, 900);
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

            if (dragged != null) {

                LocalTime newTime = getTimeAtMouse(offset.add(new Point2D(e.getX(), e.getY())));
                Room newRoom = getRoomAtMouse(offset.add(new Point2D(e.getX(), e.getY())));
                if (!dataController.checkAvailableTime(newRoom.getName(), newTime, dragged.getLesson().getDuration(), dragged.getLesson()))
                    System.out.println("No free time found");
                if (dataController.checkAvailableTime(newRoom.getName(), newTime, dragged.getLesson().getDuration(), dragged.getLesson()))
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
                }
            }
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

        //Auteur: Marleen
        lessonTab1 = new LessonTab(dataController, lessons, tableData, tableViewTableTab, agendaCanvas, this);
        roomTab1 = new RoomTab(dataController, this, agendaCanvas);
        groupTab1 = new GroupTab(dataController, this);
        tableTab1 = new TableTab(dataController, this, lessons, agendaCanvas);

        TabPane tabPane = new TabPane();

        Tab agendaTab = new Tab("Agenda");
        Tab tableTab = new Tab("Table", tableTab1);
        Tab roomTab = new Tab("Rooms", roomTab1);
        Tab groupTab = new Tab("Groups", groupTab1);
        Tab lessonTab = new Tab("Lesson", lessonTab1);
        Tab simulationTab = new Tab("Simulation");

        tabPane.getTabs().addAll(agendaTab, tableTab, roomTab, groupTab, lessonTab, simulationTab);

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        //Auteur: Rümeysa en Marleen
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(tabPane);
        Scene scene = new Scene(borderPane);

        BorderPane agendaPane = new BorderPane(agendaCanvas);
        BorderPane simulationPane = new SimulationPane(dataController, simulationTab);

        agendaTab.setContent(agendaPane);
        simulationTab.setContent(simulationPane);

        draw(new FXGraphics2D(agendaCanvas.getGraphicsContext2D()));
        primaryStage.setScene(scene);

        primaryStage.setTitle("Agenda");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) {

        createAgenda(graphics);

        //auteur : Sebastiaan
        //Het volgende blok tekent lesblokken en lestekst in blokken
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 20);

        for (LessonBlock lessonBlock : lessonBlocks) {
            graphics.setColor(new Color(0, 150, 255));
            //graphics.setColor(groupColors.get(lessonBlocks.indexOf(lessonBlock)));
            graphics.fill(lessonBlock.getTransformedShape());
            graphics.setColor(Color.BLACK);
            graphics.draw(lessonBlock.getTransformedShape());
            graphics.setColor(Color.WHITE);

            Lesson lesson = lessonBlock.getLesson();
            String lessonString = lesson.getGroup() + " " + lesson.getSubject() + ", " + lesson.getTeacher();
            Shape fontShape = font.createGlyphVector(graphics.getFontRenderContext(), lessonString).getOutline();
            AffineTransform fontTransform = new AffineTransform();
            fontTransform.translate(lessonBlock.getPosition().getX() + 4, lessonBlock.getPosition().getY() + 20);
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

    public void createAgenda(FXGraphics2D graphics) {

        //Auteur: Marleen
        int time = 300;
        int pixelVertical = (int) this.agendaCanvas.getHeight() / 27;
        int pixelHorizontal = 0;
        int hours = 0;
        int minutes = 0;

        Timetable timetable = dataController.getTimeTable();
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, (int) agendaCanvas.getHeight() * 2, (int) agendaCanvas.getWidth() * 2);
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

        }
    }
}
