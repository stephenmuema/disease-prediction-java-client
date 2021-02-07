package Hospital.Controllers.Receptionist;


import Hospital.Controllers.MasterClasses.RecordsMasterClass;
import Hospital.Controllers.Super;
import Hospital.Controllers.settings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import javafx.util.StringConverter;
import securityandtime.AesCipher;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Hospital.Controllers.settings.*;


public class Panel extends Super implements Initializable {
    private final ObservableList<RecordsMasterClass> data = FXCollections.observableArrayList();
    private final double tabWidth = 200.0;
    public Label clock;
    public AnchorPane panel;
    public TextField patientname;
    public TextField patientemail;
    public Button addpatient;
    public DatePicker dob;
    public Button logout;
    public Label datepicked;
    public TextField phonenumber;
    public RadioButton radiomale, radiofemale;
    public TextField findinrecords;
    public Button findinrecordsbutton;
    public TableView<RecordsMasterClass> patienttable;
    public TableColumn<RecordsMasterClass, String> colpatientname;
    public TableColumn<RecordsMasterClass, String> colpatientemail;
    public TableColumn<RecordsMasterClass, String> colpatientnumber;
    public TabPane tabpane;
    public Tab tabexisting;
    public Tab tabnew;
    public AnchorPane existingcontainer;
    public AnchorPane newcontainer;
    public WebView webview;
    public Label title;
    public Button bookAppointmentsButton;
    public Button bookVIPAppointments;
    ArrayList<TabPane> tabPaneArrayList = new ArrayList<>();
    private String date, radioval = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabPaneArrayList.add(tabpane);
        time(clock);
        buttonListeners();
        pickdate();
        radioListener();
        patienttable.setPlaceholder(new Label(""));
        configureView(tabPaneArrayList);
        WebEngine engine = webview.getEngine();//help web page
        engine.load(siteHelp);
        title.setText(appName + " Reception");
    }


    private void configureTab(Tab tab, String title, String iconPath) {
        double imageWidth = 40.0;

        ImageView imageView = new ImageView(new Image(iconPath));
        imageView.setFitHeight(imageWidth);
        imageView.setFitWidth(imageWidth);

        Label label = new Label(title);
        label.setMaxWidth(tabWidth - 20);
        label.setPadding(new Insets(5, 0, 0, 0));
        label.setStyle("-fx-text-fill: black; -fx-font-size: 8pt; -fx-font-weight: normal;");
        label.setTextAlignment(TextAlignment.CENTER);

        BorderPane tabPane = new BorderPane();
//        tabPane.setRotate(90.0);

        tabPane.setMaxWidth(tabWidth);
        tabPane.setCenter(imageView);
        tabPane.setBottom(label);

        tab.setText("");
        tab.setGraphic(tabPane);
    }


    //snm2@gmail.com
    private void buttonListeners() {
        bookVIPAppointments.setOnMouseClicked(event -> {
            bookVIPAppointment();
        });
        bookAppointmentsButton.setOnMouseClicked(event -> {
            ObservableList<TablePosition> getCells = patienttable.getSelectionModel().getSelectedCells();
            if (getCells.size() == 0) {
//    execute code for null selection
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "SELECT A RECORD FOR THIS OPERATION TO TAKE PLACE");
            } else {
                bookAppointments();
            }
        });
        logout.setOnMouseClicked(event -> logOut(panel));
        addpatient.setOnMouseClicked(event -> validation());
        findinrecordsbutton.setOnMouseClicked(event -> {
            if (data.size() > 0) {
                data.clear();
            }
            findInRecordsMethod(panel, data, findinrecords, patienttable, colpatientname, colpatientemail, colpatientnumber);
        });
    }

    private void bookVIPAppointment() {
//option to select doctor name
    }

    private void bookAppointments() {
        RecordsMasterClass recordsMasterClass = new RecordsMasterClass();
        try {
            tableRowIdSelected(recordsMasterClass, patienttable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void tableRowIdSelected(RecordsMasterClass recordsMasterClass, TableView<RecordsMasterClass> patienttable) throws SQLException {
        recordsMasterClass = patienttable.getSelectionModel().getSelectedItem();
        String id = recordsMasterClass.getId();
        String docSelection = "SELECT * FROM users WHERE userclearancelevel=? and status=?";
        PreparedStatement selectDoctors = hospitalDbConnection.prepareStatement(docSelection);
        selectDoctors.setString(1, AesCipher.encrypt(encryptionkey, initVector, "DOCTOR"));
        selectDoctors.setString(2, "active");
        ResultSet resultSet = selectDoctors.executeQuery();
        String checkAppointment = "SELECT * FROM appointments WHERE PatientId=? ";
        PreparedStatement prep = hospitalDbConnection.prepareStatement(checkAppointment);
        prep.setString(1, id);
        if (prep.executeQuery().isBeforeFirst()) {
            showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "RECORD EXISTS IN DATABASE", "THE USER ALREADY HAS AN APPOINTMENT");
        } else {
            if (resultSet.isBeforeFirst()) {
                ArrayList<Integer> count = new ArrayList<>();
                ArrayList<Integer> docid = new ArrayList<>();

                while (resultSet.next()) {
                    count.add(Integer.valueOf(resultSet.getString("numberoofappointments")));
                    docid.add(Integer.valueOf(resultSet.getString("id")));

                }
                int min = Integer.MAX_VALUE;
                for (Integer integer : count) {
                    if (integer < min) {
                        min = integer;
                    }
                }


                String docSelect = "SELECT * FROM users WHERE numberoofappointments=? AND userclearancelevel=?";
                PreparedStatement p1 = hospitalDbConnection.prepareStatement(docSelect);
                p1.setInt(1, min);
                p1.setString(2, AesCipher.encrypt(encryptionkey, initVector, "DOCTOR"));
                ResultSet r1 = p1.executeQuery();
                if (r1.next()) {
                    //continue from here
                    String idDoc = r1.getString("id");

                    String[] recs = {"PatientId", "doctorId"};
                    String[] values = {id, idDoc};
                    insertIntoTable("appointments", recs, values);
                    String docUpdate = "UPDATE users SET numberoofappointments=? WHERE id=?";
                    PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement(docUpdate);
                    preparedStatement.setInt(1, min + 1);
                    preparedStatement.setString(2, idDoc);
                    int x = preparedStatement.executeUpdate();
                    if (x > 0) {
                        showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "OPERATION SUCCESSFULL");
                    } else {
                        showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "OPERATION FAILED");
                    }
                }


            } else {
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "No doctors", "No doctors are available");
            }
        }

    }


    private void radioListener() {
        ToggleGroup toggleGroup = new ToggleGroup();
        radiofemale.setToggleGroup(toggleGroup);
        radiomale.setToggleGroup(toggleGroup);
        toggleGroup.selectedToggleProperty().addListener((ov, t, t1) -> {

            RadioButton chk = (RadioButton) t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button
            radioval = chk.getText().toLowerCase();

        });
    }

    private void validation() {
        if (radioval == null || patientname.getText().isEmpty() || patientemail.getText().isEmpty() || phonenumber.getText().isEmpty() || dob.getValue() == null) {
            showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(),
                    "FILL ALL FIELDS", "PLEASE FILL ALL FIELDS");

        } else {

            String regex = "^(.+)@(.+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(patientemail.getText());
            if (matcher.matches()) {
//                proceed to add patient
                insert();
            } else {
                showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "INVALID EMAIL", "ENTER A VALID EMAIL ADDRESS");
            }
        }
    }

    private void insert() {
        String name, email, number, dateofbirth, radioSelected;
        name = AesCipher.encrypt(encryptionkey, initVector, patientname.getText());
        email = AesCipher.encrypt(encryptionkey, initVector, patientemail.getText());
        number = AesCipher.encrypt(encryptionkey, initVector, phonenumber.getText());
        dateofbirth = date;
        radioSelected = radioval;
        try {
            PreparedStatement search = hospitalDbConnection.prepareStatement("SELECT * FROM patients where email=?");
            search.setString(1, email);
            ResultSet rs = search.executeQuery();
            if (rs.isBeforeFirst()) {
                showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "ERROR", "THE USER IS REGISTERED");

            } else {
                try {
                    PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement("INSERT INTO patients (name, email, phonenumber, birthdate, sex, branch) VALUES (?,?,?,?,?,?)");
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(3, number);
                    preparedStatement.setString(4, dateofbirth);
                    preparedStatement.setString(5, radioSelected);
                    preparedStatement.setString(6, AesCipher.encrypt(encryptionkey, initVector, settings.hospital.get("hospital_name")));
                    if (preparedStatement.executeUpdate() != 0) {
                        clearFields();
                        showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "THE PATIENT HAS BEEN ADDED SUCCESSFULLY");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void pickdate() {

        String pattern = "dd-MM-yyyy";
        dob.setPromptText(pattern.toLowerCase());

        dob.setConverter(new StringConverter<LocalDate>() {
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        // Must call super
                        super.updateItem(item, empty);

                        // Show Weekends in blue color
                        DayOfWeek day = DayOfWeek.from(item);
                        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                            this.setTextFill(Color.BLUE);
                        }

                        // Disable all future date cells
                        if (item.isAfter(LocalDate.now())) {
                            this.setDisable(true);
                        }
                    }
                };
            }
        };

        dob.setDayCellFactory(dayCellFactory);
        EventHandler<ActionEvent> event = e -> {
            // get the date picker value

            LocalDate i = dob.getValue();
            if (i.isAfter(LocalDate.now())) {
                showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "ERROR", "TIME TRAVEL IS NOT YET A THING ");
//                dob.setValue(null);
            } else {
                // get the selected date
                datepicked.setText(String.valueOf(i));
                date = String.valueOf(i);
            }

        };
        dob.setOnAction(event);
    }

    private void clearFields() {
        patientemail.clear();
        phonenumber.clear();
        patientname.clear();

    }
}
