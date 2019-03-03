package app.controllers;

import app.helpers.Renderer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchCardArtistController implements Initializable {

    class ImageSetter implements Runnable {
        private String url;
        ImageSetter(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            try {
                Image image = new Image(url);
                artist_image.setImage(image);
            } catch (IllegalArgumentException ignored) {}
        }
    }
    ImageSetter setter;
    @FXML
    ImageView artist_image;

    @FXML
    Label artist_name;


    JSONObject artist;
    public void start(JSONObject artist) {
        artist_name.setText(artist.getString("name"));
        setter = new ImageSetter(artist.getString("pictureURL"));
        Platform.runLater(setter);
        this.artist = artist;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }



    public void artistSelected(MouseEvent mouseEvent) {
        try {
            HomeController homeController = Renderer.getHomeController(mouseEvent);
            assert homeController != null;
            homeController.setItem(artist);
        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
