package com.mikebros.schedulehawk.controller;

import com.mikebros.schedulehawk.DBConnection;
import com.mikebros.schedulehawk.ScheduleHawkApplication;
import com.mikebros.schedulehawk.models.Appointment;
import com.mikebros.schedulehawk.models.Customer;
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
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> postalCode;
    @FXML
    private TableColumn<Customer, String> customerCreatedBy;
    @FXML
    private TableColumn<Customer, String> customerCreateDate;
    @FXML
    private TableColumn<Customer, String> customerDelete;
    @FXML
    private TableColumn<Customer, String> address;
    @FXML
    private Button createCustomer;
    @FXML
    private TableColumn<Customer, String> customerLastUpdatedBy;
    @FXML
    private TableColumn<Customer, String> customerLastUpdate;
    @FXML
    private TableColumn<Customer, String> customerEdit;
    @FXML
    private TableColumn<Customer, String> customerName;
    @FXML
    private TableColumn<Customer, String> phone;


    /**
     * Initialize the dashboard-view.fxml.
     *
     * @throws Exception the exception
     */
    public void initialize() throws Exception {
        System.out.println("......................................................................................");
        System.out.println("Initializing Dashboard");
        addApptRows(createAppointments(getAllAppointments()));
        addCustomerRows(createCustomers(getAllCustomers()));
        createAppointment.setUserData("new");
        createCustomer.setUserData("new");
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

    private ResultSet getAllCustomers() throws Exception {
        String query = "SELECT * FROM customers;";
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

    private ObservableList<Customer> createCustomers(ResultSet customers) throws SQLException {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        while (customers.next()){
            Customer cust = new Customer();
            Button editButton = new Button();
            editButton.setText("edit");
            editButton.setUserData(customers.getString("Customer_ID"));
            editButton.setOnAction(this::editCustomerButtonClicked);

            Button deleteButton = new Button();
            deleteButton.setText("delete");
            deleteButton.setUserData(customers.getString("Customer_ID"));
            deleteButton.setOnAction(this::deleteCustomerButtonClicked);

            cust.setId(customers.getString("Customer_ID"));
            cust.setName(customers.getString("Customer_Name"));
            cust.setAddress(customers.getString("Address"));
            cust.setPostalCode(customers.getString("Postal_Code"));
            cust.setPhone(customers.getString("Phone"));
            cust.setCreateDate(customers.getString("Create_Date"));
            cust.setCreatedBy(customers.getString("Created_By"));
            cust.setLastUpdate(customers.getString("Last_Update"));
            cust.setLastUpdatedBy(customers.getString("Last_Updated_By"));
            cust.setEditButton(editButton);
            cust.setDeleteButton(deleteButton);
            customerList.add(cust);
        }
        return customerList;
    }

    /**
     * Add appointment rows to dashboard.
     *
     * @param appointmentList the observable list of Appointment object(s)
     */
    private void addApptRows(ObservableList<Appointment> appointmentList){
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

    private void addCustomerRows(ObservableList<Customer> customerList){
        customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerCreateDate.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        customerCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        customerLastUpdate.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        customerLastUpdatedBy.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        customerDelete.setCellValueFactory(new PropertyValueFactory<>("editButton"));
        customerEdit.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));

        customerTable.setItems(customerList);
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

    private void editCustomerButtonClicked(ActionEvent event){
        Node node = (Node) event.getSource();
        System.out.println("Clicked on the edit button for customer: " + node.getUserData());
        System.out.println(event);
        //Change scene to the appointment-edit-view where Appointment_ID = buttonId
        ScheduleHawkApplication.changeScene(event,"customer-edit-view");
    }

    private void deleteCustomerButtonClicked(ActionEvent event){
        Node node = (Node) event.getSource();
        System.out.println("Clicked on the delete button for customer: " + node.getUserData());
        System.out.println(event);
        deleteCustomer((String) node.getUserData());
        ScheduleHawkApplication.changeScene(event,"dashboard-view");
    }

    private void deleteCustomer(String id){
        System.out.println("Deleting: " + id);
        String query = "DELETE FROM customers WHERE Customer_ID = " + id + ";";
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
