package ru.geekbrains.calculator;

import java.util.ArrayList;
import java.util.Map;

// TODO: технический долг, который планирую закрыть в следующие 1-2 дня.
// 1. Не все проверки описал - возможен рефакторинг.
// 2. Не реализовал сценарий нажатия на кнопку изменения знака (+/-).
// 3. Еще не думал над тем, каким образом буду хранить введенные данные.
// 4. Не реализован форматированный вывод значений операндов и результата.
// 5. Не создал макет для горизонтальной ориентации экрана.

public class Calculator {
    private Map<Integer, String> valuesById;
    private ArrayList<Integer> operandsId;
    private ArrayList<Integer> operatorsId;

    private double firsOperand;
    StringBuilder firsOperandBuilder;

    private double secondOperand;
    StringBuilder secondOperandBuilder;

    private int operatorId;
    private double result;

    StringBuilder expressionBuilder;

    public Calculator(Map<Integer, String> valuesById,
                      ArrayList<Integer> operandsId,
                      ArrayList<Integer> operatorsId)  {

        this.valuesById = valuesById;
        this.operandsId = operandsId;
        this.operatorsId = operatorsId;

        this.firsOperand = Double.MIN_VALUE;
        this.firsOperandBuilder = new StringBuilder();

        this.secondOperand = Double.MIN_VALUE;
        this.secondOperandBuilder = new StringBuilder();

        this.result = Double.MIN_VALUE;
        this.operatorId = Integer.MIN_VALUE;

        this.expressionBuilder = new StringBuilder();
    }

    public void calculate(int id) {
        if (id == R.id.operator_drop) {
            dropExpression();
            return;
        }

        setFirsOperand(id);
        setOperator(id);
        setSecondOperand(id);
        setResult(id);
    }

    public String getExpression() {
        setExpressionBuilder();
        return expressionBuilder.toString();
    }

    private void dropExpression() {
        firsOperand = Double.MIN_VALUE;
        firsOperandBuilder.setLength(0);

        secondOperand = Double.MIN_VALUE;
        secondOperandBuilder.setLength(0);

        operatorId = Integer.MIN_VALUE;
        result = Double.MIN_VALUE;

        expressionBuilder.setLength(0);
    }

    private void setFirsOperand(int id) {
        if (operandsId.indexOf(id) == -1) {
            return;
        }

        if (operatorId > Integer.MIN_VALUE) {
            return;
        }

        if (id == R.id.number_0 && firsOperand == 0) {
            return;
        }

        String operand = valuesById.get(id);

        if (id == R.id.decimal_separator && firsOperandBuilder.indexOf(operand) != -1) {
            return;
        }

        if (id == R.id.decimal_separator && firsOperand == Double.MIN_VALUE) {
            firsOperandBuilder.append(valuesById.get(R.id.number_0));
        }

        firsOperandBuilder.append(operand);
        firsOperand = Double.parseDouble(firsOperandBuilder.toString());
    }

    private void setOperator(int id) {
        if (operatorsId.indexOf(id) == -1) {
            return;
        }

        if (firsOperand == Double.MIN_VALUE) {
            return;
        }

        if (operatorId > Integer.MIN_VALUE) {
            return;
        }

        operatorId = id;
    }

    private void setSecondOperand(int id) {
        if (operandsId.indexOf(id) == -1) {
            return;
        }

        if (operatorId == Integer.MIN_VALUE) {
            return;
        }

        if (result > Double.MIN_VALUE) {
            return;
        }

        if (id == R.id.number_0 && secondOperand == 0) {
            return;
        }

        String operand = valuesById.get(id);

        if (id == R.id.decimal_separator && secondOperandBuilder.indexOf(operand) != -1) {
            return;
        }

        if (id == R.id.decimal_separator && secondOperand == Double.MIN_VALUE) {
            secondOperandBuilder.append(valuesById.get(R.id.number_0));
        }

        secondOperandBuilder.append(operand);
        secondOperand = Double.parseDouble(secondOperandBuilder.toString());
    }

    private void setResult(int id) {
        if (id != R.id.operator_equal) {
            return;
        }

        if (firsOperand == Double.MIN_VALUE
                || secondOperand == Double.MIN_VALUE
                || operatorId == Integer.MIN_VALUE) {
            return;
        }

        if (operatorId == R.id.operator_divide && secondOperand != 0) {
            result = firsOperand / secondOperand;

        } else if (operatorId == R.id.operator_multiply) {
            result = firsOperand * secondOperand;

        } else if (operatorId == R.id.operator_minus) {
            result = firsOperand - secondOperand;

        } else if (operatorId == R.id.operator_plus) {
            result = firsOperand + secondOperand;

        } else if (operatorId == R.id.operator_percent) {
            result = secondOperand * 100 / firsOperand;
        }
    }

    private void setExpressionBuilder() {
        expressionBuilder.setLength(0);
        expressionBuilder.append(firsOperandBuilder.toString());

        if (operatorId > Integer.MIN_VALUE) {
            expressionBuilder.append(System.lineSeparator());
            expressionBuilder.append(valuesById.get(operatorId));
        }

        if (secondOperandBuilder.length() > 0) {
            expressionBuilder.append(System.lineSeparator());
            expressionBuilder.append(secondOperandBuilder.toString());
        }

        if (result > Double.MIN_VALUE) {
            expressionBuilder.append(System.lineSeparator());
            expressionBuilder.append(valuesById.get(R.id.operator_equal));
            expressionBuilder.append(System.lineSeparator());
            expressionBuilder.append(String.valueOf(result));
        }
    }

    private void shiftData() {
        if (result == Double.MIN_VALUE) {
            return;
        }

        firsOperand = result;
        firsOperandBuilder.setLength(0);
        firsOperandBuilder.append(String.valueOf(result));

        secondOperand = Double.MIN_VALUE;
        secondOperandBuilder.setLength(0);

        operatorId = Integer.MIN_VALUE;
        result = Double.MIN_VALUE;

        expressionBuilder.setLength(0);
    }
}
