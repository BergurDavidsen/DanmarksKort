package dk.itu.MapOfDenmark.Model.objects.abstracts;

/**
 * An abstract class representing an element.
 */
public abstract class Element {
    /** The class type of the element. */
    protected Class<? extends Element> classType;

    /**
     * Constructs an element.
     */
    public Element() {
    }

    /**
     * Retrieves the class type of the element.
     *
     * @return The class type of the element.
     */
    public Class<? extends Element> getClassType(){
        return this.getClass();
    }

    /**
     * Determines if the element should be zoomed in.
     *
     * @return True if the element should be zoomed in, false otherwise.
     */
    public boolean getZoom(){
        return true;
    }
}
