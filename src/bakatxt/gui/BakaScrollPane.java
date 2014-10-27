//@author A0116538A

package bakatxt.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * This class gives an invisible vertical scrollbar to a component when it's
 * maximum height is reached
 *
 */
class BakaScrollPane extends JScrollPane {

    private static final int WIDTH = UIHelper.WINDOW_X - 4 * UIHelper.BORDER;
    private static final int BAR_WIDTH = 9;
    private static final int BAR_ROUNDNESS = 9;
    private static final int VANISH_TIMER = 1000;

    private Timer _timer;

    public BakaScrollPane(JComponent component, int initialHeight) {
        super(component);
        component.setAutoscrolls(true);
        setComponentSizeBasedOnHeight(initialHeight);
        setViewportView(component);
        setInvisiblePane();
        setScrollBars();
        setComponentZOrder(getViewport(), 1);
        setViewBox();
        setTimeout();
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
        verticalScrollBar.setOpaque(false);
        verticalScrollBar.setUI(new DisappearingScrollBar());
        verticalScrollBar.addAdjustmentListener(new AdjustmentListener(){
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                verticalScrollBar.setUI(new DisappearingScrollBar());
                _timer.stop();
                _timer.start();
            }
        });
        setComponentZOrder(verticalScrollBar, 0);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    /**
     * Sets the location of the scrollbar.
     * Expand the viewbox such that the scrollbar is within and not outside the
     * box.
     */
    private void setViewBox() {
        setLayout(new ScrollPaneLayout() {
            @Override
            public void layoutContainer(Container parent) {
                JScrollPane scrollPane = (JScrollPane)parent;

                Rectangle viewBox = scrollPane.getBounds();
                viewBox.x = 0;
                viewBox.y = 0;

                Rectangle scrollTrack = new Rectangle();
                scrollTrack.width  = BAR_WIDTH;
                scrollTrack.height = viewBox.height;
                scrollTrack.x = viewBox.x + viewBox.width - scrollTrack.width;
                scrollTrack.y = viewBox.y;

                if(viewport != null) {
                    viewport.setBounds(viewBox);
                }
                if(vsb != null) {
                    vsb.setVisible(true);
                    vsb.setBounds(scrollTrack);
                }
            }
        });
    }

    private void setTimeout() {
        _timer = new Timer(VANISH_TIMER, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                verticalScrollBar.setUI(null);
                _timer.stop();
            }
        });
    }

    private static class DisappearingScrollBar extends BasicScrollBarUI {

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return invisibleButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return invisibleButton();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            UIHelper.paintRoundedRectangle(g, UIHelper.SCROLLBAR, thumbBounds.x,
                                           thumbBounds.y, BAR_WIDTH,
                                           thumbBounds.height, BAR_ROUNDNESS);
        }

        private static JButton invisibleButton() {
            JButton invisibleButton = new JButton() {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(0, 0);
                }
            };
            return invisibleButton;
        }
    }
}
