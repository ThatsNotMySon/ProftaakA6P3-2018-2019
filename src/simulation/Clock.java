package simulation;

import java.awt.*;
import java.awt.geom.*;

//Auteur : Sebastiaan

public class Clock {

    private TimeControl timeControl;
    private Point2D location = new Point2D.Double(50,50);
    Shape clockBackground = new Ellipse2D.Double(0,0,100,100);
    Area clock = new Area();


    public Clock(TimeControl timecontrol){
        this.timeControl = timecontrol;
        for(int i = 0; i < 12; i++){
            Shape notch = new Rectangle2D.Double(-1,0,2,10);
            AffineTransform tx = new AffineTransform();
            tx.translate(this.location.getX(),this.location.getY());
            tx.rotate((Math.PI/6)*i);
            tx.translate(0,50);
            tx.rotate(Math.PI);
            clock.add(new Area(tx.createTransformedShape(notch)));
        }
    }

    public void draw(Graphics2D graphics){


        Area arms = new Area();
        //small arm
        AffineTransform arm = new AffineTransform();
        arm.translate(this.location.getX(),this.location.getY());
        arm.rotate(Math.PI);
        double theta = Math.PI*2/12*((double)this.timeControl.getHour()+(double)this.timeControl.getMinute()/60);
        arm.rotate(theta);
        arms.add(new Area(arm.createTransformedShape(new Rectangle2D.Double(-1,0,2,25))));
        //large arm
        arm = new AffineTransform();
        arm.translate(this.location.getX(),this.location.getY());
        arm.rotate(Math.PI);
        theta = Math.PI*2/60*(double)this.timeControl.getMinute();
        arm.rotate(theta);
        arms.add(new Area(arm.createTransformedShape(new Rectangle2D.Double(-1,0,2,45))));

        graphics.setColor(Color.WHITE);
        graphics.fill(clockBackground);
        graphics.setColor(Color.BLACK);
        graphics.draw(clockBackground);
        graphics.fill(clock);
        graphics.fill(arms);
    }

    public TimeControl getTimeControl() {
        return timeControl;
    }

    public void setTimeControl(TimeControl timeControl) {
        this.timeControl = timeControl;
    }

    public Point2D getLocation() {
        return location;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }

    public Shape getClockBackground() {
        return clockBackground;
    }

    public void setClockBackground(Shape clockBackground) {
        this.clockBackground = clockBackground;
    }

    public Area getClock() {
        return clock;
    }

    public void setClock(Area clock) {
        this.clock = clock;
    }
}
