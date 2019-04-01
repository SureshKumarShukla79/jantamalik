package in.filternet.jantamalik;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class Puzzle extends Activity {

    private String TAG = "Puzzle";

    public final static String bQUE_ = "Que_";

    TextView ui_question;
    ImageView ui_correct_answer, ui_wrong_answer;
    Button ui_next, ui_skip;
    RadioGroup ui_option_group;
    RadioButton ui_option_1, ui_option_2, ui_option_3, ui_option_4;

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;
    private String mLanguage;

    private int question_num = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPref.edit();

        mLanguage = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, null); // first launch
        if (mLanguage == null){
            mEditor.putString(MainActivity.sUSER_CURRENT_LANGUAGE, MainActivity.sLANGUAGE_HINDI).commit();
            mLanguage = MainActivity.sLANGUAGE_HINDI; // default - Hindi 58 crore. English 2 crore.
        }

        if(mLanguage.equals(MainActivity.sLANGUAGE_HINDI)) {
            MainActivity.setUI_Lang(this, "hi");
        }

        setContentView(R.layout.puzzle);

        ui_question = findViewById(R.id.question);
        ui_option_group = findViewById(R.id.options);
        ui_option_1 = findViewById(R.id.radio_1);
        ui_option_2 = findViewById(R.id.radio_2);
        ui_option_3 = findViewById(R.id.radio_3);
        ui_option_4 = findViewById(R.id.radio_4);
        ui_correct_answer = findViewById(R.id.correct);
        ui_wrong_answer = findViewById(R.id.wrong);
        ui_next = findViewById(R.id.next);
        ui_skip = findViewById(R.id.skip);

        update_question();
    }

    private void update_question() {

        reset_radio_buttons();

        question_num = ask_question();
        if(question_num == -1) {
            open_main_activity();
            return;
        }

        if(mLanguage.equals(MainActivity.sLANGUAGE_HINDI)) {
            ui_question.setText(Puzzle_Ques.questions[question_num][5]);
        } else {
            ui_question.setText(Puzzle_Ques.questions[question_num][0]);
        }

        //Create a list of radio buttons. Then, choose a radio button, randomly and place right/wrong options on it
        ArrayList<RadioButton> option = new ArrayList<>();
        option.add(ui_option_1);
        option.add(ui_option_2);
        option.add(ui_option_3);
        option.add(ui_option_4);

        //Choose random radio button and place right/wrong options on it
        int index = get_index(option.size());
        if(mLanguage.equals(MainActivity.sLANGUAGE_HINDI)) {
            option.get(index).setText(Puzzle_Ques.questions[question_num][6]);  //Place right answer on that index
        } else {
            option.get(index).setText(Puzzle_Ques.questions[question_num][1]);  //Place right answer on that index
        }
        option.remove(index);                                               //Remove the element of that index
        /** Removing the full option, ensures that even if random number repeats, it will go to another slot. **/

        index = get_index(option.size());
        if(mLanguage.equals(MainActivity.sLANGUAGE_HINDI)) {
            option.get(index).setText(Puzzle_Ques.questions[question_num][7]);
        } else {
            option.get(index).setText(Puzzle_Ques.questions[question_num][2]);
        }
        option.remove(index);

        index = get_index(option.size());
        if(mLanguage.equals(MainActivity.sLANGUAGE_HINDI)) {
            option.get(index).setText(Puzzle_Ques.questions[question_num][8]);
        } else {
            option.get(index).setText(Puzzle_Ques.questions[question_num][3]);
        }
        option.remove(index);

        index = get_index(option.size());
        if(mLanguage.equals(MainActivity.sLANGUAGE_HINDI)) {
            option.get(index).setText(Puzzle_Ques.questions[question_num][9]);
        } else {
            option.get(index).setText(Puzzle_Ques.questions[question_num][4]);
        }
        option.remove(index);
    }

    private int ask_question() {
        for(int i=question_num+1; i<Puzzle_Ques.questions.length; i++){
            boolean correct = mSharedPref.getBoolean(bQUE_+(i+1), false);
            if(!correct)
                return i;
        }

        return -1;
    }

    private int get_index(int max) {
        return new Random().nextInt(max);
    }

    private void open_main_activity() {
        boolean smart_voter = true;
        for(int i=0; i<Puzzle_Ques.questions.length; i++){
            boolean correct = mSharedPref.getBoolean(bQUE_+(i+1), false);
            if(!correct) {
                smart_voter = false;
                break;
            }
        }

        mEditor.putBoolean(MainActivity.bSMART_VOTER, smart_voter).commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onclick_skip(View view) {
        open_main_activity();
    }

    public void onclick_next(View view) {
        if(question_num == 9) {
            open_main_activity();
        } else {
            update_question();
        }
    }

    public void onclick_check_answer(View view) {
        String correct_answer;
        if(mLanguage.equals(MainActivity.sLANGUAGE_HINDI)) {
            correct_answer = Puzzle_Ques.questions[question_num][6];
        } else {
            correct_answer = Puzzle_Ques.questions[question_num][1];
        }

        boolean checked =  ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_1:
                if(checked) {
                    String option = ui_option_1.getText().toString();
                    if(option.equals(correct_answer)) {
                        mEditor.putBoolean(bQUE_+(question_num+1), true);
                        ui_correct_answer.setVisibility(View.VISIBLE);
                    }
                    else {
                        mEditor.putBoolean(bQUE_+(question_num+1), false);
                        ui_wrong_answer.setVisibility(View.VISIBLE);
                    }
                }
                break;

            case R.id.radio_2:
                if(checked) {
                    String option = ui_option_2.getText().toString();
                    if(option.equals(correct_answer)) {
                        mEditor.putBoolean(bQUE_+(question_num+1), true);
                        ui_correct_answer.setVisibility(View.VISIBLE);
                    }
                    else {
                        mEditor.putBoolean(bQUE_+(question_num+1), false);
                        ui_wrong_answer.setVisibility(View.VISIBLE);
                    }
                }
                break;

            case R.id.radio_3:
                if(checked) {
                    String option = ui_option_3.getText().toString();
                    if(option.equals(correct_answer)) {
                        mEditor.putBoolean(bQUE_+(question_num+1), true);
                        ui_correct_answer.setVisibility(View.VISIBLE);
                    }
                    else {
                        mEditor.putBoolean(bQUE_+(question_num+1), false);
                        ui_wrong_answer.setVisibility(View.VISIBLE);
                    }
                }
                break;

            case R.id.radio_4:
                if(checked) {
                    String option = ui_option_4.getText().toString();
                    if(option.equals(correct_answer)) {
                        mEditor.putBoolean(bQUE_+(question_num+1), true);
                        ui_correct_answer.setVisibility(View.VISIBLE);
                    }
                    else {
                        mEditor.putBoolean(bQUE_+(question_num+1), false);
                        ui_wrong_answer.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }

        mEditor.commit();

        enable_radio(false);
    }

    private void reset_radio_buttons() {
        ui_correct_answer.setVisibility(View.INVISIBLE);
        ui_wrong_answer.setVisibility(View.INVISIBLE);

        ui_option_group.clearCheck();
        enable_radio(true);
    }

    private void enable_radio(boolean value) {
        ui_option_1.setEnabled(value);
        ui_option_1.setTextColor(Color.BLACK);

        ui_option_2.setEnabled(value);
        ui_option_2.setTextColor(Color.BLACK);

        ui_option_3.setEnabled(value);
        ui_option_3.setTextColor(Color.BLACK);

        ui_option_4.setEnabled(value);
        ui_option_4.setTextColor(Color.BLACK);
    }
}
