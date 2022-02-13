package com.mikebros.schedulehawk.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private Label loginTitle;
    @FXML
    private Label locationLabel;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private Button loginButton;

    public LoginController() {
    }

    @FXML
    protected void onLoginButtonClick() {
    }
}