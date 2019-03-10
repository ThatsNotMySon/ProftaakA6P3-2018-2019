package simulation;

import Data.DataController;
import GUI.GUIMain;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import org.jfree.fx.FXGraphics2D;
import simulation.simulationgui.ForwardButton;
import simulation.simulationgui.PlayPauseButton;

import java.awt.*;
import java.util.ArrayList;

public class SimulationPane extends BorderPane {

    private Simulation simulation;
    private Canvas simulationCanvas;

    private ArrayList<Actor> actors;

    public SimulationPane(){

        this.simulation = new Simulation(new DataController());
        this.simulationCanvas = new Canvas(1200,900);

        PlayPauseButton playPauseButton = new PlayPauseButton();
        playPauseButton.setText("Play/Pause");
        playPauseButton.setOnMousePressed(e -> simulation.playPause());

        FlowPane GuiPane = new FlowPane();
        GuiPane.getChildren().add(playPauseButton);

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(new FXGraphics2D(simulationCanvas.getGraphicsContext2D()));
            }
        }.start();

//        simulationCanvas.setOnMouseMoved(e -> {
//            for(Actor actor : actors)
//                actor.setTarget(new Point2D(e.getX(), e.getY()));
//        });

        this.setBottom(GuiPane);
        this.setTop(simulationCanvas);

        //this.simulation.draw(new FXGraphics2D(simulationCanvas.getGraphicsContext2D()));

    }

    public void update(double deltaTime){
        this.simulation.update(deltaTime);
    }

    public void draw(FXGraphics2D graphics)
    {
        this.simulation.draw(graphics);
    }
}
