package com.mikebros.schedulehawk.controller;

import com.mikebros.schedulehawk.DBConnection;
import com.mikebros.schedulehawk.ScheduleHawkApplication;
import com.mikebros.schedulehawk.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class EditCustomerController {

    private String userData;

    @FXML
    private Text err_message_text;
    @FXML
    private TextField address;
    @FXML
    private Label err_message_label;
    @FXML
    private Button back_button;
    @FXML
    private TextField created_by;
    @FXML
    private ComboBox<String> create_date_hour;
    @FXML
    private ComboBox<String> last_update_min;
    @FXML
    private TextField last_updated_by;
    @FXML
    private Button submit_button;
    @FXML
    private TextField phone;
    @FXML
    private ComboBox<String> last_update_hour;
    @FXML
    private TextField customer_name;
    @FXML
    private ComboBox<String> create_date_min;
    @FXML
    private TextField customer_id;
    @FXML
    private ComboBox<String> first_level_division;
    @FXML
    private ComboBox<String> country;
    @FXML
    private TextField postal_code;
    @FXML
    private DatePicker create_date_date;
    @FXML
    private DatePicker last_update_date;

    public void initialize() throws Exception {
        System.out.println("......................................................................................");
        System.out.println("Initializing Edit View");

        getUserData();
        setFormFields();

        System.out.println("Finished initializing Edit View");
        System.out.println("......................................................................................\n");
    }

    private void getUserData() {
        ActionEvent loadEvent = ScheduleHawkApplication.lastSceneChangeEvent;
        Node eventNode = (Node) loadEvent.getSource();
        userData = (String) eventNode.getUserData();
    }

    private void setFormFields() throws Exception {
        setComboBoxes();
        err_message_label.setText("");

        if (Objects.equals(userData, "new")) {
            setNewCustomerFields();
        } else {
            setEditCustomerFields(userData);
        }
    }

    private void setNewCustomerFields() {
        User activeUser = ScheduleHawkApplication.getActiveUser();
        customer_id.setText("new appt");
        submit_button.setText("Create");

        created_by.setText(activeUser.getName());
        last_updated_by.setText(activeUser.getName());
        create_date_date.setValue(LocalDate.now());
        last_update_date.setValue(LocalDate.now());

        System.out.println(LocalTime.now().toString());
        String[] arrStr = LocalTime.now().toString().split(":");
        String localHour = arrStr[0];
        String localMin = arrStr[1];
        create_date_hour.setValue(localHour);
        create_date_min.setValue(localMin);
        last_update_hour.setValue(localHour);
        last_update_min.setValue(localMin);
    }

    private void setEditCustomerFields(String userData) {
        submit_button.setText("Update");

    }

    private void setComboBoxes() throws Exception {
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 1; i <= 24; i++) {
            if (i < 10) {
                hours.add("0" + i);
            } else {
                hours.add(String.valueOf(i));
            }
        }
        create_date_hour.setItems(hours);
        create_date_hour.getSelectionModel().selectFirst();
        last_update_hour.setItems(hours);
        last_update_hour.getSelectionModel().selectFirst();

        ObservableList<String> minutes = FXCollections.observableArrayList();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minutes.add("0" + i);
            } else {
                minutes.add(String.valueOf(i));
            }
        }
        create_date_min.setItems(minutes);
        create_date_min.getSelectionModel().selectFirst();
        last_update_min.setItems(minutes);
        last_update_min.getSelectionModel().selectFirst();

        country.setItems(getCountriesFromDB());
    }

    private ObservableList<String> getCountriesFromDB() throws Exception {
        String query = "SELECT * FROM countries";
        ResultSet countries = DBConnection.query(query);
        ObservableList<String> countryList = FXCollections.observableArrayList();

        while (countries.next()) {
            Country country = new Country();
            country.setCountry(countries.getString("Country"));
            countryList.add(country.getCountry());
        }
        return countryList;
    }

    private String getCountryIDByName(String name) throws Exception {
        String query = "SELECT * FROM countries WHERE country = \"" + name + "\"";
        ResultSet countries = DBConnection.query(query);

        if (countries.next()) {
            return countries.getString("Country_ID");
        } else {
            return "null";
        }
    }

    private ObservableList<String> getFirstLevelDivisonsFromDB(String countryID) throws Exception {
        String query = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = " + countryID;
        ResultSet firstLevelDivisions = DBConnection.query(query);
        ObservableList<String> firstLevelDivisionList = FXCollections.observableArrayList();

        while (firstLevelDivisions.next()) {
            FirstLevelDivision firstLevelDivision = new FirstLevelDivision();
            firstLevelDivision.setDivision(firstLevelDivisions.getString("Division"));
            firstLevelDivisionList.add(firstLevelDivision.getDivision());
        }
        return firstLevelDivisionList;
    }

    public void backButtonClicked(ActionEvent event) {
        System.out.println("Back button clicked");
        ScheduleHawkApplication.changeScene(event, "dashboard-view");
    }

    public void onCountrySelect(ActionEvent event) throws Exception {
        System.out.println("Selected: " + country.getValue());
        String countryID = getCountryIDByName(country.getValue());
        first_level_division.setItems(getFirstLevelDivisonsFromDB(countryID));
    }

    public void submitButtonClicked(ActionEvent event) throws Exception {
        err_message_label.setText("");
        if (Objects.equals(customer_name.getText(), "")) {
            System.out.println("Customer name cannot be empty");
            err_message_label.setText("Customer name cannot be empty");
        } else if (Objects.equals(phone.getText(), "")) {
            System.out.println("Phone cannot be empty");
            err_message_label.setText("Phone cannot be empty");
        } else if (Objects.equals(address.getText(), "")) {
            System.out.println("Address cannot be empty");
            err_message_label.setText("Address cannot be empty");
        } else if (Objects.equals(postal_code.getText(), "")) {
            System.out.println("Postal Code cannot be empty");
            err_message_label.setText("Postal Code cannot be empty");
        } else if (country.getValue() == null) {
            System.out.println("Country cannot be empty");
            err_message_label.setText("Country cannot be empty");
        } else if (first_level_division.getValue() == null) {
            System.out.println("First-Level Division cannot be empty");
            err_message_label.setText("First-Level Division cannot be empty");
        } else {
            if (Objects.equals(userData, "new")) {
                System.out.println("Creating new customer");

                Customer customer = createCustomer();
                customer.create();
                //Appointment appt = createAppointment();
                //appt.create();

                ScheduleHawkApplication.changeScene(event, "dashboard-view");
            } else {
                System.out.println("Updating existing customer");

                //Appointment appt = createAppointment();
                //appt.update();

                ScheduleHawkApplication.changeScene(event, "dashboard-view");
            }
        }
    }

    private Customer createCustomer() throws Exception {
        Customer cust = new Customer();

        cust.setId(customer_id.getText());
        cust.setName(customer_name.getText());
        cust.setPhone(phone.getText());
        cust.setAddress(address.getText());
        cust.setPostalCode(postal_code.getText());
        cust.setDivisionID(getFirstLevelDivisionID(first_level_division.getValue()));

        cust.setCreateDate(getDateTimeString(create_date_date, create_date_hour, create_date_min));
        cust.setCreatedBy(created_by.getText());
        cust.setLastUpdate(getDateTimeString(last_update_date, last_update_hour, last_update_min));
        cust.setLastUpdatedBy(last_updated_by.getText());

        return cust;
    }

    private String getFirstLevelDivisionID(String division) throws Exception {
        String query = "SELECT * FROM first_level_divisions WHERE Division = \"" + division + "\"";
        ResultSet divisions = DBConnection.query(query);
        if (divisions.next()){
            return divisions.getString("Division_ID");
        }else{
            throw new Exception("No division found in the DB with the division name: " + division);
        }
    }

    private String getDateTimeString(DatePicker datePicker, ComboBox<String> start_hour, ComboBox<String> start_min) {
        String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = start_hour.getValue() + ":" + start_min.getValue() + ":00";
        return date + " " + time;
    }
}
