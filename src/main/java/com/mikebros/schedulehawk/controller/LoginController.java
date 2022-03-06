package com.mikebros.schedulehawk.controller;

import com.mikebros.schedulehawk.DBConnection;
import com.mikebros.schedulehawk.ScheduleHawkApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.Properties;

public class LoginController {
    Properties props;
    String userLang;
    @FXML
    private Label loginTitle;
    @FXML
    private Label languageLabel;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private Button loginButton;
    @FXML
    private Label warningLabel;

    public void initialize() throws Exception {
        System.out.println("......................................................................................");
        System.out.println("Initializing LoginController");

        // Set all labels to system language
        System.out.println("Getting and setting default language...");
        getProperties();
        setDefaultText();
        System.out.println("Default language selected and labels set");

        System.out.println("Finished initializing LoginController");
        System.out.println("......................................................................................\n");
    }

    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws Exception {
        System.out.println("onLoginButtonClick, attempting to login with credentials...");
        System.out.println("Username: " + usernameInput.getText());
        System.out.println("Password: " + passwordInput.getText());

        if (validateLogin(usernameInput.getText(), passwordInput.getText())) {
            System.out.println("Login attempt successful");
            ScheduleHawkApplication.changeScene(event,"dashboard-view");
        } else {
            System.out.println("Login attempt failed");
            warningLabel.setText(props.getProperty(userLang + "warningLabel"));
        }
    }

    private static Boolean validateLogin(String user, String password) throws Exception {
        String query = "SELECT * FROM users WHERE ";
        query += "User_Name='" + user + "' AND ";
        query += "Password='" + password + "';";

        ResultSet rs = DBConnection.query(query);
        return rs.next();
    }

    private void getProperties() throws Exception {
        props = new Properties();
        InputStream is = ScheduleHawkApplication.class.getResourceAsStream("/com/mikebros/schedulehawk/language/login.properties");
        props.load(is);
        if (is != null){
            userLang = System.getProperty("user.language");
            userLang += ".";
            is.close();
        }else{
            throw new Exception("InputStream is null");
        }
    }

    private void setDefaultText(){
        loginTitle.setText(props.getProperty(userLang + "loginTitle"));
        languageLabel.setText(props.getProperty(userLang + "languageLabel"));
        loginButton.setText(props.getProperty(userLang + "loginButton"));
    }
}