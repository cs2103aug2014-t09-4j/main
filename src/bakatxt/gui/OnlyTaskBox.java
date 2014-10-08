//@author A0116538A

package bakatxt.gui;

import java.awt.Color;
import java.awt.Graphics;

import bakatxt.core.Task;

/**
 * This class dictates the color and shape of the box which the task will be put in.
 * This box is specifically for box that is by itself.
 *
 */
class OnlyTaskBox extends TaskBox {

    public OnlyTaskBox(Task task, Color backgroundColor) {
        super(task, backgroundColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        UIHelper.paintRoundedRectangle(g, getBackground(), getWidth(), getHeight());
        super.paintComponent(g);
    }
}
