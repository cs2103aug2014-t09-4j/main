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
    private static boolean isEven_ = false;
    private static boolean isLast_ = false;
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
        while(_tasks.peek() != null) {
            setIsLast(_tasks.size(), i);
            setActive(_tasks.pop(), i);
            flipIsEven();
            i++;
        }
    }

    private static void flipIsEven() {
        if (isEven_) {
            isEven_ = false;
        } else {
            isEven_ = true;
        }
    }

    private static void setIsLast(int size, int i) {
        if (i + 1 == size) {
            isLast_ = true;
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

        if (isLast_) {
            this.add(new FinalTaskBox(task, isEven_), _layout);
        } else {
            this.add(new TaskBox(task, isEven_), _layout);
        }
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
