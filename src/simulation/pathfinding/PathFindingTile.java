package simulation.pathfinding;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class PathFindingTile {

    private int size;
    private int xPos;
    private int yPos;
    private Shape shape;
    private boolean selectedTile = false;
    private int value;
    private boolean isWall;


    public PathFindingTile(int size, int xPos, int yPos, boolean isWall) {
        this.size = size;
        this.xPos = xPos;
        this.yPos = yPos;
        this.shape = new Rectangle(new Rectangle(xPos,yPos,size,size));
        this.value = 999;
        this.isWall = isWall;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public boolean isSelectedTile() {
        return selectedTile;
    }

    public void setSelectedTile(boolean selectedTile) {
        this.selectedTile = selectedTile;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }

    public void draw(Graphics2D g2d){
        if (isWall){
//            g2d.fill(shape);
        } else {
            g2d.setFont(new Font("", 1, 10));
            g2d.drawString(String.valueOf(this.value), this.xPos + 1, this.yPos + size / 2 + 4);
            g2d.draw(shape);
        }
    }

    public void update(){

    }


}
