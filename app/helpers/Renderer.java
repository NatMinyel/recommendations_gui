package app.helpers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Renderer {

    public void render(Stage primaryStage, String xmlFile) throws IOException  {
        Parent root = FXMLLoader.load(getClass().getResource(xmlFile));

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
    }
}
