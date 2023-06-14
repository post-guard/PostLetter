module top.rrricardo.postletter {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jfxtras.styles.jmetro;
                            
    opens top.rrricardo.postletter to javafx.fxml;
    exports top.rrricardo.postletter;
    exports top.rrricardo.postletter.StageController;
    opens top.rrricardo.postletter.StageController to javafx.fxml;
}