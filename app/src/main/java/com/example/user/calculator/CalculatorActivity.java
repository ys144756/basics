package com.example.user.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class CalculatorActivity extends AppCompatActivity {

    private enum Operator {
        PLUS("+"),
        SUBTRACT("-"),
        MULTIPLE("*"),
        DIVIDE("/"),
        INIT("C"),
        EQUAL("=");

        private String operator;
        Operator(String operator) {
            this.operator = operator;
        }

        public static Operator findByOperator(String operator) {
            Operator[] operatorArray = Operator.values();
            for (int i=0; i<operatorArray.length; i++) {
                if (operatorArray[i].operator.equals(operator)) {
                    return operatorArray[i];
                }
            }

            return null;
        }
    }

    private View rootView;
    private Button[] btnNumbers = new Button[10];
    private Button[] btnOperators = new Button[Operator.values().length];

    private TextView txtvPreview;
    private TextView txtvResult;

    private String currentNumber = "0";

    private List<String> queue = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        rootView = findViewById(R.id.root_view);
        txtvPreview = findViewById(R.id.txt_calc);
        txtvResult = findViewById(R.id.txt_result);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Operator operator = Operator.findByOperator(v.getTag().toString());

                //사칙연산자 버튼이 눌렸을 경우
                if (operator != null &&
                   (operator == Operator.DIVIDE ||
                    operator == Operator.PLUS ||
                    operator == Operator.SUBTRACT ||
                    operator == Operator.MULTIPLE)) {

                    if (!currentNumber.equals("0")) {
                        queue.add(currentNumber);
                        queue.add(operator.operator);
                        currentNumber = "0";
                    } else {
                        queue.remove(queue.size() - 1);
                        queue.add(operator.operator);
                    }

                //등호(계산) 버튼이 눌렸을 경우
                } else if (operator == Operator.EQUAL) {
                    queue.add(currentNumber);
                    currentNumber = getAndCalculate();
                    queue.clear();
                //초기화(C) 버튼이 눌렸을 경우
                } else if (operator == Operator.INIT) {
                    currentNumber = "0";
                    queue.clear();
                //연산자가 아니라 숫자 버튼이 눌렸을 경우
                } else if (operator == null) {
                    int number = Integer.parseInt(v.getTag().toString());
                    if (currentNumber.equals("0")) {
                        currentNumber = String.valueOf(number);
                    } else {
                        currentNumber += number;
                    }
                }

                //버튼이 눌렸으니 결과를 반영하여 텍스트뷰를 다시 그려준다
                txtvPreview.setText(getPreviewString());
                txtvResult.setText(currentNumber);
            }
        };

        //숫자 버튼 찾기
        for (int i=0; i<10; i++) {
            btnNumbers[i] = rootView.findViewWithTag(String.valueOf(i));
            btnNumbers[i].setOnClickListener(onClickListener);
        }

        //연산자 및 기타 버튼 찾기
        Operator[] operators = Operator.values();
        for (int i=0; i<operators.length; i++) {
            btnOperators[i] = rootView.findViewWithTag(operators[i].operator);
            btnOperators[i].setOnClickListener(onClickListener);
        }

    }

    private String getAndCalculate() {
        try {
            int length = queue.size();
            for (int i=0; i<length; i++) {
                String result = calculate(Float.parseFloat(queue.get(0)), Float.parseFloat(queue.get(2)), Operator.findByOperator(queue.get(1)));
                queue.remove(0);
                queue.remove(0);
                queue.remove(0);
                queue.add(0, result);
            }
        } catch (Exception e) {
            if (queue.size() > 0) {
                return queue.get(0);
            } else {
                return "0";
            }
        }

        return "0";
    }

    private String getPreviewString() {
        return queue.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",", "");
    }

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

        if (result % 1 == 0) {
            return String.valueOf((int) result);
        } else {
            return String.valueOf(result);
        }
    }

}
