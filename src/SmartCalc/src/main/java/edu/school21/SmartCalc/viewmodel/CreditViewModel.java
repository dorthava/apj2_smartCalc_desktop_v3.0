package edu.school21.SmartCalc.viewmodel;

import edu.school21.SmartCalc.model.CreditModel;
import edu.school21.SmartCalc.model.CreditType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CreditViewModel {
    private final CreditModel creditModel;

    private final StringProperty displayTotalAmount;

    private final StringProperty displayTerm;

    private final StringProperty displayInterestRate;

    private final StringProperty displayMonthlyPayment;

    private final StringProperty displayOverpaymentOfCredit;

    private final StringProperty displayTotalPayment;

    private final BooleanProperty annuityBooleanProperty;

    public CreditViewModel() {
        creditModel = new CreditModel();
        this.displayTotalAmount = new SimpleStringProperty("");
        this.displayTerm = new SimpleStringProperty("");
        this.displayInterestRate = new SimpleStringProperty("");
        this.displayMonthlyPayment = new SimpleStringProperty("");
        this.displayOverpaymentOfCredit = new SimpleStringProperty("");
        this.displayTotalPayment = new SimpleStringProperty("");
        this.annuityBooleanProperty = new SimpleBooleanProperty(true);
    }

    public void calculateResult() {
        creditModel.setTerm(Integer.parseInt(displayTerm.get()));
        creditModel.setInterestRate(Double.parseDouble(displayInterestRate.get()));
        creditModel.setTotalAmount(Double.parseDouble(displayTotalAmount.get()));
        CreditType creditType = annuityBooleanProperty.get() ? CreditType.ANNUITY_TYPE : CreditType.DIFFERENTIATED_TYPE;
        creditModel.setType(creditType);

        creditModel.calculationResults();

        double[] monthlyPayment = creditModel.getMonthlyPayment();
        if(creditType == CreditType.ANNUITY_TYPE) {
            displayMonthlyPayment.set(String.format("%.2f руб.", monthlyPayment[0]));
        } else {
            displayMonthlyPayment.set(String.format("%.2f...%.2f руб.", monthlyPayment[0], monthlyPayment[1]));
        }
        displayOverpaymentOfCredit.set(String.format("%.2f руб.", creditModel.getOverpaymentOnCredit()));
        displayTotalPayment.set(String.format("%.2f руб.", creditModel.getTotalPayment()));
    }

    public void clearDisplays() {
        this.displayTotalAmount.set("");
        this.displayTerm.set("");
        this.displayInterestRate.set("");
        this.displayMonthlyPayment.set("");
        this.displayOverpaymentOfCredit.set("");
        this.displayTotalPayment.set("");
        this.annuityBooleanProperty.set(true);
    }

    public StringProperty displayTotalAmountProperty() {
        return displayTotalAmount;
    }

    public StringProperty displayTermProperty() {
        return displayTerm;
    }

    public StringProperty displayInterestRateProperty() {
        return displayInterestRate;
    }

    public StringProperty displayMonthlyPaymentProperty() {
        return displayMonthlyPayment;
    }

    public StringProperty displayOverpaymentOfCreditProperty() {
        return displayOverpaymentOfCredit;
    }

    public StringProperty displayTotalPaymentProperty() {
        return displayTotalPayment;
    }

    public BooleanProperty annuityBooleanProperty() {
        return annuityBooleanProperty;
    }

}
