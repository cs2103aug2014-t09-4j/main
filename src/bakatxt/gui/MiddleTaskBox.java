//@author A0116538A

package bakatxt.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import bakatxt.core.Task;

/**
 * This class dictates the color and shape of the box which the task will be put in.
 * This box is specifically for a box in the middle.
 *
 */
class MiddleTaskBox extends TaskBox {

    public MiddleTaskBox(Task task, Color backgroundColor) {
        super(task, backgroundColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(getBackground());
        g2d.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        g2d.dispose();
        super.paintComponent(g);
    }
}
