package GUI;

import Data.Lesson;
import javafx.geometry.Point2D;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.AffineTransform;


//Auteur: Sebastiaan
//Deze klasse dient om een versleepbaar blok te creeren, met een vaste positie.

class LessonBlock
{
    private Shape shape;
    private Point2D position;
    private float rotation;
    private float scale;
    private Lesson lesson;


    public LessonBlock(Shape shape, Point2D position, float rotation, float scale, Lesson lesson)
    {
        this.shape = shape;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.lesson = lesson;
    }

    public void draw(FXGraphics2D g2d)
    {
        g2d.draw(getTransformedShape());
    }

    public Shape getTransformedShape()
    {
        return getTransform().createTransformedShape(shape);
    }

    public void setPosition(Point2D newPosition)
    {
        this.position = newPosition;
    }

    public Point2D getPosition(){
        return this.position;
    }

    public AffineTransform getTransform()
    {
        AffineTransform tx = new AffineTransform();
        tx.translate(position.getX(), position.getY());
        tx.rotate(rotation);
        tx.scale(scale,scale);
        return tx;
    }

    public DraggedBlock getDraggedBlock(){
        DraggedBlock draggedBlock = new DraggedBlock(this.shape,this.position,this.position,this.rotation,this.scale, this.lesson);
        return draggedBlock;
    }

    public Lesson getLesson(){
        return this.lesson;
    }
}