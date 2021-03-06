package InkGroupProject.view;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class Main extends Application {
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        Image image = new Image("./InkGroupProject/icons/povertyHelp.png");
        stage.getIcons().add(image);
        LoginPage loginPage = new LoginPage();
        loginPage.start(stage);
    }

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
