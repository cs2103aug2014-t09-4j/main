//@author A0116538A

package bakatxt.gui.theme;

import java.awt.Color;

/**
 * This class controls the look of the BakaTxt GUI
 *
 */
public class BakaTheme {

    private Integer _fontSize;
    private Integer _fontType;
    private Color _color;

    /**
     * @param color
     *        the color of the component
     * @param fontSize
     *        font size of the type
     * @param fontType
     *        whether the font is regular, italic or bold
     */
    public BakaTheme(Color color, Integer fontType, Integer fontSize) {
        _color = color;
        _fontSize = fontSize;
        _fontType = fontType;
    }

    /**
     * @return the color of the theme
     */
    public Color getColor() {
        return _color;
    }

    /**
     * @return the font size of the theme
     */
    public Integer getFontSize() {
        return _fontSize;
    }

    /**
     * @return regular, italic or bold font
     */
    public Integer getFontType() {
        return _fontType;
    }

    public void setColor(Color color) {
        _color = color;
    }

    public void setFontSize(Integer fontSize) {
        _fontSize = fontSize;
    }

    public void setFontType(Integer fontType) {
        _fontType = fontType;
    }
}
