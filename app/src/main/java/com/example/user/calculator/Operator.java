package com.example.user.calculator;

/**
 * Created by YS on 2018-11-09.
 */
public enum Operator {
    PLUS("+", true),
    SUBTRACT("-", true),
    MULTIPLE("*", true),
    DIVIDE("/", true),
    INIT("C", false),
    EQUAL("=", false);

    private String operator;
    private boolean calculable;
    Operator(String operator, boolean calculable) {
        this.operator = operator;
        this.calculable = calculable;
    }

    /**
     * 인자로 받은 String으로 맞는 연산자를 찾는다.
     * @param operator : 연산자 String
     * @return : 연산자 enum
     */
    public static Operator findByOperator(String operator) {
        Operator[] operatorArray = Operator.values();
        for (int i=0; i<operatorArray.length; i++) {
            if (operatorArray[i].operator.equals(operator)) {
                return operatorArray[i];
            }
        }

        return null;
    }

    public String getOperator() {
        return operator;
    }

    public boolean isCalculable() {
        return calculable;
    }
}
