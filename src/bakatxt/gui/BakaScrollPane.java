//@author A0116538A

package bakatxt.gui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * This class gives an invisible vertical scrollbar to a component when it's
 * maximum height is reached
 *
 */
class BakaScrollPane extends JScrollPane {

    private static final int WIDTH = UIHelper.WINDOW_X - 4 * UIHelper.BORDER;

    public BakaScrollPane(JComponent component, int initialHeight) {
        super(component);
        component.setAutoscrolls(true);
        setComponentSizeBasedOnHeight(initialHeight);
        setViewportView(component);
        setInvisiblePane();
        setScrollBars();
    }

    /**
     * @param height is to be the height of the BakaScrollPane
     */
    protected void setComponentSizeBasedOnHeight(int height) {

        assert (height >= 0) : "height of pane must not be less than zero";

        if (height > UIHelper.WINDOW_Y) {
            height = UIHelper.WINDOW_Y;
        }
        this.setPreferredSize(new Dimension(WIDTH, height));
    }

    /**
     * Make the background of the pane invisible as well as the borders to be
     * non-existent
     */
    private void setInvisiblePane() {
        setOpaque(false);
        getViewport().setOpaque(false);
        getViewport().setBackground(UIHelper.TRANSPARENT);
        setBorder(BorderFactory.createEmptyBorder());
    }

    /**
     * Remove horizontal scrolling and set the vertical scrollbar to invisible
     */
    private void setScrollBars() {
        setVerticalScrollBar(invisibleScrollBar());
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    /**
     * Use a 'hack' to have an invisible scrollbar
     * @return an invisible vertical scrollbar
     */
    private static JScrollBar invisibleScrollBar() {
        JScrollBar invisibleScrollBar = new JScrollBar(JScrollBar.VERTICAL) {

            @Override
            public boolean isVisible() {
              return true;
            }
          };
          return invisibleScrollBar;
    }
}
