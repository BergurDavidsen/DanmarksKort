package dk.itu.MapOfDenmark.View;

import dk.itu.MapOfDenmark.App;
import dk.itu.MapOfDenmark.Controller;
import dk.itu.MapOfDenmark.Model.BinaryFileData;
import dk.itu.MapOfDenmark.Model.Model;
import dk.itu.MapOfDenmark.Model.ParsingElement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.input.Dragboard;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The FileSelectorScreen class represents a screen for selecting files.
 * It allows users to choose a file from a list of files in a directory.
 */
public class TitleUI extends GUI {
    private boolean toggle = false;
    private boolean filterToggle = true;
    private final Integer[] dimensions = new Integer[]{200,20};

    // left box
    public Button selectFromList = new Button("Select from list");
    public Button filter = new Button();
    public VBox selection = new VBox(filter, selectFromList);

    public VBox fileSelection = new VBox();
    public HBox LEFT_BOX = new HBox(fileSelection, selection);


    // middle box
    private Label leftArrow = new Label("⇐ Select from files");
    private ImageView introIcon = new ImageView(getImage("titleIcon.png"));
    private Label rightArrow = new Label("Drag n' drop ⇒");
    public VBox MIDDLE_BOX = new VBox(leftArrow, introIcon, rightArrow);

    // right box
    public Label errorMessage = new Label("");
    public Rectangle dragNdrop = new Rectangle(200,200,200,200);
    private Label label = new Label("Click for drag & drop");
    public VBox RIGHT_BOX = new VBox(errorMessage, dragNdrop, label);


    // main box
    public HBox mainBox = new HBox(LEFT_BOX, MIDDLE_BOX, RIGHT_BOX);

    // fields for file management
    public static String selectedFile;
    private String pathToData = "./data/";
    private final File directory = new File(pathToData);
    private List<File> files;
    public void getFolder(String extension){
        this.files = getFiles(extension);

        for (File file : files) {
            Button button = new Button(file.getName());
            button.setVisible(toggle);
            button.getProperties().put("file", file);

            button.setStyle("-fx-font-size: 13px; -fx-text-fill: white; -fx-font-weight: bold;");
            button.setBackground(
                    new Background(
                            new BackgroundFill(Color.GRAY,
                                    new CornerRadii(10),
                                    new Insets(-0.11)
                            )
                    )
            );

            fileSelection.getChildren().add(button);
        }

        attachEventHandlers();
    }

    private void attachEventHandlers() {
        fileSelection.getChildren().forEach(child -> {
            child.setOnMouseClicked(e -> {
                selectFile(((File) child.getProperties().get("file")).getAbsolutePath());
                e.consume();
            });

            child.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    selectFile(((File) child.getProperties().get("file")).getAbsolutePath());
                    e.consume();
                }
            });
        });
    }

    private List<File> getFiles(String extension){
        List<File> files = new ArrayList<>();
        File[] items = directory.listFiles();

        if(items != null)
            for (File item : items)
                if (item.getName().endsWith(extension))
                    files.add(item);

        return files;
    }

    /**
     * Filters the files based on the extension and displays them in the file selection.
     */
    public void filter(){
        filterToggle = !filterToggle;
        fileSelection.getChildren().clear(); // clear the nodes

        getFolder(filterToggle ? ".obj" : ".osm");
    }

    /**
     * Selects the specified file and changes the screen accordingly.
     *
     * @param fileName The absolute path of the selected file.
     */
    public void selectFile(String fileName){
        String[] name = fileName.split("\\\\");
        try {
            selectedFile = name[name.length-1];
            App.path = fileName;
            changeScreen(fileName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void changeScreen(String fileName) throws Exception {
        primaryStage.hide();
        long startTime = System.currentTimeMillis();
        ParsingElement model = Model.load(fileName);
        System.out.println("Time to parse file: " + (System.currentTimeMillis() - startTime) + "ms");
        if(model instanceof BinaryFileData b){
            model = b.getModel();
            ((Model) model).setGraph(b.getGraph());
            ((Model) model).setQuadTreeMap(b.getQuadTreeMap());
            ((Model) model).setTrie(b.getTrie());
            ((Model) model).setBounds(b.getBounds());
        }
        var view = new MapUI((Model) model, primaryStage,((Model) model).getTrie());
        new Controller(view, ((Model) model).getTrie());
        primaryStage.show();
    }

    /**
     * Opens the folder containing the map data.
     */
    public void openFolder(){
        try {
            Desktop.getDesktop().open(getOsFolderPATH());
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Constructs a new FileSelectorScreen with the specified primaryStage.
     *
     * @param primaryStage The primary stage to set the scene on.
     */
    public TitleUI(Stage primaryStage) {
        super(primaryStage, new Canvas(1000,600), "Select your file");
        primaryStage.setAlwaysOnTop(true);
        redraw();
        stackpane.getChildren().addAll(canvas, getMainBox());
        StackPane.setAlignment(mainBox, Pos.TOP_CENTER);
        showStage();
    }

    /**
     * Redraws the canvas.
     */
    @Override
    public void redraw(){
        super.redraw(Color.rgb(37, 36, 40));
    }

    /**
     * Checks if a file can be transferred and verifies its validity.
     *
     * @param dragBoard The dragboard containing the file.
     * @return True if the file transfer is successful, otherwise false.
     * @throws IOException If an error occurs during file transfer.
     */
    public boolean transferFile(Dragboard dragBoard) throws IOException
    {
        if(dragBoard.getFiles().size() > 1)
            throw new UnsupportedOperationException("One file only!");
        if(dragBoard.getFiles().get(0).isDirectory())
            throw new IOException("Please drop a File and not a Folder");
        if(!(dragBoard.getFiles().get(0).toString().endsWith("osm") || dragBoard.getFiles().get(0).toString().endsWith(".zip")))
            throw new IOException("Please drop an .osm- or a .zip-file");
        if(dragBoard.getFiles().isEmpty())
            throw new NullPointerException("Selected an empty file?");

        return files.get(0) != null;
    }

    /**
     * Sets the error message.
     *
     * @param errorMessage The error message to set.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage.setText(errorMessage);

        // I wanted to make the errorMessage disappear after 3 seconds,
        // but it seems like we need Multi thread for this to work....
    }

    /**
     * Toggles the visibility of the file selection.
     */
    public void toggle(){
        toggle = !toggle;
        for (Node child : fileSelection.getChildren()) {
            if(!child.equals(selectFromList))
                child.setVisible(toggle);
        }
    }

    /**
     * Predetermined configurations for the main box.
     * @return main container.
     */
    public HBox getMainBox(){
        ImageView filterIcon = new ImageView(getImage("filter.png"));
        filterIcon.setFitHeight(16);
        filterIcon.setFitWidth(16);
        filter.setGraphic(filterIcon);

        filter.setBackground(
                new Background(
                        new BackgroundFill(Color.GRAY,
                                new CornerRadii(10),
                                new Insets(-0.11)
                        )
                )
        );

        fileSelection.setAlignment(Pos.CENTER);
        fileSelection.setSpacing(5);
        selection.setAlignment(Pos.CENTER);
        selection.setSpacing(56);
        selectFromList.setStyle(
                "-fx-font-family: 'Dialog'; -fx-font-size: 16.5; -fx-text-fill: black; -fx-font-weight: bolder"
        );
        selectFromList.setBackground(
                new Background(
                        new BackgroundFill(Color.GRAY,
                                new CornerRadii(10),
                                new Insets(-0.11)
                        )
                )
        );

        getFolder(".obj");
        selectFromList.setRotate(-90);
        selectFromList.setMaxSize(dimensions[0],dimensions[1]);
        LEFT_BOX.setAlignment(Pos.CENTER_RIGHT);
        LEFT_BOX.setSpacing(5);

        label.setStyle("-fx-font-family: 'Dialog'; -fx-font-size: 16.5; -fx-text-fill: gray; -fx-font-weight: bolder");
        errorMessage.setAlignment(Pos.BOTTOM_CENTER);
        errorMessage.setBackground(Background.fill(Color.RED));
        dragNdrop.setFill(Color.TRANSPARENT);
        dragNdrop.setStroke(Color.LIGHTGRAY);
        dragNdrop.setStrokeWidth(7);
        RIGHT_BOX.setAlignment(Pos.BASELINE_CENTER);
        RIGHT_BOX.setSpacing(5);
        RIGHT_BOX.setMaxSize(dimensions[0], dimensions[1]);

        leftArrow.setStyle(
                "-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;"
        );
        leftArrow.setAlignment(Pos.TOP_CENTER);
        rightArrow.setStyle(
                "-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;"
        );
        introIcon.setFitWidth(300);
        introIcon.setFitHeight(300);
        MIDDLE_BOX.setAlignment(Pos.CENTER);

        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(40);

        return mainBox;
    }
}
