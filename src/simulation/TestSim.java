package simulation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestSim extends Application
{
    public static void main(String[] args) {
        launch(TestSim.class);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(new SimulationPane()));
        primaryStage.show();
    }
}
