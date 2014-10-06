package bakatxt.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

class Contents extends JPanel {

    private Tasks _tasks;
    private FormattedText _alertMessage;
    private FormattedText _dayAndDate;

    private static String WELCOME_MESSAGE = "Welcome to BakaTXT! For help please type help in the box above";

    public Contents() {
        _tasks = new Tasks();
        _alertMessage = setAlertMessageText(WELCOME_MESSAGE);
        _dayAndDate = setDayAndDateText("<Day><Date>");

        setOpaque(false);
        setBackground(UIHelper.TRANSPARENT);
        setLayout(new GridBagLayout());
        addComponentsToPane();
    }

    protected void updateContents() {
        _tasks = new Tasks();
        _alertMessage = setAlertMessageText(WELCOME_MESSAGE);
        _dayAndDate = setDayAndDateText("<DayDate>");

        addComponentsToPane();
    }

    private void addComponentsToPane() {
        GridBagConstraints layout = new GridBagConstraints();

        setAlertMessage(layout);
        setDateAndDay(layout);
        setEvents(layout);
    }

    private void setAlertMessage(GridBagConstraints layout) {
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.PAGE_START;
        layout.weightx = 0.0;
        layout.weighty = 0.01;
        layout.gridx = 0;
        layout.gridy = 0;
        layout.gridheight = 1;
        layout.gridwidth = 1;
        this.add(_alertMessage, layout);
    }

    private void setDateAndDay(GridBagConstraints layout) {
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.LAST_LINE_END;
        layout.weightx = 0.0;
        layout.weighty = 0.01;
        layout.gridx = 0;
        layout.gridy = 1;
        layout.gridheight = 1;
        layout.gridwidth = 1;
        this.add(_dayAndDate, layout);
    }

    private void setEvents(GridBagConstraints layout) {
        layout.fill = GridBagConstraints.BOTH;
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridx = 0;
        layout.gridy = 2;
        layout.gridheight = 1;
        layout.gridwidth = GridBagConstraints.REMAINDER;
        layout.insets = new Insets(UIHelper.WINDOW_BORDER, 0, 0, 0);
        this.add(_tasks, layout);
    }

    private static FormattedText setAlertMessageText (String message) {
        return new FormattedText(message, UIHelper.PRESET_TYPE_DEFAULT,
                UIHelper.PRESET_SIZE_DEFAULT, UIHelper.PRESET_COLOR_ALERT);
    }

    private static FormattedText setDayAndDateText (String dayAndDate) {
        return new FormattedText(dayAndDate, UIHelper.PRESET_TYPE_DATE,
                UIHelper.PRESET_SIZE_DATE, UIHelper.PRESET_COLOR_DATE);
    }
}
