package Hospital.Controllers.Admins;

import Hospital.Controllers.MasterClasses.HistoryMasterClass;
import Hospital.Controllers.MasterClasses.StaffMasterClass;
import Hospital.Controllers.Super;
import Hospital.Controllers.settings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import securityandtime.AesCipher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Hospital.Controllers.settings.*;


public class PanelController extends Super implements Initializable {

    private final IdentityHashMap<String, String> currentSession = new IdentityHashMap<>();
    private final ObservableList<HistoryMasterClass> historyMasterClassObservableList = FXCollections.observableArrayList();
    public WebView webview;
    public Button adduser;
    public TextArea userdescription;
    public TextField useridentifier;
    public TextField useremail;
    public TextField username;
    public AnchorPane panel;
    public ChoiceBox role;
    public TextField location;
    public Button logout;
    public Button addcertfile;
    public Tab regstaff;
    public Tab patientinfo;
    public Tab staffinfo;
    public Tab news;
    public TextField searchPatientEmail;
    public Button searchpatientbutton;
    public Label clock;
    public TabPane tabpane;
    public Label title;
    public TableView<StaffMasterClass> viewStaff;
    public TableColumn<StaffMasterClass, String> viewStaffId;
    public TableColumn<StaffMasterClass, String> viewStaffName;
    public TableColumn<StaffMasterClass, String> viewStaffEmail;
    public TableColumn<StaffMasterClass, String> viewStaffIdentity;
    public TableColumn<StaffMasterClass, String> viewStaffBranch;
    public TableColumn<StaffMasterClass, String> viewStaffStatus;
    public Button fire;
    public Button suspend;
    public Button maternity;
    public Button leave;
    public Button shortBreak;
    public Button genreport;
    public TableView<HistoryMasterClass> tablehistory;
    public TableColumn<HistoryMasterClass, String> tablehistoryId;
    public TableColumn<HistoryMasterClass, String> tablehistoryDate;
    public TableColumn<HistoryMasterClass, String> tablehistoryDoctor;
    public TableColumn<HistoryMasterClass, String> tablehistoryPrescription;
    public TableColumn<HistoryMasterClass, Boolean> tablehistoryCompleted;
    private double tabWidth = 200.0;
    private ArrayList<TabPane> tabPaneArrayList = new ArrayList<>();
    private ObservableList<StaffMasterClass> staffMasterClassObservableList = FXCollections.observableArrayList();
    private File file;
    private FileInputStream fileInputStream;
    private int length;

    //Administrator panel controller
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
        //viewStaffInfo();
        title.setText(appName + " Admin Panel ");

    }

    private void init() {
//        if(changepassword.containsKey("change")){
//            if(changepassword.get("change")){
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//                alert.setHeaderText(null);
//                alert.setTitle("Change your password");
//                alert.setContentText("Please change your default password for your own account privacy");
//                Optional<ButtonType> result = alert.showAndWait();
//                if (result.get() == ButtonType.OK){
//                    PasswordDialog pd = new PasswordDialog();
//                    Optional<String> res = pd.showAndWait();
//                    res.ifPresent(password ->{
//                        out.println(password);} );
//
//
//                }
//            }
//            changepassword.remove("change");
//        }


        viewStaff.setEditable(true);
        viewStaffStatus.setCellFactory(TextFieldTableCell.forTableColumn());
        viewStaffStatus.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<StaffMasterClass, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<StaffMasterClass, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setStatus(t.getNewValue());
                        String newval = t.getNewValue();

                        try {
                            PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement("UPDATE users set status=? WHERE id=?");
                            preparedStatement.setString(1, newval);
                            preparedStatement.setString(2, t.getTableView().getItems().get(
                                    t.getTablePosition().getRow()).getId());
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        tabPaneArrayList.add(tabpane);
        time(clock);
        buttonListeners();
        enterPressed();
        WebEngine engine = webview.getEngine();//help web page
        engine.load(siteHelp);
//        configureView(tabPaneArrayList);
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

    private void enterPressed() {

        useridentifier.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                addNewUser();
            }
        });
        useremail.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                addNewUser();
            }
        });
        userdescription.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                addNewUser();
            }
        });
        username.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                addNewUser();
            }
        });

    }

    private void buttonListeners() {
//        ReportsClass reportsClass = new ReportsClass();
        genreport.setOnAction(event -> {

//            reportsClass.diseaseStats();
//
//            reportsClass.closeDocument();
        });

        searchpatientbutton.setOnAction(event -> {
            currentSession.put("currentSession", searchPatientEmail.getText());
            searchPatient();
        });

        addcertfile.setOnMouseClicked(event -> {

            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extensionFilterPdf = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().addAll(extensionFilterPdf);
            fileChooser.setTitle("SELECT PDF FILE OF CERTIFICATION");
            //Show open file dialog
            file = fileChooser.showOpenDialog(panel.getScene().getWindow());
            length = (int) file.length();

            try {
                fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        adduser.setOnMouseClicked(event -> {
//            add new user
            addNewUser();
        });
        logout.setOnMouseClicked(event -> {
            logOut(panel);

        });
        fire.setOnMouseClicked(event -> fireUser(viewStaff));
        staffinfo.setOnSelectionChanged(event -> {
            if (staffinfo.isSelected()) {
                System.out.println("Tab is Selected");
                viewStaffInfo();
            }
        });
        suspend.setOnMouseClicked(event -> suspendUser(viewStaff));
        maternity.setOnMouseClicked(event -> giveMaternityToUser(viewStaff));
        leave.setOnMouseClicked(event -> giveLeaveToUser(viewStaff));
        shortBreak.setOnMouseClicked(event -> giveBreakToUser(viewStaff));

    }

    private void addNewUser() {
        try {
            validation();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void validation() throws SQLException {
//check if all fields are filled
        if (username.getText().isEmpty() || useremail.getText().isEmpty() || userdescription.getText().isEmpty() || useridentifier.getText().isEmpty() || !file.exists() || file.length() == 0) {
            showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(),
                    "FILL ALL FIELDS", "PLEASE FILL ALL FIELDS");

        } else {
//            AesCipher.encrypt(encryptionkey,initVector,string)
//             AesCipher.decrypt(encryptionkey,string)
            String roleval = role.getValue().toString();
            String locationString = location.getText();
            String name = username.getText();
            String description = userdescription.getText();
            String identification = useridentifier.getText();
            String email = useremail.getText();
            PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement("SELECT * FROM users WHERE email=?");
            preparedStatement.setString(1, AesCipher.encrypt(encryptionkey, initVector, email));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "User exists", "The user email is already used");

            } else {
                String regex = "^(.+)@(.+)$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(email);

                if (matcher.matches()) {
                    //adding user into database
                    PreparedStatement insertStaff = hospitalDbConnection.prepareStatement("INSERT INTO users(name, email, password, hospital, status, userclearancelevel,certification,identification,description) VALUES (?,?,?,?,?,?,?,?,?)");
                    insertStaff.setString(1, AesCipher.encrypt(encryptionkey, initVector, name));
                    insertStaff.setString(2, AesCipher.encrypt(encryptionkey, initVector, email));
                    insertStaff.setString(3, AesCipher.encrypt(encryptionkey, initVector, email));
                    insertStaff.setString(4, AesCipher.encrypt(encryptionkey, initVector, locationString.toUpperCase()));
                    insertStaff.setString(5, "active");
                    insertStaff.setString(6, AesCipher.encrypt(encryptionkey, initVector, roleval));
                    try {
                        insertStaff.setBinaryStream(7, FileUtils.openInputStream(file), length);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    insertStaff.setString(8, AesCipher.encrypt(encryptionkey, initVector, identification));
                    insertStaff.setString(9, AesCipher.encrypt(encryptionkey, initVector, description));
                    int x = insertStaff.executeUpdate();
                    if (x > 0) {
                        showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "OPERATION SUCCESSFULL");
                        username.clear();
                        userdescription.clear();
                        useremail.clear();
                        location.clear();
                        useridentifier.clear();
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(),
                            "INVALID EMAIL", "PLEASE ENTER A VALID EMAIL");
                }
            }


        }
    }

    public void viewStaffInfo() {
        viewStaff.getItems().clear();
        try {
            PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement("SELECT * FROM users WHERE id <> ?");
            preparedStatement.setInt(1, Integer.parseInt(settings.id.get("userid")));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    StaffMasterClass staffMasterClass = new StaffMasterClass();
                    staffMasterClass.setId(resultSet.getString("id"));
                    staffMasterClass.setName(AesCipher.decrypt(encryptionkey, resultSet.getString("name")));
                    staffMasterClass.setEmail(AesCipher.decrypt(encryptionkey, resultSet.getString("email")));
                    staffMasterClass.setIdentity(AesCipher.decrypt(encryptionkey, resultSet.getString("userclearancelevel")));
                    staffMasterClass.setBranch(AesCipher.decrypt(encryptionkey, resultSet.getString("hospital")));
                    staffMasterClass.setStatus(resultSet.getString("status"));
                    staffMasterClassObservableList.add(staffMasterClass);
                }
                viewStaff.setItems(staffMasterClassObservableList);
                viewStaffBranch.setCellValueFactory(new PropertyValueFactory<>("branch"));
                viewStaffEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
                viewStaffId.setCellValueFactory(new PropertyValueFactory<>("id"));
                viewStaffIdentity.setCellValueFactory(new PropertyValueFactory<>("identity"));
                viewStaffName.setCellValueFactory(new PropertyValueFactory<>("name"));
                viewStaffStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fireUser(TableView<StaffMasterClass> tableView) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "DO YOU WANT TO DELETE THE USER", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.setTitle("WARNING");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            StaffMasterClass staffMasterClass = tableView.getSelectionModel().getSelectedItem();
            String email = staffMasterClass.getEmail();
            String query = "DELETE FROM users WHERE email=?";
            try {
                PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement(query);
                preparedStatement.setString(1, AesCipher.encrypt(encryptionkey, initVector, email));
                int resultSet = preparedStatement.executeUpdate();
                if (resultSet > 0) {
                    //viewStaff.getItems().clear();
                    viewStaffInfo();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void suspendUser(TableView<StaffMasterClass> tableView) {
        StaffMasterClass staffMasterClass = tableView.getSelectionModel().getSelectedItem();
        String email = staffMasterClass.getEmail();
        String status = "Suspended";
        String query = "UPDATE users SET status=? WHERE email=?";
        try {
            PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement(query);
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, AesCipher.encrypt(encryptionkey, initVector, email));
            int resultSet = preparedStatement.executeUpdate();
            if (resultSet > 0) {
                viewStaffInfo();
            }
        } catch (SQLException e) {
            System.out.println("No Success");
            e.printStackTrace();
        }
    }

    public void giveLeaveToUser(TableView<StaffMasterClass> tableView) {
        StaffMasterClass staffMasterClass = tableView.getSelectionModel().getSelectedItem();
        String email = staffMasterClass.getEmail();
        String status = "On Leave";
        String query = "UPDATE users SET status=? WHERE email=?";
        try {
            PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement(query);
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, AesCipher.encrypt(encryptionkey, initVector, email));
            int resultSet = preparedStatement.executeUpdate();
            if (resultSet > 0) {
                viewStaffInfo();
            }
        } catch (SQLException e) {
            System.out.println("No Success");
            e.printStackTrace();
        }
    }

    public void giveBreakToUser(TableView<StaffMasterClass> tableView) {
        StaffMasterClass staffMasterClass = tableView.getSelectionModel().getSelectedItem();
        String email = staffMasterClass.getEmail();
        String status = "On Break";
        String query = "UPDATE users SET status=? WHERE email=?";
        try {
            PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement(query);
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, AesCipher.encrypt(encryptionkey, initVector, email));
            int resultSet = preparedStatement.executeUpdate();
            if (resultSet > 0) {
                viewStaffInfo();
            }
        } catch (SQLException e) {
            System.out.println("No Success");
            e.printStackTrace();
        }
    }

    public void giveMaternityToUser(TableView<StaffMasterClass> tableView) {
        StaffMasterClass staffMasterClass = tableView.getSelectionModel().getSelectedItem();
        String email = staffMasterClass.getEmail();
        String status = "Maternity Leave";
        String query = "UPDATE users SET status=? WHERE email=?";
        try {
            PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement(query);
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, AesCipher.encrypt(encryptionkey, initVector, email));
            int resultSet = preparedStatement.executeUpdate();
            if (resultSet > 0) {
                viewStaffInfo();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchPatient() {
        historyMasterClassObservableList.clear();
        try {
            PreparedStatement preparedStatement = hospitalDbConnection.prepareStatement("SELECT * FROM prescriptions WHERE  patientemail = ?");
            preparedStatement.setString(1, AesCipher.encrypt(encryptionkey, initVector, currentSession.get("currentSession")));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    HistoryMasterClass historyMasterClass = new HistoryMasterClass();
                    historyMasterClass.setId(resultSet.getString("id"));
                    historyMasterClass.setDate(AesCipher.decrypt(encryptionkey, resultSet.getString("dateprescribed")));
                    historyMasterClass.setDoctor(AesCipher.decrypt(encryptionkey, resultSet.getString("doctor")));
                    historyMasterClass.setPrescription(resultSet.getString("prescription"));
                    historyMasterClass.setTests(resultSet.getString("tests"));
                    historyMasterClass.setCompleted(resultSet.getBoolean("completed"));
                    historyMasterClassObservableList.add(historyMasterClass);
                }
                tablehistory.setItems(historyMasterClassObservableList);

                tablehistoryId.setCellValueFactory(new PropertyValueFactory<>("id"));
                tablehistoryDate.setCellValueFactory(new PropertyValueFactory<>("date"));
                tablehistoryDoctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
                tablehistoryPrescription.setCellValueFactory(new PropertyValueFactory<>("prescription"));
                tablehistoryCompleted.setCellValueFactory(new PropertyValueFactory<>("completed"));
//                tablehistoryTests.setCellValueFactory(new PropertyValueFactory<HistoryMasterClass, String>("tests"));
//                public TableColumn <HistoryMasterClass,String>tablehistoryRatings;
                tablehistory.refresh();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//getters and setters for the class variables

    public WebView getWebview() {
        return webview;
    }

    public PanelController setWebview(WebView webview) {
        this.webview = webview;
        return this;
    }

    public Button getAdduser() {
        return adduser;
    }

    public PanelController setAdduser(Button adduser) {
        this.adduser = adduser;
        return this;
    }

    public TextArea getUserdescription() {
        return userdescription;
    }

    public PanelController setUserdescription(TextArea userdescription) {
        this.userdescription = userdescription;
        return this;
    }

    public TextField getUseridentifier() {
        return useridentifier;
    }

    public PanelController setUseridentifier(TextField useridentifier) {
        this.useridentifier = useridentifier;
        return this;
    }

    public TextField getUseremail() {
        return useremail;
    }

    public PanelController setUseremail(TextField useremail) {
        this.useremail = useremail;
        return this;
    }

    public TextField getUsername() {
        return username;
    }

    public PanelController setUsername(TextField username) {
        this.username = username;
        return this;
    }

    public AnchorPane getPanel() {
        return panel;
    }

    public PanelController setPanel(AnchorPane panel) {
        this.panel = panel;
        return this;
    }

    public ChoiceBox getRole() {
        return role;
    }

    public PanelController setRole(ChoiceBox role) {
        this.role = role;
        return this;
    }

    public TextField getLocation() {
        return location;
    }

    public PanelController setLocation(TextField location) {
        this.location = location;
        return this;
    }

    public Button getLogout() {
        return logout;
    }

    public PanelController setLogout(Button logout) {
        this.logout = logout;
        return this;
    }

    public Button getAddcertfile() {
        return addcertfile;
    }

    public PanelController setAddcertfile(Button addcertfile) {
        this.addcertfile = addcertfile;
        return this;
    }

    public Tab getRegstaff() {
        return regstaff;
    }

    public PanelController setRegstaff(Tab regstaff) {
        this.regstaff = regstaff;
        return this;
    }

    public Tab getPatientinfo() {
        return patientinfo;
    }

    public PanelController setPatientinfo(Tab patientinfo) {
        this.patientinfo = patientinfo;
        return this;
    }

    public Tab getStaffinfo() {
        return staffinfo;
    }

    public PanelController setStaffinfo(Tab staffinfo) {
        this.staffinfo = staffinfo;
        return this;
    }

    public Tab getNews() {
        return news;
    }

    public PanelController setNews(Tab news) {
        this.news = news;
        return this;
    }


    public Button getSearchpatientbutton() {
        return searchpatientbutton;
    }

    public PanelController setSearchpatientbutton(Button searchpatientbutton) {
        this.searchpatientbutton = searchpatientbutton;
        return this;
    }

    public Label getClock() {
        return clock;
    }

    public PanelController setClock(Label clock) {
        this.clock = clock;
        return this;
    }

    public TabPane getTabpane() {
        return tabpane;
    }

    public PanelController setTabpane(TabPane tabpane) {
        this.tabpane = tabpane;
        return this;
    }

    public Label getTitle() {
        return title;
    }

    public PanelController setTitle(Label title) {
        this.title = title;
        return this;
    }

    public TableView<StaffMasterClass> getViewStaff() {
        return viewStaff;
    }

    public PanelController setViewStaff(TableView<StaffMasterClass> viewStaff) {
        this.viewStaff = viewStaff;
        return this;
    }

    public TableColumn<StaffMasterClass, String> getViewStaffId() {
        return viewStaffId;
    }

    public PanelController setViewStaffId(TableColumn<StaffMasterClass, String> viewStaffId) {
        this.viewStaffId = viewStaffId;
        return this;
    }

    public TableColumn<StaffMasterClass, String> getViewStaffName() {
        return viewStaffName;
    }

    public PanelController setViewStaffName(TableColumn<StaffMasterClass, String> viewStaffName) {
        this.viewStaffName = viewStaffName;
        return this;
    }

    public TableColumn<StaffMasterClass, String> getViewStaffEmail() {
        return viewStaffEmail;
    }

    public PanelController setViewStaffEmail(TableColumn<StaffMasterClass, String> viewStaffEmail) {
        this.viewStaffEmail = viewStaffEmail;
        return this;
    }

    public TableColumn<StaffMasterClass, String> getViewStaffIdentity() {
        return viewStaffIdentity;
    }

    public PanelController setViewStaffIdentity(TableColumn<StaffMasterClass, String> viewStaffIdentity) {
        this.viewStaffIdentity = viewStaffIdentity;
        return this;
    }

    public TableColumn<StaffMasterClass, String> getViewStaffBranch() {
        return viewStaffBranch;
    }

    public PanelController setViewStaffBranch(TableColumn<StaffMasterClass, String> viewStaffBranch) {
        this.viewStaffBranch = viewStaffBranch;
        return this;
    }

    public TableColumn<StaffMasterClass, String> getViewStaffStatus() {
        return viewStaffStatus;
    }

    public PanelController setViewStaffStatus(TableColumn<StaffMasterClass, String> viewStaffStatus) {
        this.viewStaffStatus = viewStaffStatus;
        return this;
    }

    public Button getFire() {
        return fire;
    }

    public PanelController setFire(Button fire) {
        this.fire = fire;
        return this;
    }

    public Button getSuspend() {
        return suspend;
    }

    public PanelController setSuspend(Button suspend) {
        this.suspend = suspend;
        return this;
    }

    public Button getMaternity() {
        return maternity;
    }

    public PanelController setMaternity(Button maternity) {
        this.maternity = maternity;
        return this;
    }

    public Button getLeave() {
        return leave;
    }

    public PanelController setLeave(Button leave) {
        this.leave = leave;
        return this;
    }

    public Button getShortBreak() {
        return shortBreak;
    }

    public PanelController setShortBreak(Button shortBreak) {
        this.shortBreak = shortBreak;
        return this;
    }

    public double getTabWidth() {
        return tabWidth;
    }

    public PanelController setTabWidth(double tabWidth) {
        this.tabWidth = tabWidth;
        return this;
    }

    public ArrayList<TabPane> getTabPaneArrayList() {
        return tabPaneArrayList;
    }

    public PanelController setTabPaneArrayList(ArrayList<TabPane> tabPaneArrayList) {
        this.tabPaneArrayList = tabPaneArrayList;
        return this;
    }

    public ObservableList<StaffMasterClass> getStaffMasterClassObservableList() {
        return staffMasterClassObservableList;
    }

    public PanelController setStaffMasterClassObservableList(ObservableList<StaffMasterClass> staffMasterClassObservableList) {
        this.staffMasterClassObservableList = staffMasterClassObservableList;
        return this;
    }

    public File getFile() {
        return file;
    }

    public PanelController setFile(File file) {
        this.file = file;
        return this;
    }

    public FileInputStream getFileInputStream() {
        return fileInputStream;
    }

    public PanelController setFileInputStream(FileInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
        return this;
    }

    public int getLength() {
        return length;
    }

    public PanelController setLength(int length) {
        this.length = length;
        return this;
    }
}
