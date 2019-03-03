package app.helpers;

import app.controllers.HomeController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.io.IOException;

public class Renderer {

    public void render(Stage primaryStage, String xmlFile, String title)  {
        try {
//            System.out.println(xmlFile);
            Parent root = FXMLLoader.load(getClass().getResource(xmlFile));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void drag(MouseEvent event, double x, double y) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    public static HomeController getHomeController(MouseEvent mouseEvent) {
        Node search = (Node) mouseEvent.getSource();
        Object controller;
        Node node = search.getParent();
        do {
            controller = node.getUserData();
            node = node.getParent();
        } while (controller == null && node != null);
        return (HomeController) controller;
    }

}
