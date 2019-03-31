package simulation;

import simulation.pathfinding.DijkstraMap;

import java.time.LocalTime;
import java.util.Map;

public class Teacher extends Actor {


    @Override
    protected void arrivedAtDestination(double deltaTime) {

    }

    @Override
    public void chooseDestination(LocalTime time, Map<String, DijkstraMap> dijkstraMaps, Map<String, Room> roomMap) {

    }
}
