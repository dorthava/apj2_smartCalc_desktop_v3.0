package edu.school21.SmartCalc.viewmodel;

import edu.school21.SmartCalc.model.CalculatorModel;
import edu.school21.SmartCalc.service.HistoryService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

public class CalculatorViewModel {

    private final CalculatorModel model;
    private final StringProperty display;
    private final StringProperty displayX;

    private final StringProperty displayMinX;
    private final StringProperty displayMaxX;
    private final StringProperty displayMinY;
    private final StringProperty displayMaxY;

    private final ObservableList<String> historyItems;

    private final HistoryService historyService;

    public CalculatorViewModel(CalculatorModel model) {
        this.model = model;
        this.display = new SimpleStringProperty("");
        this.displayX = new SimpleStringProperty("");
        this.displayMinX = new SimpleStringProperty("-500");
        this.displayMaxX = new SimpleStringProperty("500");
        this.displayMinY = new SimpleStringProperty("-500");
        this.displayMaxY = new SimpleStringProperty("500");

        historyItems = FXCollections.observableArrayList();

        historyService = new HistoryService();
        historyItems.addAll(historyService.getHistoryItems());
    }

    public ObservableList<String> getHistoryItems() {
        return historyItems;
    }

    public StringProperty displayProperty() {
        return display;
    }

    public StringProperty displayXProperty() {
        return displayX;
    }

    public StringProperty displayMinXProperty() {
        return displayMinX;
    }

    public StringProperty displayMaxXProperty() {
        return displayMaxX;
    }

    public StringProperty displayMinYProperty() {
        return displayMinY;
    }

    public StringProperty displayMaxYProperty() {
        return displayMaxY;
    }

    public void handleButtonClick(String text) {
        switch (text) {
            case "=":
                evaluateExpression();
                break;
            case "C":
                clearDisplay();
                break;
            case "⌫":
                deleteLastToken();
                break;
            default:
                appendToDisplay(text);
                break;
        }
    }

    private void appendToDisplay(String text) {
        display.set(display.get() + text);
    }

    private void clearDisplay() {
        display.set("");
    }

    public void clearHistory() {
        historyItems.clear();
        historyService.clearHistory();
    }

    private void deleteLastToken() {
        if (display.get().isEmpty()) return;
        display.set(display.get().substring(0, display.get().length() - 1));
    }

    private void evaluateExpression() {
        historyService.addHistoryItem(display.get());
        historyItems.add(display.get());
        String result = model.calculateExpression(optimizeExpression(display.get()), displayX.get());
        display.set(result);
    }

    public XYChart.Series<Number, Number> createGraph() {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("График");
        double[] result;
        try {
            double[] borders = new double[4];
            borders[0] = Double.parseDouble(displayMinX.get());
            borders[1] = Double.parseDouble(displayMaxX.get());
            borders[2] = Double.parseDouble(displayMinY.get());
            borders[3] = Double.parseDouble(displayMaxY.get());

            result = model.createGraph(optimizeExpression(display.get()), borders);
            for (int i = 0; i < result.length; ++i) {
                series.getData().add(new XYChart.Data<>(result[i], result[++i]));
            }
        } catch (Exception e) {
            display.set(e.getMessage());
        }
        return series;
    }

    private String optimizeExpression(String expression) {
        StringBuilder result = new StringBuilder(expression.length());
        for (int i = 0; i != expression.length(); ++i) {
            char c = expression.charAt(i);
            switch (c) {
                case '.':
                    result.append(',');
                    break;
                case '÷':
                    result.append('/');
                    break;
                case '×':
                    result.append('*');
                    break;
                case 'π':
                    result.append(Math.PI);
                    break;
                case 'ℇ':
                    result.append(Math.E);
                    break;
                case '%':
                    result.append("/100");
                    break;
                default:
                    result.append(c);
                    break;
            }
        }
        return result.toString();
    }
}
