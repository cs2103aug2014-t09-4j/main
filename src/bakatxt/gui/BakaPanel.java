//@author A0116538A

package bakatxt.gui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import bakatxt.core.Task;
import bakatxt.gui.look.ThemeReader;
import bakatxt.gui.look.UIAnimator;
import bakatxt.gui.look.UIHelper;
import bakatxt.international.BakaTongue;

/**
 * This class is used as the actual window (i.e, what the user perceives as a window)
 * for BakaTXT. Apart from that, it also draws the input box and the rest of the content.
 *
 */
public class BakaPanel extends JPanel {
    /**
     * BakaTxt welcome message
     */
    private static final String MESSAGE_WELCOME = BakaTongue.getString("MESSAGE_WELCOME");

    private static Input _input;
    private static Contents _contents;
    private static BakaScrollPane _bakaScrollPane;
    private static FormattedText _alertMessage;

    public BakaPanel(List<Task> tasks) {

        _input = new Input();
        _contents = new Contents(tasks);
        _bakaScrollPane = new BakaScrollPane(_contents,
                _contents.getSize().height);
        _alertMessage = setAlertMessageText(MESSAGE_WELCOME);

        setOpaque(false);
        setMaximumSize(UIHelper.WINDOW_SIZE);
        setBackground(ThemeReader.getPanelColor());
        setLayout(new GridBagLayout());
        addComponentsToPane();
    }

    /**
     * @return the input box
     */
    public Input getInput() {
        return _input;
    }

    /**
     * set the text in the input box
     *
     * @param s
     *        is the string to be set
     */
    protected void setInputBoxText(String s) {
        _input.setText(s);
    }

    /**
     * set the text in the alert box
     *
     * @param s
     *        is the string to be set
     */
    protected void updateAlertMessageText(String s) {
        _alertMessage.setText(s);
    }

    /**
     * @return the scrollpane
     */
    public BakaScrollPane getScrollPane() {
        return _bakaScrollPane;
    }

    /**
     * This method refreshes what is displayed on the GUI
     *
     * @param tasks
     *        the tasks we are putting on the GUI
     */
    protected void refreshContents(List<Task> tasks) {

        setBackground(ThemeReader.getPanelColor());

        _input.setTheme();
        _alertMessage.setTheme(ThemeReader.getAlertTheme());

        _contents.removeAll();
        _contents.updateContents(tasks);

        _bakaScrollPane.setComponentSizeBasedOnHeight(_contents
                .getPreferredSize().height);
        _bakaScrollPane.revalidate();
        _bakaScrollPane.repaint();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _bakaScrollPane.getVerticalScrollBar().setValue(0);
            }
        });

        repaint();
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
     * @param layout
     *        the GridBag which is being drawn on
     */
    private void setInputBox(GridBagConstraints layout) {
        layout.fill = GridBagConstraints.BOTH;
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.weightx = 1.0;
        layout.weighty = 0.0;
        layout.gridx = 0;
        layout.gridy = 0;
        layout.insets = new Insets(2 * UIHelper.BORDER, 2 * UIHelper.BORDER,
                UIHelper.BORDER, 2 * UIHelper.BORDER);
        this.add(_input, layout);
    }

    /**
     * @param alertMessage
     *        the message to put in the layout specified
     */
    private void setAlertMessage(GridBagConstraints layout) {
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.PAGE_START;
        layout.weightx = 0.0;
        layout.weighty = 0.0;
        layout.gridx = 0;
        layout.gridy = 1;
        layout.gridheight = 1;
        layout.gridwidth = 1;
        this.add(_alertMessage, layout);
    }

    /**
     * Tell the GridBag where to draw the BakaScrollPane containing the content box
     *
     * @param layout
     *        is the GridBag which is being drawn on
     */
    private void setContent(GridBagConstraints layout) {
        layout.fill = GridBagConstraints.BOTH;
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridx = 0;
        layout.gridy = 2;
        layout.insets = new Insets(0, 2 * UIHelper.BORDER, 2 * UIHelper.BORDER,
                2 * UIHelper.BORDER);
        this.add(_bakaScrollPane, layout);
    }

    /**
     * @param message
     *        is the string to style
     * @return a alert message FormattedText with the string
     */
    private static FormattedText setAlertMessageText(String message) {
        return new FormattedText(message, ThemeReader.getAlertTheme());
    }

    /**
     * Shake the input box when an error in input is detected, flash it if it is
     * successful
     *
     * @param isSuccessful
     *        is the boolean that decides whether or not to shake the box or flash it
     */
    protected void animateInputBox(final boolean isSuccessful) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                final UIAnimator animate = new UIAnimator(_input);
                if (isSuccessful) {
                    animate.flashComponent();
                } else {
                    animate.shakeComponent();
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    /**
     * Make the corners of the BakaPanel round
     */
    @Override
    protected void paintComponent(Graphics g) {
        UIHelper.paintRoundedRectangle(g, getBackground(), getWidth(),
                getHeight());
        super.paintComponent(g);
    }
}