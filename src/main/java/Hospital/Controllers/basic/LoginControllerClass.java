package Hospital.Controllers.basic;

import Hospital.Controllers.Super;
import Hospital.Controllers.settings;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import securityandtime.AesCipher;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

import static Hospital.Controllers.settings.*;


public class LoginControllerClass extends Super implements Initializable {
    public AnchorPane panel;
    public TextField name;
    public PasswordField password;
    public Button login;
    public Button help;
    public Label message;
    public Label title;
    public ImageView logo1;
    public ImageView logo2;
    public ImageView logo3;

    ToastController toastController = new ToastController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            clickListeners();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        setLogos();
        title.setText(appName + " Login");
        LabelInvisible(message);
    }

    private void setLogos() {
        logo1.setImage(new Image(APPLICATION_ICON));
        logo2.setImage(new Image(APPLICATION_ICON));
        logo3.setImage(new Image(APPLICATION_ICON));

    }

    private void enterPressed() {

        name.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                loginValidation();
            }
        });
        password.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                loginValidation();
            }
        });

    }

    private void clickListeners() throws SocketException {

        login.setOnMouseClicked(event -> {
            loginValidation();

        });

        help.setOnMousePressed(new EventHandler<MouseEvent>() {
            //            got to help page


            @Override
            public void handle(MouseEvent event) {


                try {
                    if (isInternetAvailable()) {

                        panel.getChildren().removeAll();
                        try {
                            panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Hospital/views/basic/Help.fxml")))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "CONNECTION ERROR", "Looks Like your machine is offline");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        });
    }

    private void loginValidation() {
        if (name.getText().isEmpty() || password.getText().isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(),
                    "FILL ALL FIELDS", "PLEASE FILL ALL FIELDS");

        } else {
            login();

        }
    }

    //login method
    private void login() {
//        get input text
        String emailSubmit = AesCipher.encrypt(encryptionkey, initVector, name.getText());
        String pass = AesCipher.encrypt(encryptionkey, initVector, password.getText());
        try {
//            create a connection
//            users:id,name,email,password,status,userclearancelevel

            PreparedStatement statement = hospitalDbConnection.prepareStatement("SELECT * FROM users WHERE email=? OR name=?");
            statement.setString(1, emailSubmit);
            statement.setString(2, emailSubmit);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    if (Objects.equals(resultSet.getString("status"), "active")) {
                        //if account exists and password matches hashed password
                        if ((resultSet.getString("password").equals(pass))) {
                            settings.user.put("user", AesCipher.decrypt(encryptionkey, resultSet.getString("email")));
                            settings.name.put("username", AesCipher.decrypt(encryptionkey, resultSet.getString("name")));
                            settings.id.put("userid", resultSet.getString("id"));
                            settings.hospital.put("hospital_name", AesCipher.decrypt(encryptionkey, resultSet.getString("hospital")));

                            if (AesCipher.decrypt(encryptionkey, resultSet.getString("userclearancelevel")).equalsIgnoreCase("admin")) {
//if user account is admin
                                panel.getChildren().removeAll();
                                try {
//                                    go to admin panel
                                    settings.login.put("loggedinasadmin", true);

                                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Hospital/views/Admins/panel.fxml")))));
                                    assert false;
//                                    work as sessions and hold user session data

                                    if (resultSet.getString("email").equals(resultSet.getString("password"))) {
                                        settings.changepassword.put("change", true);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else if (AesCipher.decrypt(encryptionkey, resultSet.getString("userclearancelevel")).equalsIgnoreCase("lab tech")) {
//if user account is lab technician
                                panel.getChildren().removeAll();
                                try {
//                                    go to admin panel
                                    settings.login.put("loggedinaslabtech", true);

//                                    Hospital/views/Physicians/panel.fxml
//                                    fixme error
                                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Hospital/views/labtechnician/panel.fxml")))));
                                    assert false;
//                                    work as sessions and hold user session data

                                    if (resultSet.getString("email").equals(resultSet.getString("password"))) {
                                        settings.changepassword.put("change", true);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else if (AesCipher.decrypt(encryptionkey, resultSet.getString("userclearancelevel")).equalsIgnoreCase("doctor")) {
//                                user is not admin go to doctor panel
                                panel.getChildren().removeAll();
                                try {
                                    settings.login.put("loggedinasdoctor", true);
                                    settings.user.put("location", AesCipher.decrypt(encryptionkey, resultSet.getString("hospital")));
                                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Hospital/views/Physicians/panel.fxml")))));
                                    assert false;
                                    //                                    work as sessions and hold user session data
                                    assert false;
//                                    work as sessions and hold user session data

                                    if (resultSet.getString("email").equals(resultSet.getString("password"))) {
                                        settings.changepassword.put("change", true);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else if (AesCipher.decrypt(encryptionkey, resultSet.getString("userclearancelevel")).equalsIgnoreCase("receptionist")) {
//                                user is not admin go to receptionist panel
                                panel.getChildren().removeAll();
                                try {
                                    settings.login.put("loggedinasreceptionist", true);

                                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Hospital/views/Receptionist/panel.fxml")))));
                                    assert false;
                                    //                                    work as sessions and hold user session data
                                    assert false;
//                                    work as sessions and hold user session data
                                    if (resultSet.getString("email").equals(resultSet.getString("password"))) {
                                        settings.changepassword.put("change", true);
                                    }


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
//                            if passwords do not match
                            showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(),
                                    "WRONG PASSWORD!!", "ENTER THE CORRECT PASSWORD");

                        }
                    } else {
                        showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(),
                                "You are " + resultSet.getString("status") + "!!", "Your account is " + resultSet.getString("status") + "!!".toUpperCase());

                    }
                }

            } else {
                showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(),
                        "WRONG NAME/EMAIL !!", "PLEASE RE-ENTER A VALID USER NAME OR EMAIL");
//name or email does not exist
            }

        } catch (Exception e) {
            e.printStackTrace();
//            toastController.showToast("CHECK YOUR CONNECTION!!",panel,6000,1000,1000);
            message.setText("CHECK YOUR CONNECTION!!");
            message.setVisible(true);
            LabelInvisible(message);
        }
    }


//    getters and setters for the class variables


    public AnchorPane getPanel() {
        return panel;
    }

    public LoginControllerClass setPanel(AnchorPane panel) {
        this.panel = panel;
        return this;
    }

    public TextField getName() {
        return name;
    }

    public LoginControllerClass setName(TextField name) {
        this.name = name;
        return this;
    }

    public PasswordField getPassword() {
        return password;
    }

    public LoginControllerClass setPassword(PasswordField password) {
        this.password = password;
        return this;
    }

    public Button getLogin() {
        return login;
    }

    public LoginControllerClass setLogin(Button login) {
        this.login = login;
        return this;
    }

    public Button getHelp() {
        return help;
    }

    public LoginControllerClass setHelp(Button help) {
        this.help = help;
        return this;
    }

    public Label getMessage() {
        return message;
    }

    public LoginControllerClass setMessage(Label message) {
        this.message = message;
        return this;
    }

    public Label getTitle() {
        return title;
    }

    public LoginControllerClass setTitle(Label title) {
        this.title = title;
        return this;
    }

    public ImageView getLogo1() {
        return logo1;
    }

    public LoginControllerClass setLogo1(ImageView logo1) {
        this.logo1 = logo1;
        return this;
    }

    public ImageView getLogo2() {
        return logo2;
    }

    public LoginControllerClass setLogo2(ImageView logo2) {
        this.logo2 = logo2;
        return this;
    }

    public ImageView getLogo3() {
        return logo3;
    }

    public LoginControllerClass setLogo3(ImageView logo3) {
        this.logo3 = logo3;
        return this;
    }

    public ToastController getToastController() {
        return toastController;
    }

    public LoginControllerClass setToastController(ToastController toastController) {
        this.toastController = toastController;
        return this;
    }
}
