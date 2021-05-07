package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    public void hitEnter(ActionEvent event){
        executeLogin();
    }

    @FXML
    public void hitPlayGameBtn(ActionEvent event){
        executeLogin();
    }

    public void executeLogin(){

    }




}
