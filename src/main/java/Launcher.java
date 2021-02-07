import Hospital.Controllers.Super;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import securityandtime.AesCipher;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static Hospital.Controllers.settings.*;


public class Launcher extends Application {


    private static final int SPLASH_WIDTH = 676;
    private static final int SPLASH_HEIGHT = 227;
    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private Stage mainStage;

    public static void main(String[] args) {
        launch(args);

    }

    public static int getSplashWidth() {
        return SPLASH_WIDTH;
    }

    public static int getSplashHeight() {
        return SPLASH_HEIGHT;
    }

    @Override
    public void init() {
        pushToApi();
        ImageView splash = new ImageView(new Image(
                APPLICATION_ICON
        ));
        splash.setFitWidth(SPLASH_WIDTH - 20);
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(SPLASH_WIDTH - 20);
        progressText = new Label();
        splashLayout = new VBox();
        splashLayout.getChildren().addAll(splash, loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);
        splashLayout.setStyle(
                "-fx-padding: 5; " +
                        "-fx-background-color: #a9ff83; " +
                        "-fx-border-width:5; " +
                        "-fx-border-color: " +
                        "linear-gradient(to right, rgba(110, 243, 255, 0.4), rgba(255, 56, 87, 0.38))"
        );
        splashLayout.setEffect(new DropShadow());
    }

    private void pushToApi() {
        ApiPushControl apiPushControl = new ApiPushControl();
        apiPushControl.periodicBackUp();
    }

    @Override
    public void start(final Stage initStage) {
        final Task<ObservableList<String>> listTask = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws InterruptedException {
                ObservableList<String> observableArrayList =
                        FXCollections.observableArrayList();
                ObservableList<String> tasksToDo =
                        FXCollections.observableArrayList(
                                "Initializing modules", "Setting up files", "Opening Files", "Initiating database", "Synchronising databases"
                        );

                updateMessage("Running task . . .");
                for (int i = 0; i < tasksToDo.size(); i++) {
                    Thread.sleep(4);
                    updateProgress(i + 1, tasksToDo.size());
                    String nextTask = tasksToDo.get(i);
                    observableArrayList.add(nextTask);
                    updateMessage("Running task . . . " + nextTask);
                    if (i == 3) {
//                     create sqlite tables and db
                    } else if (i == 1) {
//                     create first user in db
                        Super super_ = new Super();
                        Connection connection = super_.getHospitalDbConnection();
                        try {
                            Statement s = connection.createStatement();
                            ResultSet r = s.executeQuery("SELECT COUNT(*) AS rowcount FROM users");
                            r.next();
                            int count = r.getInt("rowcount");
                            r.close();
                            System.out.println("MyTable has " + count + " row(s).");
                            if (count == 0) {
                                System.out.println("executing");
                                String[] recs = {"name", "email", "password", "hospital", "status", "description", "userclearancelevel"};
                                String[] values = {AesCipher.encrypt(encryptionkey, initVector, "administrator")
                                        , AesCipher.encrypt(encryptionkey, initVector, "admin@company.com"),
                                        AesCipher.encrypt(encryptionkey, initVector, "admin"),
                                        AesCipher.encrypt(encryptionkey, initVector, "Hospital"),
                                        "active"
                                        , "A talented admin account",
                                        AesCipher.encrypt(encryptionkey, initVector, "admin")
                                };
                                try {
                                    super_.insertIntoTable("users", recs, values);
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                            }

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    } else if (i == 0) {

                    }
                }
                Thread.sleep(4);
                updateMessage("All TASKS COMPLETED.");

                return observableArrayList;
            }


        };

        showSplash(
                initStage,
                listTask,
                () -> {
                    try {
                        showMainStage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        new Thread(listTask).start();
    }

    private void showMainStage(
    ) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Hospital/views/basic/LoginScene.fxml"));
        FadeTransition ft = new FadeTransition(Duration.millis(2500), root);
        ft.setFromValue(0.3);
        ft.setToValue(1.0);
        ft.play();
        mainStage = new Stage(StageStyle.DECORATED);
        mainStage.getIcons().add(new Image(
                APPLICATION_ICON
        ));
        mainStage.setTitle(appName.toUpperCase() + " HOSPITAL MANAGEMENT SYSTEM ".toUpperCase());
//        mainStage.setMaxHeight(700);
//        mainStage.setMaxWidth(1200);
        mainStage.setResizable(false);
        mainStage.setScene(new Scene(root));
        mainStage.show();
    }

    private void showSplash(
            @org.jetbrains.annotations.NotNull final Stage initStage,
            Task<?> task,
            InitCompletionHandler initCompletionHandler
    ) {

        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            }
        });

        Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
        initStage.setScene(splashScene);
        initStage.getIcons().add(new Image(APPLICATION_ICON));
        initStage.setResizable(false);
        initStage.setTitle(appName);
        initStage.setHeight(SPLASH_HEIGHT);
        initStage.setWidth(SPLASH_WIDTH);
        initStage.initStyle(StageStyle.TRANSPARENT);
        initStage.setAlwaysOnTop(true);
        initStage.show();
    }

    public Pane getSplashLayout() {
        return splashLayout;
    }

    public Launcher setSplashLayout(Pane splashLayout) {
        this.splashLayout = splashLayout;
        return this;
    }

    public ProgressBar getLoadProgress() {
        return loadProgress;
    }

    public Launcher setLoadProgress(ProgressBar loadProgress) {
        this.loadProgress = loadProgress;
        return this;
    }

    public Label getProgressText() {
        return progressText;
    }

    public Launcher setProgressText(Label progressText) {
        this.progressText = progressText;
        return this;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public Launcher setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
        return this;
    }

    public interface InitCompletionHandler {
        void complete();
    }
}