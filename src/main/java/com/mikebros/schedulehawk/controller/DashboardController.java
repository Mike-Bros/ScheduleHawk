package com.mikebros.schedulehawk.controller;

import com.mikebros.schedulehawk.DBConnection;
import com.mikebros.schedulehawk.models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardController {
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> contactID;
    @FXML
    private TableColumn<Appointment, String> edit;
    @FXML
    private TableColumn<Appointment, String> start;
    @FXML
    private TableColumn<Appointment, String> title;
    @FXML
    private TableColumn<Appointment, String> description;
    @FXML
    private TableColumn<Appointment, String> type;
    @FXML
    private TableColumn<Appointment, String> location_col;
    @FXML
    private TableColumn<Appointment, String> userID;
    @FXML
    private TableColumn<Appointment, String> createdBy;
    @FXML
    private TableColumn<Appointment, String> lastUpdate;
    @FXML
    private TableColumn<Appointment, String> lastUpdatedBy;
    @FXML
    private TableColumn<Appointment, String> customerID;
    @FXML
    private TableColumn<Appointment, String> end;
    @FXML
    private TableColumn<Appointment, String> createDate;

    public void initialize() throws Exception {
        System.out.println("......................................................................................");
        System.out.println("Initializing Dashboard");

        addRows(createAppointments(getAllAppointments()));

        System.out.println("Finished initializing Dashboard");
        System.out.println("......................................................................................\n");
    }


    private ResultSet getAllAppointments() throws Exception {
        String query = "SELECT * FROM appointments;";
        return DBConnection.query(query);
    }

    private ObservableList<Appointment> createAppointments(ResultSet appointments) throws SQLException {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        while (appointments.next()){
            Appointment appt = new Appointment();

            appt.set_id(appointments.getString("Appointment_ID"));
            appt.set_title(appointments.getString("Title"));
            appt.set_description(appointments.getString("Description"));
            appt.set_location(appointments.getString("Location"));
            appt.set_type(appointments.getString("Type"));
            appt.set_start(appointments.getString("Start"));
            appt.set_end(appointments.getString("End"));
            appt.set_createDate(appointments.getString("Create_Date"));
            appt.set_createdBy(appointments.getString("Created_By"));
            appt.set_lastUpdate(appointments.getString("Last_Update"));
            appt.set_lastUpdatedBy(appointments.getString("Last_Updated_By"));
            appt.set_customerID(appointments.getString("Customer_ID"));
            appt.set_userID(appointments.getString("User_ID"));
            appt.set_contactID(appointments.getString("Contact_ID"));
            appointmentList.add(appt);
        }
        return appointmentList;
    }

    private void addRows(ObservableList<Appointment> appointmentList){
        start.setCellValueFactory(new PropertyValueFactory<>("start"));
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        location_col.setCellValueFactory(new PropertyValueFactory<>("location"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        contactID.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        userID.setCellValueFactory(new PropertyValueFactory<>("userId"));
        createDate.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createdBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdate.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        lastUpdatedBy.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        appointmentTable.setItems(appointmentList);
    }
}
