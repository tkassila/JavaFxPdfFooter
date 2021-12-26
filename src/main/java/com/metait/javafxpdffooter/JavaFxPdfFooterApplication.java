package com.metait.javafxpdffooter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
        Scene scene = new Scene(fxmlLoader.load(), 904, 700);
        stage.setTitle("JavaFxPdfFooter");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}