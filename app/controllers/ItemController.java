package app.controllers;

import app.helpers.Requests;
import app.models.User;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
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

public class ItemController implements Initializable {


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

    public ImageView movie_image;
    public Label movie_name;
    public Label movie_rating_critic;
    public Label movie_rating_audience;
    public Label movie_ratings_no;
    public Label movie_directors;
    public Label movie_country;
    public Label movie_genre;
    public Label movie_year;
    public FontAwesomeIconView rating_1;
    public FontAwesomeIconView rating_2;
    public FontAwesomeIconView rating_3;
    public FontAwesomeIconView rating_4;
    public FontAwesomeIconView rating_5;

    public FontAwesomeIconView[] ratings;
    private int uid;

    private String movie_link;

    public void start(JSONObject movie, double rating, String directors, String genres) {
        uid = movie.getInt("uid");
        movie_name.setText(movie.getString("title"));
        movie_rating_audience.setText(String.valueOf(movie.getDouble("rtAudienceScore")));
        movie_rating_critic.setText(String.valueOf(movie.getDouble("rtAllCriticsScore")));
        movie_ratings_no.setText(String.valueOf(movie.getDouble("rtAudienceNumRatings")));
        movie_link = "http://imdb.com/title/tt" + movie.getString("imdbID");
        movie_directors.setText(directors);
        movie_genre.setText("Genre: " + genres);

        movie_year.setText(String.valueOf(movie.getInt("year")));
        movie_country.setText(movie.getString("country"));

        setter = new ImageSetter(movie.getString("rtPictureURL"));
        Platform.runLater(setter);
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

    public void rate_movie(MouseEvent mouseEvent) {
        FontAwesomeIconView fontAwesomeIconView = (FontAwesomeIconView) mouseEvent.getSource();
        int index = Integer.parseInt(fontAwesomeIconView.getId().split("_")[1]);
        Map<String, Object> rating = new HashMap<>();
        rating.put("rating", (double) index);
        System.out.println(uid);
        Requests.postRequestObj("http://localhost:8080/user/like/movie/" + uid, User.token, rating);
        setRating(index);
    }

    public void linkClicked(MouseEvent mouseEvent) {
        WebView wv = new WebView();
//        wv.getEngine().setCreatePopupHandler(new Callback<PopupFeatures, WebEngine>() {
//            @Override
//            public WebEngine call(PopupFeatures p) {
//                Stage stage = new Stage(StageStyle.UTILITY);
//                WebView wv2 = new WebView();
//                stage.setScene(new Scene(wv2));
//                stage.show();
//                return wv2.getEngine();
//            }
//        });

        wv.getEngine().load(movie_link);

        StackPane root = new StackPane();
        root.getChildren().add(wv);

        Stage primaryStage = new Stage();
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle(movie_link);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

