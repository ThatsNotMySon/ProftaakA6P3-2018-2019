package simulation;

import Data.DataController;
import GUI.GUIMain;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import simulation.simulationgui.ForwardButton;
import simulation.simulationgui.PlayPauseButton;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class SimulationPane extends BorderPane {

    private Simulation simulation;
    private Canvas simulationCanvas;
    private Point2D startPoint;

    private ArrayList<Actor> actors;

    public SimulationPane(){

        this.simulation = new Simulation(new DataController());
        this.simulationCanvas = new Canvas(1200,900);

        PlayPauseButton playPauseButton = new PlayPauseButton();
        playPauseButton.setText("Play/Pause");
        playPauseButton.setOnMousePressed(e -> simulation.playPause());

        FlowPane GuiPane = new FlowPane();
        GuiPane.getChildren().add(playPauseButton);

        FXGraphics2D g = new FXGraphics2D(simulationCanvas.getGraphicsContext2D());
//        this.setOnMousePressed(e -> {
//            this.startPoint = new Point2D.Double(this.simulation.Position.getX() - e.getX(), this.simulation.Position.getY() - e.getY());
//        });
        this.setOnMousePressed(e -> {
//            AffineTransform tx = new AffineTransform();
//            tx.translate(this.simulation.Position.getX(), this.simulation.Position.getY());
//            g.translate(tx.getTranslateX(), tx.getTranslateY());
//            this.simulation.Position = new Point2D.Double(this.startPoint.getX() + e.getX(),this.startPoint.getY() + e.getY());
//            this.startPoint = new Point2D.Double(this.simulation.Position.getX() - e.getX(), this.simulation.Position.getY() - e.getY());
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                g.translate(10,0);
                this.simulation.Position = new Point2D.Double(this.simulation.Position.getX() - 10, this.simulation.Position.getY());
            }
            if (e.getButton().equals(MouseButton.SECONDARY)) {
                g.translate(-10,0);
                this.simulation.Position = new Point2D.Double(this.simulation.Position.getX() + 10, this.simulation.Position.getY());
            }
        });
        this.setOnScroll(e -> {
            if (e.getDeltaY() > 0) {
                g.translate(0, -10);
                this.simulation.Position = new Point2D.Double(this.simulation.Position.getX(), this.simulation.Position.getY() + 10);
            }
            if (e.getDeltaY() < 0) {
                g.translate(0,10);
                this.simulation.Position = new Point2D.Double(this.simulation.Position.getX(), this.simulation.Position.getY() - 10);
            }
        });
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
        this.simulation.update(deltaTime);
    }
    public void draw(FXGraphics2D graphics){this.simulation.draw(graphics); }
}

