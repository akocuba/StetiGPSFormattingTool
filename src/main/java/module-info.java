module com.vulpes {
    requires javafx.controls;
    requires javafx.fxml;
    requires poi.ooxml;
    requires poi.ooxml.schemas;
    requires poi;


    opens com.vulpes.datamodel;
    opens com.vulpes to javafx.fxml;
    exports com.vulpes;
    exports com.vulpes.datamodel;

}