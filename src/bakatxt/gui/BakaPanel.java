//@author A0116538A

package bakatxt.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import bakatxt.core.BakaTxtMain;

// TODO comments
class BakaPanel extends JPanel {

    private static Input _input;
    private static Contents _contents;
    private static final int BORDER = UIHelper.WINDOW_BORDER;

    public BakaPanel(BakaTxtMain session) {

        _input = new Input();
        _contents = new Contents(session.getAllTasks());

        setOpaque(false);
        setPreferredSize(new Dimension(UIHelper.WINDOW_X, UIHelper.WINDOW_Y));
        setBackground(UIHelper.GRAY_MEDIUM);
        setLayout(new GridBagLayout());
        addComponentsToPane();
    }

    public Input getInput() {
        return _input;
    }

    public void setContents(BakaTxtMain session) {

         _contents.removeAll();
         _contents.updateContents(session.getAllTasks());
         _contents.validate();
         _contents.repaint();

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
        layout.fill = GridBagConstraints.BOTH;
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridx = 0;
        layout.gridy = 1;
        layout.insets = new Insets(0, 2 * BORDER, 2 * BORDER, 2 * BORDER);
        this.add(_contents, layout);
    }

    @Override
    protected void paintComponent(Graphics g) {
        UIHelper.paintRoundedRectangle(g, getBackground(), getWidth(), getHeight());
        super.paintComponent(g);
    }
}