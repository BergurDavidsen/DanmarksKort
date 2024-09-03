package dk.itu.MapOfDenmark.View;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.io.UncheckedIOException;

/**
 * Utility class for loading images from the resources folder.
 * There's an issue with extracting the Image from the resources folder,
 * so this is a workaround.
 */
public abstract class UIElement implements Serializable {
    /** The operating system name. */
    public static final String OPERATING_SYSTEM = System.getProperty("os.name");


    /** The path to the resources' folder. */
    private static final String PATH_TO_RESOURCES = "resources";

    /** The list of files in the resources folder. */
    private static final File[] RESOURCE_DIRECTORY = new File(PATH_TO_RESOURCES).listFiles();

    /**
     * Retrieves the folder path based on the operating system.
     *
     * @return The folder path corresponding to the operating system.
     * @throws UnsupportedOperationException If the system is not supported.
     */
    protected File getOsFolderPATH(){
        if(OPERATING_SYSTEM.toLowerCase().contains("windows"))
            return new File("C:\\");
        else if(OPERATING_SYSTEM.toLowerCase().contains("mac"))
            return  new File("/");
        else
            throw new UnsupportedOperationException(
                    "Your System is not supported, please selected a file.");
    }

    /**
     * Retrieves an image from the resources folder by name.
     *
     * @param imageName The name of the image file (with extension).
     * @return The Image object representing the specified image.
     * @throws UncheckedIOException If the image file is not found in the resources folder.
     */
    public static Image getImage(String imageName) {
        for (File file : RESOURCE_DIRECTORY)
            if (file.getName().equals(imageName))
                return new Image(file.toURI().toString());

        throw new UncheckedIOException(new FileNotFoundException("Could not find " + imageName + " in directory: " + PATH_TO_RESOURCES));
    }

    /**
     * Represents a toggleable button with images.
     * This class extends Button and implements the Toggle interface.
     */
    public static class ImageToggleButton extends Button implements Toggle{
        private final ImageView toggle1, toggle2;
        boolean toggle = true;

        ImageToggleButton(String image1, String image2){
            this.toggle1 = new ImageView(getImage(image1));
            this.toggle2 = new ImageView(getImage(image2));
            this.setGraphic(toggle1);
            this.toggle1.setFitHeight(20);
            this.toggle1.setFitWidth(20);
            this.toggle2.setFitHeight(20);
            this.toggle2.setFitWidth(20);
            this.setMaxSize(20,20);
        }

        public void setSize(double v){
            this.toggle1.setFitHeight(v);
            this.toggle1.setFitWidth(v);
            this.toggle2.setFitHeight(v);
            this.toggle2.setFitWidth(v);
        }

        @Override
        public void toggle(){
            toggle = !toggle;
            if(toggle)
                this.setGraphic(toggle1);
            else
                this.setGraphic(toggle2);
        }
    }

    /**
     * Represents a toggleable button with text.
     * This class extends Button and implements the Toggle interface.
     */
    public static class ToggleButton extends Button implements Toggle{
        private final String toggle1, toggle2;
        boolean toggle = true;


        ToggleButton(String var1, String var2){
            super();
            this.toggle1 = var1;
            this.toggle2 = var2;
            this.setText(toggle1);
        }

        /**
         * Toggles between the two text states of the button.
         * Sets the text of the button to the alternate state upon each invocation.
         */
        @Override
        public void toggle(){
            toggle = !toggle;
            if(toggle)
                this.setText(toggle1);
            else
                this.setText(toggle2);
        }
    }
}
