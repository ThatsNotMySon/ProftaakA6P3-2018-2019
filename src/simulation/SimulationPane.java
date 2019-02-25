package simulation;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import org.jfree.fx.FXGraphics2D;
import simulation.simulationgui.ForwardButton;
import simulation.simulationgui.PlayPauseButton;

import java.awt.*;

public class SimulationPane extends BorderPane {

    private Simulation simulation;
    private Canvas simulationCanvas;

    public SimulationPane(){
        this.simulation = new Simulation();
        this.simulationCanvas = new Canvas();

        PlayPauseButton playPauseButton = new PlayPauseButton();
        playPauseButton.setText("Play/Pause");
        playPauseButton.setOnMousePressed(e -> simulation.playPause());

        FlowPane GuiPane = new FlowPane();
        GuiPane.getChildren().add(playPauseButton);


        this.setBottom(GuiPane);
        this.setCenter(simulationCanvas);

        this.simulation.draw(new FXGraphics2D(simulationCanvas.getGraphicsContext2D()));

    }
}
