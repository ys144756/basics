package com.example.user.calculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity {
    private View rootView;

    private TextView txtvPreview;
    private TextView txtvResult;

    private String currentNumber = "0";

    private Calculator calculator = new Calculator();

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

                //숫자 버튼이 눌렸을 경우
                if (operator == null) {
                    int number = Integer.parseInt(v.getTag().toString());

                    //0(초기화 상태)인 경우, 01 02 와 같이 0을 포함해서 붙는게 아니라 입력된 숫자로 대체해준다.
                    if (currentNumber.equals("0")) {
                        currentNumber = String.valueOf(number);

                    //초기화 상태가 아니라면 11 12와 같이 앞 숫자에 입력된 숫자를 붙여준다.
                    } else {
                        currentNumber += number;
                    }

                //계산 가능한 사칙연산자 버튼이 눌렸을 경우
                } else if (operator.isCalculable()) {

                    if (!currentNumber.equals("0")) {
                        calculator.add(currentNumber);
                        calculator.add(operator.getOperator());
                        currentNumber = "0";
                    } else {
                        calculator.replaceLastOperator(operator);
                    }

                //등호(계산) 버튼이 눌렸을 경우
                } else if (operator == Operator.EQUAL) {
                    //계산하기 전에 완성된 수식을 보여준다.
                    calculator.add(currentNumber);
                    txtvPreview.setText(calculator.getPreviewString() + "=");

                    currentNumber = calculator.calculateAll();
                    txtvResult.setText(currentNumber);

                    calculator.clearAll();

                    return;
                //초기화(C) 버튼이 눌렸을 경우
                } else if (operator == Operator.INIT) {
                    currentNumber = "0";
                    calculator.clearAll();
                }

                //버튼이 눌렸으니 결과를 반영하여 텍스트뷰를 다시 그려준다
                txtvPreview.setText(calculator.getPreviewString());
                txtvResult.setText(currentNumber);
            }
        };

        //숫자 버튼 찾기
        for (int i = 0; i < 10; i++) {
            rootView.findViewWithTag(String.valueOf(i)).setOnClickListener(onClickListener);
        }

        //연산자 및 기타 버튼 찾기
        Operator[] operators = Operator.values();
        for (int i = 0; i < operators.length; i++) {
            rootView.findViewWithTag(operators[i].getOperator()).setOnClickListener(onClickListener);
        }
    }

}
