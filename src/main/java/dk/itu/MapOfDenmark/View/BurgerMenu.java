package dk.itu.MapOfDenmark.View;

import dk.itu.MapOfDenmark.Model.ColorManager;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class BurgerMenu extends UIElement {
    /** Indicates whether the menu is open or closed. */
    private boolean open = false;

    /** The toggle button for opening/closing the menu. */
    public ImageToggleButton openClose = new ImageToggleButton("burgerClosed.png", "burgerOpen.png");

    /** The button for toggling color mode. */
    public final Button normalColorButton = new Button(ColorManager.getColorMode());

    /** The button for resetting to the title screen. */
    public final Button reset = new Button("Title Screen");

    /** The toggle button for activating developer mode. */
    public final ToggleButton devButton = new ToggleButton("Dev Mode", "Go Back");

    /** The container for menu options. */
    public final VBox options = new VBox(reset, normalColorButton, devButton);

    /** The horizontal box containing the menu button and options. */
    public final HBox hBox = new HBox(openClose, options);

    /**
     * Toggles the visibility of the menu options.
     */
    public void toggle(){
        openClose.toggle();
        open = !open;
        options.getChildren().forEach(child -> child.setVisible(open));
    }

    /**
     * Retrieves the styled horizontal box containing the menu button and options.
     *
     * @return The styled HBox containing the menu elements.
     */
    public HBox gethBox() {
        openClose.setMaxHeight(40);
        openClose.setMaxWidth(40);
        openClose.setSize(30);
        openClose.setStyle(
                "-fx-background-color: transparent;" + /* Transparent background */
                        "-fx-background-radius: 0;" + /* No rounded corners */
                        "-fx-font-size: 14px;" /* Font size */
        );

        // Style for normalColorButton
        normalColorButton.setStyle(
                "-fx-background-color: Grey;" + /* Green background */
                        "-fx-background-radius: 5;" + /* Rounded corners */
                        "-fx-text-fill: white;" + /* White text color */
                        "-fx-font-size: 14px;" /* Font size */
        );


        // Style for devButton
        devButton.setStyle(
                "-fx-background-color: Grey;" + /* Grey background */
                        "-fx-background-radius: 5;" + /* Rounded corners */
                        "-fx-text-fill: white;" + /* White text color */
                        "-fx-font-size: 14px;" /* Font size */
        );

        // Style for reset button
        reset.setStyle(
                "-fx-background-color: #F44336;" + /* Blue background */
                        "-fx-background-radius: 5;" + /* Rounded corners */
                        "-fx-text-fill: white;" + /* White text color */
                        "-fx-font-size: 14px;" /* Font size */
        );
        toggle();
        options.setSpacing(5);
        hBox.setSpacing(10);
        hBox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        hBox.setMaxSize(500,200);
        hBox.setStyle("-fx-padding: 10px;" /* Padding */);
        return hBox;
    }
}