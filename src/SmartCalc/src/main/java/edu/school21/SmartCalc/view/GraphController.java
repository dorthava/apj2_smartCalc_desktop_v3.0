package edu.school21.SmartCalc.view;

import edu.school21.SmartCalc.viewmodel.CalculatorViewModel;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TextField;

public class GraphController {
    private CalculatorViewModel calculatorViewModel;

    @FXML
    private LineChart<Number, Number> graph;

    @FXML
    private TextField xMinField;
    @FXML
    private TextField xMaxField;
    @FXML
    private TextField yMinField;
    @FXML
    private TextField yMaxField;

    public void setCalculatorViewModel(CalculatorViewModel calculatorViewModel) {
        this.calculatorViewModel = calculatorViewModel;

        xMinField.textProperty().bindBidirectional(calculatorViewModel.displayMinXProperty());
        xMaxField.textProperty().bindBidirectional(calculatorViewModel.displayMaxXProperty());
        yMinField.textProperty().bindBidirectional(calculatorViewModel.displayMinYProperty());
        yMaxField.textProperty().bindBidirectional(calculatorViewModel.displayMaxYProperty());
    }

    public void updateGraph() {
        graph.getData().clear();
        graph.getData().add(calculatorViewModel.createGraph());
    }
}
