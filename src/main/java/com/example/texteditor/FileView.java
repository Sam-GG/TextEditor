package com.example.texteditor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileView extends Pane {
    private TextEditor textEditor;
    private ListView<String> fileList;

    FileView(TextEditor textEditor) {
        this.textEditor = textEditor;
        ListView<String> fileList = new ListView<>();
        fileList.setPrefSize(200, 250);
        fileList.setMinSize(100, 100);
        fileList.setMaxSize(1920, 1080);
        fileList.setItems(getFileList());
        fileList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    String fileName = fileList.getSelectionModel().getSelectedItem();
                    textEditor.notepad.setText(readFile(fileName));
                }
            }
        });
        fileList.setStyle("-fx-background-color: rgba(0,0,0,0.5);-fx-text-fill: rgba(233,236,226, 0.95);-fx-font-size: 15");
        this.fileList = fileList;
        this.getChildren().add(fileList);
    }

    private ObservableList<String> getFileList() {
        ObservableList<String> fileList = FXCollections.observableArrayList();
        File folder = new File(".");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                fileList.add(file.getName());
            }
        }
        return fileList;
    }

    private String readFile(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void updateFileList() {
        fileList.setItems(getFileList());
    }
}
