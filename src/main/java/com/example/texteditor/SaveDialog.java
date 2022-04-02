package com.example.texteditor;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SaveDialog extends HBox {
    public Stage popup;

    SaveDialog(TextEditor textEditor, Stage stage) {
        this.setStyle("-fx-background-color: rgba(32,10,44, 0.97)");
        this.setSpacing(3);
        this.setPadding(new Insets(5, 5, 5, 5));
        TextField saveName = new TextField();
        Button save = new Button("Save");
        save.setOnMouseClicked(e -> {
            try {
                saveFile(saveName.getText(), textEditor.notepad.getText());
                textEditor.currentFileName = saveName.getText();
                stage.setTitle(textEditor.currentFileName);
                textEditor.title.setText(textEditor.currentFileName);
                this.popup.close();
            }catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        this.getChildren().addAll(saveName, save);
        Scene scene = new Scene(this);
        popup = new Stage();
        popup.setX(stage.getX() + stage.getWidth() / 2 - 100);
        popup.setY(stage.getY() + stage.getHeight() / 2 - 100);
        popup.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.ESCAPE);
            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    popup.close();
                    ke.consume(); // <-- stops passing the event to next node
                }
            }
        });
        popup.initStyle(StageStyle.UNDECORATED);
        scene.getStylesheets().add("main.css");
        popup.setScene(scene);
        popup.show();
    }

    public void saveFile(String name, String text) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(name+".txt")) {
            out.println(text);
        }
    }
}
