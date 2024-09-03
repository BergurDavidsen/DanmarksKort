module dk.itu.MapOfDenmark {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires javafx.graphics;
    requires java.desktop;
    requires org.junit.jupiter.api;


    opens dk.itu.MapOfDenmark to javafx.fxml;
    exports dk.itu.MapOfDenmark;
    exports dk.itu.MapOfDenmark.View;
    opens dk.itu.MapOfDenmark.View to javafx.fxml;
    exports dk.itu.MapOfDenmark.Model.objects;
    opens dk.itu.MapOfDenmark.Model.objects to javafx.fxml;
    exports dk.itu.MapOfDenmark.Model;
    opens dk.itu.MapOfDenmark.Model to javafx.fxml;
    exports dk.itu.MapOfDenmark.Tests;
    opens dk.itu.MapOfDenmark.Tests to javafx.fxml;
    exports dk.itu.MapOfDenmark.Model.objects.abstracts;
    opens dk.itu.MapOfDenmark.Model.objects.abstracts to javafx.fxml;
    exports dk.itu.MapOfDenmark.Model.Trees.QuadTree;
    opens dk.itu.MapOfDenmark.Model.Trees.QuadTree to javafx.fxml;
}