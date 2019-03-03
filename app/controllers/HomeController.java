package app.controllers;

import app.helpers.Renderer;
import app.helpers.Requests;
import app.models.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;




public class HomeController implements Initializable {

    class SearchThread extends Thread {
        String query;
        SearchThread(String query) {
            this.query = query;
        }

        @Override
        public void run() {
            if (movie) {
                if (query.length() > 2) {
                    JSONArray array = Requests.getRequest("http://localhost:8080/user/search/movie/" + query, User.token);
                    searchItems.getChildren().clear();
                    Node[] searches = new Node[array.length()];
                    for (int i = 0; i < 100; i++) {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/searchCard.fxml"));
                            searches[i] = fxmlLoader.load();
                            SearchCardController search = fxmlLoader.getController();
                            JSONObject movie = array.getJSONObject(i);
                            search.start(movie);
                        } catch (Exception ex) {
//                        Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            searchItems.getChildren().addAll(searches);
                        } catch (Exception ignored) {

                        }
                    }
                }
            } else {
                if (query.length() > 2) {
                    JSONArray array = Requests.getRequest("http://localhost:8080/user/search/artist/" + query, User.token);
                    searchItems.getChildren().clear();
                    Node[] searches = new Node[array.length()];
                    for (int i = 0; i < 100; i++) {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/searchCardArtist.fxml"));
                            searches[i] = fxmlLoader.load();
                            SearchCardArtistController search = fxmlLoader.getController();
                            JSONObject artist = array.getJSONObject(i);
                            search.start(artist);
                        } catch (Exception ex) {
                            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            searchItems.getChildren().addAll(searches);
                        } catch (NullPointerException ignored) {

                        }
                    }
                }
            }
        }
    }

    private SearchThread searcher;
    public JFXTextField searchText;
    public JFXButton movies_bar;
    public JFXButton artists_bar;
    public ImageView propic;
    static private boolean settings = false;

    private static boolean movie = true;

    public JFXButton item_recommendations;
    public JFXButton item_ratings;

    @FXML
    private Label username_label, full_name_label;

    @FXML
    private VBox pnl_scroll;

    @FXML
    private VBox searchItems;

    @FXML
    private JFXButton edit_button;

    @FXML
    private void handleButtonAction(MouseEvent event) throws FileNotFoundException {
        recommendation = false;
        item_ratings.getStyleClass().add("active");
        item_recommendations.getStyleClass().remove("active");
        item_recommendations.getStyleClass().remove("active");
        refreshNodes();
    }

    @FXML
    private void edit_profile(MouseEvent event) {
        if (settings) {
            edit_button.setText("Edit Profile");
            setItems();
        } else {
            edit_button.setText("Close Edit Profile");
            pnl_scroll.getChildren().clear();
            try {
                Node node = FXMLLoader.load(getClass().getResource("../views/settings.fxml"));
                pnl_scroll.getChildren().add(node);
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        settings = !settings;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        movies_bar.getStyleClass().add("active");
        artists_bar.getStyleClass().removeAll("active");
//        System.out.println("Heere");
        try {
            refreshNodes();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean recommendation = false;


    private void refreshNodes() throws FileNotFoundException {
        username_label.setText(User.email);
        full_name_label.setText(User.full_name);
//        System.out.println(User.email);
        try {
            propic.setImage(new Image(new FileInputStream(User.profile_picture)));
        } catch (IllegalArgumentException e) {
            propic.setImage(new Image(new FileInputStream("/home/nathnael/Dev/recommendation/src/app/assets/images/user.png")));
        }
        if (movie) {
            setToMovie();
        } else {
            setToArtist();
        }
        if (!recommendation)
            setItems();
        else
            setRecommendationItems();
    }

    private void setRecommendationItems() {
        pnl_scroll.getChildren().clear();
//        if (itemNodes != null) {
//            pnl_scroll.getChildren().addAll(itemNodes);
//            return;
//        }
        List<Node> itemNodes;
        if (movie) {
            JSONArray array = Requests.getRequest("http://localhost:8080/user/recommend/movie", User.token);
            itemNodes = new ArrayList<>();


            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject movie = array.getJSONObject(i);
                    setMovie(movie, itemNodes, 0.0);
                } catch (Exception ex) {
//                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            pnl_scroll.getChildren().addAll(itemNodes);
        } else {
            JSONArray array = Requests.getRequest("http://localhost:8080/user/recommend/artist", User.token);
            itemNodes = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject artist = array.getJSONObject(i);
                    setArtist(artist, itemNodes, 0.0);
                } catch (Exception ex) {
//                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            pnl_scroll.getChildren().addAll(itemNodes);
        }
    }

    private void setToArtist() {
        artists_bar.getStyleClass().add("active");
        movies_bar.getStyleClass().remove("active");
        movies_bar.getStyleClass().remove("active");
        movies_bar.getStyleClass().remove("active");
        item_ratings.setText("Artist Ratings");
        item_recommendations.setText("Artist Recommendations");

    }

    private void setToMovie() {
        artists_bar.getStyleClass().remove("active");
        artists_bar.getStyleClass().remove("active");
        artists_bar.getStyleClass().remove("active");
        movies_bar.getStyleClass().add("active");

        item_ratings.setText("Movie Ratings");
        item_recommendations.setText("Movie Recommendations");
    }

    private void setItems() {
        pnl_scroll.getChildren().clear();
//        if (itemNodes != null) {
//            pnl_scroll.getChildren().addAll(itemNodes);
//            return;
//        }
        ArrayList<Node> itemNodes;
        if (movie) {
            JSONArray array = Requests.getRequest("http://localhost:8080/user/ratings/movies/my", User.token);
            itemNodes = new ArrayList<>();


            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject obj = array.getJSONObject(i);
                    JSONObject movie = obj.getJSONObject("movie");
                    double rating = obj.getDouble("rating");
                    setMovie(movie, itemNodes, rating);
                } catch (IOException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            pnl_scroll.getChildren().addAll(itemNodes);
        } else {
            JSONArray array = Requests.getRequest("http://localhost:8080/user/ratings/artists/my", User.token);
            itemNodes = new ArrayList<Node>();

            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject obj = array.getJSONObject(i);
                    JSONObject artist = obj.getJSONObject("artist");
                    double rating = obj.getDouble("rating") / 400;
                    setArtist(artist, itemNodes, rating);
                } catch (IOException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            pnl_scroll.getChildren().addAll(itemNodes);
        }
    }

    private void setMovie(JSONObject movie, List<Node> itemNodes, double rating) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/item.fxml"));
        itemNodes.add(fxmlLoader.load());
        ItemController item = fxmlLoader.getController();

        long id = movie.getLong("uid");
        JSONObject movieObj = Requests.getRequestObj("http://localhost:8080/movie/show/" + id, User.token);
        StringBuilder directorNames = new StringBuilder();
        StringBuilder genreNames = new StringBuilder();
        JSONArray directors = movieObj.getJSONArray("directors");
        JSONArray genres = movieObj.getJSONArray("genres");
        for (int j = 0; j < directors.length(); j++) {
            JSONObject director = directors.getJSONObject(j);
            directorNames.append(director.getString("name")).append(" ");
        }
        for (int j = 0; j < genres.length(); j++) {
            JSONObject genre = genres.getJSONObject(j);
            genreNames.append(genre.getString("name")).append(" ");
        }
        item.start(movie, rating, directorNames.toString(), genreNames.toString());
    }


    private void setArtist(JSONObject artist, List<Node> itemNodes, double rating) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/itemArtist.fxml"));
        itemNodes.add(fxmlLoader.load());
        ItemArtistController item = fxmlLoader.getController();
        item.start(artist, rating);
    }

    private double x = 0, y = 0;

    @FXML
    public void dragged(MouseEvent event) {
        Renderer.drag(event, x, y);
    }

    @FXML
    public void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }


    public void searchForItems(KeyEvent keyEvent) throws UnsupportedEncodingException {
        String query = URLEncoder.encode(searchText.getText() + keyEvent.getCharacter().trim(), StandardCharsets.UTF_8.name());

        if (searcher != null) {
            searcher.interrupt();
        }
        searcher = new SearchThread(query);
        Platform.runLater(searcher);

    }

    void setItem(JSONObject item) throws IOException {
        pnl_scroll.getChildren().clear();
        if (movie) {
            ArrayList<Node> movies = new ArrayList<Node>();
            setMovie(item, movies, 0.0);
            pnl_scroll.getChildren().add(movies.get(0));
        } else {
            ArrayList<Node> artists = new ArrayList<>();
            setArtist(item, artists, 0.0);
            pnl_scroll.getChildren().add(artists.get(0));
        }
    }

    public void switchToArtists(MouseEvent actionEvent) throws FileNotFoundException {
        movie = false;
        refreshNodes();
    }

    public void switchToMovies(MouseEvent actionEvent) throws FileNotFoundException {
        movie = true;
        refreshNodes();
    }

    public void handleRecommendations(MouseEvent mouseEvent) throws FileNotFoundException {
        recommendation = true;
        item_recommendations.getStyleClass().add("active");
        item_ratings.getStyleClass().remove("active");
        item_ratings.getStyleClass().remove("active");
        refreshNodes();
    }
}
