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
 * This box is specifically for a bottom most box.
 *
 */
final class FinalTaskBox extends TaskBox {

    public FinalTaskBox(Task task, int index, Color backgroundColor) {
        super(task, index, backgroundColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(getBackground());
        g2d.setRenderingHints(UIHelper.antiAlias());
        g2d.fill(new BottomRounded(getWidth(), getHeight()));
        g2d.dispose();
        super.paintComponent(g);
    }

    /**
     * This class draws curved corners for the bottom corners of a rectangle
     *
     */
    class BottomRounded extends Path2D.Double {

        public BottomRounded(double width, double height) {
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
