//@author A0116538A

package bakatxt.gui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
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

    // TODO should not need to print this, rather, take the thing to be printed from logic
    private static final String MESSAGE_WELCOME = "Welcome to BakaTXT! For help "
                                                + "please type help in the box above";

    private static Input _input;
    private static Contents _contents;
    private static BakaScrollPane _bakaScrollPane;
    private static FormattedText _alertMessage;

    public BakaPanel(LinkedList<Task> tasks) {

        _input = new Input();
        _contents = new Contents(tasks);
        _bakaScrollPane = new BakaScrollPane(_contents, _contents.getSize().height);
        _alertMessage = setAlertMessageText(MESSAGE_WELCOME);

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

        // TODO set alert message to the specific alert
         _alertMessage.updateContents(MESSAGE_WELCOME);

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
        setAlertMessage(layout);
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
        layout.weighty = 0.05;
        layout.gridx = 0;
        layout.gridy = 0;
        layout.insets = new Insets(2 * UIHelper.BORDER, 2 * UIHelper.BORDER,
                UIHelper.BORDER, 2 * UIHelper.BORDER);
        this.add(_input, layout);
    }

    /**
     * @param alertMessage is the message to put in the layout specified
     */
    private void setAlertMessage(GridBagConstraints layout) {
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.PAGE_START;
        layout.weightx = 0.0;
        layout.weighty = 0.01;
        layout.gridx = 0;
        layout.gridy = 1;
        layout.gridheight = 1;
        layout.gridwidth = 1;
        this.add(_alertMessage, layout);
    }

    /**
     * Tell the GridBag where to draw the BakaScrollPane containing the content box
     *
     * @param layout is the GridBag which is being drawn on
     */
    private void setContent(GridBagConstraints layout) {
        layout.fill = GridBagConstraints.BOTH;
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridx = 0;
        layout.gridy = 2;
        layout.insets = new Insets(0, 2 * UIHelper.BORDER,
                2 * UIHelper.BORDER, 2 * UIHelper.BORDER);
        this.add(_bakaScrollPane, layout);
    }

    /**
     * @param message is the string to style
     * @return a alert message FormattedText with the string
     */
    private static FormattedText setAlertMessageText (String message) {
        return new FormattedText(message, UIHelper.PRESET_TYPE_DEFAULT,
                UIHelper.PRESET_SIZE_DEFAULT, UIHelper.PRESET_COLOR_ALERT);
    }

    /**
     * Shake the input box when an error in input is detected
     *
     * @param isBadInput is the boolean that decides whether or not to shake the box
     */
    protected void shakeInputBox(final boolean isBadInput) {

        if(isBadInput) {
            final Point initialLocation = UIHelper.INPUT_LOCATION;
            final int movementDelay = 20;
            final int iterations = 8;

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < iterations; i++) {
                        shakeOneIteration(initialLocation, movementDelay, iterations - i);
                    }
                }
            };
            Thread t = new Thread(r);
            t.start();
        }
    }

    /**
     * Shake the component right, then back to initial, then left, then back to initial
     * again.
     *
     * @param initialLocation is the initial location of the component to be shaked
     * @param movementDelay is the delay between shakes
     * @param xMovement is the amount to move the component by
     */
    private static void shakeOneIteration(final Point initialLocation,
            final int movementDelay, int xMovement) {
        try {
            moveBox(new Point(initialLocation.x + xMovement, initialLocation.y));
            Thread.sleep(movementDelay);
            moveBox(initialLocation);
            Thread.sleep(movementDelay);
            moveBox(new Point(initialLocation.x - xMovement, initialLocation.y));
            Thread.sleep(movementDelay);
            moveBox(initialLocation);
            Thread.sleep(movementDelay);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Moves the input box to the set location
     *
     * @param p is the Point to move the box to
     */
    private static void moveBox(final Point p) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _input.setLocation(p);
            }
        });
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