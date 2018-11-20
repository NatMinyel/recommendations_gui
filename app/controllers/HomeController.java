/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

import app.models.User;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeController implements Initializable {

    private boolean settings = false;
    @FXML
    private Label username_label, full_name_label;

    @FXML
    private VBox pnl_scroll;

    @FXML
    private JFXButton edit_button;

    @FXML
    private void handleButtonAction(MouseEvent event) {
        refreshNodes();
    }

    @FXML
    private void edit_profile(MouseEvent event) {
        if (settings) {
            edit_button.setText("Edit Profile");
            pnl_scroll.getChildren().clear();
            Node[] nodes = new Node[15];
            for (int i = 0; i < 10; i++) {
                try {
                    nodes[i] = FXMLLoader.load(getClass().getResource("../views/item.fxml"));
                    pnl_scroll.getChildren().add(nodes[i]);

                } catch (IOException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
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
        refreshNodes();
    }

    private void refreshNodes() {

        username_label.setText(User.username);
        full_name_label.setText(User.full_name);

//        pnl_scroll.getChildren().clear();
//
//        Node[] nodes = new Node[15];
//
//        for (int i = 0; i < 10; i++) {
//            try {
//                nodes[i] = FXMLLoader.load(getClass().getResource("../views/item.fxml"));
//                pnl_scroll.getChildren().add(nodes[i]);
//
//            } catch (IOException ex) {
//                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
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
}
