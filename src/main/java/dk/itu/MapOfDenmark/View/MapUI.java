package dk.itu.MapOfDenmark.View;

import dk.itu.MapOfDenmark.Controller;
import dk.itu.MapOfDenmark.Model.Model;
import dk.itu.MapOfDenmark.Model.SerializableRectangle;
import dk.itu.MapOfDenmark.Model.Trees.Trie.CompressedTrie;
import dk.itu.MapOfDenmark.Model.objects.abstracts.Way;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

//import random
import java.util.ArrayList;
import java.util.List;

/**
 * The MapUI class represents the user interface for displaying the map of Denmark.
 * It extends the GUI class and provides methods for panning, zooming, and redrawing the map.
 */
public class MapUI extends GUI {
    /** The zoom factor of the map. */
    public static double zoomFactor;

    /** The label displaying the current zoom level. */
    private Label zoomLevel;

    /** The model containing map data. */
    private Model model;

    /** The burger menu for additional options. */
    public BurgerMenu burgerMenu = new BurgerMenu();

    /** The user input interface for interacting with the map. */
    public MapGUI userInput;

    /** The area of the canvas before any zooming or panning. */
    public double oldArrea;

    /**
     * Constructs a new MapUI instance.
     *
     * @param model       The model containing map data.
     * @param primaryStage The primary stage of the JavaFX application.
     * @param trie         The compressed trie data structure for efficient searching.
     */
    public MapUI(Model model, Stage primaryStage, CompressedTrie trie) {
        super(primaryStage, new Canvas(1000, 600), "Map of " + TitleUI.selectedFile);
        this.model = model;
        userInput = new MapGUI(this, trie);
        oldArrea = (canvas.getHeight()*canvas.getWidth());
        this.primaryStage.setAlwaysOnTop(true);
        Model.getGraph().addGraphToMap();
        zoomFactor = 1.0;
        zoomLevel = new Label();
        stackpane.getChildren().addAll(canvas, burgerMenu.gethBox(), zoomLevel, userInput.getUserInterface());
        StackPane.setAlignment(userInput.getUserInterface(), Pos.TOP_RIGHT);
        StackPane.setAlignment(zoomLevel, Pos.BOTTOM_LEFT);
        StackPane.setAlignment(burgerMenu.gethBox(), Pos.TOP_LEFT);

        showStage();
        pan(-0.56 * model.minlon, model.maxlat);
        zoom(0, 0, canvas.getHeight() / (model.maxlat - model.minlat));
    }

    double minX =  (-trans.getTx()/trans.getMxx())/0.56;
    double minY =  (-trans.getTy()/trans.getMyy());
    double maxX =  ((canvas.getWidth()/trans.getMxx()) + minX)/0.56;
    double maxY =  ((canvas.getHeight()/trans.getMyy()) + minY);
    double width = (maxX - minX);
    double height = (maxX - minX);

    /**
     * Redraws the map on the canvas.
     */
    @Override
    public void redraw() {
        super.redraw(Color.WHITE);

        gc.setLineWidth(1 / Math.sqrt(trans.determinant()));
        SerializableRectangle rect = new SerializableRectangle(model.minlat, model.minlon, model.maxlat, model.maxlon);
        List<Way> waysInBounds = new ArrayList<>();
        try {
            minX =  (-trans.getTx()/trans.getMxx())/0.56;
            minY =  (-trans.getTy()/trans.getMyy());
            maxX =  ((canvas.getWidth()/trans.getMxx()) + minX)/0.56;
            maxY =  ((canvas.getHeight()/trans.getMyy()) + minY);
            if(maxY<0)
                minY = maxY;

            width = (maxX - minX);
            height = (maxX - minX);

            oldArrea = (width * height);
            //area = width * height;
            rect = new SerializableRectangle(-minY, minX, width, height);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(Way.drawPath) {
            Model.drawPaths(gc, rect);
        }
        else {
            Model.draw(gc, rect);
        }
    }

    /**
     * Pans the map by the specified amount.
     *
     * @param dx The horizontal distance to pan.
     * @param dy The vertical distance to pan.
     */
    public void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        redraw();
    }

    /**
     * Zooms the map by the specified factor around the specified point.
     *
     * @param dx     The horizontal coordinate of the zoom center.
     * @param dy     The vertical coordinate of the zoom center.
     * @param factor The zoom factor.
     */
    public void zoom(double dx, double dy, double factor) {
        // Calculate the new zoom factor and check if it's within acceptable limits
        double newZoomFactor = zoomFactor * factor;
        if (newZoomFactor >= 0) {
            // Pan to the zoom center
            pan(-dx, -dy);

            // Apply scaling
            trans.prependScale(factor, factor);

            // Update zoom factor
            zoomFactor = newZoomFactor;

            // Calculate and display zoom level
            double currentArea = (canvas.getWidth() / trans.getMxx()) * (canvas.getHeight() / trans.getMyy());
            double zoomLevelPercentage = oldArrea/currentArea;
            if(zoomLevelPercentage < 1)
                zoomLevelPercentage = 1;
            zoomLevel.setText("Zoom Level: " + String.format("%.0f", 20*Math.log(zoomLevelPercentage)) +"%");
            // Pan back to the original position
            pan(dx, dy);
        }
    }


    /**
     * Changes the screen to the title UI, effectively resetting the application.
     */
    public void changeScreen(){
        primaryStage.hide();
        var startUp = new TitleUI(primaryStage);
        new Controller(startUp);
        model.hardReset();
        primaryStage.show();
        System.gc();
    }
}