package app.controllers;

import app.helpers.Requests;
import app.models.User;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ItemArtistController implements Initializable {

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

    @FXML
    private ImageView artist_image;
    @FXML
    private Label artist_name;
    @FXML
    private FontAwesomeIconView rating_1;
    @FXML
    private FontAwesomeIconView rating_2;
    @FXML
    private FontAwesomeIconView rating_3;
    @FXML
    private FontAwesomeIconView rating_4;
    @FXML
    private FontAwesomeIconView rating_5;
    private FontAwesomeIconView[] ratings;
    private int uid;

    private String artist_link;
    ImageSetter setter;

    public void start(JSONObject artist, double rating) {
        uid = artist.getInt("uid");
        artist_name.setText(artist.getString("name"));
        setter = new ImageSetter(artist.getString("pictureURL"));
        Platform.runLater(setter);
//        artist_image.setImage(new Image(artist.getString("pictureURL")));
        artist_link = artist.getString("url");
        setRating(rating);

    }

    private void setRating(double rating) {
        int i;
        for (i = 1; i <= rating; i++) {
            ratings[i - 1].setFill(Paint.valueOf("ffee00"));
        }
        for (; i < 6; i++) {
            ratings[i - 1].setFill(Paint.valueOf("white"));
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ratings = new FontAwesomeIconView[]{rating_1, rating_2, rating_3, rating_4, rating_5};
    }

    public void starHoverEnter(MouseEvent mouseEvent) {
        FontAwesomeIconView fontAwesomeIconView = (FontAwesomeIconView) mouseEvent.getSource();
        fontAwesomeIconView.setSize("35");
    }

    public void starHoverLeave(MouseEvent mouseEvent) {
        FontAwesomeIconView fontAwesomeIconView = (FontAwesomeIconView) mouseEvent.getSource();
        fontAwesomeIconView.setSize("30");
    }


    public void rate_artist(MouseEvent mouseEvent) {
        FontAwesomeIconView fontAwesomeIconView = (FontAwesomeIconView) mouseEvent.getSource();
        Map<String, Object> rating = new HashMap<>();
        int index = Integer.parseInt(fontAwesomeIconView.getId().split("_")[1]);
        System.out.println(uid);
        rating.put("rating", (double) index);
        Requests.postRequestObj("http://localhost:8080/user/like/artist/" + uid, User.token, rating);
        setRating(index);
    }

}

