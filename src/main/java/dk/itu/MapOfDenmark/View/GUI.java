package dk.itu.MapOfDenmark.View;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;

/**
 * The GUI class represents a graphical user interface.
 * It provides methods for managing the primary stage, canvas, graphics context, and scene.
 */
public abstract class GUI extends UIElement {
    public Canvas canvas;
    public Stage primaryStage;
    public GraphicsContext gc;
    public Scene scene;

    public Affine trans = new Affine();
    StackPane stackpane = new StackPane();

    /**
     * Constructs a new GUI object with the specified primary stage, canvas, and title name.
     *
     * @param primaryStage The primary stage for the GUI.
     * @param canvas       The canvas for rendering graphics.
     * @param titleName    The title name of the GUI.
     */
    public GUI(Stage primaryStage, Canvas canvas, String titleName){
        this.primaryStage = primaryStage;
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        primaryStage.setTitle(titleName);
        this.scene = new Scene(stackpane);
        primaryStage.getIcons().add(getImage("appIcon.png"));
    }

    /**
     * Abstract method for redrawing the canvas.
     */
    protected abstract void redraw();

    /**
     * Redraws the canvas with the specified color.
     *
     * @param color The color to fill the canvas with.
     */
    protected void redraw(Color color){
        gc.setTransform(new Affine());
        gc.setFill(color);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
    }

    /**
     * Shows the primary stage with the current scene.
     */
    public void showStage(){
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
