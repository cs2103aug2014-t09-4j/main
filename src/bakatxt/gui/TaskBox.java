package bakatxt.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

class TaskBox extends JPanel {

    public static boolean isEven_;

    protected static final boolean IS_LINE_WRAP = true;

    protected static final String LOREM_IPSUM =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ";
            /*+ "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim "
            + "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex "
            + "ea commodo consequat. Duis aute irure dolor in reprehenderit in "
            + "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur "
            + "sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt "
            + "mollit anim id est laborum.";*/

    public TaskBox(boolean isEven) {
        System.out.println(isEven);
        setOpaque(false);
        isEven_ = isEven;
        setColor();
        setLayout(new GridBagLayout());
        addComponentsToPane();
    }

    protected void setColor() {
        if (isEven_) {
            setBackground(UIHelper.GRAY_BLACK);
        } else {
            setBackground(UIHelper.TRANSPARENT);
        }
    }

    protected void addComponentsToPane() {
        GridBagConstraints layout = new GridBagConstraints();

        setTask(layout);
        setLocation(layout);
        setTimeStart(layout);
        setDescription(layout);
        setTimeEnd(layout);
    }

    private void setTask(GridBagConstraints layout) {
        FormattedText task = new FormattedText("<Task Name>", UIHelper.PRESET_TYPE_TITLE,
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

    private void setLocation(GridBagConstraints layout) {
        FormattedText location = new FormattedText("@" + "<Task Location>", UIHelper.PRESET_TYPE_LOCATION,
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

    private void setDescription(GridBagConstraints layout) {
        FormattedText description = new FormattedText(LOREM_IPSUM, UIHelper.PRESET_TYPE_DEFAULT,
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

    private void setTimeStart(GridBagConstraints layout) {
        FormattedText time = new FormattedText("<start time>", UIHelper.PRESET_TYPE_DATE,
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
    protected void paintComponent(Graphics g) {
        buildBackground(g);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(UIHelper.TRANSPARENT);
    }

    protected void buildBackground(Graphics g) {
        if(isEven_) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(getBackground());
            g2d.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
            g2d.dispose();
        }
    }
}
