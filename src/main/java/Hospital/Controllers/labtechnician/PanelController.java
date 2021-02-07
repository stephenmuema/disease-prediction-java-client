package Hospital.Controllers.labtechnician;


import Hospital.Controllers.MasterClasses.LabTestsMasterClass;
import Hospital.Controllers.MasterClasses.SessionMasterClass;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.io.FileUtils;
import securityandtime.AesCipher;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.ResourceBundle;

import static Hospital.Controllers.settings.*;


public class PanelController extends Super implements Initializable, LabSettings {
    public AnchorPane panel;
    public TabPane tabContainer;
    public Tab pendingteststab;
    public TableView<LabTestsMasterClass> pendingTestsTable;
    public TableColumn<LabTestsMasterClass, String> pendingTestsTableid;
    public TableColumn<LabTestsMasterClass, String> pendingTestsTabledoctor;
    public TableColumn<LabTestsMasterClass, String> pendingTestsTablepatientname;
    public TableColumn<LabTestsMasterClass, String> pendingTestsTableTests;
    public TableColumn<LabTestsMasterClass, String> pendingTestsTableStatus;
    public Button pendingTestsTablestartTest;
    public Button pendingTestsTableviewdetails;
    public Tab labtestresultstab;
    public AnchorPane labtestsresultscontainer;
    public TextArea testresults;
    public Button submitImageResult;
    public Button submitTypedResult;
    public Tab sessionsTab;
    public TableView<SessionMasterClass> tablabSessionsTable;
    public TableColumn<SessionMasterClass, String> tablabSessionsTableid;
    public TableColumn<SessionMasterClass, String> tablabSessionsTablepatient;
    public TableColumn<SessionMasterClass, String> tablabSessionsTabledoctor;
    public Button tablabSessionsTableresume;
    public Label clock;
    public Button logout;
    public Label title;
    public Label session;
    public ImageView previewImage;
    @FXML
    Button imageres;
    private File file;
    private int length = 0;
    private BufferedImage bufferedImage;
    private IdentityHashMap<String, String> currentSession = new IdentityHashMap<>();
    private ObservableList<LabTestsMasterClass> labTestsMasterClassObservableList = FXCollections.observableArrayList();
    private ObservableList<SessionMasterClass> sessionMasterClassObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        time(clock);
        title.setText(appName + " Labs");
        buttonListeners();
        reloadTables();
        tabContainer.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            if (nv.textProperty().getValue().equals("TEST RESULTS") && currentSession.isEmpty()) {
                try {
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Hospital/views/labtechnician/panel.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "CREATE SESSION FIRST");
            }
        });


    }

    private void reloadTables() {
        if (!currentSession.isEmpty())
            session.setText("Current patient session : " + AesCipher.decrypt(encryptionkey, currentSession.get("currentSession")));
        labTestsMasterClassObservableList.clear();
        sessionMasterClassObservableList.clear();
        viewSessions();
        try {
            viewTests();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    private void initializer() {
//
//        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
//            try {
//                labTestsMasterClassObservableList.clear();
//
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//
//        }),
//                new KeyFrame(Duration.seconds(60))
//        );
//        timeline.setCycleCount(Animation.INDEFINITE);
//        timeline.play();
//
//
//    }

    private void viewTests() throws SQLException {
        //select teststext where technician is the current logged in user
        labTestsMasterClassObservableList.clear();
        String selectTechnicianTests = "SELECT * FROM labtests WHERE technician=?";
        PreparedStatement select = hospitalDbConnection.prepareStatement(selectTechnicianTests);
//        System.out.println(settings.user.get("user"));
//        System.out.println(AesCipher.encrypt(encryptionkey,initVector,settings.user.get("user")));
        select.setString(1, AesCipher.encrypt(encryptionkey, initVector, settings.user.get("user")));
        ResultSet selectedResults = select.executeQuery();
        if (selectedResults.isBeforeFirst()) {
            while (selectedResults.next()) {

                LabTestsMasterClass labTestsMasterClass = new LabTestsMasterClass();
                labTestsMasterClass.setId(selectedResults.getString("id"));
                labTestsMasterClass.setDocName(AesCipher.decrypt(encryptionkey, selectedResults.getString("doctorname")));
                labTestsMasterClass.setPatientName(AesCipher.decrypt(encryptionkey, selectedResults.getString("patientname")));
                labTestsMasterClass.setTests(selectedResults.getString("tests"));
                labTestsMasterClass.setStatus(selectedResults.getString("status"));
                labTestsMasterClassObservableList.add(labTestsMasterClass);
            }
            pendingTestsTable.setItems(labTestsMasterClassObservableList);
            pendingTestsTableid.setCellValueFactory(new PropertyValueFactory<>("id"));
            pendingTestsTabledoctor.setCellValueFactory(new PropertyValueFactory<>("docName"));
            pendingTestsTablepatientname.setCellValueFactory(new PropertyValueFactory<>("patientName"));
            pendingTestsTableStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            pendingTestsTableTests.setCellValueFactory(new PropertyValueFactory<>("tests"));
            pendingTestsTable.refresh();
        } else {
//            System.out.println("there are no tests");
//            showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "FREEDOM", "THERE ARE NO TESTS TO BE DONE BY YOU");
        }
//        viewTests();
    }

    private void startSession(String tests, String email) {
        if (currentSession.isEmpty()) {
            currentSession.put("currentSession", email);
            try {
                PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement("UPDATE labtests set status=? WHERE patientname=?");
                preparedStatement.setString(1, "ONGOING");
                preparedStatement.setString(2, email);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            currentSession.replace("currentSession", email);
        }
//            viewPatientDetails();
//            System.out.println(email + " is the email");
        try {
            PreparedStatement main = hospitalDbConnection.prepareStatement("SELECT * FROM patients WHERE email=?");
            main.setString(1, email);
            ResultSet rsmain = main.executeQuery();
            //check if in sessions table
            PreparedStatement preparedStatement = sessionsDbConnection.prepareStatement("SELECT * FROM SessionLabs WHERE email=? and staffid=?");
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, Integer.parseInt(settings.id.get("userid")));
            ResultSet check = preparedStatement.executeQuery();
            if (check.isBeforeFirst()) {
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "TEST ALREADY IN SESSION", "THE TEST HAS AN EXISTING SESSION.SESSION HAS BEEN RESUMED");
            } else {
                PreparedStatement statement = sessionsDbConnection.prepareStatement("INSERT INTO SessionLabs(name, email, sessionId,testText,staffid) VALUES (?,?,?,?,?)");
                if (rsmain.isBeforeFirst()) {
                    while (rsmain.next()) {
                        statement.setString(1, rsmain.getString("name"));
                        statement.setString(2, currentSession.get("currentSession"));
                        statement.setString(3, dateTimeMethod());
                        statement.setString(4, tests);
                        statement.setInt(5, Integer.parseInt(settings.id.get("userid")));
                        if (statement.executeUpdate() > 0) {
                            showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), currentSession.get("currentSession") + " session", "SESSION CREATED SUCCESSFULLY");

                        } else {
                            showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), currentSession.get("currentSession") + " session", "SESSION CREATION FAILED");

                        }
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "ERROR", "UNEXPECTED ERROR");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        labTestsMasterClassObservableList.clear();

        loadSessions();
    }

    private void loadSessions() {
        try {
            Statement preparedStatement = sessionsDbConnection.createStatement();
            ResultSet resultSet = preparedStatement.executeQuery("SELECT * FROM SessionLabs WHERE staffid=" + Integer.parseInt(settings.id.get("userid")));
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    SessionMasterClass appointmentMasterClass = new SessionMasterClass();
                    appointmentMasterClass.setSize(appointmentMasterClass.getSize() + 1);
                    appointmentMasterClass.setId(resultSet.getString("sessionId"));
                    appointmentMasterClass.setName(AesCipher.decrypt(encryptionkey, resultSet.getString("name")));

                    appointmentMasterClass.setPatientEmail(AesCipher.decrypt(encryptionkey, resultSet.getString("email")));
                    sessionMasterClassObservableList.add(appointmentMasterClass);
                }
                tablabSessionsTable.setItems(sessionMasterClassObservableList);
                tablabSessionsTableid.setCellValueFactory(new PropertyValueFactory<>("id"));
                tablabSessionsTablepatient.setCellValueFactory(new PropertyValueFactory<>("name"));
                tablabSessionsTabledoctor.setCellValueFactory(new PropertyValueFactory<>("patientEmail"));
                tablabSessionsTable.refresh();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void endSession() {
        try {
            try {
                PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement("UPDATE labtests set status=? WHERE patientname=?");
                preparedStatement.setString(1, "COMPLETE");
                preparedStatement.setString(2, currentSession.get("currentSession"));
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            PreparedStatement preparedStatement = sessionsDbConnection.prepareStatement("DELETE from SessionLabs where email = ? AND staffid=?");
            preparedStatement.setString(1, currentSession.get("currentSession"));
            preparedStatement.setInt(2, Integer.parseInt(settings.id.get("userid")));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Hospital/views/labtechnician/panel.fxml")))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentSession.clear();
    }

    private void viewSessions() {
        loadSessions();
    }

    private void submitResults() {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = hospitalDbConnection.prepareStatement("UPDATE labtests SET results=? WHERE patientname=?");
            preparedStatement.setString(1, testresults.getText());
            preparedStatement.setString(2, currentSession.get("currentSession"));
            if (preparedStatement.executeUpdate() > 0) {
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "OPERATION SUCCESSFULL");
                testresults.clear();
            } else {
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "ERROR UPDATING LAB TESTS");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void buttonListeners() {
        imageres.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();

                //Set extension filter
                FileChooser.ExtensionFilter extensionFilterPng = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
                FileChooser.ExtensionFilter extensionFilterJpg = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
                FileChooser.ExtensionFilter extensionFilterJpeg = new FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.jpeg");
                FileChooser.ExtensionFilter extensionFilterBmp = new FileChooser.ExtensionFilter("BMP files (*.bmp)", "*.bmp");
                fileChooser.getExtensionFilters().addAll(extensionFilterPng, extensionFilterJpg, extensionFilterJpeg, extensionFilterBmp);
                fileChooser.setTitle("SELECT IMAGE FILE");

                //Show open file dialog
                file = fileChooser.showOpenDialog(panel.getScene().getWindow());
                length = (int) file.length();

                //                    fileInputStream = new FileInputStream(file);
                try {
                    bufferedImage = ImageIO.read(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                if (length > 0) {
                    previewImage.setImage(image);
                    submitTypedResult.setDisable(true);
                } else {
                    System.out.println("error");
                }
            }

        });
        tablabSessionsTableresume.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SessionMasterClass labTestsMasterClass = tablabSessionsTable.getSelectionModel().getSelectedItem();
                String gemail = labTestsMasterClass.getPatientEmail();
                if (!currentSession.isEmpty()) {
                    currentSession.replace("currentSession", gemail);

                    showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "SESSION CREATED FOR " + gemail.toUpperCase());

                } else {
                    currentSession.put("currentSession", gemail);

                    showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "SESSION CREATED FOR " + gemail.toUpperCase());

                }

            }
        });
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logOut(panel);
            }
        });
        submitImageResult.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                submitImageResults(file);
            }


        });
        submitTypedResult.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                submitResults();
                endSession();
            }
        });
        pendingTestsTablestartTest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LabTestsMasterClass labTestsMasterClass = pendingTestsTable.getSelectionModel().getSelectedItem();
                String tests = labTestsMasterClass.getTests();
                String gemail = AesCipher.encrypt(encryptionkey, initVector, labTestsMasterClass.getPatientName());
                startSession(tests, gemail);
                reloadTables();
            }
        });

        pendingTestsTableviewdetails.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LabTestsMasterClass labTestsMasterClassselected = pendingTestsTable.getSelectionModel().getSelectedItem();
                ObservableList<TablePosition> selectedList = pendingTestsTable.getSelectionModel().getSelectedCells();
                if (selectedList.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "ERROR", "YOU MUST SELECT A ROW ON THE TABLE TO COMPLETE THIS OPERATION");

                } else {
                    viewDetails(labTestsMasterClassselected.getId());
                }

            }
        });
    }

    private void submitImageResults(File file) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = hospitalDbConnection.prepareStatement("UPDATE labtests SET imageResult=? WHERE patientname=?");
            try {
                preparedStatement.setBinaryStream(1, FileUtils.openInputStream(file), length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            preparedStatement.setString(2, currentSession.get("currentSession"));
            if (preparedStatement.executeUpdate() > 0) {
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "OPERATION SUCCESSFULL");
                previewImage.setImage(null);
            } else {
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "ERROR UPDATING LAB TESTS");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        endSession();

    }

    private void viewDetails(String id) {
        String query = "SELECT tests FROM labtests WHERE id=?";
        try {
            PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement(query);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    teststext.put("teststext", resultSet.getString("tests"));

                }
//
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Hospital/views/labtechnician/popup.fxml"));
                try {
                    Parent parent = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(parent));
                    stage.initStyle(StageStyle.UTILITY);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "ERROR", "INVALID ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//    getters and setters for the class variables

    public AnchorPane getPanel() {
        return panel;
    }

    public PanelController setPanel(AnchorPane panel) {
        this.panel = panel;
        return this;
    }

    public TabPane getTabContainer() {
        return tabContainer;
    }

    public PanelController setTabContainer(TabPane tabContainer) {
        this.tabContainer = tabContainer;
        return this;
    }

    public Tab getPendingteststab() {
        return pendingteststab;
    }

    public PanelController setPendingteststab(Tab pendingteststab) {
        this.pendingteststab = pendingteststab;
        return this;
    }

    public TableView<LabTestsMasterClass> getPendingTestsTable() {
        return pendingTestsTable;
    }

    public PanelController setPendingTestsTable(TableView<LabTestsMasterClass> pendingTestsTable) {
        this.pendingTestsTable = pendingTestsTable;
        return this;
    }

    public TableColumn<LabTestsMasterClass, String> getPendingTestsTableid() {
        return pendingTestsTableid;
    }

    public PanelController setPendingTestsTableid(TableColumn<LabTestsMasterClass, String> pendingTestsTableid) {
        this.pendingTestsTableid = pendingTestsTableid;
        return this;
    }

    public TableColumn<LabTestsMasterClass, String> getPendingTestsTabledoctor() {
        return pendingTestsTabledoctor;
    }

    public PanelController setPendingTestsTabledoctor(TableColumn<LabTestsMasterClass, String> pendingTestsTabledoctor) {
        this.pendingTestsTabledoctor = pendingTestsTabledoctor;
        return this;
    }

    public TableColumn<LabTestsMasterClass, String> getPendingTestsTablepatientname() {
        return pendingTestsTablepatientname;
    }

    public PanelController setPendingTestsTablepatientname(TableColumn<LabTestsMasterClass, String> pendingTestsTablepatientname) {
        this.pendingTestsTablepatientname = pendingTestsTablepatientname;
        return this;
    }

    public TableColumn<LabTestsMasterClass, String> getPendingTestsTableTests() {
        return pendingTestsTableTests;
    }

    public PanelController setPendingTestsTableTests(TableColumn<LabTestsMasterClass, String> pendingTestsTableTests) {
        this.pendingTestsTableTests = pendingTestsTableTests;
        return this;
    }

    public TableColumn<LabTestsMasterClass, String> getPendingTestsTableStatus() {
        return pendingTestsTableStatus;
    }

    public PanelController setPendingTestsTableStatus(TableColumn<LabTestsMasterClass, String> pendingTestsTableStatus) {
        this.pendingTestsTableStatus = pendingTestsTableStatus;
        return this;
    }

    public Button getImageres() {
        return imageres;
    }

    public PanelController setImageres(Button imageres) {
        this.imageres = imageres;
        return this;
    }

    public Button getPendingTestsTablestartTest() {
        return pendingTestsTablestartTest;
    }

    public PanelController setPendingTestsTablestartTest(Button pendingTestsTablestartTest) {
        this.pendingTestsTablestartTest = pendingTestsTablestartTest;
        return this;
    }

    public Button getPendingTestsTableviewdetails() {
        return pendingTestsTableviewdetails;
    }

    public PanelController setPendingTestsTableviewdetails(Button pendingTestsTableviewdetails) {
        this.pendingTestsTableviewdetails = pendingTestsTableviewdetails;
        return this;
    }

    public Tab getLabtestresultstab() {
        return labtestresultstab;
    }

    public PanelController setLabtestresultstab(Tab labtestresultstab) {
        this.labtestresultstab = labtestresultstab;
        return this;
    }

    public AnchorPane getLabtestsresultscontainer() {
        return labtestsresultscontainer;
    }

    public PanelController setLabtestsresultscontainer(AnchorPane labtestsresultscontainer) {
        this.labtestsresultscontainer = labtestsresultscontainer;
        return this;
    }

    public TextArea getTestresults() {
        return testresults;
    }

    public PanelController setTestresults(TextArea testresults) {
        this.testresults = testresults;
        return this;
    }

    public Button getSubmitImageResult() {
        return submitImageResult;
    }

    public PanelController setSubmitImageResult(Button submitImageResult) {
        this.submitImageResult = submitImageResult;
        return this;
    }

    public Button getSubmitTypedResult() {
        return submitTypedResult;
    }

    public PanelController setSubmitTypedResult(Button submitTypedResult) {
        this.submitTypedResult = submitTypedResult;
        return this;
    }

    public Tab getSessionsTab() {
        return sessionsTab;
    }

    public PanelController setSessionsTab(Tab sessionsTab) {
        this.sessionsTab = sessionsTab;
        return this;
    }

    public TableView<SessionMasterClass> getTablabSessionsTable() {
        return tablabSessionsTable;
    }

    public PanelController setTablabSessionsTable(TableView<SessionMasterClass> tablabSessionsTable) {
        this.tablabSessionsTable = tablabSessionsTable;
        return this;
    }

    public TableColumn<SessionMasterClass, String> getTablabSessionsTableid() {
        return tablabSessionsTableid;
    }

    public PanelController setTablabSessionsTableid(TableColumn<SessionMasterClass, String> tablabSessionsTableid) {
        this.tablabSessionsTableid = tablabSessionsTableid;
        return this;
    }

    public TableColumn<SessionMasterClass, String> getTablabSessionsTablepatient() {
        return tablabSessionsTablepatient;
    }

    public PanelController setTablabSessionsTablepatient(TableColumn<SessionMasterClass, String> tablabSessionsTablepatient) {
        this.tablabSessionsTablepatient = tablabSessionsTablepatient;
        return this;
    }

    public TableColumn<SessionMasterClass, String> getTablabSessionsTabledoctor() {
        return tablabSessionsTabledoctor;
    }

    public PanelController setTablabSessionsTabledoctor(TableColumn<SessionMasterClass, String> tablabSessionsTabledoctor) {
        this.tablabSessionsTabledoctor = tablabSessionsTabledoctor;
        return this;
    }

    public Button getTablabSessionsTableresume() {
        return tablabSessionsTableresume;
    }

    public PanelController setTablabSessionsTableresume(Button tablabSessionsTableresume) {
        this.tablabSessionsTableresume = tablabSessionsTableresume;
        return this;
    }

    public Label getClock() {
        return clock;
    }

    public PanelController setClock(Label clock) {
        this.clock = clock;
        return this;
    }

    public Button getLogout() {
        return logout;
    }

    public PanelController setLogout(Button logout) {
        this.logout = logout;
        return this;
    }

    public Label getTitle() {
        return title;
    }

    public PanelController setTitle(Label title) {
        this.title = title;
        return this;
    }

    public Label getSession() {
        return session;
    }

    public PanelController setSession(Label session) {
        this.session = session;
        return this;
    }

    public ImageView getPreviewImage() {
        return previewImage;
    }

    public PanelController setPreviewImage(ImageView previewImage) {
        this.previewImage = previewImage;
        return this;
    }

    public File getFile() {
        return file;
    }

    public PanelController setFile(File file) {
        this.file = file;
        return this;
    }

    public int getLength() {
        return length;
    }

    public PanelController setLength(int length) {
        this.length = length;
        return this;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public PanelController setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        return this;
    }

    public IdentityHashMap<String, String> getCurrentSession() {
        return currentSession;
    }

    public PanelController setCurrentSession(IdentityHashMap<String, String> currentSession) {
        this.currentSession = currentSession;
        return this;
    }

    public ObservableList<LabTestsMasterClass> getLabTestsMasterClassObservableList() {
        return labTestsMasterClassObservableList;
    }

    public PanelController setLabTestsMasterClassObservableList(ObservableList<LabTestsMasterClass> labTestsMasterClassObservableList) {
        this.labTestsMasterClassObservableList = labTestsMasterClassObservableList;
        return this;
    }

    public ObservableList<SessionMasterClass> getSessionMasterClassObservableList() {
        return sessionMasterClassObservableList;
    }

    public PanelController setSessionMasterClassObservableList(ObservableList<SessionMasterClass> sessionMasterClassObservableList) {
        this.sessionMasterClassObservableList = sessionMasterClassObservableList;
        return this;
    }
}
