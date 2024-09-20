package edu.school21.SmartCalc.view;

import edu.school21.SmartCalc.viewmodel.CreditViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class CreditCalcController {

    private CreditViewModel creditViewModel;

    @FXML
    private TextField displayTotalAmount;

    @FXML
    private TextField displayTerm;

    @FXML
    private TextField displayInterestRate;

    @FXML
    private RadioButton radioButtonAnnuity;

    @FXML
    private RadioButton radioButtonDifferentiated;

    @FXML
    private TextField displayMonthlyPayment;

    @FXML
    private TextField displayOverpaymentOfCredit;

    @FXML
    private TextField displayTotalPayment;

    @FXML
    public void initialize() {
        creditViewModel = new CreditViewModel();
        displayTotalAmount.textProperty().bindBidirectional(creditViewModel.displayTotalAmountProperty());
        MainController.applyIntegerValidation(displayTotalAmount);
        displayTerm.textProperty().bindBidirectional(creditViewModel.displayTermProperty());
        MainController.applyIntegerValidation(displayTerm);
        displayInterestRate.textProperty().bindBidirectional(creditViewModel.displayInterestRateProperty());
        MainController.applyIntegerValidation(displayInterestRate);
        displayMonthlyPayment.textProperty().bindBidirectional(creditViewModel.displayMonthlyPaymentProperty());
        displayOverpaymentOfCredit.textProperty().bindBidirectional(creditViewModel.displayOverpaymentOfCreditProperty());
        displayTotalPayment.textProperty().bindBidirectional(creditViewModel.displayTotalPaymentProperty());

        ToggleGroup toggleGroup = new ToggleGroup();
        radioButtonAnnuity.setToggleGroup(toggleGroup);
        radioButtonDifferentiated.setToggleGroup(toggleGroup);

        radioButtonAnnuity.selectedProperty().bindBidirectional(creditViewModel.annuityBooleanProperty());
    }

    @FXML
    private void handleButtonCalculateClick() {
        try {
            creditViewModel.calculateResult();
        } catch (Exception e) {
            MainController.showAlert("Credit calculator alert", e.getMessage());
        }
    }

    @FXML
    private void handleButtonResetClick() {
        creditViewModel.clearDisplays();
    }
}
