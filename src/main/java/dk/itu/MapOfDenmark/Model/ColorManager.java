package dk.itu.MapOfDenmark.Model;

import dk.itu.MapOfDenmark.Model.Graph.Vertex;
import dk.itu.MapOfDenmark.Model.objects.*;
import dk.itu.MapOfDenmark.Model.objects.abstracts.Element;
import dk.itu.MapOfDenmark.Model.objects.abstracts.Road;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages colors for different map elements and provides functionality to switch between color modes.
 */
public class ColorManager {
    /* Predefined colors */
    private static final Color red = Color.rgb(178, 34, 34);
    private static final Color green = Color.rgb(34, 139, 34);
    private static final Color blue = Color.rgb(0, 157, 196);
    private static final Color grey = Color.rgb(169, 169, 169);
    private static final Color white = Color.rgb(211, 211, 211);
    private static final Color black = Color.rgb(0, 0, 0);
    private static final Color yellow = Color.rgb(255, 255, 0);

    /* Predefined colorblind colors */
    private static final Color cb_red = Color.rgb(255, 0, 0);
    private static final Color cb_green = Color.rgb(0, 179, 130);
    private static final Color cb_blue = Color.rgb(0, 15, 255);
    private static final Color cb_grey = Color.rgb(173, 171, 175);
    private static final Color cb_white = Color.rgb(199, 199, 199);
    private static final Color cb_black = Color.BLACK;
    private static final Color cb_yellow = Color.rgb(255, 255, 0);

    /* Road Colors */
    private static final Color road1 = black;
    private static final Color road2 = Color.rgb(128, 52, 2);
    private static final Color road3 = Color.rgb(212, 85, 0);
    private static final Color road4 = grey;

    /* If you see Pink an error has occurred while coloring */
    private static final Color debugColor = Color.rgb(255, 255, 255);

    /**
     * A simple enum class to determine the current color-mode.
     */
    public enum ColorMode {NORMAL, COLORBLIND;}

    /**
     * Always starts with normal as its default color-palette.
     */
    private static ColorMode currentColor = ColorMode.NORMAL;
    private static final Map<Class<? extends Element>, Color> normalColorMap, colorBlindMap;

    static {
        normalColorMap = new HashMap<>();
        colorBlindMap = new HashMap<>();

        // Normal view
        normalColorMap.put(Building.class, grey);
        normalColorMap.put(Coast.class, Color.PINK);
        normalColorMap.put(Grass.class, green);
        normalColorMap.put(Island.class, white);
        normalColorMap.put(Path.class, red);
        normalColorMap.put(Road.class, black); // default RoadColor
        normalColorMap.put(WalkWay.class, grey);
        normalColorMap.put(Water.class, blue);
        normalColorMap.put(WaterWay.class, blue);
        normalColorMap.put(Vertex.class, yellow);
        normalColorMap.put(PointOfInterest.class, blue);

        // colorBlind view
        colorBlindMap.put(Building.class, cb_grey);
        colorBlindMap.put(Coast.class, cb_black);
        colorBlindMap.put(Grass.class, cb_green);
        colorBlindMap.put(Island.class, cb_white);
        colorBlindMap.put(Path.class, cb_red);
        colorBlindMap.put(Road.class, cb_black); // default RoadColor
        colorBlindMap.put(WalkWay.class, cb_grey);
        colorBlindMap.put(Water.class, cb_blue);
        colorBlindMap.put(WaterWay.class, cb_blue);
        colorBlindMap.put(Vertex.class, cb_yellow);
        colorBlindMap.put(PointOfInterest.class, blue);
    }

    /**
     * Gets the color for the specified map element based on the current color mode.
     *
     * @param clazz The class of the map element.
     * @return The color for the specified map element.
     */
    public static Color getColor(Class<? extends Element> clazz) {
        return switch (currentColor) {
            case NORMAL -> getNormalColor(clazz);
            case COLORBLIND -> getColorBlindMode(clazz);
        };
    }

    private static Color getNormalColor(Class<? extends Element> clazz) {
        Color color = normalColorMap.get(clazz);
        return color != null ? color : debugColor;
    }

    private static Color getColorBlindMode(Class<? extends Element> clazz) {
        return colorBlindMap.getOrDefault(clazz, debugColor);
    }

    /**
     * Gets the color for the specified type of road.
     *
     * @param roadType The type of road.
     * @return The color for the specified type of road.
     */
    public static Color getRoadColors(Road.RoadType roadType) {
        return switch (roadType) {
            case PRIMARY -> road1;
            case SECONDARY -> road2;
            case TERTIARY -> road3;
            case WALKWAY -> road4;
        };
    }

    /**
     * Toggles the current color mode between normal and colorblind.
     */
    public static void toggle() {
        switch (currentColor) {
            case NORMAL -> currentColor = ColorMode.COLORBLIND;
            case COLORBLIND -> currentColor = ColorMode.NORMAL;
        }
    }

    /**
     * Sets the current color mode for the ColorManager.
     *
     * @param mode The color mode to set.
     *             Use {@link ColorMode#NORMAL} for standard colors,
     *             {@link ColorMode#COLORBLIND} for colorblind-friendly colors.
     */
    private static void setColorMode(ColorMode mode) {
        currentColor = mode;
    }

    /**
     * Gets the current color mode as a string.
     *
     * @return The current color mode.
     */
    public static String getColorMode() {
        String color = currentColor.toString().toLowerCase();
        // Capitalize the first letter
        return Character.toUpperCase(color.charAt(0)) + color.substring(1) + "-mode";
    }
}
