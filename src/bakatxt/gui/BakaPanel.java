package bakatxt.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

class BakaPanel extends JPanel {

    private static Input _input;
    private static final int BORDER = UIHelper.WINDOW_BORDER;

    public BakaPanel() {

        _input = new Input();

        setOpaque(false);
        setPreferredSize(new Dimension(UIHelper.WINDOW_X, UIHelper.WINDOW_Y));
        setBackground(UIHelper.GRAY_MEDIUM);
        setLayout(new GridBagLayout());
        addComponentsToPane();
    }

    public Input getInput() {
        return _input;
    }

    private void addComponentsToPane() {
        GridBagConstraints layout = new GridBagConstraints();

        setInputBox(layout);
        setContent(layout);
    }

    private void setInputBox(GridBagConstraints layout) {
        layout.fill = GridBagConstraints.BOTH;
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.weightx = 1.0;
        layout.weighty = 0.03;
        layout.gridx = 0;
        layout.gridy = 0;
        layout.insets = new Insets(2 * BORDER, 2 * BORDER, BORDER, 2 * BORDER);
        this.add(_input, layout);
    }

    private void setContent(GridBagConstraints layout) {
        Contents contents = new Contents(_input);
        layout.fill = GridBagConstraints.BOTH;
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridx = 0;
        layout.gridy = 1;
        layout.insets = new Insets(0, 2 * BORDER, 2 * BORDER, 2 * BORDER);
        this.add(contents, layout);
    }

    @Override
    protected void paintComponent(Graphics g) {
        UIHelper.paintRoundedRectangle(g, getBackground(), getWidth(), getHeight());
        super.paintComponent(g);
    }
}