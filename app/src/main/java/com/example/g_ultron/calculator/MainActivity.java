package com.example.g_ultron.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity{

    // Attributes
    ArrayList<Button> buttons;
    TextView oper;
    TextView res;


    // OnClick Listener
    View.OnClickListener c_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button btn = (Button) findViewById(view.getId());

            switch (view.getId()){
                case R.id.btn_dell:
                    String txt = (String)oper.getText();
                    if(txt.length()!=0)
                        txt = txt.substring(0,txt.length()-1);
                    oper.setText(txt);
                    break;

                case R.id.btn_clr:
                    oper.setText("");
                    break;

                case R.id.btn_equal:
                    res.setText(operate(oper.getText()+"" )); //operate("+5-2*6")+"");
                    break;
                default:
                    oper.setText(oper.getText() +""+ btn.getText());
                    break;
            }

        }
    };


    // Initialise UI_elements
    private void init() {
        /*
        * Buttons init
        */
        buttons = new ArrayList<>();

        //numbers
        buttons.add((Button)findViewById(R.id.btn_00));
        for (int i = 0; i <= 9; i++) {
            buttons.add((Button)findViewById(getResources().getIdentifier("btn_"+i+"", "id", getPackageName()) ) );
        }
        //operations
        buttons.add((Button)findViewById(R.id.oper_1));
        buttons.add((Button)findViewById(R.id.oper_2));
        buttons.add((Button)findViewById(R.id.oper_3));
        buttons.add((Button)findViewById(R.id.oper_4));
        buttons.add((Button)findViewById(R.id.oper_5));
        buttons.add((Button)findViewById(R.id.btn_equal));
        //other
        buttons.add((Button)findViewById(R.id.btn_dot));
        buttons.add((Button)findViewById(R.id.btn_dell));
        buttons.add((Button)findViewById(R.id.btn_clr));
        //listener
        for (Button btn: buttons) {
            btn.setOnClickListener(c_listener);
        }

        /*
        * Texts
        */
        oper = (TextView) findViewById(R.id.txt_oper);
        res = (TextView) findViewById(R.id.txt_res);

    }

    // Operate
    private String operate(String word) {

        Log.d("AAA", word);

        // verify */
        Pattern  e= Pattern.compile("[*/]");
        Matcher m = e.matcher(word);
        if(m.find()) {
            String operation = m.group();
            Log.d("AAA", "found : "+operation );
            Log.d("AAA", "info > length: "+word.length()+", fs: "+m.start()+", fe: "+m.end()+" op: "+operation );
            Log.d("AAA", "display : "+word.substring(0, m.start())+">-----------<"+ word.substring(m.end(), word.length()));
            return compute(
                        operate(word.substring(0, m.start())),
                        operate( word.substring(m.end(), word.length())) ,
                        operation
            )+"";

        }
        else
            return word;

        //String nb = "(^[-+]?\\d|\\d)";// good
        //String operat = "[-+*/]";

        //while (m.find()){
         //   Log.d("AAA", m.group());


        //}
        //if(!m.matches())
         //   Log.d("AAA", "nop");

    }

    //Compute
    public double compute(String a, String b, String opration){
        double r=0;
        switch (opration){
            case "*":
                r= Double.valueOf(a) * Double.valueOf(b);
                break;

            case "/":
                r= Double.valueOf(a) / Double.valueOf(b);
                break;

            case "+":
                r= Double.valueOf(a) + Double.valueOf(b);
                break;

            case "-":
                r= Double.valueOf(a) - Double.valueOf(b);
                break;
        }
        return r;
    }


    String text = "+5-2*6";
    //+5-2 <==> * <==> 6
    //+5 <==> - <==> 2 <==> * <==> 6
    //+5 <==> - <==> 2 <==> * <==> 6

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the activity
        setContentView(R.layout.activity_main);

        //initialise component
        init();


    }

}
