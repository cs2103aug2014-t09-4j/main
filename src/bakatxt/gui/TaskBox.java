//@author A0116538A

package bakatxt.gui;

// TODO comments

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JPanel;

import bakatxt.core.Task;
import bakatxt.gui.look.ThemeReader;
import bakatxt.gui.look.UIHelper;

/**
 * This class places the elements of a single task correctly in their box.
 *
 */
abstract class TaskBox extends JPanel implements BakaAnimator {

    // TODO set min/max size
    protected static final boolean IS_LINE_WRAP = true;
    private static final String AT = " @";

    private static final String DONE = "✓";
    private static final String UNDONE = "✗";
    private static final String OVERDUE = UNDONE + "!";
    private static final String NO_TIME = "        ";

    private final Color _baseColor;

    public TaskBox(Task task, int index, Color backgroundColor) {

        setOpaque(false);
        setBackground(backgroundColor);
        setLayout(new GridBagLayout());
        addComponentsToPane(task, index);

        _baseColor = backgroundColor;
    }

    protected void addComponentsToPane(Task task, int index) {
        GridBagConstraints layout = new GridBagConstraints();

        setNumber(layout, index);
        setTaskAndLocation(layout, task.getTitle(), task.getVenue());
        setTimeStart(layout, task.getTime());
        setDescription(layout, task.getDescription());
        setTaskCompletionState(layout, doneState(task));
        setTimeEnd(layout, task.getEndTime());
    }

    private void setNumber(GridBagConstraints layout, int number) {

        FormattedText index = new FormattedText(shouldAddLeadingZero(number),
                                                ThemeReader.getNumberTheme());
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.LAST_LINE_START;
        layout.weightx = 1.0;
        layout.weighty = 0.0;
        layout.gridx = 0;
        layout.gridy = 0;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets = new Insets(0, 3 * UIHelper.BORDER, 0, 0);
        this.add(index, layout);
    }

    // TODO make locationText a different color
    private void setTaskAndLocation(GridBagConstraints layout, String taskText,
                                    String locationText) {
        locationText = removeNullValuesLocation(AT, locationText);
        FormattedText task = new FormattedText(taskText + locationText,
                                               ThemeReader.getTitleTheme(),
                                               IS_LINE_WRAP);
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.anchor = GridBagConstraints.LAST_LINE_START;
        layout.weightx = 0.01;
        layout.weighty = 0.0;
        layout.gridx = 1;
        layout.gridy = 0;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets = new Insets(0, 5 * UIHelper.BORDER, 0, 0);
        this.add(task, layout);
    }

    private void setDescription(GridBagConstraints layout, String descriptionText) {
        FormattedText description = new FormattedText(descriptionText,
                                                      ThemeReader.getDefaultTheme(),
                                                      IS_LINE_WRAP);
        layout.fill = GridBagConstraints.BOTH;
        layout.anchor = GridBagConstraints.LINE_START;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridx = 1;
        layout.gridy = 1;
        layout.gridwidth = 1;
        layout.gridheight = 2;
        layout.insets = new Insets(0, 4 * UIHelper.BORDER, 0, 0);
        this.add(description, layout);
    }

    private void setTimeStart(GridBagConstraints layout, String startTimeText) {

        startTimeText = removeNullValuesTime(startTimeText);
        FormattedText time = new FormattedText(startTimeText,
                                               ThemeReader.getDateTheme());
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.FIRST_LINE_END;
        layout.weightx = 0.01;
        layout.weighty = 1.0;
        layout.gridx = 2;
        layout.gridy = 0;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets = new Insets(3 * UIHelper.BORDER, 0, 0,
                2 * UIHelper.BORDER);
        this.add(time, layout);
    }

    private void setTaskCompletionState(GridBagConstraints layout, String state) {
        FormattedText completionState = new FormattedText(state,
                                                          ThemeReader.getLocationTheme());
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.CENTER;
        layout.weightx = 0.01;
        layout.weighty = 1.0;
        layout.gridx = 2;
        layout.gridy = 1;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets = new Insets(0, 0, 0, 2 * UIHelper.BORDER);
        this.add(completionState, layout);
    }

    private void setTimeEnd(GridBagConstraints layout, String endTimeText) {

        endTimeText = removeNullValuesTime(endTimeText);
        FormattedText time = new FormattedText(endTimeText,
                                               ThemeReader.getDateTheme());
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.LAST_LINE_END;
        layout.weightx = 0.01;
        layout.weighty = 1.0;
        layout.gridx = 2;
        layout.gridy = 2;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets = new Insets(0, 0, 3 * UIHelper.BORDER,
                2 * UIHelper.BORDER);
        this.add(time, layout);
    }

    private static String removeNullValuesTime(String s) {
        if (isStringNull(s)) {
            return NO_TIME;
        }
        return s;
    }

    private static String removeNullValuesLocation(String prefix, String s) {
        if (isStringNull(s)) {
            return "";
        }
        return prefix + s;
    }

    private static String shouldAddLeadingZero(int number) {
        String num = Integer.toString(number);
        if (number < 10) {
            num = "0" + num;
        }
        return num;
    }

    private static boolean isStringNull(String s) {
        return (s == null || s.equals("null"));
    }

    private static String doneState(Task task) {
        if (task.isDone()) {
            return DONE;
        } else if (task.isOverdue()) {
            return OVERDUE;
        }
        return UNDONE;
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(UIHelper.TRANSPARENT);
    }


    @Override
    public Color getColor() {
        return _baseColor;
    }

    @Override
    public void setColor(Color newColor) {
        setBackground(newColor);
    }

    /**
     * We are not animating the location for TaskBox
     */
    @Override
    public Point getPoint() {
        return null;
    }

    @Override
    public void setPoint(Point newLocation) {
    }
}
