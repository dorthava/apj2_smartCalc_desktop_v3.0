package edu.school21.SmartCalc.model;

import java.util.ArrayList;

public class CreditModel {
    private double totalPayment;

    private double overpaymentOnCredit;

    private final double[] monthlyPayment;

    private CreditType type;

    private double interestRate;

    private int term;

    private double totalAmount;

    public CreditModel() {
        this.monthlyPayment = new double[2];
    }

    public void calculationResults() {
        checkError();
        ResetOutput();
        if (type == CreditType.ANNUITY_TYPE) {
            calculationAnnuity();
        } else {
            calculationDifferentiated();
        }
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void setType(CreditType type) {
        this.type = type;
    }

    public double[] getMonthlyPayment() {
        return monthlyPayment;
    }

    public double getOverpaymentOnCredit() {
        return overpaymentOnCredit;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    private void ResetOutput() {
        monthlyPayment[0] = 0.;
        monthlyPayment[1] = 0.;
        overpaymentOnCredit = 0.;
        totalPayment = 0.;
    }

    private void calculationAnnuity() {
        double interestRate = calculationInterestRate();
        monthlyPayment[0] =
                totalAmount *
                        (interestRate + (interestRate / ((Math.pow(1 + interestRate, term)) - 1)));
        totalPayment = monthlyPayment[0] * term;
        overpaymentOnCredit = totalPayment - totalAmount;
    }

    private void calculationDifferentiated() {
        double interestRate = calculationInterestRate();
        double loanBalance = totalAmount;
        double debtPortion = totalAmount / term;

        ArrayList<Double> totalPaymentList = new ArrayList<>();
        for (int i = 0; i != term; ++i) {
            double percents = loanBalance * interestRate;
            loanBalance -= debtPortion;
            totalPaymentList.add(percents + debtPortion);
        }
        monthlyPayment[0] = totalPaymentList.get(0);
        monthlyPayment[1] = totalPaymentList.get(totalPaymentList.size() - 1);

        for (Double payment : totalPaymentList) {
            totalPayment += payment;
        }
        overpaymentOnCredit = totalPayment - totalAmount;
    }

    private void checkError() {
        if (totalAmount > 1000000000000. || totalAmount < 1.)
            throw new RuntimeException("The total loan amount is incorrect");
        if (term > 600 || term < 1) throw new RuntimeException("Term field is incorrect");
        if (interestRate < 0.01 || interestRate > 999)
            throw new RuntimeException("Wrong interest rate");
    }

    private double calculationInterestRate() {
        return interestRate / 100 / 12;
    }
}
