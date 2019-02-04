package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUIMain  extends Application {

@Override
public void start(Stage primaryStage){


        MenuBar menubar=new MenuBar();
        VBox vBox=new VBox(menubar);
        Scene scene=new Scene(vBox,960,600);

        Menu file=new Menu("File");
        Menu rooms=new Menu("Rooms");
        Menu group=new Menu("Group");
        Menu lesson=new Menu("Lesson");
        Menu simulation=new Menu("Simulation");

        menubar.getMenus().add(file);
        menubar.getMenus().add(rooms);
        menubar.getMenus().add(group);
        menubar.getMenus().add(lesson);
        menubar.getMenus().add(simulation);


        primaryStage.setScene(scene);


        primaryStage.setTitle("Agenda");
        primaryStage.show();
        }
}
