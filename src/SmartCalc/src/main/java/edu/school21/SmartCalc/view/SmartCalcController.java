package edu.school21.SmartCalc.view;

import edu.school21.SmartCalc.viewmodel.CalculatorViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SmartCalcController {
    private CalculatorViewModel calculatorViewModel;
    private GraphController graphController;

    @FXML
    private TextArea display;

    @FXML
    private TextField displayX;

    @FXML
    private ListView<String> historyListView;

    @FXML
    public void handleButtonClick(ActionEvent event) {
        calculatorViewModel.handleButtonClick(((Button) event.getSource()).getText());
    }

    @FXML
    public void handleClearHistoryButtonClick() {
        calculatorViewModel.clearHistory();
    }

    @FXML
    public void handleGraphModeButtonClick() {
        if(graphController == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plot.fxml"));
                Parent root = loader.load();

                graphController = loader.getController();
                graphController.setCalculatorViewModel(calculatorViewModel);
                Stage stage = new Stage();
                stage.setTitle("Graph");
                stage.setScene(new Scene(root));
                stage.show();

                stage.setOnCloseRequest(e -> {
                    graphController = null;
                });
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        graphController.updateGraph();
    }

    public void setCalculatorViewModel(CalculatorViewModel calculatorViewModel) {
        this.calculatorViewModel = calculatorViewModel;
        display.textProperty().bindBidirectional(calculatorViewModel.displayProperty());
        MainController.applyIntegerValidation(displayX);
        displayX.textProperty().bindBidirectional(calculatorViewModel.displayXProperty());
        historyListView.setItems(calculatorViewModel.getHistoryItems());
        historyListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                String selectedExpression = historyListView.getSelectionModel().getSelectedItem();
                if (selectedExpression != null) {
                    calculatorViewModel.displayProperty().set(selectedExpression);
                }
            }
        });
    }
}
