package GUI;

import Data.DataController;
import Data.Room;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.jfree.fx.FXGraphics2D;

import java.util.ArrayList;

public class RoomTab extends GridPane {
    // Auteur: Rümeysa en Marleen (loshalen uit GUIMain door Marleen)
    ListView listRooms = new ListView();
    private DataController dataController;

    private Label editNameRoomLabel;
    private TextField editNameRoomField;
    private Label editCapacityRoomLabel;
    private TextField editCapacityRoomField;

    public RoomTab(DataController dataController, GUIMain guiMain, Canvas agendaCanvas) {
        this.dataController = dataController;

        Label nameRoomLabel = new Label("Room name: ");
        TextField nameRoom = new TextField("LA111");

        Label capacityRoomLabel = new Label("Capacity Room: ");
        TextField capacityRoom = new TextField("0");

        Button addRoom = new Button("Add Room");
        Button deleteRoom = new Button("Delete Room");
        Button editRoom = new Button("Edit Room");

        Label errorLabelRooms = new Label("");

        add(nameRoomLabel, 1, 1);
        add(nameRoom, 2, 1);
        add(capacityRoomLabel, 1, 2);
        add(capacityRoom, 2, 2);
        add(errorLabelRooms, 2, 3);
        add(addRoom, 2, 4);
        add(listRooms, 1, 4);
        add(deleteRoom, 1, 5);
        add(editRoom, 1, 6);

        listRooms.getItems().addAll(this.dataController.getTimeTable().getAllRooms());

        //Auteur: Tom

        addRoom.setOnAction(event -> {
            try {
                if (nameRoom.getText() != null && capacityRoom != null && !this.dataController.getAllRoomNames().contains(nameRoom.getText()) && Integer.parseInt(capacityRoom.getText()) > 0) {
                    this.dataController.getTimeTable().addRoom(new Room(nameRoom.getText(), Integer.parseInt(capacityRoom.getText())));
                    listRooms.getItems().clear();
                    listRooms.getItems().addAll(this.dataController.getTimeTable().getAllRooms());
                    guiMain.lessonTab1.LessonUpdate();
                    errorLabelRooms.setText("Room added");
                    guiMain.createLessonBlocks();
                    guiMain.draw(new FXGraphics2D(agendaCanvas.getGraphicsContext2D()));
                } else {
                    errorLabelRooms.setText("Check input");
                }
            } catch (Exception e) {
                errorLabelRooms.setText("Check input");
                System.out.println("Check input");
                e.printStackTrace();
            }
        });

        deleteRoom.setOnAction(event -> {
            listRooms.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

            ArrayList<String> rooms = new ArrayList<>();
            for (int i = 0; i < this.dataController.getAllLessons().size(); i++) {
                rooms.add(this.dataController.getAllLessons().get(i).getRoom().getName());
            }
            Room selectedRoom = (Room) listRooms.getSelectionModel().getSelectedItem();
            if (!rooms.contains(selectedRoom.getName())) {

                this.dataController.getTimeTable().removeRoom((Room) listRooms.getSelectionModel().getSelectedItem());
                listRooms.getItems().clear();
                listRooms.getItems().addAll(this.dataController.getTimeTable().getAllRooms());
                guiMain.lessonTab1.LessonUpdate();
                errorLabelRooms.setText("Room deleted");
                guiMain.createLessonBlocks();
                guiMain.draw(new FXGraphics2D(agendaCanvas.getGraphicsContext2D()));
            } else {
                errorLabelRooms.setText("Cannot delete room being used by lesson");
            }
        });

        //Auteur: Rümeysa
        editRoom.setOnAction(event -> {

            Room selectedItem = (Room) listRooms.getSelectionModel().getSelectedItem();

            editNameRoomLabel = new Label("Edit room name: ");
            editNameRoomField = new TextField(selectedItem.getName());

            editNameRoomField.setOnAction(a -> {
                selectedItem.setName(editNameRoomField.getText());
                listRooms.refresh();
            });

            editCapacityRoomLabel = new Label("Edit capacity room: ");
            editCapacityRoomField = new TextField(Integer.toString(selectedItem.getCapacity()));

            editCapacityRoomField.setOnAction(b -> {

                selectedItem.setCapacity(Integer.parseInt(editCapacityRoomField.getText()));
                listRooms.refresh();
            });

            if (selectedItem != null) {

                add(editNameRoomLabel, 1, 7);
                add(editNameRoomField, 2, 7);
                add(editCapacityRoomLabel, 1, 8);
                add(editCapacityRoomField, 2, 8);
            }
        });
    }

    // Auteur: Rümeysa en Marleen

    public void roomTabUpdate() {
        listRooms.getItems().clear();
        listRooms.getItems().addAll(this.dataController.getTimeTable().getAllRooms());
    }

}
