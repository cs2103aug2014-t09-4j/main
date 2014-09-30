package bakatxt.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

class UIHelper {

    protected static final double WINDOW_RATIO = 1.25; // Y:X
    protected static final int WINDOW_SCALE = 650;
    protected static final int WINDOW_X = WINDOW_SCALE;
    protected static final int WINDOW_Y = (int)(WINDOW_RATIO * WINDOW_SCALE);
    protected static final int WINDOW_ROUNDNESS = 20;
    protected static final int WINDOW_BORDER = 4; // leave as half of actual border

    /* to be implemented?
    protected static final List<String> SUGGESTIONS_MAIN = new ArrayList<>(asList(
            "add", "display", "delete", "exit"));
    protected static final List<String> SUGGESTIONS_ADD = new ArrayList<>(asList(
            "today", "tomorrow"));
    protected static final List<String> SUGGESTIONS_DISPLAY = new ArrayList<>(asList(
            "day", "week", "month"));
    protected static final List<List<String>> SUGGESTIONS = asList(
            SUGGESTIONS_MAIN, SUGGESTIONS_ADD, SUGGESTIONS_DISPLAY);
    */

    protected static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    protected static final Color GRAY_MEDIUM = new Color(66, 66, 66);
    protected static final Color GRAY_LIGHT = new Color(100, 100, 100);
    protected static final Color GRAY_DARK = new Color(48, 48, 48);
    protected static final Color GRAY_BLACK = new Color(36, 36, 36);

    protected static final Color PRESET_COLOR_DATE = new Color(253, 184, 19);
    protected static final Color PRESET_COLOR_TIME = new Color(253, 184, 19);
    protected static final Color PRESET_COLOR_LOCATION = new Color(227, 122, 37);
    protected static final Color PRESET_COLOR_TITLE = new Color(239, 62, 47);
    protected static final Color PRESET_COLOR_DEFAULT = new Color(228, 224, 227);

    protected static final int PRESET_TYPE_DATE = Font.PLAIN;
    protected static final int PRESET_TYPE_TIME = Font.PLAIN;
    protected static final int PRESET_TYPE_LOCATION = Font.BOLD;
    protected static final int PRESET_TYPE_TITLE = Font.BOLD;
    protected static final int PRESET_TYPE_DEFAULT = Font.PLAIN;

    protected static final int PRESET_SIZE_DATE = 12;
    protected static final int PRESET_SIZE_TIME = 14;
    protected static final int PRESET_SIZE_LOCATION = 14;
    protected static final int PRESET_SIZE_TITLE = 18;
    protected static final int PRESET_SIZE_DEFAULT = 12;


    protected static void paintRoundedRectangle(Graphics g, Color background, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHints(UIHelper.antiAlias());
        g2d.setColor(background);
        g2d.fill(new RoundRectangle2D.Double(0, 0, width, height,
                WINDOW_ROUNDNESS, WINDOW_ROUNDNESS));
        g2d.dispose();
    }

    protected static RenderingHints antiAlias() {
        RenderingHints qualityHints =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                                   RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        return qualityHints;
    }
}
