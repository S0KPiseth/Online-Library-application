module ui.libraryui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens ui.libraryui to javafx.fxml;
    exports ui.libraryui;
}