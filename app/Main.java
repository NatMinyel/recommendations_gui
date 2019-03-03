package app;

import app.helpers.Renderer;
import app.helpers.Security;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Renderer renderer = new Renderer();
        File tokenFile = new File("token");
        if (tokenFile.exists()) {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(tokenFile));
            String token = (String) inputStream.readObject();
            Security.loginUser(token);
            renderer.render(primaryStage, "../views/home.fxml", "Recommendations: Home");
        } else {
            renderer.render(primaryStage, "../views/login.fxml", "Recommendations: Login");
        }

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }


    public static void main(String[] args) {
//        System.out.println("Loading UI");
        launch(args);
    }
}
