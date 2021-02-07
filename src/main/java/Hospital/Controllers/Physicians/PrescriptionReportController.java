package Hospital.Controllers.Physicians;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PrescriptionReportController implements Initializable {
    public AnchorPane panel;
    public Label title;
    @FXML
    private TextArea resultText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!Physician.resultText.isEmpty()) {
            title.setText("Prescription id " + Physician.resultText.get("prescriptionId") + " For " + Physician.resultText.get("prescriptionPatient"));
            resultText.setText(Physician.resultText.get("prescriptionText"));
            Physician.resultText.clear();
        }
    }
}
