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
    private TableColumn<Appointment, String> apptEdit;
    @FXML
    private TableColumn<Appointment, String> apptDelete;
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
    private TableColumn<Appointment, String> apptCreatedBy;
    @FXML
    private TableColumn<Appointment, String> apptLastUpdate;
    @FXML
    private TableColumn<Appointment, String> apptLastUpdatedBy;
    @FXML
    private TableColumn<Appointment, String> customerID;
    @FXML
    private TableColumn<Appointment, String> end;
    @FXML
    private TableColumn<Appointment, String> apptCreateDate;
    @FXML
    private Button createAppointment;
    @FXML
    private Button logout;
    @FXML
    private TableView<?> customerTable;
    @FXML
    private TableColumn<?, ?> postalCode;
    @FXML
    private TableColumn<?, ?> customerCreatedBy;
    @FXML
    private TableColumn<?, ?> customerDelete;
    @FXML
    private TableColumn<?, ?> address;
    @FXML
    private Button createCustomer;
    @FXML
    private TableColumn<?, ?> customerLastUpdatedBy;
    @FXML
    private TableColumn<?, ?> customerEdit;
    @FXML
    private TableColumn<?, ?> customerName;
    @FXML
    private TableColumn<?, ?> phone;


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
            editButton.setUserData(appointments.getString("Appointment_ID"));
            editButton.setOnAction(this::editApptButtonClicked);

            Button deleteButton = new Button();
            deleteButton.setText("delete");
            deleteButton.setUserData(appointments.getString("Appointment_ID"));
            deleteButton.setOnAction(this::deleteApptButtonClicked);

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
            appt.set_deleteButton(deleteButton);
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
        apptCreateDate.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        apptCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        apptLastUpdate.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        apptLastUpdatedBy.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        apptEdit.setCellValueFactory(new PropertyValueFactory<>("editButton"));
        apptDelete.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));
        appointmentTable.setItems(appointmentList);
    }

    @FXML
    private void editApptButtonClicked(ActionEvent event) {
        Node node = (Node) event.getSource();
        System.out.println("Clicked on the edit button for appointment: " + node.getUserData());
        System.out.println(event);
        //Change scene to the appointment-edit-view where Appointment_ID = buttonId
        ScheduleHawkApplication.changeScene(event,"appointment-edit-view");

    }

    private void deleteApptButtonClicked(ActionEvent event){
        Node node = (Node) event.getSource();
        System.out.println("Clicked on the delete button for appointment: " + node.getUserData());
        System.out.println(event);
        deleteAppointment((String) node.getUserData());
        ScheduleHawkApplication.changeScene(event,"dashboard-view");
    }

    private void deleteAppointment(String id){
        System.out.println("Deleting: " + id);
        String query = "DELETE FROM appointments WHERE Appointment_ID = " + id + ";";
        try {
            DBConnection.update(query);
        }catch (Exception e){
            System.out.println("Error occurred, unable to delete appointment");
        }
    }

    @FXML
    private void createNewAppointmentButtonClicked(ActionEvent event){
        System.out.println("Clicked on the button to create a new appointment...");
        System.out.println(event);
        ScheduleHawkApplication.changeScene(event,"appointment-edit-view");
    }

    @FXML
    private void createNewCustomerButtonClicked(ActionEvent event){
        System.out.println("Clicked on the button to create a new customer...");
        System.out.println(event);
        ScheduleHawkApplication.changeScene(event,"customer-edit-view");
    }

    public void logoutButtonClicked(ActionEvent event) {
        System.out.println("Clicked on the logout button, returning to login view...");
        ScheduleHawkApplication.changeScene(event,"login-view");
    }
}
