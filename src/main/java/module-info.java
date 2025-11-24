module bibika.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens JFX.kt2 to javafx.fxml;
    exports JFX.kt2;
}