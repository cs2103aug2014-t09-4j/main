//@author A0116538A

package bakatxt.gui;

import java.awt.Color;
import java.awt.Point;

/**
 * Any class that wishes to use UIAnimator must implement this interface
 *
 */
public interface BakaAnimator {

    /**
     * A final variable needs to be set for the base color of the component so that
     * the UIAnimator can return the component to it's correct color. This method
     * retrieves said final color.
     *
     * @return the base color of the component
     */
    public Color getColor();

    /**
     * This method should pass the color to setBackground() for Swing components
     *
     * @param newColor is the temporary color to set the component to
     */
    public void setColor(Color newColor);

    /**
     * A final variable needs to be set for the base location of the component so that
     * the UIAnimator can return the component to it's correct location. This method
     * retrieves said final location.
     *
     * @return the original location of the component
     */
    public Point getPoint();

    /**
     * This method should pass the location to setLocation() for Swing components
     *
     * @param newLocation is the temporary location to set the component to
     */
    public void setPoint(Point newLocation);
}
