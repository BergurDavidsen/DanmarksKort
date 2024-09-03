package dk.itu.MapOfDenmark;

import dk.itu.MapOfDenmark.Model.Trees.Trie.CompressedTrie;
import dk.itu.MapOfDenmark.Model.objects.abstracts.Way;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import dk.itu.MapOfDenmark.Model.ColorManager;
import dk.itu.MapOfDenmark.Model.Graph.Graph;
import dk.itu.MapOfDenmark.Model.Graph.Vertex;
import dk.itu.MapOfDenmark.Model.Model;
import dk.itu.MapOfDenmark.Model.objects.*;
import dk.itu.MapOfDenmark.View.*;
import javafx.scene.transform.NonInvertibleTransformException;
import java.io.IOException;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Controller {

    /**
     * Constructs a Controller object for the TitleUI screen.
     *
     * @param selectorScreen The TitleUI object representing the selector screen.
     */
    public Controller(TitleUI selectorScreen) {
        selectorScreen.dragNdrop.setOnMouseClicked(e->{
            selectorScreen.openFolder();
            e.consume();
    });

        selectorScreen.dragNdrop.setOnDragOver(e->{
            if (e.getGestureSource() != selectorScreen.dragNdrop && e.getDragboard().hasFiles()) {
                e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            e.consume();
        });

        selectorScreen.dragNdrop.setOnDragDropped(e -> { // transfer the file to the dragNdrop rect
            var db = e.getDragboard();
            boolean success = false;

            try {
                if (db.hasFiles()) {
                    success = selectorScreen.transferFile(db); // make sure the dropped file is the correct fil
                    e.setDropCompleted(success);
                    if (success) {
                        selectorScreen.selectFile(db.getFiles().get(0).getAbsolutePath()); // change the screen
                    }

                }
            } catch (FileNotFoundException | NullPointerException | UnsupportedOperationException exception){
                System.out.println(exception.getMessage());
                selectorScreen.setErrorMessage(exception.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            e.consume();


        });

        selectorScreen.primaryStage.widthProperty().addListener(e -> { // Resizing width
            selectorScreen.canvas.setWidth(selectorScreen.primaryStage.getWidth());
            selectorScreen.redraw();
        });

        selectorScreen.primaryStage.heightProperty().addListener(e -> { // Resizing height
            selectorScreen.canvas.setHeight(selectorScreen.primaryStage.getHeight());
            selectorScreen.redraw();
        });

        selectorScreen.selectFromList.setOnAction(e -> { // toggling dropdown menu
            selectorScreen.toggle();
        });

        selectorScreen.filter.setOnAction(e->{
            selectorScreen.filter();
            e.consume();
        });
    }

    double lastX;
    double lastY;
    private final ArrayList<float[]> clickedPoints = new ArrayList<>();

    public CompressedTrie trie;

    /**
     * Constructs a Controller object for the MapUI screen.
     *
     * @param view The MapUI object representing the map view.
     * @param trie The CompressedTrie object representing the trie data structure.
     */
    public Controller(MapUI view, CompressedTrie trie) {
        this.trie = trie;

        view.primaryStage.widthProperty().addListener(e -> { // resizing width
            view.canvas.setWidth(view.primaryStage.getWidth());
            view.redraw();
        });

        view.primaryStage.heightProperty().addListener(e -> { // resizing height
            view.canvas.setHeight(view.primaryStage.getHeight());
            view.redraw();
        });

        view.burgerMenu.reset.setOnAction(e -> { // change to title screen
            view.changeScreen();
            e.consume();
        });

        view.canvas.setOnScroll(e -> { // zooming
            int factor = (int) e.getDeltaY() >> 2;
            view.zoom(e.getX(), e.getY(), Math.pow(1.01, factor));
            e.consume();
        });

        view.canvas.setOnMouseDragged(e -> { // panning
            if (e.isSecondaryButtonDown()) {
                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
                view.pan(dx, dy);
                e.consume();
            }
            lastX = e.getX();
            lastY = e.getY();
            e.consume();
        });

        view.burgerMenu.normalColorButton.setOnAction(e -> { // change colour mode
            ColorManager.toggle();
            view.burgerMenu.normalColorButton.setText(ColorManager.getColorMode());
            view.redraw();
            e.consume();
        });

        view.burgerMenu.openClose.setOnAction(e -> { // toggle the visibility of the other items in the burgerMenu
            view.burgerMenu.toggle();
            e.consume();
        });

        view.burgerMenu.devButton.setOnAction(e -> { // change to dev/graph mode
            view.burgerMenu.devButton.toggle();
            Way.drawPath = !Way.drawPath;
            view.redraw();
            e.consume();
        });


        view.userInput.vehiclePriority.setOnAction(e->{ // choose between walking or driving
            view.userInput.vehiclePriority.toggle();
            Graph.wantToDrive = !Graph.wantToDrive;
            e.consume();
        });


        view.userInput.searchRoute.setOnMousePressed(e->{ // event handler for
            Model.quadTreeMap.get(PointOfInterest.class).clear();
            Model.quadTreeMap.get(Path.class).clear();
            view.redraw();

            try{
                view.userInput.messageToUser.setText(Graph.executeDijkstraAction(clickedPoints));
                view.redraw();
            } catch (NullPointerException error){
                view.userInput.messageToUser.setText(error.getMessage());
            }
            e.consume();
        });



        view.canvas.setOnMousePressed(e -> { //
            lastX = e.getX();
            lastY = e.getY();

            if (e.isPrimaryButtonDown()) {
                if(clickedPoints.size() > 1){
                    clickedPoints.clear();
                    Model.quadTreeMap.get(PointOfInterest.class).clear();
                    Model.quadTreeMap.get(Path.class).clear();
                    view.redraw();
                }

                Point2D point;
                double[] queryPoint;
                float[] nearest;
                double lat;
                double lon;

                try { // getting the point from canvas.
                    point = view.trans.inverseTransform(e.getX(), e.getY());
                    lat = (point.getX() / 0.56);
                    lon = -point.getY();
                    queryPoint = new double[]{lon, lat};
                    nearest = Model.quadTreeMap.get(Vertex.class).nearestPoint(queryPoint);
                    clickedPoints.add(nearest);
                } catch (NonInvertibleTransformException ex) {
                    throw new RuntimeException(ex);
                }

                Model.add(new PointOfInterest(nearest));
            }

            view.redraw();
            e.consume();
        });



        view.userInput.userInput.setOnKeyTyped(e->{
            view.userInput.updateSearchBar(
                    new ArrayList<>(trie.searchPrefix(view.userInput.userInput.getText())));
            if(view.userInput.userInput.getText().isEmpty())
                view.userInput.updateSearchBar(new ArrayList<>());

        });

        view.userInput.userInput.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ENTER){
                if(clickedPoints.size() > 1){
                    clickedPoints.clear();
                    Model.quadTreeMap.get(PointOfInterest.class).clear();
                    Model.quadTreeMap.get(Path.class).clear();
                    view.redraw();
                }

                float[] nearest = view.userInput.findAddress(view.userInput.userInput.getText());
                clickedPoints.add(nearest);
                view.redraw();

            }
        });
    }

}
