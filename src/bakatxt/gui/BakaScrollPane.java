package bakatxt.gui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

class BakaScrollPane extends JScrollPane {

    private static final int MAX_HEIGHT = 768;
    private static final int WIDTH = UIHelper.WINDOW_X - 4 * UIHelper.BORDER;

    public BakaScrollPane(JComponent contained, int initialHeight) {
        super(contained);
        contained.setAutoscrolls(true);
        this.setComponentSizeBasedOnHeight(initialHeight);
        this.setViewportView(contained);
        this.setOpaque(false);
        this.getViewport().setOpaque(false);
        this.getViewport().setBackground(UIHelper.TRANSPARENT);
        this.setBorder(BorderFactory.createEmptyBorder());

        this.setVerticalScrollBar(invisibleScrollBar());
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    protected void setComponentSizeBasedOnHeight(int height) {

        assert (height > 0) : "height of pane must not be less than zero";

        if (height > MAX_HEIGHT) {
            height = MAX_HEIGHT;
        }
        this.setPreferredSize(new Dimension(WIDTH, height));
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
