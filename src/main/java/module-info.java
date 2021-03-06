module com.mikebros.schedulehawk {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.mikebros.schedulehawk to javafx.fxml;
    exports com.mikebros.schedulehawk;
    exports com.mikebros.schedulehawk.controller;
    exports com.mikebros.schedulehawk.models;
    opens com.mikebros.schedulehawk.controller to javafx.fxml;
}