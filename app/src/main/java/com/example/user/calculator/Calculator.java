package com.example.user.calculator;

import java.util.LinkedList;

/**
 * Created by YS on 2018-11-09.
 */

public class Calculator {

    private LinkedList<String> expressionList = new LinkedList<>();

    public void add(String numberOrOperator) {
        expressionList.add(numberOrOperator);
    }

    public void replaceLastOperator(Operator operator) {
        expressionList.remove(expressionList.size() - 1);
        expressionList.add(operator.getOperator());
    }

    public void clearAll() {
        expressionList.clear();
    }

    /**
     * 수식 리스트에 있는 값들을 String 값으로 계산하여 반환한다.
     * @return : 수식 String
     */
    public String getPreviewString() {
        return expressionList.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",", "");
    }

    /**
     * 숫자 두개와 연산자 하나를 계산한다.
     * @param number1 : 숫자1
     * @param number2 : 숫자2
     * @param operator : 연산자
     * @return : 연산한 결과값
     */
    private String calculate(float number1, float number2, Operator operator) {
        float result = 0;

        switch (operator) {
            case PLUS:
                result = number1 + number2;
                break;
            case SUBTRACT:
                result = number1 - number2;
                break;
            case MULTIPLE:
                result = number1 * number2;
                break;
            case DIVIDE:
                result = number1 / number2;
                break;
        }

        //1로 나눈 나머지가 0이면 딱 떨어지는 정수형이다.
        //정수형은 1.0, 2.0 처럼 소숫점을 표기할 필요가 없으므로 실수형을 정수형으로 형변환 해준다.
        if (result % 1 == 0) {
            return String.valueOf((int) result);
        } else {
            return String.valueOf(result);
        }
    }

    /**
     * 수식 리스트에 있는 숫자와 연산자들을 모두 계산한다.
     * @return : 계산 결과값
     */
    public String calculateAll() {
        try {
            int length = expressionList.size();
            for (int i=0; i<length; i++) {
                //숫자 2개와 연산자 1개를 앞에서 부터 한 묶음씩 계산해준다.
                String result = calculate(Float.parseFloat(expressionList.get(0)), Float.parseFloat(expressionList.get(2)), Operator.findByOperator(expressionList.get(1)));
                //1 + 2 + 3의 경우, 1+2는 이미 계산을 했으므로 3칸을 비워준다.
                expressionList.removeFirst();
                expressionList.removeFirst();
                expressionList.removeFirst();

                //1+2 계산한 결과는 앞에 붙여준다. 결과적으로 3 + 3만 남는다.
                expressionList.addFirst(result);
            }

            //1+ , 1 처럼 리스트에 계산할 수 없는 덩어리만 남은경우에 대한 처리
            //인덱스를 0,1,2 세개 사용하므로 1+, 1과 같이 모자란 경우는 IndexOutOfBoundsException 이 발생한다.
        } catch (IndexOutOfBoundsException e) {
            if (expressionList.size() > 0) {
                return expressionList.get(0);
            } else {
                return "0";
            }
        }

        return "0";
    }
}
