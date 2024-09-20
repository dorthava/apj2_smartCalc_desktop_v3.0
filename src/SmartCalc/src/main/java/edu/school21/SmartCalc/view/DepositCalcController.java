package edu.school21.SmartCalc.view;

import edu.school21.SmartCalc.viewmodel.DepositViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DepositCalcController {

    private DepositViewModel depositViewModel;

    @FXML
    public TextField displayDepositAmount;

    @FXML
    public TextField displayDepositTerm;

    @FXML
    public DatePicker datePicker;

    @FXML
    public TextField displayInterestRate;

    @FXML
    public TextField displayTaxRate;

    @FXML
    public ComboBox<String> boxPeriodicityOfPayment;

    @FXML
    public RadioButton radioButtonCapitalizationOfInterest;

    @FXML
    public TextField displayAccruedInterest;

    @FXML
    public TextField displayTaxAmount;

    @FXML
    public TextField displayDepositAmountByTheEndOfTheTerm;

    @FXML
    private TableView<List<Object>> replenishmentTable;

    @FXML
    private TableColumn<List<Object>, Double> amountColumn;

    @FXML
    private TableColumn<List<Object>, LocalDate> dateColumn;

    @FXML
    private TableColumn<List<Object>, String> typeColumn;

    @FXML
    private TableColumn<List<Object>, String> deleteColumn;

    @FXML
    private TextField displayAddDepositOrWithdrawal;

    @FXML
    private DatePicker datePickerAddDepositOrWithdrawal;

    @FXML
    public void handleButtonAddWithdrawal() {
        try {
            depositViewModel.getTransactions().add(createTransactionRecord(Double.parseDouble(displayAddDepositOrWithdrawal.getText()), datePickerAddDepositOrWithdrawal.getValue(), "W"));
        } catch (Exception e) {
            MainController.showAlert("Error", e.getMessage());
        }
    }

    @FXML
    public void handleButtonAddReplenishment() {
        try {
            depositViewModel.getTransactions().add(createTransactionRecord(Double.parseDouble(displayAddDepositOrWithdrawal.getText()), datePickerAddDepositOrWithdrawal.getValue(), "R"));
        } catch (Exception e) {
            MainController.showAlert("Error", e.getMessage());
        }
    }

    @FXML
    public void handleButtonCalculate() {
        try {
            depositViewModel.calculate();
        } catch (Exception e) {
            MainController.showAlert("Error", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void handleButtonClear() {
        depositViewModel.clear();
    }

    @FXML
    private void initialize() {
        depositViewModel = new DepositViewModel();
        displayDepositAmount.textProperty().bindBidirectional(depositViewModel.displayDepositAmountProperty());
        MainController.applyIntegerValidation(displayDepositAmount);
        displayDepositTerm.textProperty().bindBidirectional(depositViewModel.displayDepositTermProperty());
        MainController.applyIntegerValidation(displayDepositTerm);
        datePicker.valueProperty().bindBidirectional(depositViewModel.depositDateProperty());
        displayInterestRate.textProperty().bindBidirectional(depositViewModel.displayInterestRateProperty());
        MainController.applyIntegerValidation(displayInterestRate);
        displayTaxRate.textProperty().bindBidirectional(depositViewModel.displayTaxRateProperty());
        MainController.applyIntegerValidation(displayTaxRate);
        boxPeriodicityOfPayment.valueProperty()
                .bindBidirectional(depositViewModel.boxPeriodicityOfPaymentProperty());
        boxPeriodicityOfPayment.setItems(FXCollections
                .observableArrayList("Every day", "In the end of the month", "By the end of the term"));
        radioButtonCapitalizationOfInterest.selectedProperty()
                .bindBidirectional(depositViewModel.radioButtonCapitalizationOfInterestProperty());
        displayAccruedInterest.textProperty().bindBidirectional(depositViewModel.displayAccruedInterestProperty());
        displayTaxAmount.textProperty().bindBidirectional(depositViewModel.displayTaxAmountProperty());
        displayDepositAmountByTheEndOfTheTerm.textProperty()
                .bindBidirectional(depositViewModel.displayDepositAmountByTheEndOfTheTermProperty());

        MainController.applyIntegerValidation(displayAddDepositOrWithdrawal);
        datePickerAddDepositOrWithdrawal.valueProperty().set(LocalDate.now());

        replenishmentTable.setItems(depositViewModel.getTransactions());
        amountColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>((Double) cellData.getValue().get(0)));
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>((LocalDate) cellData.getValue().get(1)));
        typeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>((String) cellData.getValue().get(2)));
        deleteColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>("Delete"));
        deleteColumn.setCellFactory(column -> new TableCell<List<Object>, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                if (empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText("Delete");
                    setOnMouseClicked(event -> getTableView().getItems().remove(getIndex()));
                }
            }
        });
    }

    private List<Object> createTransactionRecord(Double amount, LocalDate date, String type) {
        List<Object> record = new ArrayList<>();
        record.add(amount);
        record.add(date);
        record.add(type);
        return record;
    }
}
