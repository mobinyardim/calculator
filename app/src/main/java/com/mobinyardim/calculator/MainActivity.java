package com.mobinyardim.calculator;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity  {

    int p_n;
    EditText et;
    Button btn_c;
    ExpressionCalculate calc;
    ImageView backSpace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //initial
        p_n=0;
        et=findViewById(R.id.editText);
        btn_c=findViewById(R.id.clear);
        backSpace = findViewById(R.id.imageView);
        calc = new ExpressionCalculate();

        //disable keyboard on edit text
        disableSoftInputFromAppearing(et);

        //listeners
        backSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et.getText().toString().isEmpty()) {
                    int cursor = et.getSelectionEnd()-1;
                    StringBuilder s = new StringBuilder(et.getText().toString());
                    if(s.charAt(cursor)=='(')p_n--;
                    else if(s.charAt(cursor)==')')p_n++;
                    s.deleteCharAt(cursor);
                    et.setText(s);
                    et.setSelection(cursor);
                }
            }
        });
    }

    //disable keyboard from edit text
    public static void disableSoftInputFromAppearing(EditText editText) {
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextIsSelectable(true);
        } else {
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(true);
        }
    }

    public boolean isOperator(char ch) {
        String s="+ - ÷ ×";
        return s.indexOf(ch)>-1;
    }

    public void numericButtonClick(View v)
    {
        Button bt = (Button) v;
        et.getText().insert(et.getSelectionStart(),bt.getText().toString());
    }
    public void opButtonClick(View v)
    {
        Button bt = (Button) v;
        StringBuilder s = new StringBuilder(et.getText().toString());
        String str =et.getText().toString();
        switch (bt.getId())
        {
            case R.id.clear:

                et.setText("");
                p_n=0;
                break;

            case R.id.equal:

                str=str.replace("÷","/").replace("×","*");
                double a =calc.calcInfix(str);
                //to delete trailing zero
                DecimalFormat df = new DecimalFormat("###.####");
                et.setText(String.valueOf(df.format(a)));
                et.setSelection(et.length());
                break;

            case R.id.percent:

                int cursor = et.getSelectionEnd()-1;
                    if(cursor==-1 || isOperator(s.charAt(cursor)) || s.charAt(cursor)==')' || s.charAt(cursor)=='(' || s.charAt(cursor)=='%'){
                        Toast.makeText(MainActivity.this , "it not valid" , Toast.LENGTH_SHORT).show();
                    }
                    else {
                        et.getText().insert(et.getSelectionStart(), bt.getText().toString());
                        et.setSelection(cursor + 2);
                    }
                break;

            default:
                cursor=et.getSelectionEnd()-1;
                if(cursor >=0 && isOperator(s.charAt(cursor)))
                {
                    s.setCharAt(cursor , bt.getText().toString().charAt(0));
                    et.setText(s);
                    et.setSelection(s.length());
                }
                else if(cursor < et.getText().toString().length()-1 && isOperator(s.charAt(cursor+1)))
                {
                    s.setCharAt(cursor-1, bt.getText().toString().charAt(0));
                    et.setText(s);
                    et.setSelection(s.length());
                }
                else if(cursor>-1 && s.charAt(cursor)!='(')
                {
                s.insert(cursor+1 ,bt.getText().toString());
                et.setText(s);
                et.setSelection(cursor+2);
                }
                break;
     }
    }
    public void coupleButtonClick(View v)
    {
        Button bt = (Button) v;
        int cursor = et.getSelectionEnd()-1;
        StringBuilder s = new StringBuilder(et.getText().toString());
        switch (v.getId())
        {
            case R.id.parantes:

                if(p_n ==0 && cursor > 0)
                {
                    p_n++;
                    if(isOperator(s.charAt(cursor)))
                    {
                        s.insert(cursor+1 , '(');
                        et.setText(s);
                        et.setSelection(cursor+2);
                    }
                    else
                    {
                        s.insert(cursor+1 , '×');
                        s.insert(cursor+2, '(');
                        et.setText(s);
                        et.setSelection(cursor+3);
                    }
                }
                else
                {
                    if(cursor < 0 || isOperator(s.charAt(cursor)) || s.charAt(cursor)=='(')
                    {
                        p_n++;
                        s.insert(cursor+1, '(');
                        et.setText(s);
                        et.setSelection(cursor+2);
                    }
                    else if (p_n>0)
                    {
                        p_n--;
                        s.insert(cursor+1, ')');
                        et.setText(s);
                        et.setSelection(cursor+2);
                    }
                    else
                    {
                        s.insert(cursor+1 , '×');
                        s.insert(cursor+2, '(');
                        et.setText(s);
                        et.setSelection(cursor+3);
                    }
                }
                break;

            case R.id.negpos:

                break;
        }
    }
}
