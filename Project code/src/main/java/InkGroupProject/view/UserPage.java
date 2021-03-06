package InkGroupProject.view;

import InkGroupProject.model.Database;
import InkGroupProject.model.UserSession;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.HashMap;

public class UserPage implements IScene {
    private GridPane root;
    private Database db;
    private UserSession currentUser;

    /**
     * User page scene displaying donation records and user controls.
     */
    public UserPage() {
        init();
    }

    private void init() {
        db = Database.getInstance(":resource:InkGroupProject/db/database.db");
        currentUser = UserSession.getInstance();

        root = new GridPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setVgap(5);
        root.setHgap(5);

        Label donationHistory = new Label("Donation History");
        donationHistory.setFont(new Font("Arial", 16));
        root.add(donationHistory, 0, 0);

        HashMap<String,Integer> donations = db.getDonatedMoneyByUser(currentUser.getId());
        TableView table = createTable(donations);
        root.add(table, 0, 1, 3, 1);

        // Go back to the map
        Hyperlink goBack = new Hyperlink("<- Go back");
        root.add(goBack, 0, 2);
        goBack.setOnAction(e -> {
            Map mapScene = new Map();
            mapScene.start(Main.getStage());
        });

        Button userSettingsButton = new Button("Account settings");
        root.add(userSettingsButton, 1, 2);
        userSettingsButton.setOnAction(e -> {
            UserSettings userSettingsScene = new UserSettings();
            userSettingsScene.start(Main.getStage());
        });

        Button logoutButton = new Button("Logout");
        logoutButton.setTextFill(Color.RED);
        root.add(logoutButton, 2, 2);
        logoutButton.setOnAction(e -> {
            currentUser.logout();
            LoginPage loginPageScene = new LoginPage();
            loginPageScene.start(Main.getStage());
        });
    }

    /**
     * Sets the scene ready for display.
     * @param stage The primary stage of the application.
     */
    @Override
    public void start(Stage stage) {
        Scene userPageScene = new Scene(root);
        stage.setTitle("User page");
        stage.setResizable(false);
        stage.setScene(userPageScene);
        stage.show();
    }

    /**
     * @return the scene GridPane root.
     */
    public Parent getRoot() {
        return root;
    }

    private TableView createTable(HashMap<String,Integer> donations) {
        TableColumn<HashMap.Entry<String,Integer>, String>  countryColumn     = new TableColumn("Country");
        TableColumn<HashMap.Entry<String,Integer>, Integer> donationSumColumn = new TableColumn("Donation Total");
        TableColumn<HashMap.Entry<String,Integer>, Integer> healthyMeals      = new TableColumn("Healthy Meals");

        countryColumn.setCellValueFactory(donationsParam -> new SimpleObjectProperty(donationsParam.getValue().getKey()));
        donationSumColumn.setCellValueFactory(donationsParam -> {
            int donationTotal = donationsParam.getValue().getValue();
            String donationTotalString = String.format("%,d", donationTotal);
            return new SimpleObjectProperty(donationTotalString);
        });
        healthyMeals.setCellValueFactory(donationsParam -> {
            int donationTotal = donationsParam.getValue().getValue();
            String country = donationsParam.getValue().getKey();
            double dietCost = db.getDietCost(country);
            String healthyMealsString = String.format("%,d", (int)(donationTotal / dietCost));
            return new SimpleObjectProperty(healthyMealsString);
        });

        ObservableList<HashMap.Entry<String,Integer>> items = FXCollections.observableArrayList(donations.entrySet());

        TableView table = new TableView(items);
        table.getColumns().setAll(countryColumn, donationSumColumn, healthyMeals);
        return table;
    }
}
