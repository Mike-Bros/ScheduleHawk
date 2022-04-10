package com.mikebros.schedulehawk.controller;

import com.mikebros.schedulehawk.DBConnection;
import com.mikebros.schedulehawk.ScheduleHawkApplication;
import com.mikebros.schedulehawk.models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Dashboard controller.
 */
public class DashboardController{
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
    @FXML
    private Button createAppointment;
    @FXML
    private Button logout;

    /**
     * Initialize the dashboard-view.fxml.
     *
     * @throws Exception the exception
     */
    public void initialize() throws Exception {
        System.out.println("......................................................................................");
        System.out.println("Initializing Dashboard");
        addRows(createAppointments(getAllAppointments()));
        createAppointment.setUserData("new");
        System.out.println("Finished initializing Dashboard");
        System.out.println("......................................................................................\n");
    }

    /**
     * Gets all appointments from the DB.
     *
     * @return result set of all appointments
     * @throws Exception the exception
     */
    private ResultSet getAllAppointments() throws Exception {
        String query = "SELECT * FROM appointments;";
        return DBConnection.query(query);
    }

    /**
     * Create observable list of Appointment objects with a given ResultSet.
     *
     * @param appointments ResultSet of appointments
     * @return the observable list of Appointment object(s)
     * @throws SQLException the sql exception
     */
    private ObservableList<Appointment> createAppointments(ResultSet appointments) throws SQLException {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        while (appointments.next()){
            Appointment appt = new Appointment();
            Button editButton = new Button();
            editButton.setText("edit");
            //editButton.setId(appointments.getString("Appointment_ID"));
            editButton.setUserData(appointments.getString("Appointment_ID"));
            editButton.setOnAction(this::editButtonClicked);

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
            appt.set_editButton(editButton);
            appointmentList.add(appt);
        }
        return appointmentList;
    }

    /**
     * Add appointment rows to dashboard.
     *
     * @param appointmentList the observable list of Appointment object(s)
     */
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
        edit.setCellValueFactory(new PropertyValueFactory<>("editButton"));
        appointmentTable.setItems(appointmentList);
    }

    @FXML
    private void editButtonClicked(ActionEvent event) {
        Node node = (Node) event.getSource();
        System.out.println("Clicked on the button for appointment: " + node.getUserData());
        System.out.println(event);
        //Change scene to the edit-view where Appointment_ID = buttonId
        ScheduleHawkApplication.changeScene(event,"edit-view");

    }

    @FXML
    private void createNewAppointmentButtonClicked(ActionEvent event){
        System.out.println("Clicked on the button to create a new appointment...");
        System.out.println(event);
        ScheduleHawkApplication.changeScene(event,"edit-view");
    }

    public void logoutButtonClicked(ActionEvent event) {
        System.out.println("Clicked on the logout button, returning to login view...");
        ScheduleHawkApplication.changeScene(event,"login-view");
    }
}
