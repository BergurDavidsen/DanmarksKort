package dk.itu.MapOfDenmark.View;

import dk.itu.MapOfDenmark.Model.Graph.Vertex;
import dk.itu.MapOfDenmark.Model.Model;
import dk.itu.MapOfDenmark.Model.Trees.Trie.CompressedTrie;
import dk.itu.MapOfDenmark.Model.objects.PointOfInterest;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;


/**
 * The MapGUI class represents the user interface elements for interacting with the map.
 * It includes search functionalities, vehicle priority selection, and route display.
 */
public class MapGUI extends UIElement {
    /** The toggle button for switching between vehicle and pedestrian priority. */
    public final ImageToggleButton vehiclePriority = new ImageToggleButton("car.png", "walkingMan.png");

    /** The button for initiating route search using Dijkstra's algorithm. */
    public final Button searchRoute = new Button();

    /** The text field for user input, typically used for address search. */
    public final TextField userInput = new TextField();

    /** The container for displaying search results based on Trie tree search. */
    public final VBox addressOutput = new VBox();

    /** The container for the search bar, including user input and search results. */
    public final VBox search = new VBox(userInput, addressOutput);

    /** The first layer of UI elements, including vehicle priority, search route, and search bar. */
    private final HBox layer1 = new HBox(vehiclePriority, searchRoute, search);

    /** The label for displaying messages to the user, typically for route information. */
    public final Label messageToUser = new Label();

    /** The second layer of UI elements, containing the message label. */
    private final HBox layer2 = new HBox(messageToUser);

    /** The container for the top bar UI elements, including both layers. */
    private final VBox topBar = new VBox(layer1, layer2);

    /** The parent MapUI instance. */
    private final MapUI mapUI;

    /** The compressed trie data structure for efficient address searching. */
    private CompressedTrie trie;

    /**
     * Constructs a new MapGUI instance with the specified MapUI parent and CompressedTrie.
     *
     * @param mapUI The parent MapUI instance.
     * @param trie The compressed trie data structure for efficient address searching.
     */
    public MapGUI(MapUI mapUI, CompressedTrie trie){
        this.mapUI = mapUI;
        this.trie = trie;
    }

    /**
     * Retrieves the user interface elements for interacting with the map.
     *
     * @return The container VBox containing all UI elements.
     */
    public VBox getUserInterface(){
        ImageView searchImage = new ImageView(getImage("search.png"));
        searchImage.setFitWidth(25);
        searchImage.setFitHeight(25);
        userInput.setStyle(
                "-fx-background-color: #FFFFFF;" + /* White background */
                        "-fx-text-fill: #000000;" + /* Black text color */
                        "-fx-font-size: 14px;" + /* Font size */
                        "-fx-font-family: Arial;" + /* Font family */
                        "-fx-border-color: #CCCCCC;" + /* Light grey border color */
                        "-fx-border-width: 1px;" + /* Border width */
                        "-fx-border-style: solid;" + /* Border style */
                        "-fx-border-radius: 5px;" + /* Border radius */
                        "-fx-min-width: 0;" + /* Reset minimum width */
                        "-fx-pref-width: 100%;" /* Set preferred width to 100% */
        );

        messageToUser.setMaxHeight(1000);
        messageToUser.setAlignment(Pos.TOP_RIGHT);
        layer1.setAlignment(Pos.TOP_CENTER);
        layer1.setMaxSize(300, 0);
        layer1.setSpacing(5);
        layer1.setSpacing(5);
        search.setPrefSize(400,300);
        searchRoute.setGraphic(searchImage);
        searchRoute.setBackground(new Background(
                new BackgroundFill(Color.GRAY, new CornerRadii(10), new Insets(-0.11))));
        searchRoute.setStyle(
                "-fx-background-color: #4CAF50;" + /* Green background */
                        "-fx-background-radius: 5;" + /* Rounded corners */
                        "-fx-text-fill: white;" + /* White text color */
                        "-fx-font-size: 14px;" /* Font size */
        );

        vehiclePriority.setSize(25);
        vehiclePriority.setBackground(
                new Background(new BackgroundFill(Color.GRAY, new CornerRadii(10), new Insets(-0.11))));
        vehiclePriority.setStyle(
                "-fx-background-color: #2196F3;" + /* Blue background */
                        "-fx-background-radius: 5;" + /* Rounded corners */
                        "-fx-text-fill: white;" + /* White text color */
                        "-fx-font-size: 14px;" /* Font size */
        );

        layer2.setAlignment(Pos.BOTTOM_RIGHT);
        topBar.setMaxSize(300, 0);
        topBar.setSpacing(5);
        topBar.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(50), null)));
        topBar.setStyle("-fx-padding: 10px;" /* Padding */);
        updateSearchBar(new ArrayList<>());
        return topBar;
    }

    /**
     * Toggles the visibility of the address output container.
     *
     * @param isOpen Indicates whether the container should be visible or hidden.
     */
    public void toggle(boolean isOpen){
        addressOutput.getChildren().forEach(child -> {
            child.setVisible(isOpen);
        });
    }

    /**
     * Updates the search bar with the provided list of addresses.
     *
     * @param addresses The list of addresses to display in the search bar.
     */
    public void updateSearchBar(List<String> addresses){
        addressOutput.getChildren().clear();

        if(addresses.isEmpty()){
            toggle(false);
            addListener();
            return;
        }

        for (int i = 0; i < addresses.size(); i++) {
            Button button = new Button(addresses.get(i));
            button.setPrefSize(500,100);
            button.setVisible(true);
            button.getProperties().put(true, addresses.get(i)); // the boolean true is the key to the address
            addressOutput.getChildren().add(button);
        }

        addListener();
    }

    /**
     * Adds event listeners to the address output buttons.
     */
    private void addListener() {
        addressOutput.getChildren().forEach(child -> {
            child.setOnMouseClicked(e -> {
                userInput.setText((String) child.getProperties().get(true));
                findAddress(userInput.getText());
                updateSearchBar(new ArrayList<>()); // throw an empty array
                mapUI.redraw();
            });
        });
    }

    /**
     * Searches for the specified address using the compressed trie.
     *
     * @param address The address to search for.
     * @return The coordinates of the found address.
     */
    public float[] findAddress(String address){

        float[] coords = trie.search(address);
        Model.add(new PointOfInterest(coords));
        float[] nearest = Model.quadTreeMap.get(Vertex.class).nearestPoint(new double[]{coords[0], coords[1]});
        return nearest;
    }
}
