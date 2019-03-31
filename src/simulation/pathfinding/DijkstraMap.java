package simulation.pathfinding;

import simulation.pathfinding.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class DijkstraMap {

    private int width;
    private int height;
    private int tileSize;
    private PathFindingTile[][] tiles2D;
    private LinkedList<PathFindingTile> queue = new LinkedList<>();
    private HashMap<PathFindingTile, Integer> visited = new HashMap<>();
    private PathFindingTile startingTile;

    public DijkstraMap(ArrayList<PathFindingTile> tiles, int width, int height, int tileSize){
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        tiles2D = new PathFindingTile[height][width];
        for (int y = 0; y < height; y++){
            for (int x = 0 ; x < width; x++){
                tiles2D[y][x] = tiles.get(y * height + x);
            }
        }
        startingTile = tiles2D[0][0];
        execute(startingTile.getxPos()/tileSize, startingTile.getyPos()/tileSize);
    }

    public DijkstraMap(ArrayList<PathFindingTile> tiles, int width, int height, int tileSize, int xPosFromStartingTile, int yPosFromStartingTile){
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
        tiles2D = new PathFindingTile[height][width];
        for (int y = 0; y < height; y++){
            for (int x = 0 ; x < width; x++){
                tiles2D[y][x] = tiles.get(y * height + x);
            }
        }
        startingTile = tiles2D[0][0];
        execute(xPosFromStartingTile, yPosFromStartingTile);
    }

    public void execute(int xPosFromStartingTileInArray, int yPosFromStartingTileInArray){
        queue.clear();
        visited.clear();

        startingTile = tiles2D[yPosFromStartingTileInArray][xPosFromStartingTileInArray];
        if (!startingTile.isWall()) {
            startingTile.setValue(0);
            visited.put(startingTile, 0);
            int xPosInArray = startingTile.getxPos() / tileSize;
            int yPosInArray = startingTile.getyPos() / tileSize;

            queue.addAll(getNeighbouringTilesNotVisited(xPosInArray, yPosInArray));
            while (!queue.isEmpty()) {
                PathFindingTile tile1 = queue.poll();

                int xPosInArrayTile1 = tile1.getxPos() / tileSize;
                int yPosInArrayTile1 = tile1.getyPos() / tileSize;
                tile1.setValue(getLowestNumberFromNeighbouringTiles(xPosInArrayTile1, yPosInArrayTile1) + 1);
                visited.put(tile1, tile1.getValue());
                queue.addAll(getNeighbouringTilesNotVisited(xPosInArrayTile1, yPosInArrayTile1));

            }
        }
    }

    public ArrayList<PathFindingTile> getNeighbouringTilesNotVisited(int xPosTileInArray, int yPosTileInArray){
        ArrayList<PathFindingTile> tiles = new ArrayList<>();
        //System.out.println("xPos: " + xPosTileInArray + ", yPos: " + yPosTileInArray);

        try {
            PathFindingTile leftTile = tiles2D[yPosTileInArray - 1][xPosTileInArray];
            if (!visited.containsKey(leftTile) && !queue.contains(leftTile) && !leftTile.isWall()){
                tiles.add(leftTile);
            }
        } catch(ArrayIndexOutOfBoundsException e){
            // System.out.println("Left tile out of bounds");
        }

        try {
            PathFindingTile rightTile = tiles2D[yPosTileInArray + 1][xPosTileInArray];
            if (!visited.containsKey(rightTile) && !queue.contains(rightTile) && !rightTile.isWall()){
                tiles.add(rightTile);
            }
        } catch(ArrayIndexOutOfBoundsException e){
            //System.out.println("Right tile out of bounds");
        }

        try {
            PathFindingTile lowerTile = tiles2D[yPosTileInArray][xPosTileInArray - 1];
            if (!visited.containsKey(lowerTile) && !queue.contains(lowerTile) && !lowerTile.isWall()){
                tiles.add(lowerTile);
            }
        } catch(ArrayIndexOutOfBoundsException e){
            //System.out.println("Lower tile out of bounds");
        }

        try {
            PathFindingTile upperTile = tiles2D[yPosTileInArray][xPosTileInArray + 1];
            if (!visited.containsKey(upperTile) && !queue.contains(upperTile) && !upperTile.isWall()) {
                tiles.add(upperTile);
            }
        } catch(ArrayIndexOutOfBoundsException e){
            //System.out.println("Upper tile out of bounds");
        }



        return tiles;
    }

    public ArrayList<PathFindingTile> getNeighbouringTilesVisited(int xPosTileInArray, int yPosTileInArray){
        ArrayList<PathFindingTile> tiles = new ArrayList<>();


        try {
            PathFindingTile leftTile = tiles2D[yPosTileInArray - 1][xPosTileInArray];
            if (visited.containsKey(leftTile) && !queue.contains(leftTile)){
                tiles.add(leftTile);
            }
        } catch(ArrayIndexOutOfBoundsException e){

        }

        try {
            PathFindingTile rightTile = tiles2D[yPosTileInArray + 1][xPosTileInArray];
            if (visited.containsKey(rightTile) && !queue.contains(rightTile)){
                tiles.add(rightTile);
            }
        } catch(ArrayIndexOutOfBoundsException e){

        }

        try {
            PathFindingTile lowerTile = tiles2D[yPosTileInArray][xPosTileInArray - 1];
            if (visited.containsKey(lowerTile) && !queue.contains(lowerTile)){
                tiles.add(lowerTile);
            }
        } catch(ArrayIndexOutOfBoundsException e){

        }

        try {
            PathFindingTile upperTile = tiles2D[yPosTileInArray][xPosTileInArray + 1];
            if (visited.containsKey(upperTile) && !queue.contains(upperTile)) {
                tiles.add(upperTile);
            }
        } catch(ArrayIndexOutOfBoundsException e){

        }



        return tiles;
    }

    public int getLowestNumberFromNeighbouringTiles(int xPosTile, int yPosTile){
        ArrayList<PathFindingTile> tiles = getNeighbouringTilesVisited(xPosTile, yPosTile);

        int lowestNumber = tiles.get(0).getValue();

        for (PathFindingTile tile : tiles){
            if (tile.getValue() < lowestNumber){
                lowestNumber = tile.getValue();
            }
        }
        return lowestNumber;
    }

    public ArrayList<PathFindingTile> getNeighbouringTilesFromCanvasPosition(double xPos, double yPos){
        ArrayList<PathFindingTile> tiles = new ArrayList<>();

        try{
            PathFindingTile leftTile = tiles2D[(int)yPos/tileSize][(int)(xPos/tileSize - 1)];
            tiles.add(leftTile);
        } catch(ArrayIndexOutOfBoundsException e){
//            e.printStackTrace();
        }

        try{
            PathFindingTile upperTile = tiles2D[(int)(yPos/tileSize - 1)][(int)xPos/tileSize];
            tiles.add(upperTile);
        } catch(ArrayIndexOutOfBoundsException e){
//            e.printStackTrace();
        }

        try{
            PathFindingTile rightTile = tiles2D[(int)yPos/tileSize][(int)(xPos/tileSize + 1)];
            tiles.add(rightTile);
        } catch(ArrayIndexOutOfBoundsException e){
//            e.printStackTrace();
        }

        try{
            PathFindingTile lowerTile = tiles2D[(int)(yPos/tileSize + 1)][(int)xPos/tileSize];
            tiles.add(lowerTile);
        } catch(ArrayIndexOutOfBoundsException e){
//            e.printStackTrace();
        }



        return tiles;
    }

    public PathFindingTile getTileWithLowestValue(ArrayList<PathFindingTile> tiles){

        if (!tiles.isEmpty()) {
            try {
                PathFindingTile lowestValueTile = tiles.get(0);
                for (PathFindingTile tile1 : tiles) {
                    int value = tile1.getValue();
                    if (value < lowestValueTile.getValue()) {
                        lowestValueTile = tile1;
                    }
                }

                return lowestValueTile;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            return null;
        }
        return null;
    }

    public Point2D getDirection(double xPos, double yPos){
        PathFindingTile directionTile = getTileWithLowestValue(getNeighbouringTilesFromCanvasPosition(xPos, yPos));
        if (directionTile == null){
            return new Point2D.Double(xPos,yPos);
        }
        return new Point2D.Double(directionTile.getxPos() + tileSize / 2, directionTile.getyPos() + tileSize / 2);
    }

    public PathFindingTile getStartingTile() {
        return startingTile;
    }

    public void setStartingTile(PathFindingTile startingTile) {
        this.startingTile = startingTile;
    }

    public boolean isTileAWall(double xPosTile, double yPosTile){
        return tiles2D[(int)yPosTile][(int)xPosTile].isWall();
    }

    public boolean isLocationAWall(double xPos, double yPos){
        return isTileAWall((xPos/16), (yPos/16));
    }

}
