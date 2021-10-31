package com.example.texteditor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloApplication extends Application {
    public double xOffset = 0;
    public double yOffset = 0;

    TextArea notepad;
    String currentFileName = null;
    Label title;
    Stage popup;
    @Override
    public void start(Stage stage) throws IOException {
//        Add css dark styling
//        add file list viewer
//        see if you can style the top window bar sleeker
        BorderPane borderPane = new BorderPane();
//        borderPane.setStyle("-fx-background-color: green;");
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

        root.setPadding(new Insets(4, 4, 4, 4));
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
        scene.getStylesheets().add("main.css");
//        scene.getStylesheets().add(getClass().getResource("/main.css").toExternalForm());



        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("BigMan's editor");
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        ResizeHelper.addResizeListener(stage);
        stage.show();
    }

    class WindowButtons extends HBox {

        public WindowButtons() {
            Button closeBtn = new Button("X");

            closeBtn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent actionEvent) {
                    Platform.exit();
                }
            });

            this.getChildren().add(closeBtn);
        }
    }

    public void saveFile(String name, String text) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(name+".txt")) {
            out.println(text);
        }
    }

    public void openSaveDialog(Stage stage){

        HBox root = new HBox();
        root.setSpacing(3);
        root.setPadding(new Insets(5, 5, 5, 5));
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

        root.getChildren().addAll(saveName, save);
        Scene scene = new Scene(root);
        popup = new Stage();
        popup.initStyle(StageStyle.UNDECORATED);
        scene.getStylesheets().add("main.css");
        popup.setScene(scene);
        popup.show();
    }

    public static void main(String[] args) {
        launch();
    }
}