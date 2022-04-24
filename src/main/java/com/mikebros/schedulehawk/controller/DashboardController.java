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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The type Dashboard controller.
 */
public class DashboardController {
    @FXML
    private ComboBox<String> selectedView;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> contactID;
    @FXML
    private TableColumn<Appointment, String> apptEdit;
    @FXML
    private TableColumn<Appointment, String> apptDelete;
    @FXML
    private TableColumn<Appointment, String> appointmentID;
    @FXML
    private TableColumn<Appointment, String> startDate;
    @FXML
    private TableColumn<Appointment, String> startTime;
    @FXML
    private TableColumn<Appointment, String> endDate;
    @FXML
    private TableColumn<Appointment, String> endTime;
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
    private TableColumn<Appointment, String> apptCreateDate;
    @FXML
    private Button createAppointment;
    @FXML
    private Button logout;
    @FXML
    private Button reports;
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
    private TableColumn<Customer, String> division;
    @FXML
    private TableColumn<Customer, String> country;
    @FXML
    private Button createCustomer;
    @FXML
    private TableColumn<Customer, String> customerLastUpdatedBy;
    @FXML
    private TableColumn<Customer, String> customerLastUpdate;
    @FXML
    private TableColumn<Customer, String> customerEdit;
    @FXML
    private TableColumn<Appointment, String> customerCustomerID;
    @FXML
    private TableColumn<Customer, String> customerName;
    @FXML
    private TableColumn<Customer, String> phone;
    private ObservableList<Appointment> upcomingAppointments;


    /**
     * Initialize the dashboard-view.fxml.
     *
     * @throws Exception the exception
     */
    public void initialize() throws Exception {
        System.out.println("......................................................................................");
        System.out.println("Initializing Dashboard");
        setView();
        addApptRows(createAppointments(getAllAppointments()));
        addCustomerRows(createCustomers(getAllCustomers()));
        notifyUpcomingAppointment(createAppointments(getAllAppointments()));
        createAppointment.setUserData("new");
        createCustomer.setUserData("new");
        System.out.println("Finished initializing Dashboard");
        System.out.println("......................................................................................\n");
    }

    private void setView() {
        ObservableList<String> viewList = FXCollections.observableArrayList();
        viewList.add("Week");
        viewList.add("Month");
        selectedView.setItems(viewList);
        selectedView.setValue("Month");
    }

    @FXML
    private void updateApptTable(ActionEvent event) throws Exception {
        addApptRows(createAppointments(getAllAppointments()));
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

    private String getAppointmentType(String id) {
        try {
            String query = "SELECT * FROM appointments WHERE Appointment_ID = " + id;
            ResultSet apptResult = DBConnection.query(query);
            if (apptResult.next()) {
                return apptResult.getString("Type");
            } else {
                return "Unknown Type";
            }
        } catch (Exception e) {
            System.out.println("There was an issue retrieving appointment type from DB");
            System.out.println(e.getMessage());
        }
        return "Unknown Type";
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
    private ObservableList<Appointment> createAppointments(ResultSet appointments) throws Exception {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        while (appointments.next()) {
            if (Objects.equals(selectedView.getValue(), "Month")) {
                if (isThisMonth(appointments.getString("Start"))) {
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
                    appt.set_start(convertFromUTC(appointments.getString("Start")));
                    appt.set_end(convertFromUTC(appointments.getString("End")));
                    appt.set_createDate(convertFromUTC(appointments.getString("Create_Date")));
                    appt.set_createdBy(appointments.getString("Created_By"));
                    appt.set_lastUpdate(convertFromUTC(appointments.getString("Last_Update")));
                    appt.set_lastUpdatedBy(appointments.getString("Last_Updated_By"));
                    appt.set_customerID(appointments.getString("Customer_ID"));
                    appt.set_userID(appointments.getString("User_ID"));
                    appt.set_contactID(getContact(appointments.getString("Contact_ID")));
                    appt.set_editButton(editButton);
                    appt.set_deleteButton(deleteButton);
                    appointmentList.add(appt);
                }
            } else {
                if (isThisWeek(appointments.getString("Start"))) {
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
                    appt.set_start(convertFromUTC(appointments.getString("Start")));
                    appt.set_end(convertFromUTC(appointments.getString("End")));
                    appt.set_createDate(convertFromUTC(appointments.getString("Create_Date")));
                    appt.set_createdBy(appointments.getString("Created_By"));
                    appt.set_lastUpdate(convertFromUTC(appointments.getString("Last_Update")));
                    appt.set_lastUpdatedBy(appointments.getString("Last_Updated_By"));
                    appt.set_customerID(appointments.getString("Customer_ID"));
                    appt.set_userID(appointments.getString("User_ID"));
                    appt.set_contactID(getContact(appointments.getString("Contact_ID")));
                    appt.set_editButton(editButton);
                    appt.set_deleteButton(deleteButton);
                    appointmentList.add(appt);
                }
            }
        }
        return appointmentList;
    }

    private ObservableList<Customer> createCustomers(ResultSet customers) throws Exception {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        while (customers.next()) {
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
            cust.setDivisionID(getFirstLevelDivisionName(customers.getString("Division_ID")));
            cust.setCountry(getCountryNameByDivisionID(customers.getString("Division_ID")));
            cust.setPhone(customers.getString("Phone"));
            cust.setCreateDate(convertFromUTC(customers.getString("Create_Date")));
            cust.setCreatedBy(customers.getString("Created_By"));
            cust.setLastUpdate(convertFromUTC(customers.getString("Last_Update")));
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
    private void addApptRows(ObservableList<Appointment> appointmentList) {
        appointmentID.setCellValueFactory(new PropertyValueFactory<>("id"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
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

    private void addCustomerRows(ObservableList<Customer> customerList) {
        customerCustomerID.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        division.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        country.setCellValueFactory(new PropertyValueFactory<>("country"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerCreateDate.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        customerCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        customerLastUpdate.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        customerLastUpdatedBy.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        customerDelete.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));
        customerEdit.setCellValueFactory(new PropertyValueFactory<>("editButton"));

        customerTable.setItems(customerList);
    }

    private void notifyUpcomingAppointment(ObservableList<Appointment> appointments) {
        upcomingAppointments = FXCollections.observableArrayList();
        appointments.forEach((appt) -> {
            try {
                if (apptStartWithin(appt, 15)) {
                    System.out.println(appt.getId() + " is within 15 minutes of login");
                }
            } catch (ParseException e) {
                System.out.println("Error occurred while checking for upcoming appointment(s)");
            }
        });
        if (!upcomingAppointments.isEmpty()){
            AtomicReference<Alert> alert = new AtomicReference<>(new Alert(Alert.AlertType.INFORMATION));
            StringBuilder body = new StringBuilder("You have " + upcomingAppointments.size() + " upcoming appointments...");
            for (Appointment appt : upcomingAppointments) {
                body.append("\nAppointment ID: ").append(appt.getId());
                body.append(" , Start Date: ").append(appt.getStartDate());
                body.append(" , Start Time: ").append(appt.getStartTime());
            }
            alert.get().setContentText(body.toString());
            alert.get().setTitle("Upcoming Appointment(s)");
            alert.get().setHeaderText(null);
            alert.get().showAndWait();
        }
    }

    @FXML
    private void editApptButtonClicked(ActionEvent event) {
        Node node = (Node) event.getSource();
        System.out.println("Clicked on the edit button for appointment: " + node.getUserData());
        System.out.println(event);
        //Change scene to the appointment-edit-view where Appointment_ID = buttonId
        ScheduleHawkApplication.changeScene(event, "appointment-edit-view");
    }

    private void deleteApptButtonClicked(ActionEvent event) {
        Node node = (Node) event.getSource();
        System.out.println("Clicked on the delete button for appointment: " + node.getUserData());
        System.out.println(event);
        deleteAppointment((String) node.getUserData());
        ScheduleHawkApplication.changeScene(event, "dashboard-view");
    }

    private void deleteAppointment(String id) {
        AtomicReference<Alert> alert = new AtomicReference<>(new Alert(Alert.AlertType.WARNING));
        alert.get().setContentText("Are you sure you want to delete...\nAppointment ID: " + id + "\nAppointment Type: " + getAppointmentType(id));
        alert.get().setTitle("Delete Appointment");
        alert.get().setHeaderText(null);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.get().getDialogPane().getButtonTypes().add(cancelButtonType);
        alert.get().showAndWait().ifPresent(type -> {
            if (type == cancelButtonType) {
                System.out.println("User has canceled the deletion");
            } else {
                System.out.println("Deleting appointment: " + id);
                String query = "DELETE FROM appointments WHERE Appointment_ID = " + id + ";";
                try {
                    DBConnection.update(query);
                } catch (Exception e) {
                    System.out.println("Error occurred, unable to delete appointment");
                }

                alert.set(new Alert(Alert.AlertType.INFORMATION));
                alert.get().setContentText("You have deleted appointment: " + id);
                alert.get().setTitle("Deleted Appointment");
                alert.get().setHeaderText(null);
                alert.get().showAndWait();
            }
        });

    }

    private void editCustomerButtonClicked(ActionEvent event) {
        Node node = (Node) event.getSource();
        System.out.println("Clicked on the edit button for customer: " + node.getUserData());
        //Change scene to the appointment-edit-view where Appointment_ID = buttonId
        ScheduleHawkApplication.changeScene(event, "customer-edit-view");
    }

    private void deleteCustomerButtonClicked(ActionEvent event) {
        Node node = (Node) event.getSource();
        System.out.println("Clicked on the delete button for customer: " + node.getUserData());
        deleteCustomer((String) node.getUserData());
        ScheduleHawkApplication.changeScene(event, "dashboard-view");
    }

    private void deleteCustomer(String id) {
        AtomicReference<Alert> alert = new AtomicReference<>(new Alert(Alert.AlertType.WARNING));
        alert.get().setContentText("Are you sure you want to delete customer: " + id + "?" + "\nThis will delete all appointments for this customer.");
        alert.get().setTitle("Delete Customer");
        alert.get().setHeaderText(null);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.get().getDialogPane().getButtonTypes().add(cancelButtonType);
        alert.get().showAndWait().ifPresent(type -> {
            if (type == cancelButtonType) {
                System.out.println("User has canceled the deletion");
            } else {
                System.out.println("Finding and delete appointments for customer: " + id);
                String query = "SELECT * FROM appointments WHERE Customer_ID = " + id;
                try {
                    ResultSet appointments = DBConnection.query(query);
                    while (appointments.next()) {
                        deleteAppointment(appointments.getString("Appointment_ID"));
                    }
                } catch (Exception e) {
                    System.out.println("Error occurred, unable to delete appointment");
                }

                System.out.println("Deleting Customer: " + id);
                query = "DELETE FROM customers WHERE Customer_ID = " + id + ";";
                try {
                    DBConnection.update(query);
                } catch (Exception e) {
                    System.out.println("Error occurred, unable to delete appointment");
                }

                alert.set(new Alert(Alert.AlertType.INFORMATION));
                alert.get().setContentText("You have deleted customer: " + id);
                alert.get().setTitle("Deleted Customer");
                alert.get().setHeaderText(null);
                alert.get().showAndWait();
            }
        });

    }

    @FXML
    private void createNewAppointmentButtonClicked(ActionEvent event) {
        System.out.println("Clicked on the button to create a new appointment...");
        System.out.println(event);
        ScheduleHawkApplication.changeScene(event, "appointment-edit-view");
    }

    @FXML
    private void createNewCustomerButtonClicked(ActionEvent event) {
        System.out.println("Clicked on the button to create a new customer...");
        System.out.println(event);
        ScheduleHawkApplication.changeScene(event, "customer-edit-view");
    }

    public void logoutButtonClicked(ActionEvent event){
        System.out.println("Clicked on the logout button, returning to login view...");
        ScheduleHawkApplication.changeScene(event, "login-view");
    }

    public void reportsButtonClicked(ActionEvent event){
        System.out.println("Clicked on the reports button, going to reports view");
        ScheduleHawkApplication.changeScene(event, "reports-view");
    }

    private void goAhead() {
        System.out.println("Go ahead");
    }

    private boolean isThisMonth(String start) {
        String apptStart = convertFromUTC(start);
        String apptDate = apptStart.split(" ")[0];
        String apptYear = apptDate.split("-")[0];
        String apptMonth = apptDate.split("-")[1];

        int localYear = LocalDate.now().getYear();
        int localMonth = LocalDate.now().getMonthValue();

        return localMonth == Integer.parseInt(apptMonth) && localYear == Integer.parseInt(apptYear);
    }

    private boolean isThisWeek(String start) throws ParseException {
        if (isThisMonth(start)) {
            Calendar localCal = Calendar.getInstance();
            Calendar apptCal = Calendar.getInstance();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date localDate = formatter.parse(start);
            apptCal.setTime(localDate);

            return apptCal.get(Calendar.WEEK_OF_YEAR) == localCal.get(Calendar.WEEK_OF_YEAR);
        } else {
            return false;
        }
    }

    private boolean apptStartWithin(Appointment appt, int minutes) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);

        Date localDate = new Date(System.currentTimeMillis());
        Date apptDate = formatter.parse(appt.getStart());

        if(apptDate.getTime() - localDate.getTime() <= TimeUnit.MINUTES.toMillis(minutes)
        && apptDate.getTime() - localDate.getTime() > 0){
            upcomingAppointments.add(appt);
            return true;
        }else {
            return false;
        }
    }

    private String convertFromUTC(String dt) {
        LocalDate localDate = LocalDate.parse(dt.split(" ")[0]);
        LocalTime localTime = LocalTime.parse(dt.split(" ")[1]);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate, localTime, ZoneId.of("UTC"));

        dt = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toString();
        dt = dt.substring(0, 16); // removes zone info
        dt = dt.replace("T", " ");
        return dt;
    }

    private String getFirstLevelDivisionName(String divisionID) throws Exception {
        String query = "SELECT * FROM first_level_divisions WHERE Division_ID = " + divisionID;
        ResultSet firstLevelDivision = DBConnection.query(query);
        firstLevelDivision.next();
        return firstLevelDivision.getString("Division");
    }

    private String getCountryNameByDivisionID(String divisionID) throws Exception {
        String query = "SELECT * FROM first_level_divisions WHERE Division_ID = " + divisionID;
        ResultSet firstLevelDivision = DBConnection.query(query);
        firstLevelDivision.next();
        String countryID = firstLevelDivision.getString("COUNTRY_ID");

        query = "SELECT * FROM countries WHERE Country_ID = " + countryID;
        ResultSet country = DBConnection.query(query);
        country.next();
        return country.getString("Country");
    }

    private String getContact(String contactID) throws Exception {
        String query = "SELECT * FROM contacts WHERE Contact_ID = " + contactID;
        ResultSet contact = DBConnection.query(query);
        contact.next();
        return contact.getString("Contact_Name");
    }
}
