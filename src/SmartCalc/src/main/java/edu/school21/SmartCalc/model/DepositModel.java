package edu.school21.SmartCalc.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DepositModel {
    private LocalDate date;

    private double depositAmount;

    private int depositTerm;

    private double interestRate;

    private double taxRate;

    private short periodicityOfPayments;

    private boolean capitalizationOfInterest;

    private final Map<LocalDate, Double> replenishmentsMap;

    private double accruedInterest;

    private double taxAmount;

    private double endAmount;


    public DepositModel() {
        date = LocalDate.now();
        depositAmount = 0.;
        depositTerm = 0;
        interestRate = 0.;
        taxRate = 0.;
        periodicityOfPayments = 0;
        capitalizationOfInterest = false;
        replenishmentsMap = new HashMap<>();
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public void setDepositTerm(int depositTerm) {
        this.depositTerm = depositTerm;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public void setPeriodicityOfPayments(short periodicityOfPayments) {
        this.periodicityOfPayments = periodicityOfPayments;
    }

    public void setCapitalizationOfInterest(boolean capitalizationOfInterest) {
        this.capitalizationOfInterest = capitalizationOfInterest;
    }

    public double getEndAmount() {
        return endAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getAccruedInterest() {
        return accruedInterest;
    }


    public void addReplenishment(LocalDate date, double amount) {
        replenishmentsMap.merge(date, amount, Double::sum);
    }

    private double updateTotal(LocalDate date, double totalValue) throws Exception {
        if (replenishmentsMap.containsKey(date)) {
            double amount = replenishmentsMap.get(date);
            if (amount > 1000000000000.0 || amount < -1000000000000.0) {
                throw new Exception("The replenishment or withdrawal is incorrect");
            }
            totalValue += amount;
        }
        return totalValue;
    }

    public void calculateResult() throws Exception {
        checkError();
        accruedInterest = 0.;
        taxAmount = 0.;
        endAmount = 0.;
        double accruedBase = interestRate / (100 * 365);
        double accruedAdv = interestRate / (100 * 366);
        LocalDate dateEnd = date.plusMonths(depositTerm);

        LocalDate lastDate = date;
        double addValue = 0.;
        double totalValue = depositAmount;

        while (!date.isEqual(dateEnd)) {
            totalValue = updateTotal(date, totalValue);
            if (totalValue <= 0.) {
                throw new Exception("You've run out of money. The deposit is automatically closed. You will not receive any interest!:D");
            }
            if (date.isLeapYear()) {
                addValue += totalValue * accruedAdv;
            } else {
                addValue += totalValue * accruedBase;
            }
            if (periodicityOfPayments == 0) {
                accruedInterest += addValue;
                if (capitalizationOfInterest) {
                    totalValue += addValue;
                }
                addValue = 0.;
            }
            date = date.plusDays(1);
            if (lastDate.getMonthValue() != date.getMonthValue()) {
                lastDate = date;
                if (periodicityOfPayments == 1) {
                    accruedInterest += addValue;
                    if (capitalizationOfInterest) {
                        totalValue += addValue;
                    }
                    addValue = 0.;
                }
            }
        }
        if (periodicityOfPayments == 2 || periodicityOfPayments == 1) {
            accruedInterest += addValue;
            if (capitalizationOfInterest) {
                totalValue += addValue;
            }
        }
        taxAmount = taxRate * 0.01 * accruedInterest;
        endAmount = totalValue;
        if (!capitalizationOfInterest) endAmount += accruedInterest;
        replenishmentsMap.clear();
    }

    private void checkError() throws Exception {
        if (depositAmount > 1000000000000.0 || depositAmount < 1.0) {
            throw new Exception("The total loan amount is incorrect");
        }
        if (depositTerm > 365 || depositTerm < 1) {
            throw new Exception("Term field is incorrect");
        }
        if (interestRate < 0.01 || interestRate > 999) {
            throw new Exception("Wrong interest rate");
        }
        if (taxRate < 0.01 || taxRate > 999) {
            throw new Exception("Wrong tax rate");
        }
    }
}
