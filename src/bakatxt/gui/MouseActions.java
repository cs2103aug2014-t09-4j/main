package bakatxt.gui;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class MouseActions implements MouseListener, MouseMotionListener {
    JComponent target;
    Point dragMousePosition;
    Point currentMouseLocation;

    public MouseActions(JComponent target) {
        this.target = target;
    }

    public JFrame getFrame(Container target) {
        if (target instanceof JFrame) {
            return (JFrame) target;
        }
        return getFrame(target.getParent());
    }

    Point getScreenLocation(MouseEvent e) {
        Point cursor = e.getPoint();
        Point movedMouseLocation = this.target.getLocationOnScreen();
        return new Point((int) (movedMouseLocation.getX() + cursor.getX()),
                (int) (movedMouseLocation.getY() + cursor.getY()));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.dragMousePosition = this.getScreenLocation(e);
        this.currentMouseLocation = this.getFrame(this.target).getLocation();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // getX(), getY() used to reduce perceived lag due to it being slightly
        // higher precision (float vs int)
        Point current = this.getScreenLocation(e);
        Point offset = new Point(
                (int) (current.getX() - dragMousePosition.getX()),
                (int) (current.getY() - dragMousePosition.getY()));
        JFrame frame = this.getFrame(target);
        Point finalLocation = new Point(
                (int) (this.currentMouseLocation.getX() + offset.getX()),
                (int) (this.currentMouseLocation.getY() + offset.getY()));
        frame.setLocation(finalLocation);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
