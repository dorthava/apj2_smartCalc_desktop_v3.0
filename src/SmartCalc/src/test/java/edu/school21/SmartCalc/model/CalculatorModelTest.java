package edu.school21.SmartCalc.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorModelTest {
    private static CalculatorModel calculatorModel;

    @BeforeAll
    public static void setUp() {
        calculatorModel = new CalculatorModel();
    }

    @Test
    public void testAddition() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("2 + 3", ""));
        assertEquals(5.0, result, 0.0000001);
    }

    @Test
    public void testSubtraction() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("5 - 3", ""));
        assertEquals(2.0, result, 0.0000001);
    }

    @Test
    public void testMultiplication() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("5 * 4", ""));
        assertEquals(20.0, result, 0.0000001);
    }

    @Test
    public void testDivision() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("5 / 3", ""));
        assertEquals(1.666666667, result, 0.0000001);
    }

    @Test
    public void testModulus() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("10 mod 3", ""));
        assertEquals(1.0, result, 0.0000001);
    }

    @Test
    public void testNumberE() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("1e4", ""));
        assertEquals(10000., result, 0.0000001);
    }

    @Test
    public void testNumberPlusE() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("1,232e+4", ""));
        assertEquals(12320., result, 0.0000001);
    }

    @Test
    public void testNumberMinusE() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("1232e-4", ""));
        assertEquals(0.12320, result, 0.0000001);
    }

    @Test
    public void testComplexExpression() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("2 + 3 * (5 - 2)^2 / sqrt(16)", ""));
        assertEquals(8.75, result, 0.0000001);
    }

    @Test
    public void testTrigonometricExpressionSin() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("sin(5)", ""));
        assertEquals(-0.95892427466, result, 0.0000001);
    }

    @Test
    public void testTrigonometricExpressionCos() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("cos(5)", ""));
        assertEquals(0.2836622, result, 0.0000001);
    }

    @Test
    public void testTrigonometricExpressionTan() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("tan(5)", ""));
        assertEquals(-3.3805150, result, 0.0000001);
    }

    @Test
    public void testTrigonometricIdentity() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("sin(1)^2 + cos(1)^2", ""));
        assertEquals(1., result, 0.0000001);
    }

    @Test
    public void testTrigonometricDegrees() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("2^2^3", ""));
        assertEquals(256., result, 0.0000001);
    }

    @Test
    public void testTrigonometricExpressionAsin() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("asin(0,5)", ""));
        assertEquals(0.5235988, result, 0.0000001);
    }

    @Test
    public void testTrigonometricExpressionAcos() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("acos(0,5)", ""));
        assertEquals(1.0471976, result, 0.0000001);
    }

    @Test
    public void testTrigonometricExpressionAtan() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("atan(0,5)", ""));
        assertEquals(0.4636476, result, 0.0000001);
    }

    @Test
    public void testExpressionLn() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("ln(5)", ""));
        assertEquals(1.6094379, result, 0.0000001);
    }

    @Test
    public void testExpressionLog() {
        double result = Double.parseDouble(calculatorModel.calculateExpression("log(10)", ""));
        assertEquals(1, result, 0.0000001);
    }

    @Test
    public void testExpressionGraphValuesTest() {
        double[] borders = new double[] {50, 100, 50, 100};
        double[] result = calculatorModel.createGraph("x", borders);
        double step = (borders[1] - borders[0]) / 1000.0;
        double tempValue = borders[0];
        for (int i = 0; i < result.length; ++i) {
            assertEquals(result[i++], tempValue, 0.000001);
            assertEquals(result[i], tempValue, 0.000001);
            tempValue += step;
        }
    }

    @Test
    public void testExpressionIncorrect() {
        String result = calculatorModel.calculateExpression("..", "");
        assertEquals("Error: Unknown function or token", result);
    }

    @Test
    public void testExpressionUnexpectedOperator() {
        String result = calculatorModel.calculateExpression("12++12", "");
        assertEquals("Error: Unexpected operator", result);
    }

    @Test
    public void testExpressionEmptyParentheses1() {
        String result = calculatorModel.calculateExpression("()", "");
        assertEquals("Error: Incorrect initialization of parentheses", result);
    }

    @Test
    public void testExpressionEmptyParentheses2() {
        String result = calculatorModel.calculateExpression("(12", "");
        assertEquals("Error: Unmatched opening bracket", result);
    }

    @Test
    public void testExpressionEmptyParentheses3() {
        String result = calculatorModel.calculateExpression("12(", "");
        assertEquals("Error: Expression cannot end with an operator", result);
    }

    @Test
    public void testExpressionEmptyParentheses4() {
        String result = calculatorModel.calculateExpression(")12", "");
        assertEquals("Error: Unmatched closing bracket", result);
    }

    @Test
    public void testExpressionIncorrectModError1() {
        String result = calculatorModel.calculateExpression("1,2mod4", "");
        assertEquals("Error: Modulus division is only defined for integers", result);
    }

    @Test
    public void testExpressionIncorrectModError2() {
        String result = calculatorModel.calculateExpression("1,2md4", "");
        assertEquals("Error: Incorrect expression", result);
    }

    @Test
    public void testExpressionTwoNumberError() {
        String result = calculatorModel.calculateExpression("1,2x", "12");
        assertEquals("Error: Two numbers in a row", result);
    }

    @Test
    public void testExpressionClearField() {
        String result = calculatorModel.calculateExpression("", "");
        assertEquals("Error: Malformed expression", result);
    }

    @Test
    public void testExpressionGraphMinXMoreThanMaxX() {
        double[] borders = new double[] {100, 50, 50, 100};
        Exception exception = assertThrows(Exception.class, () -> calculatorModel.createGraph("x", borders));
        assertEquals("minX cannot exceed the value in the field maxX", exception.getMessage());
    }

    @Test
    public void testExpressionGraphMinYMoreThanMaxY() {
        double[] borders = new double[] {50, 100, 100, 50};
        Exception exception = assertThrows(Exception.class, () -> calculatorModel.createGraph("x", borders));
        assertEquals("minY cannot exceed the value in the field maxY", exception.getMessage());
    }

    @Test
    public void testExpressionGraphRangeIsLarge() {
        double[] borders = new double[] {50, 100, 10000000, 50};
        Exception exception = assertThrows(Exception.class, () -> calculatorModel.createGraph("x", borders));
        assertEquals("Value too high", exception.getMessage());
    }
}
