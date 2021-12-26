package com.metait.javafxpdffooter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class JavaFxPdfFooterApplication extends Application {

    private JavaFxFooterController controller;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("javafxpdffooter-view.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxPdfFooterApplication.class.getResource("javafxpdffooter-view.fxml"));
        controller = new JavaFxFooterController();
        controller.setPrimaryStage(stage);
        fxmlLoader.setController(controller);

        double scene_with = 904;
        double scene_height = 700;
        //Get primary screen bounds
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        if (screenBounds.getHeight() < scene_height)
            scene_height = screenBounds.getHeight();
        if (screenBounds.getWidth() < scene_with)
            scene_with = screenBounds.getWidth();
        System.out.println(screenBounds);

        Scene scene = new Scene(fxmlLoader.load(), scene_with, scene_height);
        stage.setTitle("JavaFxPdfFooter");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}