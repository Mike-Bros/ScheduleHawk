package com.mikebros.schedulehawk.controller;

import com.mikebros.schedulehawk.DBConnection;
import com.mikebros.schedulehawk.ScheduleHawkApplication;
import com.mikebros.schedulehawk.models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;

public class ReportsController {

    private int nextGridRow;
    @FXML
    private GridPane mainGrid;
    @FXML
    private BarChart<String, Number> typeChart;
    @FXML
    private CategoryAxis typeAxisX;
    @FXML
    private NumberAxis typeAxisY;
    @FXML
    private BarChart<String, Number> monthChart;
    @FXML
    private CategoryAxis monthAxisX;
    @FXML
    private NumberAxis monthAxisY;
    @FXML
    private Button back_button;

    public void initialize() throws Exception {
        System.out.println("......................................................................................");
        System.out.println("Initializing Reports View");

        nextGridRow = 1;
        setTypeChartData();
        setMonthChartData();
        addTablesForEachContact();
        createLocationChart();

        System.out.println("Finished initializing Reports View");
        System.out.println("......................................................................................\n");
    }

    private void setTypeChartData() throws Exception {
        ObservableList<String> types = FXCollections.observableArrayList();
        String query = "SELECT * FROM appointments;";
        ResultSet appointments = DBConnection.query(query);
        while (appointments.next()) {
            types.add(appointments.getString("Type"));
        }

        XYChart.Series set = new XYChart.Series<>();
        for (String type : types) {
            set.getData().add(new XYChart.Data<>(type, getCountOfType(type)));
        }
        set.setName("Types");
        typeChart.getData().addAll(set);
    }

    private Number getCountOfType(String type) throws Exception {
        String query = "SELECT * FROM appointments WHERE Type = \"" + type + "\";";
        int count = 0;
        ResultSet appointments = DBConnection.query(query);
        while (appointments.next()) {
            count++;
        }
        return count;
    }

    private void setMonthChartData() throws Exception {
        monthAxisX.setCategories(FXCollections.<String>observableList(
                Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August",
                        "September", "October", "November", "December")));

        ObservableList<String> types = FXCollections.observableArrayList();


        XYChart.Series set = new XYChart.Series<>();
        set.getData().add(new XYChart.Data<>("January", getCountOfMonth(1)));
        set.getData().add(new XYChart.Data<>("February", getCountOfMonth(2)));
        set.getData().add(new XYChart.Data<>("March", getCountOfMonth(3)));
        set.getData().add(new XYChart.Data<>("April", getCountOfMonth(4)));
        set.getData().add(new XYChart.Data<>("May", getCountOfMonth(5)));
        set.getData().add(new XYChart.Data<>("June", getCountOfMonth(6)));
        set.getData().add(new XYChart.Data<>("July", getCountOfMonth(7)));
        set.getData().add(new XYChart.Data<>("August", getCountOfMonth(8)));
        set.getData().add(new XYChart.Data<>("September", getCountOfMonth(9)));
        set.getData().add(new XYChart.Data<>("October", getCountOfMonth(10)));
        set.getData().add(new XYChart.Data<>("November", getCountOfMonth(11)));
        set.getData().add(new XYChart.Data<>("December", getCountOfMonth(12)));

        set.setName("Months");

        monthChart.getData().addAll(set);
    }

    private Object getCountOfMonth(int month) throws Exception {
        int count = 0;
        String query = "SELECT * FROM appointments";
        ResultSet appointments = DBConnection.query(query);
        while (appointments.next()){
            String startDateTime = appointments.getString("Start");
            String startDate = startDateTime.split(" ")[0];
            int startMonth = Integer.parseInt(startDate.split("-")[1]);
            if (startMonth == month){
                count++;
                count++;
            }
        }
        return count;
    }

    private void addTablesForEachContact() throws Exception {
        String query = "SELECT * FROM contacts;";
        ResultSet contacts = DBConnection.query(query);
        while (contacts.next()){
            createTable(contacts.getString("Contact_Name"), contacts.getString("Contact_ID"));
        }
    }

    private void createTable(String contactName, String contactId) throws Exception {
        VBox vBox = new VBox();
        vBox.setMaxHeight(200);

        Label tableTitle = new Label("Schedule for " + contactName);
        tableTitle.setFont(new Font("Arial", 18));

        TableView<Appointment> tableView = new TableView();
        TableColumn<Appointment, String> appointmentId = new TableColumn<>("Appointment_ID");
        TableColumn<Appointment, String> title = new TableColumn<>("Title");
        TableColumn<Appointment, String> type = new TableColumn<>("Type");
        TableColumn<Appointment, String> description = new TableColumn<>("Description");
        TableColumn<Appointment, String> startDate = new TableColumn<>("Start Date");
        TableColumn<Appointment, String> startTime = new TableColumn<>("Start Time");
        TableColumn<Appointment, String> endDate = new TableColumn<>("End Date");
        TableColumn<Appointment, String> endTime = new TableColumn<>("End Time");
        TableColumn<Appointment, String> customerId = new TableColumn<>("Customer_ID");

        tableView.getColumns().add(appointmentId);
        tableView.getColumns().add(title);
        tableView.getColumns().add(type);
        tableView.getColumns().add(description);
        tableView.getColumns().add(startDate);
        tableView.getColumns().add(startTime);
        tableView.getColumns().add(endDate);
        tableView.getColumns().add(endTime);
        tableView.getColumns().add(customerId);

        appointmentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tableView.setItems(createAppointments(contactId));

        vBox.getChildren().add(tableTitle);
        vBox.getChildren().add(tableView);

        GridPane.setMargin(vBox, new Insets(20, 100, 20, 100));
        mainGrid.addRow(nextGridRow,vBox);
        nextGridRow++;
    }

    private ObservableList<Appointment> createAppointments(String customerId) throws Exception {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        String query = "SELECT * FROM appointments WHERE Customer_ID = " + customerId;
        ResultSet appointments = DBConnection.query(query);

        while (appointments.next()){
            Appointment appt = new Appointment();

            appt.set_id(appointments.getString("Appointment_ID"));
            appt.set_title(appointments.getString("Title"));
            appt.set_description(appointments.getString("Description"));
            appt.set_location(appointments.getString("Location"));
            appt.set_type(appointments.getString("Type"));
            appt.set_start(convertFromUTC(appointments.getString("Start")));
            appt.set_end(convertFromUTC(appointments.getString("End")));
            appt.set_customerID(appointments.getString("Customer_ID"));
            appointmentList.add(appt);
        }
        return appointmentList;
    }

    private void createLocationChart() throws Exception {
        CategoryAxis locationAxisX = new CategoryAxis();
        NumberAxis locationAxisY = new NumberAxis();
        BarChart<String, Number> locationChart = new BarChart<String, Number>(locationAxisX,locationAxisY);
        locationChart.setTitle("Number of Appointments by Location");

        ObservableList<String> locations = FXCollections.observableArrayList();
        String query = "SELECT * FROM appointments;";
        ResultSet appointments = DBConnection.query(query);
        while (appointments.next()) {
            locations.add(appointments.getString("Location"));
        }

        XYChart.Series set = new XYChart.Series<>();
        for (String loc : locations) {
            set.getData().add(new XYChart.Data<>(loc, getCountOfLocation(loc)));
        }
        set.setName("Locations");
        locationChart.getData().addAll(set);

        VBox vBox = new VBox(locationChart);
        mainGrid.addRow(nextGridRow,vBox);
        nextGridRow++;
    }

    private Number getCountOfLocation(String loc) throws Exception {
        String query = "SELECT * FROM appointments WHERE Location = \"" + loc + "\";";
        int count = 0;
        ResultSet appointments = DBConnection.query(query);
        while (appointments.next()) {
            count++;
        }
        return count;
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

    @FXML
    public void backButtonClicked(ActionEvent event) {
        System.out.println("Back button clicked");
        ScheduleHawkApplication.changeScene(event, "dashboard-view");
    }
}
