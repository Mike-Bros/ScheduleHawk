package com.mikebros.schedulehawk.controller;

import com.mikebros.schedulehawk.DBConnection;
import com.mikebros.schedulehawk.ScheduleHawkApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.ResultSet;

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
    @FXML
    private Label warningLabel;

    public LoginController() {
        System.out.println("LoginController");
    }

    @FXML
    protected void onLoginButtonClick() throws Exception {
        System.out.println("onLoginButtonClick, attempting to login with credentials...");
        System.out.println("Username: " + usernameInput.getText());
        System.out.println("Password: " + passwordInput.getText());

        if (validateLogin(usernameInput.getText(), passwordInput.getText())) {
            System.out.println("Login attempt successful");
            ScheduleHawkApplication.changeScene("dashboard-view");
        } else {
            System.out.println("Login attempt failed");
            warningLabel.setText("Login failed, Please try again");
        }
    }

    private static Boolean validateLogin(String user, String password) throws Exception {
        String query = "SELECT * FROM users WHERE ";
        query += "User_Name='" + user + "' AND ";
        query += "Password='" + password + "';";

        ResultSet rs = DBConnection.query(query);
        return rs.next();
    }
}