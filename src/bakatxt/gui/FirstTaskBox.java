//@author A0116538A

package bakatxt.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import bakatxt.core.Task;
import bakatxt.gui.look.UIHelper;

/**
 * This class dictates the color and shape of the box which the task will be put in.
 * This box is specifically for a top most box.
 *
 */
final class FirstTaskBox extends TaskBox {

    public FirstTaskBox(Task task, int index, Color backgroundColor) {
        super(task, index, backgroundColor);
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

    class TopRounded extends Path2D.Double {

        public TopRounded(double width, double height) {
            moveTo(0, height);
            lineTo(0, UIHelper.WINDOW_ROUNDNESS);
            curveTo(0, 0, 0, 0, UIHelper.WINDOW_ROUNDNESS, 0);
            lineTo(width - UIHelper.WINDOW_ROUNDNESS, 0);
            curveTo(width, 0, width, 0, width, UIHelper.WINDOW_ROUNDNESS);
            lineTo(width, height);
            lineTo(0, height);
            closePath();
        }
    }
}
