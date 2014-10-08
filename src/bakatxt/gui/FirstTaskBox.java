//@author A0116538A

package bakatxt.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import bakatxt.core.Task;

/**
 * This class dictates the color and shape of the box which the task will be put in.
 * This box is specifically for a top most box.
 *
 */
class FirstTaskBox extends TaskBox {

    public FirstTaskBox(Task task, Color backgroundColor) {
        super(task, backgroundColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(getBackground());
        g2d.setRenderingHints(UIHelper.antiAlias());
        g2d.fill(new TopRounded(getWidth(), getHeight()));
        g2d.dispose();
        super.paintComponent(g);
    }

    // TODO fix topRounded method
    class TopRounded extends Path2D.Double {

        public TopRounded(double width, double height) {
            moveTo(0, 0);
            lineTo(width, 0);
            lineTo(width, height - UIHelper.WINDOW_ROUNDNESS);
            curveTo(width, height, width, height, width - UIHelper.WINDOW_ROUNDNESS, height);
            lineTo(UIHelper.WINDOW_ROUNDNESS, height);
            curveTo(0, height, 0, height, 0, height - UIHelper.WINDOW_ROUNDNESS);
            lineTo(0, 0);
            closePath();
        }
    }
}
