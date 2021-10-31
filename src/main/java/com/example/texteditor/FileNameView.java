package com.example.texteditor;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class FileNameView extends Pane {
    FileNameView(String filename){
        Label name = new Label("filename");
        HBox viewBox = new HBox();
        viewBox.setPadding(new Insets(10, 10, 10, 10));
        viewBox.getChildren().add(name);
        this.getChildren().add(viewBox);
    }
}
