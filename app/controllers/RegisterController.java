package app.controllers;

import app.helpers.Renderer;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import static app.helpers.DBUtils.connection;
import static app.helpers.Security.loginUser;

public class RegisterController implements Initializable {

    @FXML
    private JFXTextField tf_username, tf_email;
    @FXML
    private JFXPasswordField pf_password, pf_confirm_password;

    @FXML
    public void login(MouseEvent event) throws IOException {
        Renderer renderer = new Renderer();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        renderer.render(stage, "../views/login.fxml");
    }

    @FXML
    public void signup(ActionEvent event) throws IOException, SQLException {

        Renderer renderer = new Renderer();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        Statement statement = connection.createStatement();

        String username = tf_username.getText();
        String password = pf_password.getText();
        String confirm_password = pf_confirm_password.getText();
        String email = tf_email.getText();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String datetime = dateFormat.format(date);

        if (!confirm_password.equals(password)) {
            System.out.println("Passwords don't much");
            return;
        }
        ResultSet resultSet = statement.executeQuery("SELECT * FROM account WHERE username='" + username + "'");
        if (!resultSet.next()) {
            resultSet = statement.executeQuery("SELECT * FROM account WHERE email='" + email + "'");
            if (!resultSet.next()) {
                statement.executeUpdate("INSERT INTO public.account(username, password, email, created_on, last_login) " +
                        "VALUES ('" + username + "', '" + password + "', '" + email + "', '" + datetime + "', '" + datetime + "')");
                resultSet = statement.executeQuery("SELECT * FROM account WHERE username='" + username + "'");
                if (resultSet.next()) {
                    loginUser(resultSet);
                    renderer.render(stage, "../views/home.fxml");
                }
            } else {
                System.out.println("There's a user with that email");
            }
        } else {
            System.out.println("There's a user with that username");
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
