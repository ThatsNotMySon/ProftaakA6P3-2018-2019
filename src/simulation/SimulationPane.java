package simulation;

import Data.DataController;
import GUI.GUIMain;
import GUI.TableTab;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import simulation.simulationgui.ForwardButton;
import simulation.simulationgui.PlayPauseButton;
import simulation.simulationgui.RefreshButton;
import simulation.simulationgui.ShowDirectionButton;

import java.awt.*;
import java.util.ArrayList;

public class SimulationPane extends BorderPane {

    private Simulation simulation;
    private Canvas simulationCanvas;

    private ArrayList<Actor> actors;
    private DataController dataController;
    private Camera camera;
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 900;
    private Tab simulationTab;

    public SimulationPane(DataController dataController, Tab simulationTab){

        this.simulationTab = simulationTab;
        this.dataController = dataController;
        this.simulationCanvas = new Canvas(WIDTH,HEIGHT);
        FXGraphics2D g = new FXGraphics2D(simulationCanvas.getGraphicsContext2D());
        camera = new Camera(simulationCanvas, simulation, g);
        this.simulation = new Simulation(new DataController(), this.camera);


        PlayPauseButton playPauseButton = new PlayPauseButton();
        playPauseButton.setText("Play/Pause");
        playPauseButton.setOnMousePressed(e -> simulation.playPause());

        RefreshButton refreshButton = new RefreshButton();
        refreshButton.setText("Refresh");
        refreshButton.setOnMousePressed(e -> simulation.refresh(dataController));

        ShowDirectionButton directionButton = new ShowDirectionButton();
        directionButton.setText("Show Direction");
        directionButton.setOnMousePressed(e-> simulation.showDirections());

        FlowPane GuiPane = new FlowPane();
        GuiPane.getChildren().add(playPauseButton);
        GuiPane.getChildren().add(refreshButton);
        GuiPane.getChildren().add(directionButton);





        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g);
            }
        }.start();

//        simulationCanvas.setOnMouseMoved(e -> {
//            for(Actor actor : actors)
//                actor.setTarget(new Point2D(e.getX(), e.getY()));
//        });




        this.setBottom(GuiPane);
        this.setTop(simulationCanvas);

    }

    public void update(double deltaTime){
        //only run while simulation tab is visible
        if (this.simulationTab.isSelected())
        this.simulation.update(deltaTime);
        

    }
    public void draw(FXGraphics2D graphics){this.simulation.draw(graphics); }
}

