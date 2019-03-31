package simulation;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.Resizable;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Camera {
    private Point2D centerPoint = new Point2D.Double(0,0);
    private double zoom = 1;
    private double rotation = 0;
    private Point2D lastMousePos;
    private Canvas canvas;
    private Resizable resizable;
    private FXGraphics2D g2d;

    public Camera(Canvas canvas, Resizable resizable, FXGraphics2D g2d) {
        this.canvas = canvas;
        this.resizable = resizable;
        this.g2d = g2d;
        canvas.setOnMousePressed(e -> {lastMousePos = new Point2D.Double(e.getX(), e.getY());});
        canvas.setOnMouseDragged(e -> mouseDragged(e));
        canvas.setOnScroll(e-> mouseScroll(e));
    }

    public AffineTransform getTransform(int windowWidth, int windowHeight)  {
        AffineTransform tx = new AffineTransform();
        tx.translate(windowWidth/2, windowHeight/2);
        tx.scale(zoom, zoom);
        tx.translate(centerPoint.getX(), centerPoint.getY());
        tx.rotate(rotation);
        return tx;
    }

    public void mouseDragged(MouseEvent e) {
        if(e.getButton() == MouseButton.PRIMARY) {
            centerPoint = new Point2D.Double(
                    centerPoint.getX() - (lastMousePos.getX() - e.getX()) / zoom,
                    centerPoint.getY() - (lastMousePos.getY() - e.getY()) / zoom
            );
            lastMousePos = new Point2D.Double(e.getX(), e.getY());

        }
    }

    public void mouseScroll(ScrollEvent e) {
        zoom *= (1 + e.getDeltaY()/250.0f);
    }
}
