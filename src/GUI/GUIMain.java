package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Line2D;

public class GUIMain  extends Application {

    private Canvas canvas;

@Override
public void start(Stage primaryStage){

        this.canvas = new Canvas(1200  ,900);

        MenuBar menubar = new MenuBar();
        VBox vBox = new VBox(menubar);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(vBox);
        borderPane.setCenter(canvas);

        Scene scene = new Scene(borderPane);

        Menu file = new Menu("File");
        Menu rooms = new Menu("Rooms");
        Menu group = new Menu("Group");
        Menu simulation = new Menu("Simulation");

        menubar.getMenus().add(file);
        menubar.getMenus().add(rooms);
        menubar.getMenus().add(group);
        menubar.getMenus().add(simulation);

        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(scene);


        primaryStage.setTitle("Agenda");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) {

        for (int i = 0; i < 1200 ; i+= 300) {
            graphics.draw(new Line2D.Double(i,0,i,900));
        }

        for (int i = 0; i < 900; i+=50) {
            graphics.setColor(Color.blue);
            graphics.draw(new Line2D.Double(0,i,1200,i));
        }
    }
}
