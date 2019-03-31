package simulation;

import Data.tilemap.RoomObject;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Room extends Location{
    private ArrayList<Chair> chairs;
    private String name;

    public Room(RoomObject roomObject){
        chairs = new ArrayList<>();
        for (Point2D chairposition : roomObject.getChairpositions()) {
            chairs.add(new Chair(chairposition));
        }

        this.name = roomObject.getName();


    }

    public ArrayList<Chair> getChairs() {
        return chairs;
    }

    public String getName() {
        return name;
    }

    public Chair getEmptyChair(){
        for (Chair chair : chairs) {
            if(!chair.isReserved()){
                chair.reserve();
                return chair;

            }
        }
        // TODO: 3/31/2019
        return new Chair(new Point2D.Double(0,0));
    }
}
