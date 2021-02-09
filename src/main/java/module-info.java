module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.jfoenix;
    requires javafx.web;
    requires org.jsoup;
    requires org.controlsfx.controls;
    requires static lombok;



    opens org.example.controllers to javafx.fxml;
    exports org.example;


}
