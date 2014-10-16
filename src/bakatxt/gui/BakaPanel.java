//@author A0116538A

package bakatxt.gui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import bakatxt.core.Task;

/**
 * This class is used as the actual window (i.e, what the user perceives as a window)
 * for BakaTXT. Apart from that, it also draws the input box and the rest of the
 * content.
 *
 */
class BakaPanel extends JPanel {

    private static Input _input;
    private static Contents _contents;
    private static BakaScrollPane _bakaScrollPane;

    public BakaPanel(LinkedList<Task> tasks) {

        _input = new Input();
        _contents = new Contents(tasks);
        _bakaScrollPane = new BakaScrollPane(_contents, _contents.getSize().height);

        setOpaque(false);
        setMaximumSize(UIHelper.WINDOW_SIZE);
        setBackground(UIHelper.GRAY_MEDIUM);
        setLayout(new GridBagLayout());
        addComponentsToPane();
    }

    /**
     * @return the input box
     */
    protected Input getInput() {
        return _input;
    }

    /**
     * This method refreshes what is displayed on the GUI
     *
     * @param session is the logic module we are retrieving the new information from
     */
    protected void refreshContents(LinkedList<Task> tasks) {

         _contents.removeAll();
         _contents.updateContents(tasks);

         _bakaScrollPane.setComponentSizeBasedOnHeight(_contents.getSize().height);
         _bakaScrollPane.revalidate();
         _bakaScrollPane.repaint();

         SwingUtilities.invokeLater(new Runnable() {
             @Override
            public void run() {
                 _bakaScrollPane.getVerticalScrollBar().setValue(0);
             }
          });
    }

    /**
     * This method dictates the area where the input box and the content is allowed
     * to be
     */
    private void addComponentsToPane() {
        GridBagConstraints layout = new GridBagConstraints();

        setInputBox(layout);
        setContent(layout);
    }

    /**
     * Tell the GridBag where to draw the input box
     *
     * @param layout is the GridBag which is being drawn on
     */
    private void setInputBox(GridBagConstraints layout) {
        layout.fill = GridBagConstraints.BOTH;
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.weightx = 1.0;
        layout.weighty = 0.03;
        layout.gridx = 0;
        layout.gridy = 0;
        layout.insets = new Insets(2 * UIHelper.BORDER, 2 * UIHelper.BORDER,
                UIHelper.BORDER, 2 * UIHelper.BORDER);
        this.add(_input, layout);
    }

    /**
     * Tell the GridBag where to draw the content box
     *
     * @param layout is the GridBag which is being drawn on
     */
    private void setContent(GridBagConstraints layout) {
        layout.fill = GridBagConstraints.BOTH;
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridx = 0;
        layout.gridy = 1;
        layout.insets = new Insets(0, 2 * UIHelper.BORDER,
                2 * UIHelper.BORDER, 2 * UIHelper.BORDER);
        //this.add(_contents, layout);
        this.add(_bakaScrollPane, layout);
    }

    /**
     * Make the corners of the BakaPanel round
     */
    @Override
    protected void paintComponent(Graphics g) {
        UIHelper.paintRoundedRectangle(g, getBackground(), getWidth(), getHeight());
        super.paintComponent(g);
    }
}