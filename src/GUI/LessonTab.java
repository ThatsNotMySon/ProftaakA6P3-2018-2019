package GUI;

import Data.DataController;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jfree.fx.FXGraphics2D;
import Data.*;

import javax.xml.crypto.Data;
import java.time.LocalTime;
import java.util.ArrayList;

public class LessonTab extends BorderPane {
    ListView lessonGroupsListView;
    ListView lessonRoomsListView;
    private DataController dataController;


    public LessonTab(DataController dataController, ArrayList<Lesson> lessons, ObservableList<Lesson> tableData, TableView tableViewTableTab, Canvas agendaCanvas, GUIMain guiMain){

        this.dataController = dataController;
        lessonGroupsListView = new ListView();
        lessonRoomsListView = new ListView();

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

        lessonGroupsListView.getItems().addAll(this.dataController.getTimeTable().getAllGroups());
        lessonRoomsListView.getItems().addAll(this.dataController.getTimeTable().getAllRooms());

        confirmLesson.setOnAction(event -> {
            try {
                if(lessonRoomsListView.getSelectionModel().getSelectedItem() != null && lessonGroupsListView.getSelectionModel().getSelectedItem() != null &&
                        lessonStartTimeInput != null && lessonStartTimeInput2 != null & lessonLengthTimeInput != null && lessonTeacherInput != null && lessonSubjectInput != null && Integer.parseInt(lessonLengthTimeInput.getText()) > 0
                        && Integer.parseInt(lessonStartTimeInput.getText()) > 5 && Integer.parseInt(lessonStartTimeInput.getText()) + (Integer.parseInt(lessonLengthTimeInput.getText())/60) < 18) {
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
                        guiMain.createLessonBlocks();
                        guiMain.draw(new FXGraphics2D(agendaCanvas.getGraphicsContext2D()));
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
        setCenter(hBoxLessons);
    }

    public void LessonUpdate() {
        lessonGroupsListView.getItems().clear();
        lessonGroupsListView.getItems().addAll(this.dataController.getTimeTable().getAllGroups());
        lessonRoomsListView.getItems().clear();
        lessonRoomsListView.getItems().addAll(this.dataController.getTimeTable().getAllRooms());
    }
}
