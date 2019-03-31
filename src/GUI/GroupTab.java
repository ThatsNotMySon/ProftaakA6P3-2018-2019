package GUI;

import Data.DataController;
import Data.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class GroupTab extends GridPane {

    //Auteur: Rümeysa en Marleen (loshalen uit GUIMain door Marleen)

    ListView listGroups = new ListView();
    DataController dataController;

    private Label editNameClassLabel;
    private TextField editNameClassField;
    private Label editAmountOfStudentsLabel;
    private TextField editAmountOfStudentsField;

    public GroupTab(DataController dataController, GUIMain guiMain){
        this.dataController = dataController;

        Label nameClassLabel = new Label("Name class: ");
        Label amountOfStudentsLabel = new Label("Amount of students: ");

        TextField nameClassField = new TextField("class");
        TextField amountOfStudentsField = new TextField("amount");

        Button buttonAddClass = new Button("Add class");
        Button buttonDeleteClass = new Button("Delete class");
        Button buttonEditClass = new Button("Edit class");

        Label errorInGroup = new Label("");

        add(nameClassLabel, 1, 1);
        add(nameClassField, 2, 1);
        add(amountOfStudentsLabel, 1, 2);
        add(amountOfStudentsField, 2, 2);
        add(buttonAddClass, 2, 4);
        add(listGroups, 1, 4);
        add(buttonDeleteClass, 1, 5);
        add(errorInGroup,2,3 );
        add(buttonEditClass, 1, 6);

        //Auteur: Marleen

        listGroups.getItems().addAll(this.dataController.getTimeTable().getAllGroups());

        buttonAddClass.setOnAction(event -> {
            try {
                if (nameClassField.getText() != null && amountOfStudentsField != null && !this.dataController.getAllGroupNames().contains(nameClassField.getText()) && Integer.parseInt(amountOfStudentsField.getText()) > 0) {
                    this.dataController.getTimeTable().addGroup(new Group(nameClassField.getText(), Integer.parseInt(amountOfStudentsField.getText())));
                    listGroups.getItems().clear();
                    listGroups.getItems().addAll(this.dataController.getTimeTable().getAllGroups());
                    guiMain.lessonTab1.LessonUpdate();
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
                guiMain.lessonTab1.LessonUpdate();
                errorInGroup.setText("Group deleted");
            } else {
                errorInGroup.setText("Cannot delete group being used by lesson");
            }
        });

        //Auteur: Rümeysa

        buttonEditClass.setOnAction(event -> {

            Group selectedItem = (Group) listGroups.getSelectionModel().getSelectedItem();

            editNameClassLabel = new Label("Edit name class: ");
            editNameClassField = new TextField(selectedItem.getName());

            editNameClassField.setOnAction(a -> {
                selectedItem.setName(editNameClassField.getText());
                listGroups.refresh();
            });

            editAmountOfStudentsLabel = new Label("Edit amount of students: ");
            editAmountOfStudentsField = new TextField(Integer.toString(selectedItem.getAmountOfStudents()));

            editAmountOfStudentsField.setOnAction(b -> {

                selectedItem.setAmountOfStudents(Integer.parseInt(editAmountOfStudentsField.getText()));
                listGroups.refresh();
            });

            if (selectedItem != null) {

                add(editNameClassLabel, 1, 7);
                add(editNameClassField, 2, 7);
                add(editAmountOfStudentsLabel, 1, 8);
                add(editAmountOfStudentsField, 2, 8);
            }
        });

    }

    public void groupTabUpdate(){
        // Auteur: Marleen
        listGroups.getItems().clear();
        listGroups.getItems().addAll(this.dataController.getTimeTable().getAllGroups());
    }
}
