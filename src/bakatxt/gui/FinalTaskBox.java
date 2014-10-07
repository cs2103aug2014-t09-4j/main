//@author A0116538A

package bakatxt.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import bakatxt.core.Task;

//TODO comments

class FinalTaskBox extends TaskBox {

    public FinalTaskBox(Task task, boolean isEven) {
        super(task, isEven);
    }

    @Override
    protected void buildBackground(Graphics g) {
        if(isEven_) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(getBackground());
            g2d.setRenderingHints(UIHelper.antiAlias());
            g2d.fill(new BottomRounded(getWidth(), getHeight()));
            g2d.dispose();
        }
    }

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
