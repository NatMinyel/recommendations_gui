package app.controllers;

import app.helpers.Renderer;
import app.helpers.Requests;
import app.helpers.Security;
import app.models.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    public Stage stage;

    @FXML
    public JFXButton save_profile_btn;
    @FXML
    public JFXPasswordField txt_confirm_password;
    @FXML
    public JFXButton change_password_btn;
    @FXML
    public ImageView image;
    @FXML
    public JFXTextField txt_full_name;
    @FXML
    public JFXPasswordField txt_password;
    @FXML
    public JFXButton upload_btn;

    private String fileName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    public void change_password(ActionEvent actionEvent) throws IOException {
        if (!txt_confirm_password.getText().equals(txt_password.getText())) {
            System.out.println("Passwords do not match");
//            System.out.println(txt_confirm_password.getText());
//            System.out.println(txt_password.getText());
            return;
        }
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("password", txt_password.getText());
        JSONObject obj = Requests.postRequestObj("http://localhost:8080/user/edit", User.token, userProfile);
        Security.loginUser(obj);
        stage = (Stage) txt_full_name.getScene().getWindow();
        Renderer renderer = new Renderer();
        renderer.render(stage, "../views/home.fxml", "Recommendations- Home");
    }

    public void save_profile(ActionEvent actionEvent) throws IOException {
        Map<String, Object> userProfile = new HashMap<>();
        if (fileName != null)
            userProfile.put("profilePicture", fileName);
        if (!txt_full_name.getText().equals(""))
            userProfile.put("name", txt_full_name.getText());

        if (!userProfile.isEmpty()) {
            JSONObject obj = Requests.postRequestObj("http://localhost:8080/user/edit", User.token, userProfile);
            Security.loginUser(obj);
            Renderer renderer = new Renderer();
            stage = (Stage) txt_full_name.getScene().getWindow();
            renderer.render(stage, "../views/home.fxml", "Recommendations- Home");
        }
    }

    public void upload_image(ActionEvent actionEvent) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        final File file = fileChooser.showOpenDialog(stage);
        fileName = file.getAbsolutePath();
        image.setImage(new Image(new FileInputStream(fileName)));
    }
}
