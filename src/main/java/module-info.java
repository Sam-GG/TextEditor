module com.example.texteditor {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.texteditor to javafx.fxml;
    exports com.example.texteditor;
}