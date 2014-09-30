package bakatxt.gui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

class Tasks extends JPanel {

    private static boolean isEven_ = false;
    private static boolean isLast_ = false;
    //private Input input_;

    public Tasks(Input input) {
        setOpaque(false);
        setBackground(UIHelper.GRAY_DARK);
        setLayout(new GridBagLayout());
        addComponentsToPane();
    }

    private void addComponentsToPane() {
        GridBagConstraints layout = new GridBagConstraints();

        int size = 5;

        if (size == 0) {
            setNoEvents(layout, size);
        } else {
            addCurrentEvents(layout, size);
        }
    }

    private void addCurrentEvents(GridBagConstraints layout, int size) {
        for (int i = 0; i < size; i++) {
            setIsLast(size, i);
            setActive(layout, i);
            flipIsEven();
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

    private void setNoEvents(GridBagConstraints layout, int y) {
        FormattedText task = new FormattedText("You have no tasks, hooray!", UIHelper.PRESET_TYPE_TITLE,
                UIHelper.PRESET_SIZE_TITLE, UIHelper.PRESET_COLOR_TITLE);
        /*FormattedText task = new FormattedText("", UIHelper.PRESET_TYPE_TITLE,
                UIHelper.PRESET_SIZE_TITLE, UIHelper.PRESET_COLOR_TITLE);*/
       // input_.getDocument().addDocumentListener(new Controller(input_, task));
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.CENTER;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridy = y;
        this.add(task, layout);
    }

    private void setActive(GridBagConstraints layout, int y) {
        layout.fill = GridBagConstraints.BOTH;
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridy = y;

        if (isLast_) {
            this.add(new FinalTaskBox(isEven_), layout);
        } else {
            this.add(new TaskBox(isEven_), layout);
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
