package com.mikebros.schedulehawk.controller;

import com.mikebros.schedulehawk.DBConnection;
import com.mikebros.schedulehawk.ScheduleHawkApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class ReportsController {


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

        setTypeChartData();
        setMonthChartData();

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
        ;
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

    @FXML
    public void backButtonClicked(ActionEvent event) {
        System.out.println("Back button clicked");
        ScheduleHawkApplication.changeScene(event, "dashboard-view");
    }
}
