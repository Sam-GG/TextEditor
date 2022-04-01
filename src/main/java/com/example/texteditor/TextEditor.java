package com.example.texteditor;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;


//This code should probably be refactored to follow MVC lol
//TODO: extract classes and make them more readable
public class TextEditor extends javafx.application.Application {
    public double xOffset = 0;
    public double yOffset = 0;

    TextArea notepad;
    String currentFileName = null;
    Label title;
    Stage popup;
    boolean fileViewerOpen = false;
    private FileView fileList;

    @Override
    public void start(Stage stage) throws IOException {
        BorderPane borderPane = new BorderPane();
        title = new Label("Big Man's editor");
        title.setStyle("-fx-text-fill: rgba(233,236,226, 0.95);-fx-font-family: 'Bauhaus 93';-fx-font-size: 16");
        ToolBar toolBar = new ToolBar();
        int height = 25;
        toolBar.setPrefHeight(height);
        toolBar.setMinHeight(height);
        toolBar.setMaxHeight(height);
        toolBar.getItems().addAll(new WindowButtons(), title);

        borderPane.setTop(toolBar);

        BorderPane root = new BorderPane();

        root.setPadding(new Insets(7, 7, 7, 7));
        notepad = new TextArea();
        notepad.setPrefSize(400, 250);
        notepad.setMinSize(100, 100);
        notepad.setMaxSize(1920, 1080);

        borderPane.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        borderPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        root.setTop(borderPane);
        root.setCenter(notepad);
        Scene scene = new Scene(root);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.S,
                    KeyCombination.CONTROL_DOWN);
            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    System.out.println("Key Pressed: " + keyComb);
                    if (currentFileName == null){
                        // Can create small popup dialog for text entry of file name to save here
                        openSaveDialog(stage);
                    }else{
                        try {
                            saveFile(currentFileName, notepad.getText());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    ke.consume(); // <-- stops passing the event to next node
                }
            }
        });

        //Duplicate current line in text area
        notepad.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.D,
                    KeyCombination.CONTROL_DOWN);
            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    System.out.println("Key Pressed: " + keyComb);
                    String currentLine = notepad.getSelectedText();
                    notepad.appendText(currentLine);
//                    ke.consume(); // <-- stops passing the event to next node
                }
            }
        });

        fileList = new FileView(this);

        //make file list viewer only visible on keyboard input
        notepad.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.F,
                    KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    System.out.println("Key Pressed: " + keyComb);
                    if (fileViewerOpen) {
                        root.getChildren().remove(fileList);
                        fileViewerOpen = false;
                    } else {
                        fileList.updateFileList();
                        root.setRight(fileList);
                        fileViewerOpen = true;
                    }
                    ke.consume(); // <-- stops passing the event to next node
                }
            }
        });

        scene.getStylesheets().add("main.css");

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("BigMan's editor");
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        ResizeHelper.addResizeListener(stage);
        stage.show();
    }

    public void saveFile(String name, String text) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(name+".txt")) {
            out.println(text);
            fileList.updateFileList();
        }
    }

    public void openSaveDialog(Stage stage){

        HBox popupRoot = new HBox();
        popupRoot.setStyle("-fx-background-color: rgba(32,10,44, 0.97)");
        popupRoot.setSpacing(3);
        popupRoot.setPadding(new Insets(5, 5, 5, 5));
        TextField saveName = new TextField();
        Button save = new Button("Save");
        save.setOnMouseClicked(e -> {
            try {
                saveFile(saveName.getText(), notepad.getText());
                this.currentFileName = saveName.getText();
                stage.setTitle(currentFileName);
                this.title.setText(currentFileName);
                this.popup.close();
            }catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        popupRoot.getChildren().addAll(saveName, save);
        Scene scene = new Scene(popupRoot);
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

    public static void main(String[] args) {
        launch();
    }
}