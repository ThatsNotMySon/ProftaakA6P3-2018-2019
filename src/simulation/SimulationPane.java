package simulation;

import Data.DataController;
import GUI.GUIMain;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import simulation.simulationgui.ForwardButton;
import simulation.simulationgui.PlayPauseButton;

import java.awt.*;

public class SimulationPane extends BorderPane {

    private Simulation simulation;
    private Canvas simulationCanvas;

    public SimulationPane(){

        this.simulation = new Simulation(new DataController());
        this.simulationCanvas = new Canvas(1200,900);

        PlayPauseButton playPauseButton = new PlayPauseButton();
        playPauseButton.setText("Play/Pause");
        playPauseButton.setOnMousePressed(e -> simulation.playPause());

        FlowPane GuiPane = new FlowPane();
        GuiPane.getChildren().add(playPauseButton);

        FXGraphics2D g = new FXGraphics2D(simulationCanvas.getGraphicsContext2D());
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

        this.setBottom(GuiPane);
        this.setTop(simulationCanvas);

    }

    public void update(double deltaTime){
        this.simulation.update(deltaTime);
    }
    public void draw(FXGraphics2D graphics){this.simulation.draw(graphics); }
}

