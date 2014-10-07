//@author A0116538A

package bakatxt.gui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.LinkedList;

import javax.swing.JPanel;

import bakatxt.core.Task;

//TODO comments

class TaskItems extends JPanel {

    // TODO clean up cruft & make the code clearer
    // TODO fix color bug
    private static LinkedList<Task> _tasks;
    private static GridBagConstraints _layout = new GridBagConstraints();
    //private Input input_;

    public TaskItems(LinkedList<Task> tasks) {
        _tasks = tasks;
        setOpaque(false);
        setBackground(UIHelper.GRAY_DARK);
        setLayout(new GridBagLayout());
        addComponentsToPane();
    }

    private void addComponentsToPane() {
        try {
            addCurrentEvents();
        } catch (NullPointerException e) {
            setNoEvents();
        }
    }

    private void addCurrentEvents() {
        int i = 0;
        boolean isLast = false;
        boolean isEven = false;

        while(_tasks.peek() != null) {
            if (i + 1 == _tasks.size()) {
                isLast = true;
            }
            setActive(_tasks.pop(), i);
            isEven = !isEven;
            i++;
        }
    }

    private void setNoEvents() {

        FormattedText task = new FormattedText("You have no events!", UIHelper.PRESET_TYPE_TITLE,
                UIHelper.PRESET_SIZE_TITLE, UIHelper.PRESET_COLOR_TITLE);
        /*FormattedText task = new FormattedText("", UIHelper.PRESET_TYPE_TITLE,
                UIHelper.PRESET_SIZE_TITLE, UIHelper.PRESET_COLOR_TITLE);*/
       // input_.getDocument().addDocumentListener(new Controller(input_, task));
        _layout.fill = GridBagConstraints.NONE;
        _layout.anchor = GridBagConstraints.CENTER;
        _layout.weightx = 1.0;
        _layout.weighty = 1.0;
        _layout.gridy = 0;
        this.add(task, _layout);
    }

    private void setActive(Task task, int y) {
        _layout.fill = GridBagConstraints.BOTH;
        _layout.anchor = GridBagConstraints.FIRST_LINE_START;
        _layout.weightx = 1.0;
        _layout.weighty = 1.0;
        _layout.gridy = y;
    }

    /**
     * This method decides what kind of color and shape the box should be
     *
     * @param task is the task to put in the box
     */
    private void setBoxType(Task task) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        UIHelper.paintRoundedRectangle(g, getBackground(), getWidth(), getHeight());
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(UIHelper.TRANSPARENT);
    }

}
