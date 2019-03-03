package app.controllers;

import app.helpers.Renderer;
import app.models.User;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static app.helpers.Requests.postRequestObj;
import static app.helpers.Security.loginUser;

public class LoginController implements Initializable {


    //    JFXTextField tf_username, tf_
    @FXML
    private JFXTextField tf_username;
    @FXML
    private JFXPasswordField pf_password;

    @FXML
    public void login(MouseEvent event) throws IOException {
        Renderer renderer = new Renderer();
        String email = tf_username.getText();

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        String password = pf_password.getText();

        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        JSONObject user = postRequestObj("http://localhost:8080/user/login", "", params);
        if (user.length() == 0) {
            System.out.println("Username/password incorrect");
        } else {
            loginUser(user);
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("token"));
            outputStream.writeObject(User.token);
            renderer.render(stage, "../views/home.fxml", "Recommendations- Home");
        }
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


    @FXML
    public void signup(MouseEvent event) throws IOException {
        Renderer renderer = new Renderer();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        renderer.render(stage, "../views/register.fxml", "Recommendations- Register");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
