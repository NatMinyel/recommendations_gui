package app.controllers;

import app.helpers.Renderer;
import app.helpers.Requests;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {

    @FXML
    private JFXTextField tf_email;
    @FXML
    private JFXPasswordField pf_password, pf_confirm_password;

    @FXML
    public void login(MouseEvent event) throws IOException {
        Renderer renderer = new Renderer();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        renderer.render(stage, "../views/login.fxml", "Recommendations- Login");
    }

    @FXML
    public void signup(ActionEvent event) throws IOException {

        Renderer renderer = new Renderer();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        String password = pf_password.getText();
        String confirm_password = pf_confirm_password.getText();
        String email = tf_email.getText();

        if (!confirm_password.equals(password)) {
            System.out.println("Passwords don't match");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        JSONObject user = Requests.postRequestObj("http://localhost:8080/user/", "", params);

        if (user.length() == 0) {
            System.out.println(user.toString());
            System.out.println("Email already exists.");
        } else {
            renderer.render(stage, "../views/login.fxml", "Recommendations Home");
        }
    }

    private double x = 0, y = 0;

    @FXML
    public void dragged(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setY(event.getScreenY() - y);
        stage.setX(event.getScreenX() - x);
    }

    @FXML
    public void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
