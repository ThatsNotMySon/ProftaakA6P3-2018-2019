package GUI;

import Data.DataController;
import Data.Group;
import Data.Lesson;
import Data.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jfree.fx.FXGraphics2D;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;

public class TableTab extends BorderPane {
    DataController dataController;

    // Auteur: Rümeysa en Tom (loshalen uit GUIMain door Marleen)

    public TableTab(DataController dataController, GUIMain guiMain, ArrayList<Lesson> lessons, Canvas agendaCanvas){

        this.dataController = dataController;
        TableView tableViewTableTab = new TableView();
        ObservableList<Lesson> tableData = FXCollections.observableArrayList(lessons);


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
                guiMain.groupTab1.groupTabUpdate();
                guiMain.roomTab1.roomTabUpdate();
                guiMain.lessonTab1.LessonUpdate();
                guiMain.createLessonBlocks();
                guiMain.draw(new FXGraphics2D(agendaCanvas.getGraphicsContext2D()));
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
            guiMain.createLessonBlocks();
            guiMain.draw(new FXGraphics2D(agendaCanvas.getGraphicsContext2D()));
        });

        HBox hBoxTableFiles = new HBox(openFile, saveFile);
        VBox vBoxTable = new VBox(hBoxTableFiles, tableViewTableTab, deleteLesson);
        hBoxTableFiles.setSpacing(50);
        vBoxTable.setSpacing(25);

        setCenter(vBoxTable);



    }
}
