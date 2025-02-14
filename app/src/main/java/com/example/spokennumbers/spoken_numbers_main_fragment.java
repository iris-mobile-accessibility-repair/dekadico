package com.example.spokennumbers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class spoken_numbers_main_fragment extends Fragment {

    private ImageButton startButton;
    private EditText timeDelayInput;
    private EditText timeIncInput;
    private RadioButton femaleVoiceButton;
    private RadioButton maleVoiceButton;
    private RadioButton syntheticButton;
    private RadioButton decimalButton;
    private RadioButton binaryButton;
    private String defaultTimeDelay;
    private String defaultTimeInc;
    public static final String TAG = "MAIN";

    public spoken_numbers_main_fragment() {
        // Required empty public constructor
    }
    public static spoken_numbers_main_fragment newInstance() {
        spoken_numbers_main_fragment fragment = new spoken_numbers_main_fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.spoken_numbers_main_fragment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        startButton = Objects.requireNonNull(getView()).findViewById(R.id.start_button);
        timeDelayInput = getView().findViewById(R.id.time_delay_input);
        timeIncInput = getView().findViewById(R.id.time_inc_num);

        defaultTimeDelay = MainActivity.prefConfig.loadDataDelayTime();
        defaultTimeInc = MainActivity.prefConfig.loadDataIncTime();
        timeDelayInput.setText(defaultTimeDelay);
        timeIncInput.setText(defaultTimeInc);

        femaleVoiceButton = getView().findViewById(R.id.radio_button_female);
        maleVoiceButton = getView().findViewById(R.id.radio_button_male);
        syntheticButton = getView().findViewById(R.id.radio_button_synthetic_male);
        syntheticButton.setEnabled(false);

        femaleVoiceButton.setChecked(MainActivity.prefConfig.loadDataFemaleChecked());
        maleVoiceButton.setChecked(!MainActivity.prefConfig.loadDataFemaleChecked());

        decimalButton = getView().findViewById(R.id.decimal_radio);
        binaryButton = getView().findViewById(R.id.binary_radio);
        decimalButton.setChecked(MainActivity.prefConfig.loadDataDecimalChecked());
        binaryButton.setChecked(!MainActivity.prefConfig.loadDataDecimalChecked());

        Switch evalSwitch = Objects.requireNonNull(getView()).findViewById(R.id.eval_mode_switch);
        boolean evalState = MainActivity.prefConfig.loadEvalModeChecked();
        evalSwitch.setChecked(evalState);

        Switch nightSwitch = Objects.requireNonNull(getView()).findViewById(R.id.night_mode_switch);
        boolean isNightModeOn = MainActivity.prefConfig.loadNightModeChecked();
        nightSwitch.setChecked(isNightModeOn);
        if(isNightModeOn)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        nightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                if(isChecked){
//                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                }
//                else {
//                    MainActivity.this.recreate();
//                }
                MainActivity.prefConfig.saveNightModeChecked(isChecked);
                Toast toast = Toast.makeText(getContext(), "Restart app to apply theme changes.", Toast.LENGTH_SHORT);
                toast.setMargin(50, 50);
                toast.show();
            }
        });


        TextView highScoreText = getView().findViewById(R.id.high_score_view);
        int highScoreInt = MainActivity.prefConfig.loadHighScore();
        String highScoreStr = "High Score:  " + highScoreInt;
        highScoreText.setText(highScoreStr);

        startButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String timeDelayStr = timeDelayInput.getText().toString();
                String timeIncStr = timeIncInput.getText().toString();

                float timeDelayNum;
                float timeIncNum;

                if(!timeDelayStr.matches("\\d+(?:\\.\\d+)?")){ //not a number
                    timeDelayNum = Float.parseFloat(defaultTimeDelay);
                }
                else{
                    timeDelayNum = Float.parseFloat(timeDelayStr);
                    if(timeDelayNum <= 0.1)
                        timeDelayNum = Float.parseFloat(defaultTimeDelay);
                }

                if(!timeIncStr.matches("\\d+(?:\\.\\d+)?")){ //not a number
                    timeIncNum = Float.parseFloat(defaultTimeInc);
                }
                else{
                    timeIncNum = Float.parseFloat(timeIncStr);
                    if(timeIncNum <= 0.1)
                        timeIncNum = Float.parseFloat(defaultTimeInc);
                }
                boolean isFemaleVoice = (MainActivity.prefConfig.loadDataFemaleChecked());
                if(femaleVoiceButton.isChecked()){
                    isFemaleVoice = true;
                }
                else if(maleVoiceButton.isChecked()){
                    isFemaleVoice = false;
                }

                boolean isDecimal = (MainActivity.prefConfig.loadDataDecimalChecked());
                if(decimalButton.isChecked()){
                    isDecimal = true;
                }
                else if(binaryButton.isChecked()){
                    isDecimal = false;
                }

                boolean evaluationMode = (MainActivity.prefConfig.loadEvalModeChecked());

                if(decimalButton.isChecked()){
                    isDecimal = true;
                }
                else if(binaryButton.isChecked()){
                    isDecimal = false;
                }

                MainActivity.evaluationMode = evalSwitch.isChecked();

                MainActivity.prefConfig.saveData(Float.toString(timeDelayNum),
                        Float.toString(timeIncNum), isFemaleVoice, isDecimal, MainActivity.evaluationMode, false);
                ((MainActivity) Objects.requireNonNull(getActivity())).switchToInGameFragment(timeDelayNum, timeIncNum, isFemaleVoice, isDecimal);
            }
        });
    }
}
