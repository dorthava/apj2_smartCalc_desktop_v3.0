package edu.school21.SmartCalc.model;

public class CalculatorModel {
    static {
        try {
            System.loadLibrary("calc_core");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private native double evaluateExpression(String expression, String xValue);

    private native double[] buildAGraph(String expression, double[] borders);

    public String calculateExpression(String expression, String xValue) {
        try {
             double result = evaluateExpression(expression, xValue);
             String formattedResult = String.format("%.7f", result);
             formattedResult = formattedResult.replaceAll("0+$", "").replaceAll("\\.$", "");
             return formattedResult;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public double[] createGraph(String expression, double[] borders) {
        return buildAGraph(expression, borders);
    }
}
