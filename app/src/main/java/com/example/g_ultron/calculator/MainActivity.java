package com.example.g_ultron.calculator;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity{


    // Used to load the 'native-lib' library on application startup.
    /*static {
        System.loadLibrary("native-lib");
    }
    public native String stringFromJNI();
    public static native double math(double radian, String func);*/

    /**
    * Attributes
    * */

    ArrayList<Button> buttons;
    ArrayList<String> history;
    TextView oper;
    TextView res;


    /**
    * Listener
    * */

    // OnClick Listener
    View.OnClickListener c_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button btn = (Button) findViewById(view.getId());
            String txt;



            MediaPlayer mp;

            if(view.getId()!=R.id.btn_equal)
                mp = MediaPlayer.create(getApplicationContext(), R.raw.twing);
            else
                mp = MediaPlayer.create(getApplicationContext(), R.raw.cash);

            mp.start();

            switch (view.getId()){
                case R.id.btn_spec_oper_1:
                case R.id.btn_spec_oper_2:
                case R.id.btn_spec_oper_3:
                case R.id.btn_spec_oper_8:
                    txt = oper.getText() +""+ btn.getText()+"(";
                    oper.setText(txt);
                    break;

                case R.id.btn_dell:
                    txt = oper.getText().toString();
                    if(txt.length()!=0)
                        txt = txt.substring(0,txt.length()-1);
                    oper.setText(txt);
                    break;
                case R.id.btn_clr:
                    oper.setText("");
                    break;
                case R.id.btn_equal:
                    Log.d("AAA", oper.getText().toString());
                    try {
                        txt = oper.getText().toString();
                        res.setText(String.valueOf(eval(txt )));
                    }catch (Exception e){
                        txt = "Error";
                        res.setText(txt);
                    }
                    break;
                default:
                    txt = oper.getText() +""+ btn.getText();
                    oper.setText(txt);
                    break;
            }

        }
    };

    /**
    * Methodes
    * */

    // Initialise UI_elements
    private void init() {
        /*
        * Buttons init
        */
        buttons = new ArrayList<>();

        //numbers
        for (int i = 0; i <= 9; i++) {
            buttons.add((Button)findViewById(getResources().getIdentifier("btn_"+i+"", "id", getPackageName()) ) );
        }
        //operations
        for (int i = 1; i <= 5; i++) {
            buttons.add((Button)findViewById(getResources().getIdentifier("oper_"+i, "id", getPackageName()) ) );
        }
        if (getResources().getConfiguration().orientation==2){
            for (int i = 1; i <= 8; i++) {
                buttons.add((Button)findViewById(getResources().getIdentifier("btn_spec_oper_"+i, "id", getPackageName()) ) );
            }
        }
        //other
        buttons.add((Button)findViewById(R.id.btn_equal));
        buttons.add((Button)findViewById(R.id.btn_bra_op));
        buttons.add((Button)findViewById(R.id.btn_bra_cl));
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
    //clean String
    private static String clean_fact(String oper) {
        String spec_oper = "([(].+[)][!])|\\d+[!]";
        Pattern p = Pattern.compile(spec_oper);
        Matcher m = p.matcher(oper);
        while(m.find()){
            Log.d("AAA","find ");
            String txt = m.group();
            Log.d("AAA","find "+txt);
            Log.d("AAA","find/sub "+txt.substring(0, m.group().length()-1));
            oper = oper.replace(txt, "fact"+txt.substring(0, m.group().length()-1));
        }
        return oper;
    }
    private static String clean_log(String oper) {
        String spec_oper = "(log\\(.+\\))|log\\d+\\(.+\\)";
        Pattern p = Pattern.compile(spec_oper);
        Matcher m = p.matcher(oper);

        String x;
        String base;
        while(m.find()){
            Log.d("AAA","find log expression ");
            String txt = m.group();
            Log.d("AAA","the expression "+txt);


            // find the expected X
            String exp_x = "\\(.+\\)";
            Pattern p_x = Pattern.compile(exp_x);
            Matcher m_x = p_x.matcher(txt);
            if(m_x.find()){
                String txt_x = m_x.group();
                x = txt_x.substring(1, txt_x.length()-1);
                Log.d("AAA","expected X  "+x);


                // find the expected base
                String exp_base = "log\\d+\\(";
                Pattern p_base = Pattern.compile(exp_base);
                Matcher m_base = p_base.matcher(txt);
                if(m_base.find()){
                    String txt_base = m_base.group();
                    base = txt_base.substring(3, txt_base.length()-1);
                    Log.d("AAA","expected BAse  "+base);

                    oper = oper.replace(txt, "(log("+x+")/log("+base+")");
                    Log.d("AAA","final replace  "+oper+")");
                }


            }





        }
        return oper;
    }

    // Factoriel Iteratif
    private static int fact(int x) {


        int f=x;
        if(x ==1 || x ==0 )
            f=1;
        else
            while (x!=1){
                x=x-1;
                f = f*x;
                if(f<0){
                    Log.d("AAA",x+"! = "+f);
                    f=Integer.valueOf("EROOR");
                    break;
                }
                Log.d("AAA",x+"! = "+f);
            }
        return f;
    }
    /* Expression eval from https://stackoverflow.com/questions/3422673/evaluating-a-math-expression-given-in-string-form
    *And modified
    */
    public static double eval(String oper) {
        oper = oper.replaceAll("X", "*");
        oper = oper.replaceAll("√", "sqrt");
        oper = clean_fact(oper);
        oper = clean_log(oper);


        final String str = oper;
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();

                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else if (func.equals("fact")) x = fact((int)eval(x+""));
                    else if (func.equals("log")) x = Math.log10(eval(x+""));
                    else if (func.equals("ln")) x = Math.log(eval(x+""));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                if (x == Math.cos(Math.toRadians(90)) )// 6.123233995736766e-17 smallest value near to ZERO in computer
                    return 0;
                else
                    return x;
            }
        }.parse();
    }



    /**
     *Life cycle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the activity
        setContentView(R.layout.activity_main);

        //initialise component
        init();

        //Log.d("AAA", fact(99)+"");
    }
}
