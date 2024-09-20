package edu.school21.SmartCalc.view;

import edu.school21.SmartCalc.model.CalculatorModel;
import edu.school21.SmartCalc.viewmodel.CalculatorViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainController {
    @FXML
    private StackPane mainStackPane;

    private Node basicCalculatorPane;

    private Node creditCalculatorPane;

    private Node depositCalculatorPane;

    private boolean onAboutTheProgram;

    public static void applyIntegerValidation(TextField textField) {
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("\\d*(,\\d*)?")) {
                showAlert("Invalid input", "Please enter only whole numbers.");
                textField.setText(oldText);
            }
        });
    }

    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void initialize() {

        try {
            URL xmlUrlBasic = getClass().getResource("/fxml/calculator.fxml");
            FXMLLoader loader = new FXMLLoader(xmlUrlBasic);
            basicCalculatorPane = loader.load();
            SmartCalcController smartCalcController = loader.getController();
            smartCalcController.setCalculatorViewModel(new CalculatorViewModel(new CalculatorModel()));

            URL xmlUrlCredit = getClass().getResource("/fxml/credit.fxml");
            loader = new FXMLLoader(xmlUrlCredit);
            creditCalculatorPane = loader.load();

            URL xmlUrlDeposit = getClass().getResource("/fxml/deposit.fxml");
            loader = new FXMLLoader(xmlUrlDeposit);
            depositCalculatorPane = loader.load();

            mainStackPane.getChildren().addAll(basicCalculatorPane, creditCalculatorPane, depositCalculatorPane);
            showBasicCalculator();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void showBasicCalculator() {
        basicCalculatorPane.setVisible(true);
        creditCalculatorPane.setVisible(false);
        depositCalculatorPane.setVisible(false);
    }

    @FXML
    private void showCreditCalculator() {
        basicCalculatorPane.setVisible(false);
        creditCalculatorPane.setVisible(true);
        depositCalculatorPane.setVisible(false);
    }

    @FXML
    private void showDepositCalculator() {
        basicCalculatorPane.setVisible(false);
        creditCalculatorPane.setVisible(false);
        depositCalculatorPane.setVisible(true);
    }

    @FXML
    private void showAboutProgram() {
        if(!onAboutTheProgram) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/about_the_program.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("About the program");
                stage.setScene(new Scene(root));
                stage.show();
                onAboutTheProgram = true;
                stage.setOnCloseRequest(e -> {
                    onAboutTheProgram = false;
                });
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}