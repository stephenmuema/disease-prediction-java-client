package Hospital.Controllers.Physicians;


import Hospital.Controllers.MasterClasses.*;
import Hospital.Controllers.Super;
import Hospital.Controllers.settings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.StringConverter;
import securityandtime.AesCipher;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static Hospital.Controllers.settings.*;


public class PanelController extends Super implements Initializable, Physician {
    public TabPane tabContainer;
    public Tab searchPatientTab;
    public Tab addpatienthistoryTab;
    public AnchorPane addPatientHistoryContainer;
    public Tab labtestsTab;
    public AnchorPane labtestscontainer;
    public AnchorPane panel;
    public Label clock;
    public Label title;
    public Button logout;
    //logout
    public TextField findinrecords;
    public Button findinrecordsbutton;
    public TableView<RecordsMasterClass> patienttable;
    public TableColumn<RecordsMasterClass, String> colpatientname;
    public TableColumn<RecordsMasterClass, String> colpatientemail;
    public TableColumn<RecordsMasterClass, String> colpatientnumber;
    public TabPane tabcontainerhistorypane;//history tab
    public TabPane tabcontainerclinicpane;//clinic tab
    //code for condition panel variable initialisation
    public Tab addconditionssubtab;
    public TextField conditionAddField;
    public TextField conditionAddCategoryField;
    public TextArea conditionAddDescription;
    public Button conditionAddButton;//Add condition button
    public DatePicker conditionAddDateDiagnosed;
    //code for history tab
    public Tab viewHistoryTab;
    public TableView<HistoryMasterClass> tablehistory;
    public TableColumn<HistoryMasterClass, String> tablehistoryId;
    public TableColumn<HistoryMasterClass, String> tablehistoryDate;
    public TableColumn<HistoryMasterClass, String> tablehistoryDoctor;
    public TableColumn<HistoryMasterClass, String> tablehistoryPrescription;
    //    public TableColumn<HistoryMasterClass, String> tablehistoryTests;
//    public TableColumn<HistoryMasterClass, String> tablehistoryRatings;
    public Button tablehistoryViewPrescriptionsButton;
    public Button tablehistoryViewLabTestButton;
    public Button tablehistoryViewDiagnosisButton;
    public Button tablehistoryGetReportButton;
    //existing conditions code
    public Tab existingConditionsTab;
    public TableView<ConditionsMasterClass> existingConditionsTabTable;
    public TableColumn<ConditionsMasterClass, String> existingConditionsTabTableId;
    public TableColumn<ConditionsMasterClass, String> existingConditionsTabTableName;
    public TableColumn<ConditionsMasterClass, String> existingConditionsTabTableDateAdded;
    public TableColumn<ConditionsMasterClass, String> existingConditionsTabTableCategory;
    public TableColumn<ConditionsMasterClass, String> existingConditionsTabTableDoctor;
    public Button existingConditionsTabTableViewDetailsButton;
    //    clinic Appointments
    public Tab tabClinicAppointments;
    public TableView<AppointmentMasterClass> tabClinicAppointmentsTable;
    public TableColumn<AppointmentMasterClass, String> tabClinicAppointmentsTableId;
    //    public TableColumn <AppointmentMasterClass,String> tabClinicAppointmentsTableVisitorName;
    public TableColumn<AppointmentMasterClass, String> tabClinicAppointmentsTableTypeOfVisit;
    public TableColumn<AppointmentMasterClass, String> tabClinicAppointmentsTableTimeOfAppointment;
    public Button tabClinicAppointmentsTableCallInButton;
    //    clinic lab teststext
    public Tab tabClinicLabTests;
    public TableView<LabTestsMasterClass> tabClinicLabTestsTable;
    public TableColumn<LabTestsMasterClass, String> tabClinicLabTestsTableId;
    public TableColumn<LabTestsMasterClass, String> tabClinicLabTestsTableTestType;
    public TableColumn<LabTestsMasterClass, String> tabClinicLabTestsTableTechnician;
    public TableColumn<LabTestsMasterClass, String> tabClinicLabTestsTablePatientName;
    public TableColumn<LabTestsMasterClass, String> tabClinicLabTestsTableStatus;

    public Button tabClinicLabTestsTableGetFullReportButton;
    public Button tabClinicLabTestsTablemoveReportToDiagnosis;
    //    clinic diagnosis
    public Tab tabClinicDiagnosis;
    public TextArea tabClinicDiagnosisInput;
    public Button tabClinicDiagnosisSubmit;
    public Tab tabClinicPrescription;
    public Button tabClinicPrescriptionSubmit;
    public TextArea tabClinicPrescriptionText;
    public Button endPatientSession;
    public TabPane appointmentssearch;
    //SESSIONS TAB
    public TableView<AppointmentMasterClass> tabClinicSessionsTable;
    public TableColumn<AppointmentMasterClass, String> tabClinicSessionsTableId;
    public TableColumn<AppointmentMasterClass, String> tabClinicSessionsTablePatientEmail;
    public TableColumn<AppointmentMasterClass, String> tabClinicSessionsTableName;
    public Button tabClinicSessionsTableResumeInButton;
    public Tab sessionsTab;
    public Button startSessionButton;
    public TabPane labteststabpane;
    public TextArea testsInputPhysician;
    public Button testsSendToLab;
    public ImageView resultPreview;
    private ObservableList<RecordsMasterClass> recordsMasterClassObservableList = FXCollections.observableArrayList();
    private ObservableList<HistoryMasterClass> historyMasterClassObservableList = FXCollections.observableArrayList();
    private ObservableList<ConditionsMasterClass> conditionsMasterClassObservableList = FXCollections.observableArrayList();
    private ObservableList<AppointmentMasterClass> appointmentMasterClassObservableList = FXCollections.observableArrayList();
    private ObservableList<AppointmentMasterClass> appointmentMasterClassObservableList2 = FXCollections.observableArrayList();
    private ObservableList<LabTestsMasterClass> labTestsMasterClassObservableList = FXCollections.observableArrayList();

    private ArrayList<TabPane> tabPaneArrayList = new ArrayList<>();
    private String date;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
//        loadSessions();
//        viewPatientDetails();
        loadTables();
        tabContainer.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            if ((nv.textProperty().getValue().equals("PATIENT RECORDS") && currentSession.isEmpty()) || nv.textProperty().getValue().equals("CLINIC PANEL") && currentSession.isEmpty()) {
                tabContainer.getSelectionModel().select(ov);
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "CREATE SESSION FIRST");
            } else {
                reloadTables();
            }
        });
        viewHistoryTab.setOnSelectionChanged(event -> {
            if (viewHistoryTab.isSelected()) {
                reloadTables();
            }
        });
//         uncomment to make the tabpanes bigger
//        tabPaneArrayList.add(tabContainer);
//        tabPaneArrayList.add(tabcontainerclinicpane);
//        tabPaneArrayList.add(tabcontainerhistorypane);
//        tabPaneArrayList.add(appointmentssearch);

        tabcontainerhistorypane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> reloadTables());
//        labteststabpane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
//            @Override
//            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
//                reloadTables();
//            }
//        });
        tabContainer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> reloadTables());
        tabcontainerclinicpane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> reloadTables());
        appointmentssearch.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> reloadTables());
        tabClinicDiagnosis.setOnSelectionChanged(event -> {
            tabClinicDiagnosisInput.clear();
            resultPreview.setImage(null);
        });
        configureView(tabPaneArrayList);
        time(clock);
        title.setText(appName + " Clinic Panel");
        buttonListeners();
        date = datepicker(conditionAddDateDiagnosed);
//        viewPatientAppointments();
        if (tabClinicAppointments.isSelected()) {
            viewPatientAppointments();
        }
        if (sessionsTab.isSelected()) {
            loadSessions();
        }

    }

    private void loadTables() {
        reloadTables();
    }


    private void createSession(String id, String email) {
        if (currentSession.isEmpty()) {
            currentSession.put("currentSession", email);

        } else {
            currentSession.replace("currentSession", email);
        }
        viewPatientDetails();

        try {
            PreparedStatement main = hospitalDbConnection.prepareStatement("SELECT * FROM patients WHERE id=?");
            main.setString(1, id);
            ResultSet rsmain = main.executeQuery();
            //check if in sessions table
            PreparedStatement preparedStatement = sessionsDbConnection.prepareStatement("SELECT * FROM SessionPatients WHERE email=? and staffid =?");
            preparedStatement.setString(1, AesCipher.encrypt(encryptionkey, initVector, email));
            preparedStatement.setInt(2, Integer.parseInt(settings.id.get("userid")));
            ResultSet check = preparedStatement.executeQuery();
            if (check.isBeforeFirst()) {
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "PATIENT ALREADY IN SESSION", "THE PATIENT HAS AN EXISTING SESSION");
            } else {
                PreparedStatement statement = sessionsDbConnection.prepareStatement("INSERT INTO SessionPatients(name, email, sessionId,staffid) VALUES (?,?,?,?)");
                if (rsmain.isBeforeFirst()) {
                    while (rsmain.next()) {
                        statement.setString(1, rsmain.getString("name"));
                        statement.setString(2, AesCipher.encrypt(encryptionkey, initVector, currentSession.get("currentSession")));
                        statement.setString(3, dateTimeMethod());
                        statement.setInt(4, Integer.parseInt(settings.id.get("userid")));
                        if (statement.executeUpdate() > 0) {
                            showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), currentSession.get("currentSession") + " session", "SESSION CREATED SUCCESSFULLY");

                        } else {
                            showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), currentSession.get("currentSession") + " session", "SESSION CREATION FAILED");

                        }
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, conditionAddButton.getScene().getWindow(), "ERROR", "UNEXPECTED ERROR");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        appointmentMasterClassObservableList2.clear();

        loadSessions();
    }

    private void loadSessions() {
        try {
            Statement preparedStatement = sessionsDbConnection.createStatement();
            ResultSet resultSet = preparedStatement.executeQuery("SELECT * FROM SessionPatients where staffid =" + Integer.parseInt(settings.id.get("userid")));
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    AppointmentMasterClass appointmentMasterClass = new AppointmentMasterClass();
                    appointmentMasterClass.setSize(appointmentMasterClass.getSize() + 1);
                    appointmentMasterClass.setId(resultSet.getString("sessionId"));
                    appointmentMasterClass.setName(AesCipher.decrypt(encryptionkey, resultSet.getString("name")));
                    appointmentMasterClass.setPatientEmail(AesCipher.decrypt(encryptionkey, resultSet.getString("email")));
                    appointmentMasterClassObservableList2.add(appointmentMasterClass);
                }
                tabClinicSessionsTable.setItems(appointmentMasterClassObservableList2);
                tabClinicSessionsTableId.setCellValueFactory(new PropertyValueFactory<>("id"));
                tabClinicSessionsTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
                tabClinicSessionsTablePatientEmail.setCellValueFactory(new PropertyValueFactory<>("patientEmail"));
                tabClinicSessionsTable.refresh();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void buttonListeners() {

        tabClinicPrescriptionSubmit.setOnAction(event -> Patientprescription());

//        diagnosis submitted
        tabClinicDiagnosisSubmit.setOnAction(event -> {
            String disease = tabClinicDiagnosisInput.getText().toUpperCase();
            try {
                String results = "";
                String id = "";
                String sql = "SELECT * FROM labtests WHERE patientname=? AND done=?";
                PreparedStatement ps = hospitalDbConnection.prepareStatement(sql);
                ps.setString(1, AesCipher.encrypt(encryptionkey, initVector, currentSession.get("currentSession")));
                ps.setBoolean(2, false);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    results = rs.getString("results");
                    id = rs.getString("id");
                }

//                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO prescriptions(patientemail, diagnosis, doctor, tests) VALUES (?,?,?,?)");
//                preparedStatement.setString(1, currentSession.get("currentSession"));
//                preparedStatement.setString(2, disease);
//                preparedStatement.setString(3, settings.user.get("user"));
//                preparedStatement.setString(4, results);
                String gender = null;
                PreparedStatement searchPatient = hospitalDbConnection.prepareStatement("SELECT sex FROM patients WHERE email = ?");
                searchPatient.setString(1, AesCipher.encrypt(encryptionkey, initVector, currentSession.get("currentSession")));
                ResultSet searchPatientSex = searchPatient.executeQuery();
                while (searchPatientSex.next()) {
                    gender = searchPatientSex.getString("sex");
                }
                String location = user.get("location");

                String tablename = "prescriptions";
                String[] colRecs = {"patientemail", "diagnosis", "doctor", "tests", "gender", "location"};
                String[] values = {
                        AesCipher.encrypt(encryptionkey, initVector, currentSession.get("currentSession")),
                        disease,
                        AesCipher.encrypt(encryptionkey, initVector, settings.user.get("user")),
                        results,
                        gender,
                        location
                };

                try {
                    if (insertIntoTable(tablename, colRecs, values) > 0) {
                        PreparedStatement doneTests = hospitalDbConnection.prepareStatement("UPDATE labtests SET done=? WHERE id=?");
                        doneTests.setBoolean(1, true);
                        doneTests.setString(2, id);
                        if (!(doneTests.executeUpdate() > 0)) {
                            showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "ERROR", "NETWORK ERROR.cHECK CABLE CONNECTION");
                        }
                        tabClinicDiagnosisInput.clear();
                        showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "THE OPERATION WAS SUCCESSFULL");
                        tabcontainerclinicpane.getSelectionModel().select(2);

                    } else {
                        showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "THE OPERATION WAS NOT SUCCESSFULL");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

//                int rowsUpdated = preparedStatement.executeUpdate();
//                if (rowsUpdated > 0) {
//                    PreparedStatement doneTests = connection.prepareStatement("UPDATE labtests SET done=? WHERE id=?");
//                    doneTests.setBoolean(1, true);
//                    doneTests.setString(2, id);
//                    if (doneTests.executeUpdate() > 0) {
//                        //success!!
//                    } else {
//                        showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "ERROR", "NETWORK ERROR.cHECK CABLE CONNECTION");
//                    }
//                    tabClinicDiagnosisInput.clear();
//                    showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "THE OPERATION WAS SUCCESSFULL");
//                    tabcontainerclinicpane.getSelectionModel().select(2);
//
//                } else
//                    showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "THE OPERATION WAS NOT SUCCESSFULL");


            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement("select * from disease_database where disease=?");
                preparedStatement.setString(1, disease);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (!resultSet.isBeforeFirst()) {
                    PreparedStatement preparedStatement1 = hospitalDbConnection.prepareStatement("insert into disease_database(disease) values (?)");
                    preparedStatement1.setString(1, disease);
                    int rows = preparedStatement1.executeUpdate();
                    if (rows > 0) {

                    } else {
                        System.out.println("FAILURE");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        });

        tablehistoryGetReportButton.setOnAction(event -> {
//            todo    view history instance report

        });
        tabClinicLabTestsTableGetFullReportButton.setOnAction(event -> getTestReport());
        tabClinicLabTestsTablemoveReportToDiagnosis.setOnAction(event -> {
            if (setLabResultsScenevariables()) {
                currentSession.put("currentSession", temporarySession.get("temporarySession"));
                tabContainer.getSelectionModel().select(2);
                tabcontainerclinicpane.getSelectionModel().select(1);
                viewDiagnosis();
            }
        });
        testsSendToLab.setOnAction(event -> {
            sendTest();
        });
        tabClinicSessionsTableResumeInButton.setOnAction(event -> resumeSession(tabClinicSessionsTable));
        startSessionButton.setOnAction(event -> {
            RecordsMasterClass recordsMasterClass = patienttable.getSelectionModel().getSelectedItem();
            String id = recordsMasterClass.getId();
            createSession(id, recordsMasterClass.getEmail());
        });
        tabClinicAppointmentsTableCallInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AppointmentMasterClass appointmentMasterClass = tabClinicAppointmentsTable.getSelectionModel().getSelectedItem();
                callIn(appointmentMasterClass);
                viewPatientAppointments();
            }

            private void callIn(AppointmentMasterClass appointmentMasterClass) {
                String patientId = appointmentMasterClass.getPatientId();
                ResultSet resultSet = null;
                String query = "SELECT * FROM patients WHERE id =?";
                try {
                    PreparedStatement patientSelection = hospitalDbConnection.prepareStatement(query);
                    patientSelection.setString(1, patientId);
                    resultSet = patientSelection.executeQuery();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    PreparedStatement statement = sessionsDbConnection.prepareStatement("INSERT INTO SessionPatients(name, email, sessionId,staffid) VALUES (?,?,?,?)");
                    if (resultSet != null && resultSet.isBeforeFirst()) {
                        while (resultSet.next()) {
                            statement.setString(1, resultSet.getString("name"));
                            statement.setString(2, resultSet.getString("email"));
                        }
                    }
                    statement.setString(3, dateTimeMethod());
                    statement.setInt(4, Integer.parseInt(settings.id.get("userid")));


                    if (statement.executeUpdate() > 0) {
//                        remove from appointments table
                        String query1 = "DELETE FROM appointments WHERE PatientId=?";
                        PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement(query1);
                        preparedStatement.setString(1, patientId);
                        int rows = preparedStatement.executeUpdate();
                        if (rows == 1) {
                            showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "OPERATION SUCCESSFUL");
                            tabClinicAppointmentsTable.refresh();
                        } else {
                            showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "ERROR", "THE OPERATION WAS UNSUCCESSFULL");


                        }
                    } else {
                        showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "ERROR", "THE OPERATION WAS UNSUCCESSFULL");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }


        });
        logout.setOnMouseClicked(event -> logOut(panel));
        findinrecordsbutton.setOnMouseClicked(event -> {
            if (recordsMasterClassObservableList.size() > 0) {
                recordsMasterClassObservableList.clear();
            }
            findInRecordsMethod(panel, recordsMasterClassObservableList, findinrecords, patienttable, colpatientname, colpatientemail, colpatientnumber);
        });
        endPatientSession.setOnAction(event -> {
            //end patient session
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want end the patient session?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    try {
                        PreparedStatement preparedStatement = sessionsDbConnection.prepareStatement("DELETE FROM SessionPatients WHERE email=?");
                        preparedStatement.setString(1, AesCipher.encrypt(encryptionkey, initVector, currentSession.get("currentSession")));
                        if (preparedStatement.executeUpdate() > 0) {
                            PreparedStatement prepUpd = hospitalDbConnection.prepareStatement("UPDATE prescriptions SET completed=? WHERE completed=? AND patientemail=?");
                            prepUpd.setBoolean(1, true);
                            prepUpd.setBoolean(2, false);
                            prepUpd.setString(3, AesCipher.encrypt(encryptionkey, initVector, currentSession.get("currentSession")));
                            if (prepUpd.executeUpdate() > 0) {
                                currentSession.clear();
                            } else {
                                showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "ERROR", "ERROR PERFORMING OPERATION");
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                // ... user chose CANCEL or closed the dialog

            }

        });
        conditionAddButton.setOnAction(event -> {
            //add condition button
            if (currentSession.get("currentSession") == null) {
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "CREATE SESSION FIRST");
            } else {
                addPatientDetails();
            }
        });
        tablehistoryViewPrescriptionsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (setPrescriptionResultsScenevariables()) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("HOSPITAL/views/Physicians/prescription.fxml"));
                    try {
                        Parent parent = fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(parent));
                        stage.initStyle(StageStyle.UTILITY);
                        stage.setOnCloseRequest(event -> reloadTables());
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            private boolean setPrescriptionResultsScenevariables() {
                HistoryMasterClass historyMasterClass = tablehistory.getSelectionModel().getSelectedItem();
                boolean returnValue = false;


                if (tablehistory.getSelectionModel().getSelectedCells().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "SELECT ONE ROW TO VIEW THE REPORT");
                } else {
                    returnValue = true;


                    if (resultText.isEmpty()) {
                        resultText.put("prescriptionText", historyMasterClass.getPrescription());
                        resultText.put("prescriptionId", historyMasterClass.getId());
                        resultText.put("prescriptionPatient", historyMasterClass.getPatientEmail());

                    } else {
                        resultText.replace("prescriptionText", historyMasterClass.getPrescription());
                        resultText.replace("prescriptionId", historyMasterClass.getId());
                        resultText.replace("prescriptionPatient", historyMasterClass.getPatientEmail());
                    }


                }
                return returnValue;
            }
        });
    }

    private File createImage(String text) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Arial", Font.PLAIN, 17);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(java.awt.Color.BLACK);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();
        File tempFile = null;
        try {
            tempFile = File.createTempFile("preview", ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (tempFile != null) {
            tempFile.deleteOnExit();
        }
        try {
            if (tempFile != null) {
                ImageIO.write(img, "png", tempFile);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return tempFile;
    }

    private void viewDiagnosis() {
        if (!Physician.resultText.isEmpty()) {
//            resultPreview.setImage(new Image(createImage(resultText.get("resultText")).toURI().toString()));
            tabClinicDiagnosisInput.setText(resultText.get("resultText"));
            Physician.resultText.clear();
        } else if (imageResult.get("resultImage") != null) {
            try {
                System.out.println(imageResult.containsKey("resultImage"));
                BufferedImage imBuff = ImageIO.read(imageResult.get("resultImage"));
                System.out.println(imBuff.getWidth() + "x" + imBuff.getHeight());
                Image image = SwingFXUtils.toFXImage(imBuff, null);
                resultPreview.setImage(image);
                imBuff.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (imageResult.get("resultImage") != null) {
                    try {
                        imageResult.get("resultImage").close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
//    imageResult.clear();
        }
    }

    //SENDING TESTS TO LABTESTS TABLE is complete
    private void sendTest() {
        String tests = testsInputPhysician.getText();
        if (tests.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "NULL LAB TESTS", "LAB TESTS MUST BE SUBMITTED");
        } else {
            //send teststext to lab table
            //first select technician with least teststext and who is active and select them as the person in charge of the test
            String query = "SELECT * FROM USERS WHERE userclearancelevel=? and status=? ORDER BY numberoofappointments DESC ";
            try {
                PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement(query);
                preparedStatement.setString(1, AesCipher.encrypt(encryptionkey, initVector, "lab tech".toUpperCase()));
                preparedStatement.setString(2, "active");
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.isBeforeFirst()) {
                    ArrayList<Integer> count = new ArrayList<>();
                    ArrayList<Integer> labtechid = new ArrayList<>();

                    while (resultSet.next()) {
                        count.add(Integer.valueOf(resultSet.getString("numberoofappointments")));
                        labtechid.add(Integer.valueOf(resultSet.getString("id")));

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
                    p1.setString(2, AesCipher.encrypt(encryptionkey, initVector, "LAB TECH"));
                    ResultSet r1 = p1.executeQuery();
                    if (r1.next()) {
                        String email = r1.getString("email");

                        String[] recs = {"tests", "technician", "patientname", "doctorname"};
//                        System.out.println(currentSession.get("currentSession") + "patient\n" + AesCipher.decrypt(encryptionkey, email) + ": lab tech");
                        String[] values = {tests, email, AesCipher.encrypt(encryptionkey, initVector, currentSession.get("currentSession")), AesCipher.encrypt(encryptionkey, initVector, settings.user.get("user"))};
                        insertIntoTable("labtests", recs, values);
                        String docUpdate = "UPDATE users SET numberoofappointments=? WHERE email=?";
                        PreparedStatement preparedStatement1 = hospitalDbConnection.prepareStatement(docUpdate);
                        preparedStatement1.setInt(1, min + 1);
                        preparedStatement1.setString(2, email);
                        int x = preparedStatement1.executeUpdate();
                        if (x > 0) {
                            testsInputPhysician.clear();
                            showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "OPERATION SUCCESSFULL");
                        } else {
                            showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "OPERATION FAILED");
                        }
                    }
                } else {
                    showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), null, "SUCH AN ACCOUNT TYPE DOES NOT EXIST");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //completed method
    private void resumeSession(TableView<AppointmentMasterClass> tabClinicSessionsTable) {
        AppointmentMasterClass appointmentMasterClass = tabClinicSessionsTable.getSelectionModel().getSelectedItem();

        if (currentSession.isEmpty()) {
            currentSession.put("currentSession", appointmentMasterClass.getPatientEmail());
            showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SESSION RESUMED", "SESSION FOR " + currentSession.get("currentSession") + " HAS BEEN RESUMED");
        } else if (currentSession.get("currentSession").equals(appointmentMasterClass.getPatientEmail())) {
            showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SESSION IS ACTIVE", "SESSION FOR " + currentSession.get("currentSession") + " IS ALREADY ACTIVE");

        } else {
            currentSession.replace("currentSession", appointmentMasterClass.getPatientEmail());
            showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SESSION RESUMED", "SESSION FOR " + currentSession.get("currentSession") + " HAS BEEN RESUMED");

        }
        viewPatientDetails();

    }


    @Override
    protected void findInRecordsMethod(AnchorPane panel, ObservableList<RecordsMasterClass> data, TextField findinrecords, TableView<RecordsMasterClass> patienttable, TableColumn<RecordsMasterClass, String> colpatientname, TableColumn<RecordsMasterClass, String> colpatientemail, TableColumn<RecordsMasterClass, String> colpatientnumber) {
        super.findInRecordsMethod(panel, data, findinrecords, patienttable, colpatientname, colpatientemail, colpatientnumber);
    }

    @Override//completed method
    public void addPatientDetails() {
        String condition = conditionAddField.getText();
        String category = conditionAddCategoryField.getText();
        String description = conditionAddDescription.getText();
        if (condition.isEmpty() || category.isEmpty() || description.isEmpty() || date.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "FILL ALL INPUTS", "ALL INPUT FIELDS HAVE TO BE FILLED");
        } else {

            String tablename = "conditions";
            String[] colRecs = {"conditionName", "date", "category", "description", "patientemail", "doctorMail"};
            String[] values = {condition, date, category, description, AesCipher.encrypt(encryptionkey, initVector, currentSession.get("currentSession")), AesCipher.encrypt(encryptionkey, initVector, settings.user.get("user"))};

            try {
                if (insertIntoTable(tablename, colRecs, values) > 0) {
                    showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "OPERATION WAS SUCCESSFULL");
                    conditionAddField.clear();
                    conditionAddCategoryField.clear();
                    conditionAddDescription.clear();
                    String pattern = "dd-MM-yyyy";
                    conditionAddDateDiagnosed.setPromptText(pattern.toUpperCase());
                } else {
                    showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "OPERATION FAILED");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void viewPatientHistory() {

        if (!currentSession.isEmpty()) {
            try {
                PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement("SELECT * FROM prescriptions WHERE completed=? AND patientemail=?");
                preparedStatement.setBoolean(1, true);
                preparedStatement.setString(2, AesCipher.encrypt(encryptionkey, initVector, currentSession.get("currentSession")));
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.isBeforeFirst()) {
                    while (resultSet.next()) {
                        HistoryMasterClass historyMasterClass = new HistoryMasterClass();
                        historyMasterClass.setId(resultSet.getString("id"));
                        historyMasterClass.setDate(resultSet.getString("dateprescribed"));
                        historyMasterClass.setDoctor(AesCipher.decrypt(encryptionkey, resultSet.getString("doctor")));
                        historyMasterClass.setPatientEmail(AesCipher.decrypt(encryptionkey, resultSet.getString("patientemail")));
                        historyMasterClass.setPrescription(resultSet.getString("prescription"));
                        historyMasterClass.setTests(resultSet.getString("tests"));
                        historyMasterClassObservableList.add(historyMasterClass);
                    }
                    tablehistory.setItems(historyMasterClassObservableList);

                    tablehistoryId.setCellValueFactory(new PropertyValueFactory<HistoryMasterClass, String>("id"));
                    tablehistoryDate.setCellValueFactory(new PropertyValueFactory<HistoryMasterClass, String>("date"));
                    tablehistoryDoctor.setCellValueFactory(new PropertyValueFactory<HistoryMasterClass, String>("doctor"));
                    tablehistoryPrescription.setCellValueFactory(new PropertyValueFactory<HistoryMasterClass, String>("prescription"));
//                tablehistoryTests.setCellValueFactory(new PropertyValueFactory<HistoryMasterClass, String>("tests"));
//                public TableColumn <HistoryMasterClass,String>tablehistoryRatings;
                    tablehistory.refresh();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void viewPatientLabTests() {
//select teststext where doctor is the current logged in user
        String selectTechnicianTests = "SELECT * FROM labtests WHERE doctorname=? and done=?";
        PreparedStatement select;
        try {
            select = hospitalDbConnection.prepareStatement(selectTechnicianTests);
            select.setString(1, AesCipher.encrypt(encryptionkey, initVector, settings.user.get("user")));
            select.setBoolean(2, false);
            ResultSet selectedResults = select.executeQuery();
            if (selectedResults.isBeforeFirst()) {
                InputStream is = null;
                while (selectedResults.next()) {
                    LabTestsMasterClass labTestsMasterClass = new LabTestsMasterClass();
                    labTestsMasterClass.setId(selectedResults.getString("id"));
                    labTestsMasterClass.setDocName(AesCipher.decrypt(encryptionkey, selectedResults.getString("doctorname")));
                    labTestsMasterClass.setPatientName(AesCipher.decrypt(encryptionkey, selectedResults.getString("patientname")));
                    labTestsMasterClass.setTests(selectedResults.getString("tests"));
                    labTestsMasterClass.setStatus(selectedResults.getString("status"));
                    labTestsMasterClass.setResults(selectedResults.getString("results"));
                    is = selectedResults.getBinaryStream("imageResult");
                    labTestsMasterClass.setResultImage(is);
                    labTestsMasterClassObservableList.add(labTestsMasterClass);
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tabClinicLabTestsTable.setItems(labTestsMasterClassObservableList);
                tabClinicLabTestsTableId.setCellValueFactory(new PropertyValueFactory<LabTestsMasterClass, String>("id"));
                tabClinicLabTestsTableTechnician.setCellValueFactory(new PropertyValueFactory<LabTestsMasterClass, String>("docName"));
                tabClinicLabTestsTablePatientName.setCellValueFactory(new PropertyValueFactory<LabTestsMasterClass, String>("patientName"));
                tabClinicLabTestsTableTestType.setCellValueFactory(new PropertyValueFactory<>("tests"));
                tabClinicLabTestsTableStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
                tabClinicLabTestsTable.refresh();
//                labTestsMasterClassObservableList.clear();
            } else {
//                showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "FREEDOM", "THERE ARE NO TESTS TO BE DONE BY YOU");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override//conmpleted method
    public void viewPatientDetails() {
        String query = "SELECT * FROM conditions WHERE patientemail=?";
        try {
            PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement(query);
            if (currentSession.get("currentSession") != null) {
                preparedStatement.setString(1, AesCipher.encrypt(encryptionkey, initVector, currentSession.get("currentSession")));
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.isBeforeFirst()) {
                    while (resultSet.next()) {
                        ConditionsMasterClass conditionsMasterClass = new ConditionsMasterClass();
                        conditionsMasterClass.setSize(conditionsMasterClass.getSize() + 1);
                        conditionsMasterClass.setPatientId(resultSet.getString("id"));
                        conditionsMasterClass.setPatientemail(AesCipher.decrypt(encryptionkey, resultSet.getString("patientemail")));
                        conditionsMasterClass.setConditionName(resultSet.getString("conditionName"));
                        conditionsMasterClass.setDate(resultSet.getString("date"));
                        conditionsMasterClass.setCategory(resultSet.getString("category"));
                        conditionsMasterClass.setDoctor(AesCipher.decrypt(encryptionkey, resultSet.getString("doctorMail")));
                        conditionsMasterClassObservableList.add(conditionsMasterClass);
                    }
                    existingConditionsTabTable.setItems(conditionsMasterClassObservableList);
                    existingConditionsTabTableId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
                    existingConditionsTabTableName.setCellValueFactory(new PropertyValueFactory<>("conditionName"));
                    existingConditionsTabTableDateAdded.setCellValueFactory(new PropertyValueFactory<>("date"));
                    existingConditionsTabTableCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
                    existingConditionsTabTableDoctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
                    existingConditionsTabTable.refresh();
                } else {
                    System.out.println("Error");
                }
            }
        } catch (SQLException e) {
//            System.out.println("Table haiwork");
            e.printStackTrace();
        }

    }

    @Override//completed method
    public void viewPatientAppointments() {

        appointmentMasterClassObservableList.clear();
        String query = "SELECT * FROM appointments WHERE doctorId=?";
        try {
            PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement(query);
            preparedStatement.setString(1, settings.id.get("userid"));
            ResultSet foundrecords = preparedStatement.executeQuery();
            if (foundrecords.isBeforeFirst()) {

                while (foundrecords.next()) {
                    AppointmentMasterClass appointmentMasterClass = new AppointmentMasterClass();
                    appointmentMasterClass.setSize(appointmentMasterClass.getSize() + 1);
                    appointmentMasterClass.setId(foundrecords.getString("id"));
                    appointmentMasterClass.setDoctorId(foundrecords.getString("doctorId"));
                    appointmentMasterClass.setPatientId(foundrecords.getString("PatientId"));
                    appointmentMasterClass.setTime(foundrecords.getString("time"));
                    appointmentMasterClass.setType(foundrecords.getString("type"));

                    appointmentMasterClassObservableList.add(appointmentMasterClass);
                }
                tabClinicAppointmentsTable.setItems(appointmentMasterClassObservableList);
                tabClinicAppointmentsTableTimeOfAppointment.setCellValueFactory(new PropertyValueFactory<>("time"));
                tabClinicAppointmentsTableId.setCellValueFactory(new PropertyValueFactory<>("id"));
                tabClinicAppointmentsTableTypeOfVisit.setCellValueFactory(new PropertyValueFactory<>("type"));

                tabClinicAppointmentsTable.refresh();
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    @Override
    public void Patientdiagnosis() {

    }

    private void getTestReport() {
        //show report from lab
        if (setLabResultsScenevariables()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("HOSPITAL/views/Physicians/report.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(parent));
                stage.initStyle(StageStyle.UTILITY);
                stage.setOnCloseRequest(event -> reloadTables());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean setLabResultsScenevariables() {
        LabTestsMasterClass labTestsMasterClass = tabClinicLabTestsTable.getSelectionModel().getSelectedItem();
        boolean returnValue = false;


        if (tabClinicLabTestsTable.getSelectionModel().getSelectedCells().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "SELECT ONE ROW TO VIEW THE REPORT");
        } else if (!labTestsMasterClass.getStatus().equalsIgnoreCase("COMPLETE")) {
            showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "TESTS ARE NOT DONE YET");

        } else {
            returnValue = true;
            if (temporarySession.isEmpty()) {
                temporarySession.put("temporarySession", labTestsMasterClass.getPatientName());
            } else {
                temporarySession.replace("temporarySession", labTestsMasterClass.getPatientName());
            }
            if (labTestsMasterClass.getResultImage() != null) {
                if (imageResult.isEmpty()) {
                    imageResult.put("resultImage", labTestsMasterClass.getResultImage());
                } else {
                    imageResult.replace("resultImage", labTestsMasterClass.getResultImage());

                }
                try {
                    labTestsMasterClass.getResultImage().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (resultText.isEmpty()) {
                    resultText.put("resultText", labTestsMasterClass.getResults());

                } else {
                    resultText.replace("resultText", labTestsMasterClass.getResults());

                }
            }
            System.out.println(imageResult.containsKey("resultImage"));

        }
        return returnValue;
    }

    @Override
    public void Patientprescription() {
        String prescribe = tabClinicPrescriptionText.getText();
        if (prescribe.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to submit an empty prescription?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    prescribe("NO PRESCRIPTION AVAILABLE");
                }
                // ... user chose CANCEL or closed the dialog

            }
        } else {
            prescribe(prescribe);
        }
    }

    private void prescribe(String prescription) {
        try {
            PreparedStatement preparedStatementCheck = hospitalDbConnection.prepareStatement("SELECT * FROM prescriptions WHERE completed=? AND patientemail=?");
            preparedStatementCheck.setBoolean(1, false);
            preparedStatementCheck.setString(2, AesCipher.encrypt(encryptionkey, initVector, currentSession.get("currentSession")));
            ResultSet resultSetCheck = preparedStatementCheck.executeQuery();
            if (resultSetCheck.isBeforeFirst()) {
                //the diagnosis exists
                try {
                    PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement("UPDATE prescriptions SET prescription=?  WHERE patientemail=? AND completed=?");
                    preparedStatement.setString(1, prescription);
                    preparedStatement.setString(2, AesCipher.encrypt(encryptionkey, initVector, currentSession.get("currentSession")));
                    preparedStatement.setBoolean(3, false);
                    if (preparedStatement.executeUpdate() > 0) {
                        showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "OPERATION SUCCESSFULL");
                    } else {
                        showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "OPERATION WAS NOT COMPLETED SUCCESSFULLY");

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "ERROR", "YOU HAVE NOT PROVIDED A DIAGNOSIS FOR THIS PATIENT");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private String datepicker(DatePicker dob) {
        String pattern = "dd-MM-yyyy";
        dob.setPromptText(pattern.toUpperCase());

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
                this.date = String.valueOf(i);
            }

        };
        dob.setOnAction(event);
        return this.date;
    }

    /**
     * reloads all tables
     */
    private void reloadTables() {
        recordsMasterClassObservableList.clear();
        conditionsMasterClassObservableList.clear();
        appointmentMasterClassObservableList.clear();
        appointmentMasterClassObservableList2.clear();
        labTestsMasterClassObservableList.clear();
        historyMasterClassObservableList.clear();
        loadSessions();
        viewPatientAppointments();
        viewPatientDetails();
        viewPatientHistory();
        viewPatientLabTests();
    }


//getters and setters for the class


    public TabPane getTabContainer() {
        return tabContainer;
    }

    public PanelController setTabContainer(TabPane tabContainer) {
        this.tabContainer = tabContainer;
        return this;
    }

    public Tab getSearchPatientTab() {
        return searchPatientTab;
    }

    public PanelController setSearchPatientTab(Tab searchPatientTab) {
        this.searchPatientTab = searchPatientTab;
        return this;
    }

    public Tab getAddpatienthistoryTab() {
        return addpatienthistoryTab;
    }

    public PanelController setAddpatienthistoryTab(Tab addpatienthistoryTab) {
        this.addpatienthistoryTab = addpatienthistoryTab;
        return this;
    }

    public AnchorPane getAddPatientHistoryContainer() {
        return addPatientHistoryContainer;
    }

    public PanelController setAddPatientHistoryContainer(AnchorPane addPatientHistoryContainer) {
        this.addPatientHistoryContainer = addPatientHistoryContainer;
        return this;
    }

    public Tab getLabtestsTab() {
        return labtestsTab;
    }

    public PanelController setLabtestsTab(Tab labtestsTab) {
        this.labtestsTab = labtestsTab;
        return this;
    }

    public AnchorPane getLabtestscontainer() {
        return labtestscontainer;
    }

    public PanelController setLabtestscontainer(AnchorPane labtestscontainer) {
        this.labtestscontainer = labtestscontainer;
        return this;
    }

    public AnchorPane getPanel() {
        return panel;
    }

    public PanelController setPanel(AnchorPane panel) {
        this.panel = panel;
        return this;
    }

    public Label getClock() {
        return clock;
    }

    public PanelController setClock(Label clock) {
        this.clock = clock;
        return this;
    }

    public Label getTitle() {
        return title;
    }

    public PanelController setTitle(Label title) {
        this.title = title;
        return this;
    }

    public Button getLogout() {
        return logout;
    }

    public PanelController setLogout(Button logout) {
        this.logout = logout;
        return this;
    }

    public TextField getFindinrecords() {
        return findinrecords;
    }

    public PanelController setFindinrecords(TextField findinrecords) {
        this.findinrecords = findinrecords;
        return this;
    }

    public Button getFindinrecordsbutton() {
        return findinrecordsbutton;
    }

    public PanelController setFindinrecordsbutton(Button findinrecordsbutton) {
        this.findinrecordsbutton = findinrecordsbutton;
        return this;
    }

    public TableView<RecordsMasterClass> getPatienttable() {
        return patienttable;
    }

    public PanelController setPatienttable(TableView<RecordsMasterClass> patienttable) {
        this.patienttable = patienttable;
        return this;
    }

    public TableColumn<RecordsMasterClass, String> getColpatientname() {
        return colpatientname;
    }

    public PanelController setColpatientname(TableColumn<RecordsMasterClass, String> colpatientname) {
        this.colpatientname = colpatientname;
        return this;
    }

    public TableColumn<RecordsMasterClass, String> getColpatientemail() {
        return colpatientemail;
    }

    public PanelController setColpatientemail(TableColumn<RecordsMasterClass, String> colpatientemail) {
        this.colpatientemail = colpatientemail;
        return this;
    }

    public TableColumn<RecordsMasterClass, String> getColpatientnumber() {
        return colpatientnumber;
    }

    public PanelController setColpatientnumber(TableColumn<RecordsMasterClass, String> colpatientnumber) {
        this.colpatientnumber = colpatientnumber;
        return this;
    }

    public TabPane getTabcontainerhistorypane() {
        return tabcontainerhistorypane;
    }

    public PanelController setTabcontainerhistorypane(TabPane tabcontainerhistorypane) {
        this.tabcontainerhistorypane = tabcontainerhistorypane;
        return this;
    }

    public TabPane getTabcontainerclinicpane() {
        return tabcontainerclinicpane;
    }

    public PanelController setTabcontainerclinicpane(TabPane tabcontainerclinicpane) {
        this.tabcontainerclinicpane = tabcontainerclinicpane;
        return this;
    }

    public Tab getAddconditionssubtab() {
        return addconditionssubtab;
    }

    public PanelController setAddconditionssubtab(Tab addconditionssubtab) {
        this.addconditionssubtab = addconditionssubtab;
        return this;
    }

    public TextField getConditionAddField() {
        return conditionAddField;
    }

    public PanelController setConditionAddField(TextField conditionAddField) {
        this.conditionAddField = conditionAddField;
        return this;
    }

    public TextField getConditionAddCategoryField() {
        return conditionAddCategoryField;
    }

    public PanelController setConditionAddCategoryField(TextField conditionAddCategoryField) {
        this.conditionAddCategoryField = conditionAddCategoryField;
        return this;
    }

    public TextArea getConditionAddDescription() {
        return conditionAddDescription;
    }

    public PanelController setConditionAddDescription(TextArea conditionAddDescription) {
        this.conditionAddDescription = conditionAddDescription;
        return this;
    }

    public Button getConditionAddButton() {
        return conditionAddButton;
    }

    public PanelController setConditionAddButton(Button conditionAddButton) {
        this.conditionAddButton = conditionAddButton;
        return this;
    }

    public DatePicker getConditionAddDateDiagnosed() {
        return conditionAddDateDiagnosed;
    }

    public PanelController setConditionAddDateDiagnosed(DatePicker conditionAddDateDiagnosed) {
        this.conditionAddDateDiagnosed = conditionAddDateDiagnosed;
        return this;
    }

    public Tab getViewHistoryTab() {
        return viewHistoryTab;
    }

    public PanelController setViewHistoryTab(Tab viewHistoryTab) {
        this.viewHistoryTab = viewHistoryTab;
        return this;
    }

    public TableView<HistoryMasterClass> getTablehistory() {
        return tablehistory;
    }

    public PanelController setTablehistory(TableView<HistoryMasterClass> tablehistory) {
        this.tablehistory = tablehistory;
        return this;
    }

    public TableColumn<HistoryMasterClass, String> getTablehistoryId() {
        return tablehistoryId;
    }

    public PanelController setTablehistoryId(TableColumn<HistoryMasterClass, String> tablehistoryId) {
        this.tablehistoryId = tablehistoryId;
        return this;
    }

    public TableColumn<HistoryMasterClass, String> getTablehistoryDate() {
        return tablehistoryDate;
    }

    public PanelController setTablehistoryDate(TableColumn<HistoryMasterClass, String> tablehistoryDate) {
        this.tablehistoryDate = tablehistoryDate;
        return this;
    }

    public TableColumn<HistoryMasterClass, String> getTablehistoryDoctor() {
        return tablehistoryDoctor;
    }

    public PanelController setTablehistoryDoctor(TableColumn<HistoryMasterClass, String> tablehistoryDoctor) {
        this.tablehistoryDoctor = tablehistoryDoctor;
        return this;
    }

    public TableColumn<HistoryMasterClass, String> getTablehistoryPrescription() {
        return tablehistoryPrescription;
    }

    public PanelController setTablehistoryPrescription(TableColumn<HistoryMasterClass, String> tablehistoryPrescription) {
        this.tablehistoryPrescription = tablehistoryPrescription;
        return this;
    }


    public Button getTablehistoryViewPrescriptionsButton() {
        return tablehistoryViewPrescriptionsButton;
    }

    public PanelController setTablehistoryViewPrescriptionsButton(Button tablehistoryViewPrescriptionsButton) {
        this.tablehistoryViewPrescriptionsButton = tablehistoryViewPrescriptionsButton;
        return this;
    }

    public Button getTablehistoryViewLabTestButton() {
        return tablehistoryViewLabTestButton;
    }

    public PanelController setTablehistoryViewLabTestButton(Button tablehistoryViewLabTestButton) {
        this.tablehistoryViewLabTestButton = tablehistoryViewLabTestButton;
        return this;
    }

    public Button getTablehistoryViewDiagnosisButton() {
        return tablehistoryViewDiagnosisButton;
    }

    public PanelController setTablehistoryViewDiagnosisButton(Button tablehistoryViewDiagnosisButton) {
        this.tablehistoryViewDiagnosisButton = tablehistoryViewDiagnosisButton;
        return this;
    }

    public Button getTablehistoryGetReportButton() {
        return tablehistoryGetReportButton;
    }

    public PanelController setTablehistoryGetReportButton(Button tablehistoryGetReportButton) {
        this.tablehistoryGetReportButton = tablehistoryGetReportButton;
        return this;
    }

    public Tab getExistingConditionsTab() {
        return existingConditionsTab;
    }

    public PanelController setExistingConditionsTab(Tab existingConditionsTab) {
        this.existingConditionsTab = existingConditionsTab;
        return this;
    }

    public TableView<ConditionsMasterClass> getExistingConditionsTabTable() {
        return existingConditionsTabTable;
    }

    public PanelController setExistingConditionsTabTable(TableView<ConditionsMasterClass> existingConditionsTabTable) {
        this.existingConditionsTabTable = existingConditionsTabTable;
        return this;
    }

    public TableColumn<ConditionsMasterClass, String> getExistingConditionsTabTableId() {
        return existingConditionsTabTableId;
    }

    public PanelController setExistingConditionsTabTableId(TableColumn<ConditionsMasterClass, String> existingConditionsTabTableId) {
        this.existingConditionsTabTableId = existingConditionsTabTableId;
        return this;
    }

    public TableColumn<ConditionsMasterClass, String> getExistingConditionsTabTableName() {
        return existingConditionsTabTableName;
    }

    public PanelController setExistingConditionsTabTableName(TableColumn<ConditionsMasterClass, String> existingConditionsTabTableName) {
        this.existingConditionsTabTableName = existingConditionsTabTableName;
        return this;
    }

    public TableColumn<ConditionsMasterClass, String> getExistingConditionsTabTableDateAdded() {
        return existingConditionsTabTableDateAdded;
    }

    public PanelController setExistingConditionsTabTableDateAdded(TableColumn<ConditionsMasterClass, String> existingConditionsTabTableDateAdded) {
        this.existingConditionsTabTableDateAdded = existingConditionsTabTableDateAdded;
        return this;
    }

    public TableColumn<ConditionsMasterClass, String> getExistingConditionsTabTableCategory() {
        return existingConditionsTabTableCategory;
    }

    public PanelController setExistingConditionsTabTableCategory(TableColumn<ConditionsMasterClass, String> existingConditionsTabTableCategory) {
        this.existingConditionsTabTableCategory = existingConditionsTabTableCategory;
        return this;
    }

    public TableColumn<ConditionsMasterClass, String> getExistingConditionsTabTableDoctor() {
        return existingConditionsTabTableDoctor;
    }

    public PanelController setExistingConditionsTabTableDoctor(TableColumn<ConditionsMasterClass, String> existingConditionsTabTableDoctor) {
        this.existingConditionsTabTableDoctor = existingConditionsTabTableDoctor;
        return this;
    }

    public Button getExistingConditionsTabTableViewDetailsButton() {
        return existingConditionsTabTableViewDetailsButton;
    }

    public PanelController setExistingConditionsTabTableViewDetailsButton(Button existingConditionsTabTableViewDetailsButton) {
        this.existingConditionsTabTableViewDetailsButton = existingConditionsTabTableViewDetailsButton;
        return this;
    }

    public Tab getTabClinicAppointments() {
        return tabClinicAppointments;
    }

    public PanelController setTabClinicAppointments(Tab tabClinicAppointments) {
        this.tabClinicAppointments = tabClinicAppointments;
        return this;
    }

    public TableView<AppointmentMasterClass> getTabClinicAppointmentsTable() {
        return tabClinicAppointmentsTable;
    }

    public PanelController setTabClinicAppointmentsTable(TableView<AppointmentMasterClass> tabClinicAppointmentsTable) {
        this.tabClinicAppointmentsTable = tabClinicAppointmentsTable;
        return this;
    }

    public TableColumn<AppointmentMasterClass, String> getTabClinicAppointmentsTableId() {
        return tabClinicAppointmentsTableId;
    }

    public PanelController setTabClinicAppointmentsTableId(TableColumn<AppointmentMasterClass, String> tabClinicAppointmentsTableId) {
        this.tabClinicAppointmentsTableId = tabClinicAppointmentsTableId;
        return this;
    }

    public TableColumn<AppointmentMasterClass, String> getTabClinicAppointmentsTableTypeOfVisit() {
        return tabClinicAppointmentsTableTypeOfVisit;
    }

    public PanelController setTabClinicAppointmentsTableTypeOfVisit(TableColumn<AppointmentMasterClass, String> tabClinicAppointmentsTableTypeOfVisit) {
        this.tabClinicAppointmentsTableTypeOfVisit = tabClinicAppointmentsTableTypeOfVisit;
        return this;
    }

    public TableColumn<AppointmentMasterClass, String> getTabClinicAppointmentsTableTimeOfAppointment() {
        return tabClinicAppointmentsTableTimeOfAppointment;
    }

    public PanelController setTabClinicAppointmentsTableTimeOfAppointment(TableColumn<AppointmentMasterClass, String> tabClinicAppointmentsTableTimeOfAppointment) {
        this.tabClinicAppointmentsTableTimeOfAppointment = tabClinicAppointmentsTableTimeOfAppointment;
        return this;
    }

    public Button getTabClinicAppointmentsTableCallInButton() {
        return tabClinicAppointmentsTableCallInButton;
    }

    public PanelController setTabClinicAppointmentsTableCallInButton(Button tabClinicAppointmentsTableCallInButton) {
        this.tabClinicAppointmentsTableCallInButton = tabClinicAppointmentsTableCallInButton;
        return this;
    }

    public Tab getTabClinicLabTests() {
        return tabClinicLabTests;
    }

    public PanelController setTabClinicLabTests(Tab tabClinicLabTests) {
        this.tabClinicLabTests = tabClinicLabTests;
        return this;
    }

    public TableView<LabTestsMasterClass> getTabClinicLabTestsTable() {
        return tabClinicLabTestsTable;
    }

    public PanelController setTabClinicLabTestsTable(TableView<LabTestsMasterClass> tabClinicLabTestsTable) {
        this.tabClinicLabTestsTable = tabClinicLabTestsTable;
        return this;
    }

    public TableColumn<LabTestsMasterClass, String> getTabClinicLabTestsTableId() {
        return tabClinicLabTestsTableId;
    }

    public PanelController setTabClinicLabTestsTableId(TableColumn<LabTestsMasterClass, String> tabClinicLabTestsTableId) {
        this.tabClinicLabTestsTableId = tabClinicLabTestsTableId;
        return this;
    }

    public TableColumn<LabTestsMasterClass, String> getTabClinicLabTestsTableTestType() {
        return tabClinicLabTestsTableTestType;
    }

    public PanelController setTabClinicLabTestsTableTestType(TableColumn<LabTestsMasterClass, String> tabClinicLabTestsTableTestType) {
        this.tabClinicLabTestsTableTestType = tabClinicLabTestsTableTestType;
        return this;
    }

    public TableColumn<LabTestsMasterClass, String> getTabClinicLabTestsTableTechnician() {
        return tabClinicLabTestsTableTechnician;
    }

    public PanelController setTabClinicLabTestsTableTechnician(TableColumn<LabTestsMasterClass, String> tabClinicLabTestsTableTechnician) {
        this.tabClinicLabTestsTableTechnician = tabClinicLabTestsTableTechnician;
        return this;
    }

    public TableColumn<LabTestsMasterClass, String> getTabClinicLabTestsTablePatientName() {
        return tabClinicLabTestsTablePatientName;
    }

    public PanelController setTabClinicLabTestsTablePatientName(TableColumn<LabTestsMasterClass, String> tabClinicLabTestsTablePatientName) {
        this.tabClinicLabTestsTablePatientName = tabClinicLabTestsTablePatientName;
        return this;
    }

    public TableColumn<LabTestsMasterClass, String> getTabClinicLabTestsTableStatus() {
        return tabClinicLabTestsTableStatus;
    }

    public PanelController setTabClinicLabTestsTableStatus(TableColumn<LabTestsMasterClass, String> tabClinicLabTestsTableStatus) {
        this.tabClinicLabTestsTableStatus = tabClinicLabTestsTableStatus;
        return this;
    }

    public Button getTabClinicLabTestsTableGetFullReportButton() {
        return tabClinicLabTestsTableGetFullReportButton;
    }

    public PanelController setTabClinicLabTestsTableGetFullReportButton(Button tabClinicLabTestsTableGetFullReportButton) {
        this.tabClinicLabTestsTableGetFullReportButton = tabClinicLabTestsTableGetFullReportButton;
        return this;
    }

    public Button getTabClinicLabTestsTablemoveReportToDiagnosis() {
        return tabClinicLabTestsTablemoveReportToDiagnosis;
    }

    public PanelController setTabClinicLabTestsTablemoveReportToDiagnosis(Button tabClinicLabTestsTablemoveReportToDiagnosis) {
        this.tabClinicLabTestsTablemoveReportToDiagnosis = tabClinicLabTestsTablemoveReportToDiagnosis;
        return this;
    }

    public Tab getTabClinicDiagnosis() {
        return tabClinicDiagnosis;
    }

    public PanelController setTabClinicDiagnosis(Tab tabClinicDiagnosis) {
        this.tabClinicDiagnosis = tabClinicDiagnosis;
        return this;
    }

    public TextArea getTabClinicDiagnosisInput() {
        return tabClinicDiagnosisInput;
    }

    public PanelController setTabClinicDiagnosisInput(TextArea tabClinicDiagnosisInput) {
        this.tabClinicDiagnosisInput = tabClinicDiagnosisInput;
        return this;
    }

    public Button getTabClinicDiagnosisSubmit() {
        return tabClinicDiagnosisSubmit;
    }

    public PanelController setTabClinicDiagnosisSubmit(Button tabClinicDiagnosisSubmit) {
        this.tabClinicDiagnosisSubmit = tabClinicDiagnosisSubmit;
        return this;
    }

    public Tab getTabClinicPrescription() {
        return tabClinicPrescription;
    }

    public PanelController setTabClinicPrescription(Tab tabClinicPrescription) {
        this.tabClinicPrescription = tabClinicPrescription;
        return this;
    }

    public Button getTabClinicPrescriptionSubmit() {
        return tabClinicPrescriptionSubmit;
    }

    public PanelController setTabClinicPrescriptionSubmit(Button tabClinicPrescriptionSubmit) {
        this.tabClinicPrescriptionSubmit = tabClinicPrescriptionSubmit;
        return this;
    }

    public TextArea getTabClinicPrescriptionText() {
        return tabClinicPrescriptionText;
    }

    public PanelController setTabClinicPrescriptionText(TextArea tabClinicPrescriptionText) {
        this.tabClinicPrescriptionText = tabClinicPrescriptionText;
        return this;
    }

    public Button getEndPatientSession() {
        return endPatientSession;
    }

    public PanelController setEndPatientSession(Button endPatientSession) {
        this.endPatientSession = endPatientSession;
        return this;
    }

    public TabPane getAppointmentssearch() {
        return appointmentssearch;
    }

    public PanelController setAppointmentssearch(TabPane appointmentssearch) {
        this.appointmentssearch = appointmentssearch;
        return this;
    }

    public TableView<AppointmentMasterClass> getTabClinicSessionsTable() {
        return tabClinicSessionsTable;
    }

    public PanelController setTabClinicSessionsTable(TableView<AppointmentMasterClass> tabClinicSessionsTable) {
        this.tabClinicSessionsTable = tabClinicSessionsTable;
        return this;
    }

    public TableColumn<AppointmentMasterClass, String> getTabClinicSessionsTableId() {
        return tabClinicSessionsTableId;
    }

    public PanelController setTabClinicSessionsTableId(TableColumn<AppointmentMasterClass, String> tabClinicSessionsTableId) {
        this.tabClinicSessionsTableId = tabClinicSessionsTableId;
        return this;
    }

    public TableColumn<AppointmentMasterClass, String> getTabClinicSessionsTablePatientEmail() {
        return tabClinicSessionsTablePatientEmail;
    }

    public PanelController setTabClinicSessionsTablePatientEmail(TableColumn<AppointmentMasterClass, String> tabClinicSessionsTablePatientEmail) {
        this.tabClinicSessionsTablePatientEmail = tabClinicSessionsTablePatientEmail;
        return this;
    }

    public TableColumn<AppointmentMasterClass, String> getTabClinicSessionsTableName() {
        return tabClinicSessionsTableName;
    }

    public PanelController setTabClinicSessionsTableName(TableColumn<AppointmentMasterClass, String> tabClinicSessionsTableName) {
        this.tabClinicSessionsTableName = tabClinicSessionsTableName;
        return this;
    }

    public Button getTabClinicSessionsTableResumeInButton() {
        return tabClinicSessionsTableResumeInButton;
    }

    public PanelController setTabClinicSessionsTableResumeInButton(Button tabClinicSessionsTableResumeInButton) {
        this.tabClinicSessionsTableResumeInButton = tabClinicSessionsTableResumeInButton;
        return this;
    }

    public Tab getSessionsTab() {
        return sessionsTab;
    }

    public PanelController setSessionsTab(Tab sessionsTab) {
        this.sessionsTab = sessionsTab;
        return this;
    }

    public Button getStartSessionButton() {
        return startSessionButton;
    }

    public PanelController setStartSessionButton(Button startSessionButton) {
        this.startSessionButton = startSessionButton;
        return this;
    }

    public TabPane getLabteststabpane() {
        return labteststabpane;
    }

    public PanelController setLabteststabpane(TabPane labteststabpane) {
        this.labteststabpane = labteststabpane;
        return this;
    }

    public TextArea getTestsInputPhysician() {
        return testsInputPhysician;
    }

    public PanelController setTestsInputPhysician(TextArea testsInputPhysician) {
        this.testsInputPhysician = testsInputPhysician;
        return this;
    }

    public Button getTestsSendToLab() {
        return testsSendToLab;
    }

    public PanelController setTestsSendToLab(Button testsSendToLab) {
        this.testsSendToLab = testsSendToLab;
        return this;
    }

    public ImageView getResultPreview() {
        return resultPreview;
    }

    public PanelController setResultPreview(ImageView resultPreview) {
        this.resultPreview = resultPreview;
        return this;
    }

    public ObservableList<RecordsMasterClass> getRecordsMasterClassObservableList() {
        return recordsMasterClassObservableList;
    }

    public PanelController setRecordsMasterClassObservableList(ObservableList<RecordsMasterClass> recordsMasterClassObservableList) {
        this.recordsMasterClassObservableList = recordsMasterClassObservableList;
        return this;
    }

    public ObservableList<HistoryMasterClass> getHistoryMasterClassObservableList() {
        return historyMasterClassObservableList;
    }

    public PanelController setHistoryMasterClassObservableList(ObservableList<HistoryMasterClass> historyMasterClassObservableList) {
        this.historyMasterClassObservableList = historyMasterClassObservableList;
        return this;
    }

    public ObservableList<ConditionsMasterClass> getConditionsMasterClassObservableList() {
        return conditionsMasterClassObservableList;
    }

    public PanelController setConditionsMasterClassObservableList(ObservableList<ConditionsMasterClass> conditionsMasterClassObservableList) {
        this.conditionsMasterClassObservableList = conditionsMasterClassObservableList;
        return this;
    }

    public ObservableList<AppointmentMasterClass> getAppointmentMasterClassObservableList() {
        return appointmentMasterClassObservableList;
    }

    public PanelController setAppointmentMasterClassObservableList(ObservableList<AppointmentMasterClass> appointmentMasterClassObservableList) {
        this.appointmentMasterClassObservableList = appointmentMasterClassObservableList;
        return this;
    }

    public ObservableList<AppointmentMasterClass> getAppointmentMasterClassObservableList2() {
        return appointmentMasterClassObservableList2;
    }

    public PanelController setAppointmentMasterClassObservableList2(ObservableList<AppointmentMasterClass> appointmentMasterClassObservableList2) {
        this.appointmentMasterClassObservableList2 = appointmentMasterClassObservableList2;
        return this;
    }

    public ObservableList<LabTestsMasterClass> getLabTestsMasterClassObservableList() {
        return labTestsMasterClassObservableList;
    }

    public PanelController setLabTestsMasterClassObservableList(ObservableList<LabTestsMasterClass> labTestsMasterClassObservableList) {
        this.labTestsMasterClassObservableList = labTestsMasterClassObservableList;
        return this;
    }

    public ArrayList<TabPane> getTabPaneArrayList() {
        return tabPaneArrayList;
    }

    public PanelController setTabPaneArrayList(ArrayList<TabPane> tabPaneArrayList) {
        this.tabPaneArrayList = tabPaneArrayList;
        return this;
    }

    public String getDate() {
        return date;
    }

    public PanelController setDate(String date) {
        this.date = date;
        return this;
    }
}
