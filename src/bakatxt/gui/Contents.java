package bakatxt.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

class Contents extends JPanel {

    private Input input_;

    public Contents(Input input) {
        setOpaque(false);
        setBackground(UIHelper.TRANSPARENT);
        setLayout(new GridBagLayout());
        addComponentsToPane();
    }

    private void addComponentsToPane() {
        GridBagConstraints layout = new GridBagConstraints();

        setLeftTime(layout);
        setRightTime(layout);
        setEvents(layout);
    }

    private void setLeftTime(GridBagConstraints layout) {
        FormattedText time = new FormattedText("<Day>", Font.PLAIN, 12, UIHelper.PRESET_COLOR_DATE);
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.weightx = 0.0;
        layout.weighty = 0.01;
        layout.gridx = 0;
        layout.gridy = 0;
        layout.gridheight = 1;
        layout.gridwidth = 1;
        this.add(time, layout);
    }

    private void setRightTime(GridBagConstraints layout) {
        FormattedText time = new FormattedText("<Time>", Font.PLAIN, 12, UIHelper.PRESET_COLOR_DATE);
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.FIRST_LINE_END;
        layout.weightx = 0.0;
        layout.weighty = 0.01;
        layout.gridx = 1;
        layout.gridy = 0;
        layout.gridheight = 1;
        layout.gridwidth = 1;
        this.add(time, layout);
    }

    private void setEvents(GridBagConstraints layout) {
        Tasks events = new Tasks(input_);
        layout.fill = GridBagConstraints.BOTH;
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridx = 0;
        layout.gridy = 1;
        layout.gridheight = 1;
        layout.gridwidth = GridBagConstraints.REMAINDER;
        layout.insets = new Insets(UIHelper.WINDOW_BORDER, 0, 0, 0);
        this.add(events, layout);
    }
}
