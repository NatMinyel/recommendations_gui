package app.controllers;

import app.helpers.Renderer;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static app.helpers.DBUtils.connection;
import static app.helpers.Security.loginUser;

public class LoginController implements Initializable {


    //    JFXTextField tf_username, tf_
    @FXML
    private JFXTextField tf_username;
    @FXML
    private JFXPasswordField pf_password;

    @FXML
    public void login(MouseEvent event) throws IOException, SQLException {
        Renderer renderer = new Renderer();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        String username = tf_username.getText();
        String password = pf_password.getText();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM account WHERE username=\'" + username + "\' and password=\'" + password + "\'");
        if (resultSet.next()) {
            loginUser(resultSet);
            renderer.render(stage, "../views/home.fxml");
        } else {
            System.out.println("User not found");
        }
    }

    private double x = 0, y = 0;

    @FXML
    public void dragged(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
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
        renderer.render(stage, "../views/register.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
