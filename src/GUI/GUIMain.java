package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Line2D;

public class GUIMain  extends Application {

    private Canvas canvas;

@Override
public void start(Stage primaryStage){

        this.canvas = new Canvas(1200  ,900);

        MenuBar menubar = new MenuBar();
        VBox vBox = new VBox(menubar);
        HBox hBox = new HBox(vBox, canvas);
        Scene scene = new Scene(hBox);

        Menu file = new Menu("File");
        Menu rooms = new Menu("Rooms");
        Menu group = new Menu("Group");
    //    Menu lesson=new Menu("Lesson");
        Menu simulation = new Menu("Simulation");

        menubar.getMenus().add(file);
        menubar.getMenus().add(rooms);
        menubar.getMenus().add(group);
    //    menubar.getMenus().add(lesson);
        menubar.getMenus().add(simulation);


        this.canvas = new Canvas(640, 480); //4:3

        primaryStage.setScene(scene);


        primaryStage.setTitle("Agenda");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) {
        graphics.translate(this.canvas.getWidth()/2, this.canvas.getHeight()/2);
        graphics.scale(1, -1);

        graphics.draw(new Line2D.Double(200, 100, 500, 200));
    }
}
