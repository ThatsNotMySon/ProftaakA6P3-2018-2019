package GUI;

import Data.Lesson;
import javafx.geometry.Point2D;

import java.awt.*;

public class DraggedBlock extends LessonBlock {

    private Point2D oldPosition;

    public DraggedBlock(Shape shape, Point2D position, Point2D oldPosition, float rotation, float scale, Lesson lesson) {
        super(shape, position, rotation, scale, lesson);
        this.oldPosition = oldPosition;

    }
    public DraggedBlock(Shape shape, Point2D position, float rotation, float scale, Lesson lesson) {
        super(shape, position, rotation, scale, lesson);
    }

    public Point2D getOldPosition(){
        return this.oldPosition;
    }

    public void setOldPosition(Point2D oldPosition){
        this.oldPosition = oldPosition;
    }
}
