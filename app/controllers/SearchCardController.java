package app.controllers;

import app.helpers.Renderer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONObject;

public class SearchCardController implements Initializable {

    class ImageSetter implements Runnable {
        private String url;
        ImageSetter(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            try {
                Image image = new Image(url);
                movie_image.setImage(image);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    ImageSetter setter;

    @FXML
    ImageView movie_image;

    @FXML
    Label movie_name;

    @FXML
    Label movie_year;

    JSONObject movie;
    public void start(JSONObject movie) {
        movie_name.setText(movie.getString("title"));
        movie_year.setText("Year: " + movie.getInt("year"));
        setter = new ImageSetter(movie.getString("rtPictureURL"));
        Platform.runLater(setter);
        this.movie = movie;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }



    public void movieSelected(MouseEvent mouseEvent) {
        System.out.println(movie.getInt("uid"));
        try {
            HomeController homeController = Renderer.getHomeController(mouseEvent);
            assert homeController != null;
            homeController.setItem(movie);
        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
