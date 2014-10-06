package bakatxt.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

class Contents extends JPanel {

    public Contents() {
        setOpaque(false);
        setBackground(UIHelper.TRANSPARENT);
        setLayout(new GridBagLayout());
        addComponentsToPane();
    }

    private void addComponentsToPane() {
        GridBagConstraints layout = new GridBagConstraints();

        setAlertMessage(layout);
        setDateAndDay(layout);
        setEvents(layout);
    }

    private void setAlertMessage(GridBagConstraints layout) {
        // TODO set message
        FormattedText time = new FormattedText("<Message>", Font.PLAIN, 12, UIHelper.PRESET_COLOR_DATE);
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.PAGE_START;
        layout.weightx = 0.0;
        layout.weighty = 0.01;
        layout.gridx = 0;
        layout.gridy = 0;
        layout.gridheight = 1;
        layout.gridwidth = 1;
        this.add(time, layout);
    }

    private void setDateAndDay(GridBagConstraints layout) {
        // TODO set time and day
        FormattedText time = new FormattedText("<Day><Date>", Font.PLAIN, 12, UIHelper.PRESET_COLOR_DATE);
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.LAST_LINE_END;
        layout.weightx = 0.0;
        layout.weighty = 0.01;
        layout.gridx = 0;
        layout.gridy = 1;
        layout.gridheight = 1;
        layout.gridwidth = 1;
        this.add(time, layout);
    }

    private void setEvents(GridBagConstraints layout) {
        Tasks events = new Tasks();
        layout.fill = GridBagConstraints.BOTH;
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridx = 0;
        layout.gridy = 2;
        layout.gridheight = 1;
        layout.gridwidth = GridBagConstraints.REMAINDER;
        layout.insets = new Insets(UIHelper.WINDOW_BORDER, 0, 0, 0);
        this.add(events, layout);
    }
}
