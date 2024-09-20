package edu.school21.SmartCalc.viewmodel;

import edu.school21.SmartCalc.model.DepositModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.time.LocalDate;
import java.util.List;

public class DepositViewModel {

    private final DepositModel depositModel;

    private final StringProperty displayDepositAmount;

    private final StringProperty displayDepositTerm;

    private final ObjectProperty<LocalDate> depositDate;

    private final StringProperty displayInterestRate;

    private final StringProperty displayTaxRate;

    private final StringProperty boxPeriodicityOfPayment;

    private final BooleanProperty radioButtonCapitalizationOfInterest;

    private final StringProperty displayAccruedInterest;

    private final StringProperty displayTaxAmount;

    private final StringProperty displayDepositAmountByTheEndOfTheTerm;

    private final ObservableList<List<Object>> transactions;

    public DepositViewModel() {
        depositModel = new DepositModel();
        displayDepositAmount = new SimpleStringProperty("");
        displayDepositTerm = new SimpleStringProperty("");
        depositDate = new SimpleObjectProperty<>(LocalDate.now());
        displayInterestRate = new SimpleStringProperty("");
        displayTaxRate = new SimpleStringProperty("");
        boxPeriodicityOfPayment = new SimpleStringProperty("Every day");
        radioButtonCapitalizationOfInterest = new SimpleBooleanProperty(false);
        displayAccruedInterest = new SimpleStringProperty("");
        displayTaxAmount = new SimpleStringProperty("");
        displayDepositAmountByTheEndOfTheTerm = new SimpleStringProperty("");
        transactions = FXCollections.observableArrayList();
    }

    public void calculate() throws Exception {
        depositModel.setDepositAmount(Double.parseDouble(displayDepositAmount.get()));
        depositModel.setDepositTerm(Integer.parseInt(displayDepositTerm.get()));
        depositModel.setDate(depositDate.get());
        depositModel.setInterestRate(Double.parseDouble(displayInterestRate.get()));
        depositModel.setTaxRate(Double.parseDouble(displayTaxRate.get()));
        depositModel.setPeriodicityOfPayments(handlePeriodicityOfPayment(boxPeriodicityOfPayment.get()));
        depositModel.setCapitalizationOfInterest(radioButtonCapitalizationOfInterest.get());
        addTransactions();
        depositModel.calculateResult();
        displayAccruedInterest.set(String.format("%.2f", depositModel.getAccruedInterest()));
        displayTaxAmount.set(String.format("%.2f", depositModel.getTaxAmount()));
        displayDepositAmountByTheEndOfTheTerm.set(String.format("%.2f", depositModel.getEndAmount()));
    }

    public void clear() {
        displayDepositAmount.set("");
        displayDepositTerm.set("");
        depositDate.set(LocalDate.now());
        displayInterestRate.set("");
        displayTaxRate.set("");
        boxPeriodicityOfPayment.set("Every day");
        radioButtonCapitalizationOfInterest.set(false);
        displayAccruedInterest.set("");
        displayTaxAmount.set("");
        displayDepositAmountByTheEndOfTheTerm.set("");
        transactions.clear();
    }

    public StringProperty displayDepositAmountProperty() {
        return displayDepositAmount;
    }

    public StringProperty displayDepositTermProperty() {
        return displayDepositTerm;
    }

    public StringProperty displayInterestRateProperty() {
        return displayInterestRate;
    }

    public StringProperty displayTaxRateProperty() {
        return displayTaxRate;
    }
    public StringProperty boxPeriodicityOfPaymentProperty() {
        return boxPeriodicityOfPayment;
    }

    public BooleanProperty radioButtonCapitalizationOfInterestProperty() {
        return radioButtonCapitalizationOfInterest;
    }

    public StringProperty displayAccruedInterestProperty() {
        return displayAccruedInterest;
    }

    public StringProperty displayTaxAmountProperty() {
        return displayTaxAmount;
    }

    public StringProperty displayDepositAmountByTheEndOfTheTermProperty() {
        return displayDepositAmountByTheEndOfTheTerm;
    }

    public ObjectProperty<LocalDate> depositDateProperty() {
        return depositDate;
    }

    public ObservableList<List<Object>> getTransactions() {
        return transactions;
    }

    private short handlePeriodicityOfPayment(String word) {
        short result = 0;
        switch (word) {
            case "In the end of the month":
                result = 1;
                break;
            case "By the end of the term":
                result = 2;
                break;
        }
        return result;
    }

    private void addTransactions() {
        for(List<Object> row: transactions) {
            if(row.get(2).equals("R")) {
                depositModel.addReplenishment((LocalDate) row.get(1), (Double) row.get(0));
            } else {
                depositModel.addReplenishment((LocalDate) row.get(1), -(Double) row.get(0));
            }
        }
    }
}
