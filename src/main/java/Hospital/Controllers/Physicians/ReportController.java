package Hospital.Controllers.Physicians;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ReportController implements Initializable {
    public AnchorPane panel;
    public TextArea resultText;
    public ImageView imageResultView;
    public TabPane tabpane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!Physician.resultText.isEmpty()) {
            resultText.setText(Physician.resultText.get("resultText"));
            Physician.resultText.clear();
        } else if (Physician.imageResult.get("resultImage") != null) {
            tabpane.getSelectionModel().select(1);
            try {
                System.out.println(Physician.imageResult.containsKey("resultImage"));
                BufferedImage imBuff = ImageIO.read(Physician.imageResult.get("resultImage"));
                System.out.println(imBuff.getWidth() + "x" + imBuff.getHeight());
                Image image = SwingFXUtils.toFXImage(imBuff, null);
                imageResultView.setImage(image);
                imBuff.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (Physician.imageResult.get("resultImage") != null) {
                    try {
                        Physician.imageResult.get("resultImage").close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
//    imageResult.clear();
        }
    }
}
