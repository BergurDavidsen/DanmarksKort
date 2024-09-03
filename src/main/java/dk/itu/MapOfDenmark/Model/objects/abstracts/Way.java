package dk.itu.MapOfDenmark.Model.objects.abstracts;

import java.io.Serializable;
import dk.itu.MapOfDenmark.Model.ColorManager;
import dk.itu.MapOfDenmark.Model.objects.Drawn;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * An abstract class representing a way, inherited from the Element class and implementing the Drawn interface.
 */
public abstract class Way extends Element implements Serializable, Drawn {
    /** Indicates whether to draw the path. */
    public static boolean drawPath = false;

    /** The coordinates defining the way. */
    protected float[] coords;

    /**
     * Constructs a way with the given coordinates.
     *
     * @param coords The coordinates defining the way.
     */
    public Way(float[] coords) {
        this.coords = coords;
    }

    /**
     * Draws the way on the specified graphics context.
     *
     * @param gc The graphics context on which to draw the way.
     */
    @Override
    public void draw(GraphicsContext gc){
        gc.beginPath();
        gc.setFill(getColor());
        drawPath(gc);
        gc.closePath(); // Fill the closed shape
        gc.fill();
    }

    /**
     * Draws the lines representing the way on the specified graphics context with the given line width.
     *
     * @param gc        The graphics context on which to draw the way.
     * @param lineWidth The width of the lines to draw.
     */
    public void drawLines(GraphicsContext gc, double lineWidth) {
        gc.setStroke(this.getColor());
        gc.setLineWidth(lineWidth);
        gc.beginPath();
        drawPath(gc);
        gc.stroke();
        gc.closePath();
    }

    /**
     * Draws the path of the way on the specified graphics context.
     *
     * @param gc The graphics context on which to draw the path.
     */
    private void drawPath(GraphicsContext gc){
        float x = coords[1] * 0.56f;
        float y = -coords[0];
        gc.moveTo(x,y);

        for (int i = 2; i < coords.length; i += 2) {
            x = coords[i+1] * 0.56f;
            y = -coords[i];
            if(x == 0 || y == 0)
                continue;
            gc.lineTo(x,y);
        }
    }

    /**
     * Draws an oval on the specified graphics context with the given parameters.
     *
     * @param gc       The graphics context on which to draw the oval.
     * @param x        The x-coordinate of the center of the oval.
     * @param y        The y-coordinate of the center of the oval.
     * @param diameter The diameter of the oval.
     */
    public void drawOval(GraphicsContext gc, float x, float y, float diameter){
        x *= 0.56f;
        y *= -1;
        gc.fillOval(x - diameter/2, y - diameter/2, diameter, diameter);
    }

    /**
     * Retrieves the color of the way.
     *
     * @return The color of the way.
     */
    @Override
    public Color getColor(){
        return ColorManager.getColor(this.getClassType());
    }

    /**
     * Retrieves the coordinates defining the way.
     *
     * @return The coordinates defining the way.
     */
    public float[] getCoords(){
        return this.coords;
    }

    /**
     * Determines if the way should be zoomed in.
     *
     * @return True if the way should be zoomed in, false otherwise.
     */
    @Override
    public boolean getZoom(){
        return true;
    }
}
