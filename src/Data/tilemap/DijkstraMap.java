package Data.tilemap;

import java.awt.geom.Point2D;
import java.util.*;

public class DijkstraMap {
    private int width;
    private int[][] dMap;
    private int originX;
    private int originY;
    private String locationID;
    private int[] data;
    private int length;
    private Queue<Point2D.Double> queue;

    public DijkstraMap(int originX, int originY, String locationID, int[] data, int width){

        this.dMap = new int[width][data.length/width];
        this.originX = originX;
        this.originY = originY;
        this.data = data;
        this.width = width;
        this.locationID = locationID;
        this.length = data.length/width;

        // Set Matrix to highest value
        for (int[] ints : dMap) {
            for (int anInt : ints) {
                anInt = -1;
            }
        }





        int newValue = 0;
        queue = new PriorityQueue<>();
        queue.add(new Point2D.Double(originX, originY));

        while(!queue.isEmpty()) {
            calculateAdjacentTiles(originX, originY, newValue);

        }

        System.out.println("here");

        print();

    }


    public void calculateAdjacentTiles(int x, int y, int newValue){
        if (x >= 0 && x < width && y >= 0 && y < length) {
            if(dMap[x][y] == Integer.MAX_VALUE) {
                if (data[width * y + x] == 0) {
                    int lowestSurrounding = getLowestSurrounding(x, y);
                    dMap[x][y] = lowestSurrounding + 1;
                    System.out.println(lowestSurrounding+1);
                }
            }
            if(dMap[x][y] == 0){
                if (data[width * y + x] == 0) {
                    dMap[x][y] = newValue;
                    getLowestSurrounding(x, y);
                }
            }
        }
    }


    public int getLowestSurrounding(int x, int y){
        int lowest = Integer.MAX_VALUE;

        if (x-1 >= 0 && x-1 < width) {
            if(lowest>dMap[x-1][y]) {
                lowest = dMap[x-1][y];
            }
            queue.add(new Point2D.Double(x-1, y));
        }

        if (x+1 >= 0 && x+1 < width) {
            if(lowest>dMap[x+1][y]) {
                lowest = dMap[x + 1][y];
            }
            queue.add(new Point2D.Double(x+1, y));
        }

        if(y-1 >= 0 && y-1 < length){
            if(lowest>dMap[x][y-1]) {
                lowest = dMap[x ][y-1];
            }
            queue.add(new Point2D.Double(x, y-1));
        }

        if(y+1 >= 0 && y+1 < length){
            if(lowest>dMap[x][y+1]) {
                lowest = dMap[x ][y+1];
            }
            queue.add(new Point2D.Double(x, y+1));
        }

        return lowest;
    }


    public void print(){
        for(int i = 0; i < this.length; i++){
            for(int j = 0; j < this.width; j++){
                System.out.print(dMap[j][i] + ", ");
            }
            System.out.println(" ");
        }
    }

}
