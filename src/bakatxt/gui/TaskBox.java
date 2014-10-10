//@author A0116538A

package bakatxt.gui;

//TODO comments

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import bakatxt.core.Task;

/**
 * This class places the elements of a single task correctly in their box.
 *
 */
abstract class TaskBox extends JPanel {

    //TODO set min/max size
    protected static final boolean IS_LINE_WRAP = true;
    private static final String AT = " @";

    public TaskBox(Task task, Color backgroundColor) {

        setOpaque(false);
        setBackground(backgroundColor);
        setLayout(new GridBagLayout());
        addComponentsToPane(task);
    }

    protected void addComponentsToPane(Task task) {
        GridBagConstraints layout = new GridBagConstraints();

        setTask(layout, task.getTitle());
        setLocation(layout, task.getVenue());
        setTimeStart(layout, task.getTime());
        setDescription(layout, task.getDescription());
        setTimeEnd(layout);
    }

    private void setTask(GridBagConstraints layout, String titleText) {
        FormattedText task = new FormattedText(titleText, UIHelper.PRESET_TYPE_TITLE,
                UIHelper.PRESET_SIZE_TITLE, UIHelper.PRESET_COLOR_TITLE);
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.LINE_START;
        layout.weightx = 0.0;
        layout.weighty = 0.0;
        layout.gridx = 0;
        layout.gridy = 0;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets = new Insets(0, 3 * UIHelper.WINDOW_BORDER, 0, 0);
        this.add(task, layout);
    }

    private void setLocation(GridBagConstraints layout, String locationText) {
        FormattedText location = new FormattedText(AT + locationText, UIHelper.PRESET_TYPE_LOCATION,
                UIHelper.PRESET_SIZE_LOCATION , UIHelper.PRESET_COLOR_LOCATION);
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.LINE_START;
        layout.weightx = 0.0;
        layout.weighty = 0.0;
        layout.gridx = 1;
        layout.gridy = 0;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets = new Insets(0, 0, 0, 0);
        this.add(location, layout);
    }

    private void setDescription(GridBagConstraints layout, String descriptionText) {
        FormattedText description = new FormattedText(descriptionText, UIHelper.PRESET_TYPE_DEFAULT,
                UIHelper.PRESET_SIZE_DEFAULT, UIHelper.PRESET_COLOR_DEFAULT, IS_LINE_WRAP);
        layout.fill = GridBagConstraints.BOTH;
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridx = 0;
        layout.gridy = 1;
        layout.gridwidth = 2;
        layout.gridheight = 1;
        layout.insets = new Insets(0, 4 * UIHelper.WINDOW_BORDER, 0, 0);
        this.add(description, layout);
    }

    private void setTimeStart(GridBagConstraints layout, String startTimeText) {
        FormattedText time = new FormattedText(startTimeText, UIHelper.PRESET_TYPE_DATE,
                UIHelper.PRESET_SIZE_DATE, UIHelper.PRESET_COLOR_DATE);
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.FIRST_LINE_END;
        layout.weightx = 0.5;
        layout.weighty = 1.0;
        layout.gridx = 2;
        layout.gridy = 0;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets = new Insets(3 * UIHelper.WINDOW_BORDER, 0, 0, 2 * UIHelper.WINDOW_BORDER);
        this.add(time, layout);
    }

    private void setTimeEnd(GridBagConstraints layout) {
        // TODO add end time once it has been implemented
        FormattedText time = new FormattedText("<end time>", UIHelper.PRESET_TYPE_DATE,
                UIHelper.PRESET_SIZE_DATE, UIHelper.PRESET_COLOR_DATE);
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.LAST_LINE_END;
        layout.weightx = 0.5;
        layout.weighty = 1.0;
        layout.gridx = 2;
        layout.gridy = 1;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets = new Insets(0, 0, 3 * UIHelper.WINDOW_BORDER, 2 * UIHelper.WINDOW_BORDER);
        this.add(time, layout);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(UIHelper.TRANSPARENT);
    }
}
